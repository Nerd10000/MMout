/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.DaylightDetectorBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DaylightDetectorBlock extends BaseEntityBlock {
/*  30 */   public static final MapCodec<DaylightDetectorBlock> CODEC = simpleCodec(DaylightDetectorBlock::new);
/*     */   
/*     */   public MapCodec<DaylightDetectorBlock> codec() {
/*  34 */     return CODEC;
/*     */   }
/*     */   
/*  37 */   public static final IntegerProperty POWER = BlockStateProperties.POWER;
/*     */   
/*  38 */   public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
/*     */   
/*  40 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 6.0D);
/*     */   
/*     */   public DaylightDetectorBlock(BlockBehaviour.Properties properties) {
/*  43 */     super(properties);
/*  45 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWER, Integer.valueOf(0))).setValue((Property)INVERTED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  50 */     return SHAPE;
/*     */   }
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState state) {
/*  55 */     return true;
/*     */   }
/*     */   
/*     */   private static void updateSignalStrength(BlockState state, Level level, BlockPos pos) {
/*  59 */     int target = level.getEffectiveSkyBrightness(pos);
/*  60 */     float sunAngle = ((Float)level.environmentAttributes().getValue(EnvironmentAttributes.SUN_ANGLE, pos)).floatValue() * 0.017453292F;
/*  62 */     boolean isInverted = ((Boolean)state.getValue((Property)INVERTED)).booleanValue();
/*  63 */     if (isInverted) {
/*  64 */       target = 15 - target;
/*  65 */     } else if (target > 0) {
/*  67 */       float offset = (sunAngle < 3.1415927F) ? 0.0F : 6.2831855F;
/*  68 */       sunAngle += (offset - sunAngle) * 0.2F;
/*  70 */       target = Math.round(target * Mth.cos(sunAngle));
/*     */     } 
/*  72 */     target = Mth.clamp(target, 0, 15);
/*  74 */     if (((Integer)state.getValue((Property)POWER)).intValue() != target)
/*  75 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWER, Integer.valueOf(target)), 3); 
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  81 */     if (!player.mayBuild())
/*  82 */       return super.useWithoutItem(state, level, pos, player, hitResult); 
/*  84 */     if (!level.isClientSide()) {
/*  85 */       BlockState newState = (BlockState)state.cycle((Property)INVERTED);
/*  86 */       level.setBlock(pos, newState, 2);
/*  87 */       level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of((Entity)player, newState));
/*  88 */       updateSignalStrength(newState, level, pos);
/*     */     } 
/*  90 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected boolean isSignalSource(BlockState state) {
/*  95 */     return true;
/*     */   }
/*     */   
/*     */   protected int ownSignal(BlockState state, BlockGetter level, BlockPos pos) {
/* 100 */     return ((Integer)state.getValue((Property)POWER)).intValue();
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 105 */     return (BlockEntity)new DaylightDetectorBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 110 */     if (!level.isClientSide() && level.dimensionType().hasSkyLight())
/* 111 */       return createTickerHelper(type, BlockEntityTypes.DAYLIGHT_DETECTOR, DaylightDetectorBlock::tickEntity); 
/* 113 */     return null;
/*     */   }
/*     */   
/*     */   private static void tickEntity(Level level, BlockPos blockPos, BlockState blockState, DaylightDetectorBlockEntity blockEntity) {
/* 117 */     if (level.getGameTime() % 20L == 0L)
/* 118 */       updateSignalStrength(blockState, level, blockPos); 
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 124 */     builder.add(new Property[] { (Property)POWER, (Property)INVERTED });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\DaylightDetectorBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */