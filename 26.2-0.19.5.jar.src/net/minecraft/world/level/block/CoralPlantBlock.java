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
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class CoralPlantBlock extends BaseCoralPlantTypeBlock {
/*    */   public static final MapCodec<CoralPlantBlock> CODEC;
/*    */   
/*    */   private final Block deadBlock;
/*    */   
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)CoralBlock.DEAD_CORAL_FIELD.forGetter(()), (App)propertiesCodec()).apply((Applicative)i, CoralPlantBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<CoralPlantBlock> codec() {
/* 26 */     return CODEC;
/*    */   }
/*    */   
/* 31 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 15.0D);
/*    */   
/*    */   protected CoralPlantBlock(Block deadBlock, BlockBehaviour.Properties properties) {
/* 34 */     super(properties);
/* 35 */     this.deadBlock = deadBlock;
/*    */   }
/*    */   
/*    */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 40 */     tryScheduleDieTick(state, (BlockGetter)level, (ScheduledTickAccess)level, level.getRandom(), pos);
/*    */   }
/*    */   
/*    */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 45 */     if (!scanForWater(state, (BlockGetter)level, pos))
/* 46 */       level.setBlock(pos, (BlockState)this.deadBlock.defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf(false)), 2); 
/*    */   }
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 52 */     if (directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos))
/* 53 */       return Blocks.AIR.defaultBlockState(); 
/* 56 */     tryScheduleDieTick(state, (BlockGetter)level, ticks, random, pos);
/* 58 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 59 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/* 62 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 67 */     return SHAPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\CoralPlantBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */