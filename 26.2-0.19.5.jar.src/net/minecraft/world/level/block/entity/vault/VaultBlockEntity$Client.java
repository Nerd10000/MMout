/*     */ package net.minecraft.world.level.block.entity.vault;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.VaultBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class Client {
/*     */   private static final int PARTICLE_TICK_RATE = 20;
/*     */   
/*     */   private static final float IDLE_PARTICLE_CHANCE = 0.5F;
/*     */   
/*     */   private static final float AMBIENT_SOUND_CHANCE = 0.02F;
/*     */   
/*     */   private static final int ACTIVATION_PARTICLE_COUNT = 20;
/*     */   
/*     */   private static final int DEACTIVATION_PARTICLE_COUNT = 20;
/*     */   
/*     */   public static void tick(Level clientLevel, BlockPos pos, BlockState blockState, VaultClientData clientData, VaultSharedData sharedData) {
/* 253 */     clientData.updateDisplayItemSpin();
/* 255 */     if (clientLevel.getGameTime() % 20L == 0L)
/* 256 */       emitConnectionParticlesForNearbyPlayers(clientLevel, pos, blockState, sharedData); 
/* 259 */     emitIdleParticles(clientLevel, pos, sharedData, ((Boolean)blockState.getValue((Property)VaultBlock.OMINOUS)).booleanValue() ? (ParticleOptions)ParticleTypes.SOUL_FIRE_FLAME : (ParticleOptions)ParticleTypes.SMALL_FLAME);
/* 260 */     playIdleSounds(clientLevel, pos, sharedData);
/*     */   }
/*     */   
/*     */   public static void emitActivationParticles(Level clientLevel, BlockPos pos, BlockState blockState, VaultSharedData sharedData, ParticleOptions flameParticle) {
/* 264 */     emitConnectionParticlesForNearbyPlayers(clientLevel, pos, blockState, sharedData);
/* 265 */     RandomSource random = clientLevel.getRandom();
/* 266 */     for (int i = 0; i < 20; i++) {
/* 267 */       Vec3 particlePos = randomPosInsideCage(pos, random);
/* 268 */       clientLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
/* 269 */       clientLevel.addParticle(flameParticle, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void emitDeactivationParticles(Level clientLevel, BlockPos pos, ParticleOptions flameParticle) {
/* 274 */     RandomSource random = clientLevel.getRandom();
/* 275 */     for (int i = 0; i < 20; i++) {
/* 276 */       Vec3 particlePos = randomPosCenterOfCage(pos, random);
/* 277 */       Vec3 dir = new Vec3(random.nextGaussian() * 0.02D, random.nextGaussian() * 0.02D, random.nextGaussian() * 0.02D);
/* 278 */       clientLevel.addParticle(flameParticle, particlePos.x(), particlePos.y(), particlePos.z(), dir.x(), dir.y(), dir.z());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void emitIdleParticles(Level clientLevel, BlockPos pos, VaultSharedData sharedData, ParticleOptions flameParticle) {
/* 283 */     RandomSource random = clientLevel.getRandom();
/* 284 */     if (random.nextFloat() <= 0.5F) {
/* 285 */       Vec3 particlePos = randomPosInsideCage(pos, random);
/* 286 */       clientLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
/* 287 */       if (shouldDisplayActiveEffects(sharedData))
/* 288 */         clientLevel.addParticle(flameParticle, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void emitConnectionParticlesForPlayer(Level level, Vec3 flyTowards, Player player) {
/* 294 */     RandomSource random = level.getRandom();
/* 295 */     Vec3 direction = flyTowards.vectorTo(player.position().add(0.0D, (player.getBbHeight() / 2.0F), 0.0D));
/* 296 */     int particleCount = Mth.nextInt(random, 2, 5);
/* 297 */     for (int i = 0; i < particleCount; i++) {
/* 298 */       Vec3 randomDirection = direction.offsetRandom(random, 1.0F);
/* 299 */       level.addParticle((ParticleOptions)ParticleTypes.VAULT_CONNECTION, flyTowards.x(), flyTowards.y(), flyTowards.z(), randomDirection.x(), randomDirection.y(), randomDirection.z());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void emitConnectionParticlesForNearbyPlayers(Level level, BlockPos pos, BlockState blockState, VaultSharedData sharedData) {
/* 304 */     Set<UUID> connectedPlayers = sharedData.getConnectedPlayers();
/* 306 */     if (connectedPlayers.isEmpty())
/*     */       return; 
/* 310 */     Vec3 keyholePos = keyholePos(pos, (Direction)blockState.getValue((Property)VaultBlock.FACING));
/* 312 */     for (UUID uuid : connectedPlayers) {
/* 313 */       Player player = level.getPlayerByUUID(uuid);
/* 314 */       if (player == null || !isWithinConnectionRange(pos, sharedData, player))
/*     */         continue; 
/* 318 */       emitConnectionParticlesForPlayer(level, keyholePos, player);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isWithinConnectionRange(BlockPos vaultPos, VaultSharedData sharedData, Player player) {
/* 323 */     return (player.blockPosition().distSqr((Vec3i)vaultPos) <= Mth.square(sharedData.connectedParticlesRange()));
/*     */   }
/*     */   
/*     */   private static void playIdleSounds(Level clientLevel, BlockPos pos, VaultSharedData sharedData) {
/* 327 */     if (!shouldDisplayActiveEffects(sharedData))
/*     */       return; 
/* 331 */     RandomSource random = clientLevel.getRandom();
/* 332 */     if (random.nextFloat() <= 0.02F)
/* 333 */       clientLevel.playLocalSound(pos, SoundEvents.VAULT_AMBIENT, SoundSource.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false); 
/*     */   }
/*     */   
/*     */   public static boolean shouldDisplayActiveEffects(VaultSharedData sharedData) {
/* 338 */     return sharedData.hasDisplayItem();
/*     */   }
/*     */   
/*     */   private static Vec3 randomPosCenterOfCage(BlockPos blockPos, RandomSource random) {
/* 342 */     return Vec3.atLowerCornerOf((Vec3i)blockPos).add(Mth.nextDouble(random, 0.4D, 0.6D), Mth.nextDouble(random, 0.4D, 0.6D), Mth.nextDouble(random, 0.4D, 0.6D));
/*     */   }
/*     */   
/*     */   private static Vec3 randomPosInsideCage(BlockPos blockPos, RandomSource random) {
/* 346 */     return Vec3.atLowerCornerOf((Vec3i)blockPos).add(Mth.nextDouble(random, 0.1D, 0.9D), Mth.nextDouble(random, 0.25D, 0.75D), Mth.nextDouble(random, 0.1D, 0.9D));
/*     */   }
/*     */   
/*     */   private static Vec3 keyholePos(BlockPos blockPos, Direction blockFacing) {
/* 350 */     return Vec3.atBottomCenterOf((Vec3i)blockPos).add(blockFacing.getStepX() * 0.5D, 1.75D, blockFacing.getStepZ() * 0.5D);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\vault\VaultBlockEntity$Client.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */