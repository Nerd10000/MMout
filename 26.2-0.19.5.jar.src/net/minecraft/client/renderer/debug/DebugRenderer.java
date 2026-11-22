/*     */ package net.minecraft.client.renderer.debug;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.debug.DebugValueAccess;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class DebugRenderer {
/*  23 */   private final List<SimpleDebugRenderer> renderers = new ArrayList<>();
/*     */   
/*     */   private long lastDebugEntriesVersion;
/*     */   
/*     */   public DebugRenderer() {
/*  27 */     refreshRendererList();
/*     */   }
/*     */   
/*     */   public void refreshRendererList() {
/*  31 */     Minecraft minecraft = Minecraft.getInstance();
/*  32 */     this.renderers.clear();
/*  34 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.CHUNK_BORDERS))
/*  35 */       this.renderers.add(new ChunkBorderRenderer(minecraft)); 
/*  37 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.CHUNK_SECTION_OCTREE))
/*  38 */       this.renderers.add(new OctreeDebugRenderer(minecraft)); 
/*  40 */     if (SharedConstants.DEBUG_PATHFINDING)
/*  41 */       this.renderers.add(new PathfindingRenderer()); 
/*  43 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_WATER_LEVELS))
/*  44 */       this.renderers.add(new WaterDebugRenderer(minecraft)); 
/*  46 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_HEIGHTMAP))
/*  47 */       this.renderers.add(new HeightMapRenderer(minecraft)); 
/*  49 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_COLLISION_BOXES))
/*  50 */       this.renderers.add(new CollisionBoxRenderer(minecraft)); 
/*  52 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_ENTITY_SUPPORTING_BLOCKS))
/*  53 */       this.renderers.add(new SupportBlockRenderer(minecraft)); 
/*  55 */     if (SharedConstants.DEBUG_NEIGHBORSUPDATE)
/*  56 */       this.renderers.add(new NeighborsUpdateRenderer()); 
/*  58 */     if (SharedConstants.DEBUG_EXPERIMENTAL_REDSTONEWIRE_UPDATE_ORDER)
/*  59 */       this.renderers.add(new RedstoneWireOrientationsRenderer()); 
/*  61 */     if (SharedConstants.DEBUG_STRUCTURES)
/*  62 */       this.renderers.add(new StructureRenderer()); 
/*  64 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_BLOCK_LIGHT_LEVELS) || minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_SKY_LIGHT_LEVELS))
/*  65 */       this.renderers.add(new LightDebugRenderer(minecraft, minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_BLOCK_LIGHT_LEVELS), minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_SKY_LIGHT_LEVELS))); 
/*  67 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_SOLID_FACES))
/*  68 */       this.renderers.add(new SolidFaceRenderer(minecraft)); 
/*  70 */     if (SharedConstants.DEBUG_VILLAGE_SECTIONS)
/*  71 */       this.renderers.add(new VillageSectionsDebugRenderer()); 
/*  73 */     if (SharedConstants.DEBUG_BRAIN)
/*  74 */       this.renderers.add(new BrainDebugRenderer(minecraft)); 
/*  76 */     if (SharedConstants.DEBUG_POI)
/*  77 */       this.renderers.add(new PoiDebugRenderer(new BrainDebugRenderer(minecraft))); 
/*  79 */     if (SharedConstants.DEBUG_BEES)
/*  80 */       this.renderers.add(new BeeDebugRenderer(minecraft)); 
/*  82 */     if (SharedConstants.DEBUG_RAIDS)
/*  83 */       this.renderers.add(new RaidDebugRenderer(minecraft)); 
/*  85 */     if (SharedConstants.DEBUG_GOAL_SELECTOR)
/*  86 */       this.renderers.add(new GoalSelectorDebugRenderer(minecraft)); 
/*  88 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_CHUNKS_ON_SERVER))
/*  89 */       this.renderers.add(new ChunkDebugRenderer(minecraft)); 
/*  91 */     if (SharedConstants.DEBUG_GAME_EVENT_LISTENERS)
/*  92 */       this.renderers.add(new GameEventListenerRenderer()); 
/*  94 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_SKY_LIGHT_SECTIONS))
/*  95 */       this.renderers.add(new LightSectionDebugRenderer(minecraft, LightLayer.SKY)); 
/*  97 */     if (SharedConstants.DEBUG_BREEZE_MOB)
/*  98 */       this.renderers.add(new BreezeDebugRenderer(minecraft)); 
/* 100 */     if (SharedConstants.DEBUG_ENTITY_BLOCK_INTERSECTION)
/* 101 */       this.renderers.add(new EntityBlockIntersectionDebugRenderer()); 
/* 103 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.ENTITY_HITBOXES))
/* 104 */       this.renderers.add(new EntityHitboxDebugRenderer(minecraft)); 
/* 107 */     this.renderers.add(new ChunkCullingDebugRenderer(minecraft));
/*     */   }
/*     */   
/*     */   public void emitGizmos(Frustum frustum, double camX, double camY, double camZ, float partialTicks) {
/* 111 */     Minecraft minecraft = Minecraft.getInstance();
/* 112 */     DebugValueAccess debugValues = minecraft.getConnection().createDebugValueAccess();
/* 114 */     if (minecraft.debugEntries.getCurrentlyEnabledVersion() != this.lastDebugEntriesVersion) {
/* 115 */       this.lastDebugEntriesVersion = minecraft.debugEntries.getCurrentlyEnabledVersion();
/* 116 */       refreshRendererList();
/*     */     } 
/* 119 */     for (SimpleDebugRenderer renderer : this.renderers)
/* 120 */       renderer.emitGizmos(camX, camY, camZ, debugValues, frustum, partialTicks); 
/*     */   }
/*     */   
/*     */   public static Optional<Entity> getTargetedEntity(Entity cameraEntity, int maxTargetingRange) {
/* 125 */     if (cameraEntity == null)
/* 126 */       return Optional.empty(); 
/* 131 */     Vec3 from = cameraEntity.getEyePosition();
/* 132 */     Vec3 pick = cameraEntity.getViewVector(1.0F).scale(maxTargetingRange);
/* 133 */     Vec3 to = from.add(pick);
/* 135 */     AABB box = cameraEntity.getBoundingBox().expandTowards(pick).inflate(1.0D);
/* 137 */     int rangeSquared = maxTargetingRange * maxTargetingRange;
/* 138 */     EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(cameraEntity, from, to, box, EntitySelector.CAN_BE_PICKED, rangeSquared);
/* 139 */     if (hitResult == null)
/* 140 */       return Optional.empty(); 
/* 143 */     if (from.distanceToSqr(hitResult.getLocation()) > rangeSquared)
/* 145 */       return Optional.empty(); 
/* 148 */     return Optional.of(hitResult.getEntity());
/*     */   }
/*     */   
/*     */   private static Vec3 mixColor(float hueShift) {
/* 153 */     float regions = 5.99999F;
/* 154 */     int region = (int)(Mth.clamp(hueShift, 0.0F, 1.0F) * 5.99999F);
/* 155 */     float progress = hueShift * 5.99999F - region;
/* 156 */     switch (region) {
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */       
/*     */       case 3:
/*     */       
/*     */       case 4:
/*     */       
/*     */       case 5:
/*     */       
/*     */     } 
/* 163 */     throw new IllegalStateException("Unexpected value: " + region);
/*     */   }
/*     */   
/*     */   private static Vec3 shiftHue(float r, float g, float b, float hs) {
/* 169 */     Vec3 rshifted = mixColor(hs).scale(r);
/* 170 */     Vec3 gshifted = mixColor((hs + 0.33333334F) % 1.0F).scale(g);
/* 171 */     Vec3 bshifted = mixColor((hs + 0.6666667F) % 1.0F).scale(b);
/* 172 */     Vec3 combined = rshifted.add(gshifted).add(bshifted);
/* 173 */     double max = Math.max(Math.max(1.0D, combined.x), Math.max(combined.y, combined.z));
/* 174 */     return new Vec3(combined.x / max, combined.y / max, combined.z / max);
/*     */   }
/*     */   
/*     */   public static interface SimpleDebugRenderer {
/*     */     void emitGizmos(double param1Double1, double param1Double2, double param1Double3, DebugValueAccess param1DebugValueAccess, Frustum param1Frustum, float param1Float);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\debug\DebugRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */