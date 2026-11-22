/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.Style;
/*     */ 
/*     */ public class RichStyling implements TextComponentTagVisitor.Styling {
/* 336 */   private static final Style SYNTAX_HIGHLIGHTING_NUMBER_TYPE = Style.EMPTY.withColor(ChatFormatting.RED);
/*     */   
/* 338 */   public static final TextComponentTagVisitor.Styling INSTANCE = new RichStyling();
/*     */   
/* 340 */   private final Map<TextComponentTagVisitor.Token, Component> tokens = new HashMap<>();
/*     */   
/*     */   private RichStyling() {
/* 343 */     overrideToken(TextComponentTagVisitor.Token.FOLDED, Style.EMPTY.withColor(ChatFormatting.GRAY));
/* 344 */     overrideToken(TextComponentTagVisitor.Token.BYTE_SUFFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 345 */     overrideToken(TextComponentTagVisitor.Token.BYTE_ARRAY_PREFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 346 */     overrideToken(TextComponentTagVisitor.Token.SHORT_SUFFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 347 */     overrideToken(TextComponentTagVisitor.Token.INT_ARRAY_PREFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 348 */     overrideToken(TextComponentTagVisitor.Token.LONG_SUFFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 349 */     overrideToken(TextComponentTagVisitor.Token.LONG_ARRAY_PREFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 350 */     overrideToken(TextComponentTagVisitor.Token.FLOAT_SUFFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 351 */     overrideToken(TextComponentTagVisitor.Token.DOUBLE_SUFFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 353 */     for (TextComponentTagVisitor.Token value : TextComponentTagVisitor.Token.values())
/* 354 */       this.tokens.putIfAbsent(value, Component.literal(value.text)); 
/*     */   }
/*     */   
/*     */   private void overrideToken(TextComponentTagVisitor.Token token, Style style) {
/* 359 */     this.tokens.put(token, Component.literal(token.text).withStyle(style));
/*     */   }
/*     */   
/* 362 */   private static final Style SYNTAX_HIGHLIGHTING_KEY = Style.EMPTY.withColor(ChatFormatting.AQUA);
/*     */   
/* 363 */   private static final Style SYNTAX_HIGHLIGHTING_STRING = Style.EMPTY.withColor(ChatFormatting.GREEN);
/*     */   
/* 364 */   private static final Style SYNTAX_HIGHLIGHTING_NUMBER = Style.EMPTY.withColor(ChatFormatting.GOLD);
/*     */   
/*     */   public Style keyStyle() {
/* 368 */     return SYNTAX_HIGHLIGHTING_KEY;
/*     */   }
/*     */   
/*     */   public Style stringStyle() {
/* 373 */     return SYNTAX_HIGHLIGHTING_STRING;
/*     */   }
/*     */   
/*     */   public Style numberStyle() {
/* 378 */     return SYNTAX_HIGHLIGHTING_NUMBER;
/*     */   }
/*     */   
/*     */   public Component token(TextComponentTagVisitor.Token token) {
/* 383 */     return Objects.<Component>requireNonNull(this.tokens.get(token));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\TextComponentTagVisitor$RichStyling.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */