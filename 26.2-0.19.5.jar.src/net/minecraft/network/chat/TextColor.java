/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.ChatFormatting;
/*     */ 
/*     */ public final class TextColor {
/*     */   private static final String CUSTOM_COLOR_PREFIX = "#";
/*     */   
/*  17 */   public static final Codec<TextColor> CODEC = Codec.STRING.comapFlatMap(TextColor::parseColor, TextColor::serialize);
/*     */   
/*  22 */   private static final Map<String, TextColor> NAMED_COLORS = new HashMap<>();
/*     */   
/*  24 */   public static final TextColor BLACK = named("black", 0);
/*     */   
/*  25 */   public static final TextColor DARK_BLUE = named("dark_blue", 170);
/*     */   
/*  26 */   public static final TextColor DARK_GREEN = named("dark_green", 43520);
/*     */   
/*  27 */   public static final TextColor DARK_AQUA = named("dark_aqua", 43690);
/*     */   
/*  28 */   public static final TextColor DARK_RED = named("dark_red", 11141120);
/*     */   
/*  29 */   public static final TextColor DARK_PURPLE = named("dark_purple", 11141290);
/*     */   
/*  30 */   public static final TextColor GOLD = named("gold", 16755200);
/*     */   
/*  31 */   public static final TextColor GRAY = named("gray", 11184810);
/*     */   
/*  32 */   public static final TextColor DARK_GRAY = named("dark_gray", 5592405);
/*     */   
/*  33 */   public static final TextColor BLUE = named("blue", 5592575);
/*     */   
/*  34 */   public static final TextColor GREEN = named("green", 5635925);
/*     */   
/*  35 */   public static final TextColor AQUA = named("aqua", 5636095);
/*     */   
/*  36 */   public static final TextColor RED = named("red", 16733525);
/*     */   
/*  37 */   public static final TextColor LIGHT_PURPLE = named("light_purple", 16733695);
/*     */   
/*  38 */   public static final TextColor YELLOW = named("yellow", 16777045);
/*     */   
/*  39 */   public static final TextColor WHITE = named("white", 16777215);
/*     */   
/*     */   private final int value;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private TextColor(int value, String name) {
/*  46 */     this.value = value & 0xFFFFFF;
/*  47 */     this.name = name;
/*     */   }
/*     */   
/*     */   private TextColor(int value) {
/*  51 */     this.value = value & 0xFFFFFF;
/*  52 */     this.name = null;
/*     */   }
/*     */   
/*     */   private static TextColor named(String name, int rgb) {
/*  56 */     TextColor result = new TextColor(rgb, name);
/*  57 */     NAMED_COLORS.put(name, result);
/*  58 */     return result;
/*     */   }
/*     */   
/*     */   public int getValue() {
/*  62 */     return this.value;
/*     */   }
/*     */   
/*     */   public String serialize() {
/*  66 */     return (this.name != null) ? this.name : formatValue();
/*     */   }
/*     */   
/*     */   private String formatValue() {
/*  70 */     return String.format(Locale.ROOT, "#%06X", new Object[] { Integer.valueOf(this.value) });
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  75 */     if (this == o)
/*  76 */       return true; 
/*  78 */     if (o == null || getClass() != o.getClass())
/*  79 */       return false; 
/*  81 */     TextColor other = (TextColor)o;
/*  82 */     return (this.value == other.value);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  87 */     return Objects.hash(new Object[] { Integer.valueOf(this.value), this.name });
/*     */   }
/*     */   
/*     */   public String toString() {
/*  92 */     return serialize();
/*     */   }
/*     */   
/*     */   public static TextColor fromLegacyFormat(ChatFormatting format) {
/*  96 */     switch (format) {
/*     */       case BLACK:
/*     */       
/*     */       case DARK_BLUE:
/*     */       
/*     */       case DARK_GREEN:
/*     */       
/*     */       case DARK_AQUA:
/*     */       
/*     */       case DARK_RED:
/*     */       
/*     */       case DARK_PURPLE:
/*     */       
/*     */       case GOLD:
/*     */       
/*     */       case GRAY:
/*     */       
/*     */       case DARK_GRAY:
/*     */       
/*     */       case BLUE:
/*     */       
/*     */       case GREEN:
/*     */       
/*     */       case AQUA:
/*     */       
/*     */       case RED:
/*     */       
/*     */       case LIGHT_PURPLE:
/*     */       
/*     */       case YELLOW:
/*     */       
/*     */       case WHITE:
/*     */       
/*     */     } 
/*  96 */     return 
/*     */       
/* 113 */       null;
/*     */   }
/*     */   
/*     */   public static TextColor fromRgb(int rgb) {
/* 118 */     return new TextColor(rgb);
/*     */   }
/*     */   
/*     */   public static DataResult<TextColor> parseColor(String color) {
/* 122 */     if (color.startsWith("#"))
/*     */       try {
/* 124 */         int value = Integer.parseInt(color.substring(1), 16);
/* 125 */         if (value < 0 || value > 16777215)
/* 126 */           return DataResult.error(() -> "Color value out of range: " + color); 
/* 128 */         return DataResult.success(fromRgb(value), Lifecycle.stable());
/* 129 */       } catch (NumberFormatException e) {
/* 130 */         return DataResult.error(() -> "Invalid color value: " + color);
/*     */       }  
/* 133 */     TextColor predefinedColor = NAMED_COLORS.get(color);
/* 134 */     if (predefinedColor == null)
/* 135 */       return DataResult.error(() -> "Invalid color name: " + color); 
/* 137 */     return DataResult.success(predefinedColor, Lifecycle.stable());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\TextColor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */