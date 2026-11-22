/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public abstract class LootPoolEntryContainer implements ComposableEntryContainer, Validatable {
/*    */   protected final List<LootItemCondition> conditions;
/*    */   
/*    */   private final Predicate<LootContext> compositeCondition;
/*    */   
/*    */   protected LootPoolEntryContainer(List<LootItemCondition> conditions) {
/* 23 */     this.conditions = conditions;
/* 24 */     this.compositeCondition = Util.allOf(conditions);
/*    */   }
/*    */   
/*    */   protected static <T extends LootPoolEntryContainer> Products.P1<RecordCodecBuilder.Mu<T>, List<LootItemCondition>> commonFields(RecordCodecBuilder.Instance<T> i) {
/* 28 */     return i.group((App)LootItemCondition.DIRECT_CODEC
/* 29 */         .listOf().optionalFieldOf("conditions", List.of()).forGetter(e -> e.conditions));
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext output) {
/* 35 */     Validatable.validate(output, "conditions", this.conditions);
/*    */   }
/*    */   
/*    */   protected final boolean canRun(LootContext context) {
/* 39 */     return this.compositeCondition.test(context);
/*    */   }
/*    */   
/*    */   public abstract MapCodec<? extends LootPoolEntryContainer> codec();
/*    */   
/*    */   public static abstract class Builder<T extends Builder<T>> implements ConditionUserBuilder<T> {
/* 46 */     private final ImmutableList.Builder<LootItemCondition> conditions = ImmutableList.builder();
/*    */     
/*    */     public T when(LootItemCondition.Builder condition) {
/* 52 */       this.conditions.add(condition.build());
/* 53 */       return getThis();
/*    */     }
/*    */     
/*    */     public final T unwrap() {
/* 58 */       return getThis();
/*    */     }
/*    */     
/*    */     protected List<LootItemCondition> getConditions() {
/* 62 */       return (List<LootItemCondition>)this.conditions.build();
/*    */     }
/*    */     
/*    */     public AlternativesEntry.Builder otherwise(Builder<?> other) {
/* 66 */       return new AlternativesEntry.Builder((Builder<?>[])new Builder[] { this, other });
/*    */     }
/*    */     
/*    */     public EntryGroup.Builder append(Builder<?> other) {
/* 70 */       return new EntryGroup.Builder((Builder<?>[])new Builder[] { this, other });
/*    */     }
/*    */     
/*    */     public SequentialEntry.Builder then(Builder<?> other) {
/* 74 */       return new SequentialEntry.Builder((Builder<?>[])new Builder[] { this, other });
/*    */     }
/*    */     
/*    */     protected abstract T getThis();
/*    */     
/*    */     public abstract LootPoolEntryContainer build();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolEntryContainer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */