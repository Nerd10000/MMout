/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public abstract class Builder<T extends LootPoolEntryContainer.Builder<T>> implements ConditionUserBuilder<T> {
/* 46 */   private final ImmutableList.Builder<LootItemCondition> conditions = ImmutableList.builder();
/*    */   
/*    */   public T when(LootItemCondition.Builder condition) {
/* 52 */     this.conditions.add(condition.build());
/* 53 */     return getThis();
/*    */   }
/*    */   
/*    */   public final T unwrap() {
/* 58 */     return getThis();
/*    */   }
/*    */   
/*    */   protected List<LootItemCondition> getConditions() {
/* 62 */     return (List<LootItemCondition>)this.conditions.build();
/*    */   }
/*    */   
/*    */   public AlternativesEntry.Builder otherwise(Builder<?> other) {
/* 66 */     return new AlternativesEntry.Builder((Builder<?>[])new Builder[] { this, other });
/*    */   }
/*    */   
/*    */   public EntryGroup.Builder append(Builder<?> other) {
/* 70 */     return new EntryGroup.Builder((Builder<?>[])new Builder[] { this, other });
/*    */   }
/*    */   
/*    */   public SequentialEntry.Builder then(Builder<?> other) {
/* 74 */     return new SequentialEntry.Builder((Builder<?>[])new Builder[] { this, other });
/*    */   }
/*    */   
/*    */   protected abstract T getThis();
/*    */   
/*    */   public abstract LootPoolEntryContainer build();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolEntryContainer$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */