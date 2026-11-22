/*      */ package net.minecraft.world.entity.animal.bee;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Optional;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.level.pathfinder.Path;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ class BeePollinateGoal extends Bee.BaseBeeGoal {
/*      */   private static final int MIN_POLLINATION_TICKS = 400;
/*      */   
/*      */   private static final double ARRIVAL_THRESHOLD = 0.1D;
/*      */   
/*      */   private static final int POSITION_CHANGE_CHANCE = 25;
/*      */   
/*      */   private static final float SPEED_MODIFIER = 0.35F;
/*      */   
/*      */   private static final float HOVER_HEIGHT_WITHIN_FLOWER = 0.6F;
/*      */   
/*      */   private static final float HOVER_POS_OFFSET = 0.33333334F;
/*      */   
/*      */   private static final int FLOWER_SEARCH_RADIUS = 5;
/*      */   
/*      */   private int successfulPollinatingTicks;
/*      */   
/*      */   private int lastSoundPlayedTick;
/*      */   
/*      */   private boolean pollinating;
/*      */   
/*      */   private Vec3 hoverPos;
/*      */   
/*      */   private int pollinatingTicks;
/*      */   
/*      */   private static final int MAX_POLLINATING_TICKS = 600;
/*      */   
/* 1126 */   private Long2LongOpenHashMap unreachableFlowerCache = new Long2LongOpenHashMap();
/*      */   
/*      */   public BeePollinateGoal() {
/* 1129 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */   }
/*      */   
/*      */   public boolean canBeeUse() {
/* 1134 */     if (Bee.this.remainingCooldownBeforeLocatingNewFlower > 0)
/* 1135 */       return false; 
/* 1138 */     if (Bee.this.hasNectar())
/* 1139 */       return false; 
/* 1141 */     if (Bee.this.level().isRaining())
/* 1142 */       return false; 
/* 1146 */     Optional<BlockPos> nearbyPos = findNearbyFlower();
/* 1147 */     if (nearbyPos.isPresent()) {
/* 1148 */       Bee.this.savedFlowerPos = nearbyPos.get();
/* 1150 */       Bee.access$1800(Bee.this).moveTo(Bee.this.savedFlowerPos.getX() + 0.5D, Bee.this.savedFlowerPos.getY() + 0.5D, Bee.this.savedFlowerPos.getZ() + 0.5D, 1.2000000476837158D);
/* 1151 */       return true;
/*      */     } 
/* 1155 */     Bee.this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(Bee.access$1900(Bee.this), 20, 60);
/* 1156 */     return false;
/*      */   }
/*      */   
/*      */   public boolean canBeeContinueToUse() {
/* 1161 */     if (!this.pollinating)
/* 1162 */       return false; 
/* 1164 */     if (!Bee.this.hasSavedFlowerPos())
/* 1165 */       return false; 
/* 1167 */     if (Bee.this.level().isRaining())
/* 1168 */       return false; 
/* 1170 */     if (hasPollinatedLongEnough())
/* 1171 */       return (Bee.access$2000(Bee.this).nextFloat() < 0.2F); 
/* 1173 */     return true;
/*      */   }
/*      */   
/*      */   private boolean hasPollinatedLongEnough() {
/* 1177 */     return (this.successfulPollinatingTicks > 400);
/*      */   }
/*      */   
/*      */   private boolean isPollinating() {
/* 1181 */     return this.pollinating;
/*      */   }
/*      */   
/*      */   private void stopPollinating() {
/* 1185 */     this.pollinating = false;
/*      */   }
/*      */   
/*      */   public void start() {
/* 1190 */     this.successfulPollinatingTicks = 0;
/* 1191 */     this.pollinatingTicks = 0;
/* 1192 */     this.lastSoundPlayedTick = 0;
/* 1193 */     this.pollinating = true;
/* 1194 */     Bee.this.resetTicksWithoutNectarSinceExitingHive();
/*      */   }
/*      */   
/*      */   public void stop() {
/* 1199 */     if (hasPollinatedLongEnough())
/* 1200 */       Bee.this.setHasNectar(true); 
/* 1202 */     this.pollinating = false;
/* 1203 */     Bee.access$2100(Bee.this).stop();
/* 1205 */     Bee.this.remainingCooldownBeforeLocatingNewFlower = 200;
/*      */   }
/*      */   
/*      */   public boolean requiresUpdateEveryTick() {
/* 1210 */     return true;
/*      */   }
/*      */   
/*      */   public void tick() {
/* 1216 */     if (!Bee.this.hasSavedFlowerPos())
/*      */       return; 
/* 1220 */     this.pollinatingTicks++;
/* 1221 */     if (this.pollinatingTicks > 600) {
/* 1223 */       Bee.this.dropFlower();
/* 1224 */       this.pollinating = false;
/* 1225 */       Bee.this.remainingCooldownBeforeLocatingNewFlower = 200;
/*      */       return;
/*      */     } 
/* 1229 */     Vec3 flowerPos = Vec3.atBottomCenterOf((Vec3i)Bee.this.savedFlowerPos).add(0.0D, 0.6000000238418579D, 0.0D);
/* 1231 */     if (flowerPos.distanceTo(Bee.this.position()) > 1.0D) {
/* 1232 */       this.hoverPos = flowerPos;
/* 1233 */       setWantedPos();
/*      */       return;
/*      */     } 
/* 1237 */     if (this.hoverPos == null)
/* 1238 */       this.hoverPos = flowerPos; 
/* 1241 */     boolean arrivedAtHoverPos = (Bee.this.position().distanceTo(this.hoverPos) <= 0.1D);
/* 1242 */     boolean shouldSetWantedPos = true;
/* 1244 */     if (!arrivedAtHoverPos && this.pollinatingTicks > 600) {
/* 1246 */       Bee.this.dropFlower();
/*      */       return;
/*      */     } 
/* 1250 */     if (arrivedAtHoverPos) {
/* 1251 */       boolean shouldChangeHoverPositions = (Bee.access$2200(Bee.this).nextInt(25) == 0);
/* 1252 */       if (shouldChangeHoverPositions) {
/* 1253 */         this.hoverPos = new Vec3(flowerPos.x() + getOffset(), flowerPos.y(), flowerPos.z() + getOffset());
/* 1255 */         Bee.access$2300(Bee.this).stop();
/*      */       } else {
/* 1257 */         shouldSetWantedPos = false;
/*      */       } 
/* 1260 */       Bee.this.getLookControl().setLookAt(flowerPos.x(), flowerPos.y(), flowerPos.z());
/*      */     } 
/* 1263 */     if (shouldSetWantedPos)
/* 1264 */       setWantedPos(); 
/* 1267 */     this.successfulPollinatingTicks++;
/* 1269 */     if (Bee.access$2400(Bee.this).nextFloat() < 0.05F && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
/* 1270 */       this.lastSoundPlayedTick = this.successfulPollinatingTicks;
/* 1271 */       Bee.this.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void setWantedPos() {
/* 1276 */     Bee.this.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), 0.3499999940395355D);
/*      */   }
/*      */   
/*      */   private float getOffset() {
/* 1280 */     return (Bee.access$2500(Bee.this).nextFloat() * 2.0F - 1.0F) * 0.33333334F;
/*      */   }
/*      */   
/*      */   private Optional<BlockPos> findNearbyFlower() {
/* 1284 */     Iterable<BlockPos> closestNearbyFlowers = BlockPos.withinManhattan(Bee.this.blockPosition(), 5, 5, 5);
/* 1285 */     Long2LongOpenHashMap tempCache = new Long2LongOpenHashMap();
/* 1287 */     for (BlockPos pos : closestNearbyFlowers) {
/* 1289 */       long unreachableUntilTime = this.unreachableFlowerCache.getOrDefault(pos.asLong(), Long.MIN_VALUE);
/* 1290 */       if (Bee.this.level().getGameTime() < unreachableUntilTime) {
/* 1291 */         tempCache.put(pos.asLong(), unreachableUntilTime);
/*      */         continue;
/*      */       } 
/* 1294 */       if (Bee.attractsBees(Bee.this.level().getBlockState(pos))) {
/* 1295 */         Path path = Bee.access$2600(Bee.this).createPath(pos, 1);
/* 1296 */         if (path != null && path.canReach())
/* 1297 */           return Optional.of(pos); 
/* 1299 */         tempCache.put(pos.asLong(), Bee.this.level().getGameTime() + 600L);
/*      */       } 
/*      */     } 
/* 1303 */     this.unreachableFlowerCache = tempCache;
/* 1304 */     return Optional.empty();
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\bee\Bee$BeePollinateGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */