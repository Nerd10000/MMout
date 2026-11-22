/*     */ package net.minecraft.world.entity.animal.cow;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SpellParticleOption;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.Shearable;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.SuspiciousEffectHolder;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class MushroomCow extends AbstractCow implements Shearable {
/*  61 */   private static final EntityDataAccessor<Integer> DATA_TYPE = SynchedEntityData.defineId(MushroomCow.class, EntityDataSerializers.INT);
/*     */   
/*     */   private static final int MUTATE_CHANCE = 1024;
/*     */   
/*  63 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.45F, 0.7F)
/*  64 */     .withEyeHeight(0.69F)
/*  65 */     .withAttachments(EntityAttachments.builder()
/*  66 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.75F, 0.0F));
/*     */   
/*     */   private static final String TAG_STEW_EFFECTS = "stew_effects";
/*     */   
/*     */   private SuspiciousStewEffects stewEffects;
/*     */   
/*     */   private UUID lastLightningBoltUUID;
/*     */   
/*     */   public MushroomCow(EntityType<? extends MushroomCow> type, Level level) {
/*  74 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/*  79 */     if (level.getBlockState(pos.below()).is(Blocks.MYCELIUM))
/*  80 */       return 10.0F; 
/*  82 */     return level.getPathfindingCostFromLightLevels(pos);
/*     */   }
/*     */   
/*     */   public static boolean checkMushroomSpawnRules(EntityType<MushroomCow> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/*  86 */     return (level.getBlockState(pos.below()).is(BlockTags.MOOSHROOMS_SPAWNABLE_ON) && 
/*  87 */       isBrightEnoughToSpawn((BlockAndLightGetter)level, pos));
/*     */   }
/*     */   
/*     */   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
/*  92 */     UUID lightningBoltUUID = lightningBolt.getUUID();
/*  93 */     if (!lightningBoltUUID.equals(this.lastLightningBoltUUID)) {
/*  94 */       setVariant((getVariant() == Variant.RED) ? Variant.BROWN : Variant.RED);
/*  95 */       this.lastLightningBoltUUID = lightningBoltUUID;
/*  96 */       playSound(SoundEvents.MOOSHROOM_CONVERT, 2.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 102 */     super.defineSynchedData(entityData);
/* 104 */     entityData.define(DATA_TYPE, Integer.valueOf(Variant.DEFAULT.id));
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 109 */     ItemStack itemStack = player.getItemInHand(hand);
/* 110 */     if (itemStack.is(Items.BOWL) && !isBaby()) {
/*     */       ItemStack stew;
/*     */       SoundEvent milkSound;
/* 112 */       boolean isSuspicious = false;
/* 114 */       if (this.stewEffects != null) {
/* 115 */         isSuspicious = true;
/* 116 */         stew = new ItemStack((ItemLike)Items.SUSPICIOUS_STEW);
/* 117 */         stew.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, this.stewEffects);
/* 118 */         this.stewEffects = null;
/*     */       } else {
/* 120 */         stew = new ItemStack((ItemLike)Items.MUSHROOM_STEW);
/*     */       } 
/* 123 */       ItemStack bowlOrStew = ItemUtils.createFilledResult(itemStack, player, stew, false);
/* 124 */       player.setItemInHand(hand, bowlOrStew);
/* 127 */       if (isSuspicious) {
/* 128 */         milkSound = SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY;
/*     */       } else {
/* 130 */         milkSound = SoundEvents.MOOSHROOM_MILK;
/*     */       } 
/* 133 */       playSound(milkSound, 1.0F, 1.0F);
/* 135 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 138 */     if (itemStack.is(Items.SHEARS) && readyForShearing()) {
/* 139 */       Level level = level();
/* 139 */       if (level instanceof ServerLevel) {
/* 139 */         ServerLevel serverLevel = (ServerLevel)level;
/* 140 */         shear(serverLevel, SoundSource.PLAYERS, itemStack);
/* 141 */         gameEvent((Holder)GameEvent.SHEAR, (Entity)player);
/* 142 */         itemStack.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot());
/*     */       } 
/* 144 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 148 */     if (getVariant() == Variant.BROWN && !isBaby()) {
/* 149 */       Optional<SuspiciousStewEffects> effectsFromItemStack = getEffectsFromItemStack(itemStack);
/* 150 */       if (effectsFromItemStack.isEmpty())
/* 151 */         return super.mobInteract(player, hand); 
/* 153 */       if (this.stewEffects != null) {
/* 154 */         for (int i = 0; i < 2; i++)
/* 155 */           level().addParticle((ParticleOptions)ParticleTypes.SMOKE, getX() + this.random.nextDouble() / 2.0D, getY(0.5D), getZ() + this.random.nextDouble() / 2.0D, 0.0D, this.random.nextDouble() / 5.0D, 0.0D); 
/*     */       } else {
/* 158 */         itemStack.consume(1, (LivingEntity)player);
/* 159 */         SpellParticleOption particle = SpellParticleOption.create(ParticleTypes.EFFECT, -1, 1.0F);
/* 160 */         for (int i = 0; i < 4; i++)
/* 161 */           level().addParticle((ParticleOptions)particle, getX() + this.random.nextDouble() / 2.0D, getY(0.5D), getZ() + this.random.nextDouble() / 2.0D, 0.0D, this.random.nextDouble() / 5.0D, 0.0D); 
/* 163 */         this.stewEffects = effectsFromItemStack.get();
/* 164 */         playSound(SoundEvents.MOOSHROOM_EAT, 2.0F, 1.0F);
/*     */       } 
/* 166 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 169 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   public void shear(ServerLevel level, SoundSource soundSource, ItemStack tool) {
/* 174 */     level.playSound(null, (Entity)this, SoundEvents.MOOSHROOM_SHEAR, soundSource, 1.0F, 1.0F);
/* 176 */     convertTo(EntityTypes.COW, ConversionParams.single((Mob)this, false, false), cow -> {
/*     */           level.sendParticles((ParticleOptions)ParticleTypes.EXPLOSION, getX(), getY(0.5D), getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
/*     */           dropFromShearingLootTable(level, BuiltInLootTables.SHEAR_MOOSHROOM, (ItemInstance)tool, ());
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean readyForShearing() {
/* 189 */     return !isBaby();
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 194 */     super.addAdditionalSaveData(output);
/* 195 */     output.store("Type", Variant.CODEC, getVariant());
/* 197 */     output.storeNullable("stew_effects", SuspiciousStewEffects.CODEC, this.stewEffects);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 202 */     super.readAdditionalSaveData(input);
/* 203 */     setVariant(input.read("Type", Variant.CODEC).orElse(Variant.DEFAULT));
/* 205 */     this.stewEffects = input.read("stew_effects", SuspiciousStewEffects.CODEC).orElse(null);
/*     */   }
/*     */   
/*     */   private Optional<SuspiciousStewEffects> getEffectsFromItemStack(ItemStack itemStack) {
/* 209 */     SuspiciousEffectHolder effectHolder = SuspiciousEffectHolder.tryGet((ItemLike)itemStack.getItem());
/* 210 */     if (effectHolder != null)
/* 211 */       return Optional.of(effectHolder.getSuspiciousEffects()); 
/* 213 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private void setVariant(Variant variant) {
/* 217 */     this.entityData.set(DATA_TYPE, Integer.valueOf(variant.id));
/*     */   }
/*     */   
/*     */   public Variant getVariant() {
/* 221 */     return Variant.byId(((Integer)this.entityData.get(DATA_TYPE)).intValue());
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 226 */     if (type == DataComponents.MOOSHROOM_VARIANT)
/* 227 */       return (T)castComponentValue(type, getVariant()); 
/* 230 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 235 */     applyImplicitComponentIfPresent(components, DataComponents.MOOSHROOM_VARIANT);
/* 236 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 241 */     if (type == DataComponents.MOOSHROOM_VARIANT) {
/* 242 */       setVariant((Variant)castComponentValue(DataComponents.MOOSHROOM_VARIANT, value));
/* 243 */       return true;
/*     */     } 
/* 246 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 251 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public MushroomCow getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 256 */     MushroomCow baby = (MushroomCow)EntityTypes.MOOSHROOM.create((Level)level, EntitySpawnReason.BREEDING);
/* 257 */     if (baby != null)
/* 258 */       baby.setVariant(getOffspringVariant((MushroomCow)partner)); 
/* 260 */     return baby;
/*     */   }
/*     */   
/*     */   private Variant getOffspringVariant(MushroomCow mate) {
/* 264 */     Variant babyVariant, variant = getVariant();
/* 265 */     Variant mateVariant = mate.getVariant();
/* 268 */     if (variant == mateVariant && this.random.nextInt(1024) == 0) {
/* 269 */       babyVariant = (variant == Variant.BROWN) ? Variant.RED : Variant.BROWN;
/*     */     } else {
/* 271 */       babyVariant = this.random.nextBoolean() ? variant : mateVariant;
/*     */     } 
/* 273 */     return babyVariant;
/*     */   }
/*     */   
/*     */   public enum Variant implements StringRepresentable {
/* 277 */     RED("red", 0, (String)Blocks.RED_MUSHROOM.defaultBlockState()),
/* 278 */     BROWN("brown", 1, (String)Blocks.BROWN_MUSHROOM.defaultBlockState());
/*     */     
/* 280 */     public static final Variant DEFAULT = RED;
/*     */     
/* 282 */     public static final Codec<Variant> CODEC = (Codec<Variant>)StringRepresentable.fromEnum(Variant::values);
/*     */     
/* 284 */     private static final IntFunction<Variant> BY_ID = ByIdMap.continuous(Variant::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/*     */     
/* 286 */     public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::id);
/*     */     
/*     */     private final String type;
/*     */     
/*     */     private final int id;
/*     */     
/*     */     private final BlockState blockState;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     Variant(String type, int id, BlockState blockState) {
/* 293 */       this.type = type;
/* 294 */       this.id = id;
/* 295 */       this.blockState = blockState;
/*     */     }
/*     */     
/*     */     public BlockState getBlockState() {
/* 299 */       return this.blockState;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 304 */       return this.type;
/*     */     }
/*     */     
/*     */     private int id() {
/* 308 */       return this.id;
/*     */     }
/*     */     
/*     */     private static Variant byId(int id) {
/* 312 */       return BY_ID.apply(id);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\cow\MushroomCow.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */