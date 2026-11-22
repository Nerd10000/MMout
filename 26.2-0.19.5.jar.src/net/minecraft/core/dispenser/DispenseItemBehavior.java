/*     */ package net.minecraft.core.dispenser;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.PostSpawnProcessor;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.animal.armadillo.Armadillo;
/*     */ import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
/*     */ import net.minecraft.world.entity.decoration.ArmorStand;
/*     */ import net.minecraft.world.entity.item.PrimedTnt;
/*     */ import net.minecraft.world.item.BoneMealItem;
/*     */ import net.minecraft.world.item.DispensibleContainerItem;
/*     */ import net.minecraft.world.item.HoneycombItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.BeehiveBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.BucketPickup;
/*     */ import net.minecraft.world.level.block.CarvedPumpkinBlock;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.RespawnAnchorBlock;
/*     */ import net.minecraft.world.level.block.SkullBlock;
/*     */ import net.minecraft.world.level.block.WitherSkullBlock;
/*     */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public interface DispenseItemBehavior {
/*  58 */   public static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final DispenseItemBehavior NOOP;
/*     */   
/*     */   static {
/*  61 */     NOOP = ((source, dispensed) -> dispensed);
/*     */   }
/*     */   
/*     */   static void bootStrap() {
/*  69 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.ARROW);
/*  70 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.TIPPED_ARROW);
/*  71 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.SPECTRAL_ARROW);
/*  72 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.EGG);
/*  73 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.BLUE_EGG);
/*  74 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.BROWN_EGG);
/*  75 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.SNOWBALL);
/*  76 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.EXPERIENCE_BOTTLE);
/*  77 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.SPLASH_POTION);
/*  78 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.LINGERING_POTION);
/*  79 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.FIREWORK_ROCKET);
/*  80 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.FIRE_CHARGE);
/*  81 */     DispenserBlock.registerProjectileBehavior((ItemLike)Items.WIND_CHARGE);
/*  83 */     DispenserBlock.registerBehavior((ItemLike)Items.ARMOR_STAND, new DefaultDispenseItemBehavior() {
/*     */           public ItemStack execute(BlockSource source, ItemStack dispensed) {
/*  86 */             Direction direction = (Direction)source.state().getValue((Property)DispenserBlock.FACING);
/*  87 */             BlockPos pos = source.pos().relative(direction);
/*  88 */             ServerLevel serverLevel = source.level();
/*  89 */             PostSpawnProcessor<ArmorStand> postSpawnConfig = EntityType.appendDefaultStackConfig(armorStand -> armorStand.setYRot(direction.toYRot()), (Level)serverLevel, dispensed, null);
/*  90 */             ArmorStand armorStand = (ArmorStand)EntityTypes.ARMOR_STAND.spawn(serverLevel, postSpawnConfig, pos, EntitySpawnReason.DISPENSER, false, false);
/*  91 */             if (armorStand != null)
/*  92 */               dispensed.shrink(1); 
/*  94 */             return dispensed;
/*     */           }
/*     */         });
/*  98 */     DispenserBlock.registerBehavior((ItemLike)Items.CHEST, new OptionalDispenseItemBehavior() {
/*     */           public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 101 */             BlockPos pos = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 102 */             List<AbstractChestedHorse> entities = source.level().getEntitiesOfClass(AbstractChestedHorse.class, new AABB(pos), entity -> (entity.isAlive() && !entity.hasChest()));
/* 104 */             for (AbstractChestedHorse abstractChestedHorse : entities) {
/* 105 */               if (abstractChestedHorse.isTamed()) {
/* 106 */                 SlotAccess slot = abstractChestedHorse.getSlot(499);
/* 107 */                 if (slot != null && slot.set(dispensed)) {
/* 108 */                   dispensed.shrink(1);
/* 109 */                   setSuccess(true);
/* 110 */                   return dispensed;
/*     */                 } 
/*     */               } 
/*     */             } 
/* 115 */             return super.execute(source, dispensed);
/*     */           }
/*     */         });
/* 119 */     DispenserBlock.registerBehavior((ItemLike)Items.OAK_BOAT, new BoatDispenseItemBehavior(EntityTypes.OAK_BOAT));
/* 120 */     DispenserBlock.registerBehavior((ItemLike)Items.SPRUCE_BOAT, new BoatDispenseItemBehavior(EntityTypes.SPRUCE_BOAT));
/* 121 */     DispenserBlock.registerBehavior((ItemLike)Items.BIRCH_BOAT, new BoatDispenseItemBehavior(EntityTypes.BIRCH_BOAT));
/* 122 */     DispenserBlock.registerBehavior((ItemLike)Items.JUNGLE_BOAT, new BoatDispenseItemBehavior(EntityTypes.JUNGLE_BOAT));
/* 123 */     DispenserBlock.registerBehavior((ItemLike)Items.DARK_OAK_BOAT, new BoatDispenseItemBehavior(EntityTypes.DARK_OAK_BOAT));
/* 124 */     DispenserBlock.registerBehavior((ItemLike)Items.ACACIA_BOAT, new BoatDispenseItemBehavior(EntityTypes.ACACIA_BOAT));
/* 125 */     DispenserBlock.registerBehavior((ItemLike)Items.CHERRY_BOAT, new BoatDispenseItemBehavior(EntityTypes.CHERRY_BOAT));
/* 126 */     DispenserBlock.registerBehavior((ItemLike)Items.MANGROVE_BOAT, new BoatDispenseItemBehavior(EntityTypes.MANGROVE_BOAT));
/* 127 */     DispenserBlock.registerBehavior((ItemLike)Items.PALE_OAK_BOAT, new BoatDispenseItemBehavior(EntityTypes.PALE_OAK_BOAT));
/* 128 */     DispenserBlock.registerBehavior((ItemLike)Items.BAMBOO_RAFT, new BoatDispenseItemBehavior(EntityTypes.BAMBOO_RAFT));
/* 130 */     DispenserBlock.registerBehavior((ItemLike)Items.OAK_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.OAK_CHEST_BOAT));
/* 131 */     DispenserBlock.registerBehavior((ItemLike)Items.SPRUCE_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.SPRUCE_CHEST_BOAT));
/* 132 */     DispenserBlock.registerBehavior((ItemLike)Items.BIRCH_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.BIRCH_CHEST_BOAT));
/* 133 */     DispenserBlock.registerBehavior((ItemLike)Items.JUNGLE_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.JUNGLE_CHEST_BOAT));
/* 134 */     DispenserBlock.registerBehavior((ItemLike)Items.DARK_OAK_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.DARK_OAK_CHEST_BOAT));
/* 135 */     DispenserBlock.registerBehavior((ItemLike)Items.ACACIA_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.ACACIA_CHEST_BOAT));
/* 136 */     DispenserBlock.registerBehavior((ItemLike)Items.CHERRY_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.CHERRY_CHEST_BOAT));
/* 137 */     DispenserBlock.registerBehavior((ItemLike)Items.MANGROVE_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.MANGROVE_CHEST_BOAT));
/* 138 */     DispenserBlock.registerBehavior((ItemLike)Items.PALE_OAK_CHEST_BOAT, new BoatDispenseItemBehavior(EntityTypes.PALE_OAK_CHEST_BOAT));
/* 139 */     DispenserBlock.registerBehavior((ItemLike)Items.BAMBOO_CHEST_RAFT, new BoatDispenseItemBehavior(EntityTypes.BAMBOO_CHEST_RAFT));
/* 141 */     DispenseItemBehavior filledBucketBehavior = new DefaultDispenseItemBehavior() {
/* 142 */         private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*     */         
/*     */         public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 146 */           DispensibleContainerItem bucket = (DispensibleContainerItem)dispensed.getItem();
/* 147 */           BlockPos target = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 149 */           ServerLevel serverLevel = source.level();
/* 150 */           if (bucket.emptyContents(null, (Level)serverLevel, target, null)) {
/* 151 */             bucket.checkExtraContent(null, (Level)serverLevel, dispensed, target);
/* 152 */             return consumeWithRemainder(source, dispensed, new ItemStack((ItemLike)Items.BUCKET));
/*     */           } 
/* 155 */           return this.defaultDispenseItemBehavior.dispense(source, dispensed);
/*     */         }
/*     */       };
/* 158 */     DispenserBlock.registerBehavior((ItemLike)Items.LAVA_BUCKET, filledBucketBehavior);
/* 159 */     DispenserBlock.registerBehavior((ItemLike)Items.WATER_BUCKET, filledBucketBehavior);
/* 160 */     DispenserBlock.registerBehavior((ItemLike)Items.POWDER_SNOW_BUCKET, filledBucketBehavior);
/* 161 */     DispenserBlock.registerBehavior((ItemLike)Items.SALMON_BUCKET, filledBucketBehavior);
/* 162 */     DispenserBlock.registerBehavior((ItemLike)Items.COD_BUCKET, filledBucketBehavior);
/* 163 */     DispenserBlock.registerBehavior((ItemLike)Items.PUFFERFISH_BUCKET, filledBucketBehavior);
/* 164 */     DispenserBlock.registerBehavior((ItemLike)Items.TROPICAL_FISH_BUCKET, filledBucketBehavior);
/* 165 */     DispenserBlock.registerBehavior((ItemLike)Items.AXOLOTL_BUCKET, filledBucketBehavior);
/* 166 */     DispenserBlock.registerBehavior((ItemLike)Items.SULFUR_CUBE_BUCKET, filledBucketBehavior);
/* 167 */     DispenserBlock.registerBehavior((ItemLike)Items.TADPOLE_BUCKET, filledBucketBehavior);
/* 169 */     DispenserBlock.registerBehavior((ItemLike)Items.BUCKET, new DefaultDispenseItemBehavior() {
/*     */           public ItemStack execute(BlockSource source, ItemStack dispensed) {
/*     */             Item targetType;
/* 172 */             ServerLevel serverLevel = source.level();
/* 174 */             BlockPos target = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 176 */             BlockState blockState = serverLevel.getBlockState(target);
/* 177 */             Block block = blockState.getBlock();
/* 181 */             if (block instanceof BucketPickup) {
/* 181 */               BucketPickup bucket = (BucketPickup)block;
/* 182 */               ItemStack pickup = bucket.pickupBlock(null, (LevelAccessor)serverLevel, target, blockState);
/* 183 */               if (pickup.isEmpty())
/* 184 */                 return super.execute(source, dispensed); 
/* 186 */               serverLevel.gameEvent(null, (Holder)GameEvent.FLUID_PICKUP, target);
/* 187 */               targetType = pickup.getItem();
/*     */             } else {
/* 189 */               return super.execute(source, dispensed);
/*     */             } 
/* 192 */             return consumeWithRemainder(source, dispensed, new ItemStack((ItemLike)targetType));
/*     */           }
/*     */         });
/* 196 */     DispenserBlock.registerBehavior((ItemLike)Items.FLINT_AND_STEEL, new FlintAndSteelDispenseItemBehavior());
/* 198 */     DispenserBlock.registerBehavior((ItemLike)Items.BONE_MEAL, new OptionalDispenseItemBehavior() {
/*     */           protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 201 */             setSuccess(true);
/* 202 */             ServerLevel serverLevel = source.level();
/* 204 */             BlockPos target = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 205 */             if (BoneMealItem.growCrop(dispensed, (Level)serverLevel, target) || BoneMealItem.growWaterPlant(dispensed, (Level)serverLevel, target, null)) {
/* 206 */               if (!serverLevel.isClientSide())
/* 207 */                 serverLevel.levelEvent(1505, target, 15); 
/*     */             } else {
/* 210 */               setSuccess(false);
/*     */             } 
/* 213 */             return dispensed;
/*     */           }
/*     */         });
/* 217 */     DispenserBlock.registerBehavior((ItemLike)Blocks.TNT, new OptionalDispenseItemBehavior() {
/*     */           protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 220 */             ServerLevel level = source.level();
/* 221 */             if (!((Boolean)level.getGameRules().get(GameRules.TNT_EXPLODES)).booleanValue()) {
/* 222 */               setSuccess(false);
/* 223 */               return dispensed;
/*     */             } 
/* 226 */             BlockPos target = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 228 */             if (SulfurCubeBlockDispenseItemBehavior.dispenseBlock(level, target, dispensed))
/* 229 */               return dispensed; 
/* 232 */             PrimedTnt tnt = new PrimedTnt((Level)level, target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D, null);
/* 233 */             level.addFreshEntity((Entity)tnt);
/* 234 */             level.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 235 */             level.gameEvent(null, (Holder)GameEvent.ENTITY_PLACE, target);
/* 237 */             dispensed.shrink(1);
/* 238 */             setSuccess(true);
/* 239 */             return dispensed;
/*     */           }
/*     */         });
/* 243 */     DispenserBlock.registerBehavior((ItemLike)Items.WITHER_SKELETON_SKULL, new OptionalDispenseItemBehavior() {
/*     */           protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 246 */             ServerLevel serverLevel = source.level();
/* 247 */             Direction direction = (Direction)source.state().getValue((Property)DispenserBlock.FACING);
/* 248 */             BlockPos target = source.pos().relative(direction);
/* 250 */             if (serverLevel.isEmptyBlock(target) && WitherSkullBlock.canSpawnMob((Level)serverLevel, target, dispensed)) {
/* 251 */               serverLevel.setBlock(target, (BlockState)Blocks.WITHER_SKELETON_SKULL.defaultBlockState().setValue((Property)SkullBlock.ROTATION, Integer.valueOf(RotationSegment.convertToSegment(direction))), 3);
/* 252 */               serverLevel.gameEvent(null, (Holder)GameEvent.BLOCK_PLACE, target);
/* 253 */               BlockEntity skull = serverLevel.getBlockEntity(target);
/* 254 */               if (skull instanceof SkullBlockEntity) {
/* 254 */                 SkullBlockEntity skullBlockEntity = (SkullBlockEntity)skull;
/* 255 */                 WitherSkullBlock.checkSpawn((Level)serverLevel, target, skullBlockEntity);
/*     */               } 
/* 257 */               dispensed.shrink(1);
/* 258 */               setSuccess(true);
/*     */             } else {
/* 260 */               setSuccess(EquipmentDispenseItemBehavior.dispenseEquipment(source, dispensed));
/*     */             } 
/* 262 */             return dispensed;
/*     */           }
/*     */         });
/* 266 */     DispenserBlock.registerBehavior((ItemLike)Blocks.CARVED_PUMPKIN, new OptionalDispenseItemBehavior() {
/*     */           protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 269 */             ServerLevel serverLevel = source.level();
/* 270 */             BlockPos target = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 271 */             CarvedPumpkinBlock pumpkinBlock = (CarvedPumpkinBlock)Blocks.CARVED_PUMPKIN;
/* 273 */             if (serverLevel.isEmptyBlock(target) && pumpkinBlock.canSpawnGolem((LevelReader)serverLevel, target)) {
/* 274 */               if (!serverLevel.isClientSide()) {
/* 275 */                 serverLevel.setBlock(target, pumpkinBlock.defaultBlockState(), 3);
/* 276 */                 serverLevel.gameEvent(null, (Holder)GameEvent.BLOCK_PLACE, target);
/*     */               } 
/* 278 */               dispensed.shrink(1);
/* 279 */               setSuccess(true);
/*     */             } else {
/* 281 */               setSuccess(EquipmentDispenseItemBehavior.dispenseEquipment(source, dispensed));
/*     */             } 
/* 283 */             return dispensed;
/*     */           }
/*     */         });
/* 287 */     ShulkerBoxDispenseBehavior shulkerBoxDispenseBehavior = new ShulkerBoxDispenseBehavior();
/* 288 */     DispenserBlock.registerBehavior((ItemLike)Items.SHULKER_BOX, shulkerBoxDispenseBehavior);
/* 289 */     Items.DYED_SHULKER_BOX.forEach(item -> DispenserBlock.registerBehavior((ItemLike)item, shulkerBoxDispenseBehavior));
/* 291 */     DispenserBlock.registerBehavior((ItemLike)Items.GLASS_BOTTLE, new OptionalDispenseItemBehavior() {
/*     */           private ItemStack takeLiquid(BlockSource source, ItemStack dispensed, ItemStack filledItemStack) {
/* 293 */             source.level().gameEvent(null, (Holder)GameEvent.FLUID_PICKUP, source.pos());
/* 294 */             return consumeWithRemainder(source, dispensed, filledItemStack);
/*     */           }
/*     */           
/*     */           public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 299 */             setSuccess(false);
/* 300 */             ServerLevel level = source.level();
/* 302 */             BlockPos target = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 304 */             BlockState state = level.getBlockState(target);
/* 306 */             if (state.is(BlockTags.BEEHIVES, s -> (s.hasProperty((Property)BeehiveBlock.HONEY_LEVEL) && s.getBlock() instanceof BeehiveBlock)) && ((Integer)state.getValue((Property)BeehiveBlock.HONEY_LEVEL)).intValue() >= 5) {
/* 307 */               ((BeehiveBlock)state.getBlock()).releaseBeesAndResetHoneyLevel((Level)level, state, target, null, BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED);
/* 308 */               setSuccess(true);
/* 309 */               return takeLiquid(source, dispensed, new ItemStack((ItemLike)Items.HONEY_BOTTLE));
/*     */             } 
/* 310 */             if (level.getFluidState(target).is(FluidTags.WATER)) {
/* 311 */               setSuccess(true);
/* 312 */               return takeLiquid(source, dispensed, PotionContents.createItemStack(Items.POTION, Potions.WATER));
/*     */             } 
/* 314 */             return super.execute(source, dispensed);
/*     */           }
/*     */         });
/* 319 */     DispenserBlock.registerBehavior((ItemLike)Items.GLOWSTONE, new OptionalDispenseItemBehavior() {
/*     */           public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 322 */             Direction direction = (Direction)source.state().getValue((Property)DispenserBlock.FACING);
/* 323 */             BlockPos pos = source.pos().relative(direction);
/* 324 */             ServerLevel serverLevel = source.level();
/* 325 */             BlockState blockState = serverLevel.getBlockState(pos);
/* 326 */             setSuccess(true);
/* 327 */             if (blockState.is(Blocks.RESPAWN_ANCHOR)) {
/* 328 */               if (((Integer)blockState.getValue((Property)RespawnAnchorBlock.CHARGE)).intValue() != 4) {
/* 329 */                 RespawnAnchorBlock.charge(null, (Level)serverLevel, pos, blockState);
/* 330 */                 dispensed.shrink(1);
/*     */               } else {
/* 332 */                 setSuccess(false);
/*     */               } 
/* 335 */               return dispensed;
/*     */             } 
/* 337 */             return super.execute(source, dispensed);
/*     */           }
/*     */         });
/* 342 */     DispenserBlock.registerBehavior((ItemLike)Items.SHEARS, new ShearsDispenseItemBehavior());
/* 344 */     DispenserBlock.registerBehavior((ItemLike)Items.BRUSH, new OptionalDispenseItemBehavior() {
/*     */           protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 347 */             ServerLevel level = source.level();
/* 349 */             BlockPos pos = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 351 */             List<Armadillo> armadillos = level.getEntitiesOfClass(Armadillo.class, new AABB(pos), EntitySelector.NO_SPECTATORS);
/* 352 */             if (armadillos.isEmpty()) {
/* 353 */               setSuccess(false);
/* 354 */               return dispensed;
/*     */             } 
/* 356 */             for (Armadillo armadillo : armadillos) {
/* 357 */               if (armadillo.brushOffScute(null, dispensed)) {
/* 358 */                 dispensed.hurtAndBreak(16, level, null, item -> {
/*     */                     
/*     */                     });
/* 359 */                 return dispensed;
/*     */               } 
/*     */             } 
/* 362 */             setSuccess(false);
/* 363 */             return dispensed;
/*     */           }
/*     */         });
/* 367 */     DispenserBlock.registerBehavior((ItemLike)Items.HONEYCOMB, new OptionalDispenseItemBehavior() {
/*     */           public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 370 */             BlockPos pos = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 371 */             ServerLevel serverLevel = source.level();
/* 372 */             BlockState blockState = serverLevel.getBlockState(pos);
/* 374 */             Optional<BlockState> maybeWaxed = HoneycombItem.getWaxed(blockState);
/* 375 */             if (maybeWaxed.isPresent()) {
/* 376 */               serverLevel.setBlockAndUpdate(pos, maybeWaxed.get());
/* 377 */               serverLevel.levelEvent(3003, pos, 0);
/* 378 */               dispensed.shrink(1);
/* 379 */               setSuccess(true);
/* 381 */               return dispensed;
/*     */             } 
/* 383 */             return super.execute(source, dispensed);
/*     */           }
/*     */         });
/* 387 */     DispenserBlock.registerBehavior((ItemLike)Items.POTION, new DefaultDispenseItemBehavior() {
/* 388 */           private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
/*     */           
/*     */           public ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 392 */             PotionContents potion = (PotionContents)dispensed.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/* 393 */             if (!potion.is(Potions.WATER))
/* 394 */               return this.defaultDispenseItemBehavior.dispense(source, dispensed); 
/* 397 */             ServerLevel level = source.level();
/* 398 */             BlockPos pos = source.pos();
/* 400 */             BlockPos target = source.pos().relative((Direction)source.state().getValue((Property)DispenserBlock.FACING));
/* 401 */             if (level.getBlockState(target).is(BlockTags.CONVERTABLE_TO_MUD)) {
/* 402 */               if (!level.isClientSide()) {
/* 403 */                 RandomSource random = level.getRandom();
/* 404 */                 for (int i = 0; i < 5; i++)
/* 405 */                   level.sendParticles((ParticleOptions)ParticleTypes.SPLASH, pos.getX() + random.nextDouble(), (pos.getY() + 1), pos.getZ() + random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D); 
/*     */               } 
/* 409 */               level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 410 */               level.gameEvent(null, (Holder)GameEvent.FLUID_PLACE, pos);
/* 412 */               level.setBlockAndUpdate(target, Blocks.MUD.defaultBlockState());
/* 414 */               return consumeWithRemainder(source, dispensed, new ItemStack((ItemLike)Items.GLASS_BOTTLE));
/*     */             } 
/* 417 */             return this.defaultDispenseItemBehavior.dispense(source, dispensed);
/*     */           }
/*     */         });
/* 421 */     DispenserBlock.registerBehavior((ItemLike)Items.MINECART, new MinecartDispenseItemBehavior(EntityTypes.MINECART));
/* 422 */     DispenserBlock.registerBehavior((ItemLike)Items.CHEST_MINECART, new MinecartDispenseItemBehavior(EntityTypes.CHEST_MINECART));
/* 423 */     DispenserBlock.registerBehavior((ItemLike)Items.FURNACE_MINECART, new MinecartDispenseItemBehavior(EntityTypes.FURNACE_MINECART));
/* 424 */     DispenserBlock.registerBehavior((ItemLike)Items.TNT_MINECART, new MinecartDispenseItemBehavior(EntityTypes.TNT_MINECART));
/* 425 */     DispenserBlock.registerBehavior((ItemLike)Items.HOPPER_MINECART, new MinecartDispenseItemBehavior(EntityTypes.HOPPER_MINECART));
/* 426 */     DispenserBlock.registerBehavior((ItemLike)Items.COMMAND_BLOCK_MINECART, new MinecartDispenseItemBehavior(EntityTypes.COMMAND_BLOCK_MINECART));
/*     */   }
/*     */   
/*     */   ItemStack dispense(BlockSource paramBlockSource, ItemStack paramItemStack);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\dispenser\DispenseItemBehavior.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */