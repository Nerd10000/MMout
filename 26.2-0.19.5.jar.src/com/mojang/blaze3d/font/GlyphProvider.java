/*    */ package com.mojang.blaze3d.font;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntSet;
/*    */ import net.minecraft.client.gui.font.FontOption;
/*    */ 
/*    */ public interface GlyphProvider extends AutoCloseable {
/*    */   public static final float BASELINE = 7.0F;
/*    */   
/*    */   default void close() {}
/*    */   
/*    */   default UnbakedGlyph getGlyph(int codepoint) {
/* 20 */     return null;
/*    */   }
/*    */   
/*    */   IntSet getSupportedGlyphs();
/*    */   
/*    */   public static final class Conditional extends Record implements AutoCloseable {
/*    */     private final GlyphProvider provider;
/*    */     
/*    */     private final FontOption.Filter filter;
/*    */     
/*    */     public Conditional(GlyphProvider provider, FontOption.Filter filter) {
/* 25 */       this.provider = provider;
/* 25 */       this.filter = filter;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/font/GlyphProvider$Conditional;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/font/GlyphProvider$Conditional;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/font/GlyphProvider$Conditional;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/font/GlyphProvider$Conditional;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/font/GlyphProvider$Conditional;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/font/GlyphProvider$Conditional;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public GlyphProvider provider() {
/* 25 */       return this.provider;
/*    */     }
/*    */     
/*    */     public FontOption.Filter filter() {
/* 25 */       return this.filter;
/*    */     }
/*    */     
/*    */     public void close() {
/* 31 */       this.provider.close();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\font\GlyphProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */