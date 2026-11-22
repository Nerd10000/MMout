/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import org.apache.commons.lang3.function.TriFunction;
/*     */ 
/*     */ enum Type {
/* 107 */   EMPTY(EmptyWaypoint::new),
/* 108 */   VEC3I(Vec3iWaypoint::new),
/* 109 */   CHUNK(ChunkWaypoint::new),
/* 110 */   AZIMUTH(AzimuthWaypoint::new);
/*     */   
/*     */   private final TriFunction<Either<UUID, String>, Waypoint.Icon, FriendlyByteBuf, TrackedWaypoint> constructor;
/*     */   
/*     */   Type(TriFunction<Either<UUID, String>, Waypoint.Icon, FriendlyByteBuf, TrackedWaypoint> constructor) {
/* 116 */     this.constructor = constructor;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\waypoints\TrackedWaypoint$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */