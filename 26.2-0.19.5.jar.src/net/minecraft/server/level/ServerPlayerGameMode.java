/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Abilities;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerPlayerGameMode {
/*     */   private static final double FLIGHT_DISABLE_RANGE = 1.0D;
/*     */   
/*  38 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   protected ServerLevel level;
/*     */   
/*     */   protected final ServerPlayer player;
/*     */   
/*  43 */   private GameType gameModeForPlayer = GameType.DEFAULT_MODE;
/*     */   
/*     */   private GameType previousGameModeForPlayer;
/*     */   
/*     */   private boolean isDestroyingBlock;
/*     */   
/*     */   private int destroyProgressStart;
/*     */   
/*  48 */   private BlockPos destroyPos = BlockPos.ZERO;
/*     */   
/*     */   private int gameTicks;
/*     */   
/*     */   private boolean hasDelayedDestroy;
/*     */   
/*  52 */   private BlockPos delayedDestroyPos = BlockPos.ZERO;
/*     */   
/*     */   private int delayedTickStart;
/*     */   
/*  54 */   private int lastSentState = -1;
/*     */   
/*     */   public ServerPlayerGameMode(ServerPlayer player) {
/*  57 */     this.player = player;
/*  58 */     this.level = player.level();
/*     */   }
/*     */   
/*     */   public boolean changeGameModeForPlayer(GameType gameModeForPlayer) {
/*  62 */     if (gameModeForPlayer == this.gameModeForPlayer)
/*  63 */       return false; 
/*  66 */     Abilities abilities = this.player.getAbilities();
/*  67 */     setGameModeForPlayer(gameModeForPlayer, this.gameModeForPlayer);
/*  69 */     if (abilities.flying && gameModeForPlayer != GameType.SPECTATOR && isInRangeOfGround())
/*  70 */       abilities.flying = false; 
/*  73 */     this.player.onUpdateAbilities();
/*  74 */     this.level.getServer().getPlayerList().broadcastAll((Packet)new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE, this.player));
/*  75 */     this.level.updateSleepingPlayerList();
/*  77 */     if (gameModeForPlayer == GameType.CREATIVE)
/*  78 */       this.player.resetCurrentImpulseContext(); 
/*  81 */     return true;
/*     */   }
/*     */   
/*     */   protected void setGameModeForPlayer(GameType gameModeForPlayer, GameType previousGameModeForPlayer) {
/*  85 */     this.previousGameModeForPlayer = previousGameModeForPlayer;
/*  86 */     this.gameModeForPlayer = gameModeForPlayer;
/*  88 */     Abilities abilities = this.player.getAbilities();
/*  89 */     gameModeForPlayer.updatePlayerAbilities(abilities);
/*     */   }
/*     */   
/*     */   private boolean isInRangeOfGround() {
/*  93 */     List<VoxelShape> clipping = Entity.collectAllColliders((Entity)this.player, this.level, this.player.getBoundingBox());
/*  94 */     return (clipping.isEmpty() && this.player.getAvailableSpaceBelow(1.0D) < 1.0D);
/*     */   }
/*     */   
/*     */   public GameType getGameModeForPlayer() {
/*  98 */     return this.gameModeForPlayer;
/*     */   }
/*     */   
/*     */   public GameType getPreviousGameModeForPlayer() {
/* 102 */     return this.previousGameModeForPlayer;
/*     */   }
/*     */   
/*     */   public boolean isSurvival() {
/* 106 */     return this.gameModeForPlayer.isSurvival();
/*     */   }
/*     */   
/*     */   public boolean isCreative() {
/* 110 */     return this.gameModeForPlayer.isCreative();
/*     */   }
/*     */   
/*     */   public void tick() {
/* 114 */     this.gameTicks++;
/* 116 */     if (this.hasDelayedDestroy) {
/* 117 */       BlockState blockState = this.level.getBlockState(this.delayedDestroyPos);
/* 118 */       if (blockState.isAir()) {
/* 119 */         this.hasDelayedDestroy = false;
/*     */       } else {
/* 121 */         float destroyProgress = incrementDestroyProgress(blockState, this.delayedDestroyPos, this.delayedTickStart);
/* 123 */         if (destroyProgress >= 1.0F) {
/* 124 */           this.hasDelayedDestroy = false;
/* 125 */           destroyBlock(this.delayedDestroyPos);
/*     */         } 
/*     */       } 
/* 128 */     } else if (this.isDestroyingBlock) {
/* 129 */       BlockState blockState = this.level.getBlockState(this.destroyPos);
/* 131 */       if (blockState.isAir()) {
/* 132 */         this.level.destroyBlockProgress(this.player.getId(), this.destroyPos, -1);
/* 133 */         this.lastSentState = -1;
/* 134 */         this.isDestroyingBlock = false;
/*     */       } else {
/* 136 */         incrementDestroyProgress(blockState, this.destroyPos, this.destroyProgressStart);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private float incrementDestroyProgress(BlockState blockState, BlockPos delayedDestroyPos, int destroyStartTick) {
/* 142 */     int ticksSpentDestroying = this.gameTicks - destroyStartTick;
/* 143 */     float destroyProgress = blockState.getDestroyProgress(this.player, (BlockGetter)this.player.level(), delayedDestroyPos) * (ticksSpentDestroying + 1);
/* 144 */     int state = (int)(destroyProgress * 10.0F);
/* 146 */     if (state != this.lastSentState) {
/* 147 */       this.level.destroyBlockProgress(this.player.getId(), delayedDestroyPos, state);
/* 148 */       this.lastSentState = state;
/*     */     } 
/* 150 */     return destroyProgress;
/*     */   }
/*     */   
/*     */   private void debugLogging(BlockPos pos, boolean allGood, int sequence, String message) {
/* 154 */     if (SharedConstants.DEBUG_BLOCK_BREAK)
/* 155 */       LOGGER.debug("Server ACK {} {} {} {}", new Object[] { Integer.valueOf(sequence), pos, Boolean.valueOf(allGood), message }); 
/*     */   }
/*     */   
/*     */   public void handleBlockBreakAction(BlockPos pos, ServerboundPlayerActionPacket.Action action, Direction direction, int maxY, int sequence) {
/* 161 */     if (!this.player.isWithinBlockInteractionRange(pos, 1.0D)) {
/* 162 */       debugLogging(pos, false, sequence, "too far");
/*     */       return;
/*     */     } 
/* 165 */     if (pos.getY() > maxY) {
/* 166 */       this.player.connection.send((Packet)new ClientboundBlockUpdatePacket(pos, this.level.getBlockState(pos)));
/* 167 */       debugLogging(pos, false, sequence, "too high");
/*     */       return;
/*     */     } 
/* 171 */     if (action == ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) {
/* 172 */       if (this.level.getServer().isUnderSpawnProtection(this.level, pos, this.player)) {
/* 173 */         this.player.sendSpawnProtectionMessage(pos);
/* 174 */         debugLogging(pos, false, sequence, "spawn protection");
/*     */         return;
/*     */       } 
/* 176 */       if (!this.level.mayInteract((Entity)this.player, pos)) {
/* 177 */         this.player.connection.send((Packet)new ClientboundBlockUpdatePacket(pos, this.level.getBlockState(pos)));
/* 178 */         debugLogging(pos, false, sequence, "may not interact");
/*     */         return;
/*     */       } 
/* 181 */       if ((this.player.getAbilities()).instabuild) {
/* 182 */         destroyAndAck(pos, sequence, "creative destroy");
/*     */         return;
/*     */       } 
/* 186 */       if (this.player.blockActionRestricted(this.level, pos, this.gameModeForPlayer)) {
/* 187 */         this.player.connection.send((Packet)new ClientboundBlockUpdatePacket(pos, this.level.getBlockState(pos)));
/* 188 */         debugLogging(pos, false, sequence, "block action restricted");
/*     */         return;
/*     */       } 
/* 192 */       this.destroyProgressStart = this.gameTicks;
/* 193 */       float progress = 1.0F;
/* 194 */       BlockState blockState = this.level.getBlockState(pos);
/* 195 */       if (!blockState.isAir()) {
/* 196 */         EnchantmentHelper.onHitBlock(this.level, this.player.getMainHandItem(), (LivingEntity)this.player, (Entity)this.player, EquipmentSlot.MAINHAND, Vec3.atCenterOf((Vec3i)pos), blockState, item -> this.player.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
/* 197 */         blockState.attack(this.level, pos, this.player);
/* 198 */         progress = blockState.getDestroyProgress(this.player, (BlockGetter)this.player.level(), pos);
/*     */       } 
/* 201 */       if (!blockState.isAir() && progress >= 1.0F) {
/* 202 */         destroyAndAck(pos, sequence, "insta mine");
/*     */       } else {
/* 204 */         if (this.isDestroyingBlock) {
/* 205 */           this.player.connection.send((Packet)new ClientboundBlockUpdatePacket(this.destroyPos, this.level.getBlockState(this.destroyPos)));
/* 206 */           debugLogging(pos, false, sequence, "abort destroying since another started (client insta mine, server disagreed)");
/*     */         } 
/* 208 */         this.isDestroyingBlock = true;
/* 209 */         this.destroyPos = pos.immutable();
/* 211 */         int state = (int)(progress * 10.0F);
/* 212 */         this.level.destroyBlockProgress(this.player.getId(), pos, state);
/* 213 */         debugLogging(pos, true, sequence, "actual start of destroying");
/* 214 */         this.lastSentState = state;
/*     */       } 
/* 216 */     } else if (action == ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK) {
/* 218 */       if (pos.equals(this.destroyPos)) {
/* 219 */         int ticksSpentDestroying = this.gameTicks - this.destroyProgressStart;
/* 221 */         BlockState state = this.level.getBlockState(pos);
/* 222 */         if (!state.isAir()) {
/* 223 */           float destroyProgress = state.getDestroyProgress(this.player, (BlockGetter)this.player.level(), pos) * (ticksSpentDestroying + 1);
/* 224 */           if (destroyProgress >= 0.7F) {
/* 225 */             this.isDestroyingBlock = false;
/* 226 */             this.level.destroyBlockProgress(this.player.getId(), pos, -1);
/* 227 */             destroyAndAck(pos, sequence, "destroyed");
/*     */             return;
/*     */           } 
/* 229 */           if (!this.hasDelayedDestroy) {
/* 230 */             this.isDestroyingBlock = false;
/* 231 */             this.hasDelayedDestroy = true;
/* 232 */             this.delayedDestroyPos = pos;
/* 233 */             this.delayedTickStart = this.destroyProgressStart;
/*     */           } 
/*     */         } 
/*     */       } 
/* 237 */       debugLogging(pos, true, sequence, "stopped destroying");
/* 238 */     } else if (action == ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK) {
/* 239 */       this.isDestroyingBlock = false;
/* 240 */       if (!Objects.equals(this.destroyPos, pos)) {
/* 241 */         LOGGER.warn("Mismatch in destroy block pos: {} {}", this.destroyPos, pos);
/* 242 */         this.level.destroyBlockProgress(this.player.getId(), this.destroyPos, -1);
/* 243 */         debugLogging(pos, true, sequence, "aborted mismatched destroying");
/*     */       } 
/* 246 */       this.level.destroyBlockProgress(this.player.getId(), pos, -1);
/* 247 */       debugLogging(pos, true, sequence, "aborted destroying");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void destroyAndAck(BlockPos pos, int sequence, String exitId) {
/* 252 */     if (destroyBlock(pos)) {
/* 253 */       debugLogging(pos, true, sequence, exitId);
/*     */     } else {
/* 255 */       this.player.connection.send((Packet)new ClientboundBlockUpdatePacket(pos, this.level.getBlockState(pos)));
/* 256 */       debugLogging(pos, false, sequence, exitId);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean destroyBlock(BlockPos pos) {
/* 265 */     BlockState state = this.level.getBlockState(pos);
/* 266 */     if (!this.player.getMainHandItem().canDestroyBlock(state, this.level, pos, this.player))
/* 267 */       return false; 
/* 270 */     BlockEntity blockEntity = this.level.getBlockEntity(pos);
/* 271 */     Block block = state.getBlock();
/* 274 */     if (block instanceof net.minecraft.world.level.block.GameMasterBlock && !this.player.canUseGameMasterBlocks()) {
/* 275 */       this.level.sendBlockUpdated(pos, state, state, 3);
/* 276 */       return false;
/*     */     } 
/* 279 */     if (this.player.blockActionRestricted(this.level, pos, this.gameModeForPlayer))
/* 280 */       return false; 
/* 283 */     BlockState adjustedState = block.playerWillDestroy(this.level, pos, state, this.player);
/* 286 */     boolean changed = this.level.removeBlock(pos, false);
/* 287 */     if (SharedConstants.DEBUG_BLOCK_BREAK)
/* 288 */       LOGGER.info("server broke {} {} -> {}", new Object[] { pos, adjustedState, this.level.getBlockState(pos) }); 
/* 290 */     if (changed)
/* 291 */       block.destroy((LevelAccessor)this.level, pos, adjustedState); 
/* 294 */     if (this.player.preventsBlockDrops())
/* 295 */       return true; 
/* 298 */     ItemStack itemStack = this.player.getMainHandItem();
/* 300 */     ItemStack destroyedWith = itemStack.copy();
/* 301 */     boolean canDestroy = this.player.hasCorrectToolForDrops(adjustedState);
/* 302 */     itemStack.mineBlock(this.level, adjustedState, pos, this.player);
/* 303 */     if (changed && canDestroy)
/* 304 */       block.playerDestroy(this.level, this.player, pos, adjustedState, blockEntity, destroyedWith); 
/* 306 */     return true;
/*     */   }
/*     */   
/*     */   public InteractionResult useItem(ServerPlayer player, Level level, ItemStack itemStack, InteractionHand hand) {
/*     */     ItemStack resultStack;
/* 310 */     if (this.gameModeForPlayer == GameType.SPECTATOR)
/* 311 */       return (InteractionResult)InteractionResult.PASS; 
/* 313 */     if (player.getCooldowns().isOnCooldown(itemStack))
/* 314 */       return (InteractionResult)InteractionResult.PASS; 
/* 317 */     int oldCount = itemStack.getCount();
/* 318 */     int oldDamage = itemStack.getDamageValue();
/* 319 */     InteractionResult result = itemStack.use(level, player, hand);
/* 322 */     if (result instanceof InteractionResult.Success) {
/* 322 */       InteractionResult.Success success = (InteractionResult.Success)result;
/* 323 */       resultStack = Objects.<ItemStack>requireNonNullElse(success.heldItemTransformedTo(), player.getItemInHand(hand));
/*     */     } else {
/* 325 */       resultStack = player.getItemInHand(hand);
/*     */     } 
/* 328 */     if (resultStack == itemStack && resultStack.getCount() == oldCount && resultStack.getUseDuration((LivingEntity)player) <= 0 && resultStack.getDamageValue() == oldDamage)
/* 329 */       return result; 
/* 332 */     if (result instanceof InteractionResult.Fail && resultStack.getUseDuration((LivingEntity)player) > 0 && !player.isUsingItem())
/* 333 */       return result; 
/* 337 */     if (itemStack != resultStack)
/* 338 */       player.setItemInHand(hand, resultStack); 
/* 340 */     if (resultStack.isEmpty())
/* 341 */       player.setItemInHand(hand, ItemStack.EMPTY); 
/* 343 */     if (!player.isUsingItem())
/* 344 */       player.inventoryMenu.sendAllDataToRemote(); 
/* 346 */     return result;
/*     */   }
/*     */   
/*     */   public InteractionResult useItemOn(ServerPlayer player, Level level, ItemStack itemStack, InteractionHand hand, BlockHitResult hitResult) {
/*     */     InteractionResult success;
/* 350 */     BlockPos pos = hitResult.getBlockPos();
/* 352 */     BlockState state = level.getBlockState(pos);
/* 353 */     if (!state.getBlock().isEnabled(level.enabledFeatures()))
/* 354 */       return (InteractionResult)InteractionResult.FAIL; 
/* 357 */     if (this.gameModeForPlayer == GameType.SPECTATOR) {
/* 358 */       MenuProvider menuProvider = state.getMenuProvider(level, pos);
/* 359 */       if (menuProvider != null) {
/* 360 */         player.openMenu(menuProvider);
/* 362 */         return (InteractionResult)InteractionResult.CONSUME;
/*     */       } 
/* 364 */       return (InteractionResult)InteractionResult.PASS;
/*     */     } 
/* 367 */     boolean haveSomethingInOurHands = (!player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty());
/* 368 */     boolean suppressUsingBlock = (player.isSecondaryUseActive() && haveSomethingInOurHands);
/* 369 */     ItemStack usedItemStack = itemStack.copy();
/* 371 */     if (!suppressUsingBlock) {
/* 372 */       InteractionResult itemUse = state.useItemOn(player.getItemInHand(hand), level, player, hand, hitResult);
/* 373 */       if (itemUse.consumesAction()) {
/* 374 */         CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, pos, (ItemInstance)usedItemStack);
/* 375 */         return itemUse;
/*     */       } 
/* 378 */       if (itemUse instanceof InteractionResult.TryEmptyHandInteraction && hand == InteractionHand.MAIN_HAND) {
/* 379 */         InteractionResult use = state.useWithoutItem(level, player, hitResult);
/* 380 */         if (use.consumesAction()) {
/* 381 */           CriteriaTriggers.DEFAULT_BLOCK_USE.trigger(player, pos);
/* 382 */           return use;
/*     */         } 
/*     */       } 
/*     */     } 
/* 387 */     if (itemStack.isEmpty() || player.getCooldowns().isOnCooldown(itemStack))
/* 388 */       return (InteractionResult)InteractionResult.PASS; 
/* 391 */     UseOnContext context = new UseOnContext(player, hand, hitResult);
/* 393 */     if (player.hasInfiniteMaterials()) {
/* 395 */       int count = itemStack.getCount();
/* 396 */       success = itemStack.useOn(context);
/* 397 */       itemStack.setCount(count);
/*     */     } else {
/* 399 */       success = itemStack.useOn(context);
/*     */     } 
/* 401 */     if (success.consumesAction())
/* 402 */       CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, pos, (ItemInstance)usedItemStack); 
/* 404 */     return success;
/*     */   }
/*     */   
/*     */   public void setLevel(ServerLevel newLevel) {
/* 408 */     this.level = newLevel;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerPlayerGameMode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */