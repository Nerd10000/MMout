/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.server.players.OldUsersConverter;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.entity.UUIDLookup;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ 
/*     */ public final class EntityReference<StoredEntityType extends UniquelyIdentifyable> {
/*  22 */   private static final Codec<? extends EntityReference<?>> CODEC = UUIDUtil.CODEC.xmap(EntityReference::new, EntityReference::getUUID);
/*     */   
/*  23 */   private static final StreamCodec<ByteBuf, ? extends EntityReference<?>> STREAM_CODEC = UUIDUtil.STREAM_CODEC.map(EntityReference::new, EntityReference::getUUID);
/*     */   
/*     */   private Either<UUID, StoredEntityType> entity;
/*     */   
/*     */   public static <Type extends UniquelyIdentifyable> Codec<EntityReference<Type>> codec() {
/*  27 */     return (Codec)CODEC;
/*     */   }
/*     */   
/*     */   public static <Type extends UniquelyIdentifyable> StreamCodec<ByteBuf, EntityReference<Type>> streamCodec() {
/*  32 */     return (StreamCodec)STREAM_CODEC;
/*     */   }
/*     */   
/*     */   private EntityReference(StoredEntityType entity) {
/*  38 */     this.entity = Either.right(entity);
/*     */   }
/*     */   
/*     */   private EntityReference(UUID uuid) {
/*  42 */     this.entity = Either.left(uuid);
/*     */   }
/*     */   
/*     */   @Contract("!null -> !null; null -> null")
/*     */   public static <T extends UniquelyIdentifyable> EntityReference<T> of(T entity) {
/*  47 */     return (entity != null) ? new EntityReference<>(entity) : null;
/*     */   }
/*     */   
/*     */   public static <T extends UniquelyIdentifyable> EntityReference<T> of(UUID uuid) {
/*  51 */     return new EntityReference<>(uuid);
/*     */   }
/*     */   
/*     */   public UUID getUUID() {
/*  55 */     return (UUID)this.entity.map(uuid -> uuid, UniquelyIdentifyable::getUUID);
/*     */   }
/*     */   
/*     */   public StoredEntityType getEntity(UUIDLookup<? extends UniquelyIdentifyable> lookup, Class<StoredEntityType> clazz) {
/*  59 */     Optional<StoredEntityType> stored = this.entity.right();
/*  60 */     if (stored.isPresent()) {
/*  61 */       UniquelyIdentifyable uniquelyIdentifyable = (UniquelyIdentifyable)stored.get();
/*  62 */       if (uniquelyIdentifyable.isRemoved()) {
/*  64 */         this.entity = Either.left(uniquelyIdentifyable.getUUID());
/*     */       } else {
/*  66 */         return (StoredEntityType)uniquelyIdentifyable;
/*     */       } 
/*     */     } 
/*  70 */     Optional<UUID> uuid = this.entity.left();
/*  71 */     if (uuid.isPresent()) {
/*  72 */       StoredEntityType resolved = resolve(lookup.lookup(uuid.get()), clazz);
/*  73 */       if (resolved != null && !resolved.isRemoved()) {
/*  74 */         this.entity = Either.right(resolved);
/*  75 */         return resolved;
/*     */       } 
/*     */     } 
/*  78 */     return null;
/*     */   }
/*     */   
/*     */   public StoredEntityType getEntity(Level level, Class<StoredEntityType> clazz) {
/*  82 */     if (Player.class.isAssignableFrom(clazz)) {
/*  83 */       Objects.requireNonNull(level);
/*  83 */       return getEntity(level::getPlayerInAnyDimension, clazz);
/*     */     } 
/*  85 */     Objects.requireNonNull(level);
/*  85 */     return getEntity(level::getEntityInAnyDimension, clazz);
/*     */   }
/*     */   
/*     */   private StoredEntityType resolve(UniquelyIdentifyable entity, Class<StoredEntityType> clazz) {
/*  89 */     if (entity != null && clazz.isAssignableFrom(entity.getClass()))
/*  90 */       return clazz.cast(entity); 
/*  92 */     return null;
/*     */   }
/*     */   
/*     */   public boolean matches(StoredEntityType entity) {
/*  96 */     return getUUID().equals(entity.getUUID());
/*     */   }
/*     */   
/*     */   public void store(ValueOutput output, String key) {
/* 100 */     output.store(key, UUIDUtil.CODEC, getUUID());
/*     */   }
/*     */   
/*     */   public static void store(EntityReference<?> reference, ValueOutput output, String key) {
/* 104 */     if (reference != null)
/* 105 */       reference.store(output, key); 
/*     */   }
/*     */   
/*     */   public static <StoredEntityType extends UniquelyIdentifyable> StoredEntityType get(EntityReference<StoredEntityType> reference, Level level, Class<StoredEntityType> clazz) {
/* 110 */     return (reference != null) ? reference.getEntity(level, clazz) : null;
/*     */   }
/*     */   
/*     */   public static Entity getEntity(EntityReference<Entity> reference, Level level) {
/* 114 */     return get(reference, level, Entity.class);
/*     */   }
/*     */   
/*     */   public static LivingEntity getLivingEntity(EntityReference<LivingEntity> reference, Level level) {
/* 118 */     return get(reference, level, LivingEntity.class);
/*     */   }
/*     */   
/*     */   public static Player getPlayer(EntityReference<Player> reference, Level level) {
/* 122 */     return get(reference, level, Player.class);
/*     */   }
/*     */   
/*     */   public static <StoredEntityType extends UniquelyIdentifyable> EntityReference<StoredEntityType> read(ValueInput input, String key) {
/* 126 */     return input.read(key, codec()).orElse(null);
/*     */   }
/*     */   
/*     */   public static <StoredEntityType extends UniquelyIdentifyable> EntityReference<StoredEntityType> readWithOldOwnerConversion(ValueInput input, String key, Level level) {
/* 130 */     Optional<UUID> uuid = input.read(key, UUIDUtil.CODEC);
/* 131 */     if (uuid.isPresent())
/* 132 */       return of(uuid.get()); 
/* 134 */     return input.getString(key)
/* 135 */       .map(oldName -> OldUsersConverter.convertMobOwnerIfNecessary(level.getServer(), oldName))
/* 136 */       .map(EntityReference::new)
/* 137 */       .orElse(null);
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 143 */     if (obj == this)
/* 144 */       return true; 
/* 146 */     if (obj instanceof EntityReference) {
/* 146 */       EntityReference<?> reference = (EntityReference)obj;
/* 146 */       if (getUUID().equals(reference.getUUID()));
/*     */     } 
/* 146 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 151 */     return getUUID().hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\EntityReference.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */