/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
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
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FlowingFluid;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.EntityCollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LiquidBlock extends Block implements BucketPickup {
/*     */   private static final Codec<FlowingFluid> FLOWING_FLUID;
/*     */   
/*     */   public static final MapCodec<LiquidBlock> CODEC;
/*     */   
/*     */   static {
/*  44 */     FLOWING_FLUID = BuiltInRegistries.FLUID.byNameCodec().comapFlatMap(fluid -> {
/*     */           FlowingFluid flowing = (FlowingFluid)fluid;
/*  44 */           return (Function)((fluid instanceof FlowingFluid) ? DataResult.success(flowing) : DataResult.error(()));
/*     */         }fluid -> fluid);
/*  46 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)FLOWING_FLUID.fieldOf("fluid").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, LiquidBlock::new));
/*     */   }
/*     */   
/*     */   public MapCodec<LiquidBlock> codec() {
/*  53 */     return CODEC;
/*     */   }
/*     */   
/*  56 */   public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;
/*     */   
/*     */   protected final FlowingFluid fluid;
/*     */   
/*     */   private final List<FluidState> stateCache;
/*     */   
/*  61 */   public static final ImmutableList<Direction> POSSIBLE_FLOW_DIRECTIONS = ImmutableList.of(Direction.DOWN, Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST);
/*     */   
/*     */   private static final int BUBBLE_COLUMN_CHECK_DELAY = 20;
/*     */   
/*     */   protected LiquidBlock(FlowingFluid fluid, BlockBehaviour.Properties properties) {
/*  65 */     super(properties);
/*  66 */     this.fluid = fluid;
/*  67 */     this.stateCache = Lists.newArrayList();
/*  68 */     this.stateCache.add(fluid.getSource(false));
/*  69 */     for (int level = 1; level < 8; level++)
/*  70 */       this.stateCache.add(fluid.getFlowing(8 - level, false)); 
/*  72 */     this.stateCache.add(fluid.getFlowing(8, true));
/*  73 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LEVEL, Integer.valueOf(0)));
/*     */   }
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  78 */     if (context.alwaysCollideWithFluid())
/*  79 */       return Shapes.block(); 
/*  82 */     if (((Integer)state.getValue((Property)LEVEL)).intValue() != 0)
/*  83 */       return Shapes.empty(); 
/*  86 */     return ifMobIsColliding(context)
/*  87 */       .<VoxelShape>map(LivingEntity::getLiquidCollisionShape)
/*  88 */       .filter(liquidStableShape -> (context.isAbove(liquidStableShape, pos, true) && context.canStandOnFluid(level.getFluidState(pos.above()), state.getFluidState())))
/*  89 */       .orElse(Shapes.empty());
/*     */   }
/*     */   
/*     */   private Optional<LivingEntity> ifMobIsColliding(CollisionContext context) {
/*  93 */     if (context instanceof EntityCollisionContext) {
/*  93 */       EntityCollisionContext entityCollisionContext = (EntityCollisionContext)context;
/*  93 */       Entity entity = entityCollisionContext.getEntity();
/*  93 */       if (entity instanceof LivingEntity) {
/*  93 */         LivingEntity mob = (LivingEntity)entity;
/*  94 */         return Optional.of(mob);
/*     */       } 
/*     */     } 
/*  96 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState state) {
/* 101 */     return state.getFluidState().isRandomlyTicking();
/*     */   }
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 106 */     state.getFluidState().randomTick(level, pos, random);
/*     */   }
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState state) {
/* 111 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 116 */     return !this.fluid.is(FluidTags.LAVA);
/*     */   }
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 121 */     int level = ((Integer)state.getValue((Property)LEVEL)).intValue();
/* 122 */     return this.stateCache.get(Math.min(level, 8));
/*     */   }
/*     */   
/*     */   protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
/* 127 */     return neighborState.getFluidState().getType().isSame((Fluid)this.fluid);
/*     */   }
/*     */   
/*     */   protected RenderShape getRenderShape(BlockState state) {
/* 132 */     return RenderShape.INVISIBLE;
/*     */   }
/*     */   
/*     */   protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
/* 137 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 142 */     return Shapes.empty();
/*     */   }
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 147 */     if (shouldSpreadLiquid(level, pos, state))
/* 148 */       level.scheduleTick(pos, state.getFluidState().getType(), this.fluid.getTickDelay((LevelReader)level)); 
/* 151 */     if (shouldBubbleColumnOccupy(state)) {
/* 152 */       BlockState stateBelow = level.getBlockState(pos.below());
/* 153 */       tryScheduleBubbleBlockColumn((ScheduledTickAccess)level, pos, stateBelow);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 159 */     if (shouldBubbleColumnOccupy(state)) {
/* 160 */       BlockState stateBelow = level.getBlockState(pos.below());
/* 161 */       BubbleColumnBlock.updateColumn(Blocks.BUBBLE_COLUMN, (LevelAccessor)level, pos, stateBelow);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 167 */     if (state.getFluidState().isSource() || neighbourState.getFluidState().isSource())
/* 168 */       ticks.scheduleTick(pos, state.getFluidState().getType(), this.fluid.getTickDelay(level)); 
/* 171 */     if (directionToNeighbour == Direction.DOWN && 
/* 172 */       shouldBubbleColumnOccupy(state))
/* 173 */       tryScheduleBubbleBlockColumn(ticks, pos, neighbourState); 
/* 177 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   private static boolean shouldBubbleColumnOccupy(BlockState state) {
/* 181 */     return (state.getFluidState().is(FluidTags.BUBBLE_COLUMN_CAN_OCCUPY) && state.getFluidState().isSource() && state.getFluidState().isFull());
/*     */   }
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 186 */     if (shouldSpreadLiquid(level, pos, state))
/* 187 */       level.scheduleTick(pos, state.getFluidState().getType(), this.fluid.getTickDelay((LevelReader)level)); 
/* 190 */     if (shouldBubbleColumnOccupy(state)) {
/* 191 */       BlockState stateBelow = level.getBlockState(pos.below());
/* 192 */       tryScheduleBubbleBlockColumn((ScheduledTickAccess)level, pos, stateBelow);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tryScheduleBubbleBlockColumn(ScheduledTickAccess ticks, BlockPos pos, BlockState stateBelow) {
/* 197 */     if (stateBelow.is(BlockTags.ENABLES_BUBBLE_COLUMN_DRAG_DOWN) || stateBelow.is(BlockTags.ENABLES_BUBBLE_COLUMN_PUSH_UP))
/* 198 */       ticks.scheduleTick(pos, this, 20); 
/*     */   }
/*     */   
/*     */   private boolean shouldSpreadLiquid(Level level, BlockPos pos, BlockState state) {
/* 203 */     if (this.fluid.is(FluidTags.LAVA)) {
/* 204 */       boolean isOverSoulSoil = level.getBlockState(pos.below()).is(Blocks.SOUL_SOIL);
/* 206 */       for (UnmodifiableIterator<Direction> unmodifiableIterator = POSSIBLE_FLOW_DIRECTIONS.iterator(); unmodifiableIterator.hasNext(); ) {
/* 206 */         Direction direction = unmodifiableIterator.next();
/* 207 */         BlockPos neighbourPos = pos.relative(direction.getOpposite());
/* 209 */         if (level.getFluidState(neighbourPos).is(FluidTags.WATER)) {
/* 210 */           Block convertToBlock = level.getFluidState(pos).isSource() ? Blocks.OBSIDIAN : Blocks.COBBLESTONE;
/* 211 */           level.setBlockAndUpdate(pos, convertToBlock.defaultBlockState());
/* 212 */           fizz((LevelAccessor)level, pos);
/* 213 */           return false;
/*     */         } 
/* 216 */         if (isOverSoulSoil && level.getBlockState(neighbourPos).is(Blocks.BLUE_ICE)) {
/* 217 */           level.setBlockAndUpdate(pos, Blocks.BASALT.defaultBlockState());
/* 218 */           fizz((LevelAccessor)level, pos);
/* 219 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/* 223 */     return true;
/*     */   }
/*     */   
/*     */   private void fizz(LevelAccessor level, BlockPos pos) {
/* 227 */     level.levelEvent(1501, pos, 0);
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 232 */     builder.add(new Property[] { (Property)LEVEL });
/*     */   }
/*     */   
/*     */   public ItemStack pickupBlock(LivingEntity user, LevelAccessor level, BlockPos pos, BlockState state) {
/* 237 */     if (((Integer)state.getValue((Property)LEVEL)).intValue() == 0) {
/* 238 */       level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
/* 239 */       return new ItemStack((ItemLike)this.fluid.getBucket());
/*     */     } 
/* 241 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   public Optional<SoundEvent> getPickupSound() {
/* 246 */     return this.fluid.getPickupSound();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\LiquidBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */