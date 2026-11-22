/*      */ package net.minecraft.world.entity.animal.bee;
/*      */ 
/*      */ import java.util.Comparator;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.tags.PoiTypeTags;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*      */ 
/*      */ class BeeLocateHiveGoal extends Bee.BaseBeeGoal {
/*      */   public boolean canBeeUse() {
/* 1312 */     return (Bee.this.remainingCooldownBeforeLocatingNewHive == 0 && 
/* 1313 */       !Bee.this.hasHive() && Bee.this
/* 1314 */       .wantsToEnterHive());
/*      */   }
/*      */   
/*      */   public boolean canBeeContinueToUse() {
/* 1319 */     return false;
/*      */   }
/*      */   
/*      */   public void start() {
/* 1324 */     Bee.this.remainingCooldownBeforeLocatingNewHive = 200;
/* 1327 */     List<BlockPos> hivesWithSpace = findNearbyHivesWithSpace();
/* 1329 */     if (hivesWithSpace.isEmpty())
/*      */       return; 
/* 1335 */     for (BlockPos posToCheck : hivesWithSpace) {
/* 1336 */       if (!Bee.this.goToHiveGoal.isTargetBlacklisted(posToCheck)) {
/* 1338 */         Bee.this.hivePos = posToCheck;
/*      */         return;
/*      */       } 
/*      */     } 
/* 1346 */     Bee.this.goToHiveGoal.clearBlacklist();
/* 1347 */     Bee.this.hivePos = hivesWithSpace.get(0);
/*      */   }
/*      */   
/*      */   private List<BlockPos> findNearbyHivesWithSpace() {
/* 1351 */     BlockPos beePos = Bee.this.blockPosition();
/* 1352 */     PoiManager poiManager = ((ServerLevel)Bee.this.level()).getPoiManager();
/* 1353 */     Stream<PoiRecord> nearbyHives = poiManager.getInRange(p -> p.is(PoiTypeTags.BEE_HOME), beePos, 20, PoiManager.Occupancy.ANY);
/* 1355 */     Objects.requireNonNull(Bee.this);
/* 1355 */     return (List<BlockPos>)nearbyHives.map(PoiRecord::getPos).filter(Bee.this::doesHiveHaveSpace)
/* 1356 */       .sorted(Comparator.comparingDouble(pos -> pos.distSqr((Vec3i)beePos))).collect(Collectors.toList());
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\bee\Bee$BeeLocateHiveGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */