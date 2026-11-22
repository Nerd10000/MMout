/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.lighting.LightEngine;
/*    */ 
/*    */ public abstract class SpreadingSnowyBlock extends SnowyBlock {
/*    */   private final ResourceKey<Block> baseBlock;
/*    */   
/*    */   protected SpreadingSnowyBlock(BlockBehaviour.Properties properties, ResourceKey<Block> baseBlock) {
/* 24 */     super(properties);
/* 25 */     this.baseBlock = baseBlock;
/*    */   }
/*    */   
/*    */   protected abstract MapCodec<? extends SpreadingSnowyBlock> codec();
/*    */   
/*    */   private static boolean canStayAlive(BlockState state, LevelReader level, BlockPos pos) {
/* 32 */     BlockPos above = pos.above();
/* 33 */     BlockState aboveState = level.getBlockState(above);
/* 34 */     if (aboveState.is(Blocks.SNOW) && ((Integer)aboveState.getValue((Property)SnowLayerBlock.LAYERS)).intValue() == 1)
/* 35 */       return true; 
/* 38 */     if (aboveState.getFluidState().isFull())
/* 39 */       return false; 
/* 43 */     int lightDampeningTopFace = LightEngine.getLightDampeningInto(state, aboveState, Direction.UP, aboveState.getLightDampening());
/* 45 */     return (lightDampeningTopFace < 15);
/*    */   }
/*    */   
/*    */   private static boolean canPropagate(BlockState state, LevelReader level, BlockPos pos) {
/* 49 */     BlockPos above = pos.above();
/* 50 */     return (canStayAlive(state, level, pos) && !level.getFluidState(above).is(FluidTags.WATER));
/*    */   }
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 55 */     Registry<Block> blocks = level.registryAccess().lookupOrThrow(Registries.BLOCK);
/* 56 */     Optional<Block> baseBlock = blocks.getOptional(this.baseBlock);
/* 58 */     if (baseBlock.isEmpty())
/*    */       return; 
/* 62 */     if (!canStayAlive(state, (LevelReader)level, pos)) {
/* 63 */       level.setBlockAndUpdate(pos, ((Block)baseBlock.get()).defaultBlockState());
/*    */       return;
/*    */     } 
/* 67 */     if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
/* 68 */       BlockState defaultBlockState = defaultBlockState();
/* 70 */       for (int i = 0; i < 4; i++) {
/* 71 */         BlockPos testPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
/* 72 */         if (level.getBlockState(testPos).is(baseBlock.get()) && canPropagate(defaultBlockState, (LevelReader)level, testPos))
/* 73 */           level.setBlockAndUpdate(testPos, (BlockState)defaultBlockState.setValue((Property)SNOWY, Boolean.valueOf(isSnowySetting(level.getBlockState(testPos.above()))))); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SpreadingSnowyBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */