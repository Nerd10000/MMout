/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.debug.DebugHiveInfo;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debug.DebugValueSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityProcessor;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.bee.Bee;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.component.Bees;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BeehiveBlock;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class BeehiveBlockEntity extends BlockEntity {
/*  56 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String TAG_FLOWER_POS = "flower_pos";
/*     */   
/*     */   private static final String BEES = "bees";
/*     */   
/*  62 */   private static final List<String> IGNORED_BEE_TAGS = Arrays.asList(new String[] { 
/*  62 */         "Air", "drop_chances", "equipment", "Brain", "CanPickUpLoot", "DeathTime", "fall_distance", "FallFlying", "Fire", "HurtTime", 
/*  62 */         "LeftHanded", "Motion", "NoGravity", "OnGround", "PortalCooldown", "Pos", "Rotation", "sleeping_pos", "CannotEnterHiveTicks", "TicksSincePollination", 
/*  62 */         "CropsGrownSincePollination", "hive_pos", "Passengers", "leash", "UUID" });
/*     */   
/*     */   public static final int MAX_OCCUPANTS = 3;
/*     */   
/*     */   private static final int MIN_TICKS_BEFORE_REENTERING_HIVE = 400;
/*     */   
/*     */   private static final int MIN_OCCUPATION_TICKS_NECTAR = 2400;
/*     */   
/*     */   public static final int MIN_OCCUPATION_TICKS_NECTARLESS = 600;
/*     */   
/*  83 */   private final List<BeeData> stored = Lists.newArrayList();
/*     */   
/*     */   private BlockPos savedFlowerPos;
/*     */   
/*     */   public enum BeeReleaseStatus {
/*  88 */     HONEY_DELIVERED, BEE_RELEASED, EMERGENCY;
/*     */   }
/*     */   
/*     */   public BeehiveBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  94 */     super(BlockEntityTypes.BEEHIVE, worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public void setChanged() {
/*  99 */     if (isFireNearby())
/* 101 */       emptyAllLivingFromHive((Player)null, this.level.getBlockState(getBlockPos()), BeeReleaseStatus.EMERGENCY); 
/* 103 */     super.setChanged();
/*     */   }
/*     */   
/*     */   public boolean isFireNearby() {
/* 107 */     if (this.level == null)
/* 108 */       return false; 
/* 111 */     for (BlockPos pos : BlockPos.betweenClosed(this.worldPosition.offset(-1, -1, -1), this.worldPosition.offset(1, 1, 1))) {
/* 112 */       if (this.level.getBlockState(pos).getBlock() instanceof net.minecraft.world.level.block.FireBlock)
/* 113 */         return true; 
/*     */     } 
/* 117 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 121 */     return this.stored.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean isFull() {
/* 125 */     return (this.stored.size() == 3);
/*     */   }
/*     */   
/*     */   public void emptyAllLivingFromHive(Player player, BlockState state, BeeReleaseStatus releaseReason) {
/* 129 */     List<Entity> releasedFromHive = releaseAllOccupants(state, releaseReason);
/* 131 */     if (player != null)
/* 132 */       for (Entity released : releasedFromHive) {
/* 133 */         if (released instanceof Bee) {
/* 133 */           Bee bee = (Bee)released;
/* 134 */           if (player.position().distanceToSqr(released.position()) <= 16.0D) {
/* 135 */             if (!isSedated()) {
/* 136 */               bee.setTarget((LivingEntity)player);
/*     */               continue;
/*     */             } 
/* 138 */             bee.setStayOutOfHiveCountdown(400);
/*     */           } 
/*     */         } 
/*     */       }  
/*     */   }
/*     */   
/*     */   private List<Entity> releaseAllOccupants(BlockState state, BeeReleaseStatus releaseStatus) {
/* 147 */     List<Entity> spawned = Lists.newArrayList();
/* 148 */     this.stored.removeIf(occupantEntry -> releaseOccupant(this.level, this.worldPosition, state, occupantEntry.toOccupant(), spawned, releaseStatus, this.savedFlowerPos));
/* 149 */     if (!spawned.isEmpty())
/* 150 */       super.setChanged(); 
/* 152 */     return spawned;
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public int getOccupantCount() {
/* 157 */     return this.stored.size();
/*     */   }
/*     */   
/*     */   public static int getHoneyLevel(BlockState blockState) {
/* 161 */     return ((Integer)blockState.getValue((Property)BeehiveBlock.HONEY_LEVEL)).intValue();
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public boolean isSedated() {
/* 166 */     return CampfireBlock.isSmokeyPos(this.level, getBlockPos());
/*     */   }
/*     */   
/*     */   public void addOccupant(Bee bee) {
/* 170 */     if (this.stored.size() >= 3)
/*     */       return; 
/* 175 */     bee.stopRiding();
/* 176 */     bee.ejectPassengers();
/* 177 */     bee.dropLeash();
/* 178 */     storeBee(Occupant.of((Entity)bee));
/* 180 */     if (this.level != null) {
/* 182 */       if (bee.hasSavedFlowerPos() && (!hasSavedFlowerPos() || this.level.getRandom().nextBoolean()))
/* 183 */         this.savedFlowerPos = bee.getSavedFlowerPos(); 
/* 186 */       BlockPos blockPos = getBlockPos();
/* 187 */       this.level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 188 */       this.level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of((Entity)bee, getBlockState()));
/*     */     } 
/* 192 */     bee.discard();
/* 193 */     super.setChanged();
/*     */   }
/*     */   
/*     */   public void storeBee(Occupant occupant) {
/* 197 */     this.stored.add(new BeeData(occupant));
/*     */   }
/*     */   
/*     */   private static boolean releaseOccupant(Level level, BlockPos blockPos, BlockState state, Occupant beeData, List<Entity> spawned, BeeReleaseStatus releaseStatus, BlockPos savedFlowerPos) {
/* 202 */     if (((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.BEES_STAY_IN_HIVE, blockPos)).booleanValue() && releaseStatus != BeeReleaseStatus.EMERGENCY)
/* 203 */       return false; 
/* 206 */     Direction facing = (Direction)state.getValue((Property)BeehiveBlock.FACING);
/* 207 */     BlockPos facingPos = blockPos.relative(facing);
/* 208 */     boolean frontBlocked = !level.getBlockState(facingPos).getCollisionShape((BlockGetter)level, facingPos).isEmpty();
/* 210 */     if (frontBlocked && releaseStatus != BeeReleaseStatus.EMERGENCY)
/* 211 */       return false; 
/* 214 */     Entity entity = beeData.createEntity(level, blockPos);
/* 215 */     if (entity != null) {
/* 216 */       if (entity instanceof Bee) {
/* 216 */         Bee bee = (Bee)entity;
/* 219 */         RandomSource random = level.getRandom();
/* 220 */         if (savedFlowerPos != null && !bee.hasSavedFlowerPos() && random.nextFloat() < 0.9F)
/* 221 */           bee.setSavedFlowerPos(savedFlowerPos); 
/* 224 */         if (releaseStatus == BeeReleaseStatus.HONEY_DELIVERED) {
/* 225 */           bee.dropOffNectar();
/* 227 */           if (state.is(BlockTags.BEEHIVES, s -> s.hasProperty((Property)BeehiveBlock.HONEY_LEVEL))) {
/* 228 */             int honeyLevel = getHoneyLevel(state);
/* 229 */             if (honeyLevel < 5) {
/* 230 */               int levelIncrease = (random.nextInt(100) == 0) ? 2 : 1;
/* 231 */               if (honeyLevel + levelIncrease > 5)
/* 232 */                 levelIncrease--; 
/* 234 */               level.setBlockAndUpdate(blockPos, (BlockState)state.setValue((Property)BeehiveBlock.HONEY_LEVEL, Integer.valueOf(honeyLevel + levelIncrease)));
/*     */             } 
/*     */           } 
/*     */         } 
/* 239 */         if (spawned != null)
/* 240 */           spawned.add(bee); 
/* 243 */         float bbWidth = entity.getBbWidth();
/* 244 */         double delta = frontBlocked ? 0.0D : (0.55D + (bbWidth / 2.0F));
/* 245 */         double spawnX = blockPos.getX() + 0.5D + delta * facing.getStepX();
/* 246 */         double spawnY = blockPos.getY() + 0.5D - (entity.getBbHeight() / 2.0F);
/* 247 */         double spawnZ = blockPos.getZ() + 0.5D + delta * facing.getStepZ();
/* 248 */         entity.snapTo(spawnX, spawnY, spawnZ, entity.getYRot(), entity.getXRot());
/*     */       } 
/* 251 */       level.playSound(null, blockPos, SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 252 */       level.gameEvent((Holder)GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(entity, level.getBlockState(blockPos)));
/* 254 */       return level.addFreshEntity(entity);
/*     */     } 
/* 257 */     return false;
/*     */   }
/*     */   
/*     */   private boolean hasSavedFlowerPos() {
/* 261 */     return (this.savedFlowerPos != null);
/*     */   }
/*     */   
/*     */   private static void tickOccupants(Level level, BlockPos pos, BlockState state, List<BeeData> stored, BlockPos savedFlowerPos) {
/* 265 */     boolean changed = false;
/* 266 */     Iterator<BeeData> iterator = stored.iterator();
/* 267 */     while (iterator.hasNext()) {
/* 268 */       BeeData data = iterator.next();
/* 269 */       if (data.tick()) {
/* 271 */         BeeReleaseStatus releaseStatus = data.hasNectar() ? BeeReleaseStatus.HONEY_DELIVERED : BeeReleaseStatus.BEE_RELEASED;
/* 272 */         if (releaseOccupant(level, pos, state, data.toOccupant(), (List<Entity>)null, releaseStatus, savedFlowerPos)) {
/* 273 */           changed = true;
/* 274 */           iterator.remove();
/*     */         } 
/*     */       } 
/*     */     } 
/* 278 */     if (changed)
/* 279 */       setChanged(level, pos, state); 
/*     */   }
/*     */   
/*     */   public static void serverTick(Level level, BlockPos blockPos, BlockState state, BeehiveBlockEntity entity) {
/* 284 */     tickOccupants(level, blockPos, state, entity.stored, entity.savedFlowerPos);
/* 286 */     if (!entity.stored.isEmpty() && level.getRandom().nextDouble() < 0.005D) {
/* 287 */       double x = blockPos.getX() + 0.5D;
/* 288 */       double y = blockPos.getY();
/* 289 */       double z = blockPos.getZ() + 0.5D;
/* 290 */       level.playSound(null, x, y, z, SoundEvents.BEEHIVE_WORK, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 296 */     super.loadAdditional(input);
/* 298 */     this.stored.clear();
/* 299 */     ((List)input.read("bees", Occupant.LIST_CODEC).orElse(List.of()))
/* 300 */       .forEach(this::storeBee);
/* 302 */     this.savedFlowerPos = input.read("flower_pos", BlockPos.CODEC).orElse(null);
/*     */   }
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 307 */     super.saveAdditional(output);
/* 309 */     output.store("bees", Occupant.LIST_CODEC, getBees());
/* 310 */     output.storeNullable("flower_pos", BlockPos.CODEC, this.savedFlowerPos);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 315 */     super.applyImplicitComponents(components);
/* 316 */     this.stored.clear();
/* 317 */     List<Occupant> bees = ((Bees)components.getOrDefault(DataComponents.BEES, Bees.EMPTY)).bees();
/* 318 */     bees.forEach(this::storeBee);
/*     */   }
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/* 323 */     super.collectImplicitComponents(components);
/* 324 */     components.set(DataComponents.BEES, new Bees(getBees()));
/*     */   }
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput output) {
/* 329 */     super.removeComponentsFromTag(output);
/* 330 */     output.discard("bees");
/*     */   }
/*     */   
/*     */   private List<Occupant> getBees() {
/* 334 */     return this.stored.stream().map(BeeData::toOccupant).toList();
/*     */   }
/*     */   
/*     */   public void registerDebugValues(ServerLevel level, DebugValueSource.Registration registration) {
/* 339 */     registration.register(DebugSubscriptions.BEE_HIVES, () -> DebugHiveInfo.pack(this));
/*     */   }
/*     */   
/*     */   private static class BeeData {
/*     */     private final BeehiveBlockEntity.Occupant occupant;
/*     */     
/*     */     private int ticksInHive;
/*     */     
/*     */     private BeeData(BeehiveBlockEntity.Occupant occupant) {
/* 347 */       this.occupant = occupant;
/* 348 */       this.ticksInHive = occupant.ticksInHive();
/*     */     }
/*     */     
/*     */     public boolean tick() {
/* 352 */       return (this.ticksInHive++ > this.occupant.minTicksInHive);
/*     */     }
/*     */     
/*     */     public BeehiveBlockEntity.Occupant toOccupant() {
/* 356 */       return new BeehiveBlockEntity.Occupant(this.occupant.entityData, this.ticksInHive, this.occupant.minTicksInHive);
/*     */     }
/*     */     
/*     */     public boolean hasNectar() {
/* 360 */       return this.occupant.entityData.getUnsafe().getBooleanOr("HasNectar", false);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Occupant extends Record {
/*     */     private final TypedEntityData<EntityType<?>> entityData;
/*     */     
/*     */     private final int ticksInHive;
/*     */     
/*     */     private final int minTicksInHive;
/*     */     
/*     */     public static final Codec<Occupant> CODEC;
/*     */     
/*     */     public Occupant(TypedEntityData<EntityType<?>> entityData, int ticksInHive, int minTicksInHive) {
/* 364 */       this.entityData = entityData;
/* 364 */       this.ticksInHive = ticksInHive;
/* 364 */       this.minTicksInHive = minTicksInHive;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #364	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #364	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #364	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public TypedEntityData<EntityType<?>> entityData() {
/* 364 */       return this.entityData;
/*     */     }
/*     */     
/*     */     public int ticksInHive() {
/* 364 */       return this.ticksInHive;
/*     */     }
/*     */     
/*     */     public int minTicksInHive() {
/* 364 */       return this.minTicksInHive;
/*     */     }
/*     */     
/*     */     static {
/* 369 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)TypedEntityData.codec(EntityType.CODEC).fieldOf("entity_data").forGetter(Occupant::entityData), (App)Codec.INT.fieldOf("ticks_in_hive").forGetter(Occupant::ticksInHive), (App)Codec.INT.fieldOf("min_ticks_in_hive").forGetter(Occupant::minTicksInHive)).apply((Applicative)i, Occupant::new));
/*     */     }
/*     */     
/* 375 */     public static final Codec<List<Occupant>> LIST_CODEC = CODEC.listOf();
/*     */     
/* 377 */     public static final StreamCodec<RegistryFriendlyByteBuf, Occupant> STREAM_CODEC = StreamCodec.composite(
/* 378 */         TypedEntityData.streamCodec(EntityType.STREAM_CODEC), Occupant::entityData, ByteBufCodecs.VAR_INT, Occupant::ticksInHive, ByteBufCodecs.VAR_INT, Occupant::minTicksInHive, Occupant::new);
/*     */     
/*     */     public static Occupant of(Entity entity) {
/* 385 */       ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), BeehiveBlockEntity.LOGGER);
/*     */       try {
/* 386 */         TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, (HolderLookup.Provider)entity.registryAccess());
/* 387 */         entity.save((ValueOutput)output);
/* 388 */         Objects.requireNonNull(output);
/* 388 */         BeehiveBlockEntity.IGNORED_BEE_TAGS.forEach(output::discard);
/* 389 */         CompoundTag entityTag = output.buildResult();
/* 390 */         boolean hasNectar = entityTag.getBooleanOr("HasNectar", false);
/* 391 */         Occupant occupant = new Occupant(TypedEntityData.of(entity.getType(), entityTag), 0, hasNectar ? 2400 : 600);
/* 392 */         reporter.close();
/*     */         return occupant;
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           reporter.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/*     */     }
/*     */     
/*     */     public static Occupant create(int ticksInHive) {
/* 396 */       return new Occupant(TypedEntityData.of(EntityTypes.BEE, new CompoundTag()), ticksInHive, 600);
/*     */     }
/*     */     
/*     */     public Entity createEntity(Level level, BlockPos hivePos) {
/* 400 */       CompoundTag entityTag = this.entityData.copyTagWithoutId();
/* 402 */       Objects.requireNonNull(entityTag);
/* 402 */       BeehiveBlockEntity.IGNORED_BEE_TAGS.forEach(entityTag::remove);
/* 404 */       Entity entity = EntityType.loadEntityRecursive((EntityType)this.entityData.type(), entityTag, level, EntitySpawnReason.LOAD, EntityProcessor.NOP);
/* 405 */       if (entity == null || !entity.is(EntityTypeTags.BEEHIVE_INHABITORS))
/* 406 */         return null; 
/* 410 */       entity.setNoGravity(true);
/* 412 */       if (entity instanceof Bee) {
/* 412 */         Bee bee = (Bee)entity;
/* 413 */         bee.setHivePos(hivePos);
/* 414 */         setBeeReleaseData(this.ticksInHive, bee);
/*     */       } 
/* 417 */       return entity;
/*     */     }
/*     */     
/*     */     private static void setBeeReleaseData(int ticksInHive, Bee bee) {
/* 421 */       updateBeeAge(ticksInHive, bee);
/* 422 */       bee.setInLoveTime(Math.max(0, bee.getInLoveTime() - ticksInHive));
/*     */     }
/*     */     
/*     */     private static void updateBeeAge(int ticksInHive, Bee bee) {
/* 426 */       if (bee.isAgeLocked())
/*     */         return; 
/* 430 */       int age = bee.getAge();
/* 431 */       if (age < 0) {
/* 432 */         bee.setAge(Math.min(0, age + ticksInHive));
/* 433 */       } else if (age > 0) {
/* 434 */         bee.setAge(Math.max(0, age - ticksInHive));
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BeehiveBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */