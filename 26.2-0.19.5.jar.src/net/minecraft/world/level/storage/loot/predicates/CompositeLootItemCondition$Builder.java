/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ 
/*    */ public abstract class Builder implements LootItemCondition.Builder {
/* 51 */   private final ImmutableList.Builder<LootItemCondition> terms = ImmutableList.builder();
/*    */   
/*    */   protected Builder(LootItemCondition.Builder... terms) {
/* 54 */     for (LootItemCondition.Builder term : terms)
/* 55 */       this.terms.add(term.build()); 
/*    */   }
/*    */   
/*    */   public void addTerm(LootItemCondition.Builder term) {
/* 60 */     this.terms.add(term.build());
/*    */   }
/*    */   
/*    */   public LootItemCondition build() {
/* 65 */     return create((List<LootItemCondition>)this.terms.build());
/*    */   }
/*    */   
/*    */   protected abstract LootItemCondition create(List<LootItemCondition> paramList);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\CompositeLootItemCondition$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */