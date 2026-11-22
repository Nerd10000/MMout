/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.PrimitiveTopology;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.BufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.DefaultVertexFormat;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.math.Axis;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalDouble;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.state.level.SkyRenderState;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.model.sprite.AtlasManager;
/*     */ import net.minecraft.data.AtlasIds;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeProbe;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.MoonPhase;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import org.joml.Matrix3f;
/*     */ import org.joml.Matrix3fc;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fStack;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ import org.joml.Vector4f;
/*     */ 
/*     */ public class SkyRenderer implements AutoCloseable {
/*  47 */   private static final Identifier SUN_SPRITE = Identifier.withDefaultNamespace("sun");
/*     */   
/*  48 */   private static final Identifier END_FLASH_SPRITE = Identifier.withDefaultNamespace("end_flash");
/*     */   
/*  49 */   private static final Identifier END_SKY_LOCATION = Identifier.withDefaultNamespace("textures/environment/end_sky.png");
/*     */   
/*     */   private static final float SKY_DISC_RADIUS = 512.0F;
/*     */   
/*     */   private static final int SKY_VERTICES = 10;
/*     */   
/*     */   private static final int STAR_COUNT = 1500;
/*     */   
/*     */   private static final float SUN_SIZE = 30.0F;
/*     */   
/*     */   private static final float SUN_HEIGHT = 100.0F;
/*     */   
/*     */   private static final float MOON_SIZE = 20.0F;
/*     */   
/*     */   private static final float MOON_HEIGHT = 100.0F;
/*     */   
/*     */   private static final int SUNRISE_STEPS = 16;
/*     */   
/*     */   private static final int END_SKY_QUAD_COUNT = 6;
/*     */   
/*     */   private static final float END_FLASH_HEIGHT = 100.0F;
/*     */   
/*     */   private static final float END_FLASH_SCALE = 60.0F;
/*     */   
/*     */   private final TextureAtlas celestialsAtlas;
/*     */   
/*     */   private final RenderTarget renderTarget;
/*     */   
/*     */   private final GpuBuffer starBuffer;
/*     */   
/*     */   private final GpuBuffer topSkyBuffer;
/*     */   
/*     */   private final GpuBuffer bottomSkyBuffer;
/*     */   
/*     */   private final GpuBuffer endSkyBuffer;
/*     */   
/*     */   private final GpuBuffer sunBuffer;
/*     */   
/*     */   private final GpuBuffer moonBuffer;
/*     */   
/*     */   private final GpuBuffer sunriseBuffer;
/*     */   
/*     */   private final GpuBuffer endFlashBuffer;
/*     */   
/*  75 */   private final RenderSystem.AutoStorageIndexBuffer quadIndices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
/*     */   
/*     */   private final AbstractTexture endSkyTexture;
/*     */   
/*     */   private int starIndexCount;
/*     */   
/*     */   public SkyRenderer(TextureManager textureManager, AtlasManager atlasManager, RenderTarget renderTarget) {
/*  82 */     this.celestialsAtlas = atlasManager.getAtlasOrThrow(AtlasIds.CELESTIALS);
/*  84 */     this.renderTarget = renderTarget;
/*  86 */     this.starBuffer = buildStars();
/*  87 */     this.endSkyBuffer = buildEndSky();
/*  88 */     this.endSkyTexture = getTexture(textureManager, END_SKY_LOCATION);
/*  89 */     this.endFlashBuffer = buildEndFlashQuad(this.celestialsAtlas);
/*  90 */     this.sunBuffer = buildSunQuad(this.celestialsAtlas);
/*  91 */     this.moonBuffer = buildMoonPhases(this.celestialsAtlas);
/*  92 */     this.sunriseBuffer = buildSunriseFan();
/*  94 */     ByteBufferBuilder builder = ByteBufferBuilder.exactlySized(10 * DefaultVertexFormat.POSITION.getVertexSize());
/*     */     try {
/*  95 */       BufferBuilder bufferBuilder = new BufferBuilder(builder, PrimitiveTopology.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
/*  96 */       buildSkyDisc((VertexConsumer)bufferBuilder, 16.0F);
/*  97 */       MeshData meshData = bufferBuilder.buildOrThrow();
/*     */       try {
/*  98 */         this.topSkyBuffer = RenderSystem.getDevice().createBuffer(() -> "Top sky vertex buffer", 32, meshData.vertexBuffer());
/*  99 */         if (meshData != null)
/*  99 */           meshData.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (meshData != null)
/*     */           try {
/*     */             meshData.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 100 */       bufferBuilder = new BufferBuilder(builder, PrimitiveTopology.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
/* 101 */       buildSkyDisc((VertexConsumer)bufferBuilder, -16.0F);
/* 102 */       meshData = bufferBuilder.buildOrThrow();
/*     */       try {
/* 103 */         this.bottomSkyBuffer = RenderSystem.getDevice().createBuffer(() -> "Bottom sky vertex buffer", 32, meshData.vertexBuffer());
/* 104 */         if (meshData != null)
/* 104 */           meshData.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (meshData != null)
/*     */           try {
/*     */             meshData.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 105 */       if (builder != null)
/* 105 */         builder.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (builder != null)
/*     */         try {
/*     */           builder.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private AbstractTexture getTexture(TextureManager textureManager, Identifier location) {
/* 109 */     return textureManager.getTexture(location);
/*     */   }
/*     */   
/*     */   private GpuBuffer buildSunriseFan() {
/* 113 */     int vertices = 18;
/* 114 */     int vtxSize = DefaultVertexFormat.POSITION_COLOR.getVertexSize();
/* 115 */     ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(18 * vtxSize);
/*     */     try {
/* 116 */       BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
/* 117 */       int centerColor = ARGB.white(1.0F);
/* 118 */       int ringColor = ARGB.white(0.0F);
/* 119 */       bufferBuilder.addVertex(0.0F, 100.0F, 0.0F).setColor(centerColor);
/* 120 */       for (int i = 0; i <= 16; i++) {
/* 121 */         float angle = i * 6.2831855F / 16.0F;
/* 122 */         float sinAngle = Mth.sin(angle);
/* 123 */         float cosAngle = Mth.cos(angle);
/* 124 */         bufferBuilder.addVertex(sinAngle * 120.0F, cosAngle * 120.0F, -cosAngle * 40.0F).setColor(ringColor);
/*     */       } 
/* 126 */       MeshData mesh = bufferBuilder.buildOrThrow();
/*     */       try {
/* 127 */         GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "Sunrise/Sunset fan", 32, mesh.vertexBuffer());
/* 128 */         if (mesh != null)
/* 128 */           mesh.close(); 
/* 129 */         if (byteBufferBuilder != null)
/* 129 */           byteBufferBuilder.close(); 
/*     */         return gpuBuffer;
/*     */       } catch (Throwable throwable) {
/*     */         if (mesh != null)
/*     */           try {
/*     */             mesh.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } catch (Throwable throwable) {
/*     */       if (byteBufferBuilder != null)
/*     */         try {
/*     */           byteBufferBuilder.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static GpuBuffer buildSunQuad(TextureAtlas atlas) {
/* 133 */     return buildCelestialQuad("Sun quad", atlas.getSprite(SUN_SPRITE));
/*     */   }
/*     */   
/*     */   private static GpuBuffer buildEndFlashQuad(TextureAtlas atlas) {
/* 137 */     return buildCelestialQuad("End flash quad", atlas.getSprite(END_FLASH_SPRITE));
/*     */   }
/*     */   
/*     */   private static GpuBuffer buildCelestialQuad(String name, TextureAtlasSprite sprite) {
/* 141 */     VertexFormat format = DefaultVertexFormat.POSITION_TEX;
/* 142 */     ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(4 * format.getVertexSize());
/*     */     try {
/* 143 */       BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, format);
/* 144 */       bufferBuilder.addVertex(-1.0F, 0.0F, -1.0F).setUv(sprite.getU0(), sprite.getV0());
/* 145 */       bufferBuilder.addVertex(1.0F, 0.0F, -1.0F).setUv(sprite.getU1(), sprite.getV0());
/* 146 */       bufferBuilder.addVertex(1.0F, 0.0F, 1.0F).setUv(sprite.getU1(), sprite.getV1());
/* 147 */       bufferBuilder.addVertex(-1.0F, 0.0F, 1.0F).setUv(sprite.getU0(), sprite.getV1());
/* 148 */       MeshData mesh = bufferBuilder.buildOrThrow();
/*     */       try {
/* 149 */         GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> name, 32, mesh.vertexBuffer());
/* 150 */         if (mesh != null)
/* 150 */           mesh.close(); 
/* 151 */         if (byteBufferBuilder != null)
/* 151 */           byteBufferBuilder.close(); 
/*     */         return gpuBuffer;
/*     */       } catch (Throwable throwable) {
/*     */         if (mesh != null)
/*     */           try {
/*     */             mesh.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } catch (Throwable throwable) {
/*     */       if (byteBufferBuilder != null)
/*     */         try {
/*     */           byteBufferBuilder.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static GpuBuffer buildMoonPhases(TextureAtlas atlas) {
/* 155 */     MoonPhase[] phases = MoonPhase.values();
/* 156 */     VertexFormat format = DefaultVertexFormat.POSITION_TEX;
/* 157 */     ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(phases.length * 4 * format.getVertexSize());
/*     */     try {
/* 158 */       BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, format);
/* 159 */       for (MoonPhase phase : phases) {
/* 160 */         TextureAtlasSprite sprite = atlas.getSprite(Identifier.withDefaultNamespace("moon/" + phase.getSerializedName()));
/* 161 */         bufferBuilder.addVertex(-1.0F, 0.0F, -1.0F).setUv(sprite.getU1(), sprite.getV1());
/* 162 */         bufferBuilder.addVertex(1.0F, 0.0F, -1.0F).setUv(sprite.getU0(), sprite.getV1());
/* 163 */         bufferBuilder.addVertex(1.0F, 0.0F, 1.0F).setUv(sprite.getU0(), sprite.getV0());
/* 164 */         bufferBuilder.addVertex(-1.0F, 0.0F, 1.0F).setUv(sprite.getU1(), sprite.getV0());
/*     */       } 
/* 166 */       MeshData mesh = bufferBuilder.buildOrThrow();
/*     */       try {
/* 167 */         GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "Moon phases", 32, mesh.vertexBuffer());
/* 168 */         if (mesh != null)
/* 168 */           mesh.close(); 
/* 169 */         if (byteBufferBuilder != null)
/* 169 */           byteBufferBuilder.close(); 
/*     */         return gpuBuffer;
/*     */       } catch (Throwable throwable) {
/*     */         if (mesh != null)
/*     */           try {
/*     */             mesh.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } catch (Throwable throwable) {
/*     */       if (byteBufferBuilder != null)
/*     */         try {
/*     */           byteBufferBuilder.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private GpuBuffer buildStars() {
/* 173 */     RandomSource random = RandomSource.createThreadLocalInstance(10842L);
/* 174 */     float starDistance = 100.0F;
/* 176 */     ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION.getVertexSize() * 1500 * 4);
/*     */     try {
/* 177 */       BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);
/* 178 */       for (int i = 0; i < 1500; i++) {
/* 179 */         float x = random.nextFloat() * 2.0F - 1.0F;
/* 180 */         float y = random.nextFloat() * 2.0F - 1.0F;
/* 181 */         float z = random.nextFloat() * 2.0F - 1.0F;
/* 183 */         float starSize = 0.15F + random.nextFloat() * 0.1F;
/* 185 */         float lengthSq = Mth.lengthSquared(x, y, z);
/* 186 */         if (lengthSq > 0.010000001F && lengthSq < 1.0F) {
/* 190 */           Vector3f starCenter = (new Vector3f(x, y, z)).normalize(100.0F);
/* 193 */           float zRot = (float)(random.nextDouble() * 3.1415927410125732D * 2.0D);
/* 197 */           Matrix3f rotation = (new Matrix3f()).rotateTowards((Vector3fc)(new Vector3f((Vector3fc)starCenter)).negate(), (Vector3fc)new Vector3f(0.0F, 1.0F, 0.0F)).rotateZ(-zRot);
/* 199 */           bufferBuilder.addVertex((Vector3fc)(new Vector3f(starSize, -starSize, 0.0F)).mul((Matrix3fc)rotation).add((Vector3fc)starCenter));
/* 200 */           bufferBuilder.addVertex((Vector3fc)(new Vector3f(starSize, starSize, 0.0F)).mul((Matrix3fc)rotation).add((Vector3fc)starCenter));
/* 201 */           bufferBuilder.addVertex((Vector3fc)(new Vector3f(-starSize, starSize, 0.0F)).mul((Matrix3fc)rotation).add((Vector3fc)starCenter));
/* 202 */           bufferBuilder.addVertex((Vector3fc)(new Vector3f(-starSize, -starSize, 0.0F)).mul((Matrix3fc)rotation).add((Vector3fc)starCenter));
/*     */         } 
/*     */       } 
/* 204 */       MeshData mesh = bufferBuilder.buildOrThrow();
/*     */       try {
/* 205 */         this.starIndexCount = mesh.drawState().indexCount();
/* 206 */         GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "Stars vertex buffer", 40, mesh.vertexBuffer());
/* 207 */         if (mesh != null)
/* 207 */           mesh.close(); 
/* 208 */         if (byteBufferBuilder != null)
/* 208 */           byteBufferBuilder.close(); 
/*     */         return gpuBuffer;
/*     */       } catch (Throwable throwable) {
/*     */         if (mesh != null)
/*     */           try {
/*     */             mesh.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } catch (Throwable throwable) {
/*     */       if (byteBufferBuilder != null)
/*     */         try {
/*     */           byteBufferBuilder.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void buildSkyDisc(VertexConsumer builder, float yy) {
/* 212 */     float x = Math.signum(yy) * 512.0F;
/* 214 */     builder.addVertex(0.0F, yy, 0.0F);
/* 215 */     for (int i = -180; i <= 180; i += 45)
/* 216 */       builder.addVertex(x * Mth.cos((i * 0.017453292F)), yy, 512.0F * Mth.sin((i * 0.017453292F))); 
/*     */   }
/*     */   
/*     */   private static GpuBuffer buildEndSky() {
/* 221 */     ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(24 * DefaultVertexFormat.POSITION_TEX_COLOR.getVertexSize());
/*     */     try {
/* 222 */       BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
/* 223 */       for (int i = 0; i < 6; i++) {
/* 224 */         Matrix4f pose = new Matrix4f();
/* 225 */         switch (i) {
/*     */           case 1:
/* 226 */             pose.rotationX(1.5707964F);
/*     */             break;
/*     */           case 2:
/* 227 */             pose.rotationX(-1.5707964F);
/*     */             break;
/*     */           case 3:
/* 228 */             pose.rotationX(3.1415927F);
/*     */             break;
/*     */           case 4:
/* 229 */             pose.rotationZ(1.5707964F);
/*     */             break;
/*     */           case 5:
/* 230 */             pose.rotationZ(-1.5707964F);
/*     */             break;
/*     */         } 
/* 233 */         bufferBuilder.addVertex((Matrix4fc)pose, -100.0F, -100.0F, -100.0F).setUv(0.0F, 0.0F).setColor(-14145496);
/* 234 */         bufferBuilder.addVertex((Matrix4fc)pose, -100.0F, -100.0F, 100.0F).setUv(0.0F, 16.0F).setColor(-14145496);
/* 235 */         bufferBuilder.addVertex((Matrix4fc)pose, 100.0F, -100.0F, 100.0F).setUv(16.0F, 16.0F).setColor(-14145496);
/* 236 */         bufferBuilder.addVertex((Matrix4fc)pose, 100.0F, -100.0F, -100.0F).setUv(16.0F, 0.0F).setColor(-14145496);
/*     */       } 
/* 239 */       MeshData meshData = bufferBuilder.buildOrThrow();
/*     */       try {
/* 240 */         GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(() -> "End sky vertex buffer", 40, meshData.vertexBuffer());
/* 241 */         if (meshData != null)
/* 241 */           meshData.close(); 
/* 242 */         if (byteBufferBuilder != null)
/* 242 */           byteBufferBuilder.close(); 
/*     */         return gpuBuffer;
/*     */       } catch (Throwable throwable) {
/*     */         if (meshData != null)
/*     */           try {
/*     */             meshData.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } catch (Throwable throwable) {
/*     */       if (byteBufferBuilder != null)
/*     */         try {
/*     */           byteBufferBuilder.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderSkyDisc(int skyColor) {
/* 246 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewMatrixCopy(), ARGB.vector4fFromARGB32(skyColor));
/* 247 */     GpuTextureView colorTexture = this.renderTarget.getColorTextureView();
/* 248 */     GpuTextureView depthTexture = this.renderTarget.getDepthTextureView();
/* 249 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Sky disc", colorTexture, Optional.empty(), depthTexture, OptionalDouble.empty());
/*     */     try {
/* 250 */       renderPass.setPipeline(RenderPipelines.SKY);
/* 251 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 252 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 253 */       renderPass.setVertexBuffer(0, this.topSkyBuffer.slice());
/* 254 */       renderPass.draw(10, 1, 0, 0);
/* 255 */       if (renderPass != null)
/* 255 */         renderPass.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (renderPass != null)
/*     */         try {
/*     */           renderPass.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void extractRenderState(ClientLevel level, float partialTicks, Camera camera, SkyRenderState state) {
/* 259 */     state.skybox = level.dimensionType().skybox();
/* 260 */     if (state.skybox == DimensionType.Skybox.NONE)
/*     */       return; 
/* 263 */     if (state.skybox == DimensionType.Skybox.END) {
/* 264 */       EndFlashState endFlashState = level.endFlashState();
/* 265 */       if (endFlashState == null)
/*     */         return; 
/* 268 */       state.endFlashIntensity = endFlashState.getIntensity(partialTicks);
/* 269 */       state.endFlashXAngle = endFlashState.getXAngle();
/* 270 */       state.endFlashYAngle = endFlashState.getYAngle();
/*     */       return;
/*     */     } 
/* 274 */     EnvironmentAttributeProbe attributeProbe = camera.attributeProbe();
/* 275 */     state.sunAngle = ((Float)attributeProbe.getValue(EnvironmentAttributes.SUN_ANGLE, partialTicks)).floatValue() * 0.017453292F;
/* 276 */     state.moonAngle = ((Float)attributeProbe.getValue(EnvironmentAttributes.MOON_ANGLE, partialTicks)).floatValue() * 0.017453292F;
/* 277 */     state.starAngle = ((Float)attributeProbe.getValue(EnvironmentAttributes.STAR_ANGLE, partialTicks)).floatValue() * 0.017453292F;
/* 278 */     state.rainBrightness = 1.0F - level.getRainLevel(partialTicks);
/* 279 */     state.starBrightness = ((Float)attributeProbe.getValue(EnvironmentAttributes.STAR_BRIGHTNESS, partialTicks)).floatValue();
/* 280 */     state.sunriseAndSunsetColor = ((Integer)camera.attributeProbe().getValue(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, partialTicks)).intValue();
/* 281 */     state.moonPhase = (MoonPhase)attributeProbe.getValue(EnvironmentAttributes.MOON_PHASE, partialTicks);
/* 282 */     state.skyColor = ((Integer)attributeProbe.getValue(EnvironmentAttributes.SKY_COLOR, partialTicks)).intValue();
/* 283 */     state.shouldRenderDarkDisc = shouldRenderDarkDisc(partialTicks, level);
/*     */   }
/*     */   
/*     */   private boolean shouldRenderDarkDisc(float deltaPartialTick, ClientLevel level) {
/* 287 */     return (((Minecraft.getInstance()).player.getEyePosition(deltaPartialTick)).y - level.getLevelData().getHorizonHeight((LevelHeightAccessor)level) < 0.0D && !(Minecraft.getInstance()).player.isUnderWater());
/*     */   }
/*     */   
/*     */   public void renderDarkDisc() {
/* 291 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 292 */     modelViewStack.pushMatrix();
/* 293 */     modelViewStack.translate(0.0F, 12.0F, 0.0F);
/* 294 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(new Matrix4f((Matrix4fc)modelViewStack), new Vector4f(0.0F, 0.0F, 0.0F, 1.0F));
/* 296 */     GpuTextureView colorTexture = this.renderTarget.getColorTextureView();
/* 297 */     GpuTextureView depthTexture = this.renderTarget.getDepthTextureView();
/* 298 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Sky dark", colorTexture, Optional.empty(), depthTexture, OptionalDouble.empty());
/*     */     try {
/* 299 */       renderPass.setPipeline(RenderPipelines.SKY);
/* 300 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 301 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 302 */       renderPass.setVertexBuffer(0, this.bottomSkyBuffer.slice());
/* 303 */       renderPass.draw(10, 1, 0, 0);
/* 304 */       if (renderPass != null)
/* 304 */         renderPass.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (renderPass != null)
/*     */         try {
/*     */           renderPass.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 306 */     modelViewStack.popMatrix();
/*     */   }
/*     */   
/*     */   public void renderSunMoonAndStars(PoseStack poseStack, float sunAngle, float moonAngle, float starAngle, MoonPhase moonPhase, float rainBrightness, float starBrightness) {
/* 310 */     poseStack.pushPose();
/* 311 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-90.0F));
/* 313 */     poseStack.pushPose();
/* 314 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotation(sunAngle));
/* 315 */     renderSun(rainBrightness, poseStack);
/* 316 */     poseStack.popPose();
/* 318 */     poseStack.pushPose();
/* 319 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotation(moonAngle));
/* 320 */     renderMoon(moonPhase, rainBrightness, poseStack);
/* 321 */     poseStack.popPose();
/* 323 */     if (starBrightness > 0.0F) {
/* 324 */       poseStack.pushPose();
/* 325 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotation(starAngle));
/* 326 */       renderStars(starBrightness, poseStack);
/* 327 */       poseStack.popPose();
/*     */     } 
/* 330 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private void renderSun(float rainBrightness, PoseStack poseStack) {
/* 334 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 335 */     modelViewStack.pushMatrix();
/* 336 */     modelViewStack.mul((Matrix4fc)poseStack.last().pose());
/* 337 */     modelViewStack.translate(0.0F, 100.0F, 0.0F);
/* 338 */     modelViewStack.scale(30.0F, 1.0F, 30.0F);
/* 340 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(new Matrix4f((Matrix4fc)modelViewStack), new Vector4f(1.0F, 1.0F, 1.0F, rainBrightness));
/* 345 */     GpuTextureView color = this.renderTarget.getColorTextureView();
/* 346 */     GpuTextureView depth = this.renderTarget.getDepthTextureView();
/* 347 */     GpuBuffer indexBuffer = this.quadIndices.getBuffer(6);
/* 350 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Sky sun", color, Optional.empty(), depth, OptionalDouble.empty());
/*     */     try {
/* 351 */       renderPass.setPipeline(RenderPipelines.CELESTIAL);
/* 352 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 353 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 354 */       renderPass.bindTexture("Sampler0", this.celestialsAtlas.getTextureView(), this.celestialsAtlas.getSampler());
/* 355 */       renderPass.setVertexBuffer(0, this.sunBuffer.slice());
/* 356 */       renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
/* 357 */       renderPass.drawIndexed(6, 1, 0, 0, 0);
/* 358 */       if (renderPass != null)
/* 358 */         renderPass.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (renderPass != null)
/*     */         try {
/*     */           renderPass.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 360 */     modelViewStack.popMatrix();
/*     */   }
/*     */   
/*     */   private void renderMoon(MoonPhase moonPhase, float rainBrightness, PoseStack poseStack) {
/* 364 */     int baseVertex = moonPhase.index() * 4;
/* 366 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 367 */     modelViewStack.pushMatrix();
/* 368 */     modelViewStack.mul((Matrix4fc)poseStack.last().pose());
/* 369 */     modelViewStack.translate(0.0F, 100.0F, 0.0F);
/* 370 */     modelViewStack.scale(20.0F, 1.0F, 20.0F);
/* 372 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(new Matrix4f((Matrix4fc)modelViewStack), new Vector4f(1.0F, 1.0F, 1.0F, rainBrightness));
/* 377 */     GpuTextureView color = this.renderTarget.getColorTextureView();
/* 378 */     GpuTextureView depth = this.renderTarget.getDepthTextureView();
/* 379 */     GpuBuffer indexBuffer = this.quadIndices.getBuffer(6);
/* 382 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Sky moon", color, Optional.empty(), depth, OptionalDouble.empty());
/*     */     try {
/* 383 */       renderPass.setPipeline(RenderPipelines.CELESTIAL);
/* 384 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 385 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 386 */       renderPass.bindTexture("Sampler0", this.celestialsAtlas.getTextureView(), this.celestialsAtlas.getSampler());
/* 387 */       renderPass.setVertexBuffer(0, this.moonBuffer.slice());
/* 388 */       renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
/* 389 */       renderPass.drawIndexed(6, 1, 0, baseVertex, 0);
/* 390 */       if (renderPass != null)
/* 390 */         renderPass.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (renderPass != null)
/*     */         try {
/*     */           renderPass.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 392 */     modelViewStack.popMatrix();
/*     */   }
/*     */   
/*     */   private void renderStars(float starBrightness, PoseStack poseStack) {
/* 396 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 397 */     modelViewStack.pushMatrix();
/* 398 */     modelViewStack.mul((Matrix4fc)poseStack.last().pose());
/* 399 */     RenderPipeline renderPipeline = RenderPipelines.STARS;
/* 401 */     GpuTextureView colorTexture = this.renderTarget.getColorTextureView();
/* 402 */     GpuTextureView depthTexture = this.renderTarget.getDepthTextureView();
/* 403 */     GpuBuffer indexBuffer = this.quadIndices.getBuffer(this.starIndexCount);
/* 404 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(new Matrix4f((Matrix4fc)modelViewStack), new Vector4f(starBrightness, starBrightness, starBrightness, starBrightness));
/* 406 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Stars", colorTexture, Optional.empty(), depthTexture, OptionalDouble.empty());
/*     */     try {
/* 407 */       renderPass.setPipeline(renderPipeline);
/* 408 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 409 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 410 */       renderPass.setVertexBuffer(0, this.starBuffer.slice());
/* 411 */       renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
/* 412 */       renderPass.drawIndexed(this.starIndexCount, 1, 0, 0, 0);
/* 413 */       if (renderPass != null)
/* 413 */         renderPass.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (renderPass != null)
/*     */         try {
/*     */           renderPass.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 414 */     modelViewStack.popMatrix();
/*     */   }
/*     */   
/*     */   public void renderSunriseAndSunset(PoseStack poseStack, float sunAngle, int sunriseAndSunsetColor) {
/* 418 */     float alpha = ARGB.alphaFloat(sunriseAndSunsetColor);
/* 419 */     if (alpha <= 0.001F)
/*     */       return; 
/* 423 */     poseStack.pushPose();
/* 424 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(90.0F));
/* 425 */     float angle = (Mth.sin(sunAngle) < 0.0F) ? 180.0F : 0.0F;
/* 426 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(angle + 90.0F));
/* 428 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 429 */     modelViewStack.pushMatrix();
/* 430 */     modelViewStack.mul((Matrix4fc)poseStack.last().pose());
/* 431 */     modelViewStack.scale(1.0F, 1.0F, alpha);
/* 433 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(new Matrix4f((Matrix4fc)modelViewStack), 
/*     */         
/* 435 */         ARGB.vector4fFromARGB32(sunriseAndSunsetColor));
/* 438 */     GpuTextureView color = this.renderTarget.getColorTextureView();
/* 439 */     GpuTextureView depth = this.renderTarget.getDepthTextureView();
/* 442 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Sunrise sunset", color, Optional.empty(), depth, OptionalDouble.empty());
/*     */     try {
/* 443 */       renderPass.setPipeline(RenderPipelines.SUNRISE_SUNSET);
/* 444 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 445 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 446 */       renderPass.setVertexBuffer(0, this.sunriseBuffer.slice());
/* 447 */       renderPass.draw(18, 1, 0, 0);
/* 448 */       if (renderPass != null)
/* 448 */         renderPass.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (renderPass != null)
/*     */         try {
/*     */           renderPass.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 450 */     modelViewStack.popMatrix();
/* 451 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public void renderEndSky() {
/* 455 */     RenderSystem.AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
/* 456 */     GpuBuffer indexBuffer = autoIndices.getBuffer(36);
/* 458 */     GpuTextureView colorTexture = this.renderTarget.getColorTextureView();
/* 459 */     GpuTextureView depthTexture = this.renderTarget.getDepthTextureView();
/* 460 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewMatrixCopy());
/* 462 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "End sky", colorTexture, Optional.empty(), depthTexture, OptionalDouble.empty());
/*     */     try {
/* 463 */       renderPass.setPipeline(RenderPipelines.END_SKY);
/* 464 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 465 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 466 */       renderPass.bindTexture("Sampler0", this.endSkyTexture.getTextureView(), this.endSkyTexture.getSampler());
/* 467 */       renderPass.setVertexBuffer(0, this.endSkyBuffer.slice());
/* 468 */       renderPass.setIndexBuffer(indexBuffer, autoIndices.type());
/* 469 */       renderPass.drawIndexed(36, 1, 0, 0, 0);
/* 470 */       if (renderPass != null)
/* 470 */         renderPass.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (renderPass != null)
/*     */         try {
/*     */           renderPass.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderEndFlash(PoseStack poseStack, float intensity, float xAngle, float yAngle) {
/* 474 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F - yAngle));
/* 475 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-90.0F - xAngle));
/* 477 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 478 */     modelViewStack.pushMatrix();
/* 479 */     modelViewStack.mul((Matrix4fc)poseStack.last().pose());
/* 480 */     modelViewStack.translate(0.0F, 100.0F, 0.0F);
/* 481 */     modelViewStack.scale(60.0F, 1.0F, 60.0F);
/* 483 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(new Matrix4f((Matrix4fc)modelViewStack), new Vector4f(intensity, intensity, intensity, intensity));
/* 488 */     GpuTextureView color = this.renderTarget.getColorTextureView();
/* 489 */     GpuTextureView depth = this.renderTarget.getDepthTextureView();
/* 490 */     GpuBuffer indexBuffer = this.quadIndices.getBuffer(6);
/* 493 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "End flash", color, Optional.empty(), depth, OptionalDouble.empty());
/*     */     try {
/* 494 */       renderPass.setPipeline(RenderPipelines.CELESTIAL);
/* 495 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 496 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 497 */       renderPass.bindTexture("Sampler0", this.celestialsAtlas.getTextureView(), this.celestialsAtlas.getSampler());
/* 498 */       renderPass.setVertexBuffer(0, this.endFlashBuffer.slice());
/* 499 */       renderPass.setIndexBuffer(indexBuffer, this.quadIndices.type());
/* 500 */       renderPass.drawIndexed(6, 1, 0, 0, 0);
/* 501 */       if (renderPass != null)
/* 501 */         renderPass.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (renderPass != null)
/*     */         try {
/*     */           renderPass.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 503 */     modelViewStack.popMatrix();
/*     */   }
/*     */   
/*     */   public void close() {
/* 508 */     this.sunBuffer.close();
/* 509 */     this.moonBuffer.close();
/* 510 */     this.starBuffer.close();
/* 511 */     this.topSkyBuffer.close();
/* 512 */     this.bottomSkyBuffer.close();
/* 513 */     this.endSkyBuffer.close();
/* 514 */     this.sunriseBuffer.close();
/* 515 */     this.endFlashBuffer.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\SkyRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */