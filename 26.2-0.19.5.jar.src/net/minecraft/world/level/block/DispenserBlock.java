/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.dispenser.BlockSource;
/*     */ import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
/*     */ import net.minecraft.core.dispenser.DispenseItemBehavior;
/*     */ import net.minecraft.core.dispenser.EquipmentDispenseItemBehavior;
/*     */ import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
/*     */ import net.minecraft.core.dispenser.SpawnEggItemBehavior;
/*     */ import net.minecraft.core.dispenser.SulfurCubeBlockDispenseItemBehavior;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.DispenserBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DispenserBlock extends BaseEntityBlock {
/*  50 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  52 */   public static final MapCodec<DispenserBlock> CODEC = simpleCodec(DispenserBlock::new);
/*     */   
/*     */   public MapCodec<? extends DispenserBlock> codec() {
/*  56 */     return CODEC;
/*     */   }
/*     */   
/*  59 */   public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
/*     */   
/*  60 */   public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
/*     */   
/*  62 */   private static final DefaultDispenseItemBehavior DEFAULT_BEHAVIOR = new DefaultDispenseItemBehavior();
/*     */   
/*  63 */   public static final Map<Item, DispenseItemBehavior> DISPENSER_REGISTRY = new IdentityHashMap<>();
/*     */   
/*     */   private static final int TRIGGER_DURATION = 4;
/*     */   
/*     */   public static void registerBehavior(ItemLike item, DispenseItemBehavior behavior) {
/*  67 */     DISPENSER_REGISTRY.put(item.asItem(), behavior);
/*     */   }
/*     */   
/*     */   public static void registerProjectileBehavior(ItemLike item) {
/*  71 */     DISPENSER_REGISTRY.put(item.asItem(), new ProjectileDispenseBehavior(item.asItem()));
/*     */   }
/*     */   
/*     */   protected DispenserBlock(BlockBehaviour.Properties properties) {
/*  75 */     super(properties);
/*  76 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)TRIGGERED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  81 */     if (!level.isClientSide()) {
/*  81 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/*  81 */       if (blockEntity instanceof DispenserBlockEntity) {
/*  81 */         DispenserBlockEntity dispenser = (DispenserBlockEntity)blockEntity;
/*  82 */         player.openMenu((MenuProvider)dispenser);
/*  83 */         player.awardStat((dispenser instanceof net.minecraft.world.level.block.entity.DropperBlockEntity) ? Stats.INSPECT_DROPPER : Stats.INSPECT_DISPENSER);
/*     */       } 
/*     */     } 
/*  85 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected void dispenseFrom(ServerLevel level, BlockState state, BlockPos pos) {
/*  89 */     DispenserBlockEntity blockEntity = level.getBlockEntity(pos, BlockEntityTypes.DISPENSER).orElse(null);
/*  90 */     if (blockEntity == null) {
/*  91 */       LOGGER.warn("Ignoring dispensing attempt for Dispenser without matching block entity at {}", pos);
/*     */       return;
/*     */     } 
/*  94 */     BlockSource source = new BlockSource(level, pos, state, blockEntity);
/*  96 */     int slot = blockEntity.getRandomSlot(level.getRandom());
/*  97 */     if (slot < 0) {
/*  98 */       level.levelEvent(1001, pos, 0);
/*  99 */       level.gameEvent((Holder)GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Context.of(blockEntity.getBlockState()));
/*     */       return;
/*     */     } 
/* 103 */     ItemStack itemStack = blockEntity.getItem(slot);
/* 104 */     DispenseItemBehavior behavior = getDispenseMethod((Level)level, itemStack);
/* 106 */     if (behavior != DispenseItemBehavior.NOOP)
/* 107 */       blockEntity.setItem(slot, behavior.dispense(source, itemStack)); 
/*     */   }
/*     */   
/*     */   protected DispenseItemBehavior getDispenseMethod(Level level, ItemStack itemStack) {
/* 112 */     if (!itemStack.isItemEnabled(level.enabledFeatures()))
/* 113 */       return (DispenseItemBehavior)DEFAULT_BEHAVIOR; 
/* 115 */     DispenseItemBehavior behavior = DISPENSER_REGISTRY.get(itemStack.getItem());
/* 116 */     if (behavior != null)
/* 117 */       return behavior; 
/* 119 */     return getDefaultDispenseMethod(itemStack);
/*     */   }
/*     */   
/*     */   private static DispenseItemBehavior getDefaultDispenseMethod(ItemStack itemStack) {
/* 123 */     if (itemStack.has(DataComponents.EQUIPPABLE))
/* 124 */       return (DispenseItemBehavior)EquipmentDispenseItemBehavior.INSTANCE; 
/* 126 */     if (itemStack.is(ItemTags.SULFUR_CUBE_SWALLOWABLE))
/* 127 */       return (DispenseItemBehavior)SulfurCubeBlockDispenseItemBehavior.INSTANCE; 
/* 131 */     if (itemStack.getItem() instanceof net.minecraft.world.item.SpawnEggItem && itemStack.has(DataComponents.ENTITY_DATA))
/* 132 */       return (DispenseItemBehavior)SpawnEggItemBehavior.INSTANCE; 
/* 134 */     return (DispenseItemBehavior)DEFAULT_BEHAVIOR;
/*     */   }
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 139 */     boolean shouldTrigger = (level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above()));
/* 140 */     boolean isTriggered = ((Boolean)state.getValue((Property)TRIGGERED)).booleanValue();
/* 142 */     if (shouldTrigger && !isTriggered) {
/* 143 */       level.scheduleTick(pos, this, 4);
/* 144 */       level.setBlock(pos, (BlockState)state.setValue((Property)TRIGGERED, Boolean.valueOf(true)), 2);
/* 145 */     } else if (!shouldTrigger && isTriggered) {
/* 146 */       level.setBlock(pos, (BlockState)state.setValue((Property)TRIGGERED, Boolean.valueOf(false)), 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 152 */     dispenseFrom(level, state, pos);
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 157 */     return (BlockEntity)new DispenserBlockEntity(worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 162 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getNearestLookingDirection().getOpposite());
/*     */   }
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 167 */     Containers.updateNeighboursAfterDestroy(state, (Level)level, pos);
/*     */   }
/*     */   
/*     */   public static Position getDispensePosition(BlockSource source) {
/* 171 */     return getDispensePosition(source, 0.7D, Vec3.ZERO);
/*     */   }
/*     */   
/*     */   public static Position getDispensePosition(BlockSource source, double scale, Vec3 offset) {
/* 175 */     Direction direction = (Direction)source.state().getValue((Property)FACING);
/* 177 */     return (Position)source.center().add(scale * direction
/* 178 */         .getStepX() + offset.x(), scale * direction
/* 179 */         .getStepY() + offset.y(), scale * direction
/* 180 */         .getStepZ() + offset.z());
/*     */   }
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 186 */     return true;
/*     */   }
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 191 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 196 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 201 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 206 */     builder.add(new Property[] { (Property)FACING, (Property)TRIGGERED });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\DispenserBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */