/*    */ package net.minecraft.client.multiplayer;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class CacheSlot<C extends CacheSlot.Cleaner<C>, D> {
/*    */   private final Function<C, D> operation;
/*    */   
/*    */   private C context;
/*    */   
/*    */   private D value;
/*    */   
/*    */   public CacheSlot(Function<C, D> operation) {
/* 17 */     this.operation = operation;
/*    */   }
/*    */   
/*    */   public D compute(C context) {
/* 21 */     if (context == this.context && this.value != null)
/* 22 */       return this.value; 
/* 25 */     D newValue = this.operation.apply(context);
/* 26 */     this.value = newValue;
/* 27 */     this.context = context;
/* 28 */     context.registerForCleaning(this);
/* 29 */     return newValue;
/*    */   }
/*    */   
/*    */   public void clear() {
/* 33 */     this.value = null;
/* 34 */     this.context = null;
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Cleaner<C extends Cleaner<C>> {
/*    */     void registerForCleaning(CacheSlot<C, ?> param1CacheSlot);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\CacheSlot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */