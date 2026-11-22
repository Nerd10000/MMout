/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BrewingStandBlock extends BaseEntityBlock {
/*  33 */   public static final MapCodec<BrewingStandBlock> CODEC = simpleCodec(BrewingStandBlock::new);
/*     */   
/*     */   public MapCodec<BrewingStandBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */   
/*  40 */   public static final BooleanProperty[] HAS_BOTTLE = new BooleanProperty[] { BlockStateProperties.HAS_BOTTLE_0, BlockStateProperties.HAS_BOTTLE_1, BlockStateProperties.HAS_BOTTLE_2 };
/*     */   
/*  46 */   private static final VoxelShape SHAPE = Shapes.or(
/*  47 */       Block.column(2.0D, 2.0D, 14.0D), 
/*  48 */       Block.column(14.0D, 0.0D, 2.0D));
/*     */   
/*     */   public BrewingStandBlock(BlockBehaviour.Properties properties) {
/*  52 */     super(properties);
/*  53 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HAS_BOTTLE[0], Boolean.valueOf(false))).setValue((Property)HAS_BOTTLE[1], Boolean.valueOf(false))).setValue((Property)HAS_BOTTLE[2], Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  58 */     return (BlockEntity)new BrewingStandBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/*  63 */     return level.isClientSide() ? null : createTickerHelper(type, BlockEntityTypes.BREWING_STAND, BrewingStandBlockEntity::serverTick);
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  68 */     return SHAPE;
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  73 */     if (!level.isClientSide()) {
/*  73 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/*  73 */       if (blockEntity instanceof BrewingStandBlockEntity) {
/*  73 */         BrewingStandBlockEntity brewingStandBlockEntity = (BrewingStandBlockEntity)blockEntity;
/*  74 */         player.openMenu((MenuProvider)brewingStandBlockEntity);
/*  75 */         player.awardStat(Stats.INTERACT_WITH_BREWINGSTAND);
/*     */       } 
/*     */     } 
/*  77 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  82 */     double x = pos.getX() + 0.4D + random.nextFloat() * 0.2D;
/*  83 */     double y = pos.getY() + 0.7D + random.nextFloat() * 0.3D;
/*  84 */     double z = pos.getZ() + 0.4D + random.nextFloat() * 0.2D;
/*  86 */     level.addParticle((ParticleOptions)ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/*  91 */     Containers.updateNeighboursAfterDestroy(state, (Level)level, pos);
/*     */   }
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/*  96 */     return true;
/*     */   }
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 101 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 106 */     builder.add(new Property[] { (Property)HAS_BOTTLE[0], (Property)HAS_BOTTLE[1], (Property)HAS_BOTTLE[2] });
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 111 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\BrewingStandBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */