/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.primitives.Shorts;
/*     */ import com.google.common.primitives.SignedBytes;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.ClientRecipeBook;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
/*     */ import net.minecraft.client.multiplayer.prediction.PredictiveAction;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.network.HashedStack;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ServerboundAttackPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundContainerSlotStateChangedPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundInteractPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPickItemFromBlockPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPickItemFromEntityPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundSpectatorActionPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.StatsCounter;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Input;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ContainerInput;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.PiercingWeapon;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.SoundType;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MultiPlayerGameMode {
/*  74 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final ClientPacketListener connection;
/*     */   
/*  79 */   private BlockPos destroyBlockPos = new BlockPos(-1, -1, -1);
/*     */   
/*  80 */   private ItemStack destroyingItem = ItemStack.EMPTY;
/*     */   
/*     */   private float destroyProgress;
/*     */   
/*     */   private float destroyTicks;
/*     */   
/*     */   private int destroyDelay;
/*     */   
/*     */   private boolean isDestroying;
/*     */   
/*  85 */   private GameType localPlayerMode = GameType.DEFAULT_MODE;
/*     */   
/*     */   private GameType previousLocalPlayerMode;
/*     */   
/*     */   private int carriedIndex;
/*     */   
/*     */   public MultiPlayerGameMode(Minecraft minecraft, ClientPacketListener connection) {
/*  89 */     this.minecraft = minecraft;
/*  90 */     this.connection = connection;
/*     */   }
/*     */   
/*     */   public void adjustPlayer(Player player) {
/*  94 */     this.localPlayerMode.updatePlayerAbilities(player.getAbilities());
/*     */   }
/*     */   
/*     */   public void setLocalMode(GameType mode, GameType previousMode) {
/*  98 */     this.localPlayerMode = mode;
/*  99 */     this.previousLocalPlayerMode = previousMode;
/* 100 */     this.localPlayerMode.updatePlayerAbilities(this.minecraft.player.getAbilities());
/*     */   }
/*     */   
/*     */   public void setLocalMode(GameType mode) {
/* 104 */     if (mode != this.localPlayerMode)
/* 105 */       this.previousLocalPlayerMode = this.localPlayerMode; 
/* 107 */     this.localPlayerMode = mode;
/* 108 */     this.localPlayerMode.updatePlayerAbilities(this.minecraft.player.getAbilities());
/*     */   }
/*     */   
/*     */   public boolean canHurtPlayer() {
/* 112 */     return this.localPlayerMode.isSurvival();
/*     */   }
/*     */   
/*     */   public boolean destroyBlock(BlockPos pos) {
/* 116 */     if (this.minecraft.player.blockActionRestricted(this.minecraft.level, pos, this.localPlayerMode))
/* 117 */       return false; 
/* 120 */     Level level = this.minecraft.level;
/* 121 */     BlockState oldState = level.getBlockState(pos);
/* 122 */     if (!this.minecraft.player.getMainHandItem().canDestroyBlock(oldState, level, pos, (Player)this.minecraft.player))
/* 123 */       return false; 
/* 126 */     Block oldBlock = oldState.getBlock();
/* 128 */     if (oldBlock instanceof net.minecraft.world.level.block.GameMasterBlock && !this.minecraft.player.canUseGameMasterBlocks())
/* 129 */       return false; 
/* 133 */     if (oldState.isAir())
/* 134 */       return false; 
/* 137 */     oldBlock.playerWillDestroy(level, pos, oldState, (Player)this.minecraft.player);
/* 138 */     FluidState fluidState = level.getFluidState(pos);
/* 140 */     boolean changed = level.setBlock(pos, fluidState.createLegacyBlock(), 11);
/* 141 */     if (changed)
/* 142 */       oldBlock.destroy((LevelAccessor)level, pos, oldState); 
/* 144 */     if (SharedConstants.DEBUG_BLOCK_BREAK)
/* 145 */       LOGGER.error("client broke {} {} -> {}", new Object[] { pos, oldState, level.getBlockState(pos) }); 
/* 148 */     return changed;
/*     */   }
/*     */   
/*     */   public boolean startDestroyBlock(BlockPos pos, Direction direction) {
/* 152 */     if (this.minecraft.player.blockActionRestricted(this.minecraft.level, pos, this.localPlayerMode))
/* 153 */       return false; 
/* 156 */     if (!this.minecraft.level.getWorldBorder().isWithinBounds(pos))
/* 157 */       return false; 
/* 160 */     if ((this.minecraft.player.getAbilities()).instabuild) {
/* 161 */       BlockState state = this.minecraft.level.getBlockState(pos);
/* 162 */       this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, pos, state, 1.0F);
/* 163 */       if (SharedConstants.DEBUG_BLOCK_BREAK)
/* 164 */         LOGGER.info("Creative start {} {}", pos, state); 
/* 166 */       startPrediction(this.minecraft.level, sequence -> {
/*     */             destroyBlock(pos);
/*     */             return (Packet)new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, pos, direction, sequence);
/*     */           });
/* 171 */       this.destroyDelay = 5;
/* 172 */     } else if (!this.isDestroying || !sameDestroyTarget(pos)) {
/* 173 */       if (this.isDestroying) {
/* 174 */         if (SharedConstants.DEBUG_BLOCK_BREAK)
/* 175 */           LOGGER.info("Abort old break {} {}", pos, this.minecraft.level.getBlockState(pos)); 
/* 177 */         this.connection.send((Packet<?>)new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, this.destroyBlockPos, direction));
/*     */       } 
/* 179 */       BlockState state = this.minecraft.level.getBlockState(pos);
/* 180 */       this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, pos, state, 0.0F);
/* 181 */       if (SharedConstants.DEBUG_BLOCK_BREAK)
/* 182 */         LOGGER.info("Start break {} {}", pos, state); 
/* 185 */       startPrediction(this.minecraft.level, sequence -> {
/*     */             boolean notAir = !state.isAir();
/*     */             if (notAir && this.destroyProgress == 0.0F)
/*     */               state.attack(this.minecraft.level, pos, (Player)this.minecraft.player); 
/*     */             if (notAir && state.getDestroyProgress((Player)this.minecraft.player, (BlockGetter)this.minecraft.player.level(), pos) >= 1.0F) {
/*     */               destroyBlock(pos);
/*     */             } else {
/*     */               this.isDestroying = true;
/*     */               this.destroyBlockPos = pos;
/*     */               this.destroyingItem = this.minecraft.player.getMainHandItem();
/*     */               this.destroyProgress = 0.0F;
/*     */               this.destroyTicks = 0.0F;
/*     */               this.minecraft.level.destroyBlockProgress(this.minecraft.player.getId(), this.destroyBlockPos, getDestroyStage());
/*     */             } 
/*     */             return (Packet)new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, pos, direction, sequence);
/*     */           });
/*     */     } 
/* 203 */     return true;
/*     */   }
/*     */   
/*     */   public void stopDestroyBlock() {
/* 207 */     if (this.isDestroying) {
/* 208 */       BlockState state = this.minecraft.level.getBlockState(this.destroyBlockPos);
/* 209 */       this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, this.destroyBlockPos, state, -1.0F);
/* 210 */       if (SharedConstants.DEBUG_BLOCK_BREAK)
/* 211 */         LOGGER.info("Stop dest {} {}", this.destroyBlockPos, state); 
/* 213 */       this.connection.send((Packet<?>)new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, this.destroyBlockPos, Direction.DOWN));
/* 214 */       this.isDestroying = false;
/* 215 */       this.destroyProgress = 0.0F;
/* 216 */       this.minecraft.level.destroyBlockProgress(this.minecraft.player.getId(), this.destroyBlockPos, -1);
/* 217 */       this.minecraft.player.resetAttackStrengthTicker();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean continueDestroyBlock(BlockPos pos, Direction direction) {
/* 222 */     ensureHasSentCarriedItem();
/* 224 */     if (this.destroyDelay > 0) {
/* 225 */       this.destroyDelay--;
/* 226 */       return true;
/*     */     } 
/* 229 */     if ((this.minecraft.player.getAbilities()).instabuild && this.minecraft.level.getWorldBorder().isWithinBounds(pos)) {
/* 230 */       this.destroyDelay = 5;
/* 231 */       BlockState state = this.minecraft.level.getBlockState(pos);
/* 232 */       this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, pos, state, 1.0F);
/* 233 */       if (SharedConstants.DEBUG_BLOCK_BREAK)
/* 234 */         LOGGER.info("Creative cont {} {}", pos, state); 
/* 236 */       startPrediction(this.minecraft.level, sequence -> {
/*     */             destroyBlock(pos);
/*     */             return (Packet)new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, pos, direction, sequence);
/*     */           });
/* 240 */       return true;
/*     */     } 
/* 243 */     if (sameDestroyTarget(pos)) {
/* 244 */       BlockState state = this.minecraft.level.getBlockState(pos);
/* 246 */       if (state.isAir()) {
/* 247 */         this.isDestroying = false;
/* 248 */         return false;
/*     */       } 
/* 251 */       this.destroyProgress += state.getDestroyProgress((Player)this.minecraft.player, (BlockGetter)this.minecraft.player.level(), pos);
/* 253 */       if (this.destroyTicks % 4.0F == 0.0F) {
/* 254 */         SoundType soundType = state.getSoundType();
/* 255 */         this.minecraft.getSoundManager().play((SoundInstance)new SimpleSoundInstance(soundType
/* 256 */               .getHitSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 8.0F, soundType.getPitch() * 0.5F, SoundInstance.createUnseededRandom(), pos));
/*     */       } 
/* 260 */       this.destroyTicks++;
/* 262 */       this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, pos, state, Mth.clamp(this.destroyProgress, 0.0F, 1.0F));
/* 263 */       if (this.destroyProgress >= 1.0F) {
/* 264 */         this.isDestroying = false;
/* 265 */         if (SharedConstants.DEBUG_BLOCK_BREAK)
/* 266 */           LOGGER.info("Finished breaking {} {}", pos, state); 
/* 268 */         startPrediction(this.minecraft.level, sequence -> {
/*     */               destroyBlock(pos);
/*     */               return (Packet)new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, pos, direction, sequence);
/*     */             });
/* 273 */         this.destroyProgress = 0.0F;
/* 274 */         this.destroyTicks = 0.0F;
/* 275 */         this.destroyDelay = 5;
/*     */       } 
/* 278 */       this.minecraft.level.destroyBlockProgress(this.minecraft.player.getId(), this.destroyBlockPos, getDestroyStage());
/*     */     } else {
/* 280 */       return startDestroyBlock(pos, direction);
/*     */     } 
/* 282 */     return true;
/*     */   }
/*     */   
/*     */   private void startPrediction(ClientLevel level, PredictiveAction predictiveAction) {
/* 286 */     BlockStatePredictionHandler prediction = level.getBlockStatePredictionHandler().startPredicting();
/*     */     try {
/* 287 */       int sequence = prediction.currentSequence();
/* 288 */       Packet<ServerGamePacketListener> packetConcludingPrediction = predictiveAction.predict(sequence);
/* 289 */       this.connection.send(packetConcludingPrediction);
/* 290 */       if (prediction != null)
/* 290 */         prediction.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (prediction != null)
/*     */         try {
/*     */           prediction.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 294 */     ensureHasSentCarriedItem();
/* 296 */     if (this.connection.getConnection().isConnected()) {
/* 297 */       this.connection.getConnection().tick();
/*     */     } else {
/* 299 */       this.connection.getConnection().handleDisconnection();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean sameDestroyTarget(BlockPos pos) {
/* 306 */     ItemStack selected = this.minecraft.player.getMainHandItem();
/* 307 */     return (pos.equals(this.destroyBlockPos) && ItemStack.isSameItemSameComponents(selected, this.destroyingItem));
/*     */   }
/*     */   
/*     */   private void ensureHasSentCarriedItem() {
/* 311 */     int index = this.minecraft.player.getInventory().getSelectedSlot();
/* 312 */     if (index != this.carriedIndex) {
/* 313 */       this.carriedIndex = index;
/* 314 */       this.connection.send((Packet<?>)new ServerboundSetCarriedItemPacket(this.carriedIndex));
/*     */     } 
/*     */   }
/*     */   
/*     */   public InteractionResult useItemOn(LocalPlayer player, InteractionHand hand, BlockHitResult blockHit) {
/* 319 */     ensureHasSentCarriedItem();
/* 321 */     if (!this.minecraft.level.getWorldBorder().isWithinBounds(blockHit.getBlockPos()))
/* 322 */       return (InteractionResult)InteractionResult.FAIL; 
/* 325 */     MutableObject<InteractionResult> result = new MutableObject();
/* 326 */     startPrediction(this.minecraft.level, sequence -> {
/*     */           result.setValue(performUseItemOn(player, hand, blockHit));
/*     */           return (Packet)new ServerboundUseItemOnPacket(hand, blockHit, sequence);
/*     */         });
/* 331 */     return (InteractionResult)result.get();
/*     */   }
/*     */   
/*     */   private InteractionResult performUseItemOn(LocalPlayer player, InteractionHand hand, BlockHitResult blockHit) {
/*     */     InteractionResult success;
/* 335 */     BlockPos pos = blockHit.getBlockPos();
/* 336 */     ItemStack itemStack = player.getItemInHand(hand);
/* 337 */     if (this.localPlayerMode == GameType.SPECTATOR)
/* 338 */       return (InteractionResult)InteractionResult.CONSUME; 
/* 342 */     boolean haveSomethingInOurHands = (!player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty());
/* 343 */     boolean suppressUsingBlock = (player.isSecondaryUseActive() && haveSomethingInOurHands);
/* 345 */     if (!suppressUsingBlock) {
/* 346 */       BlockState blockState = this.minecraft.level.getBlockState(pos);
/* 347 */       if (!this.connection.isFeatureEnabled(blockState.getBlock().requiredFeatures()))
/* 348 */         return (InteractionResult)InteractionResult.FAIL; 
/* 350 */       InteractionResult itemUse = blockState.useItemOn(player.getItemInHand(hand), this.minecraft.level, (Player)player, hand, blockHit);
/* 351 */       if (itemUse.consumesAction())
/* 352 */         return itemUse; 
/* 354 */       if (itemUse instanceof InteractionResult.TryEmptyHandInteraction && hand == InteractionHand.MAIN_HAND) {
/* 355 */         InteractionResult use = blockState.useWithoutItem(this.minecraft.level, (Player)player, blockHit);
/* 356 */         if (use.consumesAction())
/* 357 */           return use; 
/*     */       } 
/*     */     } 
/* 362 */     if (itemStack.isEmpty() || player.getCooldowns().isOnCooldown(itemStack))
/* 363 */       return (InteractionResult)InteractionResult.PASS; 
/* 367 */     UseOnContext context = new UseOnContext((Player)player, hand, blockHit);
/* 369 */     if (player.hasInfiniteMaterials()) {
/* 371 */       int count = itemStack.getCount();
/* 372 */       success = itemStack.useOn(context);
/* 373 */       itemStack.setCount(count);
/*     */     } else {
/* 375 */       success = itemStack.useOn(context);
/*     */     } 
/* 377 */     return success;
/*     */   }
/*     */   
/*     */   public InteractionResult useItem(Player player, InteractionHand hand) {
/* 381 */     if (this.localPlayerMode == GameType.SPECTATOR)
/* 382 */       return (InteractionResult)InteractionResult.PASS; 
/* 384 */     ensureHasSentCarriedItem();
/* 385 */     MutableObject<InteractionResult> interactionResult = new MutableObject();
/* 386 */     startPrediction(this.minecraft.level, sequence -> {
/*     */           ItemStack result;
/*     */           ServerboundUseItemPacket packet = new ServerboundUseItemPacket(hand, sequence, player.getYRot(), player.getXRot());
/*     */           ItemStack itemStack = player.getItemInHand(hand);
/*     */           if (player.getCooldowns().isOnCooldown(itemStack)) {
/*     */             interactionResult.setValue(InteractionResult.PASS);
/*     */             return (Packet)packet;
/*     */           } 
/*     */           InteractionResult resultHolder = itemStack.use(this.minecraft.level, player, hand);
/*     */           if (resultHolder instanceof InteractionResult.Success) {
/*     */             InteractionResult.Success success = (InteractionResult.Success)resultHolder;
/*     */             result = Objects.<ItemStack>requireNonNullElseGet(success.heldItemTransformedTo(), ());
/*     */           } else {
/*     */             result = player.getItemInHand(hand);
/*     */           } 
/*     */           if (result != itemStack)
/*     */             player.setItemInHand(hand, result); 
/*     */           interactionResult.setValue(resultHolder);
/*     */           return (Packet)packet;
/*     */         });
/* 411 */     return (InteractionResult)interactionResult.get();
/*     */   }
/*     */   
/*     */   public LocalPlayer createPlayer(ClientLevel level, StatsCounter stats, ClientRecipeBook recipeBook) {
/* 415 */     return createPlayer(level, stats, recipeBook, Input.EMPTY, false);
/*     */   }
/*     */   
/*     */   public LocalPlayer createPlayer(ClientLevel level, StatsCounter stats, ClientRecipeBook recipeBook, Input lastSentInput, boolean wasSprinting) {
/* 419 */     return new LocalPlayer(this.minecraft, level, this.connection, stats, recipeBook, lastSentInput, wasSprinting, this.minecraft.computeChatAbilities());
/*     */   }
/*     */   
/*     */   public void attack(Player player, Entity entity) {
/* 423 */     ensureHasSentCarriedItem();
/* 424 */     this.connection.send((Packet<?>)new ServerboundAttackPacket(entity.getId()));
/* 425 */     player.attack(entity);
/* 426 */     player.resetAttackStrengthTicker();
/*     */   }
/*     */   
/*     */   public void spectate(Entity entity) {
/* 430 */     this.connection.send((Packet<?>)new ServerboundSpectatorActionPacket(OptionalInt.of(entity.getId())));
/*     */   }
/*     */   
/*     */   public void spectatorNoAction() {
/* 436 */     this.connection.send((Packet<?>)new ServerboundSpectatorActionPacket(OptionalInt.empty()));
/*     */   }
/*     */   
/*     */   public InteractionResult interact(Player player, Entity entity, EntityHitResult hitResult, InteractionHand hand) {
/* 440 */     ensureHasSentCarriedItem();
/* 441 */     Vec3 location = hitResult.getLocation().subtract(entity.getX(), entity.getY(), entity.getZ());
/* 442 */     this.connection.send((Packet<?>)new ServerboundInteractPacket(entity.getId(), hand, location, player.isShiftKeyDown()));
/* 443 */     if (this.localPlayerMode == GameType.SPECTATOR)
/* 444 */       return (InteractionResult)InteractionResult.PASS; 
/* 446 */     return player.interactOn(entity, hand, location);
/*     */   }
/*     */   
/*     */   public void handleContainerInput(int containerId, int slotNum, int buttonNum, ContainerInput containerInput, Player player) {
/* 451 */     AbstractContainerMenu containerMenu = player.containerMenu;
/* 452 */     if (containerId != containerMenu.containerId) {
/* 453 */       LOGGER.warn("Ignoring click in mismatching container. Click in {}, player has {}.", Integer.valueOf(containerId), Integer.valueOf(containerMenu.containerId));
/*     */       return;
/*     */     } 
/* 456 */     NonNullList<Slot> slots = containerMenu.slots;
/* 457 */     int slotCount = slots.size();
/* 458 */     List<ItemStack> itemsBeforeClick = Lists.newArrayListWithCapacity(slotCount);
/* 459 */     for (Slot slot : slots)
/* 460 */       itemsBeforeClick.add(slot.getItem().copy()); 
/* 463 */     containerMenu.clicked(slotNum, buttonNum, containerInput, player);
/* 465 */     Int2ObjectOpenHashMap int2ObjectOpenHashMap = new Int2ObjectOpenHashMap();
/* 466 */     for (int i = 0; i < slotCount; i++) {
/* 467 */       ItemStack before = itemsBeforeClick.get(i);
/* 468 */       ItemStack after = ((Slot)slots.get(i)).getItem();
/* 469 */       if (!ItemStack.matches(before, after))
/* 470 */         int2ObjectOpenHashMap.put(i, HashedStack.create(after, this.connection.decoratedHashOpsGenenerator())); 
/*     */     } 
/* 473 */     HashedStack carriedItem = HashedStack.create(containerMenu.getCarried(), this.connection.decoratedHashOpsGenenerator());
/* 476 */     this.connection.send((Packet<?>)new ServerboundContainerClickPacket(containerId, containerMenu.getStateId(), Shorts.checkedCast(slotNum), SignedBytes.checkedCast(buttonNum), containerInput, (Int2ObjectMap)int2ObjectOpenHashMap, carriedItem));
/*     */   }
/*     */   
/*     */   public void handlePlaceRecipe(int containerId, RecipeDisplayId recipe, boolean useMaxItems) {
/* 480 */     this.connection.send((Packet<?>)new ServerboundPlaceRecipePacket(containerId, recipe, useMaxItems));
/*     */   }
/*     */   
/*     */   public void handleInventoryButtonClick(int containerId, int buttonId) {
/* 484 */     this.connection.send((Packet<?>)new ServerboundContainerButtonClickPacket(containerId, buttonId));
/*     */   }
/*     */   
/*     */   public void handleCreativeModeItemAdd(ItemStack clicked, int slot) {
/* 488 */     if (this.minecraft.player.hasInfiniteMaterials() && this.connection.isFeatureEnabled(clicked.getItem().requiredFeatures()))
/* 489 */       this.connection.send((Packet<?>)new ServerboundSetCreativeModeSlotPacket(slot, clicked)); 
/*     */   }
/*     */   
/*     */   public void handleCreativeModeItemDrop(ItemStack clicked) {
/* 495 */     boolean hasOtherInventoryOpen = (this.minecraft.gui.screen() instanceof net.minecraft.client.gui.screens.inventory.AbstractContainerScreen && !(this.minecraft.gui.screen() instanceof net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen));
/* 497 */     if (this.minecraft.player.hasInfiniteMaterials() && !hasOtherInventoryOpen && !clicked.isEmpty() && this.connection.isFeatureEnabled(clicked.getItem().requiredFeatures())) {
/* 498 */       this.connection.send((Packet<?>)new ServerboundSetCreativeModeSlotPacket(-1, clicked));
/* 499 */       this.minecraft.player.getDropSpamThrottler().increment();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void releaseUsingItem(Player player) {
/* 504 */     ensureHasSentCarriedItem();
/* 505 */     this.connection.send((Packet<?>)new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM, BlockPos.ZERO, Direction.DOWN));
/* 506 */     player.releaseUsingItem();
/*     */   }
/*     */   
/*     */   public void piercingAttack(PiercingWeapon weapon) {
/* 510 */     ensureHasSentCarriedItem();
/* 511 */     this.connection.send((Packet<?>)new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STAB, BlockPos.ZERO, Direction.DOWN));
/* 512 */     this.minecraft.player.onAttack();
/* 513 */     this.minecraft.player.postPiercingAttack();
/* 514 */     weapon.makeSound((Entity)this.minecraft.player);
/*     */   }
/*     */   
/*     */   public boolean hasExperience() {
/* 518 */     return this.localPlayerMode.isSurvival();
/*     */   }
/*     */   
/*     */   public boolean hasMissTime() {
/* 522 */     return !this.localPlayerMode.isCreative();
/*     */   }
/*     */   
/*     */   public boolean isServerControlledInventory() {
/* 529 */     return (this.minecraft.player.isPassenger() && this.minecraft.player.getVehicle() instanceof net.minecraft.world.entity.HasCustomInventoryScreen);
/*     */   }
/*     */   
/*     */   public boolean isSpectator() {
/* 533 */     return (this.localPlayerMode == GameType.SPECTATOR);
/*     */   }
/*     */   
/*     */   public GameType getPreviousPlayerMode() {
/* 537 */     return this.previousLocalPlayerMode;
/*     */   }
/*     */   
/*     */   public GameType getPlayerMode() {
/* 541 */     return this.localPlayerMode;
/*     */   }
/*     */   
/*     */   public boolean isDestroying() {
/* 545 */     return this.isDestroying;
/*     */   }
/*     */   
/*     */   public int getDestroyStage() {
/* 549 */     return (this.destroyProgress > 0.0F) ? (int)(this.destroyProgress * 10.0F) : -1;
/*     */   }
/*     */   
/*     */   public void handlePickItemFromBlock(BlockPos pos, boolean includeData) {
/* 553 */     this.connection.send((Packet<?>)new ServerboundPickItemFromBlockPacket(pos, includeData));
/*     */   }
/*     */   
/*     */   public void handlePickItemFromEntity(Entity entity, boolean includeData) {
/* 557 */     this.connection.send((Packet<?>)new ServerboundPickItemFromEntityPacket(entity.getId(), includeData));
/*     */   }
/*     */   
/*     */   public void handleSlotStateChanged(int slotId, int containerId, boolean newState) {
/* 561 */     this.connection.send((Packet<?>)new ServerboundContainerSlotStateChangedPacket(slotId, containerId, newState));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\MultiPlayerGameMode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */