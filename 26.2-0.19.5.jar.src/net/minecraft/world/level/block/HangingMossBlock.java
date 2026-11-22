/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class HangingMossBlock extends Block implements BonemealableBlock {
/*  23 */   public static final MapCodec<HangingMossBlock> CODEC = simpleCodec(HangingMossBlock::new);
/*     */   
/*  25 */   private static final VoxelShape SHAPE_BASE = Block.column(14.0D, 0.0D, 16.0D);
/*     */   
/*  26 */   private static final VoxelShape SHAPE_TIP = Block.column(14.0D, 2.0D, 16.0D);
/*     */   
/*     */   public MapCodec<HangingMossBlock> codec() {
/*  30 */     return CODEC;
/*     */   }
/*     */   
/*  33 */   public static final BooleanProperty TIP = BlockStateProperties.TIP;
/*     */   
/*     */   public HangingMossBlock(BlockBehaviour.Properties properties) {
/*  36 */     super(properties);
/*  37 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)TIP, Boolean.valueOf(true)));
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  42 */     return ((Boolean)state.getValue((Property)TIP)).booleanValue() ? SHAPE_TIP : SHAPE_BASE;
/*     */   }
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  47 */     if (random.nextInt(500) == 0) {
/*  48 */       BlockState above = level.getBlockState(pos.above());
/*  49 */       if (above.is(BlockTags.PALE_OAK_LOGS) || above.is(Blocks.PALE_OAK_LEAVES))
/*  50 */         level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.PALE_HANGING_MOSS_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState state) {
/*  57 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  62 */     return canStayAtPosition((BlockGetter)level, pos);
/*     */   }
/*     */   
/*     */   private boolean canStayAtPosition(BlockGetter level, BlockPos pos) {
/*  66 */     BlockPos neighbourPos = pos.relative(Direction.UP);
/*  67 */     BlockState blockState = level.getBlockState(neighbourPos);
/*  68 */     return (MultifaceBlock.canAttachTo(level, Direction.UP, neighbourPos, blockState) || blockState.is(this));
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  73 */     if (!canStayAtPosition((BlockGetter)level, pos))
/*  74 */       ticks.scheduleTick(pos, this, 1); 
/*  76 */     return (BlockState)state.setValue((Property)TIP, Boolean.valueOf(!level.getBlockState(pos.below()).is(this)));
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  81 */     if (!canStayAtPosition((BlockGetter)level, pos))
/*  82 */       level.destroyBlock(pos, true); 
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  88 */     builder.add(new Property[] { (Property)TIP });
/*     */   }
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/*  93 */     BlockPos growPos = getTip((BlockGetter)level, pos).below();
/*  94 */     return (canGrowInto(level.getBlockState(growPos)) && level.isInsideBuildHeight(growPos));
/*     */   }
/*     */   
/*     */   private boolean canGrowInto(BlockState state) {
/*  98 */     return state.isAir();
/*     */   }
/*     */   
/*     */   public BlockPos getTip(BlockGetter level, BlockPos pos) {
/*     */     BlockState forwardState;
/* 102 */     BlockPos.MutableBlockPos forwardPos = pos.mutable();
/*     */     do {
/* 105 */       forwardPos.move(Direction.DOWN);
/* 106 */       forwardState = level.getBlockState((BlockPos)forwardPos);
/* 107 */     } while (forwardState.is(this));
/* 109 */     return forwardPos.relative(Direction.UP).immutable();
/*     */   }
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 114 */     return true;
/*     */   }
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 119 */     BlockPos tipPos = getTip((BlockGetter)level, pos).below();
/* 121 */     if (!canGrowInto(level.getBlockState(tipPos)))
/*     */       return; 
/* 124 */     level.setBlockAndUpdate(tipPos, (BlockState)state.setValue((Property)TIP, Boolean.valueOf(true)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\HangingMossBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */