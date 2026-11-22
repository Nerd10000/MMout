/*     */ package net.minecraft.client.renderer.debug;
/*     */ 
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.CameraType;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.gizmos.GizmoStyle;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.gizmos.TextGizmo;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.debug.DebugValueAccess;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EntityHitboxDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   public EntityHitboxDebugRenderer(Minecraft minecraft) {
/*  27 */     this.minecraft = minecraft;
/*     */   }
/*     */   
/*     */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/*  32 */     if (this.minecraft.level == null)
/*     */       return; 
/*  35 */     for (Entity entity : this.minecraft.level.entitiesForRendering()) {
/*  36 */       if (entity.isInvisible() || !frustum.isVisible(entity.getBoundingBox()))
/*     */         continue; 
/*  40 */       if (!this.minecraft.levelExtractor.isEntityVisible(entity, frustum, camX, camY, camZ))
/*     */         continue; 
/*  43 */       if (entity == this.minecraft.getCameraEntity() && this.minecraft.options.getCameraType() == CameraType.FIRST_PERSON)
/*     */         continue; 
/*  50 */       float entityPartialTicks = this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(
/*  51 */           !this.minecraft.level.tickRateManager().isEntityFrozen(entity));
/*  54 */       showHitboxes(entity, entityPartialTicks, false);
/*  56 */       if (SharedConstants.DEBUG_SHOW_LOCAL_SERVER_ENTITY_HIT_BOXES) {
/*  57 */         Entity serverEntity = getServerEntity(entity);
/*  58 */         if (serverEntity != null) {
/*  59 */           showHitboxes(entity, entityPartialTicks, true);
/*     */           continue;
/*     */         } 
/*  61 */         Gizmos.billboardText("Missing Server Entity", entity.getPosition(entityPartialTicks).add(0.0D, entity.getBoundingBox().getYsize() + 1.5D, 0.0D), TextGizmo.Style.forColorAndCentered(-65536));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Entity getServerEntity(Entity entity) {
/*  68 */     IntegratedServer server = this.minecraft.getSingleplayerServer();
/*  69 */     if (server != null) {
/*  70 */       ServerLevel level = server.getLevel(entity.level().dimension());
/*  71 */       if (level != null)
/*  72 */         return level.getEntity(entity.getId()); 
/*     */     } 
/*  75 */     return null;
/*     */   }
/*     */   
/*     */   private void showHitboxes(Entity entity, float partialTicks, boolean isServerEntity) {
/*  81 */     Vec3 latestPosition = entity.position();
/*  82 */     Vec3 currentPosition = entity.getPosition(partialTicks);
/*  83 */     Vec3 offset = currentPosition.subtract(latestPosition);
/*  86 */     int mainColor = isServerEntity ? -16711936 : -1;
/*  87 */     Gizmos.cuboid(entity.getBoundingBox().move(offset), GizmoStyle.stroke(mainColor));
/*  88 */     Gizmos.point(currentPosition, mainColor, 2.0F);
/*  91 */     Entity vehicle = entity.getVehicle();
/*  92 */     if (vehicle != null) {
/*  93 */       float width = Math.min(vehicle.getBbWidth(), entity.getBbWidth()) / 2.0F;
/*  94 */       float height = 0.0625F;
/*  95 */       Vec3 position = vehicle.getPassengerRidingPosition(entity).add(offset);
/*  96 */       Gizmos.cuboid(new AABB(position.x - width, position.y, position.z - width, position.x + width, position.y + 0.0625D, position.z + width), 
/*     */           
/* 104 */           GizmoStyle.stroke(-256));
/*     */     } 
/* 109 */     if (entity instanceof net.minecraft.world.entity.LivingEntity) {
/* 110 */       AABB bb = entity.getBoundingBox().move(offset);
/* 111 */       float padding = 0.01F;
/* 112 */       Gizmos.cuboid(new AABB(bb.minX, bb.minY + entity
/*     */             
/* 114 */             .getEyeHeight() - 0.009999999776482582D, bb.minZ, bb.maxX, bb.minY + entity
/*     */             
/* 117 */             .getEyeHeight() + 0.009999999776482582D, bb.maxZ), 
/*     */           
/* 120 */           GizmoStyle.stroke(-65536));
/*     */     } 
/* 125 */     if (entity instanceof EnderDragon) {
/* 125 */       EnderDragon dragon = (EnderDragon)entity;
/* 126 */       for (EnderDragonPart subEntity : dragon.getSubEntities()) {
/* 127 */         Vec3 latestSubPosition = subEntity.position();
/* 128 */         Vec3 currentSubPosition = subEntity.getPosition(partialTicks);
/* 129 */         Vec3 subOffset = currentSubPosition.subtract(latestSubPosition);
/* 130 */         Gizmos.cuboid(subEntity
/* 131 */             .getBoundingBox().move(subOffset), 
/* 132 */             GizmoStyle.stroke(ARGB.colorFromFloat(1.0F, 0.25F, 1.0F, 0.0F)));
/*     */       } 
/*     */     } 
/* 138 */     Vec3 eyePosition = currentPosition.add(0.0D, entity.getEyeHeight(), 0.0D);
/* 139 */     Vec3 viewVector = entity.getViewVector(partialTicks);
/* 140 */     Gizmos.arrow(eyePosition, eyePosition.add(viewVector.scale(2.0D)), -16776961);
/* 143 */     if (isServerEntity) {
/* 144 */       Vec3 deltaMovement = entity.getDeltaMovement();
/* 145 */       Gizmos.arrow(currentPosition, currentPosition.add(deltaMovement), -256);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\debug\EntityHitboxDebugRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */