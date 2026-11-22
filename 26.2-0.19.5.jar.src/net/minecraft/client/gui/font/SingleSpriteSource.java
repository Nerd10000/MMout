/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ import net.minecraft.client.gui.GlyphSource;
/*    */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public final class SingleSpriteSource extends Record implements GlyphSource {
/*    */   private final BakedGlyph glyph;
/*    */   
/*    */   public SingleSpriteSource(BakedGlyph glyph) {
/*  7 */     this.glyph = glyph;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/SingleSpriteSource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/SingleSpriteSource;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/SingleSpriteSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/SingleSpriteSource;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/SingleSpriteSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/font/SingleSpriteSource;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BakedGlyph glyph() {
/*  7 */     return this.glyph;
/*    */   }
/*    */   
/*    */   public BakedGlyph getGlyph(int codepoint) {
/* 13 */     return this.glyph;
/*    */   }
/*    */   
/*    */   public BakedGlyph getRandomGlyph(RandomSource random, int width) {
/* 18 */     return this.glyph;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\SingleSpriteSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */