/*    */ package net.minecraft;
/*    */ 
/*    */ import java.util.regex.Pattern;
/*    */ import org.jetbrains.annotations.Contract;
/*    */ 
/*    */ public enum ChatFormatting {
/*  9 */   BLACK('0'),
/* 10 */   DARK_BLUE('1'),
/* 11 */   DARK_GREEN('2'),
/* 12 */   DARK_AQUA('3'),
/* 13 */   DARK_RED('4'),
/* 14 */   DARK_PURPLE('5'),
/* 15 */   GOLD('6'),
/* 16 */   GRAY('7'),
/* 17 */   DARK_GRAY('8'),
/* 18 */   BLUE('9'),
/* 19 */   GREEN('a'),
/* 20 */   AQUA('b'),
/* 21 */   RED('c'),
/* 22 */   LIGHT_PURPLE('d'),
/* 23 */   YELLOW('e'),
/* 24 */   WHITE('f'),
/* 25 */   OBFUSCATED('k'),
/* 26 */   BOLD('l'),
/* 27 */   STRIKETHROUGH('m'),
/* 28 */   UNDERLINE('n'),
/* 29 */   ITALIC('o'),
/* 30 */   RESET('r');
/*    */   
/*    */   public static final char PREFIX_CODE = '§';
/*    */   
/*    */   private static final Pattern STRIP_FORMATTING_PATTERN;
/*    */   
/*    */   private final char code;
/*    */   
/*    */   private final String toString;
/*    */   
/*    */   static {
/* 33 */     STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
/*    */   }
/*    */   
/*    */   ChatFormatting(char code) {
/* 39 */     this.code = code;
/* 40 */     this.toString = "§" + String.valueOf(code);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 45 */     return this.toString;
/*    */   }
/*    */   
/*    */   @Contract("!null->!null;_->_")
/*    */   public static String stripFormatting(String input) {
/* 50 */     return (input == null) ? null : STRIP_FORMATTING_PATTERN.matcher(input).replaceAll("");
/*    */   }
/*    */   
/*    */   public static ChatFormatting getByCode(char code) {
/* 54 */     char sanitized = Character.toLowerCase(code);
/* 55 */     for (ChatFormatting format : values()) {
/* 56 */       if (format.code == sanitized)
/* 57 */         return format; 
/*    */     } 
/* 60 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\ChatFormatting.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */