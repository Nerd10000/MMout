/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.stats.Stat;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*    */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TrappedChestBlock extends ChestBlock {
/* 20 */   public static final MapCodec<TrappedChestBlock> CODEC = simpleCodec(TrappedChestBlock::new);
/*    */   
/*    */   public MapCodec<TrappedChestBlock> codec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/*    */   public TrappedChestBlock(BlockBehaviour.Properties properties) {
/* 28 */     super(() -> BlockEntityTypes.TRAPPED_CHEST, SoundEvents.CHEST_OPEN, SoundEvents.CHEST_CLOSE, properties);
/*    */   }
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 33 */     return (BlockEntity)new TrappedChestBlockEntity(worldPosition, blockState);
/*    */   }
/*    */   
/*    */   protected Stat<Identifier> getOpenChestStat() {
/* 38 */     return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
/*    */   }
/*    */   
/*    */   protected boolean isSignalSource(BlockState state) {
/* 43 */     return true;
/*    */   }
/*    */   
/*    */   protected int ownSignal(BlockState state, BlockGetter level, BlockPos pos) {
/* 48 */     return Mth.clamp(ChestBlockEntity.getOpenCount(level, pos), 0, 15);
/*    */   }
/*    */   
/*    */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 53 */     if (direction == Direction.UP)
/* 54 */       return state.getSignal(level, pos, direction); 
/* 57 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\TrappedChestBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */