/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityProcessor;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.animal.bee.Bee;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public final class Occupant extends Record {
/*     */   private final TypedEntityData<EntityType<?>> entityData;
/*     */   
/*     */   private final int ticksInHive;
/*     */   
/*     */   private final int minTicksInHive;
/*     */   
/*     */   public static final Codec<Occupant> CODEC;
/*     */   
/*     */   public Occupant(TypedEntityData<EntityType<?>> entityData, int ticksInHive, int minTicksInHive) {
/* 364 */     this.entityData = entityData;
/* 364 */     this.ticksInHive = ticksInHive;
/* 364 */     this.minTicksInHive = minTicksInHive;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #364	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #364	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #364	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public TypedEntityData<EntityType<?>> entityData() {
/* 364 */     return this.entityData;
/*     */   }
/*     */   
/*     */   public int ticksInHive() {
/* 364 */     return this.ticksInHive;
/*     */   }
/*     */   
/*     */   public int minTicksInHive() {
/* 364 */     return this.minTicksInHive;
/*     */   }
/*     */   
/*     */   static {
/* 369 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)TypedEntityData.codec(EntityType.CODEC).fieldOf("entity_data").forGetter(Occupant::entityData), (App)Codec.INT.fieldOf("ticks_in_hive").forGetter(Occupant::ticksInHive), (App)Codec.INT.fieldOf("min_ticks_in_hive").forGetter(Occupant::minTicksInHive)).apply((Applicative)i, Occupant::new));
/*     */   }
/*     */   
/* 375 */   public static final Codec<List<Occupant>> LIST_CODEC = CODEC.listOf();
/*     */   
/* 377 */   public static final StreamCodec<RegistryFriendlyByteBuf, Occupant> STREAM_CODEC = StreamCodec.composite(
/* 378 */       TypedEntityData.streamCodec(EntityType.STREAM_CODEC), Occupant::entityData, ByteBufCodecs.VAR_INT, Occupant::ticksInHive, ByteBufCodecs.VAR_INT, Occupant::minTicksInHive, Occupant::new);
/*     */   
/*     */   public static Occupant of(Entity entity) {
/* 385 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), BeehiveBlockEntity.LOGGER);
/*     */     try {
/* 386 */       TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, (HolderLookup.Provider)entity.registryAccess());
/* 387 */       entity.save((ValueOutput)output);
/* 388 */       Objects.requireNonNull(output);
/* 388 */       BeehiveBlockEntity.IGNORED_BEE_TAGS.forEach(output::discard);
/* 389 */       CompoundTag entityTag = output.buildResult();
/* 390 */       boolean hasNectar = entityTag.getBooleanOr("HasNectar", false);
/* 391 */       Occupant occupant = new Occupant(TypedEntityData.of(entity.getType(), entityTag), 0, hasNectar ? 2400 : 600);
/* 392 */       reporter.close();
/*     */       return occupant;
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         reporter.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Occupant create(int ticksInHive) {
/* 396 */     return new Occupant(TypedEntityData.of(EntityTypes.BEE, new CompoundTag()), ticksInHive, 600);
/*     */   }
/*     */   
/*     */   public Entity createEntity(Level level, BlockPos hivePos) {
/* 400 */     CompoundTag entityTag = this.entityData.copyTagWithoutId();
/* 402 */     Objects.requireNonNull(entityTag);
/* 402 */     BeehiveBlockEntity.IGNORED_BEE_TAGS.forEach(entityTag::remove);
/* 404 */     Entity entity = EntityType.loadEntityRecursive((EntityType)this.entityData.type(), entityTag, level, EntitySpawnReason.LOAD, EntityProcessor.NOP);
/* 405 */     if (entity == null || !entity.is(EntityTypeTags.BEEHIVE_INHABITORS))
/* 406 */       return null; 
/* 410 */     entity.setNoGravity(true);
/* 412 */     if (entity instanceof Bee) {
/* 412 */       Bee bee = (Bee)entity;
/* 413 */       bee.setHivePos(hivePos);
/* 414 */       setBeeReleaseData(this.ticksInHive, bee);
/*     */     } 
/* 417 */     return entity;
/*     */   }
/*     */   
/*     */   private static void setBeeReleaseData(int ticksInHive, Bee bee) {
/* 421 */     updateBeeAge(ticksInHive, bee);
/* 422 */     bee.setInLoveTime(Math.max(0, bee.getInLoveTime() - ticksInHive));
/*     */   }
/*     */   
/*     */   private static void updateBeeAge(int ticksInHive, Bee bee) {
/* 426 */     if (bee.isAgeLocked())
/*     */       return; 
/* 430 */     int age = bee.getAge();
/* 431 */     if (age < 0) {
/* 432 */       bee.setAge(Math.min(0, age + ticksInHive));
/* 433 */     } else if (age > 0) {
/* 434 */       bee.setAge(Math.max(0, age - ticksInHive));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BeehiveBlockEntity$Occupant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */