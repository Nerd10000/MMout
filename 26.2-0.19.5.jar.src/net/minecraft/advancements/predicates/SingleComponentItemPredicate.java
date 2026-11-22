/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*    */ 
/*    */ public interface SingleComponentItemPredicate<T> extends DataComponentPredicate {
/*    */   default boolean matches(DataComponentGetter components) {
/* 10 */     T value = (T)components.get(componentType());
/* 11 */     return (value != null && matches(value));
/*    */   }
/*    */   
/*    */   DataComponentType<T> componentType();
/*    */   
/*    */   boolean matches(T paramT);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\SingleComponentItemPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */