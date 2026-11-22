/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Bucketable;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Salmon extends AbstractSchoolingFish {
/*     */   private static final String TAG_TYPE = "type";
/*     */   
/*  39 */   private static final EntityDataAccessor<Integer> DATA_TYPE = SynchedEntityData.defineId(Salmon.class, EntityDataSerializers.INT);
/*     */   
/*     */   public Salmon(EntityType<? extends Salmon> type, Level level) {
/*  42 */     super((EntityType)type, level);
/*  44 */     refreshDimensions();
/*     */   }
/*     */   
/*     */   public int getMaxSchoolSize() {
/*  51 */     return 5;
/*     */   }
/*     */   
/*     */   public ItemStack getBucketItemStack() {
/*  56 */     return new ItemStack((ItemLike)Items.SALMON_BUCKET);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  61 */     return SoundEvents.SALMON_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  66 */     return SoundEvents.SALMON_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/*  71 */     return SoundEvents.SALMON_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getFlopSound() {
/*  76 */     return SoundEvents.SALMON_FLOP;
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  81 */     super.defineSynchedData(entityData);
/*  83 */     entityData.define(DATA_TYPE, Integer.valueOf(Variant.DEFAULT.id()));
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/*  88 */     super.onSyncedDataUpdated(accessor);
/*  89 */     if (DATA_TYPE.equals(accessor))
/*  90 */       refreshDimensions(); 
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  96 */     super.addAdditionalSaveData(output);
/*  97 */     output.store("type", Variant.CODEC, getVariant());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 102 */     super.readAdditionalSaveData(input);
/* 103 */     setVariant(input.read("type", Variant.CODEC).orElse(Variant.DEFAULT));
/*     */   }
/*     */   
/*     */   public void saveToBucketTag(ItemStack bucket) {
/* 108 */     Bucketable.saveDefaultDataToBucketTag((Mob)this, bucket);
/* 109 */     bucket.copyFrom(DataComponents.SALMON_SIZE, (DataComponentGetter)this);
/*     */   }
/*     */   
/*     */   private void setVariant(Variant variant) {
/* 113 */     this.entityData.set(DATA_TYPE, Integer.valueOf(variant.id));
/*     */   }
/*     */   
/*     */   public Variant getVariant() {
/* 117 */     return Variant.BY_ID.apply(((Integer)this.entityData.get(DATA_TYPE)).intValue());
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 122 */     if (type == DataComponents.SALMON_SIZE)
/* 123 */       return (T)castComponentValue(type, getVariant()); 
/* 126 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 131 */     applyImplicitComponentIfPresent(components, DataComponents.SALMON_SIZE);
/* 132 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 137 */     if (type == DataComponents.SALMON_SIZE) {
/* 138 */       setVariant((Variant)castComponentValue(DataComponents.SALMON_SIZE, value));
/* 139 */       return true;
/*     */     } 
/* 142 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 147 */     WeightedList.Builder<Variant> builder = WeightedList.builder();
/* 148 */     builder.add(Variant.SMALL, 30);
/* 149 */     builder.add(Variant.MEDIUM, 50);
/* 150 */     builder.add(Variant.LARGE, 15);
/* 151 */     builder.build().getRandom(this.random).ifPresent(this::setVariant);
/* 153 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public float getSalmonScale() {
/* 157 */     return (getVariant()).boundingBoxScale;
/*     */   }
/*     */   
/*     */   protected EntityDimensions getDefaultDimensions(Pose pose) {
/* 162 */     return super.getDefaultDimensions(pose).scale(getSalmonScale());
/*     */   }
/*     */   
/*     */   public enum Variant implements StringRepresentable {
/* 166 */     SMALL("small", 0, 0.5F),
/* 167 */     MEDIUM("medium", 1, 1.0F),
/* 168 */     LARGE("large", 2, 1.5F);
/*     */     
/* 170 */     public static final Variant DEFAULT = MEDIUM;
/*     */     
/* 172 */     public static final Codec<Variant> CODEC = (Codec<Variant>)StringRepresentable.fromEnum(Variant::values);
/*     */     
/* 174 */     private static final IntFunction<Variant> BY_ID = ByIdMap.continuous(Variant::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/*     */     
/* 176 */     public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::id);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final int id;
/*     */     
/*     */     private final float boundingBoxScale;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     Variant(String name, int id, float boundingBoxScale) {
/* 183 */       this.name = name;
/* 184 */       this.id = id;
/* 185 */       this.boundingBoxScale = boundingBoxScale;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 190 */       return this.name;
/*     */     }
/*     */     
/*     */     private int id() {
/* 194 */       return this.id;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fish\Salmon.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */