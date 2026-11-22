/*    */ package net.minecraft.client.renderer.state.gui.pip;
/*    */ 
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import org.joml.Quaternionfc;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public final class GuiEntityRenderState extends Record implements PictureInPictureRenderState {
/*    */   private final EntityRenderState renderState;
/*    */   
/*    */   private final Vector3fc translation;
/*    */   
/*    */   private final Quaternionfc rotation;
/*    */   
/*    */   private final Quaternionfc overrideCameraAngle;
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
/*  9 */     return this.bounds;
/*    */   }
/*    */   
/*    */   public ScreenRectangle scissorArea() {
/*  9 */     return this.scissorArea;
/*    */   }
/*    */   
/*    */   public float scale() {
/*  9 */     return this.scale;
/*    */   }
/*    */   
/*    */   public int y1() {
/*  9 */     return this.y1;
/*    */   }
/*    */   
/*    */   public int x1() {
/*  9 */     return this.x1;
/*    */   }
/*    */   
/*    */   public int y0() {
/*  9 */     return this.y0;
/*    */   }
/*    */   
/*    */   public int x0() {
/*  9 */     return this.x0;
/*    */   }
/*    */   
/*    */   public Quaternionfc overrideCameraAngle() {
/*  9 */     return this.overrideCameraAngle;
/*    */   }
/*    */   
/*    */   public Quaternionfc rotation() {
/*  9 */     return this.rotation;
/*    */   }
/*    */   
/*    */   public Vector3fc translation() {
/*  9 */     return this.translation;
/*    */   }
/*    */   
/*    */   public EntityRenderState renderState() {
/*  9 */     return this.renderState;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/state/gui/pip/GuiEntityRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/state/gui/pip/GuiEntityRenderState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/state/gui/pip/GuiEntityRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/gui/pip/GuiEntityRenderState;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/state/gui/pip/GuiEntityRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/gui/pip/GuiEntityRenderState;
/*    */   }
/*    */   
/*    */   public GuiEntityRenderState(EntityRenderState renderState, Vector3fc translation, Quaternionfc rotation, Quaternionfc overrideCameraAngle, int x0, int y0, int x1, int y1, float scale, ScreenRectangle scissorArea, ScreenRectangle bounds) {
/*  9 */     this.renderState = renderState;
/*  9 */     this.translation = translation;
/*  9 */     this.rotation = rotation;
/*  9 */     this.overrideCameraAngle = overrideCameraAngle;
/*  9 */     this.x0 = x0;
/*  9 */     this.y0 = y0;
/*  9 */     this.x1 = x1;
/*  9 */     this.y1 = y1;
/*  9 */     this.scale = scale;
/*  9 */     this.scissorArea = scissorArea;
/*  9 */     this.bounds = bounds;
/*    */   }
/*    */   
/*    */   public GuiEntityRenderState(EntityRenderState renderState, Vector3fc translation, Quaternionfc rotation, Quaternionfc overrideCameraAngle, int x0, int y0, int x1, int y1, float scale, ScreenRectangle scissorArea) {
/* 12 */     this(renderState, translation, rotation, overrideCameraAngle, x0, y0, x1, y1, scale, scissorArea, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\gui\pip\GuiEntityRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */