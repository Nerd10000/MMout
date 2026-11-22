/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class Builder extends CompositeLootItemCondition.Builder {
/*    */   public Builder(LootItemCondition.Builder... terms) {
/* 22 */     super(terms);
/*    */   }
/*    */   
/*    */   public Builder or(LootItemCondition.Builder term) {
/* 27 */     addTerm(term);
/* 28 */     return this;
/*    */   }
/*    */   
/*    */   protected LootItemCondition create(List<LootItemCondition> terms) {
/* 33 */     return new AnyOfCondition(terms);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\AnyOfCondition$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */