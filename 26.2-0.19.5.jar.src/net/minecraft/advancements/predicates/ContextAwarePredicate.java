/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class ContextAwarePredicate implements Validatable {
/*    */   public static final Codec<ContextAwarePredicate> CODEC;
/*    */   
/*    */   private final List<LootItemCondition> conditions;
/*    */   
/*    */   private final Predicate<LootContext> compositePredicates;
/*    */   
/*    */   static {
/* 15 */     CODEC = LootItemCondition.DIRECT_CODEC.listOf().xmap(ContextAwarePredicate::new, predicate -> predicate.conditions);
/*    */   }
/*    */   
/*    */   public ContextAwarePredicate(List<LootItemCondition> conditions) {
/* 21 */     this.conditions = conditions;
/* 22 */     this.compositePredicates = Util.allOf(conditions);
/*    */   }
/*    */   
/*    */   public static ContextAwarePredicate create(LootItemCondition... conditions) {
/* 26 */     return new ContextAwarePredicate(List.of(conditions));
/*    */   }
/*    */   
/*    */   public boolean matches(LootContext context) {
/* 30 */     return this.compositePredicates.test(context);
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 35 */     Validatable.validate(context, this.conditions);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\ContextAwarePredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */