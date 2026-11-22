/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class ChunkWaypoint extends TrackedWaypoint {
/*     */   private ChunkPos chunkPos;
/*     */   
/*     */   public ChunkWaypoint(UUID identifier, Waypoint.Icon icon, ChunkPos chunkPos) {
/* 238 */     super(Either.left(identifier), icon, TrackedWaypoint.Type.CHUNK);
/* 239 */     this.chunkPos = chunkPos;
/*     */   }
/*     */   
/*     */   public ChunkWaypoint(Either<UUID, String> identifier, Waypoint.Icon icon, FriendlyByteBuf byteBuf) {
/* 243 */     super(identifier, icon, TrackedWaypoint.Type.CHUNK);
/* 244 */     this
/*     */       
/* 246 */       .chunkPos = new ChunkPos(byteBuf.readVarInt(), byteBuf.readVarInt());
/*     */   }
/*     */   
/*     */   public void update(TrackedWaypoint other) {
/* 252 */     if (other instanceof ChunkWaypoint) {
/* 252 */       ChunkWaypoint chunkWaypoint = (ChunkWaypoint)other;
/* 253 */       this.chunkPos = chunkWaypoint.chunkPos;
/*     */     } else {
/* 255 */       TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void writeContents(ByteBuf buf) {
/* 261 */     VarInt.write(buf, this.chunkPos.x());
/* 262 */     VarInt.write(buf, this.chunkPos.z());
/*     */   }
/*     */   
/*     */   private Vec3 position(double positionY) {
/* 267 */     return Vec3.atCenterOf((Vec3i)this.chunkPos.getMiddleBlockPosition((int)positionY));
/*     */   }
/*     */   
/*     */   public double yawAngleToCamera(Level level, TrackedWaypoint.Camera camera, PartialTickSupplier partialTickSupplier) {
/* 272 */     Vec3 cameraPosition = camera.position();
/* 273 */     Vec3 direction = cameraPosition.subtract(position(cameraPosition.y())).rotateClockwise90();
/* 274 */     float waypointAngle = (float)Mth.atan2(direction.z(), direction.x()) * 57.295776F;
/* 275 */     return Mth.degreesDifference(camera.yaw(), waypointAngle);
/*     */   }
/*     */   
/*     */   public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level level, TrackedWaypoint.Projector projector, PartialTickSupplier partialTickSupplier) {
/* 280 */     double onScreenHorizon = projector.projectHorizonToScreen();
/* 281 */     if (onScreenHorizon < -1.0D)
/* 282 */       return TrackedWaypoint.PitchDirection.DOWN; 
/* 284 */     if (onScreenHorizon > 1.0D)
/* 285 */       return TrackedWaypoint.PitchDirection.UP; 
/* 287 */     return TrackedWaypoint.PitchDirection.NONE;
/*     */   }
/*     */   
/*     */   public double distanceSquared(Entity fromEntity) {
/* 292 */     return fromEntity.distanceToSqr(Vec3.atCenterOf((Vec3i)this.chunkPos.getMiddleBlockPosition(fromEntity.getBlockY())));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\waypoints\TrackedWaypoint$ChunkWaypoint.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */