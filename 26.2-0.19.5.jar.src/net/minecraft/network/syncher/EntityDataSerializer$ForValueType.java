/*    */ package net.minecraft.network.syncher;
/*    */ 
/*    */ public interface ForValueType<T> extends EntityDataSerializer<T> {
/*    */   default T copy(T value) {
/* 18 */     return value;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\syncher\EntityDataSerializer$ForValueType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */