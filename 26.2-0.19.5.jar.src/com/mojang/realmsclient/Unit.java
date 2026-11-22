/*    */ package com.mojang.realmsclient;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ public enum Unit {
/*  6 */   B, KB, MB, GB;
/*    */   
/*    */   private static final int BASE_UNIT = 1024;
/*    */   
/*    */   public static Unit getLargest(long bytes) {
/* 15 */     if (bytes < 1024L)
/* 16 */       return B; 
/*    */     try {
/* 20 */       int exp = (int)(Math.log(bytes) / Math.log(1024.0D));
/* 21 */       String pre = String.valueOf("KMGTPE".charAt(exp - 1));
/* 23 */       return valueOf(pre + "B");
/* 24 */     } catch (Exception ignored) {
/* 25 */       return GB;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static double convertTo(long bytes, Unit unit) {
/* 30 */     if (unit == B)
/* 31 */       return bytes; 
/* 34 */     return bytes / Math.pow(1024.0D, unit.ordinal());
/*    */   }
/*    */   
/*    */   public static String humanReadable(long bytes) {
/* 38 */     int unit = 1024;
/* 39 */     if (bytes < 1024L)
/* 40 */       return "" + bytes + " B"; 
/* 42 */     int exp = (int)(Math.log(bytes) / Math.log(1024.0D));
/* 43 */     String pre = "" + "KMGTPE".charAt(exp - 1);
/* 44 */     return String.format(Locale.ROOT, "%.1f %sB", new Object[] { Double.valueOf(bytes / Math.pow(1024.0D, exp)), pre });
/*    */   }
/*    */   
/*    */   public static String humanReadable(long bytes, Unit unit) {
/* 48 */     return String.format(Locale.ROOT, "%." + ((unit == GB) ? "1" : "0") + "f %s", new Object[] { Double.valueOf(convertTo(bytes, unit)), unit.name() });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\Unit.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */