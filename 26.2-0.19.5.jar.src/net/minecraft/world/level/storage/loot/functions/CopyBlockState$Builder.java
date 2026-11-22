/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class Builder extends LootItemConditionalFunction.Builder<CopyBlockState.Builder> {
/*    */   private final Holder<Block> block;
/*    */   
/* 77 */   private final ImmutableSet.Builder<Property<?>> properties = ImmutableSet.builder();
/*    */   
/*    */   private Builder(Block block) {
/* 80 */     this.block = (Holder<Block>)block.builtInRegistryHolder();
/*    */   }
/*    */   
/*    */   public Builder copy(Property<?> property) {
/* 84 */     if (!((Block)this.block.value()).getStateDefinition().getProperties().contains(property))
/* 85 */       throw new IllegalStateException("Property " + String.valueOf(property) + " is not present on block " + String.valueOf(this.block)); 
/* 87 */     this.properties.add(property);
/* 88 */     return this;
/*    */   }
/*    */   
/*    */   protected Builder getThis() {
/* 93 */     return this;
/*    */   }
/*    */   
/*    */   public LootItemFunction build() {
/* 98 */     return new CopyBlockState(getConditions(), this.block, (Set<Property<?>>)this.properties.build());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\CopyBlockState$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */