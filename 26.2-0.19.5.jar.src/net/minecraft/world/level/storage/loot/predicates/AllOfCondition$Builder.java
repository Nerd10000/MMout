/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class Builder extends CompositeLootItemCondition.Builder {
/*    */   public Builder(LootItemCondition.Builder... terms) {
/* 28 */     super(terms);
/*    */   }
/*    */   
/*    */   public Builder and(LootItemCondition.Builder term) {
/* 33 */     addTerm(term);
/* 34 */     return this;
/*    */   }
/*    */   
/*    */   protected LootItemCondition create(List<LootItemCondition> terms) {
/* 39 */     return new AllOfCondition(terms);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\AllOfCondition$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */