/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import com.mojang.blaze3d.GpuFormat;
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import com.mojang.blaze3d.systems.GpuDevice;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.textures.FilterMode;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DynamicTexture extends AbstractTexture implements Dumpable {
/* 18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private NativeImage pixels;
/*    */   
/*    */   public DynamicTexture(Supplier<String> label, NativeImage image) {
/* 22 */     this.pixels = image;
/* 23 */     createTexture(label);
/* 24 */     upload();
/*    */   }
/*    */   
/*    */   public DynamicTexture(String label, int width, int height, boolean zero) {
/* 28 */     this.pixels = new NativeImage(width, height, zero);
/* 29 */     createTexture(label);
/*    */   }
/*    */   
/*    */   public DynamicTexture(Supplier<String> label, int width, int height, boolean zero) {
/* 33 */     this.pixels = new NativeImage(width, height, zero);
/* 34 */     createTexture(label);
/*    */   }
/*    */   
/*    */   private void createTexture(Supplier<String> label) {
/* 38 */     GpuDevice device = RenderSystem.getDevice();
/* 39 */     this.texture = device.createTexture(label, 5, GpuFormat.RGBA8_UNORM, this.pixels.getWidth(), this.pixels.getHeight(), 1, 1);
/* 40 */     this.sampler = RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST);
/* 41 */     this.textureView = device.createTextureView(this.texture);
/*    */   }
/*    */   
/*    */   private void createTexture(String label) {
/* 45 */     GpuDevice device = RenderSystem.getDevice();
/* 46 */     this.texture = device.createTexture(label, 5, GpuFormat.RGBA8_UNORM, this.pixels.getWidth(), this.pixels.getHeight(), 1, 1);
/* 47 */     this.sampler = RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST);
/* 48 */     this.textureView = device.createTextureView(this.texture);
/*    */   }
/*    */   
/*    */   public void upload() {
/* 52 */     if (this.texture != null) {
/* 53 */       RenderSystem.getDevice().createCommandEncoder().writeToTexture(this.texture, this.pixels);
/*    */     } else {
/* 55 */       LOGGER.warn("Trying to upload disposed texture {}", getTexture().getLabel());
/*    */     } 
/*    */   }
/*    */   
/*    */   public NativeImage getPixels() {
/* 60 */     return this.pixels;
/*    */   }
/*    */   
/*    */   public void setPixels(NativeImage pixels) {
/* 64 */     this.pixels.close();
/* 65 */     this.pixels = pixels;
/*    */   }
/*    */   
/*    */   public void close() {
/* 70 */     this.pixels.close();
/* 71 */     super.close();
/*    */   }
/*    */   
/*    */   public void dumpContents(Identifier selfId, Path dir) throws IOException {
/* 76 */     if (!this.pixels.isClosed()) {
/* 77 */       String outputId = selfId.toDebugFileName() + ".png";
/* 78 */       Path path = dir.resolve(outputId);
/* 79 */       this.pixels.writeToFile(path);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\DynamicTexture.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */