/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.ibm.icu.text.ArabicShaping;
/*     */ import com.ibm.icu.text.ArabicShapingException;
/*     */ import com.ibm.icu.text.Bidi;
/*     */ import com.mojang.blaze3d.font.GlyphInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.StringSplitter;
/*     */ import net.minecraft.client.gui.font.EmptyArea;
/*     */ import net.minecraft.client.gui.font.TextRenderable;
/*     */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*     */ import net.minecraft.client.gui.font.glyphs.EffectGlyph;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.FontDescription;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.network.chat.TextColor;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.FormattedCharSink;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringDecomposer;
/*     */ 
/*     */ public class Font {
/*     */   private static final float EFFECT_DEPTH = 0.01F;
/*     */   
/*     */   private static final float OVER_EFFECT_DEPTH = 0.01F;
/*     */   
/*     */   private static final float UNDER_EFFECT_DEPTH = -0.01F;
/*     */   
/*     */   public static final float SHADOW_DEPTH = 0.03F;
/*     */   
/*  36 */   public final int lineHeight = 9;
/*     */   
/*  37 */   private final RandomSource random = RandomSource.create();
/*     */   
/*     */   private final Provider provider;
/*     */   
/*     */   private final StringSplitter splitter;
/*     */   
/*     */   public Font(Provider provider) {
/*  44 */     this.provider = provider;
/*  45 */     this.splitter = new StringSplitter((codepoint, style) -> getGlyphSource(style.getFont()).getGlyph(codepoint).info().getAdvance(style.isBold()));
/*     */   }
/*     */   
/*     */   private GlyphSource getGlyphSource(FontDescription fontLocation) {
/*  49 */     return this.provider.glyphs(fontLocation);
/*     */   }
/*     */   
/*     */   public String bidirectionalShaping(String text) {
/*     */     try {
/*  54 */       Bidi bidi = new Bidi((new ArabicShaping(8)).shape(text), 127);
/*  55 */       bidi.setReorderingMode(0);
/*  56 */       return bidi.writeReordered(2);
/*  57 */     } catch (ArabicShapingException arabicShapingException) {
/*  60 */       return text;
/*     */     } 
/*     */   }
/*     */   
/*     */   public PreparedText prepare8xTextOutline(FormattedCharSequence str, float x, float y, int outlineColor) {
/*  64 */     PreparedTextBuilder outlineOutput = new PreparedTextBuilder(0.0F, 0.0F, outlineColor, false, false);
/*  65 */     for (int xo = -1; xo <= 1; xo++) {
/*  66 */       for (int yo = -1; yo <= 1; yo++) {
/*  67 */         if (xo != 0 || yo != 0) {
/*  68 */           float[] startX = { x };
/*  69 */           int finalXo = xo;
/*  70 */           int finalYo = yo;
/*  71 */           str.accept((position, style, codepoint) -> {
/*     */                 boolean bold = style.isBold();
/*     */                 BakedGlyph glyph = getGlyph(codepoint, style);
/*     */                 outlineOutput.x = startX[0] + finalXo * glyph.info().getShadowOffset();
/*     */                 outlineOutput.y = y + finalYo * glyph.info().getShadowOffset();
/*     */                 startX[0] = startX[0] + glyph.info().getAdvance(bold);
/*     */                 return outlineOutput.accept(position, style.withColor(outlineColor), glyph);
/*     */               });
/*     */         } 
/*     */       } 
/*     */     } 
/*  84 */     outlineOutput.discardEffects();
/*  85 */     return outlineOutput;
/*     */   }
/*     */   
/*     */   public enum DisplayMode {
/*  89 */     NORMAL, SEE_THROUGH, POLYGON_OFFSET;
/*     */   }
/*     */   
/*     */   private class PreparedTextBuilder implements PreparedText, FormattedCharSink {
/*     */     private final boolean drawShadow;
/*     */     
/*     */     private final int color;
/*     */     
/*     */     private final int backgroundColor;
/*     */     
/*     */     private final boolean includeEmpty;
/*     */     
/*     */     private float x;
/*     */     
/*     */     private float y;
/*     */     
/* 109 */     private float left = Float.MAX_VALUE;
/*     */     
/* 110 */     private float top = Float.MAX_VALUE;
/*     */     
/* 111 */     private float right = -3.4028235E38F;
/*     */     
/* 112 */     private float bottom = -3.4028235E38F;
/*     */     
/* 114 */     private float backgroundLeft = Float.MAX_VALUE;
/*     */     
/* 115 */     private float backgroundTop = Float.MAX_VALUE;
/*     */     
/* 116 */     private float backgroundRight = -3.4028235E38F;
/*     */     
/* 117 */     private float backgroundBottom = -3.4028235E38F;
/*     */     
/* 119 */     private final List<TextRenderable.Styled> glyphs = new ArrayList<>();
/*     */     
/*     */     private List<TextRenderable> effects;
/*     */     
/*     */     private List<EmptyArea> emptyAreas;
/*     */     
/*     */     public PreparedTextBuilder(float x, float y, int color, boolean drawShadow, boolean includeEmpty) {
/* 124 */       this(x, y, color, 0, drawShadow, includeEmpty);
/*     */     }
/*     */     
/*     */     public PreparedTextBuilder(float x, float y, int color, int backgroundColor, boolean drawShadow, boolean includeEmpty) {
/* 128 */       this.x = x;
/* 129 */       this.y = y;
/* 130 */       this.drawShadow = drawShadow;
/* 131 */       this.color = color;
/* 132 */       this.backgroundColor = backgroundColor;
/* 133 */       this.includeEmpty = includeEmpty;
/* 134 */       markBackground(x, y, 0.0F);
/*     */     }
/*     */     
/*     */     private void markSize(float left, float top, float right, float bottom) {
/* 138 */       this.left = Math.min(this.left, left);
/* 139 */       this.top = Math.min(this.top, top);
/* 140 */       this.right = Math.max(this.right, right);
/* 141 */       this.bottom = Math.max(this.bottom, bottom);
/*     */     }
/*     */     
/*     */     private void markBackground(float x, float y, float advance) {
/* 145 */       if (ARGB.alpha(this.backgroundColor) == 0)
/*     */         return; 
/* 148 */       this.backgroundLeft = Math.min(this.backgroundLeft, x - 1.0F);
/* 149 */       this.backgroundTop = Math.min(this.backgroundTop, y - 1.0F);
/* 150 */       this.backgroundRight = Math.max(this.backgroundRight, x + advance);
/* 151 */       this.backgroundBottom = Math.max(this.backgroundBottom, y + 9.0F);
/* 152 */       markSize(this.backgroundLeft, this.backgroundTop, this.backgroundRight, this.backgroundBottom);
/*     */     }
/*     */     
/*     */     private void addGlyph(TextRenderable.Styled instance) {
/* 156 */       this.glyphs.add(instance);
/* 157 */       markSize(instance.left(), instance.top(), instance.right(), instance.bottom());
/*     */     }
/*     */     
/*     */     private void addEffect(TextRenderable effect) {
/* 161 */       if (this.effects == null)
/* 162 */         this.effects = new ArrayList<>(); 
/* 164 */       this.effects.add(effect);
/* 165 */       markSize(effect.left(), effect.top(), effect.right(), effect.bottom());
/*     */     }
/*     */     
/*     */     private void addEmptyGlyph(EmptyArea empty) {
/* 169 */       if (this.emptyAreas == null)
/* 170 */         this.emptyAreas = new ArrayList<>(); 
/* 172 */       this.emptyAreas.add(empty);
/*     */     }
/*     */     
/*     */     public boolean accept(int position, Style style, int c) {
/* 177 */       BakedGlyph glyph = Font.this.getGlyph(c, style);
/* 178 */       return accept(position, style, glyph);
/*     */     }
/*     */     
/*     */     public boolean accept(int position, Style style, BakedGlyph glyph) {
/* 182 */       GlyphInfo glyphInfo = glyph.info();
/* 184 */       boolean bold = style.isBold();
/* 186 */       TextColor styleColor = style.getColor();
/* 187 */       int textColor = getTextColor(styleColor);
/* 188 */       int shadowColor = getShadowColor(style, textColor);
/* 190 */       float advance = glyphInfo.getAdvance(bold);
/* 191 */       float effectX0 = (position == 0) ? (this.x - 1.0F) : this.x;
/* 192 */       float shadowOffset = glyphInfo.getShadowOffset();
/* 193 */       float boldOffset = bold ? glyphInfo.getBoldOffset() : 0.0F;
/* 195 */       TextRenderable.Styled instance = glyph.createGlyph(this.x, this.y, textColor, shadowColor, style, boldOffset, shadowOffset);
/* 196 */       if (instance != null) {
/* 197 */         addGlyph(instance);
/* 198 */       } else if (this.includeEmpty) {
/* 199 */         addEmptyGlyph(new EmptyArea(this.x, this.y, advance, 7.0F, 9.0F, style));
/*     */       } 
/* 202 */       markBackground(this.x, this.y, advance);
/* 204 */       if (style.isStrikethrough())
/* 205 */         addEffect(Font.this.provider.effect().createEffect(effectX0, this.y + 4.5F - 1.0F, this.x + advance, this.y + 4.5F, 0.01F, textColor, shadowColor, shadowOffset)); 
/* 217 */       if (style.isUnderlined())
/* 218 */         addEffect(Font.this.provider.effect().createEffect(effectX0, this.y + 9.0F - 1.0F, this.x + advance, this.y + 9.0F, 0.01F, textColor, shadowColor, shadowOffset)); 
/* 229 */       this.x += advance;
/* 230 */       return true;
/*     */     }
/*     */     
/*     */     public void visit(Font.GlyphVisitor visitor) {
/* 235 */       if (ARGB.alpha(this.backgroundColor) != 0)
/* 236 */         visitor.acceptEffect(Font.this.provider.effect().createEffect(this.backgroundLeft, this.backgroundTop, this.backgroundRight, this.backgroundBottom, -0.01F, this.backgroundColor, 0, 0.0F)); 
/* 248 */       for (TextRenderable.Styled glyph : this.glyphs)
/* 249 */         visitor.acceptGlyph(glyph); 
/* 252 */       if (this.effects != null)
/* 253 */         for (TextRenderable effect : this.effects)
/* 254 */           visitor.acceptEffect(effect);  
/* 258 */       if (this.emptyAreas != null)
/* 259 */         for (EmptyArea emptyArea : this.emptyAreas)
/* 260 */           visitor.acceptEmptyArea(emptyArea);  
/*     */     }
/*     */     
/*     */     public void discardEffects() {
/* 266 */       this.effects = null;
/*     */     }
/*     */     
/*     */     private int getTextColor(TextColor textColor) {
/* 270 */       if (textColor != null) {
/* 271 */         int alpha = ARGB.alpha(this.color);
/* 272 */         int rgb = textColor.getValue();
/* 273 */         return ARGB.color(alpha, rgb);
/*     */       } 
/* 275 */       return this.color;
/*     */     }
/*     */     
/*     */     private int getShadowColor(Style style, int textColor) {
/* 279 */       Integer shadow = style.getShadowColor();
/* 280 */       if (shadow != null) {
/* 281 */         float textAlpha = ARGB.alphaFloat(textColor);
/* 282 */         float shadowAlpha = ARGB.alphaFloat(shadow.intValue());
/* 283 */         if (textAlpha != 1.0F)
/* 284 */           return ARGB.color(ARGB.as8BitChannel(textAlpha * shadowAlpha), shadow.intValue()); 
/* 286 */         return shadow.intValue();
/*     */       } 
/* 287 */       if (this.drawShadow)
/* 288 */         return ARGB.scaleRGB(textColor, 0.25F); 
/* 290 */       return 0;
/*     */     }
/*     */     
/*     */     public ScreenRectangle bounds() {
/* 295 */       if (this.left >= this.right || this.top >= this.bottom)
/* 296 */         return null; 
/* 298 */       int left = Mth.floor(this.left);
/* 299 */       int top = Mth.floor(this.top);
/* 300 */       int right = Mth.ceil(this.right);
/* 301 */       int bottom = Mth.ceil(this.bottom);
/* 302 */       return new ScreenRectangle(left, top, right - left, bottom - top);
/*     */     }
/*     */   }
/*     */   
/*     */   private BakedGlyph getGlyph(int codepoint, Style style) {
/* 307 */     GlyphSource glyphSource = getGlyphSource(style.getFont());
/* 308 */     BakedGlyph glyph = glyphSource.getGlyph(codepoint);
/* 309 */     if (style.isObfuscated() && codepoint != 32) {
/* 310 */       int targetWidth = Mth.ceil(glyph.info().getAdvance(false));
/* 311 */       glyph = glyphSource.getRandomGlyph(this.random, targetWidth);
/*     */     } 
/* 313 */     return glyph;
/*     */   }
/*     */   
/*     */   public PreparedText prepareText(String text, float x, float y, int originalColor, boolean drawShadow, int backgroundColor) {
/* 317 */     if (isBidirectional())
/* 318 */       text = bidirectionalShaping(text); 
/* 320 */     PreparedTextBuilder output = new PreparedTextBuilder(x, y, originalColor, backgroundColor, drawShadow, false);
/* 321 */     StringDecomposer.iterateFormatted(text, Style.EMPTY, output);
/* 322 */     return output;
/*     */   }
/*     */   
/*     */   public PreparedText prepareText(FormattedCharSequence text, float x, float y, int originalColor, boolean drawShadow, boolean includeEmpty, int backgroundColor) {
/* 326 */     PreparedTextBuilder builder = new PreparedTextBuilder(x, y, originalColor, backgroundColor, drawShadow, includeEmpty);
/* 327 */     text.accept(builder);
/* 328 */     return builder;
/*     */   }
/*     */   
/*     */   public int width(String str) {
/* 332 */     return Mth.ceil(this.splitter.stringWidth(str));
/*     */   }
/*     */   
/*     */   public int width(FormattedText text) {
/* 336 */     return Mth.ceil(this.splitter.stringWidth(text));
/*     */   }
/*     */   
/*     */   public int width(FormattedCharSequence text) {
/* 340 */     return Mth.ceil(this.splitter.stringWidth(text));
/*     */   }
/*     */   
/*     */   public String plainSubstrByWidth(String str, int width, boolean reverse) {
/* 344 */     return reverse ? this.splitter.plainTailByWidth(str, width, Style.EMPTY) : this.splitter.plainHeadByWidth(str, width, Style.EMPTY);
/*     */   }
/*     */   
/*     */   public String plainSubstrByWidth(String str, int width) {
/* 348 */     return this.splitter.plainHeadByWidth(str, width, Style.EMPTY);
/*     */   }
/*     */   
/*     */   public FormattedText substrByWidth(FormattedText text, int width) {
/* 352 */     return this.splitter.headByWidth(text, width, Style.EMPTY);
/*     */   }
/*     */   
/*     */   public int wordWrapHeight(FormattedText input, int textWidth) {
/* 356 */     return 9 * this.splitter.splitLines(input, textWidth, Style.EMPTY).size();
/*     */   }
/*     */   
/*     */   public List<FormattedCharSequence> split(FormattedText input, int maxWidth) {
/* 360 */     return Language.getInstance().getVisualOrder(this.splitter.splitLines(input, maxWidth, Style.EMPTY));
/*     */   }
/*     */   
/*     */   public List<FormattedText> splitIgnoringLanguage(FormattedText input, int maxWidth) {
/* 364 */     return this.splitter.splitLines(input, maxWidth, Style.EMPTY);
/*     */   }
/*     */   
/*     */   public boolean isBidirectional() {
/* 368 */     return Language.getInstance().isDefaultRightToLeft();
/*     */   }
/*     */   
/*     */   public StringSplitter getSplitter() {
/* 372 */     return this.splitter;
/*     */   }
/*     */   
/*     */   public static interface GlyphVisitor {
/*     */     default void acceptGlyph(TextRenderable.Styled glyph) {
/* 377 */       acceptRenderable((TextRenderable)glyph);
/*     */     }
/*     */     
/*     */     default void acceptEffect(TextRenderable effect) {
/* 381 */       acceptRenderable(effect);
/*     */     }
/*     */     
/*     */     default void acceptRenderable(TextRenderable renderable) {}
/*     */     
/*     */     default void acceptEmptyArea(EmptyArea empty) {}
/*     */   }
/*     */   
/*     */   public static interface Provider {
/*     */     GlyphSource glyphs(FontDescription param1FontDescription);
/*     */     
/*     */     EffectGlyph effect();
/*     */   }
/*     */   
/*     */   public static interface PreparedText {
/*     */     void visit(Font.GlyphVisitor param1GlyphVisitor);
/*     */     
/*     */     ScreenRectangle bounds();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\Font.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */