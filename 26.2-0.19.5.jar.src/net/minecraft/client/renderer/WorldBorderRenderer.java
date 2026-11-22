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
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalDouble;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.state.level.WorldBorderRenderState;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector4f;
/*     */ 
/*     */ public class WorldBorderRenderer implements AutoCloseable {
/*  35 */   public static final Identifier FORCEFIELD_LOCATION = Identifier.withDefaultNamespace("textures/misc/forcefield.png");
/*     */   
/*     */   private boolean needsRebuild = true;
/*     */   
/*     */   private double lastMinX;
/*     */   
/*     */   private double lastMinZ;
/*     */   
/*     */   private double lastBorderMinX;
/*     */   
/*     */   private double lastBorderMaxX;
/*     */   
/*     */   private double lastBorderMinZ;
/*     */   
/*     */   private double lastBorderMaxZ;
/*     */   
/*  46 */   private final GpuBuffer worldBorderBuffer = RenderSystem.getDevice().createBuffer(() -> "World border vertex buffer", 40, 16L * DefaultVertexFormat.POSITION_TEX.getVertexSize());
/*     */   
/*     */   private final RenderSystem.AutoStorageIndexBuffer indices;
/*     */   
/*     */   public WorldBorderRenderer() {
/*  50 */     this.indices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
/*     */   }
/*     */   
/*     */   public void close() {
/*  55 */     this.worldBorderBuffer.close();
/*     */   }
/*     */   
/*     */   private void rebuildWorldBorderBuffer(WorldBorderRenderState state, double renderDistance, double cameraZ, double cameraX, float halfHeightY, float v1, float v0) {
/*  59 */     ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION_TEX.getVertexSize() * 4 * 4);
/*     */     try {
/*  60 */       double borderMinX = state.minX;
/*  61 */       double borderMaxX = state.maxX;
/*  62 */       double borderMinZ = state.minZ;
/*  63 */       double borderMaxZ = state.maxZ;
/*  65 */       double minZ = Math.max(Mth.floor(cameraZ - renderDistance), borderMinZ);
/*  66 */       double maxZ = Math.min(Mth.ceil(cameraZ + renderDistance), borderMaxZ);
/*  67 */       float u0z = (Mth.floor(minZ) & 0x1) * 0.5F;
/*  68 */       float u1z = (float)(maxZ - minZ) / 2.0F;
/*  70 */       double minX = Math.max(Mth.floor(cameraX - renderDistance), borderMinX);
/*  71 */       double maxX = Math.min(Mth.ceil(cameraX + renderDistance), borderMaxX);
/*  72 */       float u0x = (Mth.floor(minX) & 0x1) * 0.5F;
/*  73 */       float u1x = (float)(maxX - minX) / 2.0F;
/*  75 */       BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX);
/*  77 */       bufferBuilder.addVertex(0.0F, -halfHeightY, (float)(borderMaxZ - minZ)).setUv(u0x, v1);
/*  78 */       bufferBuilder.addVertex((float)(maxX - minX), -halfHeightY, (float)(borderMaxZ - minZ)).setUv(u1x + u0x, v1);
/*  79 */       bufferBuilder.addVertex((float)(maxX - minX), halfHeightY, (float)(borderMaxZ - minZ)).setUv(u1x + u0x, v0);
/*  80 */       bufferBuilder.addVertex(0.0F, halfHeightY, (float)(borderMaxZ - minZ)).setUv(u0x, v0);
/*  82 */       bufferBuilder.addVertex(0.0F, -halfHeightY, 0.0F).setUv(u0z, v1);
/*  83 */       bufferBuilder.addVertex(0.0F, -halfHeightY, (float)(maxZ - minZ)).setUv(u1z + u0z, v1);
/*  84 */       bufferBuilder.addVertex(0.0F, halfHeightY, (float)(maxZ - minZ)).setUv(u1z + u0z, v0);
/*  85 */       bufferBuilder.addVertex(0.0F, halfHeightY, 0.0F).setUv(u0z, v0);
/*  87 */       bufferBuilder.addVertex((float)(maxX - minX), -halfHeightY, 0.0F).setUv(u0x, v1);
/*  88 */       bufferBuilder.addVertex(0.0F, -halfHeightY, 0.0F).setUv(u1x + u0x, v1);
/*  89 */       bufferBuilder.addVertex(0.0F, halfHeightY, 0.0F).setUv(u1x + u0x, v0);
/*  90 */       bufferBuilder.addVertex((float)(maxX - minX), halfHeightY, 0.0F).setUv(u0x, v0);
/*  92 */       bufferBuilder.addVertex((float)(borderMaxX - minX), -halfHeightY, (float)(maxZ - minZ)).setUv(u0z, v1);
/*  93 */       bufferBuilder.addVertex((float)(borderMaxX - minX), -halfHeightY, 0.0F).setUv(u1z + u0z, v1);
/*  94 */       bufferBuilder.addVertex((float)(borderMaxX - minX), halfHeightY, 0.0F).setUv(u1z + u0z, v0);
/*  95 */       bufferBuilder.addVertex((float)(borderMaxX - minX), halfHeightY, (float)(maxZ - minZ)).setUv(u0z, v0);
/*  97 */       MeshData meshData = bufferBuilder.buildOrThrow();
/*     */       try {
/*  98 */         RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.worldBorderBuffer.slice(), meshData.vertexBuffer());
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
/* 101 */       this.lastBorderMinX = borderMinX;
/* 102 */       this.lastBorderMaxX = borderMaxX;
/* 103 */       this.lastBorderMinZ = borderMinZ;
/* 104 */       this.lastBorderMaxZ = borderMaxZ;
/* 105 */       this.lastMinX = minX;
/* 106 */       this.lastMinZ = minZ;
/* 107 */       this.needsRebuild = false;
/* 108 */       if (byteBufferBuilder != null)
/* 108 */         byteBufferBuilder.close(); 
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
/*     */   public void extract(WorldBorder border, float deltaPartialTick, Vec3 cameraPos, double renderDistance, WorldBorderRenderState state) {
/* 112 */     state.minX = border.getMinX(deltaPartialTick);
/* 113 */     state.maxX = border.getMaxX(deltaPartialTick);
/* 114 */     state.minZ = border.getMinZ(deltaPartialTick);
/* 115 */     state.maxZ = border.getMaxZ(deltaPartialTick);
/* 116 */     if ((cameraPos.x < state.maxX - renderDistance && cameraPos.x > state.minX + renderDistance && cameraPos.z < state.maxZ - renderDistance && cameraPos.z > state.minZ + renderDistance) || cameraPos.x < state.minX - renderDistance || cameraPos.x > state.maxX + renderDistance || cameraPos.z < state.minZ - renderDistance || cameraPos.z > state.maxZ + renderDistance) {
/* 118 */       state.alpha = 0.0D;
/*     */       return;
/*     */     } 
/* 122 */     state.alpha = 1.0D - border.getDistanceToBorder(cameraPos.x, cameraPos.z) / renderDistance;
/* 123 */     state.alpha = Math.pow(state.alpha, 4.0D);
/* 124 */     state.alpha = Mth.clamp(state.alpha, 0.0D, 1.0D);
/* 125 */     state.tint = border.getStatus().getColor();
/*     */   }
/*     */   
/*     */   public void render(WorldBorderRenderState state, Vec3 cameraPos, double renderDistance, double depthFar) {
/*     */     GpuTextureView colorTexture, depthTexture;
/* 129 */     if (state.alpha <= 0.0D)
/*     */       return; 
/* 132 */     double cameraX = cameraPos.x;
/* 133 */     double cameraZ = cameraPos.z;
/* 134 */     float halfHeightY = (float)depthFar;
/* 136 */     float red = ARGB.red(state.tint) / 255.0F;
/* 137 */     float green = ARGB.green(state.tint) / 255.0F;
/* 138 */     float blue = ARGB.blue(state.tint) / 255.0F;
/* 140 */     float offset = (float)(Util.getMillis() % 3000L) / 3000.0F;
/* 142 */     float v0 = (float)-Mth.frac(cameraPos.y * 0.5D);
/* 143 */     float v1 = v0 + halfHeightY;
/* 145 */     if (shouldRebuildWorldBorderBuffer(state))
/* 146 */       rebuildWorldBorderBuffer(state, renderDistance, cameraZ, cameraX, halfHeightY, v1, v0); 
/* 149 */     TextureManager textureManager = Minecraft.getInstance().getTextureManager();
/* 150 */     AbstractTexture abstractTexture = textureManager.getTexture(FORCEFIELD_LOCATION);
/* 152 */     RenderPipeline renderPipeline = RenderPipelines.WORLD_BORDER;
/* 154 */     RenderTarget mainRenderTarget = (Minecraft.getInstance()).gameRenderer.mainRenderTarget();
/* 155 */     RenderTarget weatherTarget = (Minecraft.getInstance()).levelRenderer.weatherTarget();
/* 158 */     if (weatherTarget != null) {
/* 159 */       colorTexture = weatherTarget.getColorTextureView();
/* 160 */       depthTexture = weatherTarget.getDepthTextureView();
/*     */     } else {
/* 162 */       colorTexture = mainRenderTarget.getColorTextureView();
/* 163 */       depthTexture = mainRenderTarget.getDepthTextureView();
/*     */     } 
/* 166 */     GpuBuffer indexBuffer = this.indices.getBuffer(6);
/* 167 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewMatrixCopy(), new Vector4f(red, green, blue, (float)state.alpha), new Vector3f((float)(this.lastMinX - cameraX), (float)-cameraPos.y, (float)(this.lastMinZ - cameraZ)), (new Matrix4f()).translation(offset, offset, 0.0F));
/* 168 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "World border", colorTexture, Optional.empty(), depthTexture, OptionalDouble.empty());
/*     */     try {
/* 169 */       renderPass.setPipeline(renderPipeline);
/* 170 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 171 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 172 */       renderPass.setIndexBuffer(indexBuffer, this.indices.type());
/* 173 */       renderPass.bindTexture("Sampler0", abstractTexture.getTextureView(), abstractTexture.getSampler());
/* 174 */       renderPass.setVertexBuffer(0, this.worldBorderBuffer.slice());
/* 176 */       ArrayList<RenderPass.Draw<WorldBorderRenderer>> draws = new ArrayList<>();
/* 177 */       for (WorldBorderRenderState.DistancePerDirection distancePerDirection : state.closestBorder(cameraX, cameraZ)) {
/* 178 */         if (distancePerDirection.distance() < renderDistance) {
/* 179 */           int sideIndex = distancePerDirection.direction().get2DDataValue();
/* 180 */           draws.add(new RenderPass.Draw(0, this.worldBorderBuffer, indexBuffer, this.indices.type(), 6 * sideIndex, 6, 0));
/*     */         } 
/*     */       } 
/* 184 */       renderPass.drawMultipleIndexed(draws, null, null, Collections.emptyList(), this);
/* 185 */       if (renderPass != null)
/* 185 */         renderPass.close(); 
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
/*     */   public void invalidate() {
/* 189 */     this.needsRebuild = true;
/*     */   }
/*     */   
/*     */   private boolean shouldRebuildWorldBorderBuffer(WorldBorderRenderState state) {
/* 193 */     return (this.needsRebuild || state.minX != this.lastBorderMinX || state.minZ != this.lastBorderMinZ || state.maxX != this.lastBorderMaxX || state.maxZ != this.lastBorderMaxZ);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\WorldBorderRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */