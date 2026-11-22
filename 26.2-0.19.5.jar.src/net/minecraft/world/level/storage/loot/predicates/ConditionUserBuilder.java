/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface ConditionUserBuilder<T extends ConditionUserBuilder<T>> {
/*    */   T when(LootItemCondition.Builder paramBuilder);
/*    */   
/*    */   default <E> T when(Iterable<E> collection, Function<E, LootItemCondition.Builder> conditionProvider) {
/*  9 */     T result = unwrap();
/* 10 */     for (E value : collection)
/* 11 */       result = result.when(conditionProvider.apply(value)); 
/* 13 */     return result;
/*    */   }
/*    */   
/*    */   T unwrap();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\ConditionUserBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */