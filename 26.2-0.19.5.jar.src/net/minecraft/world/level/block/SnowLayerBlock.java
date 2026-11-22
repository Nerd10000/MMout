/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SnowLayerBlock extends Block {
/*  24 */   public static final MapCodec<SnowLayerBlock> CODEC = simpleCodec(SnowLayerBlock::new);
/*     */   
/*     */   public static final int MAX_HEIGHT = 8;
/*     */   
/*     */   public MapCodec<SnowLayerBlock> codec() {
/*  28 */     return CODEC;
/*     */   }
/*     */   
/*  32 */   public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
/*     */   
/*     */   private static final VoxelShape[] SHAPES;
/*     */   
/*     */   public static final int HEIGHT_IMPASSABLE = 5;
/*     */   
/*     */   static {
/*  34 */     SHAPES = Block.boxes(8, height -> Block.column(16.0D, 0.0D, (height * 2)));
/*     */   }
/*     */   
/*     */   protected SnowLayerBlock(BlockBehaviour.Properties properties) {
/*  39 */     super(properties);
/*  40 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LAYERS, Integer.valueOf(1)));
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/*  45 */     if (type == PathComputationType.LAND)
/*  46 */       return (((Integer)state.getValue((Property)LAYERS)).intValue() < 5); 
/*  48 */     return false;
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  53 */     return SHAPES[((Integer)state.getValue((Property)LAYERS)).intValue()];
/*     */   }
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  58 */     return SHAPES[((Integer)state.getValue((Property)LAYERS)).intValue() - 1];
/*     */   }
/*     */   
/*     */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
/*  63 */     return SHAPES[((Integer)state.getValue((Property)LAYERS)).intValue()];
/*     */   }
/*     */   
/*     */   protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  68 */     return SHAPES[((Integer)state.getValue((Property)LAYERS)).intValue()];
/*     */   }
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState state) {
/*  73 */     return true;
/*     */   }
/*     */   
/*     */   protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
/*  78 */     return (((Integer)state.getValue((Property)LAYERS)).intValue() == 8) ? 0.2F : 1.0F;
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  83 */     BlockState belowState = level.getBlockState(pos.below());
/*  85 */     if (belowState.is(BlockTags.CANNOT_SUPPORT_SNOW_LAYER))
/*  86 */       return false; 
/*  88 */     if (belowState.is(BlockTags.SUPPORT_OVERRIDE_SNOW_LAYER))
/*  89 */       return true; 
/*  92 */     return (Block.isFaceFull(belowState.getCollisionShape((BlockGetter)level, pos.below()), Direction.UP) || (belowState.is(this) && ((Integer)belowState.getValue((Property)LAYERS)).intValue() == 8));
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  97 */     if (!state.canSurvive(level, pos))
/*  98 */       return Blocks.AIR.defaultBlockState(); 
/* 100 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 105 */     if (level.getBrightness(LightLayer.BLOCK, pos) > 11) {
/* 106 */       dropResources(state, (Level)level, pos);
/* 107 */       level.removeBlock(pos, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/* 113 */     int layers = ((Integer)state.getValue((Property)LAYERS)).intValue();
/* 115 */     if (context.getItemInHand().is(asItem()) && layers < 8) {
/* 116 */       if (context.replacingClickedOnBlock())
/* 117 */         return (context.getClickedFace() == Direction.UP); 
/* 119 */       return true;
/*     */     } 
/* 122 */     return (layers == 1);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 127 */     BlockState state = context.getLevel().getBlockState(context.getClickedPos());
/* 128 */     if (state.is(this)) {
/* 129 */       int layers = ((Integer)state.getValue((Property)LAYERS)).intValue();
/* 130 */       return (BlockState)state.setValue((Property)LAYERS, Integer.valueOf(Math.min(8, layers + 1)));
/*     */     } 
/* 133 */     return super.getStateForPlacement(context);
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 138 */     builder.add(new Property[] { (Property)LAYERS });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SnowLayerBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */