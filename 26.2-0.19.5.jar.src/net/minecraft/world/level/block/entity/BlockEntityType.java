/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockEntityType<T extends BlockEntity> {
/*    */   private final BlockEntitySupplier<? extends T> factory;
/*    */   
/*    */   private final Set<Block> validBlocks;
/*    */   
/* 17 */   private final Holder.Reference<BlockEntityType<?>> builtInRegistryHolder = BuiltInRegistries.BLOCK_ENTITY_TYPE.createIntrusiveHolder(this);
/*    */   
/*    */   public BlockEntityType(BlockEntitySupplier<? extends T> factory, Set<Block> validBlocks) {
/* 20 */     this.factory = factory;
/* 21 */     this.validBlocks = validBlocks;
/*    */   }
/*    */   
/*    */   public T create(BlockPos worldPosition, BlockState blockState) {
/* 25 */     return this.factory.create(worldPosition, blockState);
/*    */   }
/*    */   
/*    */   public boolean isValid(BlockState state) {
/* 30 */     return this.validBlocks.contains(state.getBlock());
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public Holder.Reference<BlockEntityType<?>> builtInRegistryHolder() {
/* 36 */     return this.builtInRegistryHolder;
/*    */   }
/*    */   
/*    */   public T getBlockEntity(BlockGetter level, BlockPos pos) {
/* 46 */     BlockEntity entity = level.getBlockEntity(pos);
/* 47 */     if (entity == null || entity.getType() != this)
/* 48 */       return null; 
/* 50 */     return (T)entity;
/*    */   }
/*    */   
/*    */   public boolean onlyOpCanSetNbt() {
/* 54 */     return BlockEntityTypes.OP_ONLY_CUSTOM_DATA.contains(this);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface BlockEntitySupplier<T extends BlockEntity> {
/*    */     T create(BlockPos param1BlockPos, BlockState param1BlockState);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BlockEntityType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */