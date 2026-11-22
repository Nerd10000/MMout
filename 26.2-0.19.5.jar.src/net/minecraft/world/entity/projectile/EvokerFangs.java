/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.TraceableEntity;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class EvokerFangs extends Entity implements TraceableEntity {
/*     */   public static final int ATTACK_DURATION = 20;
/*     */   
/*     */   public static final int LIFE_OFFSET = 2;
/*     */   
/*     */   public static final int ATTACK_TRIGGER_TICKS = 14;
/*     */   
/*     */   private static final int DEFAULT_WARMUP_DELAY = 0;
/*     */   
/*  31 */   private int warmupDelayTicks = 0;
/*     */   
/*     */   private boolean sentSpikeEvent;
/*     */   
/*  33 */   private int lifeTicks = 22;
/*     */   
/*     */   private boolean clientSideAttackStarted;
/*     */   
/*     */   private EntityReference<LivingEntity> owner;
/*     */   
/*     */   public EvokerFangs(EntityType<? extends EvokerFangs> type, Level level) {
/*  39 */     super(type, level);
/*     */   }
/*     */   
/*     */   public EvokerFangs(Level level, double x, double y, double z, float rotaionRadians, int warmupDelayTicks, LivingEntity owner) {
/*  43 */     this(EntityTypes.EVOKER_FANGS, level);
/*  44 */     this.warmupDelayTicks = warmupDelayTicks;
/*  45 */     setOwner(owner);
/*  46 */     setYRot(rotaionRadians * 57.295776F);
/*  47 */     setPos(x, y, z);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {}
/*     */   
/*     */   public void setOwner(LivingEntity owner) {
/*  55 */     this.owner = EntityReference.of((UniquelyIdentifyable)owner);
/*     */   }
/*     */   
/*     */   public LivingEntity getOwner() {
/*  60 */     return EntityReference.getLivingEntity(this.owner, level());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  65 */     this.warmupDelayTicks = input.getIntOr("Warmup", 0);
/*  66 */     this.owner = EntityReference.read(input, "Owner");
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  71 */     output.putInt("Warmup", this.warmupDelayTicks);
/*  72 */     EntityReference.store(this.owner, output, "Owner");
/*     */   }
/*     */   
/*     */   public void tick() {
/*  77 */     super.tick();
/*  79 */     if (level().isClientSide()) {
/*  80 */       if (this.clientSideAttackStarted) {
/*  81 */         this.lifeTicks--;
/*  82 */         if (this.lifeTicks == 14)
/*  83 */           for (int i = 0; i < 12; i++) {
/*  84 */             double x = getX() + (this.random.nextDouble() * 2.0D - 1.0D) * getBbWidth() * 0.5D;
/*  85 */             double y = getY() + 0.05D + this.random.nextDouble();
/*  86 */             double z = getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * getBbWidth() * 0.5D;
/*  87 */             double xd = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
/*  88 */             double yd = 0.3D + this.random.nextDouble() * 0.3D;
/*  89 */             double zd = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
/*  90 */             level().addParticle((ParticleOptions)ParticleTypes.CRIT, x, y + 1.0D, z, xd, yd, zd);
/*     */           }  
/*     */       } 
/*  95 */     } else if (--this.warmupDelayTicks < 0) {
/*  96 */       if (this.warmupDelayTicks == -8) {
/*  98 */         List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(0.2D, 0.0D, 0.2D));
/*  99 */         for (LivingEntity entity : entities)
/* 100 */           dealDamageTo(entity); 
/*     */       } 
/* 103 */       if (!this.sentSpikeEvent) {
/* 104 */         level().broadcastEntityEvent(this, (byte)4);
/* 105 */         this.sentSpikeEvent = true;
/*     */       } 
/* 107 */       if (--this.lifeTicks < 0)
/* 108 */         discard(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void dealDamageTo(LivingEntity entity) {
/* 115 */     LivingEntity currentOwner = getOwner();
/* 116 */     if (!entity.isAlive() || entity.isInvulnerable() || entity == currentOwner)
/*     */       return; 
/* 119 */     if (currentOwner == null) {
/* 120 */       entity.hurt(damageSources().magic(), 6.0F);
/*     */     } else {
/* 122 */       if (currentOwner.isAlliedTo((Entity)entity))
/*     */         return; 
/* 125 */       DamageSource damageSource = damageSources().indirectMagic(this, (Entity)currentOwner);
/* 126 */       Level level = level();
/* 126 */       if (level instanceof ServerLevel) {
/* 126 */         ServerLevel serverLevel = (ServerLevel)level;
/* 126 */         if (entity.hurtServer(serverLevel, damageSource, 6.0F))
/* 127 */           EnchantmentHelper.doPostAttackEffects(serverLevel, (Entity)entity, damageSource); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 134 */     super.handleEntityEvent(id);
/* 136 */     if (id == 4) {
/* 137 */       this.clientSideAttackStarted = true;
/* 138 */       if (!isSilent())
/* 139 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.EVOKER_FANGS_ATTACK, getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getAnimationProgress(float a) {
/* 145 */     if (!this.clientSideAttackStarted)
/* 146 */       return 0.0F; 
/* 148 */     int remainingLife = this.lifeTicks - 2;
/* 149 */     if (remainingLife <= 0)
/* 150 */       return 1.0F; 
/* 152 */     return 1.0F - (remainingLife - a) / 20.0F;
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 157 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\EvokerFangs.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */