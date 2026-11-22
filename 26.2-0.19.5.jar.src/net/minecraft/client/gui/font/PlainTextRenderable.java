/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public interface PlainTextRenderable extends TextRenderable.Styled {
/*    */   public static final float DEFAULT_WIDTH = 8.0F;
/*    */   
/*    */   public static final float DEFAULT_HEIGHT = 8.0F;
/*    */   
/*    */   public static final float DEFUAULT_ASCENT = 8.0F;
/*    */   
/*    */   default void render(Matrix4fc pose, VertexConsumer buffer, int packedLightCoords, boolean flat) {
/* 18 */     float frontDepth = 0.0F;
/* 19 */     if (shadowColor() != 0) {
/* 20 */       renderSprite(pose, buffer, packedLightCoords, shadowOffset(), shadowOffset(), 0.0F, shadowColor());
/* 21 */       if (!flat)
/* 22 */         frontDepth += 0.03F; 
/*    */     } 
/* 25 */     renderSprite(pose, buffer, packedLightCoords, 0.0F, 0.0F, frontDepth, color());
/*    */   }
/*    */   
/*    */   void renderSprite(Matrix4fc paramMatrix4fc, VertexConsumer paramVertexConsumer, int paramInt1, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt2);
/*    */   
/*    */   float x();
/*    */   
/*    */   float y();
/*    */   
/*    */   int color();
/*    */   
/*    */   int shadowColor();
/*    */   
/*    */   float shadowOffset();
/*    */   
/*    */   default float width() {
/* 41 */     return 8.0F;
/*    */   }
/*    */   
/*    */   default float height() {
/* 45 */     return 8.0F;
/*    */   }
/*    */   
/*    */   default float ascent() {
/* 49 */     return 8.0F;
/*    */   }
/*    */   
/*    */   default float left() {
/* 54 */     return x();
/*    */   }
/*    */   
/*    */   default float right() {
/* 59 */     return left() + width();
/*    */   }
/*    */   
/*    */   default float top() {
/* 64 */     return y() + 7.0F - ascent();
/*    */   }
/*    */   
/*    */   default float bottom() {
/* 69 */     return activeTop() + height();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\PlainTextRenderable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */