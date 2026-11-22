/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.DecimalFormatSymbols;
/*    */ import java.text.NumberFormat;
/*    */ import java.util.Locale;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public interface StatFormatter {
/*  9 */   public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("########0.00", DecimalFormatSymbols.getInstance(Locale.ROOT));
/*    */   
/* 11 */   public static final StatFormatter DEFAULT = NumberFormat.getIntegerInstance(Locale.US)::format;
/*    */   
/*    */   public static final StatFormatter DIVIDE_BY_TEN;
/*    */   
/*    */   public static final StatFormatter DISTANCE;
/*    */   
/*    */   public static final StatFormatter TIME;
/*    */   
/*    */   static {
/* 11 */     Objects.requireNonNull(NumberFormat.getIntegerInstance(Locale.US));
/*    */   }
/*    */   
/*    */   static {
/* 12 */     DIVIDE_BY_TEN = (value -> DECIMAL_FORMAT.format(value * 0.1D));
/* 13 */     DISTANCE = (cm -> {
/*    */         double meters = cm / 100.0D;
/*    */         double kilometers = meters / 1000.0D;
/*    */         return (kilometers > 0.5D) ? (DECIMAL_FORMAT.format(kilometers) + " km") : ((meters > 0.5D) ? (DECIMAL_FORMAT.format(meters) + " m") : ("" + cm + " cm"));
/*    */       });
/* 24 */     TIME = (value -> {
/*    */         double seconds = value / 20.0D;
/*    */         double minutes = seconds / 60.0D;
/*    */         double hours = minutes / 60.0D;
/*    */         double days = hours / 24.0D;
/*    */         double years = days / 365.0D;
/*    */         return (years > 0.5D) ? (DECIMAL_FORMAT.format(years) + " y") : ((days > 0.5D) ? (DECIMAL_FORMAT.format(days) + " d") : ((hours > 0.5D) ? (DECIMAL_FORMAT.format(hours) + " h") : ((minutes > 0.5D) ? (DECIMAL_FORMAT.format(minutes) + " min") : ("" + seconds + " s"))));
/*    */       });
/*    */   }
/*    */   
/*    */   String format(int paramInt);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\stats\StatFormatter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */