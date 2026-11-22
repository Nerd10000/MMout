/*    */ package net.minecraft.client.renderer.state.gui.pip;
/*    */ 
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.model.object.book.BookModel;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class GuiBookModelRenderState extends Record implements PictureInPictureRenderState {
/*    */   private final BookModel bookModel;
/*    */   
/*    */   private final Identifier texture;
/*    */   
/*    */   private final float open;
/*    */   
/*    */   private final float flip;
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
/*    */   public float flip() {
/*  8 */     return this.flip;
/*    */   }
/*    */   
/*    */   public float open() {
/*  8 */     return this.open;
/*    */   }
/*    */   
/*    */   public Identifier texture() {
/*  8 */     return this.texture;
/*    */   }
/*    */   
/*    */   public BookModel bookModel() {
/*  8 */     return this.bookModel;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/state/gui/pip/GuiBookModelRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/state/gui/pip/GuiBookModelRenderState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/state/gui/pip/GuiBookModelRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/gui/pip/GuiBookModelRenderState;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/state/gui/pip/GuiBookModelRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/gui/pip/GuiBookModelRenderState;
/*    */   }
/*    */   
/*    */   public GuiBookModelRenderState(BookModel bookModel, Identifier texture, float open, float flip, int x0, int y0, int x1, int y1, float scale, ScreenRectangle scissorArea, ScreenRectangle bounds) {
/*  8 */     this.bookModel = bookModel;
/*  8 */     this.texture = texture;
/*  8 */     this.open = open;
/*  8 */     this.flip = flip;
/*  8 */     this.x0 = x0;
/*  8 */     this.y0 = y0;
/*  8 */     this.x1 = x1;
/*  8 */     this.y1 = y1;
/*  8 */     this.scale = scale;
/*  8 */     this.scissorArea = scissorArea;
/*  8 */     this.bounds = bounds;
/*    */   }
/*    */   
/*    */   public GuiBookModelRenderState(BookModel bookModel, Identifier texture, float open, float flip, int x0, int y0, int x1, int y1, float scale, ScreenRectangle scissorArea) {
/* 11 */     this(bookModel, texture, open, flip, x0, y0, x1, y1, scale, scissorArea, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\gui\pip\GuiBookModelRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */