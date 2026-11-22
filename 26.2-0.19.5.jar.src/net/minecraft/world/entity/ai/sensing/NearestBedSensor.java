/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import it.unimi.dsi.fastutil.longs.Long2LongMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.behavior.AcquirePoi;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ 
/*    */ public class NearestBedSensor extends Sensor<Mob> {
/*    */   private static final int CACHE_TIMEOUT = 40;
/*    */   
/*    */   private static final int BATCH_SIZE = 5;
/*    */   
/*    */   private static final int RATE = 20;
/*    */   
/* 30 */   private final Long2LongMap batchCache = (Long2LongMap)new Long2LongOpenHashMap();
/*    */   
/*    */   private int triedCount;
/*    */   
/*    */   private long lastUpdate;
/*    */   
/*    */   public NearestBedSensor() {
/* 35 */     super(20);
/*    */   }
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 40 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_BED);
/*    */   }
/*    */   
/*    */   protected void doTick(ServerLevel level, Mob body) {
/* 45 */     if (!body.isBaby())
/*    */       return; 
/* 49 */     this.triedCount = 0;
/* 50 */     this.lastUpdate = level.getGameTime() + level.getRandom().nextInt(20);
/* 52 */     PoiManager poiManager = level.getPoiManager();
/* 54 */     Predicate<BlockPos> cacheTest = pos -> {
/*    */         long key = pos.asLong();
/*    */         if (this.batchCache.containsKey(key))
/*    */           return false; 
/*    */         if (++this.triedCount >= 5)
/*    */           return false; 
/*    */         this.batchCache.put(key, this.lastUpdate + 40L);
/*    */         return true;
/*    */       };
/* 68 */     Set<Pair<Holder<PoiType>, BlockPos>> pois = (Set<Pair<Holder<PoiType>, BlockPos>>)poiManager.findAllWithType(e -> e.is(PoiTypes.HOME), cacheTest, body.blockPosition(), 48, PoiManager.Occupancy.ANY).collect(Collectors.toSet());
/* 69 */     Path path = AcquirePoi.findPathToPois(body, pois);
/* 71 */     if (path != null && path.canReach()) {
/* 72 */       BlockPos targetPos = path.getTarget();
/* 73 */       Optional<Holder<PoiType>> type = poiManager.getType(targetPos);
/* 74 */       if (type.isPresent())
/* 76 */         body.getBrain().setMemory(MemoryModuleType.NEAREST_BED, targetPos); 
/* 78 */     } else if (this.triedCount < 5) {
/* 79 */       this.batchCache.long2LongEntrySet().removeIf(entry -> (entry.getLongValue() < this.lastUpdate));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\NearestBedSensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */