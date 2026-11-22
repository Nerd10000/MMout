/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.numbers.NumberFormat;
/*    */ 
/*    */ public interface ReadOnlyScoreInfo {
/*    */   int value();
/*    */   
/*    */   boolean isLocked();
/*    */   
/*    */   NumberFormat numberFormat();
/*    */   
/*    */   default MutableComponent formatValue(NumberFormat defaultFormat) {
/* 17 */     return ((NumberFormat)Objects.<NumberFormat>requireNonNullElse(numberFormat(), defaultFormat)).format(value());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\ReadOnlyScoreInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */