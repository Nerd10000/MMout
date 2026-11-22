/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.ItemPredicate;
/*    */ 
/*    */ public class Builder extends LootItemConditionalFunction.Builder<FilteredFunction.Builder> {
/*    */   private final ItemPredicate itemPredicate;
/*    */   
/* 61 */   private Optional<LootItemFunction> onPass = Optional.empty();
/*    */   
/* 62 */   private Optional<LootItemFunction> onFail = Optional.empty();
/*    */   
/*    */   private Builder(ItemPredicate itemPredicate) {
/* 65 */     this.itemPredicate = itemPredicate;
/*    */   }
/*    */   
/*    */   protected Builder getThis() {
/* 70 */     return this;
/*    */   }
/*    */   
/*    */   public Builder onPass(Optional<LootItemFunction> onPass) {
/* 74 */     this.onPass = onPass;
/* 75 */     return this;
/*    */   }
/*    */   
/*    */   public Builder onFail(Optional<LootItemFunction> onFail) {
/* 79 */     this.onFail = onFail;
/* 80 */     return this;
/*    */   }
/*    */   
/*    */   public LootItemFunction build() {
/* 85 */     return new FilteredFunction(getConditions(), this.itemPredicate, this.onPass, this.onFail);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\FilteredFunction$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */