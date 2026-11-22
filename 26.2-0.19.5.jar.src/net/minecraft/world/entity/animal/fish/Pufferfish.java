/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Pufferfish extends AbstractFish {
/*  33 */   private static final EntityDataAccessor<Integer> PUFF_STATE = SynchedEntityData.defineId(Pufferfish.class, EntityDataSerializers.INT);
/*     */   
/*     */   private int inflateCounter;
/*     */   
/*     */   private int deflateTimer;
/*     */   
/*     */   private static final TargetingConditions.Selector SCARY_MOB;
/*     */   
/*     */   static {
/*  38 */     SCARY_MOB = ((target, level) -> {
/*     */         if (target instanceof Player) {
/*     */           Player player = (Player)target;
/*     */           if (player.isCreative())
/*     */             return false; 
/*     */         } 
/*     */         return !target.is(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH);
/*     */       });
/*     */   }
/*     */   
/*  45 */   private static final TargetingConditions TARGETING_CONDITIONS = TargetingConditions.forNonCombat().ignoreInvisibilityTesting().ignoreLineOfSight().selector(SCARY_MOB);
/*     */   
/*     */   public static final int STATE_SMALL = 0;
/*     */   
/*     */   public static final int STATE_MID = 1;
/*     */   
/*     */   public static final int STATE_FULL = 2;
/*     */   
/*     */   private static final int DEFAULT_PUFF_STATE = 0;
/*     */   
/*     */   public Pufferfish(EntityType<? extends Pufferfish> type, Level level) {
/*  53 */     super((EntityType)type, level);
/*  55 */     refreshDimensions();
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  60 */     super.defineSynchedData(entityData);
/*  62 */     entityData.define(PUFF_STATE, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   public int getPuffState() {
/*  66 */     return ((Integer)this.entityData.get(PUFF_STATE)).intValue();
/*     */   }
/*     */   
/*     */   public void setPuffState(int state) {
/*  70 */     this.entityData.set(PUFF_STATE, Integer.valueOf(state));
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/*  75 */     if (PUFF_STATE.equals(accessor))
/*  76 */       refreshDimensions(); 
/*  79 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  84 */     super.addAdditionalSaveData(output);
/*  86 */     output.putInt("PuffState", getPuffState());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  91 */     super.readAdditionalSaveData(input);
/*  93 */     setPuffState(Math.min(input.getIntOr("PuffState", 0), 2));
/*     */   }
/*     */   
/*     */   public ItemStack getBucketItemStack() {
/*  98 */     return new ItemStack((ItemLike)Items.PUFFERFISH_BUCKET);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/* 103 */     super.registerGoals();
/* 105 */     this.goalSelector.addGoal(1, new PufferfishPuffGoal(this));
/*     */   }
/*     */   
/*     */   public void tick() {
/* 110 */     if (!level().isClientSide() && isAlive() && isEffectiveAi())
/* 111 */       if (this.inflateCounter > 0) {
/* 113 */         if (getPuffState() == 0) {
/* 114 */           makeSound(SoundEvents.PUFFER_FISH_BLOW_UP);
/* 115 */           setPuffState(1);
/* 117 */         } else if (this.inflateCounter > 40 && getPuffState() == 1) {
/* 118 */           makeSound(SoundEvents.PUFFER_FISH_BLOW_UP);
/* 119 */           setPuffState(2);
/*     */         } 
/* 123 */         this.inflateCounter++;
/* 124 */       } else if (getPuffState() != 0) {
/* 126 */         if (this.deflateTimer > 60 && getPuffState() == 2) {
/* 127 */           makeSound(SoundEvents.PUFFER_FISH_BLOW_OUT);
/* 128 */           setPuffState(1);
/* 129 */         } else if (this.deflateTimer > 100 && getPuffState() == 1) {
/* 130 */           makeSound(SoundEvents.PUFFER_FISH_BLOW_OUT);
/* 131 */           setPuffState(0);
/*     */         } 
/* 134 */         this.deflateTimer++;
/*     */       }  
/* 137 */     super.tick();
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 142 */     super.aiStep();
/* 144 */     Level level = level();
/* 144 */     if (level instanceof ServerLevel) {
/* 144 */       ServerLevel serverLevel = (ServerLevel)level;
/* 144 */       if (isAlive() && getPuffState() > 0) {
/* 145 */         List<Mob> mobs = level().getEntitiesOfClass(Mob.class, getBoundingBox().inflate(0.3D), target -> TARGETING_CONDITIONS.test(level, (LivingEntity)this, (LivingEntity)target));
/* 146 */         for (Mob mob : mobs) {
/* 147 */           if (mob.isAlive())
/* 148 */             touch(serverLevel, mob); 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void touch(ServerLevel level, Mob mob) {
/* 155 */     int puffState = getPuffState();
/* 156 */     if (mob.hurtServer(level, damageSources().mobAttack((LivingEntity)this), (1 + puffState))) {
/* 157 */       mob.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * puffState, 0), (Entity)this);
/* 158 */       playSound(SoundEvents.PUFFER_FISH_STING, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void playerTouch(Player player) {
/* 164 */     int puffState = getPuffState();
/* 165 */     if (player instanceof ServerPlayer) {
/* 165 */       ServerPlayer serverPlayer = (ServerPlayer)player;
/* 165 */       if (puffState > 0 && 
/* 166 */         player.hurtServer(serverPlayer.level(), damageSources().mobAttack((LivingEntity)this), (1 + puffState))) {
/* 167 */         if (!isSilent())
/* 168 */           serverPlayer.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.PUFFER_FISH_STING, 0.0F)); 
/* 170 */         player.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * puffState, 0), (Entity)this);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 177 */     return SoundEvents.PUFFER_FISH_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 182 */     return SoundEvents.PUFFER_FISH_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getFlopSound() {
/* 187 */     return SoundEvents.PUFFER_FISH_FLOP;
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 192 */     return super.getDefaultDimensions(pose).scale(getScale(getPuffState()));
/*     */   }
/*     */   
/*     */   private static float getScale(int state) {
/* 196 */     switch (state) {
/*     */       case 1:
/*     */       
/*     */       case 0:
/*     */       
/*     */     } 
/* 196 */     return 
/*     */       
/* 199 */       1.0F;
/*     */   }
/*     */   
/*     */   private static class PufferfishPuffGoal extends Goal {
/*     */     private final Pufferfish fish;
/*     */     
/*     */     public PufferfishPuffGoal(Pufferfish fish) {
/* 207 */       this.fish = fish;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 212 */       List<LivingEntity> entities = this.fish.level().getEntitiesOfClass(LivingEntity.class, this.fish.getBoundingBox().inflate(2.0D), target -> Pufferfish.TARGETING_CONDITIONS.test(getServerLevel((Entity)this.fish), (LivingEntity)this.fish, target));
/* 214 */       return !entities.isEmpty();
/*     */     }
/*     */     
/*     */     public void start() {
/* 219 */       this.fish.inflateCounter = 1;
/* 220 */       this.fish.deflateTimer = 0;
/*     */     }
/*     */     
/*     */     public void stop() {
/* 225 */       this.fish.inflateCounter = 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fish\Pufferfish.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */