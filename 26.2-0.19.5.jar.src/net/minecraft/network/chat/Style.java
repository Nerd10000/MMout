/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class Style {
/*  17 */   public static final Style EMPTY = new Style(null, null, null, null, null, null, null, null, null, null, null);
/*     */   
/*     */   public static final int NO_SHADOW = 0;
/*     */   
/*     */   private final TextColor color;
/*     */   
/*     */   private final Integer shadowColor;
/*     */   
/*     */   private final Boolean bold;
/*     */   
/*     */   private final Boolean italic;
/*     */   
/*     */   private final Boolean underlined;
/*     */   
/*     */   private final Boolean strikethrough;
/*     */   
/*     */   private final Boolean obfuscated;
/*     */   
/*     */   private final ClickEvent clickEvent;
/*     */   
/*     */   private final HoverEvent hoverEvent;
/*     */   
/*     */   private final String insertion;
/*     */   
/*     */   private final FontDescription font;
/*     */   
/*     */   public static class Serializer {
/*     */     public static final MapCodec<Style> MAP_CODEC;
/*     */     
/*     */     static {
/*  21 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TextColor.CODEC.optionalFieldOf("color").forGetter(()), (App)ExtraCodecs.ARGB_COLOR_CODEC.optionalFieldOf("shadow_color").forGetter(()), (App)Codec.BOOL.optionalFieldOf("bold").forGetter(()), (App)Codec.BOOL.optionalFieldOf("italic").forGetter(()), (App)Codec.BOOL.optionalFieldOf("underlined").forGetter(()), (App)Codec.BOOL.optionalFieldOf("strikethrough").forGetter(()), (App)Codec.BOOL.optionalFieldOf("obfuscated").forGetter(()), (App)ClickEvent.CODEC.optionalFieldOf("click_event").forGetter(()), (App)HoverEvent.CODEC.optionalFieldOf("hover_event").forGetter(()), (App)Codec.STRING.optionalFieldOf("insertion").forGetter(()), (App)FontDescription.CODEC.optionalFieldOf("font").forGetter(())).apply((Applicative)i, Style::create));
/*     */     }
/*     */     
/*  36 */     public static final Codec<Style> CODEC = MAP_CODEC.codec();
/*     */     
/*  37 */     public static final StreamCodec<RegistryFriendlyByteBuf, Style> TRUSTED_STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistriesTrusted(CODEC);
/*     */   }
/*     */   
/*     */   private static Style create(Optional<TextColor> color, Optional<Integer> shadowColor, Optional<Boolean> bold, Optional<Boolean> italic, Optional<Boolean> underlined, Optional<Boolean> strikethrough, Optional<Boolean> obfuscated, Optional<ClickEvent> clickEvent, Optional<HoverEvent> hoverEvent, Optional<String> insertion, Optional<FontDescription> font) {
/*  55 */     Style result = new Style(color.orElse(null), shadowColor.orElse(null), bold.orElse(null), italic.orElse(null), underlined.orElse(null), strikethrough.orElse(null), obfuscated.orElse(null), clickEvent.orElse(null), hoverEvent.orElse(null), insertion.orElse(null), font.orElse(null));
/*  56 */     if (result.equals(EMPTY))
/*  57 */       return EMPTY; 
/*  59 */     return result;
/*     */   }
/*     */   
/*     */   private Style(TextColor color, Integer shadowColor, Boolean bold, Boolean italic, Boolean underlined, Boolean strikethrough, Boolean obfuscated, ClickEvent clickEvent, HoverEvent hoverEvent, String insertion, FontDescription font) {
/*  63 */     this.color = color;
/*  64 */     this.shadowColor = shadowColor;
/*  65 */     this.bold = bold;
/*  66 */     this.italic = italic;
/*  67 */     this.underlined = underlined;
/*  68 */     this.strikethrough = strikethrough;
/*  69 */     this.obfuscated = obfuscated;
/*  70 */     this.clickEvent = clickEvent;
/*  71 */     this.hoverEvent = hoverEvent;
/*  72 */     this.insertion = insertion;
/*  73 */     this.font = font;
/*     */   }
/*     */   
/*     */   public TextColor getColor() {
/*  77 */     return this.color;
/*     */   }
/*     */   
/*     */   public Integer getShadowColor() {
/*  81 */     return this.shadowColor;
/*     */   }
/*     */   
/*     */   public boolean isBold() {
/*  85 */     return (this.bold == Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public boolean isItalic() {
/*  89 */     return (this.italic == Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public boolean isStrikethrough() {
/*  93 */     return (this.strikethrough == Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public boolean isUnderlined() {
/*  97 */     return (this.underlined == Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public boolean isObfuscated() {
/* 101 */     return (this.obfuscated == Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 105 */     return (this == EMPTY);
/*     */   }
/*     */   
/*     */   public ClickEvent getClickEvent() {
/* 109 */     return this.clickEvent;
/*     */   }
/*     */   
/*     */   public HoverEvent getHoverEvent() {
/* 113 */     return this.hoverEvent;
/*     */   }
/*     */   
/*     */   public String getInsertion() {
/* 117 */     return this.insertion;
/*     */   }
/*     */   
/*     */   public FontDescription getFont() {
/* 121 */     return (this.font != null) ? this.font : FontDescription.DEFAULT;
/*     */   }
/*     */   
/*     */   private static <T> Style checkEmptyAfterChange(Style newStyle, T previous, T next) {
/* 127 */     if (previous != null && next == null && newStyle.equals(EMPTY))
/* 128 */       return EMPTY; 
/* 130 */     return newStyle;
/*     */   }
/*     */   
/*     */   public Style withColor(TextColor color) {
/* 134 */     if (Objects.equals(this.color, color))
/* 135 */       return this; 
/* 137 */     return checkEmptyAfterChange(new Style(color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.color, color);
/*     */   }
/*     */   
/*     */   public Style withColor(ChatFormatting color) {
/* 144 */     return withColor((color != null) ? TextColor.fromLegacyFormat(color) : null);
/*     */   }
/*     */   
/*     */   public Style withColor(int color) {
/* 148 */     return withColor(TextColor.fromRgb(color));
/*     */   }
/*     */   
/*     */   public Style withShadowColor(int shadowColor) {
/* 152 */     if (Objects.equals(this.shadowColor, Integer.valueOf(shadowColor)))
/* 153 */       return this; 
/* 156 */     return checkEmptyAfterChange(new Style(this.color, 
/* 157 */           Integer.valueOf(shadowColor), this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.shadowColor, 
/* 158 */         Integer.valueOf(shadowColor));
/*     */   }
/*     */   
/*     */   public Style withoutShadow() {
/* 163 */     return withShadowColor(0);
/*     */   }
/*     */   
/*     */   public Style withBold(Boolean bold) {
/* 167 */     if (Objects.equals(this.bold, bold))
/* 168 */       return this; 
/* 170 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.bold, bold);
/*     */   }
/*     */   
/*     */   public Style withItalic(Boolean italic) {
/* 177 */     if (Objects.equals(this.italic, italic))
/* 178 */       return this; 
/* 180 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.italic, italic);
/*     */   }
/*     */   
/*     */   public Style withUnderlined(Boolean underlined) {
/* 187 */     if (Objects.equals(this.underlined, underlined))
/* 188 */       return this; 
/* 190 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.underlined, underlined);
/*     */   }
/*     */   
/*     */   public Style withStrikethrough(Boolean strikethrough) {
/* 197 */     if (Objects.equals(this.strikethrough, strikethrough))
/* 198 */       return this; 
/* 200 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.strikethrough, strikethrough);
/*     */   }
/*     */   
/*     */   public Style withObfuscated(Boolean obfuscated) {
/* 207 */     if (Objects.equals(this.obfuscated, obfuscated))
/* 208 */       return this; 
/* 210 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font), this.obfuscated, obfuscated);
/*     */   }
/*     */   
/*     */   public Style withClickEvent(ClickEvent clickEvent) {
/* 217 */     if (Objects.equals(this.clickEvent, clickEvent))
/* 218 */       return this; 
/* 220 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, clickEvent, this.hoverEvent, this.insertion, this.font), this.clickEvent, clickEvent);
/*     */   }
/*     */   
/*     */   public Style withHoverEvent(HoverEvent hoverEvent) {
/* 227 */     if (Objects.equals(this.hoverEvent, hoverEvent))
/* 228 */       return this; 
/* 230 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, hoverEvent, this.insertion, this.font), this.hoverEvent, hoverEvent);
/*     */   }
/*     */   
/*     */   public Style withInsertion(String insertion) {
/* 237 */     if (Objects.equals(this.insertion, insertion))
/* 238 */       return this; 
/* 240 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, insertion, this.font), this.insertion, insertion);
/*     */   }
/*     */   
/*     */   public Style withFont(FontDescription font) {
/* 247 */     if (Objects.equals(this.font, font))
/* 248 */       return this; 
/* 250 */     return checkEmptyAfterChange(new Style(this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, font), this.font, font);
/*     */   }
/*     */   
/*     */   public Style applyFormat(ChatFormatting format) {
/* 257 */     TextColor color = this.color;
/* 258 */     Boolean bold = this.bold;
/* 259 */     Boolean italic = this.italic;
/* 260 */     Boolean strikethrough = this.strikethrough;
/* 261 */     Boolean underlined = this.underlined;
/* 262 */     Boolean obfuscated = this.obfuscated;
/* 264 */     switch (format) {
/*     */       case OBFUSCATED:
/* 265 */         obfuscated = Boolean.valueOf(true);
/* 276 */         return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */       case BOLD:
/*     */         bold = Boolean.valueOf(true);
/* 276 */         return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */       case STRIKETHROUGH:
/*     */         strikethrough = Boolean.valueOf(true);
/* 276 */         return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */       case UNDERLINE:
/*     */         underlined = Boolean.valueOf(true);
/* 276 */         return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */       case ITALIC:
/*     */         italic = Boolean.valueOf(true);
/* 276 */         return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */       case RESET:
/*     */         return EMPTY;
/*     */     } 
/*     */     color = TextColor.fromLegacyFormat(format);
/* 276 */     return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */   }
/*     */   
/*     */   public Style applyLegacyFormat(ChatFormatting format) {
/* 280 */     TextColor color = this.color;
/* 281 */     Boolean bold = this.bold;
/* 282 */     Boolean italic = this.italic;
/* 283 */     Boolean strikethrough = this.strikethrough;
/* 284 */     Boolean underlined = this.underlined;
/* 285 */     Boolean obfuscated = this.obfuscated;
/* 287 */     switch (format) {
/*     */       case OBFUSCATED:
/* 288 */         obfuscated = Boolean.valueOf(true);
/* 306 */         return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */       case BOLD:
/*     */         bold = Boolean.valueOf(true);
/* 306 */         return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */       case STRIKETHROUGH:
/*     */         strikethrough = Boolean.valueOf(true);
/* 306 */         return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */       case UNDERLINE:
/*     */         underlined = Boolean.valueOf(true);
/* 306 */         return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */       case ITALIC:
/*     */         italic = Boolean.valueOf(true);
/* 306 */         return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */       case RESET:
/*     */         return EMPTY;
/*     */     } 
/*     */     obfuscated = Boolean.valueOf(false);
/*     */     bold = Boolean.valueOf(false);
/*     */     strikethrough = Boolean.valueOf(false);
/*     */     underlined = Boolean.valueOf(false);
/*     */     italic = Boolean.valueOf(false);
/*     */     color = TextColor.fromLegacyFormat(format);
/* 306 */     return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */   }
/*     */   
/*     */   public Style applyFormats(ChatFormatting... formats) {
/* 310 */     TextColor color = this.color;
/* 311 */     Boolean bold = this.bold;
/* 312 */     Boolean italic = this.italic;
/* 313 */     Boolean strikethrough = this.strikethrough;
/* 314 */     Boolean underlined = this.underlined;
/* 315 */     Boolean obfuscated = this.obfuscated;
/* 317 */     for (ChatFormatting format : formats) {
/* 318 */       switch (format) {
/*     */         case OBFUSCATED:
/* 319 */           obfuscated = Boolean.valueOf(true);
/*     */           break;
/*     */         case BOLD:
/* 320 */           bold = Boolean.valueOf(true);
/*     */           break;
/*     */         case STRIKETHROUGH:
/* 321 */           strikethrough = Boolean.valueOf(true);
/*     */           break;
/*     */         case UNDERLINE:
/* 322 */           underlined = Boolean.valueOf(true);
/*     */           break;
/*     */         case ITALIC:
/* 323 */           italic = Boolean.valueOf(true);
/*     */           break;
/*     */         case RESET:
/* 325 */           return EMPTY;
/*     */         default:
/* 327 */           color = TextColor.fromLegacyFormat(format);
/*     */           break;
/*     */       } 
/*     */     } 
/* 331 */     return new Style(color, this.shadowColor, bold, italic, underlined, strikethrough, obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
/*     */   }
/*     */   
/*     */   public Style applyTo(Style other) {
/* 335 */     if (this == EMPTY)
/* 336 */       return other; 
/* 339 */     if (other == EMPTY)
/* 340 */       return this; 
/* 343 */     return new Style(
/* 344 */         (this.color != null) ? this.color : other.color, 
/* 345 */         (this.shadowColor != null) ? this.shadowColor : other.shadowColor, 
/* 346 */         (this.bold != null) ? this.bold : other.bold, 
/* 347 */         (this.italic != null) ? this.italic : other.italic, 
/* 348 */         (this.underlined != null) ? this.underlined : other.underlined, 
/* 349 */         (this.strikethrough != null) ? this.strikethrough : other.strikethrough, 
/* 350 */         (this.obfuscated != null) ? this.obfuscated : other.obfuscated, 
/* 351 */         (this.clickEvent != null) ? this.clickEvent : other.clickEvent, 
/* 352 */         (this.hoverEvent != null) ? this.hoverEvent : other.hoverEvent, 
/* 353 */         (this.insertion != null) ? this.insertion : other.insertion, 
/* 354 */         (this.font != null) ? this.font : other.font);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 360 */     final StringBuilder result = new StringBuilder("{");
/*     */     class Collector {
/*     */       private boolean isNotFirst;
/*     */       
/*     */       Collector(Style this$0) {}
/*     */       
/*     */       private void prependSeparator() {
/* 366 */         if (this.isNotFirst)
/* 367 */           result.append(','); 
/* 369 */         this.isNotFirst = true;
/*     */       }
/*     */       
/*     */       private void addFlagString(String name, Boolean value) {
/* 373 */         if (value != null) {
/* 374 */           prependSeparator();
/* 375 */           if (!value.booleanValue())
/* 376 */             result.append('!'); 
/* 378 */           result.append(name);
/*     */         } 
/*     */       }
/*     */       
/*     */       private void addValueString(String name, Object value) {
/* 383 */         if (value != null) {
/* 384 */           prependSeparator();
/* 385 */           result.append(name);
/* 386 */           result.append('=');
/* 387 */           result.append(value);
/*     */         } 
/*     */       }
/*     */     };
/* 392 */     Collector collector = new Collector(this);
/* 394 */     collector.addValueString("color", this.color);
/* 396 */     collector.addValueString("shadowColor", this.shadowColor);
/* 398 */     collector.addFlagString("bold", this.bold);
/* 399 */     collector.addFlagString("italic", this.italic);
/* 400 */     collector.addFlagString("underlined", this.underlined);
/* 401 */     collector.addFlagString("strikethrough", this.strikethrough);
/* 402 */     collector.addFlagString("obfuscated", this.obfuscated);
/* 404 */     collector.addValueString("clickEvent", this.clickEvent);
/* 405 */     collector.addValueString("hoverEvent", this.hoverEvent);
/* 406 */     collector.addValueString("insertion", this.insertion);
/* 407 */     collector.addValueString("font", this.font);
/* 409 */     result.append("}");
/* 410 */     return result.toString();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 415 */     if (this == o)
/* 416 */       return true; 
/* 418 */     if (o instanceof Style) {
/* 418 */       Style style = (Style)o;
/* 419 */       return (this.bold == style.bold && 
/* 420 */         Objects.equals(getColor(), style.getColor()) && 
/* 421 */         Objects.equals(getShadowColor(), style.getShadowColor()) && this.italic == style.italic && this.obfuscated == style.obfuscated && this.strikethrough == style.strikethrough && this.underlined == style.underlined && 
/*     */         
/* 426 */         Objects.equals(this.clickEvent, style.clickEvent) && 
/* 427 */         Objects.equals(this.hoverEvent, style.hoverEvent) && 
/* 428 */         Objects.equals(this.insertion, style.insertion) && 
/* 429 */         Objects.equals(this.font, style.font));
/*     */     } 
/* 433 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 438 */     return Objects.hash(new Object[] { this.color, this.shadowColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\Style.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */