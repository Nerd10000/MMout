/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public abstract class Builder<T extends LootItemConditionalFunction.Builder<T>> implements LootItemFunction.Builder, ConditionUserBuilder<T> {
/* 52 */   private final ImmutableList.Builder<LootItemCondition> conditions = ImmutableList.builder();
/*    */   
/*    */   public T when(LootItemCondition.Builder condition) {
/* 56 */     this.conditions.add(condition.build());
/* 57 */     return getThis();
/*    */   }
/*    */   
/*    */   public final T unwrap() {
/* 62 */     return getThis();
/*    */   }
/*    */   
/*    */   protected List<LootItemCondition> getConditions() {
/* 68 */     return (List<LootItemCondition>)this.conditions.build();
/*    */   }
/*    */   
/*    */   protected abstract T getThis();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\LootItemConditionalFunction$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */