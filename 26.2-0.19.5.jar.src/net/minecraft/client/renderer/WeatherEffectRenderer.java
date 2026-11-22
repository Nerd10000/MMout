/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.IndexType;
/*     */ import com.mojang.blaze3d.PrimitiveTopology;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.BufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.DefaultVertexFormat;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalDouble;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.rendertype.OutputTarget;
/*     */ import net.minecraft.client.renderer.state.level.WeatherRenderState;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.LightCoordsUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class WeatherEffectRenderer implements AutoCloseable {
/*  43 */   private static final Identifier RAIN_LOCATION = Identifier.withDefaultNamespace("textures/environment/rain.png");
/*     */   
/*  44 */   private static final Identifier SNOW_LOCATION = Identifier.withDefaultNamespace("textures/environment/snow.png");
/*     */   
/*     */   private static final int RAIN_TABLE_SIZE = 32;
/*     */   
/*     */   private static final int HALF_RAIN_TABLE_SIZE = 16;
/*     */   
/*     */   private static final int INDICES_PER_COLUMN = 6;
/*     */   
/*  49 */   private final float[] columnSizeX = new float[1024];
/*     */   
/*  50 */   private final float[] columnSizeZ = new float[1024];
/*     */   
/*     */   private GpuBuffer vertexBuffer;
/*     */   
/*     */   public WeatherEffectRenderer() {
/*  55 */     for (int z = 0; z < 32; z++) {
/*  56 */       for (int x = 0; x < 32; x++) {
/*  57 */         float deltaX = (x - 16);
/*  58 */         float deltaZ = (z - 16);
/*  59 */         float distance = Mth.length(deltaX, deltaZ);
/*  60 */         this.columnSizeX[z * 32 + x] = -deltaZ / distance;
/*  61 */         this.columnSizeZ[z * 32 + x] = deltaX / distance;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void extractRenderState(ClientLevel level, float partialTicks, Vec3 cameraPos, WeatherRenderState renderState) {
/*  67 */     renderState.intensity = level.getRainLevel(partialTicks);
/*  68 */     if (renderState.intensity <= 0.0F)
/*     */       return; 
/*  72 */     renderState.radius = ((Integer)(Minecraft.getInstance()).options.weatherRadius().get()).intValue();
/*  73 */     int cameraBlockX = Mth.floor(cameraPos.x);
/*  74 */     int cameraBlockY = Mth.floor(cameraPos.y);
/*  75 */     int cameraBlockZ = Mth.floor(cameraPos.z);
/*  77 */     BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
/*  78 */     RandomSource random = RandomSource.createThreadLocalInstance();
/*  80 */     for (int z = cameraBlockZ - renderState.radius; z <= cameraBlockZ + renderState.radius; z++) {
/*  81 */       for (int x = cameraBlockX - renderState.radius; x <= cameraBlockX + renderState.radius; x++) {
/*  82 */         int terrainHeight = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
/*  83 */         int y0 = Math.max(cameraBlockY - renderState.radius, terrainHeight);
/*  84 */         int y1 = Math.max(cameraBlockY + renderState.radius, terrainHeight);
/*  85 */         if (y1 - y0 != 0) {
/*  89 */           Biome.Precipitation precipitation = level.getPrecipitationAt((BlockPos)mutablePos.set(x, cameraBlockY, z));
/*  90 */           if (precipitation != Biome.Precipitation.NONE) {
/*  94 */             int seed = x * x * 3121 + x * 45238971 ^ z * z * 418711 + z * 13761;
/*  95 */             random.setSeed(seed);
/*  98 */             int lightSampleY = Math.max(cameraBlockY, terrainHeight);
/*  99 */             int lightCoords = LightCoordsUtil.getLightCoords((BlockAndLightGetter)level, (BlockPos)mutablePos.set(x, lightSampleY, z));
/* 101 */             if (precipitation == Biome.Precipitation.RAIN) {
/* 102 */               renderState.rainColumns.add(createRainColumnInstance(random, level.getGameTime(), x, y0, y1, z, lightCoords, partialTicks));
/* 103 */             } else if (precipitation == Biome.Precipitation.SNOW) {
/* 104 */               renderState.snowColumns.add(createSnowColumnInstance(random, level.getGameTime(), x, y0, y1, z, lightCoords, partialTicks));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderWeather(RenderPass renderPass, AbstractTexture texture, int startColumn, int columnCount) {
/* 111 */     renderPass.bindTexture("Sampler0", texture.getTextureView(), texture.getSampler());
/* 112 */     renderPass.drawIndexed(columnCount * 6, 1, startColumn * 6, 0, 0);
/*     */   }
/*     */   
/*     */   private GpuBuffer uploadVertexBuffer(ByteBuffer buffer) {
/* 116 */     GpuDevice device = RenderSystem.getDevice();
/* 117 */     if (this.vertexBuffer == null || this.vertexBuffer.size() < buffer.remaining()) {
/* 118 */       if (this.vertexBuffer != null)
/* 119 */         this.vertexBuffer.close(); 
/* 121 */       this.vertexBuffer = device.createBuffer(() -> "Weather Vertex Buffer", 40, buffer.remaining());
/*     */     } 
/* 123 */     device.createCommandEncoder().writeToBuffer(this.vertexBuffer.slice(), buffer);
/* 124 */     return this.vertexBuffer;
/*     */   }
/*     */   
/*     */   public void render(Vec3 cameraPos, WeatherRenderState renderState) {
/*     */     GpuBuffer vertexBuffer, indexBuffer;
/*     */     IndexType indexType;
/* 128 */     int columnCount = renderState.rainColumns.size() + renderState.snowColumns.size();
/* 129 */     if (columnCount == 0)
/*     */       return; 
/* 133 */     TextureManager textureManager = Minecraft.getInstance().getTextureManager();
/* 134 */     AbstractTexture rainTexture = textureManager.getTexture(RAIN_LOCATION);
/* 135 */     AbstractTexture snowTexture = textureManager.getTexture(SNOW_LOCATION);
/* 137 */     RenderTarget weatherRenderTarget = OutputTarget.WEATHER_TARGET.getRenderTarget();
/* 138 */     GpuTextureView colorTexture = weatherRenderTarget.getColorTextureView();
/* 139 */     GpuTextureView depthTexture = weatherRenderTarget.getDepthTextureView();
/* 141 */     RenderPipeline renderPipeline = (Minecraft.getInstance()).gameRenderer.gameRenderState().useShaderTransparency() ? RenderPipelines.WEATHER_DEPTH_WRITE : RenderPipelines.WEATHER_NO_DEPTH_WRITE;
/* 147 */     ByteBufferBuilder builder = ByteBufferBuilder.exactlySized(columnCount * DefaultVertexFormat.PARTICLE.getVertexSize() * 4);
/*     */     try {
/* 148 */       BufferBuilder bufferBuilder = new BufferBuilder(builder, PrimitiveTopology.QUADS, DefaultVertexFormat.PARTICLE);
/* 150 */       renderInstances((VertexConsumer)bufferBuilder, renderState.rainColumns, cameraPos, 1.0F, renderState.radius, renderState.intensity);
/* 151 */       renderInstances((VertexConsumer)bufferBuilder, renderState.snowColumns, cameraPos, 0.8F, renderState.radius, renderState.intensity);
/* 153 */       MeshData mesh = bufferBuilder.buildOrThrow();
/*     */       try {
/* 154 */         vertexBuffer = uploadVertexBuffer(mesh.vertexBuffer());
/* 155 */         RenderSystem.AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(mesh.drawState().primitiveTopology());
/* 156 */         indexBuffer = autoIndices.getBuffer(mesh.drawState().indexCount());
/* 157 */         indexType = autoIndices.type();
/* 158 */         if (mesh != null)
/* 158 */           mesh.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (mesh != null)
/*     */           try {
/*     */             mesh.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 159 */       if (builder != null)
/* 159 */         builder.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (builder != null)
/*     */         try {
/*     */           builder.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 161 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewMatrixCopy());
/* 163 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Weather Effect", colorTexture, Optional.empty(), depthTexture, OptionalDouble.empty());
/*     */     try {
/* 164 */       renderPass.setPipeline(renderPipeline);
/* 166 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 167 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 169 */       renderPass.bindTexture("Sampler2", (Minecraft.getInstance()).gameRenderer.lightmap(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
/* 171 */       renderPass.setIndexBuffer(indexBuffer, indexType);
/* 172 */       renderPass.setVertexBuffer(0, vertexBuffer.slice());
/* 174 */       renderWeather(renderPass, rainTexture, 0, renderState.rainColumns.size());
/* 175 */       renderWeather(renderPass, snowTexture, renderState.rainColumns.size(), renderState.snowColumns.size());
/* 176 */       if (renderPass != null)
/* 176 */         renderPass.close(); 
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
/*     */   private ColumnInstance createRainColumnInstance(RandomSource random, long ticks, int x, int bottomY, int topY, int z, int lightCoords, float partialTicks) {
/* 181 */     int wrappedTicks = (int)(ticks & 0x1FFFFL);
/* 182 */     int tickOffset = x * x * 3121 + x * 45238971 + z * z * 418711 + z * 13761 & 0xFF;
/* 183 */     float blockPosRainSpeed = 3.0F + random.nextFloat();
/* 184 */     float textureOffset = -((wrappedTicks + tickOffset) + partialTicks) / 32.0F * blockPosRainSpeed;
/* 185 */     float wrappedTextureOffset = textureOffset % 32.0F;
/* 187 */     return new ColumnInstance(x, z, bottomY, topY, 0.0F, wrappedTextureOffset, lightCoords);
/*     */   }
/*     */   
/*     */   private ColumnInstance createSnowColumnInstance(RandomSource random, long ticks, int x, int bottomY, int topY, int z, int lightCoords, float partialTicks) {
/* 192 */     int wrappedTicks = (int)(ticks & 0x1FFFFL);
/* 193 */     float time = wrappedTicks + partialTicks;
/* 195 */     float u = (float)(random.nextDouble() + (time * 0.01F * (float)random.nextGaussian()));
/* 196 */     float v = (float)(random.nextDouble() + (time * (float)random.nextGaussian() * 0.001F));
/* 197 */     float vOffset = -((float)(ticks & 0x1FFL) + partialTicks) / 512.0F;
/* 199 */     int brightenedLightCoords = LightCoordsUtil.pack((
/* 200 */         LightCoordsUtil.block(lightCoords) * 3 + 15) / 4, (
/* 201 */         LightCoordsUtil.sky(lightCoords) * 3 + 15) / 4);
/* 204 */     return new ColumnInstance(x, z, bottomY, topY, u, vOffset + v, brightenedLightCoords);
/*     */   }
/*     */   
/*     */   public static final class ColumnInstance extends Record {
/*     */     private final int x;
/*     */     
/*     */     private final int z;
/*     */     
/*     */     private final int bottomY;
/*     */     
/*     */     private final int topY;
/*     */     
/*     */     private final float uOffset;
/*     */     
/*     */     private final float vOffset;
/*     */     
/*     */     private final int lightCoords;
/*     */     
/*     */     public ColumnInstance(int x, int z, int bottomY, int topY, float uOffset, float vOffset, int lightCoords) {
/* 207 */       this.x = x;
/* 207 */       this.z = z;
/* 207 */       this.bottomY = bottomY;
/* 207 */       this.topY = topY;
/* 207 */       this.uOffset = uOffset;
/* 207 */       this.vOffset = vOffset;
/* 207 */       this.lightCoords = lightCoords;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #207	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #207	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #207	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int x() {
/* 207 */       return this.x;
/*     */     }
/*     */     
/*     */     public int z() {
/* 207 */       return this.z;
/*     */     }
/*     */     
/*     */     public int bottomY() {
/* 207 */       return this.bottomY;
/*     */     }
/*     */     
/*     */     public int topY() {
/* 207 */       return this.topY;
/*     */     }
/*     */     
/*     */     public float uOffset() {
/* 207 */       return this.uOffset;
/*     */     }
/*     */     
/*     */     public float vOffset() {
/* 207 */       return this.vOffset;
/*     */     }
/*     */     
/*     */     public int lightCoords() {
/* 207 */       return this.lightCoords;
/*     */     }
/*     */   }
/*     */   
/*     */   private void renderInstances(VertexConsumer builder, List<ColumnInstance> columns, Vec3 cameraPos, float maxAlpha, int radius, float intensity) {
/* 211 */     if (columns.isEmpty())
/*     */       return; 
/* 215 */     float radiusSq = (radius * radius);
/* 216 */     for (ColumnInstance column : columns) {
/* 217 */       float relativeX = (float)(column.x + 0.5D - cameraPos.x);
/* 218 */       float relativeZ = (float)(column.z + 0.5D - cameraPos.z);
/* 219 */       float distanceSq = (float)Mth.lengthSquared(relativeX, relativeZ);
/* 221 */       float alpha = Mth.lerp(Math.min(distanceSq / radiusSq, 1.0F), maxAlpha, 0.5F) * intensity;
/* 222 */       int color = ARGB.white(alpha);
/* 224 */       int index = (column.z - Mth.floor(cameraPos.z) + 16) * 32 + column.x - Mth.floor(cameraPos.x) + 16;
/* 225 */       float halfSizeX = this.columnSizeX[index] / 2.0F;
/* 226 */       float halfSizeZ = this.columnSizeZ[index] / 2.0F;
/* 228 */       float x0 = relativeX - halfSizeX;
/* 229 */       float x1 = relativeX + halfSizeX;
/* 230 */       float y1 = (float)(column.topY - cameraPos.y);
/* 231 */       float y0 = (float)(column.bottomY - cameraPos.y);
/* 232 */       float z0 = relativeZ - halfSizeZ;
/* 233 */       float z1 = relativeZ + halfSizeZ;
/* 235 */       float u0 = column.uOffset + 0.0F;
/* 236 */       float u1 = column.uOffset + 1.0F;
/* 237 */       float v0 = column.bottomY * 0.25F + column.vOffset;
/* 238 */       float v1 = column.topY * 0.25F + column.vOffset;
/* 240 */       builder.addVertex(x0, y1, z0).setUv(u0, v0).setColor(color).setLight(column.lightCoords);
/* 241 */       builder.addVertex(x1, y1, z1).setUv(u1, v0).setColor(color).setLight(column.lightCoords);
/* 242 */       builder.addVertex(x1, y0, z1).setUv(u1, v1).setColor(color).setLight(column.lightCoords);
/* 243 */       builder.addVertex(x0, y0, z0).setUv(u0, v1).setColor(color).setLight(column.lightCoords);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/* 249 */     if (this.vertexBuffer != null) {
/* 250 */       this.vertexBuffer.close();
/* 251 */       this.vertexBuffer = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\WeatherEffectRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */