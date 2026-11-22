/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.Std140Builder;
/*     */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.renderer.state.LightmapRenderState;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import org.joml.Vector4fc;
/*     */ 
/*     */ public class Lightmap implements AutoCloseable {
/*     */   public static final int TEXTURE_SIZE = 16;
/*     */   
/*  28 */   private static final Vector4fc CLEAR_COLOR = (Vector4fc)ARGB.vector4fFromARGB32(-1);
/*     */   
/*  29 */   private static final int LIGHTMAP_UBO_SIZE = (new Std140SizeCalculator())
/*  30 */     .putFloat()
/*  31 */     .putFloat()
/*  32 */     .putFloat()
/*  33 */     .putFloat()
/*  34 */     .putFloat()
/*  35 */     .putFloat()
/*  36 */     .putVec3()
/*  37 */     .putVec3()
/*  38 */     .putVec3()
/*  39 */     .putVec3()
/*  40 */     .get();
/*     */   
/*     */   private final GpuTexture texture;
/*     */   
/*     */   private final GpuTextureView textureView;
/*     */   
/*     */   private final MappableRingBuffer ubo;
/*     */   
/*     */   public Lightmap() {
/*  48 */     GpuDevice device = RenderSystem.getDevice();
/*  49 */     this.texture = device.createTexture("Lightmap", 13, GpuFormat.RGBA8_UNORM, 16, 16, 1, 1);
/*  50 */     this.textureView = device.createTextureView(this.texture);
/*  52 */     device.createCommandEncoder().clearColorTexture(this.texture, CLEAR_COLOR);
/*  53 */     this.ubo = new MappableRingBuffer(() -> "Lightmap UBO", 130, LIGHTMAP_UBO_SIZE);
/*     */   }
/*     */   
/*     */   public GpuTextureView getTextureView() {
/*  57 */     return this.textureView;
/*     */   }
/*     */   
/*     */   public void close() {
/*  62 */     this.texture.close();
/*  63 */     this.textureView.close();
/*  64 */     this.ubo.close();
/*     */   }
/*     */   
/*     */   public void render(LightmapRenderState renderState) {
/*  68 */     if (!renderState.needsUpdate)
/*     */       return; 
/*  71 */     ProfilerFiller profiler = Profiler.get();
/*  72 */     profiler.push("lightmap");
/*  73 */     CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
/*  75 */     GpuBufferSlice.MappedView view = this.ubo.currentBuffer().map(false, true);
/*     */     try {
/*  76 */       Std140Builder.intoBuffer(view.data())
/*  77 */         .putFloat(renderState.skyFactor)
/*  78 */         .putFloat(renderState.blockFactor)
/*  79 */         .putFloat(renderState.nightVisionEffectIntensity)
/*  80 */         .putFloat(renderState.darknessEffectScale)
/*  81 */         .putFloat(renderState.bossOverlayWorldDarkening)
/*  82 */         .putFloat(renderState.brightness)
/*  83 */         .putVec3(renderState.blockLightTint)
/*  84 */         .putVec3(renderState.skyLightColor)
/*  85 */         .putVec3(renderState.ambientColor)
/*  86 */         .putVec3(renderState.nightVisionColor);
/*  87 */       if (view != null)
/*  87 */         view.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (view != null)
/*     */         try {
/*     */           view.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*  89 */     RenderPass renderPass = commandEncoder.createRenderPass(() -> "Update light", this.textureView, Optional.empty());
/*     */     try {
/*  90 */       renderPass.setPipeline(RenderPipelines.LIGHTMAP);
/*  91 */       RenderSystem.bindDefaultUniforms(renderPass);
/*  92 */       renderPass.setUniform("LightmapInfo", this.ubo.currentBuffer());
/*  93 */       renderPass.draw(3, 1, 0, 0);
/*  94 */       if (renderPass != null)
/*  94 */         renderPass.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (renderPass != null)
/*     */         try {
/*     */           renderPass.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*  95 */     this.ubo.rotate();
/*  96 */     profiler.pop();
/*     */   }
/*     */   
/*     */   public static float getBrightness(DimensionType dimensionType, int level) {
/* 103 */     float v = level / 15.0F;
/* 105 */     float curvedV = v / (4.0F - 3.0F * v);
/* 106 */     return Mth.lerp(dimensionType.ambientLight(), curvedV, 1.0F);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\Lightmap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */