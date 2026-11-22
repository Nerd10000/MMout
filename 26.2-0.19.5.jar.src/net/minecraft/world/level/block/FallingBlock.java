/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.particles.BlockParticleOption;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.ParticleUtils;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class FallingBlock extends Block implements Fallable {
/*    */   public FallingBlock(BlockBehaviour.Properties properties) {
/* 23 */     super(properties);
/*    */   }
/*    */   
/*    */   protected abstract MapCodec<? extends FallingBlock> codec();
/*    */   
/*    */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 31 */     level.scheduleTick(pos, this, getDelayAfterPlace());
/*    */   }
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 36 */     ticks.scheduleTick(pos, this, getDelayAfterPlace());
/* 38 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */   
/*    */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 43 */     if (!isFree(level.getBlockState(pos.below())) || pos.getY() < level.getMinY())
/*    */       return; 
/* 47 */     FallingBlockEntity entity = FallingBlockEntity.fall((Level)level, pos, state);
/* 48 */     falling(entity);
/*    */   }
/*    */   
/*    */   protected void falling(FallingBlockEntity entity) {}
/*    */   
/*    */   protected int getDelayAfterPlace() {
/* 55 */     return 2;
/*    */   }
/*    */   
/*    */   public static boolean isFree(BlockState state) {
/* 60 */     return (state.isAir() || state.is(BlockTags.FIRE) || state.liquid() || state.canBeReplaced());
/*    */   }
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 65 */     if (random.nextInt(16) == 0) {
/* 66 */       BlockPos below = pos.below();
/* 68 */       if (isFree(level.getBlockState(below)))
/* 69 */         ParticleUtils.spawnParticleBelow(level, pos, random, (ParticleOptions)new BlockParticleOption(ParticleTypes.FALLING_DUST, state)); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract int getDustColor(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\FallingBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */