/*    */ package net.minecraft.util.profiling.metrics.profiling;
/*    */ 
/*    */ import com.google.common.base.Ticker;
/*    */ import java.util.function.LongSupplier;
/*    */ 
/*    */ class null extends Ticker {
/*    */   public long read() {
/* 68 */     return timeSource.getAsLong();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\metrics\profiling\ServerMetricsSamplersProvider$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */