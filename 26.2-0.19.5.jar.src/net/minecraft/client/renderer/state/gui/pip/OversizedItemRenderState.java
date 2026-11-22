/*    */ package net.minecraft.client.renderer.state.gui.pip;
/*    */ 
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.renderer.state.gui.GuiItemRenderState;
/*    */ import org.joml.Matrix3x2fc;
/*    */ 
/*    */ public final class OversizedItemRenderState extends Record implements PictureInPictureRenderState {
/*    */   private final GuiItemRenderState guiItemRenderState;
/*    */   
/*    */   private final int x0;
/*    */   
/*    */   private final int y0;
/*    */   
/*    */   private final int x1;
/*    */   
/*    */   private final int y1;
/*    */   
/*    */   public OversizedItemRenderState(GuiItemRenderState guiItemRenderState, int x0, int y0, int x1, int y1) {
/* 10 */     this.guiItemRenderState = guiItemRenderState;
/* 10 */     this.x0 = x0;
/* 10 */     this.y0 = y0;
/* 10 */     this.x1 = x1;
/* 10 */     this.y1 = y1;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/state/gui/pip/OversizedItemRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/gui/pip/OversizedItemRenderState;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/state/gui/pip/OversizedItemRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/gui/pip/OversizedItemRenderState;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/state/gui/pip/OversizedItemRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/state/gui/pip/OversizedItemRenderState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public GuiItemRenderState guiItemRenderState() {
/* 10 */     return this.guiItemRenderState;
/*    */   }
/*    */   
/*    */   public int x0() {
/* 10 */     return this.x0;
/*    */   }
/*    */   
/*    */   public int y0() {
/* 10 */     return this.y0;
/*    */   }
/*    */   
/*    */   public int x1() {
/* 10 */     return this.x1;
/*    */   }
/*    */   
/*    */   public int y1() {
/* 10 */     return this.y1;
/*    */   }
/*    */   
/*    */   public float scale() {
/* 13 */     return 16.0F;
/*    */   }
/*    */   
/*    */   public Matrix3x2fc pose() {
/* 18 */     return (Matrix3x2fc)this.guiItemRenderState.pose();
/*    */   }
/*    */   
/*    */   public ScreenRectangle scissorArea() {
/* 23 */     return this.guiItemRenderState.scissorArea();
/*    */   }
/*    */   
/*    */   public ScreenRectangle bounds() {
/* 28 */     return this.guiItemRenderState.bounds();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\gui\pip\OversizedItemRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */