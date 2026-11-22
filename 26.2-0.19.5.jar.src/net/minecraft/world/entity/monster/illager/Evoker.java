/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.animal.sheep.Sheep;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.Vex;
/*     */ import net.minecraft.world.entity.monster.creaking.Creaking;
/*     */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.EvokerFangs;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ 
/*     */ public class Evoker extends SpellcasterIllager {
/*     */   private Sheep wololoTarget;
/*     */   
/*     */   public Evoker(EntityType<? extends Evoker> type, Level level) {
/*  53 */     super((EntityType)type, level);
/*  55 */     this.xpReward = 10;
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  60 */     super.registerGoals();
/*  62 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  63 */     this.goalSelector.addGoal(1, new EvokerCastingSpellGoal());
/*  64 */     this.goalSelector.addGoal(2, (Goal)new AvoidEntityGoal((PathfinderMob)this, Player.class, 8.0F, 0.6D, 1.0D));
/*  65 */     this.goalSelector.addGoal(3, (Goal)new AvoidEntityGoal((PathfinderMob)this, Creaking.class, 8.0F, 0.6D, 1.0D));
/*  66 */     this.goalSelector.addGoal(4, new EvokerSummonSpellGoal());
/*  67 */     this.goalSelector.addGoal(5, new EvokerAttackSpellGoal());
/*  68 */     this.goalSelector.addGoal(6, new EvokerWololoSpellGoal());
/*  69 */     this.goalSelector.addGoal(8, (Goal)new RandomStrollGoal((PathfinderMob)this, 0.6D));
/*  70 */     this.goalSelector.addGoal(9, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 3.0F, 1.0F));
/*  71 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 8.0F));
/*  73 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[] { Raider.class })).setAlertOthers(new Class[0]));
/*  74 */     this.targetSelector.addGoal(2, (Goal)(new NearestAttackableTargetGoal((Mob)this, Player.class, true)).setUnseenMemoryTicks(300));
/*  75 */     this.targetSelector.addGoal(3, (Goal)(new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, false)).setUnseenMemoryTicks(300));
/*  76 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, false));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  80 */     return Monster.createMonsterAttributes()
/*  81 */       .add(Attributes.MOVEMENT_SPEED, 0.5D)
/*  82 */       .add(Attributes.FOLLOW_RANGE, 12.0D)
/*  83 */       .add(Attributes.MAX_HEALTH, 24.0D);
/*     */   }
/*     */   
/*     */   public SoundEvent getCelebrateSound() {
/*  88 */     return SoundEvents.EVOKER_CELEBRATE;
/*     */   }
/*     */   
/*     */   protected boolean considersEntityAsAlly(Entity other) {
/*  93 */     if (other == this)
/*  94 */       return true; 
/*  96 */     if (super.considersEntityAsAlly(other))
/*  97 */       return true; 
/*  99 */     if (other instanceof Vex) {
/*  99 */       Vex vex = (Vex)other;
/* 100 */       LivingEntity rootOwner = vex.getRootOwner();
/* 101 */       if (rootOwner != null && (rootOwner == this || super.considersEntityAsAlly((Entity)rootOwner)))
/* 102 */         return true; 
/*     */     } 
/* 105 */     return false;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 110 */     return SoundEvents.EVOKER_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 115 */     return SoundEvents.EVOKER_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 120 */     return SoundEvents.EVOKER_HURT;
/*     */   }
/*     */   
/*     */   private void setWololoTarget(Sheep wololoTarget) {
/* 124 */     this.wololoTarget = wololoTarget;
/*     */   }
/*     */   
/*     */   private Sheep getWololoTarget() {
/* 128 */     return this.wololoTarget;
/*     */   }
/*     */   
/*     */   protected SoundEvent getCastingSoundEvent() {
/* 133 */     return SoundEvents.EVOKER_CAST_SPELL;
/*     */   }
/*     */   
/*     */   public void applyRaidBuffs(ServerLevel level, int wave, boolean isCaptain) {}
/*     */   
/*     */   private class EvokerCastingSpellGoal extends SpellcasterIllager.SpellcasterCastingSpellGoal {
/*     */     public void tick() {
/* 143 */       if (Evoker.this.getTarget() != null) {
/* 144 */         Evoker.this.getLookControl().setLookAt((Entity)Evoker.this.getTarget(), Evoker.this.getMaxHeadYRot(), Evoker.this.getMaxHeadXRot());
/* 145 */       } else if (Evoker.this.getWololoTarget() != null) {
/* 146 */         Evoker.this.getLookControl().setLookAt((Entity)Evoker.this.getWololoTarget(), Evoker.this.getMaxHeadYRot(), Evoker.this.getMaxHeadXRot());
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class EvokerAttackSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
/*     */     protected int getCastingTime() {
/* 154 */       return 40;
/*     */     }
/*     */     
/*     */     protected int getCastingInterval() {
/* 159 */       return 100;
/*     */     }
/*     */     
/*     */     protected void performSpellCasting() {
/* 165 */       LivingEntity target = Evoker.this.getTarget();
/* 166 */       double minY = Math.min(target.getY(), Evoker.this.getY());
/* 167 */       double maxY = Math.max(target.getY(), Evoker.this.getY()) + 1.0D;
/* 168 */       float angleTowardsTarget = (float)Mth.atan2(target.getZ() - Evoker.this.getZ(), target.getX() - Evoker.this.getX());
/* 169 */       if (Evoker.this.distanceToSqr((Entity)target) < 9.0D) {
/*     */         int i;
/* 171 */         for (i = 0; i < 5; i++) {
/* 172 */           float angle = angleTowardsTarget + i * 3.1415927F * 0.4F;
/* 173 */           createSpellEntity(Evoker.this.getX() + Mth.cos(angle) * 1.5D, Evoker.this.getZ() + Mth.sin(angle) * 1.5D, minY, maxY, angle, 0);
/*     */         } 
/* 176 */         for (i = 0; i < 8; i++) {
/* 177 */           float angle = angleTowardsTarget + i * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
/* 178 */           createSpellEntity(Evoker.this.getX() + Mth.cos(angle) * 2.5D, Evoker.this.getZ() + Mth.sin(angle) * 2.5D, minY, maxY, angle, 3);
/*     */         } 
/*     */       } else {
/* 182 */         for (int i = 0; i < 16; i++) {
/* 183 */           double reach = 1.25D * (i + 1);
/* 184 */           int spellSpeed = 1 * i;
/* 185 */           createSpellEntity(Evoker.this.getX() + Mth.cos(angleTowardsTarget) * reach, Evoker.this.getZ() + Mth.sin(angleTowardsTarget) * reach, minY, maxY, angleTowardsTarget, spellSpeed);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void createSpellEntity(double x, double z, double minY, double maxY, float angle, int delayTicks) {
/* 192 */       BlockPos pos = BlockPos.containing(x, maxY, z);
/* 193 */       boolean success = false;
/* 194 */       double topOffset = 0.0D;
/*     */       do {
/* 196 */         BlockPos below = pos.below();
/* 197 */         BlockState belowState = Evoker.this.level().getBlockState(below);
/* 198 */         if (belowState.isFaceSturdy((BlockGetter)Evoker.this.level(), below, Direction.UP)) {
/* 199 */           if (!Evoker.this.level().isEmptyBlock(pos)) {
/* 200 */             BlockState blockState = Evoker.this.level().getBlockState(pos);
/* 201 */             VoxelShape shape = blockState.getCollisionShape((BlockGetter)Evoker.this.level(), pos);
/* 202 */             if (!shape.isEmpty())
/* 203 */               topOffset = shape.max(Direction.Axis.Y); 
/*     */           } 
/* 206 */           success = true;
/*     */           break;
/*     */         } 
/* 209 */         pos = pos.below();
/* 210 */       } while (pos.getY() >= Mth.floor(minY) - 1);
/* 211 */       if (success) {
/* 212 */         Evoker.this.level().addFreshEntity((Entity)new EvokerFangs(Evoker.this.level(), x, pos.getY() + topOffset, z, angle, delayTicks, (LivingEntity)Evoker.this));
/* 213 */         Evoker.this.level().gameEvent((Holder)GameEvent.ENTITY_PLACE, new Vec3(x, pos.getY() + topOffset, z), GameEvent.Context.of((Entity)Evoker.this));
/*     */       } 
/*     */     }
/*     */     
/*     */     protected SoundEvent getSpellPrepareSound() {
/* 219 */       return SoundEvents.EVOKER_PREPARE_ATTACK;
/*     */     }
/*     */     
/*     */     protected SpellcasterIllager.IllagerSpell getSpell() {
/* 224 */       return SpellcasterIllager.IllagerSpell.FANGS;
/*     */     }
/*     */   }
/*     */   
/*     */   private class EvokerSummonSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
/*     */     private final TargetingConditions vexCountTargeting;
/*     */     
/*     */     private EvokerSummonSpellGoal() {
/* 229 */       this.vexCountTargeting = TargetingConditions.forNonCombat().range(16.0D).ignoreLineOfSight().ignoreInvisibilityTesting();
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 233 */       if (!super.canUse())
/* 234 */         return false; 
/* 238 */       int vexes = getServerLevel(Evoker.this.level()).getNearbyEntities(Vex.class, this.vexCountTargeting, (LivingEntity)Evoker.this, Evoker.this.getBoundingBox().inflate(16.0D)).size();
/* 239 */       return (Evoker.this.random.nextInt(8) + 1 > vexes);
/*     */     }
/*     */     
/*     */     protected int getCastingTime() {
/* 244 */       return 100;
/*     */     }
/*     */     
/*     */     protected int getCastingInterval() {
/* 249 */       return 340;
/*     */     }
/*     */     
/*     */     protected void performSpellCasting() {
/* 254 */       ServerLevel serverLevel = (ServerLevel)Evoker.this.level();
/* 255 */       PlayerTeam evokerTeam = Evoker.this.getTeam();
/* 256 */       for (int i = 0; i < 3; i++) {
/* 257 */         BlockPos pos = Evoker.this.blockPosition().offset(-2 + Evoker.this.random.nextInt(5), 1, -2 + Evoker.this.random.nextInt(5));
/* 258 */         Vex vex = (Vex)EntityTypes.VEX.create(Evoker.this.level(), EntitySpawnReason.MOB_SUMMONED);
/* 259 */         if (vex != null) {
/* 260 */           vex.snapTo(pos, 0.0F, 0.0F);
/* 261 */           vex.finalizeSpawn((ServerLevelAccessor)serverLevel, serverLevel.getCurrentDifficultyAt(pos), EntitySpawnReason.MOB_SUMMONED, null);
/* 262 */           vex.setOwner((Mob)Evoker.this);
/* 263 */           vex.setBoundOrigin(pos);
/* 264 */           vex.setLimitedLife(20 * (30 + Evoker.this.random.nextInt(90)));
/* 265 */           if (evokerTeam != null)
/* 266 */             serverLevel.getScoreboard().addPlayerToTeam(vex.getScoreboardName(), evokerTeam); 
/* 268 */           serverLevel.addFreshEntityWithPassengers((Entity)vex);
/* 269 */           serverLevel.gameEvent((Holder)GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of((Entity)Evoker.this));
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     protected SoundEvent getSpellPrepareSound() {
/* 276 */       return SoundEvents.EVOKER_PREPARE_SUMMON;
/*     */     }
/*     */     
/*     */     protected SpellcasterIllager.IllagerSpell getSpell() {
/* 281 */       return SpellcasterIllager.IllagerSpell.SUMMON_VEX;
/*     */     }
/*     */   }
/*     */   
/*     */   public class EvokerWololoSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
/*     */     private final TargetingConditions wololoTargeting;
/*     */     
/*     */     public EvokerWololoSpellGoal() {
/* 286 */       this.wololoTargeting = TargetingConditions.forNonCombat().range(16.0D).selector((target, level) -> (((Sheep)target).getColor() == DyeColor.BLUE));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 290 */       if (Evoker.this.getTarget() != null)
/* 292 */         return false; 
/* 294 */       if (Evoker.this.isCastingSpell())
/* 296 */         return false; 
/* 298 */       if (Evoker.this.tickCount < this.nextAttackTickCount)
/* 299 */         return false; 
/* 301 */       ServerLevel level = getServerLevel(Evoker.this.level());
/* 302 */       if (!((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue())
/* 303 */         return false; 
/* 306 */       List<Sheep> entities = level.getNearbyEntities(Sheep.class, this.wololoTargeting, (LivingEntity)Evoker.this, Evoker.this.getBoundingBox().inflate(16.0D, 4.0D, 16.0D));
/* 308 */       if (entities.isEmpty())
/* 309 */         return false; 
/* 311 */       Evoker.this.setWololoTarget(entities.get(Evoker.this.random.nextInt(entities.size())));
/* 312 */       return true;
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 318 */       return (Evoker.this.getWololoTarget() != null && this.attackWarmupDelay > 0);
/*     */     }
/*     */     
/*     */     public void stop() {
/* 323 */       super.stop();
/* 324 */       Evoker.this.setWololoTarget((Sheep)null);
/*     */     }
/*     */     
/*     */     protected void performSpellCasting() {
/* 329 */       Sheep wololoTarget = Evoker.this.getWololoTarget();
/* 330 */       if (wololoTarget != null && wololoTarget.isAlive())
/* 331 */         wololoTarget.setColor(DyeColor.RED); 
/*     */     }
/*     */     
/*     */     protected int getCastWarmupTime() {
/* 337 */       return 40;
/*     */     }
/*     */     
/*     */     protected int getCastingTime() {
/* 342 */       return 60;
/*     */     }
/*     */     
/*     */     protected int getCastingInterval() {
/* 347 */       return 140;
/*     */     }
/*     */     
/*     */     protected SoundEvent getSpellPrepareSound() {
/* 352 */       return SoundEvents.EVOKER_PREPARE_WOLOLO;
/*     */     }
/*     */     
/*     */     protected SpellcasterIllager.IllagerSpell getSpell() {
/* 357 */       return SpellcasterIllager.IllagerSpell.WOLOLO;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\illager\Evoker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */