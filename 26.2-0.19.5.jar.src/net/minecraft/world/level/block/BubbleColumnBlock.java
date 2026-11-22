/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BubbleColumnBlock extends Block implements BucketPickup {
/*  38 */   public static final MapCodec<BubbleColumnBlock> CODEC = simpleCodec(BubbleColumnBlock::new);
/*     */   
/*     */   public MapCodec<BubbleColumnBlock> codec() {
/*  42 */     return CODEC;
/*     */   }
/*     */   
/*  45 */   public static final BooleanProperty DRAG_DOWN = BlockStateProperties.DRAG;
/*     */   
/*     */   private static final int CHECK_PERIOD = 5;
/*     */   
/*     */   public BubbleColumnBlock(BlockBehaviour.Properties properties) {
/*  49 */     super(properties);
/*  50 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)DRAG_DOWN, Boolean.valueOf(true)));
/*     */   }
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/*  55 */     if (isPrecise) {
/*  56 */       BlockState stateAbove = level.getBlockState(pos.above());
/*  57 */       boolean nothingAbove = (stateAbove.getCollisionShape((BlockGetter)level, pos).isEmpty() && stateAbove.getFluidState().isEmpty());
/*  58 */       if (nothingAbove) {
/*  59 */         entity.onAboveBubbleColumn(((Boolean)state.getValue((Property)DRAG_DOWN)).booleanValue(), pos);
/*     */       } else {
/*  61 */         entity.onInsideBubbleColumn(((Boolean)state.getValue((Property)DRAG_DOWN)).booleanValue());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  68 */     updateColumn(this, (LevelAccessor)level, pos, state, level.getBlockState(pos.below()));
/*     */   }
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/*  73 */     return Fluids.WATER.getSource(false);
/*     */   }
/*     */   
/*     */   public static void updateColumn(Block bubbleColumn, LevelAccessor level, BlockPos occupyAt, BlockState belowState) {
/*  77 */     updateColumn(bubbleColumn, level, occupyAt, level.getBlockState(occupyAt), belowState);
/*     */   }
/*     */   
/*     */   public static void updateColumn(Block bubbleColumn, LevelAccessor level, BlockPos occupyAt, BlockState occupyState, BlockState belowState) {
/*  81 */     if (!canOccupy(bubbleColumn, occupyState))
/*     */       return; 
/*  84 */     BlockState columnState = getColumnState(bubbleColumn, belowState, occupyState);
/*  85 */     level.setBlock(occupyAt, columnState, 2);
/*  87 */     BlockPos.MutableBlockPos pos = occupyAt.mutable().move(Direction.UP);
/*  88 */     while (canOccupy(bubbleColumn, level.getBlockState((BlockPos)pos))) {
/*  89 */       if (!level.setBlock((BlockPos)pos, columnState, 2))
/*     */         return; 
/*  92 */       pos.move(Direction.UP);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean canOccupy(Block bubbleColumn, BlockState occupyState) {
/*  97 */     if (occupyState.is(bubbleColumn))
/*  98 */       return true; 
/* 101 */     FluidState occupyFluid = occupyState.getFluidState();
/* 102 */     return (occupyFluid.is(FluidTags.BUBBLE_COLUMN_CAN_OCCUPY) && occupyState.getBlock() instanceof LiquidBlock && occupyFluid.isSource() && occupyFluid.getAmount() >= 8);
/*     */   }
/*     */   
/*     */   private static BlockState getColumnState(Block bubbleColumn, BlockState belowState, BlockState occupyState) {
/* 106 */     if (belowState.is(bubbleColumn))
/* 107 */       return belowState; 
/* 109 */     if (belowState.is(BlockTags.ENABLES_BUBBLE_COLUMN_PUSH_UP))
/* 110 */       return (BlockState)bubbleColumn.defaultBlockState().setValue((Property)DRAG_DOWN, Boolean.valueOf(false)); 
/* 112 */     if (belowState.is(BlockTags.ENABLES_BUBBLE_COLUMN_DRAG_DOWN))
/* 113 */       return (BlockState)bubbleColumn.defaultBlockState().setValue((Property)DRAG_DOWN, Boolean.valueOf(true)); 
/* 115 */     if (occupyState.is(bubbleColumn))
/* 116 */       return Blocks.WATER.defaultBlockState(); 
/* 119 */     return occupyState;
/*     */   }
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 124 */     double x = pos.getX();
/* 125 */     double y = pos.getY();
/* 126 */     double z = pos.getZ();
/* 128 */     if (((Boolean)state.getValue((Property)DRAG_DOWN)).booleanValue()) {
/* 129 */       level.addAlwaysVisibleParticle((ParticleOptions)ParticleTypes.CURRENT_DOWN, x + 0.5D, y + 0.8D, z, 0.0D, 0.0D, 0.0D);
/* 130 */       if (random.nextInt(200) == 0)
/* 131 */         level.playLocalSound(x, y, z, SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false); 
/*     */     } else {
/* 134 */       level.addAlwaysVisibleParticle((ParticleOptions)ParticleTypes.BUBBLE_COLUMN_UP, x + 0.5D, y, z + 0.5D, 0.0D, 0.04D, 0.0D);
/* 135 */       level.addAlwaysVisibleParticle((ParticleOptions)ParticleTypes.BUBBLE_COLUMN_UP, x + random.nextFloat(), y + random.nextFloat(), z + random.nextFloat(), 0.0D, 0.04D, 0.0D);
/* 136 */       if (random.nextInt(200) == 0)
/* 137 */         level.playLocalSound(x, y, z, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 144 */     ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/* 146 */     if (!state.canSurvive(level, pos) || directionToNeighbour == Direction.DOWN || (directionToNeighbour == Direction.UP && 
/*     */       
/* 148 */       !neighbourState.is(this) && canOccupy(this, neighbourState)))
/* 150 */       ticks.scheduleTick(pos, this, 5); 
/* 153 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 158 */     BlockState belowState = level.getBlockState(pos.below());
/* 160 */     return (belowState.is(this) || belowState.is(BlockTags.ENABLES_BUBBLE_COLUMN_PUSH_UP) || belowState.is(BlockTags.ENABLES_BUBBLE_COLUMN_DRAG_DOWN));
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 165 */     return Shapes.empty();
/*     */   }
/*     */   
/*     */   protected RenderShape getRenderShape(BlockState state) {
/* 170 */     return RenderShape.INVISIBLE;
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 175 */     builder.add(new Property[] { (Property)DRAG_DOWN });
/*     */   }
/*     */   
/*     */   public ItemStack pickupBlock(LivingEntity user, LevelAccessor level, BlockPos pos, BlockState state) {
/* 180 */     level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
/* 181 */     return new ItemStack((ItemLike)Items.WATER_BUCKET);
/*     */   }
/*     */   
/*     */   public Optional<SoundEvent> getPickupSound() {
/* 186 */     return Fluids.WATER.getPickupSound();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\BubbleColumnBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */