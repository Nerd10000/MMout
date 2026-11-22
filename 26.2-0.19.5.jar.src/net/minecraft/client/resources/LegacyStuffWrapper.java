/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ 
/*    */ public class LegacyStuffWrapper {
/*    */   @Deprecated
/*    */   public static int[] getPixels(ResourceManager resourceManager, Identifier location) throws IOException {
/* 14 */     InputStream resource = resourceManager.open(location);
/*    */     try {
/* 15 */       NativeImage image = NativeImage.read(resource);
/*    */       try {
/* 17 */         int[] arrayOfInt = image.makePixelArray();
/* 18 */         if (image != null)
/* 18 */           image.close(); 
/* 18 */         if (resource != null)
/* 18 */           resource.close(); 
/*    */         return arrayOfInt;
/*    */       } catch (Throwable throwable) {
/*    */         if (image != null)
/*    */           try {
/*    */             image.close();
/*    */           } catch (Throwable throwable1) {
/*    */             throwable.addSuppressed(throwable1);
/*    */           }  
/*    */         throw throwable;
/*    */       } 
/*    */     } catch (Throwable throwable) {
/*    */       if (resource != null)
/*    */         try {
/*    */           resource.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\LegacyStuffWrapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */