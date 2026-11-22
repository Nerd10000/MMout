/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class AbstractChestBlock<E extends BlockEntity> extends BaseEntityBlock {
/*    */   protected final Supplier<BlockEntityType<? extends E>> blockEntityType;
/*    */   
/*    */   protected AbstractChestBlock(BlockBehaviour.Properties properties, Supplier<BlockEntityType<? extends E>> blockEntityType) {
/* 17 */     super(properties);
/* 18 */     this.blockEntityType = blockEntityType;
/*    */   }
/*    */   
/*    */   protected abstract MapCodec<? extends AbstractChestBlock<E>> codec();
/*    */   
/*    */   public abstract DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, boolean paramBoolean);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\AbstractChestBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */