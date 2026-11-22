/*     */ package net.minecraft.world.entity.monster.cubemob;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.monster.Enemy;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ 
/*     */ public class Slime extends AbstractCubeMob implements Enemy {
/*     */   public Slime(EntityType<? extends Slime> type, Level level) {
/*  33 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   protected void addBehaviourGoals() {
/*  38 */     this.goalSelector.addGoal(2, new AbstractCubeMob.CubeMobAttackGoal(this));
/*     */   }
/*     */   
/*     */   protected void addTargetingGoals() {
/*  44 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, (target, level) -> (Math.abs(target.getY() - getY()) <= 4.0D)));
/*  45 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/*  50 */     if (isTiny())
/*  51 */       return SoundEvents.SLIME_HURT_SMALL; 
/*  53 */     return SoundEvents.SLIME_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  59 */     if (isTiny())
/*  60 */       return SoundEvents.SLIME_DEATH_SMALL; 
/*  62 */     return SoundEvents.SLIME_DEATH;
/*     */   }
/*     */   
/*     */   protected ParticleOptions getParticleType() {
/*  68 */     return (ParticleOptions)ParticleTypes.ITEM_SLIME;
/*     */   }
/*     */   
/*     */   protected SoundEvent getSquishSound() {
/*  73 */     if (isTiny())
/*  74 */       return SoundEvents.SLIME_SQUISH_SMALL; 
/*  76 */     return SoundEvents.SLIME_SQUISH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getJumpSound() {
/*  82 */     return isTiny() ? SoundEvents.SLIME_JUMP_SMALL : SoundEvents.SLIME_JUMP;
/*     */   }
/*     */   
/*     */   public static boolean checkSlimeSpawnRules(EntityType<Slime> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/*  86 */     if (level.getDifficulty() != Difficulty.PEACEFUL) {
/*     */       WorldGenLevel worldGenLevel;
/*  87 */       if (EntitySpawnReason.isSpawner(spawnReason))
/*  88 */         return checkMobSpawnRules(type, level, spawnReason, pos, random); 
/*  93 */       if (level.getBiome(pos).is(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS) && pos.getY() > 50 && pos.getY() < 70) {
/*  94 */         float surfaceSlimeSpawnChance = ((Float)level.environmentAttributes().getValue(EnvironmentAttributes.SURFACE_SLIME_SPAWN_CHANCE, pos)).floatValue();
/*  95 */         if (random.nextFloat() < surfaceSlimeSpawnChance && level.getMaxLocalRawBrightness(pos) <= random.nextInt(8))
/*  96 */           return checkMobSpawnRules(type, level, spawnReason, pos, random); 
/*     */       } 
/* 101 */       if (level instanceof WorldGenLevel) {
/* 101 */         worldGenLevel = (WorldGenLevel)level;
/*     */       } else {
/* 102 */         return false;
/*     */       } 
/* 104 */       ChunkPos chunkPos = ChunkPos.containing(pos);
/* 105 */       boolean slimeChunk = (WorldgenRandom.seedSlimeChunk(chunkPos.x(), chunkPos.z(), worldGenLevel.getSeed(), 987234911L).nextInt(10) == 0);
/* 106 */       if (random.nextInt(10) == 0 && slimeChunk && pos.getY() < 40)
/* 107 */         return checkMobSpawnRules(type, level, spawnReason, pos, random); 
/*     */     } 
/* 110 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean canBeABaby() {
/* 115 */     return false;
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/* 120 */     if (groupData == null)
/* 121 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(false); 
/* 124 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)ageableMobGroupData);
/*     */   }
/*     */   
/*     */   public void setSize(int size, boolean updateHealth) {
/* 129 */     super.setSize(size, updateHealth);
/* 130 */     int actualSize = ((Integer)this.entityData.get(ID_SIZE)).intValue();
/* 131 */     getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(actualSize);
/* 132 */     this.xpReward = actualSize;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\cubemob\Slime.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */