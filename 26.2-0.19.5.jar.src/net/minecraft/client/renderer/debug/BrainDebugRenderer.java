/*     */ package net.minecraft.client.renderer.debug;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.util.debug.DebugBrainDump;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debug.DebugValueAccess;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public class BrainDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
/*     */   private static final boolean SHOW_NAME_FOR_ALL = true;
/*     */   
/*     */   private static final boolean SHOW_PROFESSION_FOR_ALL = false;
/*     */   
/*     */   private static final boolean SHOW_BEHAVIORS_FOR_ALL = false;
/*     */   
/*     */   private static final boolean SHOW_ACTIVITIES_FOR_ALL = false;
/*     */   
/*     */   private static final boolean SHOW_INVENTORY_FOR_ALL = false;
/*     */   
/*     */   private static final boolean SHOW_GOSSIPS_FOR_ALL = false;
/*     */   
/*     */   private static final boolean SHOW_HEALTH_FOR_ALL = false;
/*     */   
/*     */   private static final boolean SHOW_WANTS_GOLEM_FOR_ALL = true;
/*     */   
/*     */   private static final boolean SHOW_ANGER_LEVEL_FOR_ALL = false;
/*     */   
/*     */   private static final boolean SHOW_NAME_FOR_SELECTED = true;
/*     */   
/*     */   private static final boolean SHOW_PROFESSION_FOR_SELECTED = true;
/*     */   
/*     */   private static final boolean SHOW_BEHAVIORS_FOR_SELECTED = true;
/*     */   
/*     */   private static final boolean SHOW_ACTIVITIES_FOR_SELECTED = true;
/*     */   
/*     */   private static final boolean SHOW_MEMORIES_FOR_SELECTED = true;
/*     */   
/*     */   private static final boolean SHOW_INVENTORY_FOR_SELECTED = true;
/*     */   
/*     */   private static final boolean SHOW_GOSSIPS_FOR_SELECTED = true;
/*     */   
/*     */   private static final boolean SHOW_HEALTH_FOR_SELECTED = true;
/*     */   
/*     */   private static final boolean SHOW_WANTS_GOLEM_FOR_SELECTED = true;
/*     */   
/*     */   private static final boolean SHOW_ANGER_LEVEL_FOR_SELECTED = true;
/*     */   
/*     */   private static final int MAX_RENDER_DIST_FOR_BRAIN_INFO = 30;
/*     */   
/*     */   private static final int MAX_TARGETING_DIST = 8;
/*     */   
/*     */   private static final float TEXT_SCALE = 0.32F;
/*     */   
/*     */   private static final int CYAN = -16711681;
/*     */   
/*     */   private static final int GRAY = -3355444;
/*     */   
/*     */   private static final int PINK = -98404;
/*     */   
/*     */   private static final int ORANGE = -23296;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private UUID lastLookedAtUuid;
/*     */   
/*     */   public BrainDebugRenderer(Minecraft minecraft) {
/*  68 */     this.minecraft = minecraft;
/*     */   }
/*     */   
/*     */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/*  73 */     doRender(debugValues);
/*  75 */     if (!this.minecraft.player.isSpectator())
/*  76 */       updateLastLookedAtUuid(); 
/*     */   }
/*     */   
/*     */   private void doRender(DebugValueAccess debugValues) {
/*  81 */     debugValues.forEachEntity(DebugSubscriptions.BRAINS, (entity, brainDump) -> {
/*     */           if (this.minecraft.player.closerThan(entity, 30.0D))
/*     */             renderBrainInfo(entity, brainDump); 
/*     */         });
/*     */   }
/*     */   
/*     */   private void renderBrainInfo(Entity entity, DebugBrainDump brainDump) {
/*  89 */     boolean selected = isMobSelected(entity);
/*  90 */     int row = 0;
/*  93 */     Gizmos.billboardTextOverMob(entity, row, brainDump.name(), -1, 0.48F);
/*  94 */     row++;
/*  97 */     if (selected) {
/*  98 */       Gizmos.billboardTextOverMob(entity, row, brainDump.profession() + " " + brainDump.profession() + " xp", -1, 0.32F);
/*  99 */       row++;
/*     */     } 
/* 102 */     if (selected) {
/* 103 */       int color = (brainDump.health() < brainDump.maxHealth()) ? -23296 : -1;
/* 104 */       Gizmos.billboardTextOverMob(entity, row, "health: " + String.format(Locale.ROOT, "%.1f", new Object[] { Float.valueOf(brainDump.health()) }) + " / " + String.format(Locale.ROOT, "%.1f", new Object[] { Float.valueOf(brainDump.maxHealth()) }), color, 0.32F);
/* 105 */       row++;
/*     */     } 
/* 108 */     if (selected && 
/* 109 */       !brainDump.inventory().equals("")) {
/* 110 */       Gizmos.billboardTextOverMob(entity, row, brainDump.inventory(), -98404, 0.32F);
/* 111 */       row++;
/*     */     } 
/* 115 */     if (selected)
/* 116 */       for (String goal : brainDump.behaviors()) {
/* 117 */         Gizmos.billboardTextOverMob(entity, row, goal, -16711681, 0.32F);
/* 118 */         row++;
/*     */       }  
/* 121 */     if (selected)
/* 122 */       for (String activity : brainDump.activities()) {
/* 123 */         Gizmos.billboardTextOverMob(entity, row, activity, -16711936, 0.32F);
/* 124 */         row++;
/*     */       }  
/* 129 */     if (brainDump.wantsGolem()) {
/* 130 */       Gizmos.billboardTextOverMob(entity, row, "Wants Golem", -23296, 0.32F);
/* 131 */       row++;
/*     */     } 
/* 135 */     if (selected && 
/* 136 */       brainDump.angerLevel() != -1) {
/* 137 */       Gizmos.billboardTextOverMob(entity, row, "Anger Level: " + brainDump.angerLevel(), -98404, 0.32F);
/* 138 */       row++;
/*     */     } 
/* 142 */     if (selected)
/* 143 */       for (String gossip : brainDump.gossips()) {
/* 144 */         if (gossip.startsWith(brainDump.name())) {
/* 145 */           Gizmos.billboardTextOverMob(entity, row, gossip, -1, 0.32F);
/*     */         } else {
/* 147 */           Gizmos.billboardTextOverMob(entity, row, gossip, -23296, 0.32F);
/*     */         } 
/* 149 */         row++;
/*     */       }  
/* 153 */     if (selected)
/* 154 */       for (String memory : Lists.reverse(brainDump.memories())) {
/* 155 */         Gizmos.billboardTextOverMob(entity, row, memory, -3355444, 0.32F);
/* 156 */         row++;
/*     */       }  
/*     */   }
/*     */   
/*     */   private boolean isMobSelected(Entity entity) {
/* 162 */     return Objects.equals(this.lastLookedAtUuid, entity.getUUID());
/*     */   }
/*     */   
/*     */   public Map<BlockPos, List<String>> getGhostPois(DebugValueAccess debugValues) {
/* 168 */     Map<BlockPos, List<String>> ghostPois = Maps.newHashMap();
/* 169 */     debugValues.forEachEntity(DebugSubscriptions.BRAINS, (entity, brainDump) -> {
/*     */           for (BlockPos poiPos : Iterables.concat(brainDump.pois(), brainDump.potentialPois()))
/*     */             ((List<String>)ghostPois.computeIfAbsent(poiPos, ())).add(brainDump.name()); 
/*     */         });
/* 174 */     return ghostPois;
/*     */   }
/*     */   
/*     */   private void updateLastLookedAtUuid() {
/* 178 */     DebugRenderer.getTargetedEntity(this.minecraft.getCameraEntity(), 8).ifPresent(entity -> this.lastLookedAtUuid = entity.getUUID());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\debug\BrainDebugRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */