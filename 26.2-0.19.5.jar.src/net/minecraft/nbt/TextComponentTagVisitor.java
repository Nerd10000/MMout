/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TextComponentTagVisitor implements TagVisitor {
/*  21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int INLINE_LIST_THRESHOLD = 8;
/*     */   
/*     */   private static final int MAX_DEPTH = 64;
/*     */   
/*     */   private static final int MAX_LENGTH = 128;
/*     */   
/*  27 */   private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
/*     */   
/*  29 */   private static final Component NEWLINE = (Component)Component.literal("\n");
/*     */   
/*  30 */   private static final Component ELEMENT_SPACING = (Component)Component.literal(" ");
/*     */   
/*     */   private final String indentation;
/*     */   
/*     */   private final Styling styling;
/*     */   
/*     */   private final boolean sortKeys;
/*     */   
/*     */   private int indentDepth;
/*     */   
/*     */   private int depth;
/*     */   
/*  38 */   private final MutableComponent result = Component.empty();
/*     */   
/*     */   public TextComponentTagVisitor(String indentation) {
/*  41 */     this(indentation, RichStyling.INSTANCE);
/*     */   }
/*     */   
/*     */   public TextComponentTagVisitor(String indentation, Styling styling) {
/*  45 */     this(indentation, styling, LOGGER.isDebugEnabled());
/*     */   }
/*     */   
/*     */   public TextComponentTagVisitor(String indentation, Styling styling, boolean sortKeys) {
/*  49 */     this.indentation = indentation;
/*  50 */     this.styling = styling;
/*  51 */     this.sortKeys = sortKeys;
/*     */   }
/*     */   
/*     */   public Component visit(Tag tag) {
/*  55 */     tag.accept(this);
/*  56 */     return (Component)this.result;
/*     */   }
/*     */   
/*     */   private TextComponentTagVisitor append(String string, Style style) {
/*  60 */     this.result.append((Component)Component.literal(string).withStyle(style));
/*  61 */     return this;
/*     */   }
/*     */   
/*     */   private TextComponentTagVisitor append(Component component) {
/*  65 */     this.result.append(component);
/*  66 */     return this;
/*     */   }
/*     */   
/*     */   private TextComponentTagVisitor append(Token token) {
/*  70 */     this.result.append(this.styling.token(token));
/*  71 */     return this;
/*     */   }
/*     */   
/*     */   public void visitString(StringTag tag) {
/*  76 */     String quoted = StringTag.quoteAndEscape(tag.value());
/*  77 */     MutableComponent mutableComponent = Component.literal(quoted.substring(0, 1));
/*  78 */     append((Component)mutableComponent).append(quoted.substring(1, quoted.length() - 1), this.styling.stringStyle()).append((Component)mutableComponent);
/*     */   }
/*     */   
/*     */   public void visitByte(ByteTag tag) {
/*  83 */     append(String.valueOf(tag.value()), this.styling.numberStyle()).append(Token.BYTE_SUFFIX);
/*     */   }
/*     */   
/*     */   public void visitShort(ShortTag tag) {
/*  88 */     append(String.valueOf(tag.value()), this.styling.numberStyle()).append(Token.SHORT_SUFFIX);
/*     */   }
/*     */   
/*     */   public void visitInt(IntTag tag) {
/*  93 */     append(String.valueOf(tag.value()), this.styling.numberStyle());
/*     */   }
/*     */   
/*     */   public void visitLong(LongTag tag) {
/*  98 */     append(String.valueOf(tag.value()), this.styling.numberStyle()).append(Token.LONG_SUFFIX);
/*     */   }
/*     */   
/*     */   public void visitFloat(FloatTag tag) {
/* 103 */     append(String.valueOf(tag.value()), this.styling.numberStyle()).append(Token.FLOAT_SUFFIX);
/*     */   }
/*     */   
/*     */   public void visitDouble(DoubleTag tag) {
/* 108 */     append(String.valueOf(tag.value()), this.styling.numberStyle()).append(Token.DOUBLE_SUFFIX);
/*     */   }
/*     */   
/*     */   public void visitByteArray(ByteArrayTag tag) {
/* 113 */     append(Token.LIST_OPEN).append(Token.BYTE_ARRAY_PREFIX).append(Token.LIST_TYPE_SEPARATOR);
/* 115 */     byte[] data = tag.getAsByteArray();
/* 116 */     for (int i = 0; i < data.length && i < 128; i++) {
/* 117 */       append(ELEMENT_SPACING).append(String.valueOf(data[i]), this.styling.numberStyle()).append(Token.BYTE_SUFFIX);
/* 119 */       if (i != data.length - 1)
/* 120 */         append(Token.ELEMENT_SEPARATOR); 
/*     */     } 
/* 124 */     if (data.length > 128)
/* 125 */       append(Token.FOLDED); 
/* 128 */     append(Token.LIST_CLOSE);
/*     */   }
/*     */   
/*     */   public void visitIntArray(IntArrayTag tag) {
/* 133 */     append(Token.LIST_OPEN).append(Token.INT_ARRAY_PREFIX).append(Token.LIST_TYPE_SEPARATOR);
/* 135 */     int[] data = tag.getAsIntArray();
/* 136 */     for (int i = 0; i < data.length && i < 128; i++) {
/* 137 */       append(ELEMENT_SPACING).append(String.valueOf(data[i]), this.styling.numberStyle());
/* 138 */       if (i != data.length - 1)
/* 139 */         append(Token.ELEMENT_SEPARATOR); 
/*     */     } 
/* 143 */     if (data.length > 128)
/* 144 */       append(Token.FOLDED); 
/* 147 */     append(Token.LIST_CLOSE);
/*     */   }
/*     */   
/*     */   public void visitLongArray(LongArrayTag tag) {
/* 152 */     append(Token.LIST_OPEN).append(Token.LONG_ARRAY_PREFIX).append(Token.LIST_TYPE_SEPARATOR);
/* 154 */     long[] data = tag.getAsLongArray();
/* 155 */     for (int i = 0; i < data.length && i < 128; i++) {
/* 156 */       append(ELEMENT_SPACING).append(String.valueOf(data[i]), this.styling.numberStyle()).append(Token.LONG_SUFFIX);
/* 157 */       if (i != data.length - 1)
/* 158 */         append(Token.ELEMENT_SEPARATOR); 
/*     */     } 
/* 162 */     if (data.length > 128)
/* 163 */       append(Token.FOLDED); 
/* 166 */     append(Token.LIST_CLOSE);
/*     */   }
/*     */   
/*     */   private static boolean shouldWrapListElements(ListTag list) {
/* 170 */     if (list.size() >= 8)
/* 171 */       return false; 
/* 173 */     for (Tag element : list) {
/* 174 */       if (!(element instanceof NumericTag))
/* 175 */         return true; 
/*     */     } 
/* 178 */     return false;
/*     */   }
/*     */   
/*     */   public void visitList(ListTag tag) {
/* 183 */     if (tag.isEmpty()) {
/* 184 */       append(Token.LIST_OPEN).append(Token.LIST_CLOSE);
/*     */       return;
/*     */     } 
/* 186 */     if (this.depth >= 64) {
/* 187 */       append(Token.LIST_OPEN).append(Token.FOLDED).append(Token.LIST_CLOSE);
/*     */       return;
/*     */     } 
/* 191 */     if (!shouldWrapListElements(tag)) {
/* 192 */       append(Token.LIST_OPEN);
/* 193 */       for (int j = 0; j < tag.size(); j++) {
/* 194 */         if (j != 0)
/* 195 */           append(Token.ELEMENT_SEPARATOR).append(ELEMENT_SPACING); 
/* 197 */         appendSubTag(tag.get(j), false);
/*     */       } 
/* 199 */       append(Token.LIST_CLOSE);
/*     */       return;
/*     */     } 
/* 203 */     append(Token.LIST_OPEN);
/* 204 */     if (!this.indentation.isEmpty())
/* 205 */       append(NEWLINE); 
/* 207 */     MutableComponent mutableComponent = Component.literal(this.indentation.repeat(this.indentDepth + 1));
/* 208 */     Component elementSpacing = this.indentation.isEmpty() ? ELEMENT_SPACING : NEWLINE;
/* 209 */     for (int i = 0; i < tag.size() && i < 128; i++) {
/* 210 */       append((Component)mutableComponent);
/* 211 */       appendSubTag(tag.get(i), true);
/* 212 */       if (i != tag.size() - 1)
/* 213 */         append(Token.ELEMENT_SEPARATOR).append(elementSpacing); 
/*     */     } 
/* 216 */     if (tag.size() > 128)
/* 217 */       append((Component)mutableComponent).append(Token.FOLDED); 
/* 219 */     if (!this.indentation.isEmpty())
/* 220 */       append(NEWLINE).append((Component)Component.literal(this.indentation.repeat(this.indentDepth))); 
/* 222 */     append(Token.LIST_CLOSE);
/*     */   }
/*     */   
/*     */   public void visitCompound(CompoundTag tag) {
/*     */     Collection<String> keys;
/* 227 */     if (tag.isEmpty()) {
/* 228 */       append(Token.STRUCT_OPEN).append(Token.STRUCT_CLOSE);
/*     */       return;
/*     */     } 
/* 230 */     if (this.depth >= 64) {
/* 231 */       append(Token.STRUCT_OPEN).append(Token.FOLDED).append(Token.STRUCT_CLOSE);
/*     */       return;
/*     */     } 
/* 235 */     append(Token.STRUCT_OPEN);
/* 238 */     if (this.sortKeys) {
/* 239 */       List<String> keyCopy = new ArrayList<>(tag.keySet());
/* 240 */       Collections.sort(keyCopy);
/* 241 */       keys = keyCopy;
/*     */     } else {
/* 243 */       keys = tag.keySet();
/*     */     } 
/* 246 */     if (!this.indentation.isEmpty())
/* 247 */       append(NEWLINE); 
/* 250 */     MutableComponent mutableComponent = Component.literal(this.indentation.repeat(this.indentDepth + 1));
/* 251 */     Component elementSpacing = this.indentation.isEmpty() ? ELEMENT_SPACING : NEWLINE;
/* 252 */     for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
/* 253 */       String key = iterator.next();
/* 254 */       append((Component)mutableComponent)
/* 255 */         .append(handleEscapePretty(key))
/* 256 */         .append(Token.NAME_VALUE_SEPARATOR)
/* 257 */         .append(ELEMENT_SPACING);
/* 258 */       appendSubTag(tag.get(key), true);
/* 260 */       if (iterator.hasNext())
/* 261 */         append(Token.ELEMENT_SEPARATOR).append(elementSpacing); 
/*     */     } 
/* 264 */     if (!this.indentation.isEmpty())
/* 265 */       append(NEWLINE).append((Component)Component.literal(this.indentation.repeat(this.indentDepth))); 
/* 267 */     append(Token.STRUCT_CLOSE);
/*     */   }
/*     */   
/*     */   private void appendSubTag(Tag tag, boolean indent) {
/* 271 */     if (indent)
/* 272 */       this.indentDepth++; 
/* 274 */     this.depth++;
/*     */     try {
/* 276 */       tag.accept(this);
/*     */     } finally {
/* 278 */       if (indent)
/* 279 */         this.indentDepth--; 
/* 281 */       this.depth--;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Component handleEscapePretty(String input) {
/* 286 */     if (SIMPLE_VALUE.matcher(input).matches())
/* 287 */       return (Component)Component.literal(input).withStyle(this.styling.keyStyle()); 
/* 290 */     String quoted = StringTag.quoteAndEscape(input);
/* 291 */     String quote = quoted.substring(0, 1);
/* 292 */     MutableComponent mutableComponent = Component.literal(quoted.substring(1, quoted.length() - 1)).withStyle(this.styling.keyStyle());
/* 293 */     return (Component)Component.literal(quote).append((Component)mutableComponent).append(quote);
/*     */   }
/*     */   
/*     */   public void visitEnd(EndTag tag) {}
/*     */   
/*     */   public enum Token {
/* 301 */     FOLDED("<...>"),
/* 302 */     ELEMENT_SEPARATOR(","),
/* 303 */     LIST_CLOSE("]"),
/* 304 */     LIST_OPEN("["),
/* 305 */     LIST_TYPE_SEPARATOR(";"),
/* 306 */     STRUCT_CLOSE("}"),
/* 307 */     STRUCT_OPEN("{"),
/* 308 */     NAME_VALUE_SEPARATOR(":"),
/* 309 */     BYTE_SUFFIX("b"),
/* 310 */     BYTE_ARRAY_PREFIX("B"),
/* 311 */     SHORT_SUFFIX("s"),
/* 312 */     INT_ARRAY_PREFIX("I"),
/* 313 */     LONG_SUFFIX("L"),
/* 314 */     LONG_ARRAY_PREFIX("L"),
/* 315 */     FLOAT_SUFFIX("f"),
/* 316 */     DOUBLE_SUFFIX("d");
/*     */     
/*     */     public final String text;
/*     */     
/*     */     Token(String text) {
/* 321 */       this.text = text;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RichStyling implements Styling {
/* 336 */     private static final Style SYNTAX_HIGHLIGHTING_NUMBER_TYPE = Style.EMPTY.withColor(ChatFormatting.RED);
/*     */     
/* 338 */     public static final TextComponentTagVisitor.Styling INSTANCE = new RichStyling();
/*     */     
/* 340 */     private final Map<TextComponentTagVisitor.Token, Component> tokens = new HashMap<>();
/*     */     
/*     */     private RichStyling() {
/* 343 */       overrideToken(TextComponentTagVisitor.Token.FOLDED, Style.EMPTY.withColor(ChatFormatting.GRAY));
/* 344 */       overrideToken(TextComponentTagVisitor.Token.BYTE_SUFFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 345 */       overrideToken(TextComponentTagVisitor.Token.BYTE_ARRAY_PREFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 346 */       overrideToken(TextComponentTagVisitor.Token.SHORT_SUFFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 347 */       overrideToken(TextComponentTagVisitor.Token.INT_ARRAY_PREFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 348 */       overrideToken(TextComponentTagVisitor.Token.LONG_SUFFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 349 */       overrideToken(TextComponentTagVisitor.Token.LONG_ARRAY_PREFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 350 */       overrideToken(TextComponentTagVisitor.Token.FLOAT_SUFFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 351 */       overrideToken(TextComponentTagVisitor.Token.DOUBLE_SUFFIX, SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
/* 353 */       for (TextComponentTagVisitor.Token value : TextComponentTagVisitor.Token.values())
/* 354 */         this.tokens.putIfAbsent(value, Component.literal(value.text)); 
/*     */     }
/*     */     
/*     */     private void overrideToken(TextComponentTagVisitor.Token token, Style style) {
/* 359 */       this.tokens.put(token, Component.literal(token.text).withStyle(style));
/*     */     }
/*     */     
/* 362 */     private static final Style SYNTAX_HIGHLIGHTING_KEY = Style.EMPTY.withColor(ChatFormatting.AQUA);
/*     */     
/* 363 */     private static final Style SYNTAX_HIGHLIGHTING_STRING = Style.EMPTY.withColor(ChatFormatting.GREEN);
/*     */     
/* 364 */     private static final Style SYNTAX_HIGHLIGHTING_NUMBER = Style.EMPTY.withColor(ChatFormatting.GOLD);
/*     */     
/*     */     public Style keyStyle() {
/* 368 */       return SYNTAX_HIGHLIGHTING_KEY;
/*     */     }
/*     */     
/*     */     public Style stringStyle() {
/* 373 */       return SYNTAX_HIGHLIGHTING_STRING;
/*     */     }
/*     */     
/*     */     public Style numberStyle() {
/* 378 */       return SYNTAX_HIGHLIGHTING_NUMBER;
/*     */     }
/*     */     
/*     */     public Component token(TextComponentTagVisitor.Token token) {
/* 383 */       return Objects.<Component>requireNonNull(this.tokens.get(token));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PlainStyling implements Styling {
/* 388 */     public static final TextComponentTagVisitor.Styling INSTANCE = new PlainStyling();
/*     */     
/* 390 */     private final Map<TextComponentTagVisitor.Token, Component> tokens = new HashMap<>();
/*     */     
/*     */     private PlainStyling() {
/* 393 */       for (TextComponentTagVisitor.Token value : TextComponentTagVisitor.Token.values())
/* 394 */         this.tokens.put(value, Component.literal(value.text)); 
/*     */     }
/*     */     
/*     */     public Style keyStyle() {
/* 400 */       return Style.EMPTY;
/*     */     }
/*     */     
/*     */     public Style stringStyle() {
/* 405 */       return Style.EMPTY;
/*     */     }
/*     */     
/*     */     public Style numberStyle() {
/* 410 */       return Style.EMPTY;
/*     */     }
/*     */     
/*     */     public Component token(TextComponentTagVisitor.Token token) {
/* 415 */       return Objects.<Component>requireNonNull(this.tokens.get(token));
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Styling {
/*     */     Style keyStyle();
/*     */     
/*     */     Style stringStyle();
/*     */     
/*     */     Style numberStyle();
/*     */     
/*     */     Component token(TextComponentTagVisitor.Token param1Token);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\TextComponentTagVisitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */