/*    */ package net.minecraft.client.renderer.texture.atlas.sources;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.Resource;
/*    */ 
/*    */ public class LazyLoadedImage {
/*    */   private final Identifier id;
/*    */   
/*    */   private final Resource resource;
/*    */   
/* 16 */   private final AtomicReference<NativeImage> image = new AtomicReference<>();
/*    */   
/*    */   private final AtomicInteger referenceCount;
/*    */   
/*    */   public LazyLoadedImage(Identifier id, Resource resource, int count) {
/* 20 */     this.id = id;
/* 21 */     this.resource = resource;
/* 22 */     this.referenceCount = new AtomicInteger(count);
/*    */   }
/*    */   
/*    */   public NativeImage get() throws IOException {
/* 26 */     NativeImage nativeImage = this.image.get();
/* 27 */     if (nativeImage == null)
/* 28 */       synchronized (this) {
/* 29 */         nativeImage = this.image.get();
/* 30 */         if (nativeImage == null)
/*    */           try {
/* 31 */             InputStream stream = this.resource.open();
/*    */             try {
/* 32 */               nativeImage = NativeImage.read(stream);
/* 33 */               this.image.set(nativeImage);
/* 34 */               if (stream != null)
/* 34 */                 stream.close(); 
/*    */             } catch (Throwable throwable) {
/*    */               if (stream != null)
/*    */                 try {
/*    */                   stream.close();
/*    */                 } catch (Throwable throwable1) {
/*    */                   throwable.addSuppressed(throwable1);
/*    */                 }  
/*    */               throw throwable;
/*    */             } 
/* 34 */           } catch (IOException e) {
/* 35 */             throw new IOException("Failed to load image " + String.valueOf(this.id), e);
/*    */           }  
/*    */       }  
/* 41 */     return nativeImage;
/*    */   }
/*    */   
/*    */   public void release() {
/* 45 */     int references = this.referenceCount.decrementAndGet();
/* 46 */     if (references <= 0) {
/* 47 */       NativeImage nativeImage = this.image.getAndSet(null);
/* 48 */       if (nativeImage != null)
/* 49 */         nativeImage.close(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\atlas\sources\LazyLoadedImage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */