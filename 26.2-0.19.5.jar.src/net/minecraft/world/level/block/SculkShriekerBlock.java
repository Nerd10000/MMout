/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.SculkShriekerBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SculkShriekerBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*  34 */   public static final MapCodec<SculkShriekerBlock> CODEC = simpleCodec(SculkShriekerBlock::new);
/*     */   
/*     */   public MapCodec<SculkShriekerBlock> codec() {
/*  38 */     return CODEC;
/*     */   }
/*     */   
/*  41 */   public static final BooleanProperty SHRIEKING = BlockStateProperties.SHRIEKING;
/*     */   
/*  42 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  43 */   public static final BooleanProperty CAN_SUMMON = BlockStateProperties.CAN_SUMMON;
/*     */   
/*  45 */   private static final VoxelShape SHAPE_COLLISION = Block.column(16.0D, 0.0D, 8.0D);
/*     */   
/*  47 */   public static final double TOP_Y = SHAPE_COLLISION.max(Direction.Axis.Y);
/*     */   
/*     */   public SculkShriekerBlock(BlockBehaviour.Properties properties) {
/*  50 */     super(properties);
/*  52 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)SHRIEKING, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false))).setValue((Property)CAN_SUMMON, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  57 */     builder.add(new Property[] { (Property)SHRIEKING });
/*  58 */     builder.add(new Property[] { (Property)WATERLOGGED });
/*  59 */     builder.add(new Property[] { (Property)CAN_SUMMON });
/*     */   }
/*     */   
/*     */   public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
/*  64 */     if (level instanceof ServerLevel) {
/*  64 */       ServerLevel serverLevel = (ServerLevel)level;
/*  65 */       ServerPlayer player = SculkShriekerBlockEntity.tryGetPlayer(entity);
/*  66 */       if (player != null)
/*  67 */         serverLevel.getBlockEntity(pos, BlockEntityTypes.SCULK_SHRIEKER).ifPresent(shrieker -> shrieker.tryShriek(serverLevel, player)); 
/*     */     } 
/*  71 */     super.stepOn(level, pos, onState, entity);
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  76 */     if (((Boolean)state.getValue((Property)SHRIEKING)).booleanValue()) {
/*  77 */       level.setBlock(pos, (BlockState)state.setValue((Property)SHRIEKING, Boolean.valueOf(false)), 3);
/*  79 */       level.getBlockEntity(pos, BlockEntityTypes.SCULK_SHRIEKER).ifPresent(shrieker -> shrieker.tryRespond(level));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  85 */     return SHAPE_COLLISION;
/*     */   }
/*     */   
/*     */   protected VoxelShape getOcclusionShape(BlockState state) {
/*  90 */     return SHAPE_COLLISION;
/*     */   }
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState state) {
/*  95 */     return true;
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 100 */     return (BlockEntity)new SculkShriekerBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 105 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 106 */       ticks.scheduleTick(pos, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level)); 
/* 108 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 113 */     return (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf(context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER)));
/*     */   }
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 118 */     if (((Boolean)state.getValue((Property)WATERLOGGED)).booleanValue())
/* 119 */       return Fluids.WATER.getSource(false); 
/* 121 */     return super.getFluidState(state);
/*     */   }
/*     */   
/*     */   protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/* 126 */     super.spawnAfterBreak(state, level, pos, tool, dropExperience);
/* 127 */     if (dropExperience)
/* 128 */       tryDropExperience(level, pos, tool, (IntProvider)ConstantInt.of(5)); 
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 134 */     if (!level.isClientSide())
/* 135 */       return BaseEntityBlock.createTickerHelper(type, BlockEntityTypes.SCULK_SHRIEKER, (innerLevel, pos, state, entity) -> VibrationSystem.Ticker.tick(innerLevel, entity.getVibrationData(), entity.getVibrationUser())); 
/* 138 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SculkShriekerBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */