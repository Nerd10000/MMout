/*    */ package net.minecraft.core.component;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public interface DataComponentHolder extends DataComponentGetter {
/*    */   default <T> T get(DataComponentType<? extends T> type) {
/* 12 */     return getComponents().get(type);
/*    */   }
/*    */   
/*    */   default <T> Stream<T> getAllOfType(Class<? extends T> valueClass) {
/* 17 */     return getComponents().stream().map(TypedDataComponent::value).filter(value -> valueClass.isAssignableFrom(value.getClass())).map(value -> value);
/*    */   }
/*    */   
/*    */   default <T> T getOrDefault(DataComponentType<? extends T> type, T defaultValue) {
/* 22 */     return getComponents().getOrDefault(type, defaultValue);
/*    */   }
/*    */   
/*    */   default boolean has(DataComponentType<?> type) {
/* 26 */     return getComponents().has(type);
/*    */   }
/*    */   
/*    */   DataComponentMap getComponents();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */