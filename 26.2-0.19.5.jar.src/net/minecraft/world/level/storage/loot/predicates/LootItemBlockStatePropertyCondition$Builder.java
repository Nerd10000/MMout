/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.StatePropertiesPredicate;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class Builder implements LootItemCondition.Builder {
/*    */   private final Holder<Block> block;
/*    */   
/* 52 */   private Optional<StatePropertiesPredicate> properties = Optional.empty();
/*    */   
/*    */   public Builder(Block block) {
/* 55 */     this.block = (Holder<Block>)block.builtInRegistryHolder();
/*    */   }
/*    */   
/*    */   public Builder setProperties(StatePropertiesPredicate.Builder properties) {
/* 59 */     this.properties = properties.build();
/* 60 */     return this;
/*    */   }
/*    */   
/*    */   public LootItemCondition build() {
/* 65 */     return new LootItemBlockStatePropertyCondition(this.block, this.properties);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemBlockStatePropertyCondition$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */