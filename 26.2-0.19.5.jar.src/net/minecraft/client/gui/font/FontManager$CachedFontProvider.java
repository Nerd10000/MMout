/*     */ package net.minecraft.client.gui.font;
/*     */ 
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GlyphSource;
/*     */ import net.minecraft.client.gui.font.glyphs.EffectGlyph;
/*     */ import net.minecraft.network.chat.FontDescription;
/*     */ 
/*     */ class CachedFontProvider implements Font.Provider, AutoCloseable {
/*     */   private final boolean nonFishyOnly;
/*     */   
/*     */   private volatile CachedEntry lastEntry;
/*     */   
/*     */   private volatile EffectGlyph whiteGlyph;
/*     */   
/*     */   private static final class CachedEntry extends Record {
/*     */     private final FontDescription description;
/*     */     
/*     */     private final GlyphSource source;
/*     */     
/*     */     private CachedEntry(FontDescription description, GlyphSource source) {
/* 372 */       this.description = description;
/* 372 */       this.source = source;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #372	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #372	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #372	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public FontDescription description() {
/* 372 */       return this.description;
/*     */     }
/*     */     
/*     */     public GlyphSource source() {
/* 372 */       return this.source;
/*     */     }
/*     */   }
/*     */   
/*     */   private CachedFontProvider(boolean nonFishyOnly) {
/* 383 */     this.nonFishyOnly = nonFishyOnly;
/*     */   }
/*     */   
/*     */   public void invalidate() {
/* 387 */     this.lastEntry = null;
/* 388 */     this.whiteGlyph = null;
/*     */   }
/*     */   
/*     */   public void close() {
/* 393 */     invalidate();
/*     */   }
/*     */   
/*     */   private GlyphSource getGlyphSource(FontDescription description) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore_2
/*     */     //   7: iconst_0
/*     */     //   8: istore_3
/*     */     //   9: aload_2
/*     */     //   10: iload_3
/*     */     //   11: <illegal opcode> typeSwitch : (Lnet/minecraft/network/chat/FontDescription;I)I
/*     */     //   16: tableswitch default -> 111, 0 -> 44, 1 -> 72, 2 -> 90
/*     */     //   44: aload_2
/*     */     //   45: checkcast net/minecraft/network/chat/FontDescription$Resource
/*     */     //   48: astore #4
/*     */     //   50: aload_0
/*     */     //   51: getfield this$0 : Lnet/minecraft/client/gui/font/FontManager;
/*     */     //   54: aload #4
/*     */     //   56: invokevirtual id : ()Lnet/minecraft/resources/Identifier;
/*     */     //   59: invokevirtual getFontSetRaw : (Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/gui/font/FontSet;
/*     */     //   62: aload_0
/*     */     //   63: getfield nonFishyOnly : Z
/*     */     //   66: invokevirtual source : (Z)Lnet/minecraft/client/gui/GlyphSource;
/*     */     //   69: goto -> 125
/*     */     //   72: aload_2
/*     */     //   73: checkcast net/minecraft/network/chat/FontDescription$AtlasSprite
/*     */     //   76: astore #5
/*     */     //   78: aload_0
/*     */     //   79: getfield this$0 : Lnet/minecraft/client/gui/font/FontManager;
/*     */     //   82: aload #5
/*     */     //   84: invokevirtual getSpriteFont : (Lnet/minecraft/network/chat/FontDescription$AtlasSprite;)Lnet/minecraft/client/gui/GlyphSource;
/*     */     //   87: goto -> 125
/*     */     //   90: aload_2
/*     */     //   91: checkcast net/minecraft/network/chat/FontDescription$PlayerSprite
/*     */     //   94: astore #6
/*     */     //   96: aload_0
/*     */     //   97: getfield this$0 : Lnet/minecraft/client/gui/font/FontManager;
/*     */     //   100: getfield playerProvider : Lnet/minecraft/client/gui/font/PlayerGlyphProvider;
/*     */     //   103: aload #6
/*     */     //   105: invokevirtual sourceForPlayer : (Lnet/minecraft/network/chat/FontDescription$PlayerSprite;)Lnet/minecraft/client/gui/GlyphSource;
/*     */     //   108: goto -> 125
/*     */     //   111: aload_0
/*     */     //   112: getfield this$0 : Lnet/minecraft/client/gui/font/FontManager;
/*     */     //   115: getfield missingFontSet : Lnet/minecraft/client/gui/font/FontSet;
/*     */     //   118: aload_0
/*     */     //   119: getfield nonFishyOnly : Z
/*     */     //   122: invokevirtual source : (Z)Lnet/minecraft/client/gui/GlyphSource;
/*     */     //   125: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #397	-> 0
/*     */     //   #398	-> 44
/*     */     //   #399	-> 72
/*     */     //   #400	-> 90
/*     */     //   #401	-> 111
/*     */     //   #397	-> 125
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   50	22	4	resource	Lnet/minecraft/network/chat/FontDescription$Resource;
/*     */     //   78	12	5	sprite	Lnet/minecraft/network/chat/FontDescription$AtlasSprite;
/*     */     //   96	15	6	player	Lnet/minecraft/network/chat/FontDescription$PlayerSprite;
/*     */     //   0	126	0	this	Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider;
/*     */     //   0	126	1	description	Lnet/minecraft/network/chat/FontDescription;
/*     */   }
/*     */   
/*     */   public GlyphSource glyphs(FontDescription description) {
/* 407 */     CachedEntry lastEntry = this.lastEntry;
/* 408 */     if (lastEntry != null && description.equals(lastEntry.description))
/* 409 */       return lastEntry.source; 
/* 411 */     GlyphSource result = getGlyphSource(description);
/* 413 */     this.lastEntry = new CachedEntry(description, result);
/* 414 */     return result;
/*     */   }
/*     */   
/*     */   public EffectGlyph effect() {
/* 419 */     EffectGlyph whiteGlyph = this.whiteGlyph;
/* 420 */     if (whiteGlyph == null) {
/* 421 */       whiteGlyph = FontManager.this.getFontSetRaw(FontDescription.DEFAULT.id()).whiteGlyph();
/* 422 */       this.whiteGlyph = whiteGlyph;
/*     */     } 
/* 424 */     return whiteGlyph;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\FontManager$CachedFontProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */