/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface Builder {
/*    */   LootItemCondition build();
/*    */   
/*    */   default Builder invert() {
/* 29 */     return InvertedLootItemCondition.invert(this);
/*    */   }
/*    */   
/*    */   default AnyOfCondition.Builder or(Builder other) {
/* 33 */     return AnyOfCondition.anyOf(new Builder[] { this, other });
/*    */   }
/*    */   
/*    */   default AllOfCondition.Builder and(Builder other) {
/* 37 */     return AllOfCondition.allOf(new Builder[] { this, other });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemCondition$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */