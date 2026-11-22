/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.FrontAndTop;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.crafting.CraftingInput;
/*     */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeCache;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeInput;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.CrafterBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.HopperBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class CrafterBlock extends BaseEntityBlock {
/*  44 */   public static final MapCodec<CrafterBlock> CODEC = simpleCodec(CrafterBlock::new);
/*     */   
/*  45 */   public static final BooleanProperty CRAFTING = BlockStateProperties.CRAFTING;
/*     */   
/*  46 */   public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
/*     */   
/*  47 */   private static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;
/*     */   
/*     */   private static final int MAX_CRAFTING_TICKS = 6;
/*     */   
/*     */   private static final int CRAFTING_TICK_DELAY = 4;
/*     */   
/*  50 */   private static final RecipeCache RECIPE_CACHE = new RecipeCache(10);
/*     */   
/*     */   private static final int CRAFTER_ADVANCEMENT_DIAMETER = 17;
/*     */   
/*     */   public CrafterBlock(BlockBehaviour.Properties properties) {
/*  55 */     super(properties);
/*  56 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ORIENTATION, (Comparable)FrontAndTop.NORTH_UP))
/*  57 */         .setValue((Property)TRIGGERED, Boolean.valueOf(false)))
/*  58 */         .setValue((Property)CRAFTING, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   protected MapCodec<CrafterBlock> codec() {
/*  63 */     return CODEC;
/*     */   }
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/*  68 */     return true;
/*     */   }
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/*  73 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  74 */     if (blockEntity instanceof CrafterBlockEntity) {
/*  74 */       CrafterBlockEntity crafterBlockEntity = (CrafterBlockEntity)blockEntity;
/*  75 */       return crafterBlockEntity.getRedstoneSignal();
/*     */     } 
/*  77 */     return 0;
/*     */   }
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*  82 */     boolean shouldTrigger = level.hasNeighborSignal(pos);
/*  83 */     boolean isTriggered = ((Boolean)state.getValue((Property)TRIGGERED)).booleanValue();
/*  84 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  86 */     if (shouldTrigger && !isTriggered) {
/*  87 */       level.scheduleTick(pos, this, 4);
/*  88 */       level.setBlock(pos, (BlockState)state.setValue((Property)TRIGGERED, Boolean.valueOf(true)), 2);
/*  89 */       setBlockEntityTriggered(blockEntity, true);
/*  90 */     } else if (!shouldTrigger && isTriggered) {
/*  91 */       level.setBlock(pos, (BlockState)((BlockState)state.setValue((Property)TRIGGERED, Boolean.valueOf(false))).setValue((Property)CRAFTING, Boolean.valueOf(false)), 2);
/*  92 */       setBlockEntityTriggered(blockEntity, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  98 */     dispenseFrom(state, level, pos);
/*     */   }
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 103 */     return level.isClientSide() ? null : createTickerHelper(type, BlockEntityTypes.CRAFTER, CrafterBlockEntity::serverTick);
/*     */   }
/*     */   
/*     */   private void setBlockEntityTriggered(BlockEntity blockEntity, boolean triggered) {
/* 107 */     if (blockEntity instanceof CrafterBlockEntity) {
/* 107 */       CrafterBlockEntity crafterBlockEntity = (CrafterBlockEntity)blockEntity;
/* 108 */       crafterBlockEntity.setTriggered(triggered);
/*     */     } 
/*     */   }
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 114 */     CrafterBlockEntity crafterBlockEntity = new CrafterBlockEntity(worldPosition, blockState);
/* 115 */     crafterBlockEntity.setTriggered((blockState.hasProperty((Property)TRIGGERED) && ((Boolean)blockState.getValue((Property)TRIGGERED)).booleanValue()));
/* 116 */     return (BlockEntity)crafterBlockEntity;
/*     */   }
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 121 */     Direction nearestLookingDirection = context.getNearestLookingDirection().getOpposite();
/* 122 */     switch (nearestLookingDirection) {
/*     */       default:
/* 122 */         throw new MatchException(null, null);
/*     */       case DOWN:
/*     */       
/*     */       case UP:
/*     */       
/*     */       case NORTH:
/*     */       case SOUTH:
/*     */       case WEST:
/*     */       case EAST:
/*     */         break;
/*     */     } 
/* 122 */     Direction verticalDirection = 
/*     */       
/* 125 */       Direction.UP;
/* 128 */     return (BlockState)((BlockState)defaultBlockState()
/* 129 */       .setValue((Property)ORIENTATION, (Comparable)FrontAndTop.fromFrontAndTop(nearestLookingDirection, verticalDirection)))
/* 130 */       .setValue((Property)TRIGGERED, Boolean.valueOf(context.getLevel().hasNeighborSignal(context.getClickedPos())));
/*     */   }
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/* 135 */     if (((Boolean)state.getValue((Property)TRIGGERED)).booleanValue())
/* 136 */       level.scheduleTick(pos, this, 4); 
/*     */   }
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 142 */     Containers.updateNeighboursAfterDestroy(state, (Level)level, pos);
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 147 */     if (!level.isClientSide()) {
/* 147 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/* 147 */       if (blockEntity instanceof CrafterBlockEntity) {
/* 147 */         CrafterBlockEntity crafter = (CrafterBlockEntity)blockEntity;
/* 148 */         player.openMenu((MenuProvider)crafter);
/*     */       } 
/*     */     } 
/* 150 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected void dispenseFrom(BlockState state, ServerLevel level, BlockPos pos) {
/*     */     CrafterBlockEntity blockEntity;
/* 154 */     BlockEntity blockEntity1 = level.getBlockEntity(pos);
/* 154 */     if (blockEntity1 instanceof CrafterBlockEntity) {
/* 154 */       blockEntity = (CrafterBlockEntity)blockEntity1;
/*     */     } else {
/*     */       return;
/*     */     } 
/* 158 */     CraftingInput craftInput = blockEntity.asCraftInput();
/* 159 */     Optional<RecipeHolder<CraftingRecipe>> recipe = getPotentialResults(level, craftInput);
/* 161 */     if (recipe.isEmpty()) {
/* 162 */       level.levelEvent(1050, pos, 0);
/*     */       return;
/*     */     } 
/* 166 */     RecipeHolder<CraftingRecipe> pickedRecipe = recipe.get();
/* 168 */     ItemStack results = ((CraftingRecipe)pickedRecipe.value()).assemble((RecipeInput)craftInput);
/* 170 */     if (results.isEmpty()) {
/* 171 */       level.levelEvent(1050, pos, 0);
/*     */       return;
/*     */     } 
/* 175 */     blockEntity.setCraftingTicksRemaining(6);
/* 176 */     level.setBlock(pos, (BlockState)state.setValue((Property)CRAFTING, Boolean.valueOf(true)), 2);
/* 177 */     results.onCraftedBySystem((Level)level);
/* 180 */     dispenseItem(level, pos, blockEntity, results, state, pickedRecipe);
/* 183 */     for (ItemStack remainingItem : ((CraftingRecipe)pickedRecipe.value()).getRemainingItems(craftInput)) {
/* 184 */       if (!remainingItem.isEmpty())
/* 185 */         dispenseItem(level, pos, blockEntity, remainingItem, state, pickedRecipe); 
/*     */     } 
/* 190 */     blockEntity.getItems().forEach(it -> {
/*     */           if (it.isEmpty())
/*     */             return; 
/*     */           it.shrink(1);
/*     */         });
/* 196 */     blockEntity.setChanged();
/*     */   }
/*     */   
/*     */   public static Optional<RecipeHolder<CraftingRecipe>> getPotentialResults(ServerLevel level, CraftingInput input) {
/* 200 */     return RECIPE_CACHE.get(level, input);
/*     */   }
/*     */   
/*     */   private void dispenseItem(ServerLevel level, BlockPos pos, CrafterBlockEntity blockEntity, ItemStack results, BlockState blockState, RecipeHolder<?> recipe) {
/* 204 */     Direction direction = ((FrontAndTop)blockState.getValue((Property)ORIENTATION)).front();
/* 205 */     Container into = HopperBlockEntity.getContainerAt((Level)level, pos.relative(direction));
/* 206 */     ItemStack remaining = results.copy();
/* 208 */     if (into != null && (into instanceof CrafterBlockEntity || results.getCount() > into.getMaxStackSize(results))) {
/* 210 */       while (!remaining.isEmpty()) {
/* 211 */         ItemStack copy = remaining.copyWithCount(1);
/* 213 */         ItemStack itemStack = HopperBlockEntity.addItem((Container)blockEntity, into, copy, direction.getOpposite());
/* 215 */         if (!itemStack.isEmpty())
/*     */           break; 
/* 218 */         remaining.shrink(1);
/*     */       } 
/* 220 */     } else if (into != null) {
/* 222 */       while (!remaining.isEmpty()) {
/* 223 */         int oldSize = remaining.getCount();
/* 224 */         remaining = HopperBlockEntity.addItem((Container)blockEntity, into, remaining, direction.getOpposite());
/* 226 */         if (oldSize == remaining.getCount())
/*     */           break; 
/*     */       } 
/*     */     } 
/* 233 */     if (!remaining.isEmpty()) {
/* 234 */       Vec3 centerPos = Vec3.atCenterOf((Vec3i)pos);
/* 235 */       Vec3 itemSpawnOffset = centerPos.relative(direction, 0.7D);
/* 236 */       DefaultDispenseItemBehavior.spawnItem((Level)level, remaining, 6, direction, (Position)itemSpawnOffset);
/* 238 */       for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, AABB.ofSize(centerPos, 17.0D, 17.0D, 17.0D)))
/* 239 */         CriteriaTriggers.CRAFTER_RECIPE_CRAFTED.trigger(player, recipe.id(), (List)blockEntity.getItems()); 
/* 242 */       level.levelEvent(1049, pos, 0);
/* 243 */       level.levelEvent(2010, pos, direction.get3DDataValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 249 */     return (BlockState)state.setValue((Property)ORIENTATION, (Comparable)rotation.rotation().rotate((FrontAndTop)state.getValue((Property)ORIENTATION)));
/*     */   }
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 254 */     return (BlockState)state.setValue((Property)ORIENTATION, (Comparable)mirror.rotation().rotate((FrontAndTop)state.getValue((Property)ORIENTATION)));
/*     */   }
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 259 */     builder.add(new Property[] { (Property)ORIENTATION, (Property)TRIGGERED, (Property)CRAFTING });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\CrafterBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */