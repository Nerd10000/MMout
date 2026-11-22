/*     */ package net.minecraft.world.entity.monster.skeleton;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Skeleton extends AbstractSkeleton {
/*     */   private static final int TOTAL_CONVERSION_TIME = 300;
/*     */   
/*  22 */   private static final EntityDataAccessor<Boolean> DATA_STRAY_CONVERSION_ID = SynchedEntityData.defineId(Skeleton.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   public static final String CONVERSION_TAG = "StrayConversionTime";
/*     */   
/*     */   private static final int NOT_CONVERTING = -1;
/*     */   
/*     */   private int inPowderSnowTime;
/*     */   
/*     */   private int conversionTime;
/*     */   
/*     */   public Skeleton(EntityType<? extends Skeleton> type, Level level) {
/*  30 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  35 */     super.defineSynchedData(entityData);
/*  37 */     entityData.define(DATA_STRAY_CONVERSION_ID, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean isFreezeConverting() {
/*  41 */     return ((Boolean)getEntityData().get(DATA_STRAY_CONVERSION_ID)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setFreezeConverting(boolean isConverting) {
/*  45 */     this.entityData.set(DATA_STRAY_CONVERSION_ID, Boolean.valueOf(isConverting));
/*     */   }
/*     */   
/*     */   public boolean isShaking() {
/*  50 */     return isFreezeConverting();
/*     */   }
/*     */   
/*     */   public void tick() {
/*  55 */     if (!level().isClientSide() && isAlive() && !isNoAi())
/*  56 */       if (this.isInPowderSnow) {
/*  57 */         if (isFreezeConverting()) {
/*  58 */           this.conversionTime--;
/*  60 */           if (this.conversionTime < 0)
/*  61 */             doFreezeConversion(); 
/*     */         } else {
/*  64 */           this.inPowderSnowTime++;
/*  66 */           if (this.inPowderSnowTime >= 140)
/*  67 */             startFreezeConversion(300); 
/*     */         } 
/*     */       } else {
/*  71 */         this.inPowderSnowTime = -1;
/*  72 */         setFreezeConverting(false);
/*     */       }  
/*  76 */     super.tick();
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  81 */     super.addAdditionalSaveData(output);
/*  83 */     output.putInt("StrayConversionTime", isFreezeConverting() ? this.conversionTime : -1);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  88 */     super.readAdditionalSaveData(input);
/*  90 */     int conversionTime = input.getIntOr("StrayConversionTime", -1);
/*  91 */     if (conversionTime != -1) {
/*  92 */       startFreezeConversion(conversionTime);
/*     */     } else {
/*  94 */       setFreezeConverting(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void startFreezeConversion(int time) {
/* 100 */     this.conversionTime = time;
/* 101 */     setFreezeConverting(true);
/*     */   }
/*     */   
/*     */   protected void doFreezeConversion() {
/* 106 */     convertTo(EntityTypes.STRAY, ConversionParams.single((Mob)this, true, true), stray -> {
/*     */           if (!isSilent())
/*     */             level().levelEvent(null, 1048, blockPosition(), 0); 
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean canFreeze() {
/* 116 */     return false;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 121 */     return SoundEvents.SKELETON_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 126 */     return SoundEvents.SKELETON_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 131 */     return SoundEvents.SKELETON_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getStepSound() {
/* 136 */     return SoundEvents.SKELETON_STEP;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\skeleton\Skeleton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */