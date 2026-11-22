/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.SoundType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ 
/*     */ public class BlockItem extends Item {
/*     */   @Deprecated
/*     */   private final Block block;
/*     */   
/*     */   public BlockItem(Block block, Item.Properties properties) {
/*  37 */     super(properties);
/*  38 */     this.block = block;
/*     */   }
/*     */   
/*     */   public InteractionResult useOn(UseOnContext context) {
/*  43 */     InteractionResult placeResult = place(new BlockPlaceContext(context));
/*  45 */     if (!placeResult.consumesAction() && context.getItemInHand().has(DataComponents.CONSUMABLE))
/*  46 */       return use(context.getLevel(), context.getPlayer(), context.getHand()); 
/*  48 */     return placeResult;
/*     */   }
/*     */   
/*     */   public InteractionResult place(BlockPlaceContext placeContext) {
/*  52 */     if (!getBlock().isEnabled(placeContext.getLevel().enabledFeatures()))
/*  53 */       return (InteractionResult)InteractionResult.FAIL; 
/*  56 */     if (!placeContext.canPlace())
/*  57 */       return (InteractionResult)InteractionResult.FAIL; 
/*  60 */     BlockPlaceContext updatedPlaceContext = updatePlacementContext(placeContext);
/*  61 */     if (updatedPlaceContext == null)
/*  62 */       return (InteractionResult)InteractionResult.FAIL; 
/*  65 */     BlockState placementState = getPlacementState(updatedPlaceContext);
/*  66 */     if (placementState == null)
/*  67 */       return (InteractionResult)InteractionResult.FAIL; 
/*  70 */     if (!placeBlock(updatedPlaceContext, placementState))
/*  71 */       return (InteractionResult)InteractionResult.FAIL; 
/*  74 */     BlockPos pos = updatedPlaceContext.getClickedPos();
/*  75 */     Level level = updatedPlaceContext.getLevel();
/*  76 */     Player player = updatedPlaceContext.getPlayer();
/*  77 */     ItemStack itemStack = updatedPlaceContext.getItemInHand();
/*  80 */     BlockState placedState = level.getBlockState(pos);
/*  81 */     if (placedState.is(placementState.getBlock())) {
/*  82 */       placedState = updateBlockStateFromTag(pos, level, itemStack, placedState);
/*  83 */       updateCustomBlockEntityTag(pos, level, player, itemStack, placedState);
/*  84 */       updateBlockEntityComponents(level, pos, itemStack);
/*  85 */       placedState.getBlock().setPlacedBy(level, pos, placedState, (LivingEntity)player, itemStack);
/*  86 */       if (player instanceof ServerPlayer) {
/*  86 */         ServerPlayer serverPlayer = (ServerPlayer)player;
/*  87 */         CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, itemStack);
/*     */       } 
/*     */     } 
/*  90 */     SoundType soundType = placedState.getSoundType();
/*  91 */     level.playSound((Entity)player, pos, getPlaceSound(placedState), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
/*  92 */     level.gameEvent((Holder)GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of((Entity)player, placedState));
/*  93 */     itemStack.consume(1, (LivingEntity)player);
/*  94 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected SoundEvent getPlaceSound(BlockState blockState) {
/*  98 */     return blockState.getSoundType().getPlaceSound();
/*     */   }
/*     */   
/*     */   public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
/* 102 */     return context;
/*     */   }
/*     */   
/*     */   private static void updateBlockEntityComponents(Level level, BlockPos pos, ItemStack itemStack) {
/* 106 */     BlockEntity entity = level.getBlockEntity(pos);
/* 107 */     if (entity != null) {
/* 108 */       entity.applyComponentsFromItemStack(itemStack);
/* 109 */       entity.setChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, Player player, ItemStack itemStack, BlockState placedState) {
/* 114 */     return updateCustomBlockEntityTag(level, player, pos, itemStack);
/*     */   }
/*     */   
/*     */   protected BlockState getPlacementState(BlockPlaceContext context) {
/* 118 */     BlockState stateForPlacement = getBlock().getStateForPlacement(context);
/* 119 */     return (stateForPlacement != null && canPlace(context, stateForPlacement)) ? stateForPlacement : null;
/*     */   }
/*     */   
/*     */   private BlockState updateBlockStateFromTag(BlockPos pos, Level level, ItemStack itemStack, BlockState placedState) {
/* 123 */     BlockItemStateProperties blockState = (BlockItemStateProperties)itemStack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
/* 124 */     if (blockState.isEmpty())
/* 125 */       return placedState; 
/* 127 */     BlockState modifiedState = blockState.apply(placedState);
/* 128 */     if (modifiedState != placedState)
/* 129 */       level.setBlock(pos, modifiedState, 2); 
/* 131 */     return modifiedState;
/*     */   }
/*     */   
/*     */   protected boolean canPlace(BlockPlaceContext context, BlockState stateForPlacement) {
/* 135 */     Player player = context.getPlayer();
/* 136 */     return ((!mustSurvive() || stateForPlacement.canSurvive((LevelReader)context.getLevel(), context.getClickedPos())) && context
/* 137 */       .getLevel().isUnobstructed(stateForPlacement, context.getClickedPos(), CollisionContext.placementContext(player)));
/*     */   }
/*     */   
/*     */   protected boolean mustSurvive() {
/* 141 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean placeBlock(BlockPlaceContext context, BlockState placementState) {
/* 145 */     return context.getLevel().setBlock(context.getClickedPos(), placementState, 11);
/*     */   }
/*     */   
/*     */   public static boolean updateCustomBlockEntityTag(Level level, Player player, BlockPos pos, ItemStack itemStack) {
/* 149 */     if (level.isClientSide())
/* 150 */       return false; 
/* 153 */     TypedEntityData<BlockEntityType<?>> customData = (TypedEntityData<BlockEntityType<?>>)itemStack.get(DataComponents.BLOCK_ENTITY_DATA);
/* 154 */     if (customData != null) {
/* 155 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/* 157 */       if (blockEntity != null) {
/* 158 */         BlockEntityType<?> type = blockEntity.getType();
/* 159 */         if (type != customData.type())
/* 160 */           return false; 
/* 162 */         if (type.onlyOpCanSetNbt() && (player == null || !player.canUseGameMasterBlocks()))
/* 163 */           return false; 
/* 165 */         return customData.loadInto(blockEntity, (HolderLookup.Provider)level.registryAccess());
/*     */       } 
/*     */     } 
/* 168 */     return false;
/*     */   }
/*     */   
/*     */   public boolean shouldPrintOpWarning(ItemStack stack, Player player) {
/* 173 */     if (player != null && player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
/* 174 */       TypedEntityData<BlockEntityType<?>> blockEntityData = (TypedEntityData<BlockEntityType<?>>)stack.get(DataComponents.BLOCK_ENTITY_DATA);
/* 175 */       if (blockEntityData != null)
/* 176 */         return ((BlockEntityType)blockEntityData.type()).onlyOpCanSetNbt(); 
/*     */     } 
/* 180 */     return false;
/*     */   }
/*     */   
/*     */   public Block getBlock() {
/* 184 */     return this.block;
/*     */   }
/*     */   
/*     */   public void registerBlocks(Map<Block, Item> map, Item item) {
/* 188 */     map.put(getBlock(), item);
/*     */   }
/*     */   
/*     */   public boolean canFitInsideContainerItems() {
/* 194 */     return !(getBlock() instanceof net.minecraft.world.level.block.ShulkerBoxBlock);
/*     */   }
/*     */   
/*     */   public void onDestroyed(ItemEntity entity) {
/* 199 */     ItemContainerContents container = entity.getItem().<ItemContainerContents>set(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
/* 200 */     if (container != null)
/* 201 */       ItemUtils.onContainerDestroyed(entity, container.nonEmptyItemCopyStream()); 
/*     */   }
/*     */   
/*     */   public static void setBlockEntityData(ItemStack stack, BlockEntityType<?> type, TagValueOutput output) {
/* 206 */     output.discard("id");
/* 207 */     if (output.isEmpty()) {
/* 208 */       stack.remove(DataComponents.BLOCK_ENTITY_DATA);
/*     */     } else {
/* 210 */       BlockEntity.addEntityType((ValueOutput)output, type);
/* 211 */       stack.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(type, output.buildResult()));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\BlockItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */