/*     */ package net.minecraft.world.level.block.piston;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.BaseEntityBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.PistonType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class MovingPistonBlock extends BaseEntityBlock {
/*  39 */   public static final MapCodec<MovingPistonBlock> CODEC = simpleCodec(MovingPistonBlock::new);
/*     */   
/*     */   public MapCodec<MovingPistonBlock> codec() {
/*  43 */     return CODEC;
/*     */   }
/*     */   
/*  46 */   public static final EnumProperty<Direction> FACING = PistonHeadBlock.FACING;
/*     */   
/*  47 */   public static final EnumProperty<PistonType> TYPE = PistonHeadBlock.TYPE;
/*     */   
/*     */   public MovingPistonBlock(BlockBehaviour.Properties properties) {
/*  50 */     super(properties);
/*  51 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)TYPE, (Comparable)PistonType.DEFAULT));
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  56 */     return null;
/*     */   }
/*     */   
/*     */   public static BlockEntity newMovingBlockEntity(BlockPos position, BlockState blockState, BlockState movedState, Direction direction, boolean extending, boolean isSourcePiston) {
/*  61 */     return new PistonMovingBlockEntity(position, blockState, movedState, direction, extending, isSourcePiston);
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/*  66 */     return createTickerHelper(type, BlockEntityTypes.PISTON, PistonMovingBlockEntity::tick);
/*     */   }
/*     */   
/*     */   public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
/*  72 */     BlockPos relative = pos.relative(((Direction)state.getValue((Property)FACING)).getOpposite());
/*  73 */     BlockState blockState = level.getBlockState(relative);
/*  74 */     if (blockState.getBlock() instanceof PistonBaseBlock && ((Boolean)blockState.getValue((Property)PistonBaseBlock.EXTENDED)).booleanValue())
/*  75 */       level.removeBlock(relative, false); 
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  82 */     if (!level.isClientSide() && level.getBlockEntity(pos) == null) {
/*  84 */       level.removeBlock(pos, false);
/*  85 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     } 
/*  87 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
/*  93 */     PistonMovingBlockEntity entity = getBlockEntity((BlockGetter)params.getLevel(), BlockPos.containing((Position)params.getParameter(LootContextParams.ORIGIN)));
/*  94 */     if (entity == null)
/*  95 */       return Collections.emptyList(); 
/*  98 */     return entity.getMovedState().getDrops(params);
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 104 */     return Shapes.empty();
/*     */   }
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 109 */     PistonMovingBlockEntity blockEntity = getBlockEntity(level, pos);
/* 110 */     if (blockEntity != null)
/* 111 */       return blockEntity.getCollisionShape(level, pos); 
/* 113 */     return Shapes.empty();
/*     */   }
/*     */   
/*     */   private PistonMovingBlockEntity getBlockEntity(BlockGetter level, BlockPos pos) {
/* 117 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 118 */     if (blockEntity instanceof PistonMovingBlockEntity) {
/* 118 */       PistonMovingBlockEntity pistonMovingBlockEntity = (PistonMovingBlockEntity)blockEntity;
/* 119 */       return pistonMovingBlockEntity;
/*     */     } 
/* 121 */     return null;
/*     */   }
/*     */   
/*     */   protected RenderShape getRenderShape(BlockState state) {
/* 126 */     return RenderShape.INVISIBLE;
/*     */   }
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 131 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 136 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 141 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 146 */     builder.add(new Property[] { (Property)FACING, (Property)TYPE });
/*     */   }
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 151 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\piston\MovingPistonBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */