/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import com.mojang.blaze3d.GpuFormat;
/*    */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import com.mojang.blaze3d.systems.GpuDevice;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import java.io.IOException;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ 
/*    */ public class CubeMapTexture extends ReloadableTexture {
/* 19 */   private static final String[] SUFFIXES = new String[] { "_1.png", "_3.png", "_5.png", "_4.png", "_0.png", "_2.png" };
/*    */   
/*    */   public CubeMapTexture(Identifier resourceId) {
/* 22 */     super(resourceId);
/*    */   }
/*    */   
/*    */   public TextureContents loadContents(ResourceManager resourceManager) throws IOException {
/* 27 */     Identifier location = resourceId();
/* 28 */     TextureContents first = TextureContents.load(resourceManager, location.withSuffix(SUFFIXES[0]));
/*    */     try {
/* 29 */       int width = first.image().getWidth();
/* 30 */       int height = first.image().getHeight();
/* 31 */       NativeImage stackedImage = new NativeImage(width, height * 6, false);
/* 32 */       first.image().copyRect(stackedImage, 0, 0, 0, 0, width, height, false, true);
/* 33 */       for (int i = 1; i < 6; i++) {
/* 34 */         TextureContents part = TextureContents.load(resourceManager, location.withSuffix(SUFFIXES[i]));
/*    */         try {
/* 35 */           if (part.image().getWidth() != width || part.image().getHeight() != height)
/* 36 */             throw new IOException("Image dimensions of cubemap '" + String.valueOf(location) + "' sides do not match: part 0 is " + width + "x" + height + ", but part " + i + " is " + part.image().getWidth() + "x" + part.image().getHeight()); 
/* 38 */           part.image().copyRect(stackedImage, 0, 0, 0, i * height, width, height, false, true);
/* 39 */           if (part != null)
/* 39 */             part.close(); 
/*    */         } catch (Throwable throwable) {
/*    */           if (part != null)
/*    */             try {
/*    */               part.close();
/*    */             } catch (Throwable throwable1) {
/*    */               throwable.addSuppressed(throwable1);
/*    */             }  
/*    */           throw throwable;
/*    */         } 
/*    */       } 
/* 41 */       TextureContents textureContents = new TextureContents(stackedImage, new TextureMetadataSection(true, false, MipmapStrategy.MEAN, 0.0F));
/* 42 */       if (first != null)
/* 42 */         first.close(); 
/*    */       return textureContents;
/*    */     } catch (Throwable throwable) {
/*    */       if (first != null)
/*    */         try {
/*    */           first.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void doLoad(NativeImage image) {
/* 47 */     GpuDevice device = RenderSystem.getDevice();
/* 48 */     int width = image.getWidth();
/* 49 */     int height = image.getHeight() / 6;
/* 50 */     close();
/* 51 */     Objects.requireNonNull(resourceId());
/* 51 */     this.texture = device.createTexture(resourceId()::toString, 21, GpuFormat.RGBA8_UNORM, width, height, 6, 1);
/* 52 */     this.textureView = device.createTextureView(this.texture);
/* 53 */     GpuBufferSlice stagingBuffer = device.createCommandEncoder().transientMemory().uploadStaging(image.getPixelBytes(), 1L, 16);
/* 54 */     for (int i = 0; i < 6; i++)
/* 55 */       device.createCommandEncoder().copyBufferToTexture(stagingBuffer, 0, height * i, image.getWidth(), image.getHeight(), this.texture, 0, 0, width, height, 0, i); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\CubeMapTexture.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */