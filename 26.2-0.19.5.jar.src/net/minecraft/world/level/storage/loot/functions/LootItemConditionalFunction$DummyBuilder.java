/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ final class DummyBuilder extends LootItemConditionalFunction.Builder<LootItemConditionalFunction.DummyBuilder> {
/*    */   private final Function<List<LootItemCondition>, LootItemFunction> constructor;
/*    */   
/*    */   public DummyBuilder(Function<List<LootItemCondition>, LootItemFunction> constructor) {
/* 76 */     this.constructor = constructor;
/*    */   }
/*    */   
/*    */   protected DummyBuilder getThis() {
/* 81 */     return this;
/*    */   }
/*    */   
/*    */   public LootItemFunction build() {
/* 86 */     return this.constructor.apply(getConditions());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\LootItemConditionalFunction$DummyBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */