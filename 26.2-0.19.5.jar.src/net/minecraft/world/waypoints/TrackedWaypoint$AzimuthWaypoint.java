/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ class AzimuthWaypoint extends TrackedWaypoint {
/*     */   private float angle;
/*     */   
/*     */   public AzimuthWaypoint(UUID identifier, Waypoint.Icon icon, float angle) {
/* 300 */     super(Either.left(identifier), icon, TrackedWaypoint.Type.AZIMUTH);
/* 301 */     this.angle = angle;
/*     */   }
/*     */   
/*     */   public AzimuthWaypoint(Either<UUID, String> identifier, Waypoint.Icon icon, FriendlyByteBuf byteBuf) {
/* 305 */     super(identifier, icon, TrackedWaypoint.Type.AZIMUTH);
/* 306 */     this.angle = byteBuf.readFloat();
/*     */   }
/*     */   
/*     */   public void update(TrackedWaypoint other) {
/* 311 */     if (other instanceof AzimuthWaypoint) {
/* 311 */       AzimuthWaypoint azimuthWaypoint = (AzimuthWaypoint)other;
/* 312 */       this.angle = azimuthWaypoint.angle;
/*     */     } else {
/* 314 */       TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void writeContents(ByteBuf buf) {
/* 320 */     buf.writeFloat(this.angle);
/*     */   }
/*     */   
/*     */   public double yawAngleToCamera(Level level, TrackedWaypoint.Camera camera, PartialTickSupplier partialTickSupplier) {
/* 325 */     return Mth.degreesDifference(camera.yaw(), this.angle * 57.295776F);
/*     */   }
/*     */   
/*     */   public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level level, TrackedWaypoint.Projector projector, PartialTickSupplier partialTickSupplier) {
/* 330 */     double horizon = projector.projectHorizonToScreen();
/* 331 */     if (horizon < -1.0D)
/* 332 */       return TrackedWaypoint.PitchDirection.DOWN; 
/* 334 */     if (horizon > 1.0D)
/* 335 */       return TrackedWaypoint.PitchDirection.UP; 
/* 337 */     return TrackedWaypoint.PitchDirection.NONE;
/*     */   }
/*     */   
/*     */   public double distanceSquared(Entity fromEntity) {
/* 342 */     return Double.POSITIVE_INFINITY;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\waypoints\TrackedWaypoint$AzimuthWaypoint.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */