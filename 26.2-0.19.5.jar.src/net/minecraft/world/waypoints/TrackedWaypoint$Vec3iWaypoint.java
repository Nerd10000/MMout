/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class Vec3iWaypoint extends TrackedWaypoint {
/*     */   private Vec3i vector;
/*     */   
/*     */   public Vec3iWaypoint(UUID identifier, Waypoint.Icon icon, Vec3i vector) {
/* 157 */     super(Either.left(identifier), icon, TrackedWaypoint.Type.VEC3I);
/* 158 */     this.vector = vector;
/*     */   }
/*     */   
/*     */   public Vec3iWaypoint(Either<UUID, String> identifier, Waypoint.Icon icon, FriendlyByteBuf byteBuf) {
/* 162 */     super(identifier, icon, TrackedWaypoint.Type.VEC3I);
/* 163 */     this
/*     */       
/* 166 */       .vector = new Vec3i(byteBuf.readVarInt(), byteBuf.readVarInt(), byteBuf.readVarInt());
/*     */   }
/*     */   
/*     */   public void update(TrackedWaypoint other) {
/* 172 */     if (other instanceof Vec3iWaypoint) {
/* 172 */       Vec3iWaypoint vec3iWaypoint = (Vec3iWaypoint)other;
/* 173 */       this.vector = vec3iWaypoint.vector;
/*     */     } else {
/* 175 */       TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void writeContents(ByteBuf buf) {
/* 181 */     VarInt.write(buf, this.vector.getX());
/* 182 */     VarInt.write(buf, this.vector.getY());
/* 183 */     VarInt.write(buf, this.vector.getZ());
/*     */   }
/*     */   
/*     */   private Vec3 position(Level level, PartialTickSupplier partialTick) {
/* 189 */     Objects.requireNonNull(level);
/* 189 */     return this.identifier.left().map(level::getEntity).map(e -> (e.blockPosition().distManhattan(this.vector) > 3) ? null : e.getEyePosition(partialTick.apply(e)))
/*     */       
/* 195 */       .orElseGet(() -> Vec3.atCenterOf(this.vector));
/*     */   }
/*     */   
/*     */   public double yawAngleToCamera(Level level, TrackedWaypoint.Camera camera, PartialTickSupplier partialTickSupplier) {
/* 200 */     Vec3 direction = camera.position().subtract(position(level, partialTickSupplier)).rotateClockwise90();
/* 201 */     float waypointAngle = (float)Mth.atan2(direction.z(), direction.x()) * 57.295776F;
/* 202 */     return Mth.degreesDifference(camera.yaw(), waypointAngle);
/*     */   }
/*     */   
/*     */   public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level level, TrackedWaypoint.Projector projector, PartialTickSupplier partialTickSupplier) {
/* 207 */     Vec3 pointOnScreen = projector.projectPointToScreen(position(level, partialTickSupplier));
/* 208 */     boolean isBehindCamera = (pointOnScreen.z > 1.0D);
/* 210 */     double yInFrontOfCamera = isBehindCamera ? -pointOnScreen.y : pointOnScreen.y;
/* 211 */     if (yInFrontOfCamera < -1.0D)
/* 212 */       return TrackedWaypoint.PitchDirection.DOWN; 
/* 214 */     if (yInFrontOfCamera > 1.0D)
/* 215 */       return TrackedWaypoint.PitchDirection.UP; 
/* 217 */     if (isBehindCamera) {
/* 218 */       if (pointOnScreen.y > 0.0D)
/* 219 */         return TrackedWaypoint.PitchDirection.UP; 
/* 221 */       if (pointOnScreen.y < 0.0D)
/* 222 */         return TrackedWaypoint.PitchDirection.DOWN; 
/*     */     } 
/* 225 */     return TrackedWaypoint.PitchDirection.NONE;
/*     */   }
/*     */   
/*     */   public double distanceSquared(Entity fromEntity) {
/* 230 */     return fromEntity.distanceToSqr(Vec3.atCenterOf(this.vector));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\waypoints\TrackedWaypoint$Vec3iWaypoint.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */