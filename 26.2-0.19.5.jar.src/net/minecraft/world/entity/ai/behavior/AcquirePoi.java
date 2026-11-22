/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import org.apache.commons.lang3.mutable.MutableLong;
/*     */ 
/*     */ public class AcquirePoi {
/*     */   public static final int SCAN_RANGE = 48;
/*     */   
/*     */   public static BehaviorControl<PathfinderMob> create(Predicate<Holder<PoiType>> poiType, MemoryModuleType<GlobalPos> memoryToAcquire, boolean onlyIfAdult, Optional<Byte> onPoiAcquisitionEvent, BiPredicate<ServerLevel, BlockPos> validPoi) {
/*  37 */     return create(poiType, memoryToAcquire, memoryToAcquire, onlyIfAdult, onPoiAcquisitionEvent, validPoi);
/*     */   }
/*     */   
/*     */   public static BehaviorControl<PathfinderMob> create(Predicate<Holder<PoiType>> poiType, MemoryModuleType<GlobalPos> memoryToAcquire, boolean onlyIfAdult, Optional<Byte> onPoiAcquisitionEvent) {
/*  41 */     return create(poiType, memoryToAcquire, memoryToAcquire, onlyIfAdult, onPoiAcquisitionEvent, (l, p) -> true);
/*     */   }
/*     */   
/*     */   public static BehaviorControl<PathfinderMob> create(Predicate<Holder<PoiType>> poiType, MemoryModuleType<GlobalPos> memoryToValidate, MemoryModuleType<GlobalPos> memoryToAcquire, boolean onlyIfAdult, Optional<Byte> onPoiAcquisitionEvent, BiPredicate<ServerLevel, BlockPos> validPoi) {
/*  45 */     int batchSize = 5;
/*  46 */     int rate = 20;
/*  49 */     MutableLong nextScheduledStart = new MutableLong(0L);
/*  50 */     Long2ObjectOpenHashMap long2ObjectOpenHashMap = new Long2ObjectOpenHashMap();
/*  52 */     OneShot<PathfinderMob> acquirePoi = BehaviorBuilder.create(i -> i.group((App)i.absent(memoryToAcquire)).apply((Applicative)i, ()));
/* 116 */     if (memoryToAcquire == memoryToValidate)
/* 117 */       return acquirePoi; 
/* 120 */     return BehaviorBuilder.create(i -> i.group((App)i.absent(memoryToValidate)).apply((Applicative)i, ()));
/*     */   }
/*     */   
/*     */   public static Path findPathToPois(Mob body, Set<Pair<Holder<PoiType>, BlockPos>> pois) {
/* 126 */     if (pois.isEmpty())
/* 127 */       return null; 
/* 129 */     Set<BlockPos> targets = new HashSet<>();
/* 130 */     int maxRange = 1;
/* 131 */     for (Pair<Holder<PoiType>, BlockPos> p : pois) {
/* 132 */       maxRange = Math.max(maxRange, ((PoiType)((Holder)p.getFirst()).value()).validRange());
/* 133 */       targets.add((BlockPos)p.getSecond());
/*     */     } 
/* 135 */     return body.getNavigation().createPath(targets, maxRange);
/*     */   }
/*     */   
/*     */   private static class JitteredLinearRetry {
/*     */     private static final int MIN_INTERVAL_INCREASE = 40;
/*     */     
/*     */     private static final int MAX_INTERVAL_INCREASE = 80;
/*     */     
/*     */     private static final int MAX_RETRY_PATHFINDING_INTERVAL = 400;
/*     */     
/*     */     private final RandomSource random;
/*     */     
/*     */     private long previousAttemptTimestamp;
/*     */     
/*     */     private long nextScheduledAttemptTimestamp;
/*     */     
/*     */     private int currentDelay;
/*     */     
/*     */     public JitteredLinearRetry(RandomSource random, long firstAttemptTimestamp) {
/* 150 */       this.random = random;
/* 151 */       markAttempt(firstAttemptTimestamp);
/*     */     }
/*     */     
/*     */     public void markAttempt(long timestamp) {
/* 155 */       this.previousAttemptTimestamp = timestamp;
/* 156 */       int suggestedDelay = this.currentDelay + this.random.nextInt(40) + 40;
/* 157 */       this.currentDelay = Math.min(suggestedDelay, 400);
/* 158 */       this.nextScheduledAttemptTimestamp = timestamp + this.currentDelay;
/*     */     }
/*     */     
/*     */     public boolean isStillValid(long timestamp) {
/* 162 */       return (timestamp - this.previousAttemptTimestamp < 400L);
/*     */     }
/*     */     
/*     */     public boolean shouldRetry(long timestamp) {
/* 166 */       return (timestamp >= this.nextScheduledAttemptTimestamp);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 171 */       return "RetryMarker{, previousAttemptAt=" + this.previousAttemptTimestamp + ", nextScheduledAttemptAt=" + this.nextScheduledAttemptTimestamp + ", currentDelay=" + this.currentDelay + "}";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\AcquirePoi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */