/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.Shulker;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ShulkerBoxBlock extends BaseEntityBlock {
/*     */   public static final MapCodec<ShulkerBoxBlock> CODEC;
/*     */   
/*     */   static {
/*  45 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DyeColor.CODEC.optionalFieldOf("color").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, ()));
/*     */   }
/*     */   
/*     */   public MapCodec<ShulkerBoxBlock> codec() {
/*  52 */     return CODEC;
/*     */   }
/*     */   
/*  55 */   public static final Map<Direction, VoxelShape> SHAPES_OPEN_SUPPORT = Shapes.rotateAll(Block.boxZ(16.0D, 0.0D, 1.0D));
/*     */   
/*  57 */   public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
/*     */   
/*  59 */   public static final Identifier CONTENTS = Identifier.withDefaultNamespace("contents");
/*     */   
/*     */   private final DyeColor color;
/*     */   
/*     */   public ShulkerBoxBlock(DyeColor color, BlockBehaviour.Properties properties) {
/*  64 */     super(properties);
/*  65 */     this.color = color;
/*  66 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.UP));
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  71 */     return (BlockEntity)new ShulkerBoxBlockEntity(this.color, worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/*  76 */     return createTickerHelper(type, BlockEntityTypes.SHULKER_BOX, ShulkerBoxBlockEntity::tick);
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  81 */     if (level instanceof ServerLevel) {
/*  81 */       ServerLevel serverLevel = (ServerLevel)level;
/*  81 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/*  81 */       if (blockEntity instanceof ShulkerBoxBlockEntity) {
/*  81 */         ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity;
/*  81 */         if (canOpen(state, level, pos, shulkerBoxBlockEntity)) {
/*  82 */           player.openMenu((MenuProvider)shulkerBoxBlockEntity);
/*  83 */           player.awardStat(Stats.OPEN_SHULKER_BOX);
/*  84 */           PiglinAi.angerNearbyPiglins(serverLevel, player, true);
/*     */         } 
/*     */       } 
/*     */     } 
/*  86 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static boolean canOpen(BlockState state, Level level, BlockPos pos, ShulkerBoxBlockEntity blockEntity) {
/*  90 */     if (blockEntity.getAnimationStatus() != ShulkerBoxBlockEntity.AnimationStatus.CLOSED)
/*  91 */       return true; 
/*  94 */     AABB lidOpenBoundingBox = Shulker.getProgressDeltaAabb(1.0F, (Direction)state.getValue((Property)FACING), 0.0F, 0.5F, Vec3.atBottomCenterOf((Vec3i)pos)).deflate(1.0E-6D);
/*  95 */     return level.noCollision(lidOpenBoundingBox);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 100 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getClickedFace());
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 105 */     builder.add(new Property[] { (Property)FACING });
/*     */   }
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 110 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 111 */     if (blockEntity instanceof ShulkerBoxBlockEntity) {
/* 111 */       ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity;
/* 112 */       if (!level.isClientSide() && player.preventsBlockDrops() && !shulkerBoxBlockEntity.isEmpty()) {
/* 114 */         ItemStack itemStack = new ItemStack(state.getBlock());
/* 115 */         itemStack.applyComponents(blockEntity.collectComponents());
/* 117 */         ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, itemStack);
/* 118 */         entity.setDefaultPickUpDelay();
/* 119 */         level.addFreshEntity((Entity)entity);
/*     */       } else {
/* 121 */         shulkerBoxBlockEntity.unpackLootTable(player);
/*     */       } 
/*     */     } 
/* 124 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */   
/*     */   protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
/* 129 */     BlockEntity blockEntity = (BlockEntity)params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
/* 131 */     if (blockEntity instanceof ShulkerBoxBlockEntity) {
/* 131 */       ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity;
/* 132 */       params = params.withDynamicDrop(CONTENTS, output -> {
/*     */             for (int i = 0; i < shulkerBoxBlockEntity.getContainerSize(); i++)
/*     */               output.accept(shulkerBoxBlockEntity.getItem(i)); 
/*     */           });
/*     */     } 
/* 139 */     return super.getDrops(state, params);
/*     */   }
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 144 */     Containers.updateNeighboursAfterDestroy(state, (Level)level, pos);
/*     */   }
/*     */   
/*     */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
/* 149 */     BlockEntity entity = level.getBlockEntity(pos);
/* 150 */     if (entity instanceof ShulkerBoxBlockEntity) {
/* 150 */       ShulkerBoxBlockEntity shulker = (ShulkerBoxBlockEntity)entity;
/* 150 */       if (!shulker.isClosed())
/* 151 */         return SHAPES_OPEN_SUPPORT.get(((Direction)state.getValue((Property)FACING)).getOpposite()); 
/*     */     } 
/* 153 */     return Shapes.block();
/*     */   }
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 158 */     BlockEntity entity = level.getBlockEntity(pos);
/* 159 */     if (entity instanceof ShulkerBoxBlockEntity) {
/* 159 */       ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)entity;
/* 160 */       return Shapes.create(shulkerBoxBlockEntity.getBoundingBox(state));
/*     */     } 
/* 162 */     return Shapes.block();
/*     */   }
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState state) {
/* 167 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 172 */     return true;
/*     */   }
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 177 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
/*     */   }
/*     */   
/*     */   public DyeColor getColor() {
/* 181 */     return this.color;
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 186 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 191 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\ShulkerBoxBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */