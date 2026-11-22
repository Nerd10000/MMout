/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.Products;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public abstract class LootItemConditionalFunction implements LootItemFunction {
/*    */   protected final List<LootItemCondition> predicates;
/*    */   
/*    */   private final Predicate<LootContext> compositePredicates;
/*    */   
/*    */   protected LootItemConditionalFunction(List<LootItemCondition> predicates) {
/* 24 */     this.predicates = predicates;
/* 25 */     this.compositePredicates = Util.allOf(predicates);
/*    */   }
/*    */   
/*    */   protected static <T extends LootItemConditionalFunction> Products.P1<RecordCodecBuilder.Mu<T>, List<LootItemCondition>> commonFields(RecordCodecBuilder.Instance<T> i) {
/* 32 */     return i.group((App)LootItemCondition.DIRECT_CODEC
/* 33 */         .listOf().optionalFieldOf("conditions", List.of()).forGetter(f -> f.predicates));
/*    */   }
/*    */   
/*    */   public final ItemStack apply(ItemStack itemStack, LootContext context) {
/* 39 */     return this.compositePredicates.test(context) ? run(itemStack, context) : itemStack;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 46 */     super.validate(context);
/* 48 */     Validatable.validate(context, "conditions", this.predicates);
/*    */   }
/*    */   
/*    */   public static abstract class Builder<T extends Builder<T>> implements LootItemFunction.Builder, ConditionUserBuilder<T> {
/* 52 */     private final ImmutableList.Builder<LootItemCondition> conditions = ImmutableList.builder();
/*    */     
/*    */     public T when(LootItemCondition.Builder condition) {
/* 56 */       this.conditions.add(condition.build());
/* 57 */       return getThis();
/*    */     }
/*    */     
/*    */     public final T unwrap() {
/* 62 */       return getThis();
/*    */     }
/*    */     
/*    */     protected List<LootItemCondition> getConditions() {
/* 68 */       return (List<LootItemCondition>)this.conditions.build();
/*    */     }
/*    */     
/*    */     protected abstract T getThis();
/*    */   }
/*    */   
/*    */   private static final class DummyBuilder extends Builder<DummyBuilder> {
/*    */     private final Function<List<LootItemCondition>, LootItemFunction> constructor;
/*    */     
/*    */     public DummyBuilder(Function<List<LootItemCondition>, LootItemFunction> constructor) {
/* 76 */       this.constructor = constructor;
/*    */     }
/*    */     
/*    */     protected DummyBuilder getThis() {
/* 81 */       return this;
/*    */     }
/*    */     
/*    */     public LootItemFunction build() {
/* 86 */       return this.constructor.apply(getConditions());
/*    */     }
/*    */   }
/*    */   
/*    */   protected static Builder<?> simpleBuilder(Function<List<LootItemCondition>, LootItemFunction> constructor) {
/* 91 */     return new DummyBuilder(constructor);
/*    */   }
/*    */   
/*    */   public abstract MapCodec<? extends LootItemConditionalFunction> codec();
/*    */   
/*    */   protected abstract ItemStack run(ItemStack paramItemStack, LootContext paramLootContext);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\LootItemConditionalFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */