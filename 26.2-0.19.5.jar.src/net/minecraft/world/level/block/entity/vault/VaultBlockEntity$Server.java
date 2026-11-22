/*     */ package net.minecraft.world.level.block.entity.vault;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.VaultBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class Server {
/*     */   private static final int UNLOCKING_DELAY_TICKS = 14;
/*     */   
/*     */   private static final int DISPLAY_CYCLE_TICK_RATE = 20;
/*     */   
/*     */   private static final int INSERT_FAIL_SOUND_BUFFER_TICKS = 15;
/*     */   
/*     */   public static void tick(ServerLevel serverLevel, BlockPos pos, BlockState blockState, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData) {
/* 115 */     VaultState currentState = (VaultState)blockState.getValue(VaultBlock.STATE);
/* 117 */     if (shouldCycleDisplayItem(serverLevel.getGameTime(), currentState))
/* 118 */       cycleDisplayItemFromLootTable(serverLevel, currentState, config, sharedData, pos); 
/* 121 */     BlockState nextBlockState = blockState;
/* 122 */     if (serverLevel.getGameTime() >= serverData.stateUpdatingResumesAt()) {
/* 123 */       nextBlockState = (BlockState)nextBlockState.setValue(VaultBlock.STATE, currentState.tickAndGetNext(serverLevel, pos, config, serverData, sharedData));
/* 125 */       if (blockState != nextBlockState)
/* 126 */         setVaultState(serverLevel, pos, blockState, nextBlockState, config, sharedData); 
/*     */     } 
/* 130 */     if (serverData.isDirty || sharedData.isDirty) {
/* 132 */       VaultBlockEntity.access$000((Level)serverLevel, pos, blockState);
/* 135 */       if (sharedData.isDirty)
/* 136 */         serverLevel.sendBlockUpdated(pos, blockState, nextBlockState, 2); 
/* 138 */       serverData.isDirty = false;
/* 139 */       sharedData.isDirty = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void tryInsertKey(ServerLevel serverLevel, BlockPos pos, BlockState blockState, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, Player player, ItemStack stackToInsert) {
/* 144 */     VaultState vaultState = (VaultState)blockState.getValue(VaultBlock.STATE);
/* 146 */     if (!canEjectReward(config, vaultState))
/*     */       return; 
/* 150 */     if (!isValidToInsert(config, stackToInsert)) {
/* 151 */       playInsertFailSound(serverLevel, serverData, pos, SoundEvents.VAULT_INSERT_ITEM_FAIL);
/*     */       return;
/*     */     } 
/* 155 */     if (serverData.hasRewardedPlayer(player)) {
/* 156 */       playInsertFailSound(serverLevel, serverData, pos, SoundEvents.VAULT_REJECT_REWARDED_PLAYER);
/*     */       return;
/*     */     } 
/* 160 */     List<ItemStack> itemsToEject = resolveItemsToEject(serverLevel, config, pos, player, (ItemInstance)stackToInsert);
/* 161 */     if (itemsToEject.isEmpty())
/*     */       return; 
/* 165 */     player.awardStat(Stats.ITEM_USED.get(stackToInsert.getItem()));
/* 166 */     stackToInsert.consume(config.keyItem().getCount(), (LivingEntity)player);
/* 168 */     unlock(serverLevel, blockState, pos, config, serverData, sharedData, itemsToEject);
/* 169 */     serverData.addToRewardedPlayers(player);
/* 170 */     sharedData.updateConnectedPlayersWithinRange(serverLevel, pos, serverData, config, config.deactivationRange());
/*     */   }
/*     */   
/*     */   private static void setVaultState(ServerLevel serverLevel, BlockPos pos, BlockState currentBlockState, BlockState newBlockState, VaultConfig config, VaultSharedData sharedData) {
/* 174 */     VaultState currentVaultState = (VaultState)currentBlockState.getValue(VaultBlock.STATE);
/* 175 */     VaultState newVaultState = (VaultState)newBlockState.getValue(VaultBlock.STATE);
/* 177 */     serverLevel.setBlock(pos, newBlockState, 3);
/* 178 */     currentVaultState.onTransition(serverLevel, pos, newVaultState, config, sharedData, ((Boolean)newBlockState.getValue((Property)VaultBlock.OMINOUS)).booleanValue());
/*     */   }
/*     */   
/*     */   static void cycleDisplayItemFromLootTable(ServerLevel serverLevel, VaultState vaultState, VaultConfig config, VaultSharedData sharedData, BlockPos pos) {
/* 183 */     if (!canEjectReward(config, vaultState)) {
/* 184 */       sharedData.setDisplayItem(ItemStack.EMPTY);
/*     */       return;
/*     */     } 
/* 188 */     ItemStack displayItem = getRandomDisplayItemFromLootTable(serverLevel, pos, config.overrideLootTableToDisplay().orElse(config.lootTable()));
/* 189 */     sharedData.setDisplayItem(displayItem);
/*     */   }
/*     */   
/*     */   private static ItemStack getRandomDisplayItemFromLootTable(ServerLevel serverLevel, BlockPos pos, ResourceKey<LootTable> lootTableId) {
/* 193 */     LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(lootTableId);
/* 196 */     LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)pos)).create(LootContextParamSets.VAULT);
/* 197 */     ObjectArrayList objectArrayList = lootTable.getRandomItems(params, serverLevel.getRandom());
/* 199 */     if (objectArrayList.isEmpty())
/* 200 */       return ItemStack.EMPTY; 
/* 203 */     return (ItemStack)Util.getRandom((List)objectArrayList, serverLevel.getRandom());
/*     */   }
/*     */   
/*     */   private static void unlock(ServerLevel serverLevel, BlockState blockState, BlockPos pos, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, List<ItemStack> itemsToEject) {
/* 207 */     serverData.setItemsToEject(itemsToEject);
/* 208 */     sharedData.setDisplayItem(serverData.getNextItemToEject());
/* 209 */     serverData.pauseStateUpdatingUntil(serverLevel.getGameTime() + 14L);
/* 210 */     setVaultState(serverLevel, pos, blockState, (BlockState)blockState.setValue(VaultBlock.STATE, VaultState.UNLOCKING), config, sharedData);
/*     */   }
/*     */   
/*     */   private static List<ItemStack> resolveItemsToEject(ServerLevel serverLevel, VaultConfig config, BlockPos pos, Player player, ItemInstance insertedStack) {
/* 214 */     LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(config.lootTable());
/* 220 */     LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)pos)).withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player).withParameter(LootContextParams.TOOL, insertedStack).create(LootContextParamSets.VAULT);
/* 222 */     return (List<ItemStack>)lootTable.getRandomItems(params);
/*     */   }
/*     */   
/*     */   private static boolean canEjectReward(VaultConfig config, VaultState vaultState) {
/* 226 */     return (!config.keyItem().isEmpty() && vaultState != VaultState.INACTIVE);
/*     */   }
/*     */   
/*     */   private static boolean isValidToInsert(VaultConfig config, ItemStack stackToInsert) {
/* 230 */     return (ItemStack.isSameItemSameComponents(stackToInsert, config.keyItem()) && stackToInsert.getCount() >= config.keyItem().getCount());
/*     */   }
/*     */   
/*     */   private static boolean shouldCycleDisplayItem(long gameTime, VaultState vaultState) {
/* 234 */     return (gameTime % 20L == 0L && vaultState == VaultState.ACTIVE);
/*     */   }
/*     */   
/*     */   private static void playInsertFailSound(ServerLevel serverLevel, VaultServerData serverData, BlockPos pos, SoundEvent sound) {
/* 238 */     if (serverLevel.getGameTime() >= serverData.getLastInsertFailTimestamp() + 15L) {
/* 239 */       serverLevel.playSound(null, pos, sound, SoundSource.BLOCKS);
/* 240 */       serverData.setLastInsertFailTimestamp(serverLevel.getGameTime());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\vault\VaultBlockEntity$Server.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */