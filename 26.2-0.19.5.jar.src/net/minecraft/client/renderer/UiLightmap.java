/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*    */ 
/*    */ public class UiLightmap implements AutoCloseable {
/* 12 */   private final DynamicTexture texture = new DynamicTexture("UI Lightmap", 1, 1, false);
/*    */   
/*    */   public UiLightmap() {
/* 13 */     NativeImage pixels = this.texture.getPixels();
/* 14 */     pixels.setPixel(0, 0, -1);
/* 15 */     this.texture.upload();
/*    */   }
/*    */   
/*    */   public GpuTextureView getTextureView() {
/* 19 */     return this.texture.getTextureView();
/*    */   }
/*    */   
/*    */   public void close() {
/* 24 */     this.texture.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\UiLightmap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */