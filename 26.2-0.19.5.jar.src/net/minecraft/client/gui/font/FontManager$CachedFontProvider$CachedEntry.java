/*     */ package net.minecraft.client.gui.font;
/*     */ 
/*     */ import net.minecraft.client.gui.GlyphSource;
/*     */ import net.minecraft.network.chat.FontDescription;
/*     */ 
/*     */ final class CachedEntry extends Record {
/*     */   private final FontDescription description;
/*     */   
/*     */   private final GlyphSource source;
/*     */   
/*     */   private CachedEntry(FontDescription description, GlyphSource source) {
/* 372 */     this.description = description;
/* 372 */     this.source = source;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #372	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #372	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #372	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public FontDescription description() {
/* 372 */     return this.description;
/*     */   }
/*     */   
/*     */   public GlyphSource source() {
/* 372 */     return this.source;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\FontManager$CachedFontProvider$CachedEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */