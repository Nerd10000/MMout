/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ import net.minecraft.network.chat.Style;
/*    */ 
/*    */ public final class EmptyArea extends Record implements ActiveArea {
/*    */   private final float x;
/*    */   
/*    */   private final float y;
/*    */   
/*    */   private final float advance;
/*    */   
/*    */   private final float ascent;
/*    */   
/*    */   private final float height;
/*    */   
/*    */   private final Style style;
/*    */   
/*    */   public static final float DEFAULT_HEIGHT = 9.0F;
/*    */   
/*    */   public static final float DEFAULT_ASCENT = 7.0F;
/*    */   
/*    */   public EmptyArea(float x, float y, float advance, float ascent, float height, Style style) {
/*  6 */     this.x = x;
/*  6 */     this.y = y;
/*  6 */     this.advance = advance;
/*  6 */     this.ascent = ascent;
/*  6 */     this.height = height;
/*  6 */     this.style = style;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/EmptyArea;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/EmptyArea;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/EmptyArea;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/EmptyArea;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/EmptyArea;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/font/EmptyArea;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float x() {
/*  6 */     return this.x;
/*    */   }
/*    */   
/*    */   public float y() {
/*  6 */     return this.y;
/*    */   }
/*    */   
/*    */   public float advance() {
/*  6 */     return this.advance;
/*    */   }
/*    */   
/*    */   public float ascent() {
/*  6 */     return this.ascent;
/*    */   }
/*    */   
/*    */   public float height() {
/*  6 */     return this.height;
/*    */   }
/*    */   
/*    */   public Style style() {
/*  6 */     return this.style;
/*    */   }
/*    */   
/*    */   public float activeLeft() {
/* 19 */     return this.x;
/*    */   }
/*    */   
/*    */   public float activeTop() {
/* 24 */     return this.y + 7.0F - this.ascent;
/*    */   }
/*    */   
/*    */   public float activeRight() {
/* 29 */     return this.x + this.advance;
/*    */   }
/*    */   
/*    */   public float activeBottom() {
/* 34 */     return activeTop() + this.height;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\EmptyArea.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */