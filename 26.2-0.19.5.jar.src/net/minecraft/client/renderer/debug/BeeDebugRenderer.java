/*     */ package net.minecraft.client.renderer.debug;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.gizmos.GizmoStyle;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.network.protocol.game.DebugEntityNameGenerator;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.debug.DebugBeeInfo;
/*     */ import net.minecraft.util.debug.DebugGoalInfo;
/*     */ import net.minecraft.util.debug.DebugHiveInfo;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debug.DebugValueAccess;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public class BeeDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
/*     */   private static final boolean SHOW_GOAL_FOR_ALL_BEES = true;
/*     */   
/*     */   private static final boolean SHOW_NAME_FOR_ALL_BEES = true;
/*     */   
/*     */   private static final boolean SHOW_HIVE_FOR_ALL_BEES = true;
/*     */   
/*     */   private static final boolean SHOW_FLOWER_POS_FOR_ALL_BEES = true;
/*     */   
/*     */   private static final boolean SHOW_TRAVEL_TICKS_FOR_ALL_BEES = true;
/*     */   
/*     */   private static final boolean SHOW_GOAL_FOR_SELECTED_BEE = true;
/*     */   
/*     */   private static final boolean SHOW_NAME_FOR_SELECTED_BEE = true;
/*     */   
/*     */   private static final boolean SHOW_HIVE_FOR_SELECTED_BEE = true;
/*     */   
/*     */   private static final boolean SHOW_FLOWER_POS_FOR_SELECTED_BEE = true;
/*     */   
/*     */   private static final boolean SHOW_TRAVEL_TICKS_FOR_SELECTED_BEE = true;
/*     */   
/*     */   private static final boolean SHOW_HIVE_MEMBERS = true;
/*     */   
/*     */   private static final boolean SHOW_BLACKLISTS = true;
/*     */   
/*     */   private static final int MAX_RENDER_DIST_FOR_HIVE_OVERLAY = 30;
/*     */   
/*     */   private static final int MAX_RENDER_DIST_FOR_BEE_OVERLAY = 30;
/*     */   
/*     */   private static final int MAX_TARGETING_DIST = 8;
/*     */   
/*     */   private static final float TEXT_SCALE = 0.32F;
/*     */   
/*     */   private static final int ORANGE = -23296;
/*     */   
/*     */   private static final int GRAY = -3355444;
/*     */   
/*     */   private static final int PINK = -98404;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private UUID lastLookedAtUuid;
/*     */   
/*     */   public BeeDebugRenderer(Minecraft minecraft) {
/*  66 */     this.minecraft = minecraft;
/*     */   }
/*     */   
/*     */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/*  71 */     doRender(debugValues);
/*  73 */     if (!this.minecraft.player.isSpectator())
/*  74 */       updateLastLookedAtUuid(); 
/*     */   }
/*     */   
/*     */   private void doRender(DebugValueAccess debugValues) {
/*  79 */     BlockPos playerPos = getCamera().blockPosition();
/*  81 */     debugValues.forEachEntity(DebugSubscriptions.BEES, (entity, beeInfo) -> {
/*     */           if (this.minecraft.player.closerThan(entity, 30.0D)) {
/*     */             DebugGoalInfo goalInfo = (DebugGoalInfo)debugValues.getEntityValue(DebugSubscriptions.GOAL_SELECTORS, entity);
/*     */             renderBeeInfo(entity, beeInfo, goalInfo);
/*     */           } 
/*     */         });
/*  89 */     renderFlowerInfos(debugValues);
/*  93 */     Map<BlockPos, Set<UUID>> hiveBlacklistMap = createHiveBlacklistMap(debugValues);
/*  95 */     debugValues.forEachBlock(DebugSubscriptions.BEE_HIVES, (pos, hive) -> {
/*     */           if (playerPos.closerThan((Vec3i)pos, 30.0D)) {
/*     */             highlightHive(pos);
/*     */             Set<UUID> beesWhoBlacklistThisHive = (Set<UUID>)hiveBlacklistMap.getOrDefault(pos, Set.of());
/*     */             renderHiveInfo(pos, hive, beesWhoBlacklistThisHive, debugValues);
/*     */           } 
/*     */         });
/* 104 */     getGhostHives(debugValues).forEach((ghostHivePos, value) -> {
/*     */           if (playerPos.closerThan((Vec3i)ghostHivePos, 30.0D))
/*     */             renderGhostHive(ghostHivePos, value); 
/*     */         });
/*     */   }
/*     */   
/*     */   private Map<BlockPos, Set<UUID>> createHiveBlacklistMap(DebugValueAccess debugValues) {
/* 114 */     Map<BlockPos, Set<UUID>> hiveBlacklistMap = new HashMap<>();
/* 115 */     debugValues.forEachEntity(DebugSubscriptions.BEES, (entity, bee) -> {
/*     */           for (BlockPos blacklistedFlowerPos : bee.blacklistedHives())
/*     */             ((Set<UUID>)hiveBlacklistMap.computeIfAbsent(blacklistedFlowerPos, ())).add(entity.getUUID()); 
/*     */         });
/* 120 */     return hiveBlacklistMap;
/*     */   }
/*     */   
/*     */   private void renderFlowerInfos(DebugValueAccess debugValues) {
/* 124 */     Map<BlockPos, Set<UUID>> beesPerFlower = new HashMap<>();
/* 126 */     debugValues.forEachEntity(DebugSubscriptions.BEES, (entity, bee) -> {
/*     */           if (bee.flowerPos().isPresent())
/*     */             ((Set<UUID>)beesPerFlower.computeIfAbsent(bee.flowerPos().get(), ())).add(entity.getUUID()); 
/*     */         });
/* 132 */     beesPerFlower.forEach((flowerPos, beesWithThisFlower) -> {
/*     */           Set<String> beeNames = (Set<String>)beesWithThisFlower.stream().map(DebugEntityNameGenerator::getEntityName).collect(Collectors.toSet());
/*     */           int row = 1;
/*     */           Gizmos.billboardTextOverBlock(beeNames.toString(), flowerPos, row++, -256, 0.32F);
/*     */           Gizmos.billboardTextOverBlock("Flower", flowerPos, row++, -1, 0.32F);
/*     */           Gizmos.cuboid(flowerPos, 0.05F, GizmoStyle.fill(ARGB.colorFromFloat(0.3F, 0.8F, 0.8F, 0.0F)));
/*     */         });
/*     */   }
/*     */   
/*     */   private static String getBeeUuidsAsString(Collection<UUID> uuids) {
/* 144 */     if (uuids.isEmpty())
/* 145 */       return "-"; 
/* 146 */     if (uuids.size() > 3)
/* 147 */       return "" + uuids.size() + " bees"; 
/* 149 */     return ((Set)uuids.stream().map(DebugEntityNameGenerator::getEntityName).collect(Collectors.toSet())).toString();
/*     */   }
/*     */   
/*     */   private static void highlightHive(BlockPos hivePos) {
/* 154 */     float padding = 0.05F;
/* 155 */     Gizmos.cuboid(hivePos, 0.05F, GizmoStyle.fill(ARGB.colorFromFloat(0.3F, 0.2F, 0.2F, 1.0F)));
/*     */   }
/*     */   
/*     */   private void renderGhostHive(BlockPos ghostHivePos, List<String> hiveMemberNames) {
/* 159 */     float padding = 0.05F;
/* 160 */     Gizmos.cuboid(ghostHivePos, 0.05F, GizmoStyle.fill(ARGB.colorFromFloat(0.3F, 0.2F, 0.2F, 1.0F)));
/* 161 */     Gizmos.billboardTextOverBlock(hiveMemberNames.toString(), ghostHivePos, 0, -256, 0.32F);
/* 162 */     Gizmos.billboardTextOverBlock("Ghost Hive", ghostHivePos, 1, -65536, 0.32F);
/*     */   }
/*     */   
/*     */   private void renderHiveInfo(BlockPos hivePos, DebugHiveInfo hive, Collection<UUID> beesWhoBlacklistThisHive, DebugValueAccess debugValues) {
/* 168 */     int row = 0;
/* 170 */     if (!beesWhoBlacklistThisHive.isEmpty())
/* 171 */       renderTextOverHive("Blacklisted by " + getBeeUuidsAsString(beesWhoBlacklistThisHive), hivePos, row++, -65536); 
/* 174 */     renderTextOverHive("Out: " + getBeeUuidsAsString(getHiveMembers(hivePos, debugValues)), hivePos, row++, -3355444);
/* 176 */     if (hive.occupantCount() == 0) {
/* 177 */       renderTextOverHive("In: -", hivePos, row++, -256);
/* 178 */     } else if (hive.occupantCount() == 1) {
/* 179 */       renderTextOverHive("In: 1 bee", hivePos, row++, -256);
/*     */     } else {
/* 181 */       renderTextOverHive("In: " + hive.occupantCount() + " bees", hivePos, row++, -256);
/*     */     } 
/* 184 */     renderTextOverHive("Honey: " + hive.honeyLevel(), hivePos, row++, -23296);
/* 186 */     renderTextOverHive(hive.type().getName().getString() + hive.type().getName().getString(), hivePos, row++, -1);
/*     */   }
/*     */   
/*     */   private void renderBeeInfo(Entity entity, DebugBeeInfo beeInfo, DebugGoalInfo goalInfo) {
/* 190 */     boolean selected = isBeeSelected(entity);
/* 193 */     int row = 0;
/* 196 */     Gizmos.billboardTextOverMob(entity, row++, beeInfo.toString(), -1, 0.48F);
/* 200 */     if (beeInfo.hivePos().isEmpty()) {
/* 201 */       Gizmos.billboardTextOverMob(entity, row++, "No hive", -98404, 0.32F);
/*     */     } else {
/* 203 */       Gizmos.billboardTextOverMob(entity, row++, "Hive: " + getPosDescription(entity, beeInfo.hivePos().get()), -256, 0.32F);
/*     */     } 
/* 208 */     if (beeInfo.flowerPos().isEmpty()) {
/* 209 */       Gizmos.billboardTextOverMob(entity, row++, "No flower", -98404, 0.32F);
/*     */     } else {
/* 211 */       Gizmos.billboardTextOverMob(entity, row++, "Flower: " + getPosDescription(entity, beeInfo.flowerPos().get()), -256, 0.32F);
/*     */     } 
/* 215 */     if (goalInfo != null)
/* 216 */       for (DebugGoalInfo.DebugGoal goal : goalInfo.goals()) {
/* 217 */         if (goal.isRunning())
/* 218 */           Gizmos.billboardTextOverMob(entity, row++, goal.name(), -16711936, 0.32F); 
/*     */       }  
/* 224 */     if (beeInfo.travelTicks() > 0) {
/* 225 */       int color = (beeInfo.travelTicks() < 2400) ? -3355444 : -23296;
/* 226 */       Gizmos.billboardTextOverMob(entity, row++, "Travelling: " + beeInfo.travelTicks() + " ticks", color, 0.32F);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void renderTextOverHive(String text, BlockPos hivePos, int row, int color) {
/* 232 */     Gizmos.billboardTextOverBlock(text, hivePos, row, color, 0.32F);
/*     */   }
/*     */   
/*     */   private Camera getCamera() {
/* 236 */     return this.minecraft.gameRenderer.mainCamera();
/*     */   }
/*     */   
/*     */   private String getPosDescription(Entity entity, BlockPos pos) {
/* 240 */     double dist = pos.distToCenterSqr((Position)entity.position());
/* 241 */     double distRounded = Math.round(dist * 10.0D) / 10.0D;
/* 242 */     return pos.toShortString() + " (dist " + pos.toShortString() + ")";
/*     */   }
/*     */   
/*     */   private boolean isBeeSelected(Entity entity) {
/* 246 */     return Objects.equals(this.lastLookedAtUuid, entity.getUUID());
/*     */   }
/*     */   
/*     */   private Collection<UUID> getHiveMembers(BlockPos hivePos, DebugValueAccess debugValues) {
/* 250 */     Set<UUID> hiveMembers = new HashSet<>();
/* 251 */     debugValues.forEachEntity(DebugSubscriptions.BEES, (entity, beeInfo) -> {
/*     */           if (beeInfo.hasHive(hivePos))
/*     */             hiveMembers.add(entity.getUUID()); 
/*     */         });
/* 256 */     return hiveMembers;
/*     */   }
/*     */   
/*     */   private Map<BlockPos, List<String>> getGhostHives(DebugValueAccess debugValues) {
/* 262 */     Map<BlockPos, List<String>> ghostHives = new HashMap<>();
/* 263 */     debugValues.forEachEntity(DebugSubscriptions.BEES, (entity, beeInfo) -> {
/*     */           if (beeInfo.hivePos().isPresent() && debugValues.getBlockValue(DebugSubscriptions.BEE_HIVES, beeInfo.hivePos().get()) == null)
/*     */             ((List<String>)ghostHives.computeIfAbsent(beeInfo.hivePos().get(), ())).add(DebugEntityNameGenerator.getEntityName(entity)); 
/*     */         });
/* 269 */     return ghostHives;
/*     */   }
/*     */   
/*     */   private void updateLastLookedAtUuid() {
/* 273 */     DebugRenderer.getTargetedEntity(this.minecraft.getCameraEntity(), 8).ifPresent(entity -> this.lastLookedAtUuid = entity.getUUID());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\debug\BeeDebugRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */