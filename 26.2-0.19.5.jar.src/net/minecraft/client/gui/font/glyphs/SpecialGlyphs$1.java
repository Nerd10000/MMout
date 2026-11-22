/*    */ package net.minecraft.client.gui.font.glyphs;
/*    */ 
/*    */ import com.mojang.blaze3d.font.GlyphBitmap;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.textures.GpuTexture;
/*    */ 
/*    */ class null implements GlyphBitmap {
/*    */   public int getPixelWidth() {
/* 60 */     return SpecialGlyphs.this.image.getWidth();
/*    */   }
/*    */   
/*    */   public int getPixelHeight() {
/* 65 */     return SpecialGlyphs.this.image.getHeight();
/*    */   }
/*    */   
/*    */   public float getOversample() {
/* 70 */     return 1.0F;
/*    */   }
/*    */   
/*    */   public void upload(int x, int y, GpuTexture texture) {
/* 75 */     RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, SpecialGlyphs.this.image, 0, 0, x, y);
/*    */   }
/*    */   
/*    */   public boolean isColored() {
/* 80 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\glyphs\SpecialGlyphs$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */