/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.numbers.NumberFormat;
/*    */ 
/*    */ public interface ScoreAccess {
/*    */   int get();
/*    */   
/*    */   void set(int paramInt);
/*    */   
/*    */   default int add(int count) {
/* 13 */     int newValue = get() + count;
/* 14 */     set(newValue);
/* 15 */     return newValue;
/*    */   }
/*    */   
/*    */   default int increment() {
/* 19 */     return add(1);
/*    */   }
/*    */   
/*    */   default void reset() {
/* 23 */     set(0);
/*    */   }
/*    */   
/*    */   boolean locked();
/*    */   
/*    */   void unlock();
/*    */   
/*    */   void lock();
/*    */   
/*    */   Component display();
/*    */   
/*    */   void display(Component paramComponent);
/*    */   
/*    */   void numberFormatOverride(NumberFormat paramNumberFormat);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\ScoreAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */