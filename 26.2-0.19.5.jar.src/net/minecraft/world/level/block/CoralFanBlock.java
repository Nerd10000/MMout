/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class CoralFanBlock extends BaseCoralFanBlock {
/*    */   public static final MapCodec<CoralFanBlock> CODEC;
/*    */   
/*    */   private final Block deadBlock;
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)CoralBlock.DEAD_CORAL_FIELD.forGetter(()), (App)propertiesCodec()).apply((Applicative)i, CoralFanBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<CoralFanBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected CoralFanBlock(Block deadBlock, BlockBehaviour.Properties properties) {
/* 29 */     super(properties);
/* 30 */     this.deadBlock = deadBlock;
/*    */   }
/*    */   
/*    */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 35 */     tryScheduleDieTick(state, (BlockGetter)level, (ScheduledTickAccess)level, level.getRandom(), pos);
/*    */   }
/*    */   
/*    */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 40 */     if (!scanForWater(state, (BlockGetter)level, pos))
/* 41 */       level.setBlock(pos, (BlockState)this.deadBlock.defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf(false)), 2); 
/*    */   }
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 47 */     if (directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos))
/* 48 */       return Blocks.AIR.defaultBlockState(); 
/* 51 */     tryScheduleDieTick(state, (BlockGetter)level, ticks, random, pos);
/* 53 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 54 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/* 57 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\CoralFanBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */