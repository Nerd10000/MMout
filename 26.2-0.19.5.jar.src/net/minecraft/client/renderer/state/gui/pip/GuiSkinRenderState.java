/*    */ package net.minecraft.client.renderer.state.gui.pip;
/*    */ 
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class GuiSkinRenderState extends Record implements PictureInPictureRenderState {
/*    */   private final Model.Simple playerModel;
/*    */   
/*    */   private final Identifier texture;
/*    */   
/*    */   private final float rotationX;
/*    */   
/*    */   private final float rotationY;
/*    */   
/*    */   private final float pivotY;
/*    */   
/*    */   private final int x0;
/*    */   
/*    */   private final int y0;
/*    */   
/*    */   private final int x1;
/*    */   
/*    */   private final int y1;
/*    */   
/*    */   private final float scale;
/*    */   
/*    */   private final ScreenRectangle scissorArea;
/*    */   
/*    */   private final ScreenRectangle bounds;
/*    */   
/*    */   public ScreenRectangle bounds() {
/*  8 */     return this.bounds;
/*    */   }
/*    */   
/*    */   public ScreenRectangle scissorArea() {
/*  8 */     return this.scissorArea;
/*    */   }
/*    */   
/*    */   public float scale() {
/*  8 */     return this.scale;
/*    */   }
/*    */   
/*    */   public int y1() {
/*  8 */     return this.y1;
/*    */   }
/*    */   
/*    */   public int x1() {
/*  8 */     return this.x1;
/*    */   }
/*    */   
/*    */   public int y0() {
/*  8 */     return this.y0;
/*    */   }
/*    */   
/*    */   public int x0() {
/*  8 */     return this.x0;
/*    */   }
/*    */   
/*    */   public float pivotY() {
/*  8 */     return this.pivotY;
/*    */   }
/*    */   
/*    */   public float rotationY() {
/*  8 */     return this.rotationY;
/*    */   }
/*    */   
/*    */   public float rotationX() {
/*  8 */     return this.rotationX;
/*    */   }
/*    */   
/*    */   public Identifier texture() {
/*  8 */     return this.texture;
/*    */   }
/*    */   
/*    */   public Model.Simple playerModel() {
/*  8 */     return this.playerModel;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;
/*    */   }
/*    */   
/*    */   public GuiSkinRenderState(Model.Simple playerModel, Identifier texture, float rotationX, float rotationY, float pivotY, int x0, int y0, int x1, int y1, float scale, ScreenRectangle scissorArea, ScreenRectangle bounds) {
/*  8 */     this.playerModel = playerModel;
/*  8 */     this.texture = texture;
/*  8 */     this.rotationX = rotationX;
/*  8 */     this.rotationY = rotationY;
/*  8 */     this.pivotY = pivotY;
/*  8 */     this.x0 = x0;
/*  8 */     this.y0 = y0;
/*  8 */     this.x1 = x1;
/*  8 */     this.y1 = y1;
/*  8 */     this.scale = scale;
/*  8 */     this.scissorArea = scissorArea;
/*  8 */     this.bounds = bounds;
/*    */   }
/*    */   
/*    */   public GuiSkinRenderState(Model.Simple playerModel, Identifier texture, float rotationX, float rotationY, float pivotY, int x0, int y0, int x1, int y1, float scale, ScreenRectangle scissorArea) {
/* 11 */     this(playerModel, texture, rotationX, rotationY, pivotY, x0, y0, x1, y1, scale, scissorArea, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\gui\pip\GuiSkinRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */