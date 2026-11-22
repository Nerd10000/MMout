/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.function.LongSupplier;
/*    */ 
/*    */ public interface NanoTimeSource extends LongSupplier, TimeSource {
/*    */   default long get(TimeUnit timeUnit) {
/* 13 */     return timeUnit.convert(getAsLong(), TimeUnit.NANOSECONDS);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\TimeSource$NanoTimeSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */