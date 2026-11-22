/*     */ package net.minecraft.world.entity.ai.sensing;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.function.BiPredicate;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ 
/*     */ public abstract class Sensor<E extends LivingEntity> {
/*     */   private static final int DEFAULT_SCAN_RATE = 20;
/*     */   
/*     */   private static final int DEFAULT_TARGETING_RANGE = 16;
/*     */   
/*  23 */   private static final TargetingConditions TARGET_CONDITIONS = TargetingConditions.forNonCombat().range(16.0D);
/*     */   
/*  24 */   private static final TargetingConditions TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING = TargetingConditions.forNonCombat().range(16.0D).ignoreInvisibilityTesting();
/*     */   
/*  25 */   private static final TargetingConditions ATTACK_TARGET_CONDITIONS = TargetingConditions.forCombat().range(16.0D);
/*     */   
/*  26 */   private static final TargetingConditions ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING = TargetingConditions.forCombat().range(16.0D).ignoreInvisibilityTesting();
/*     */   
/*  27 */   private static final TargetingConditions ATTACK_TARGET_CONDITIONS_IGNORE_LINE_OF_SIGHT = TargetingConditions.forCombat().range(16.0D).ignoreLineOfSight();
/*     */   
/*  28 */   private static final TargetingConditions ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_AND_LINE_OF_SIGHT = TargetingConditions.forCombat().range(16.0D).ignoreLineOfSight().ignoreInvisibilityTesting();
/*     */   
/*     */   private final int scanRate;
/*     */   
/*     */   private long timeToTick;
/*     */   
/*     */   public Sensor(int scanRate) {
/*  34 */     this.scanRate = scanRate;
/*     */   }
/*     */   
/*     */   public Sensor() {
/*  38 */     this(20);
/*     */   }
/*     */   
/*     */   public void randomlyDelayStart(RandomSource randomSource) {
/*  42 */     this.timeToTick = randomSource.nextInt(this.scanRate);
/*     */   }
/*     */   
/*     */   public final void tick(ServerLevel level, E body) {
/*  46 */     if (--this.timeToTick <= 0L) {
/*  47 */       this.timeToTick = this.scanRate;
/*  48 */       updateTargetingConditionRanges(body);
/*  49 */       doTick(level, body);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateTargetingConditionRanges(E body) {
/*  54 */     double followRange = body.getAttributeValue(Attributes.FOLLOW_RANGE);
/*  55 */     TARGET_CONDITIONS.range(followRange);
/*  56 */     TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING.range(followRange);
/*  57 */     ATTACK_TARGET_CONDITIONS.range(followRange);
/*  58 */     ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING.range(followRange);
/*  59 */     ATTACK_TARGET_CONDITIONS_IGNORE_LINE_OF_SIGHT.range(followRange);
/*  60 */     ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_AND_LINE_OF_SIGHT.range(followRange);
/*     */   }
/*     */   
/*     */   public static boolean isEntityTargetable(ServerLevel level, LivingEntity body, LivingEntity entity) {
/*  68 */     if (body.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, entity))
/*  70 */       return TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING.test(level, body, entity); 
/*  72 */     return TARGET_CONDITIONS.test(level, body, entity);
/*     */   }
/*     */   
/*     */   public static boolean isEntityAttackable(ServerLevel level, LivingEntity body, LivingEntity target) {
/*  77 */     if (body.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target))
/*  79 */       return ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING.test(level, body, target); 
/*  81 */     return ATTACK_TARGET_CONDITIONS.test(level, body, target);
/*     */   }
/*     */   
/*     */   public static BiPredicate<ServerLevel, LivingEntity> wasEntityAttackableLastNTicks(LivingEntity body, int ticks) {
/*  86 */     return rememberPositives(ticks, (level, target) -> isEntityAttackable(level, body, target));
/*     */   }
/*     */   
/*     */   public static boolean isEntityAttackableIgnoringLineOfSight(ServerLevel level, LivingEntity body, LivingEntity target) {
/*  90 */     if (body.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, target))
/*  92 */       return ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_AND_LINE_OF_SIGHT.test(level, body, target); 
/*  94 */     return ATTACK_TARGET_CONDITIONS_IGNORE_LINE_OF_SIGHT.test(level, body, target);
/*     */   }
/*     */   
/*     */   public static <T, U> BiPredicate<T, U> rememberPositives(int invocations, BiPredicate<T, U> predicate) {
/* 101 */     AtomicInteger positivesLeft = new AtomicInteger(0);
/* 102 */     return (t, u) -> {
/*     */         if (predicate.test(t, u)) {
/*     */           positivesLeft.set(invocations);
/*     */           return true;
/*     */         } 
/*     */         return (positivesLeft.decrementAndGet() >= 0);
/*     */       };
/*     */   }
/*     */   
/*     */   protected abstract void doTick(ServerLevel paramServerLevel, E paramE);
/*     */   
/*     */   public abstract Set<MemoryModuleType<?>> requires();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\Sensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */