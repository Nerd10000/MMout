/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class StringUtil {
/*  12 */   private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
/*     */   
/*  13 */   private static final Pattern LINE_PATTERN = Pattern.compile("\\r\\n|\\v");
/*     */   
/*  14 */   private static final Pattern LINE_END_PATTERN = Pattern.compile("(?:\\r\\n|\\v)$");
/*     */   
/*     */   public static String formatTickDuration(int ticks, float tickrate) {
/*  17 */     int seconds = Mth.floor(ticks / tickrate);
/*  18 */     int minutes = seconds / 60;
/*  19 */     seconds %= 60;
/*  20 */     int hours = minutes / 60;
/*  21 */     minutes %= 60;
/*  23 */     if (hours > 0)
/*  24 */       return String.format(Locale.ROOT, "%02d:%02d:%02d", new Object[] { Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds) }); 
/*  26 */     return String.format(Locale.ROOT, "%02d:%02d", new Object[] { Integer.valueOf(minutes), Integer.valueOf(seconds) });
/*     */   }
/*     */   
/*     */   public static String stripColor(String input) {
/*  30 */     return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
/*     */   }
/*     */   
/*     */   public static boolean isNullOrEmpty(String s) {
/*  34 */     return StringUtils.isEmpty(s);
/*     */   }
/*     */   
/*     */   public static String truncateStringIfNecessary(String s, int maxLength, boolean addDotDotDotIfTruncated) {
/*  41 */     if (s.length() <= maxLength)
/*  42 */       return s; 
/*  45 */     if (addDotDotDotIfTruncated && maxLength > 3)
/*  46 */       return s.substring(0, maxLength - 3) + "..."; 
/*  48 */     return s.substring(0, maxLength);
/*     */   }
/*     */   
/*     */   public static int lineCount(String s) {
/*  53 */     if (s.isEmpty())
/*  54 */       return 0; 
/*  57 */     Matcher matcher = LINE_PATTERN.matcher(s);
/*  59 */     int count = 1;
/*  60 */     while (matcher.find())
/*  61 */       count++; 
/*  63 */     return count;
/*     */   }
/*     */   
/*     */   public static boolean endsWithNewLine(String s) {
/*  67 */     return LINE_END_PATTERN.matcher(s).find();
/*     */   }
/*     */   
/*     */   public static String trimChatMessage(String message) {
/*  71 */     return truncateStringIfNecessary(message, 256, false);
/*     */   }
/*     */   
/*     */   public static boolean isAllowedChatCharacter(int ch) {
/*  75 */     return (ch != 167 && ch >= 32 && ch != 127);
/*     */   }
/*     */   
/*     */   public static boolean isValidPlayerName(String name) {
/*  79 */     if (name.length() > 16)
/*  80 */       return false; 
/*  82 */     return name.chars().filter(c -> (c <= 32 || c >= 127)).findAny().isEmpty();
/*     */   }
/*     */   
/*     */   public static String filterText(String input) {
/*  86 */     return filterText(input, false);
/*     */   }
/*     */   
/*     */   public static String filterText(String input, boolean multiline) {
/*  90 */     StringBuilder builder = new StringBuilder();
/*  92 */     for (char c : input.toCharArray()) {
/*  93 */       if (isAllowedChatCharacter(c)) {
/*  94 */         builder.append(c);
/*  95 */       } else if (multiline && c == '\n') {
/*  96 */         builder.append(c);
/*     */       } 
/*     */     } 
/* 100 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static boolean isWhitespace(int codepoint) {
/* 105 */     return (Character.isWhitespace(codepoint) || Character.isSpaceChar(codepoint));
/*     */   }
/*     */   
/*     */   public static boolean isBlank(String string) {
/* 109 */     if (string == null || string.isEmpty())
/* 110 */       return true; 
/* 112 */     return string.chars().allMatch(StringUtil::isWhitespace);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\StringUtil.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */