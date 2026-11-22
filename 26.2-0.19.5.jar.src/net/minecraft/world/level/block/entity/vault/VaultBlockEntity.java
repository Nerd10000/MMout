/*     */ package net.minecraft.world.level.block.entity.vault;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.VaultBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class VaultBlockEntity extends BlockEntity {
/*     */   private final VaultServerData serverData;
/*     */   
/*     */   private final VaultSharedData sharedData;
/*     */   
/*     */   private final VaultClientData clientData;
/*     */   
/*     */   private VaultConfig config;
/*     */   
/*     */   public VaultBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  53 */     super(BlockEntityTypes.VAULT, worldPosition, blockState);
/*  54 */     this.serverData = new VaultServerData();
/*  55 */     this.sharedData = new VaultSharedData();
/*  56 */     this.clientData = new VaultClientData();
/*  57 */     this.config = VaultConfig.DEFAULT;
/*     */   }
/*     */   
/*     */   public Packet<ClientGamePacketListener> getUpdatePacket() {
/*  62 */     return (Packet<ClientGamePacketListener>)ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/*  67 */     return (CompoundTag)Util.make(new CompoundTag(), tag -> tag.store("shared_data", VaultSharedData.CODEC, (DynamicOps)registries.createSerializationContext((DynamicOps)NbtOps.INSTANCE), this.sharedData));
/*     */   }
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  72 */     super.saveAdditional(output);
/*  74 */     output.store("config", VaultConfig.CODEC, this.config);
/*  75 */     output.store("shared_data", VaultSharedData.CODEC, this.sharedData);
/*  76 */     output.store("server_data", VaultServerData.CODEC, this.serverData);
/*     */   }
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  81 */     super.loadAdditional(input);
/*  83 */     Objects.requireNonNull(this.serverData);
/*  83 */     input.read("server_data", VaultServerData.CODEC).ifPresent(this.serverData::set);
/*  84 */     this.config = input.read("config", VaultConfig.CODEC).orElse(VaultConfig.DEFAULT);
/*  85 */     Objects.requireNonNull(this.sharedData);
/*  85 */     input.read("shared_data", VaultSharedData.CODEC).ifPresent(this.sharedData::set);
/*     */   }
/*     */   
/*     */   public VaultServerData getServerData() {
/*  89 */     return (this.level == null || this.level.isClientSide()) ? null : this.serverData;
/*     */   }
/*     */   
/*     */   public VaultSharedData getSharedData() {
/*  93 */     return this.sharedData;
/*     */   }
/*     */   
/*     */   public VaultClientData getClientData() {
/*  97 */     return this.clientData;
/*     */   }
/*     */   
/*     */   public VaultConfig getConfig() {
/* 101 */     return this.config;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setConfig(VaultConfig config) {
/* 106 */     this.config = config;
/*     */   }
/*     */   
/*     */   public static final class Server {
/*     */     private static final int UNLOCKING_DELAY_TICKS = 14;
/*     */     
/*     */     private static final int DISPLAY_CYCLE_TICK_RATE = 20;
/*     */     
/*     */     private static final int INSERT_FAIL_SOUND_BUFFER_TICKS = 15;
/*     */     
/*     */     public static void tick(ServerLevel serverLevel, BlockPos pos, BlockState blockState, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData) {
/* 115 */       VaultState currentState = (VaultState)blockState.getValue(VaultBlock.STATE);
/* 117 */       if (shouldCycleDisplayItem(serverLevel.getGameTime(), currentState))
/* 118 */         cycleDisplayItemFromLootTable(serverLevel, currentState, config, sharedData, pos); 
/* 121 */       BlockState nextBlockState = blockState;
/* 122 */       if (serverLevel.getGameTime() >= serverData.stateUpdatingResumesAt()) {
/* 123 */         nextBlockState = (BlockState)nextBlockState.setValue(VaultBlock.STATE, currentState.tickAndGetNext(serverLevel, pos, config, serverData, sharedData));
/* 125 */         if (blockState != nextBlockState)
/* 126 */           setVaultState(serverLevel, pos, blockState, nextBlockState, config, sharedData); 
/*     */       } 
/* 130 */       if (serverData.isDirty || sharedData.isDirty) {
/* 132 */         VaultBlockEntity.setChanged((Level)serverLevel, pos, blockState);
/* 135 */         if (sharedData.isDirty)
/* 136 */           serverLevel.sendBlockUpdated(pos, blockState, nextBlockState, 2); 
/* 138 */         serverData.isDirty = false;
/* 139 */         sharedData.isDirty = false;
/*     */       } 
/*     */     }
/*     */     
/*     */     public static void tryInsertKey(ServerLevel serverLevel, BlockPos pos, BlockState blockState, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, Player player, ItemStack stackToInsert) {
/* 144 */       VaultState vaultState = (VaultState)blockState.getValue(VaultBlock.STATE);
/* 146 */       if (!canEjectReward(config, vaultState))
/*     */         return; 
/* 150 */       if (!isValidToInsert(config, stackToInsert)) {
/* 151 */         playInsertFailSound(serverLevel, serverData, pos, SoundEvents.VAULT_INSERT_ITEM_FAIL);
/*     */         return;
/*     */       } 
/* 155 */       if (serverData.hasRewardedPlayer(player)) {
/* 156 */         playInsertFailSound(serverLevel, serverData, pos, SoundEvents.VAULT_REJECT_REWARDED_PLAYER);
/*     */         return;
/*     */       } 
/* 160 */       List<ItemStack> itemsToEject = resolveItemsToEject(serverLevel, config, pos, player, (ItemInstance)stackToInsert);
/* 161 */       if (itemsToEject.isEmpty())
/*     */         return; 
/* 165 */       player.awardStat(Stats.ITEM_USED.get(stackToInsert.getItem()));
/* 166 */       stackToInsert.consume(config.keyItem().getCount(), (LivingEntity)player);
/* 168 */       unlock(serverLevel, blockState, pos, config, serverData, sharedData, itemsToEject);
/* 169 */       serverData.addToRewardedPlayers(player);
/* 170 */       sharedData.updateConnectedPlayersWithinRange(serverLevel, pos, serverData, config, config.deactivationRange());
/*     */     }
/*     */     
/*     */     private static void setVaultState(ServerLevel serverLevel, BlockPos pos, BlockState currentBlockState, BlockState newBlockState, VaultConfig config, VaultSharedData sharedData) {
/* 174 */       VaultState currentVaultState = (VaultState)currentBlockState.getValue(VaultBlock.STATE);
/* 175 */       VaultState newVaultState = (VaultState)newBlockState.getValue(VaultBlock.STATE);
/* 177 */       serverLevel.setBlock(pos, newBlockState, 3);
/* 178 */       currentVaultState.onTransition(serverLevel, pos, newVaultState, config, sharedData, ((Boolean)newBlockState.getValue((Property)VaultBlock.OMINOUS)).booleanValue());
/*     */     }
/*     */     
/*     */     static void cycleDisplayItemFromLootTable(ServerLevel serverLevel, VaultState vaultState, VaultConfig config, VaultSharedData sharedData, BlockPos pos) {
/* 183 */       if (!canEjectReward(config, vaultState)) {
/* 184 */         sharedData.setDisplayItem(ItemStack.EMPTY);
/*     */         return;
/*     */       } 
/* 188 */       ItemStack displayItem = getRandomDisplayItemFromLootTable(serverLevel, pos, config.overrideLootTableToDisplay().orElse(config.lootTable()));
/* 189 */       sharedData.setDisplayItem(displayItem);
/*     */     }
/*     */     
/*     */     private static ItemStack getRandomDisplayItemFromLootTable(ServerLevel serverLevel, BlockPos pos, ResourceKey<LootTable> lootTableId) {
/* 193 */       LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(lootTableId);
/* 196 */       LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)pos)).create(LootContextParamSets.VAULT);
/* 197 */       ObjectArrayList objectArrayList = lootTable.getRandomItems(params, serverLevel.getRandom());
/* 199 */       if (objectArrayList.isEmpty())
/* 200 */         return ItemStack.EMPTY; 
/* 203 */       return (ItemStack)Util.getRandom((List)objectArrayList, serverLevel.getRandom());
/*     */     }
/*     */     
/*     */     private static void unlock(ServerLevel serverLevel, BlockState blockState, BlockPos pos, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, List<ItemStack> itemsToEject) {
/* 207 */       serverData.setItemsToEject(itemsToEject);
/* 208 */       sharedData.setDisplayItem(serverData.getNextItemToEject());
/* 209 */       serverData.pauseStateUpdatingUntil(serverLevel.getGameTime() + 14L);
/* 210 */       setVaultState(serverLevel, pos, blockState, (BlockState)blockState.setValue(VaultBlock.STATE, VaultState.UNLOCKING), config, sharedData);
/*     */     }
/*     */     
/*     */     private static List<ItemStack> resolveItemsToEject(ServerLevel serverLevel, VaultConfig config, BlockPos pos, Player player, ItemInstance insertedStack) {
/* 214 */       LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(config.lootTable());
/* 220 */       LootParams params = (new LootParams.Builder(serverLevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf((Vec3i)pos)).withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player).withParameter(LootContextParams.TOOL, insertedStack).create(LootContextParamSets.VAULT);
/* 222 */       return (List<ItemStack>)lootTable.getRandomItems(params);
/*     */     }
/*     */     
/*     */     private static boolean canEjectReward(VaultConfig config, VaultState vaultState) {
/* 226 */       return (!config.keyItem().isEmpty() && vaultState != VaultState.INACTIVE);
/*     */     }
/*     */     
/*     */     private static boolean isValidToInsert(VaultConfig config, ItemStack stackToInsert) {
/* 230 */       return (ItemStack.isSameItemSameComponents(stackToInsert, config.keyItem()) && stackToInsert.getCount() >= config.keyItem().getCount());
/*     */     }
/*     */     
/*     */     private static boolean shouldCycleDisplayItem(long gameTime, VaultState vaultState) {
/* 234 */       return (gameTime % 20L == 0L && vaultState == VaultState.ACTIVE);
/*     */     }
/*     */     
/*     */     private static void playInsertFailSound(ServerLevel serverLevel, VaultServerData serverData, BlockPos pos, SoundEvent sound) {
/* 238 */       if (serverLevel.getGameTime() >= serverData.getLastInsertFailTimestamp() + 15L) {
/* 239 */         serverLevel.playSound(null, pos, sound, SoundSource.BLOCKS);
/* 240 */         serverData.setLastInsertFailTimestamp(serverLevel.getGameTime());
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Client {
/*     */     private static final int PARTICLE_TICK_RATE = 20;
/*     */     
/*     */     private static final float IDLE_PARTICLE_CHANCE = 0.5F;
/*     */     
/*     */     private static final float AMBIENT_SOUND_CHANCE = 0.02F;
/*     */     
/*     */     private static final int ACTIVATION_PARTICLE_COUNT = 20;
/*     */     
/*     */     private static final int DEACTIVATION_PARTICLE_COUNT = 20;
/*     */     
/*     */     public static void tick(Level clientLevel, BlockPos pos, BlockState blockState, VaultClientData clientData, VaultSharedData sharedData) {
/* 253 */       clientData.updateDisplayItemSpin();
/* 255 */       if (clientLevel.getGameTime() % 20L == 0L)
/* 256 */         emitConnectionParticlesForNearbyPlayers(clientLevel, pos, blockState, sharedData); 
/* 259 */       emitIdleParticles(clientLevel, pos, sharedData, ((Boolean)blockState.getValue((Property)VaultBlock.OMINOUS)).booleanValue() ? (ParticleOptions)ParticleTypes.SOUL_FIRE_FLAME : (ParticleOptions)ParticleTypes.SMALL_FLAME);
/* 260 */       playIdleSounds(clientLevel, pos, sharedData);
/*     */     }
/*     */     
/*     */     public static void emitActivationParticles(Level clientLevel, BlockPos pos, BlockState blockState, VaultSharedData sharedData, ParticleOptions flameParticle) {
/* 264 */       emitConnectionParticlesForNearbyPlayers(clientLevel, pos, blockState, sharedData);
/* 265 */       RandomSource random = clientLevel.getRandom();
/* 266 */       for (int i = 0; i < 20; i++) {
/* 267 */         Vec3 particlePos = randomPosInsideCage(pos, random);
/* 268 */         clientLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
/* 269 */         clientLevel.addParticle(flameParticle, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */     }
/*     */     
/*     */     public static void emitDeactivationParticles(Level clientLevel, BlockPos pos, ParticleOptions flameParticle) {
/* 274 */       RandomSource random = clientLevel.getRandom();
/* 275 */       for (int i = 0; i < 20; i++) {
/* 276 */         Vec3 particlePos = randomPosCenterOfCage(pos, random);
/* 277 */         Vec3 dir = new Vec3(random.nextGaussian() * 0.02D, random.nextGaussian() * 0.02D, random.nextGaussian() * 0.02D);
/* 278 */         clientLevel.addParticle(flameParticle, particlePos.x(), particlePos.y(), particlePos.z(), dir.x(), dir.y(), dir.z());
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void emitIdleParticles(Level clientLevel, BlockPos pos, VaultSharedData sharedData, ParticleOptions flameParticle) {
/* 283 */       RandomSource random = clientLevel.getRandom();
/* 284 */       if (random.nextFloat() <= 0.5F) {
/* 285 */         Vec3 particlePos = randomPosInsideCage(pos, random);
/* 286 */         clientLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
/* 287 */         if (shouldDisplayActiveEffects(sharedData))
/* 288 */           clientLevel.addParticle(flameParticle, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D); 
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void emitConnectionParticlesForPlayer(Level level, Vec3 flyTowards, Player player) {
/* 294 */       RandomSource random = level.getRandom();
/* 295 */       Vec3 direction = flyTowards.vectorTo(player.position().add(0.0D, (player.getBbHeight() / 2.0F), 0.0D));
/* 296 */       int particleCount = Mth.nextInt(random, 2, 5);
/* 297 */       for (int i = 0; i < particleCount; i++) {
/* 298 */         Vec3 randomDirection = direction.offsetRandom(random, 1.0F);
/* 299 */         level.addParticle((ParticleOptions)ParticleTypes.VAULT_CONNECTION, flyTowards.x(), flyTowards.y(), flyTowards.z(), randomDirection.x(), randomDirection.y(), randomDirection.z());
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void emitConnectionParticlesForNearbyPlayers(Level level, BlockPos pos, BlockState blockState, VaultSharedData sharedData) {
/* 304 */       Set<UUID> connectedPlayers = sharedData.getConnectedPlayers();
/* 306 */       if (connectedPlayers.isEmpty())
/*     */         return; 
/* 310 */       Vec3 keyholePos = keyholePos(pos, (Direction)blockState.getValue((Property)VaultBlock.FACING));
/* 312 */       for (UUID uuid : connectedPlayers) {
/* 313 */         Player player = level.getPlayerByUUID(uuid);
/* 314 */         if (player == null || !isWithinConnectionRange(pos, sharedData, player))
/*     */           continue; 
/* 318 */         emitConnectionParticlesForPlayer(level, keyholePos, player);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static boolean isWithinConnectionRange(BlockPos vaultPos, VaultSharedData sharedData, Player player) {
/* 323 */       return (player.blockPosition().distSqr((Vec3i)vaultPos) <= Mth.square(sharedData.connectedParticlesRange()));
/*     */     }
/*     */     
/*     */     private static void playIdleSounds(Level clientLevel, BlockPos pos, VaultSharedData sharedData) {
/* 327 */       if (!shouldDisplayActiveEffects(sharedData))
/*     */         return; 
/* 331 */       RandomSource random = clientLevel.getRandom();
/* 332 */       if (random.nextFloat() <= 0.02F)
/* 333 */         clientLevel.playLocalSound(pos, SoundEvents.VAULT_AMBIENT, SoundSource.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false); 
/*     */     }
/*     */     
/*     */     public static boolean shouldDisplayActiveEffects(VaultSharedData sharedData) {
/* 338 */       return sharedData.hasDisplayItem();
/*     */     }
/*     */     
/*     */     private static Vec3 randomPosCenterOfCage(BlockPos blockPos, RandomSource random) {
/* 342 */       return Vec3.atLowerCornerOf((Vec3i)blockPos).add(Mth.nextDouble(random, 0.4D, 0.6D), Mth.nextDouble(random, 0.4D, 0.6D), Mth.nextDouble(random, 0.4D, 0.6D));
/*     */     }
/*     */     
/*     */     private static Vec3 randomPosInsideCage(BlockPos blockPos, RandomSource random) {
/* 346 */       return Vec3.atLowerCornerOf((Vec3i)blockPos).add(Mth.nextDouble(random, 0.1D, 0.9D), Mth.nextDouble(random, 0.25D, 0.75D), Mth.nextDouble(random, 0.1D, 0.9D));
/*     */     }
/*     */     
/*     */     private static Vec3 keyholePos(BlockPos blockPos, Direction blockFacing) {
/* 350 */       return Vec3.atBottomCenterOf((Vec3i)blockPos).add(blockFacing.getStepX() * 0.5D, 1.75D, blockFacing.getStepZ() * 0.5D);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\vault\VaultBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */