/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ class EmptyWaypoint extends TrackedWaypoint {
/*     */   private EmptyWaypoint(Either<UUID, String> identifier, Waypoint.Icon icon, FriendlyByteBuf byteBuf) {
/* 122 */     super(identifier, icon, TrackedWaypoint.Type.EMPTY);
/*     */   }
/*     */   
/*     */   private EmptyWaypoint(UUID identifier) {
/* 126 */     super(Either.left(identifier), Waypoint.Icon.NULL, TrackedWaypoint.Type.EMPTY);
/*     */   }
/*     */   
/*     */   public void update(TrackedWaypoint other) {}
/*     */   
/*     */   public void writeContents(ByteBuf buf) {}
/*     */   
/*     */   public double yawAngleToCamera(Level level, TrackedWaypoint.Camera camera, PartialTickSupplier partialTickSupplier) {
/* 139 */     return Double.NaN;
/*     */   }
/*     */   
/*     */   public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level level, TrackedWaypoint.Projector projector, PartialTickSupplier partialTickSupplier) {
/* 144 */     return TrackedWaypoint.PitchDirection.NONE;
/*     */   }
/*     */   
/*     */   public double distanceSquared(Entity fromEntity) {
/* 149 */     return Double.POSITIVE_INFINITY;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\waypoints\TrackedWaypoint$EmptyWaypoint.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */