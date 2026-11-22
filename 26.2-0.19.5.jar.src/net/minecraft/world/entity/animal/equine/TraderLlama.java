/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.TargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.monster.illager.AbstractIllager;
/*     */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*     */ import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class TraderLlama extends Llama {
/*     */   private static final int DEFAULT_DESPAWN_DELAY = 47999;
/*     */   
/*  30 */   private int despawnDelay = 47999;
/*     */   
/*     */   public TraderLlama(EntityType<? extends TraderLlama> type, Level level) {
/*  33 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   public boolean isTraderLlama() {
/*  38 */     return true;
/*     */   }
/*     */   
/*     */   protected Llama makeNewLlama() {
/*  43 */     TraderLlama baby = (TraderLlama)EntityTypes.TRADER_LLAMA.create(level(), EntitySpawnReason.BREEDING);
/*  44 */     if (baby != null)
/*  45 */       baby.setPersistenceRequired(); 
/*  47 */     return baby;
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  52 */     super.addAdditionalSaveData(output);
/*  53 */     output.putInt("DespawnDelay", this.despawnDelay);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  58 */     super.readAdditionalSaveData(input);
/*  59 */     this.despawnDelay = input.getIntOr("DespawnDelay", 47999);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  64 */     super.registerGoals();
/*  66 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 2.0D));
/*  68 */     this.targetSelector.addGoal(1, (Goal)new TraderLlamaDefendWanderingTraderGoal(this));
/*  69 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Zombie.class, true, (target, level) -> !target.is(EntityTypes.ZOMBIFIED_PIGLIN)));
/*  70 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractIllager.class, true));
/*     */   }
/*     */   
/*     */   public void setDespawnDelay(int despawnDelay) {
/*  74 */     this.despawnDelay = despawnDelay;
/*     */   }
/*     */   
/*     */   protected void doPlayerRide(Player player) {
/*  79 */     Entity leashHolder = getLeashHolder();
/*  80 */     if (leashHolder instanceof WanderingTrader)
/*     */       return; 
/*  84 */     super.doPlayerRide(player);
/*     */   }
/*     */   
/*     */   public void aiStep() {
/*  89 */     super.aiStep();
/*  91 */     if (!level().isClientSide())
/*  92 */       maybeDespawn(); 
/*     */   }
/*     */   
/*     */   private void maybeDespawn() {
/*  97 */     if (!canDespawn())
/*     */       return; 
/* 101 */     this.despawnDelay = isLeashedToWanderingTrader() ? (((WanderingTrader)getLeashHolder()).getDespawnDelay() - 1) : (this.despawnDelay - 1);
/* 103 */     if (this.despawnDelay <= 0) {
/* 104 */       removeLeash();
/* 105 */       discard();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canDespawn() {
/* 110 */     return (!isTamed() && 
/* 111 */       !isLeashedToSomethingOtherThanTheWanderingTrader() && 
/* 112 */       !hasExactlyOnePlayerPassenger() && 
/* 113 */       !isAgeLocked() && 
/* 114 */       !isPersistenceRequired());
/*     */   }
/*     */   
/*     */   private boolean isLeashedToWanderingTrader() {
/* 118 */     return getLeashHolder() instanceof WanderingTrader;
/*     */   }
/*     */   
/*     */   private boolean isLeashedToSomethingOtherThanTheWanderingTrader() {
/* 122 */     return (isLeashed() && !isLeashedToWanderingTrader());
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/* 127 */     if (spawnReason == EntitySpawnReason.EVENT)
/* 128 */       setAge(0); 
/* 131 */     if (groupData == null)
/* 132 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(false); 
/* 135 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)ageableMobGroupData);
/*     */   }
/*     */   
/*     */   protected static class TraderLlamaDefendWanderingTraderGoal extends TargetGoal {
/*     */     private final Llama llama;
/*     */     
/*     */     private LivingEntity ownerLastHurtBy;
/*     */     
/*     */     private int timestamp;
/*     */     
/*     */     public TraderLlamaDefendWanderingTraderGoal(Llama tameAnimal) {
/* 144 */       super((Mob)tameAnimal, false);
/* 145 */       this.llama = tameAnimal;
/* 146 */       setFlags(EnumSet.of(Goal.Flag.TARGET));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/*     */       WanderingTrader owner;
/* 151 */       if (!this.llama.isLeashed())
/* 152 */         return false; 
/* 154 */       Entity leashHolder = this.llama.getLeashHolder();
/* 155 */       if (leashHolder instanceof WanderingTrader) {
/* 155 */         owner = (WanderingTrader)leashHolder;
/*     */       } else {
/* 156 */         return false;
/*     */       } 
/* 159 */       this.ownerLastHurtBy = owner.getLastHurtByMob();
/* 160 */       int timeStamp = owner.getLastHurtByMobTimestamp();
/* 161 */       return (timeStamp != this.timestamp && canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT));
/*     */     }
/*     */     
/*     */     public void start() {
/* 166 */       this.mob.setTarget(this.ownerLastHurtBy);
/* 168 */       Entity leashHolder = this.llama.getLeashHolder();
/* 169 */       if (leashHolder instanceof WanderingTrader) {
/* 169 */         WanderingTrader wanderingTrader = (WanderingTrader)leashHolder;
/* 170 */         this.timestamp = wanderingTrader.getLastHurtByMobTimestamp();
/*     */       } 
/* 173 */       super.start();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\equine\TraderLlama.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */