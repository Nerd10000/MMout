/*    */ package net.minecraft.client.renderer.state.gui.pip;
/*    */ 
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.renderer.state.gui.ScreenArea;
/*    */ import org.joml.Matrix3x2f;
/*    */ import org.joml.Matrix3x2fc;
/*    */ 
/*    */ public interface PictureInPictureRenderState extends ScreenArea {
/* 10 */   public static final Matrix3x2fc IDENTITY_POSE = (Matrix3x2fc)new Matrix3x2f();
/*    */   
/*    */   int x0();
/*    */   
/*    */   int x1();
/*    */   
/*    */   int y0();
/*    */   
/*    */   int y1();
/*    */   
/*    */   float scale();
/*    */   
/*    */   default Matrix3x2fc pose() {
/* 23 */     return IDENTITY_POSE;
/*    */   }
/*    */   
/*    */   ScreenRectangle scissorArea();
/*    */   
/*    */   static ScreenRectangle getBounds(int x0, int y0, int x1, int y1, ScreenRectangle scissorArea) {
/* 29 */     ScreenRectangle bounds = new ScreenRectangle(x0, y0, x1 - x0, y1 - y0);
/* 30 */     return (scissorArea != null) ? scissorArea.intersection(bounds) : bounds;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\gui\pip\PictureInPictureRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */