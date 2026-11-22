/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.animal.camel.CamelHusk;
/*     */ import net.minecraft.world.entity.monster.skeleton.Parched;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ 
/*     */ public class Husk extends Zombie {
/*  34 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.49F, 0.98F)
/*  35 */     .withEyeHeight(0.825F)
/*  36 */     .withAttachments(EntityAttachments.builder()
/*  37 */       .attach(EntityAttachment.VEHICLE, 0.0F, 0.1875F, 0.0F));
/*     */   
/*     */   public Husk(EntityType<? extends Husk> type, Level level) {
/*  40 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   protected boolean isSunSensitive() {
/*  45 */     return false;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  50 */     return SoundEvents.HUSK_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/*  55 */     return SoundEvents.HUSK_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  60 */     return SoundEvents.HUSK_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getStepSound() {
/*  65 */     return SoundEvents.HUSK_STEP;
/*     */   }
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/*  70 */     boolean result = super.doHurtTarget(level, target);
/*  71 */     if (result && getMainHandItem().isEmpty() && target instanceof LivingEntity) {
/*  71 */       LivingEntity livingEntity = (LivingEntity)target;
/*  72 */       float difficulty = level.getCurrentDifficultyAt(blockPosition()).getEffectiveDifficulty();
/*  73 */       livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 140 * (int)difficulty), (Entity)this);
/*     */     } 
/*  76 */     return result;
/*     */   }
/*     */   
/*     */   protected boolean convertsInWater() {
/*  81 */     return true;
/*     */   }
/*     */   
/*     */   protected void doUnderWaterConversion(ServerLevel level) {
/*  86 */     convertToZombieType(level, EntityTypes.ZOMBIE);
/*  87 */     if (!isSilent())
/*  88 */       level.levelEvent(null, 1041, blockPosition(), 0); 
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*  94 */     RandomSource random = level.getRandom();
/*  96 */     groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*  97 */     float difficultyModifier = difficulty.getSpecialMultiplier();
/*  99 */     if (spawnReason != EntitySpawnReason.CONVERSION)
/* 100 */       setCanPickUpLoot((random.nextFloat() < 0.55F * difficultyModifier)); 
/* 103 */     if (groupData != null) {
/* 104 */       groupData = new HuskGroupData((Zombie.ZombieGroupData)groupData);
/* 105 */       ((HuskGroupData)groupData).triedToSpawnCamelHusk = (spawnReason != EntitySpawnReason.NATURAL);
/*     */     } 
/* 107 */     if (groupData instanceof HuskGroupData) {
/* 107 */       HuskGroupData huskGroupData = (HuskGroupData)groupData;
/* 108 */       if (!huskGroupData.triedToSpawnCamelHusk) {
/* 110 */         BlockPos pos = blockPosition();
/* 111 */         if (level.noCollision(EntityTypes.CAMEL_HUSK.getSpawnAABB(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D))) {
/* 113 */           huskGroupData.triedToSpawnCamelHusk = true;
/* 114 */           if (random.nextFloat() < 0.1F) {
/* 116 */             setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.IRON_SPEAR));
/* 118 */             CamelHusk camelHusk = (CamelHusk)EntityTypes.CAMEL_HUSK.create(level(), EntitySpawnReason.NATURAL);
/* 119 */             if (camelHusk != null) {
/* 120 */               camelHusk.setPos(getX(), getY(), getZ());
/* 121 */               camelHusk.finalizeSpawn(level, difficulty, spawnReason, null);
/* 122 */               startRiding((Entity)camelHusk, true, true);
/* 123 */               level.addFreshEntity((Entity)camelHusk);
/* 125 */               Parched parched = (Parched)EntityTypes.PARCHED.create(level(), EntitySpawnReason.NATURAL);
/* 126 */               if (parched != null) {
/* 127 */                 parched.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/* 128 */                 parched.finalizeSpawn(level, difficulty, spawnReason, null);
/* 129 */                 parched.startRiding((Entity)camelHusk, false, false);
/* 130 */                 level.addFreshEntityWithPassengers((Entity)parched);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 137 */     return groupData;
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 142 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public static class HuskGroupData extends Zombie.ZombieGroupData {
/*     */     public boolean triedToSpawnCamelHusk = false;
/*     */     
/*     */     public HuskGroupData(Zombie.ZombieGroupData groupData) {
/* 149 */       super(groupData.isBaby, groupData.canSpawnJockey);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\zombie\Husk.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */