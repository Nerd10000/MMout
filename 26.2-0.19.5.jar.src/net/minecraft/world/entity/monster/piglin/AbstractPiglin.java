/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public abstract class AbstractPiglin extends Monster {
/*  27 */   protected static final EntityDataAccessor<Boolean> DATA_IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(AbstractPiglin.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   public static final int CONVERSION_TIME = 300;
/*     */   
/*     */   private static final boolean DEFAULT_IMMUNE_TO_ZOMBIFICATION = false;
/*     */   
/*     */   private static final boolean DEFAULT_PICK_UP_LOOT = true;
/*     */   
/*     */   private static final int DEFAULT_TIME_IN_OVERWORLD = 0;
/*     */   
/*  32 */   protected int timeInOverworld = 0;
/*     */   
/*     */   public AbstractPiglin(EntityType<? extends AbstractPiglin> type, Level level) {
/*  35 */     super(type, level);
/*  36 */     setCanPickUpLoot(true);
/*  37 */     applyOpenDoorsAbility();
/*  38 */     setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, 16.0F);
/*  39 */     setPathfindingMalus(PathType.FIRE, -1.0F);
/*     */   }
/*     */   
/*     */   private void applyOpenDoorsAbility() {
/*  43 */     if (GoalUtils.hasGroundPathNavigation((Mob)this))
/*  44 */       getNavigation().setCanOpenDoors(true); 
/*     */   }
/*     */   
/*     */   public void setImmuneToZombification(boolean isImmuneToZombification) {
/*  51 */     getEntityData().set(DATA_IMMUNE_TO_ZOMBIFICATION, Boolean.valueOf(isImmuneToZombification));
/*     */   }
/*     */   
/*     */   protected boolean isImmuneToZombification() {
/*  55 */     return ((Boolean)getEntityData().get(DATA_IMMUNE_TO_ZOMBIFICATION)).booleanValue();
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  60 */     super.defineSynchedData(entityData);
/*  61 */     entityData.define(DATA_IMMUNE_TO_ZOMBIFICATION, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  66 */     super.addAdditionalSaveData(output);
/*  68 */     output.putBoolean("IsImmuneToZombification", isImmuneToZombification());
/*  69 */     output.putInt("TimeInOverworld", this.timeInOverworld);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  74 */     super.readAdditionalSaveData(input);
/*  77 */     setCanPickUpLoot(input.getBooleanOr("CanPickUpLoot", true));
/*  79 */     setImmuneToZombification(input.getBooleanOr("IsImmuneToZombification", false));
/*  80 */     this.timeInOverworld = input.getIntOr("TimeInOverworld", 0);
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/*  85 */     super.customServerAiStep(level);
/*  87 */     if (isConverting()) {
/*  88 */       this.timeInOverworld++;
/*     */     } else {
/*  90 */       this.timeInOverworld = 0;
/*     */     } 
/*  92 */     if (this.timeInOverworld > 300) {
/*  93 */       if (level.getDifficulty() != Difficulty.PEACEFUL)
/*  94 */         playConvertedSound(); 
/*  96 */       finishConversion(level);
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setTimeInOverworld(int timeInOverworld) {
/* 102 */     this.timeInOverworld = timeInOverworld;
/*     */   }
/*     */   
/*     */   public boolean isConverting() {
/* 106 */     return (!isImmuneToZombification() && !isNoAi() && ((Boolean)level().environmentAttributes().getValue(EnvironmentAttributes.PIGLINS_ZOMBIFY, position())).booleanValue());
/*     */   }
/*     */   
/*     */   protected void finishConversion(ServerLevel level) {
/* 110 */     convertTo(EntityTypes.ZOMBIFIED_PIGLIN, 
/*     */         
/* 112 */         ConversionParams.single((Mob)this, true, true), zombified -> zombified.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 0)));
/*     */   }
/*     */   
/*     */   public boolean isAdult() {
/* 118 */     return !isBaby();
/*     */   }
/*     */   
/*     */   public LivingEntity getTarget() {
/* 125 */     return getTargetFromBrain();
/*     */   }
/*     */   
/*     */   protected boolean isHoldingMeleeWeapon() {
/* 129 */     return getMainHandItem().has(DataComponents.TOOL);
/*     */   }
/*     */   
/*     */   public void playAmbientSound() {
/* 134 */     if (PiglinAi.isIdle(this))
/* 135 */       super.playAmbientSound(); 
/*     */   }
/*     */   
/*     */   protected abstract boolean canHunt();
/*     */   
/*     */   public abstract PiglinArmPose getArmPose();
/*     */   
/*     */   protected abstract void playConvertedSound();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\piglin\AbstractPiglin.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */