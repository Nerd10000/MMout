/*     */ package net.minecraft.client.gui.font;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*     */ 
/*     */ final class SelectedGlyphs extends Record {
/*     */   private final Supplier<BakedGlyph> any;
/*     */   
/*     */   private final Supplier<BakedGlyph> nonFishy;
/*     */   
/*     */   private SelectedGlyphs(Supplier<BakedGlyph> any, Supplier<BakedGlyph> nonFishy) {
/* 215 */     this.any = any;
/* 215 */     this.nonFishy = nonFishy;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/FontSet$SelectedGlyphs;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #215	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/FontSet$SelectedGlyphs;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/FontSet$SelectedGlyphs;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #215	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/FontSet$SelectedGlyphs;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/FontSet$SelectedGlyphs;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #215	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/font/FontSet$SelectedGlyphs;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Supplier<BakedGlyph> any() {
/* 215 */     return this.any;
/*     */   }
/*     */   
/*     */   public Supplier<BakedGlyph> nonFishy() {
/* 215 */     return this.nonFishy;
/*     */   }
/*     */   
/*     */   private Supplier<BakedGlyph> select(boolean filterFishy) {
/* 217 */     return filterFishy ? this.nonFishy : this.any;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\FontSet$SelectedGlyphs.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */