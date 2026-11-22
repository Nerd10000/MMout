/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public abstract class CompositeLootItemCondition implements LootItemCondition {
/*    */   protected final List<LootItemCondition> terms;
/*    */   
/*    */   private final Predicate<LootContext> composedPredicate;
/*    */   
/*    */   protected CompositeLootItemCondition(List<LootItemCondition> terms, Predicate<LootContext> composedPredicate) {
/* 21 */     this.terms = terms;
/* 22 */     this.composedPredicate = composedPredicate;
/*    */   }
/*    */   
/*    */   protected static <T extends CompositeLootItemCondition> MapCodec<T> createCodec(Function<List<LootItemCondition>, T> factory) {
/* 29 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)LootItemCondition.DIRECT_CODEC.listOf().fieldOf("terms").forGetter(())).apply((Applicative)i, factory));
/*    */   }
/*    */   
/*    */   protected static <T extends CompositeLootItemCondition> Codec<T> createInlineCodec(Function<List<LootItemCondition>, T> factory) {
/* 35 */     return LootItemCondition.DIRECT_CODEC.listOf().xmap(factory, condition -> condition.terms);
/*    */   }
/*    */   
/*    */   public final boolean test(LootContext context) {
/* 40 */     return this.composedPredicate.test(context);
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext output) {
/* 45 */     super.validate(output);
/* 47 */     Validatable.validate(output, "terms", this.terms);
/*    */   }
/*    */   
/*    */   public abstract MapCodec<? extends CompositeLootItemCondition> codec();
/*    */   
/*    */   public static abstract class Builder implements LootItemCondition.Builder {
/* 51 */     private final ImmutableList.Builder<LootItemCondition> terms = ImmutableList.builder();
/*    */     
/*    */     protected Builder(LootItemCondition.Builder... terms) {
/* 54 */       for (LootItemCondition.Builder term : terms)
/* 55 */         this.terms.add(term.build()); 
/*    */     }
/*    */     
/*    */     public void addTerm(LootItemCondition.Builder term) {
/* 60 */       this.terms.add(term.build());
/*    */     }
/*    */     
/*    */     public LootItemCondition build() {
/* 65 */       return create((List<LootItemCondition>)this.terms.build());
/*    */     }
/*    */     
/*    */     protected abstract LootItemCondition create(List<LootItemCondition> param1List);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\CompositeLootItemCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */