/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ParticleUtils {
/*     */   public static void spawnParticlesOnBlockFaces(Level level, BlockPos pos, ParticleOptions particle, IntProvider particlesPerFaceRange) {
/*  20 */     RandomSource random = level.getRandom();
/*  21 */     for (Direction direction : Direction.values())
/*  22 */       spawnParticlesOnBlockFace(level, pos, particle, particlesPerFaceRange, direction, () -> getRandomSpeedRanges(random), 0.55D); 
/*     */   }
/*     */   
/*     */   public static void spawnParticlesOnBlockFace(Level level, BlockPos pos, ParticleOptions particle, IntProvider particlesPerFaceRange, Direction face, Supplier<Vec3> speedSupplier, double stepFactor) {
/*  27 */     int particleCount = particlesPerFaceRange.sample(level.getRandom());
/*  28 */     for (int i = 0; i < particleCount; i++)
/*  29 */       spawnParticleOnFace(level, pos, face, particle, speedSupplier.get(), stepFactor); 
/*     */   }
/*     */   
/*     */   private static Vec3 getRandomSpeedRanges(RandomSource random) {
/*  34 */     return new Vec3(Mth.nextDouble(random, -0.5D, 0.5D), Mth.nextDouble(random, -0.5D, 0.5D), Mth.nextDouble(random, -0.5D, 0.5D));
/*     */   }
/*     */   
/*     */   public static void spawnParticlesAlongAxis(Direction.Axis attachedAxis, Level level, BlockPos pos, double radius, ParticleOptions particle, UniformInt sparkCount) {
/*  38 */     Vec3 centerOfBlock = Vec3.atCenterOf((Vec3i)pos);
/*  40 */     boolean stepX = (attachedAxis == Direction.Axis.X);
/*  41 */     boolean stepY = (attachedAxis == Direction.Axis.Y);
/*  42 */     boolean stepZ = (attachedAxis == Direction.Axis.Z);
/*  44 */     RandomSource random = level.getRandom();
/*  45 */     int particleCount = sparkCount.sample(random);
/*  46 */     for (int i = 0; i < particleCount; i++) {
/*  47 */       double x = centerOfBlock.x + Mth.nextDouble(random, -1.0D, 1.0D) * (stepX ? 0.5D : radius);
/*  48 */       double y = centerOfBlock.y + Mth.nextDouble(random, -1.0D, 1.0D) * (stepY ? 0.5D : radius);
/*  49 */       double z = centerOfBlock.z + Mth.nextDouble(random, -1.0D, 1.0D) * (stepZ ? 0.5D : radius);
/*  50 */       double xBaseSpeed = stepX ? Mth.nextDouble(random, -1.0D, 1.0D) : 0.0D;
/*  51 */       double yBaseSpeed = stepY ? Mth.nextDouble(random, -1.0D, 1.0D) : 0.0D;
/*  52 */       double zBaseSpeed = stepZ ? Mth.nextDouble(random, -1.0D, 1.0D) : 0.0D;
/*  54 */       level.addParticle(particle, x, y, z, xBaseSpeed, yBaseSpeed, zBaseSpeed);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void spawnParticleOnFace(Level level, BlockPos pos, Direction face, ParticleOptions particle, Vec3 speed, double stepFactor) {
/*  59 */     Vec3 centerOfBlock = Vec3.atCenterOf((Vec3i)pos);
/*  60 */     int stepX = face.getStepX();
/*  61 */     int stepY = face.getStepY();
/*  62 */     int stepZ = face.getStepZ();
/*  63 */     RandomSource random = level.getRandom();
/*  64 */     double x = centerOfBlock.x + ((stepX == 0) ? Mth.nextDouble(random, -0.5D, 0.5D) : (stepX * stepFactor));
/*  65 */     double y = centerOfBlock.y + ((stepY == 0) ? Mth.nextDouble(random, -0.5D, 0.5D) : (stepY * stepFactor));
/*  66 */     double z = centerOfBlock.z + ((stepZ == 0) ? Mth.nextDouble(random, -0.5D, 0.5D) : (stepZ * stepFactor));
/*  67 */     double xBaseSpeed = (stepX == 0) ? speed.x() : 0.0D;
/*  68 */     double yBaseSpeed = (stepY == 0) ? speed.y() : 0.0D;
/*  69 */     double zBaseSpeed = (stepZ == 0) ? speed.z() : 0.0D;
/*  71 */     level.addParticle(particle, x, y, z, xBaseSpeed, yBaseSpeed, zBaseSpeed);
/*     */   }
/*     */   
/*     */   public static void spawnParticleBelow(Level level, BlockPos pos, RandomSource random, ParticleOptions particle) {
/*  75 */     double x = pos.getX() + random.nextDouble();
/*  76 */     double y = pos.getY() - 0.05D;
/*  77 */     double z = pos.getZ() + random.nextDouble();
/*  79 */     level.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   public static void spawnParticleInBlock(LevelAccessor level, BlockPos pos, int count, ParticleOptions particle) {
/*  83 */     double spreadWidth = 0.5D;
/*  84 */     BlockState blockState = level.getBlockState(pos);
/*  85 */     double spreadHeight = blockState.isAir() ? 1.0D : blockState.getShape((BlockGetter)level, pos).max(Direction.Axis.Y);
/*  86 */     spawnParticles(level, pos, count, 0.5D, spreadHeight, true, particle);
/*     */   }
/*     */   
/*     */   public static void spawnParticles(LevelAccessor level, BlockPos pos, int count, double spreadWidth, double spreadHeight, boolean allowFloatingParticles, ParticleOptions particle) {
/*  90 */     RandomSource random = level.getRandom();
/*  91 */     for (int i = 0; i < count; i++) {
/*  92 */       double xVelocity = random.nextGaussian() * 0.02D;
/*  93 */       double yVelocity = random.nextGaussian() * 0.02D;
/*  94 */       double zVelocity = random.nextGaussian() * 0.02D;
/*  96 */       double spreadStartOffset = 0.5D - spreadWidth;
/*  97 */       double x = pos.getX() + spreadStartOffset + random.nextDouble() * spreadWidth * 2.0D;
/*  98 */       double y = pos.getY() + random.nextDouble() * spreadHeight;
/*  99 */       double z = pos.getZ() + spreadStartOffset + random.nextDouble() * spreadWidth * 2.0D;
/* 101 */       if (allowFloatingParticles || !level.getBlockState(BlockPos.containing(x, y, z).below()).isAir())
/* 102 */         level.addParticle(particle, x, y, z, xVelocity, yVelocity, zVelocity); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void spawnSmashAttackParticles(LevelAccessor level, BlockPos pos, int count) {
/* 108 */     Vec3 center = Vec3.atCenterOf((Vec3i)pos).add(0.0D, 0.5D, 0.0D);
/* 109 */     BlockParticleOption particle = new BlockParticleOption(ParticleTypes.DUST_PILLAR, level.getBlockState(pos));
/*     */     int i;
/* 111 */     for (i = 0; i < count / 3.0F; i++) {
/* 112 */       double x = center.x + level.getRandom().nextGaussian() / 2.0D;
/* 113 */       double y = center.y;
/* 114 */       double z = center.z + level.getRandom().nextGaussian() / 2.0D;
/* 116 */       double xd = level.getRandom().nextGaussian() * 0.20000000298023224D;
/* 117 */       double yd = level.getRandom().nextGaussian() * 0.20000000298023224D;
/* 118 */       double zd = level.getRandom().nextGaussian() * 0.20000000298023224D;
/* 120 */       level.addParticle((ParticleOptions)particle, x, y, z, xd, yd, zd);
/*     */     } 
/* 123 */     for (i = 0; i < count / 1.5F; i++) {
/* 124 */       double x = center.x + 3.5D * Math.cos(i) + level.getRandom().nextGaussian() / 2.0D;
/* 125 */       double y = center.y;
/* 126 */       double z = center.z + 3.5D * Math.sin(i) + level.getRandom().nextGaussian() / 2.0D;
/* 128 */       double xd = level.getRandom().nextGaussian() * 0.05000000074505806D;
/* 129 */       double yd = level.getRandom().nextGaussian() * 0.05000000074505806D;
/* 130 */       double zd = level.getRandom().nextGaussian() * 0.05000000074505806D;
/* 132 */       level.addParticle((ParticleOptions)particle, x, y, z, xd, yd, zd);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ParticleUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */