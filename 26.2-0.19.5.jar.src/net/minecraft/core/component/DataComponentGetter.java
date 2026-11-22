/*    */ package net.minecraft.core.component;
/*    */ 
/*    */ public interface DataComponentGetter {
/*    */   <T> T get(DataComponentType<? extends T> paramDataComponentType);
/*    */   
/*    */   default <T> T getOrDefault(DataComponentType<? extends T> type, T defaultValue) {
/* 22 */     T value = get(type);
/* 23 */     return (value != null) ? value : defaultValue;
/*    */   }
/*    */   
/*    */   default <T> TypedDataComponent<T> getTyped(DataComponentType<T> type) {
/* 27 */     T value = get(type);
/* 28 */     return (value != null) ? new TypedDataComponent<>(type, value) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentGetter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */