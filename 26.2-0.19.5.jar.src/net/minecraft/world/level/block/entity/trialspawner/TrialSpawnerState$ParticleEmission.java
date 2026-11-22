/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ interface ParticleEmission {
/*     */   public static final ParticleEmission NONE = (level, random, pos, isOminous) -> {
/*     */     
/*     */     };
/*     */   
/*     */   public static final ParticleEmission SMALL_FLAMES;
/*     */   
/*     */   public static final ParticleEmission FLAMES_AND_SMOKE;
/*     */   
/*     */   public static final ParticleEmission SMOKE_INSIDE_AND_TOP_FACE;
/*     */   
/*     */   static {
/* 283 */     SMALL_FLAMES = ((level, random, pos, isOminous) -> {
/*     */         if (random.nextInt(2) == 0) {
/*     */           Vec3 vec = Vec3.atCenterOf((Vec3i)pos).offsetRandom(random, 0.9F);
/*     */           addParticle(isOminous ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.SMALL_FLAME, vec, level);
/*     */         } 
/*     */       });
/* 289 */     FLAMES_AND_SMOKE = ((level, random, pos, isOminous) -> {
/*     */         Vec3 vec = Vec3.atCenterOf((Vec3i)pos).offsetRandom(random, 1.0F);
/*     */         addParticle(ParticleTypes.SMOKE, vec, level);
/*     */         addParticle(isOminous ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, vec, level);
/*     */       });
/* 294 */     SMOKE_INSIDE_AND_TOP_FACE = ((level, random, pos, isOminous) -> {
/*     */         Vec3 vec = Vec3.atCenterOf((Vec3i)pos).offsetRandom(random, 0.9F);
/*     */         if (random.nextInt(3) == 0)
/*     */           addParticle(ParticleTypes.SMOKE, vec, level); 
/*     */         if (level.getGameTime() % 20L == 0L) {
/*     */           Vec3 topFaceVec = Vec3.atCenterOf((Vec3i)pos).add(0.0D, 0.5D, 0.0D);
/*     */           int smokeCount = level.getRandom().nextInt(4) + 20;
/*     */           for (int i = 0; i < smokeCount; i++)
/*     */             addParticle(ParticleTypes.SMOKE, topFaceVec, level); 
/*     */         } 
/*     */       });
/*     */   }
/*     */   
/*     */   private static void addParticle(SimpleParticleType smoke, Vec3 vec, Level level) {
/* 310 */     level.addParticle((ParticleOptions)smoke, vec.x(), vec.y(), vec.z(), 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   void emit(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, boolean paramBoolean);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawnerState$ParticleEmission.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */