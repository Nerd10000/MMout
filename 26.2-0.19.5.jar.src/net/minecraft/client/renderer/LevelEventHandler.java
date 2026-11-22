/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ItemParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleType;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.PowerParticleOption;
/*     */ import net.minecraft.core.particles.SculkChargeParticleOptions;
/*     */ import net.minecraft.core.particles.ShriekParticleOption;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.core.particles.SpellParticleOption;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ParticleUtils;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.BoneMealItem;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.JukeboxSong;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.BrushableBlock;
/*     */ import net.minecraft.world.level.block.ComposterBlock;
/*     */ import net.minecraft.world.level.block.MultifaceBlock;
/*     */ import net.minecraft.world.level.block.PointedDripstoneBlock;
/*     */ import net.minecraft.world.level.block.SculkShriekerBlock;
/*     */ import net.minecraft.world.level.block.SoundType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
/*     */ import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class LevelEventHandler {
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final ClientLevel level;
/*     */   
/*  59 */   private final Map<BlockPos, SoundInstance> playingJukeboxSongs = new HashMap<>();
/*     */   
/*     */   public LevelEventHandler(Minecraft minecraft, ClientLevel level) {
/*  62 */     this.minecraft = minecraft;
/*  63 */     this.level = level;
/*     */   }
/*     */   
/*     */   public void globalLevelEvent(int type, BlockPos pos, int data) {
/*     */     Camera camera;
/*  67 */     switch (type) {
/*     */       case 1023:
/*     */       case 1028:
/*     */       case 1038:
/*  69 */         camera = this.minecraft.gameRenderer.mainCamera();
/*  70 */         if (camera.isInitialized()) {
/*  72 */           Vec3 directionToEvent = Vec3.atCenterOf((Vec3i)pos).subtract(camera.position()).normalize();
/*  74 */           Vec3 soundPos = camera.position().add(directionToEvent.scale(2.0D));
/*  75 */           if (type == 1023) {
/*  76 */             this.level.playLocalSound(soundPos.x, soundPos.y, soundPos.z, SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 1.0F, 1.0F, false);
/*     */             break;
/*     */           } 
/*  77 */           if (type == 1038) {
/*  78 */             this.level.playLocalSound(soundPos.x, soundPos.y, soundPos.z, SoundEvents.END_PORTAL_SPAWN, SoundSource.HOSTILE, 1.0F, 1.0F, false);
/*     */             break;
/*     */           } 
/*  80 */           this.level.playLocalSound(soundPos.x, soundPos.y, soundPos.z, SoundEvents.ENDER_DRAGON_DEATH, SoundSource.HOSTILE, 5.0F, 1.0F, false);
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void levelEvent(int eventType, BlockPos pos, int data) {
/*     */     double x;
/*     */     Vec3 particlePos;
/*     */     BlockState blockState, blockStateForBrushing;
/*     */     int k, count, j;
/*     */     BlockState state;
/*     */     int i;
/*     */     ItemParticleOption breakParticle;
/*     */     BlockEntity blockEntity;
/*     */     boolean isSolid, isWaterlogged;
/*     */     double y;
/*     */     int m;
/*     */     float red;
/*     */     Block block;
/*     */     int particleCount;
/*     */     float green, spread;
/*     */     double z;
/*     */     float blue, speed;
/*     */     ParticleType<SpellParticleOption> particleType;
/*     */     int n;
/*     */     ItemParticleOption itemParticleOption1;
/*     */     int i1, i2;
/*     */     double angle;
/*  88 */     RandomSource random = this.level.getRandom();
/*  89 */     switch (eventType) {
/*     */       case 1035:
/*  90 */         this.level.playLocalSound(pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */         break;
/*     */       case 1033:
/*  91 */         this.level.playLocalSound(pos, SoundEvents.CHORUS_FLOWER_GROW, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */         break;
/*     */       case 1034:
/*  92 */         this.level.playLocalSound(pos, SoundEvents.CHORUS_FLOWER_DEATH, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */         break;
/*     */       case 1032:
/*  93 */         this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forLocalAmbience(SoundEvents.PORTAL_TRAVEL, random.nextFloat() * 0.4F + 0.8F, 0.25F));
/*     */         break;
/*     */       case 1001:
/*  94 */         this.level.playLocalSound(pos, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 1.0F, 1.2F, false);
/*     */         break;
/*     */       case 1000:
/*  95 */         this.level.playLocalSound(pos, SoundEvents.DISPENSER_DISPENSE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */         break;
/*     */       case 1049:
/*  96 */         this.level.playLocalSound(pos, SoundEvents.CRAFTER_CRAFT, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */         break;
/*     */       case 1050:
/*  97 */         this.level.playLocalSound(pos, SoundEvents.CRAFTER_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */         break;
/*     */       case 1004:
/*  98 */         this.level.playLocalSound(pos, SoundEvents.FIREWORK_ROCKET_SHOOT, SoundSource.NEUTRAL, 1.0F, 1.2F, false);
/*     */         break;
/*     */       case 1002:
/*  99 */         this.level.playLocalSound(pos, SoundEvents.DISPENSER_LAUNCH, SoundSource.BLOCKS, 1.0F, 1.2F, false);
/*     */         break;
/*     */       case 1051:
/* 100 */         this.level.playLocalSound(pos, SoundEvents.WIND_CHARGE_THROW, SoundSource.BLOCKS, 0.5F, 0.4F / (this.level.getRandom().nextFloat() * 0.4F + 0.8F), false);
/*     */         break;
/*     */       case 2010:
/* 101 */         shootParticles(data, pos, random, ParticleTypes.WHITE_SMOKE);
/*     */         break;
/*     */       case 2000:
/* 102 */         shootParticles(data, pos, random, ParticleTypes.SMOKE);
/*     */         break;
/*     */       case 2003:
/* 105 */         x = pos.getX() + 0.5D;
/* 106 */         y = pos.getY();
/* 107 */         z = pos.getZ() + 0.5D;
/* 109 */         itemParticleOption1 = new ItemParticleOption(ParticleTypes.ITEM, Items.ENDER_EYE);
/* 110 */         for (i2 = 0; i2 < 8; i2++)
/* 111 */           this.level.addParticle((ParticleOptions)itemParticleOption1, x, y, z, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D); 
/* 113 */         for (angle = 0.0D; angle < 6.283185307179586D; angle += 0.15707963267948966D) {
/* 114 */           this.level.addParticle((ParticleOptions)ParticleTypes.PORTAL, x + Math.cos(angle) * 5.0D, y - 0.4D, z + Math.sin(angle) * 5.0D, Math.cos(angle) * -5.0D, 0.0D, Math.sin(angle) * -5.0D);
/* 115 */           this.level.addParticle((ParticleOptions)ParticleTypes.PORTAL, x + Math.cos(angle) * 5.0D, y - 0.4D, z + Math.sin(angle) * 5.0D, Math.cos(angle) * -7.0D, 0.0D, Math.sin(angle) * -7.0D);
/*     */         } 
/*     */         break;
/*     */       case 2002:
/*     */       case 2007:
/* 120 */         particlePos = Vec3.atBottomCenterOf((Vec3i)pos);
/* 122 */         breakParticle = new ItemParticleOption(ParticleTypes.ITEM, Items.SPLASH_POTION);
/* 123 */         for (m = 0; m < 8; m++)
/* 124 */           this.level.addParticle((ParticleOptions)breakParticle, particlePos.x, particlePos.y, particlePos.z, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D); 
/* 127 */         red = (data >> 16 & 0xFF) / 255.0F;
/* 128 */         green = (data >> 8 & 0xFF) / 255.0F;
/* 129 */         blue = (data >> 0 & 0xFF) / 255.0F;
/* 131 */         particleType = (eventType == 2007) ? ParticleTypes.INSTANT_EFFECT : ParticleTypes.EFFECT;
/* 132 */         for (i1 = 0; i1 < 100; i1++) {
/* 133 */           double dist = random.nextDouble() * 4.0D;
/* 134 */           double d1 = random.nextDouble() * Math.PI * 2.0D;
/* 135 */           double velocityX = Math.cos(d1) * dist;
/* 136 */           double velocityY = 0.01D + random.nextDouble() * 0.5D;
/* 137 */           double velocityZ = Math.sin(d1) * dist;
/* 139 */           float randomBrightness = 0.75F + random.nextFloat() * 0.25F;
/* 140 */           SpellParticleOption particle = SpellParticleOption.create(particleType, red * randomBrightness, green * randomBrightness, blue * randomBrightness, (float)dist);
/* 141 */           this.level.addParticle((ParticleOptions)particle, particlePos.x + velocityX * 0.1D, particlePos.y + 0.3D, particlePos.z + velocityZ * 0.1D, velocityX, velocityY, velocityZ);
/*     */         } 
/* 143 */         this.level.playLocalSound(pos, SoundEvents.SPLASH_POTION_BREAK, SoundSource.NEUTRAL, 1.0F, random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 2001:
/* 146 */         blockState = Block.stateById(data);
/* 147 */         if (!blockState.isAir()) {
/* 148 */           SoundType soundType = blockState.getSoundType();
/* 149 */           this.level.playLocalSound(pos, soundType.getBreakSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F, false);
/*     */         } 
/* 151 */         this.level.addDestroyBlockEffect(pos, blockState);
/*     */         break;
/*     */       case 3008:
/* 154 */         blockStateForBrushing = Block.stateById(data);
/* 155 */         block = blockStateForBrushing.getBlock();
/* 155 */         if (block instanceof BrushableBlock) {
/* 155 */           BrushableBlock brushableBlock = (BrushableBlock)block;
/* 156 */           this.level.playLocalSound(pos, brushableBlock.getBrushCompletedSound(), SoundSource.PLAYERS, 1.0F, 1.0F, false);
/*     */         } 
/* 158 */         this.level.addDestroyBlockEffect(pos, blockStateForBrushing);
/*     */         break;
/*     */       case 2004:
/* 161 */         for (k = 0; k < 20; k++) {
/* 162 */           double d1 = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 2.0D;
/* 163 */           double d2 = pos.getY() + 0.5D + (random.nextDouble() - 0.5D) * 2.0D;
/* 164 */           double d3 = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 2.0D;
/* 166 */           this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/* 167 */           this.level.addParticle((ParticleOptions)ParticleTypes.FLAME, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */         break;
/*     */       case 3011:
/* 170 */         TrialSpawner.addSpawnParticles((Level)this.level, pos, random, (TrialSpawner.FlameParticle.decode(data)).particleType);
/*     */         break;
/*     */       case 3012:
/* 172 */         this.level.playLocalSound(pos, SoundEvents.TRIAL_SPAWNER_SPAWN_MOB, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/* 173 */         TrialSpawner.addSpawnParticles((Level)this.level, pos, random, (TrialSpawner.FlameParticle.decode(data)).particleType);
/*     */         break;
/*     */       case 3021:
/* 176 */         this.level.playLocalSound(pos, SoundEvents.TRIAL_SPAWNER_SPAWN_ITEM, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/* 177 */         TrialSpawner.addSpawnParticles((Level)this.level, pos, random, (TrialSpawner.FlameParticle.decode(data)).particleType);
/*     */         break;
/*     */       case 3013:
/* 180 */         this.level.playLocalSound(pos, SoundEvents.TRIAL_SPAWNER_DETECT_PLAYER, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/* 181 */         TrialSpawner.addDetectPlayerParticles((Level)this.level, pos, random, data, (ParticleOptions)ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER);
/*     */         break;
/*     */       case 3019:
/* 184 */         this.level.playLocalSound(pos, SoundEvents.TRIAL_SPAWNER_DETECT_PLAYER, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/* 185 */         TrialSpawner.addDetectPlayerParticles((Level)this.level, pos, random, data, (ParticleOptions)ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS);
/*     */         break;
/*     */       case 3020:
/* 188 */         this.level.playLocalSound(pos, SoundEvents.TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundSource.BLOCKS, (data == 0) ? 0.3F : 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/* 189 */         TrialSpawner.addDetectPlayerParticles((Level)this.level, pos, random, 0, (ParticleOptions)ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS);
/* 190 */         TrialSpawner.addBecomeOminousParticles((Level)this.level, pos, random);
/*     */         break;
/*     */       case 3014:
/* 193 */         this.level.playLocalSound(pos, SoundEvents.TRIAL_SPAWNER_EJECT_ITEM, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/* 194 */         TrialSpawner.addEjectItemParticles((Level)this.level, pos, random);
/*     */         break;
/*     */       case 3017:
/* 196 */         TrialSpawner.addEjectItemParticles((Level)this.level, pos, random);
/*     */         break;
/*     */       case 3015:
/* 198 */         blockEntity = this.level.getBlockEntity(pos);
/* 198 */         if (blockEntity instanceof VaultBlockEntity) {
/* 198 */           VaultBlockEntity entity = (VaultBlockEntity)blockEntity;
/* 199 */           VaultBlockEntity.Client.emitActivationParticles((Level)this.level, entity.getBlockPos(), entity.getBlockState(), entity.getSharedData(), (data == 0) ? (ParticleOptions)ParticleTypes.SMALL_FLAME : (ParticleOptions)ParticleTypes.SOUL_FIRE_FLAME);
/* 200 */           this.level.playLocalSound(pos, SoundEvents.VAULT_ACTIVATE, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/*     */         } 
/*     */         break;
/*     */       case 3016:
/* 204 */         VaultBlockEntity.Client.emitDeactivationParticles((Level)this.level, pos, (data == 0) ? (ParticleOptions)ParticleTypes.SMALL_FLAME : (ParticleOptions)ParticleTypes.SOUL_FIRE_FLAME);
/* 205 */         this.level.playLocalSound(pos, SoundEvents.VAULT_DEACTIVATE, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/*     */         break;
/*     */       case 3018:
/* 208 */         for (k = 0; k < 10; k++) {
/* 209 */           double velocityX = random.nextGaussian() * 0.02D;
/* 210 */           double velocityY = random.nextGaussian() * 0.02D;
/* 211 */           double velocityZ = random.nextGaussian() * 0.02D;
/* 212 */           this.level.addParticle((ParticleOptions)ParticleTypes.POOF, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), velocityX, velocityY, velocityZ);
/*     */         } 
/* 214 */         this.level.playLocalSound(pos, SoundEvents.COBWEB_PLACE, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);
/*     */         break;
/*     */       case 1505:
/* 217 */         BoneMealItem.addGrowthParticles((LevelAccessor)this.level, pos, data);
/* 218 */         this.level.playLocalSound(pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */         break;
/*     */       case 2011:
/* 220 */         ParticleUtils.spawnParticleInBlock((LevelAccessor)this.level, pos, data, (ParticleOptions)ParticleTypes.HAPPY_VILLAGER);
/*     */         break;
/*     */       case 2012:
/* 221 */         ParticleUtils.spawnParticleInBlock((LevelAccessor)this.level, pos, data, (ParticleOptions)ParticleTypes.HAPPY_VILLAGER);
/*     */         break;
/*     */       case 3009:
/* 222 */         ParticleUtils.spawnParticlesOnBlockFaces((Level)this.level, pos, (ParticleOptions)ParticleTypes.EGG_CRACK, (IntProvider)UniformInt.of(3, 6));
/*     */         break;
/*     */       case 3002:
/* 224 */         if (data >= 0 && data < Direction.Axis.VALUES.length) {
/* 225 */           ParticleUtils.spawnParticlesAlongAxis(Direction.Axis.VALUES[data], (Level)this.level, pos, 0.125D, (ParticleOptions)ParticleTypes.ELECTRIC_SPARK, UniformInt.of(10, 19));
/*     */           break;
/*     */         } 
/* 227 */         ParticleUtils.spawnParticlesOnBlockFaces((Level)this.level, pos, (ParticleOptions)ParticleTypes.ELECTRIC_SPARK, (IntProvider)UniformInt.of(3, 5));
/*     */         break;
/*     */       case 2013:
/* 230 */         ParticleUtils.spawnSmashAttackParticles((LevelAccessor)this.level, pos, data);
/*     */         break;
/*     */       case 3006:
/* 232 */         count = data >> 6;
/* 233 */         if (count > 0) {
/* 235 */           if (random.nextFloat() < 0.3F + count * 0.1F) {
/* 236 */             float volume = 0.15F + 0.02F * count * count * random.nextFloat();
/* 237 */             float pitch = 0.4F + 0.3F * count * random.nextFloat();
/* 238 */             this.level.playLocalSound(pos, SoundEvents.SCULK_BLOCK_CHARGE, SoundSource.BLOCKS, volume, pitch, false);
/*     */           } 
/* 240 */           byte particleData = (byte)(data & 0x3F);
/* 241 */           UniformInt uniformInt = UniformInt.of(0, count);
/* 242 */           float speedVar = 0.005F;
/* 243 */           Supplier<Vec3> speedSupplier = () -> new Vec3(Mth.nextDouble(random, -0.004999999888241291D, 0.004999999888241291D), Mth.nextDouble(random, -0.004999999888241291D, 0.004999999888241291D), Mth.nextDouble(random, -0.004999999888241291D, 0.004999999888241291D));
/* 248 */           if (particleData == 0) {
/* 249 */             for (Direction direction : Direction.values()) {
/* 250 */               float fullBlockRotation = (direction == Direction.DOWN) ? 3.1415927F : 0.0F;
/* 251 */               double fullBlockFactor = (direction.getAxis() == Direction.Axis.Y) ? 0.65D : 0.57D;
/* 252 */               ParticleUtils.spawnParticlesOnBlockFace((Level)this.level, pos, (ParticleOptions)new SculkChargeParticleOptions(fullBlockRotation), (IntProvider)uniformInt, direction, speedSupplier, fullBlockFactor);
/*     */             } 
/*     */             break;
/*     */           } 
/* 255 */           for (Direction direction : MultifaceBlock.unpack(particleData)) {
/* 256 */             float facesBlockRotation = (direction == Direction.UP) ? 3.1415927F : 0.0F;
/* 257 */             double facesBlockFactor = 0.35D;
/* 258 */             ParticleUtils.spawnParticlesOnBlockFace((Level)this.level, pos, (ParticleOptions)new SculkChargeParticleOptions(facesBlockRotation), (IntProvider)uniformInt, direction, speedSupplier, 0.35D);
/*     */           } 
/*     */           break;
/*     */         } 
/* 262 */         this.level.playLocalSound(pos, SoundEvents.SCULK_BLOCK_CHARGE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/* 263 */         isSolid = this.level.getBlockState(pos).isCollisionShapeFullBlock((BlockGetter)this.level, pos);
/* 264 */         particleCount = isSolid ? 40 : 20;
/* 265 */         spread = isSolid ? 0.45F : 0.25F;
/* 266 */         speed = 0.07F;
/* 267 */         for (n = 0; n < particleCount; n++) {
/* 268 */           float velocityX = 2.0F * random.nextFloat() - 1.0F;
/* 269 */           float velocityY = 2.0F * random.nextFloat() - 1.0F;
/* 270 */           float velocityZ = 2.0F * random.nextFloat() - 1.0F;
/* 271 */           this.level.addParticle((ParticleOptions)ParticleTypes.SCULK_CHARGE_POP, pos
/*     */               
/* 273 */               .getX() + 0.5D + (velocityX * spread), pos.getY() + 0.5D + (velocityY * spread), pos.getZ() + 0.5D + (velocityZ * spread), (velocityX * 0.07F), (velocityY * 0.07F), (velocityZ * 0.07F));
/*     */         } 
/*     */         break;
/*     */       case 3007:
/* 280 */         for (j = 0; j < 10; j++)
/* 281 */           this.level.addParticle((ParticleOptions)new ShriekParticleOption(j * 5), pos.getX() + 0.5D, pos.getY() + SculkShriekerBlock.TOP_Y, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D); 
/* 283 */         state = this.level.getBlockState(pos);
/* 284 */         isWaterlogged = (state.hasProperty((Property)BlockStateProperties.WATERLOGGED) && ((Boolean)state.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue());
/* 286 */         if (!isWaterlogged)
/* 287 */           this.level.playLocalSound(pos.getX() + 0.5D, pos.getY() + SculkShriekerBlock.TOP_Y, pos.getZ() + 0.5D, SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.BLOCKS, 2.0F, 0.6F + random.nextFloat() * 0.4F, false); 
/*     */         break;
/*     */       case 3003:
/* 291 */         ParticleUtils.spawnParticlesOnBlockFaces((Level)this.level, pos, (ParticleOptions)ParticleTypes.WAX_ON, (IntProvider)UniformInt.of(3, 5));
/* 292 */         this.level.playLocalSound(pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*     */         break;
/*     */       case 3004:
/* 294 */         ParticleUtils.spawnParticlesOnBlockFaces((Level)this.level, pos, (ParticleOptions)ParticleTypes.WAX_OFF, (IntProvider)UniformInt.of(3, 5));
/*     */         break;
/*     */       case 3005:
/* 295 */         ParticleUtils.spawnParticlesOnBlockFaces((Level)this.level, pos, (ParticleOptions)ParticleTypes.SCRAPE, (IntProvider)UniformInt.of(3, 5));
/*     */         break;
/*     */       case 2008:
/* 296 */         this.level.addParticle((ParticleOptions)ParticleTypes.EXPLOSION, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
/*     */         break;
/*     */       case 1500:
/* 297 */         ComposterBlock.handleFill((Level)this.level, pos, (data > 0));
/*     */         break;
/*     */       case 1504:
/* 298 */         PointedDripstoneBlock.spawnDripParticle((Level)this.level, pos, this.level.getBlockState(pos));
/*     */         break;
/*     */       case 1501:
/* 300 */         this.level.playLocalSound(pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F, false);
/* 301 */         for (i = 0; i < 8; i++)
/* 302 */           this.level.addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, pos.getX() + random.nextDouble(), pos.getY() + 1.2D, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D); 
/*     */         break;
/*     */       case 1502:
/* 306 */         this.level.playLocalSound(pos, SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.BLOCKS, 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F, false);
/* 307 */         for (i = 0; i < 5; i++) {
/* 308 */           double d1 = pos.getX() + random.nextDouble() * 0.6D + 0.2D;
/* 309 */           double d2 = pos.getY() + random.nextDouble() * 0.6D + 0.2D;
/* 310 */           double d3 = pos.getZ() + random.nextDouble() * 0.6D + 0.2D;
/* 312 */           this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */         break;
/*     */       case 1503:
/* 316 */         this.level.playLocalSound(pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/* 317 */         for (i = 0; i < 16; i++) {
/* 318 */           double d1 = pos.getX() + (5.0D + random.nextDouble() * 6.0D) / 16.0D;
/* 319 */           double d2 = pos.getY() + 0.8125D;
/* 320 */           double d3 = pos.getZ() + (5.0D + random.nextDouble() * 6.0D) / 16.0D;
/* 322 */           this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, d1, d2, d3, 0.0D, 0.0D, 0.0D);
/*     */         } 
/*     */         break;
/*     */       case 2006:
/* 326 */         for (i = 0; i < 200; i++) {
/* 327 */           float dist = random.nextFloat() * 4.0F;
/* 328 */           float f1 = random.nextFloat() * 6.2831855F;
/* 329 */           double velocityX = (Mth.cos(f1) * dist);
/* 330 */           double velocityY = 0.01D + random.nextDouble() * 0.5D;
/* 331 */           double velocityZ = (Mth.sin(f1) * dist);
/* 333 */           this.level.addParticle((ParticleOptions)PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, dist), pos.getX() + velocityX * 0.1D, pos.getY() + 0.3D, pos.getZ() + velocityZ * 0.1D, velocityX, velocityY, velocityZ);
/*     */         } 
/* 336 */         if (data == 1)
/* 337 */           this.level.playLocalSound(pos, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.HOSTILE, 1.0F, random.nextFloat() * 0.1F + 0.9F, false); 
/*     */         break;
/*     */       case 2009:
/* 341 */         for (i = 0; i < 8; i++)
/* 342 */           this.level.addParticle((ParticleOptions)ParticleTypes.CLOUD, pos.getX() + random.nextDouble(), pos.getY() + 1.2D, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D); 
/*     */         break;
/*     */       case 1009:
/* 346 */         if (data == 0) {
/* 347 */           this.level.playLocalSound(pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F, false);
/*     */           break;
/*     */         } 
/* 348 */         if (data == 1)
/* 349 */           this.level.playLocalSound(pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.7F, 1.6F + (random.nextFloat() - random.nextFloat()) * 0.4F, false); 
/*     */         break;
/*     */       case 1029:
/* 352 */         this.level.playLocalSound(pos, SoundEvents.ANVIL_DESTROY, SoundSource.BLOCKS, 1.0F, random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1030:
/* 353 */         this.level.playLocalSound(pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0F, random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1044:
/* 354 */         this.level.playLocalSound(pos, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 1.0F, random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1031:
/* 355 */         this.level.playLocalSound(pos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.3F, random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1039:
/* 356 */         this.level.playLocalSound(pos, SoundEvents.PHANTOM_BITE, SoundSource.HOSTILE, 0.3F, random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1010:
/* 357 */         this.level.registryAccess().lookupOrThrow(Registries.JUKEBOX_SONG).get(data).ifPresent(song -> playJukeboxSong((Holder<JukeboxSong>)song, pos));
/*     */         break;
/*     */       case 1011:
/* 358 */         stopJukeboxSongAndNotifyNearby(pos);
/*     */         break;
/*     */       case 1015:
/* 359 */         this.level.playLocalSound(pos, SoundEvents.GHAST_WARN, SoundSource.HOSTILE, 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1017:
/* 360 */         this.level.playLocalSound(pos, SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.HOSTILE, 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1016:
/* 361 */         this.level.playLocalSound(pos, SoundEvents.GHAST_SHOOT, SoundSource.HOSTILE, 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1019:
/* 362 */         this.level.playLocalSound(pos, SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1022:
/* 363 */         this.level.playLocalSound(pos, SoundEvents.WITHER_BREAK_BLOCK, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1021:
/* 364 */         this.level.playLocalSound(pos, SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1020:
/* 365 */         this.level.playLocalSound(pos, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1018:
/* 366 */         this.level.playLocalSound(pos, SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1024:
/* 367 */         this.level.playLocalSound(pos, SoundEvents.WITHER_SHOOT, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1026:
/* 368 */         this.level.playLocalSound(pos, SoundEvents.ZOMBIE_INFECT, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1027:
/* 369 */         this.level.playLocalSound(pos, SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1040:
/* 370 */         this.level.playLocalSound(pos, SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1041:
/* 371 */         this.level.playLocalSound(pos, SoundEvents.HUSK_CONVERTED_TO_ZOMBIE, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1025:
/* 372 */         this.level.playLocalSound(pos, SoundEvents.BAT_TAKEOFF, SoundSource.NEUTRAL, 0.05F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1042:
/* 373 */         this.level.playLocalSound(pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1.0F, random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1043:
/* 374 */         this.level.playLocalSound(pos, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 1.0F, random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 3000:
/* 376 */         this.level.addAlwaysVisibleParticle((ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, true, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
/* 377 */         this.level.playLocalSound(pos, SoundEvents.END_GATEWAY_SPAWN, SoundSource.BLOCKS, 10.0F, (1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F, false);
/*     */         break;
/*     */       case 3001:
/* 379 */         this.level.playLocalSound(pos, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 64.0F, 0.8F + random.nextFloat() * 0.3F, false);
/*     */         break;
/*     */       case 1045:
/* 380 */         this.level.playLocalSound(pos, SoundEvents.POINTED_DRIPSTONE_LAND, SoundSource.BLOCKS, 2.0F, random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1046:
/* 381 */         this.level.playLocalSound(pos, SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON, SoundSource.BLOCKS, 2.0F, random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1047:
/* 382 */         this.level.playLocalSound(pos, SoundEvents.POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON, SoundSource.BLOCKS, 2.0F, random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */       case 1048:
/* 383 */         this.level.playLocalSound(pos, SoundEvents.SKELETON_CONVERTED_TO_STRAY, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*     */         break;
/*     */       case 1052:
/* 384 */         this.level.playLocalSound(pos, SoundEvents.SULFUR_SPIKE_LAND, SoundSource.BLOCKS, 2.0F, random.nextFloat() * 0.1F + 0.9F, false);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void shootParticles(int data, BlockPos pos, RandomSource random, SimpleParticleType particle) {
/* 389 */     Direction direction = Direction.from3DDataValue(data);
/* 390 */     int normalX = direction.getStepX();
/* 391 */     int normalY = direction.getStepY();
/* 392 */     int normalZ = direction.getStepZ();
/* 394 */     for (int i = 0; i < 10; i++) {
/* 395 */       double pow = random.nextDouble() * 0.2D + 0.01D;
/* 396 */       double x = pos.getX() + normalX * 0.6D + 0.5D + normalX * 0.01D + (random.nextDouble() - 0.5D) * normalZ * 0.5D;
/* 397 */       double y = pos.getY() + normalY * 0.6D + 0.5D + normalY * 0.01D + (random.nextDouble() - 0.5D) * normalY * 0.5D;
/* 398 */       double z = pos.getZ() + normalZ * 0.6D + 0.5D + normalZ * 0.01D + (random.nextDouble() - 0.5D) * normalX * 0.5D;
/* 399 */       double velocityX = normalX * pow + random.nextGaussian() * 0.01D;
/* 400 */       double velocityY = normalY * pow + random.nextGaussian() * 0.01D;
/* 401 */       double velocityZ = normalZ * pow + random.nextGaussian() * 0.01D;
/* 402 */       this.level.addParticle((ParticleOptions)particle, x, y, z, velocityX, velocityY, velocityZ);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void playJukeboxSong(Holder<JukeboxSong> songHolder, BlockPos pos) {
/* 407 */     stopJukeboxSong(pos);
/* 409 */     JukeboxSong song = (JukeboxSong)songHolder.value();
/* 410 */     SoundEvent sound = (SoundEvent)song.soundEvent().value();
/* 411 */     SimpleSoundInstance simpleSoundInstance = SimpleSoundInstance.forJukeboxSong(sound, Vec3.atCenterOf((Vec3i)pos));
/* 412 */     this.playingJukeboxSongs.put(pos, simpleSoundInstance);
/* 413 */     this.minecraft.getSoundManager().play((SoundInstance)simpleSoundInstance);
/* 414 */     this.minecraft.gui.hud.setNowPlaying(song.description());
/* 415 */     notifyNearbyEntities((Level)this.level, pos, true);
/*     */   }
/*     */   
/*     */   private void stopJukeboxSong(BlockPos pos) {
/* 419 */     SoundInstance removedInstance = this.playingJukeboxSongs.remove(pos);
/* 420 */     if (removedInstance != null)
/* 421 */       this.minecraft.getSoundManager().stop(removedInstance); 
/*     */   }
/*     */   
/*     */   private void stopJukeboxSongAndNotifyNearby(BlockPos pos) {
/* 426 */     stopJukeboxSong(pos);
/* 427 */     notifyNearbyEntities((Level)this.level, pos, false);
/*     */   }
/*     */   
/*     */   private void notifyNearbyEntities(Level level, BlockPos pos, boolean isPlaying) {
/* 431 */     List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, (new AABB(pos)).inflate(3.0D));
/* 432 */     for (LivingEntity entity : entities)
/* 433 */       entity.setRecordPlayingNearby(pos, isPlaying); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\LevelEventHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */