/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.codec.StreamEncoder;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.function.TriFunction;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class TrackedWaypoint implements Waypoint {
/*  22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  24 */   public static final StreamCodec<ByteBuf, TrackedWaypoint> STREAM_CODEC = StreamCodec.ofMember(TrackedWaypoint::write, TrackedWaypoint::read);
/*     */   
/*     */   protected final Either<UUID, String> identifier;
/*     */   
/*     */   private final Waypoint.Icon icon;
/*     */   
/*     */   private final Type type;
/*     */   
/*     */   private TrackedWaypoint(Either<UUID, String> identifier, Waypoint.Icon icon, Type type) {
/*  31 */     this.identifier = identifier;
/*  32 */     this.icon = icon;
/*  33 */     this.type = type;
/*     */   }
/*     */   
/*     */   public Either<UUID, String> id() {
/*  37 */     return this.identifier;
/*     */   }
/*     */   
/*     */   public abstract void update(TrackedWaypoint paramTrackedWaypoint);
/*     */   
/*     */   public void write(ByteBuf buf) {
/*  43 */     FriendlyByteBuf byteBuf = new FriendlyByteBuf(buf);
/*  44 */     byteBuf.writeEither(this.identifier, (StreamEncoder)UUIDUtil.STREAM_CODEC, FriendlyByteBuf::writeUtf);
/*  45 */     Waypoint.Icon.STREAM_CODEC.encode(byteBuf, this.icon);
/*  46 */     byteBuf.writeEnum(this.type);
/*  47 */     writeContents(buf);
/*     */   }
/*     */   
/*     */   public abstract void writeContents(ByteBuf paramByteBuf);
/*     */   
/*     */   private static TrackedWaypoint read(ByteBuf buf) {
/*  53 */     FriendlyByteBuf byteBuf = new FriendlyByteBuf(buf);
/*  55 */     Either<UUID, String> identifier = byteBuf.readEither((StreamDecoder)UUIDUtil.STREAM_CODEC, FriendlyByteBuf::readUtf);
/*  56 */     Waypoint.Icon icon = (Waypoint.Icon)Waypoint.Icon.STREAM_CODEC.decode(byteBuf);
/*  57 */     Type type = (Type)byteBuf.readEnum(Type.class);
/*  59 */     return (TrackedWaypoint)type.constructor.apply(identifier, icon, byteBuf);
/*     */   }
/*     */   
/*     */   public static TrackedWaypoint setPosition(UUID identifier, Waypoint.Icon icon, Vec3i position) {
/*  63 */     return new Vec3iWaypoint(identifier, icon, position);
/*     */   }
/*     */   
/*     */   public static TrackedWaypoint setChunk(UUID identifier, Waypoint.Icon icon, ChunkPos chunk) {
/*  67 */     return new ChunkWaypoint(identifier, icon, chunk);
/*     */   }
/*     */   
/*     */   public static TrackedWaypoint setAzimuth(UUID identifier, Waypoint.Icon icon, float angle) {
/*  71 */     return new AzimuthWaypoint(identifier, icon, angle);
/*     */   }
/*     */   
/*     */   public static TrackedWaypoint empty(UUID identifier) {
/*  75 */     return new EmptyWaypoint(identifier);
/*     */   }
/*     */   
/*     */   public abstract double yawAngleToCamera(Level paramLevel, Camera paramCamera, PartialTickSupplier paramPartialTickSupplier);
/*     */   
/*     */   public abstract PitchDirection pitchDirectionToCamera(Level paramLevel, Projector paramProjector, PartialTickSupplier paramPartialTickSupplier);
/*     */   
/*     */   public abstract double distanceSquared(Entity paramEntity);
/*     */   
/*     */   public Waypoint.Icon icon() {
/*  85 */     return this.icon;
/*     */   }
/*     */   
/*     */   public enum PitchDirection {
/*  89 */     NONE, UP, DOWN;
/*     */   }
/*     */   
/*     */   private enum Type {
/* 107 */     EMPTY((String)EmptyWaypoint::new),
/* 108 */     VEC3I((String)Vec3iWaypoint::new),
/* 109 */     CHUNK((String)ChunkWaypoint::new),
/* 110 */     AZIMUTH((String)AzimuthWaypoint::new);
/*     */     
/*     */     private final TriFunction<Either<UUID, String>, Waypoint.Icon, FriendlyByteBuf, TrackedWaypoint> constructor;
/*     */     
/*     */     Type(TriFunction<Either<UUID, String>, Waypoint.Icon, FriendlyByteBuf, TrackedWaypoint> constructor) {
/* 116 */       this.constructor = constructor;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EmptyWaypoint extends TrackedWaypoint {
/*     */     private EmptyWaypoint(Either<UUID, String> identifier, Waypoint.Icon icon, FriendlyByteBuf byteBuf) {
/* 122 */       super(identifier, icon, TrackedWaypoint.Type.EMPTY);
/*     */     }
/*     */     
/*     */     private EmptyWaypoint(UUID identifier) {
/* 126 */       super(Either.left(identifier), Waypoint.Icon.NULL, TrackedWaypoint.Type.EMPTY);
/*     */     }
/*     */     
/*     */     public void update(TrackedWaypoint other) {}
/*     */     
/*     */     public void writeContents(ByteBuf buf) {}
/*     */     
/*     */     public double yawAngleToCamera(Level level, TrackedWaypoint.Camera camera, PartialTickSupplier partialTickSupplier) {
/* 139 */       return Double.NaN;
/*     */     }
/*     */     
/*     */     public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level level, TrackedWaypoint.Projector projector, PartialTickSupplier partialTickSupplier) {
/* 144 */       return TrackedWaypoint.PitchDirection.NONE;
/*     */     }
/*     */     
/*     */     public double distanceSquared(Entity fromEntity) {
/* 149 */       return Double.POSITIVE_INFINITY;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Vec3iWaypoint extends TrackedWaypoint {
/*     */     private Vec3i vector;
/*     */     
/*     */     public Vec3iWaypoint(UUID identifier, Waypoint.Icon icon, Vec3i vector) {
/* 157 */       super(Either.left(identifier), icon, TrackedWaypoint.Type.VEC3I);
/* 158 */       this.vector = vector;
/*     */     }
/*     */     
/*     */     public Vec3iWaypoint(Either<UUID, String> identifier, Waypoint.Icon icon, FriendlyByteBuf byteBuf) {
/* 162 */       super(identifier, icon, TrackedWaypoint.Type.VEC3I);
/* 163 */       this
/*     */         
/* 166 */         .vector = new Vec3i(byteBuf.readVarInt(), byteBuf.readVarInt(), byteBuf.readVarInt());
/*     */     }
/*     */     
/*     */     public void update(TrackedWaypoint other) {
/* 172 */       if (other instanceof Vec3iWaypoint) {
/* 172 */         Vec3iWaypoint vec3iWaypoint = (Vec3iWaypoint)other;
/* 173 */         this.vector = vec3iWaypoint.vector;
/*     */       } else {
/* 175 */         TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass());
/*     */       } 
/*     */     }
/*     */     
/*     */     public void writeContents(ByteBuf buf) {
/* 181 */       VarInt.write(buf, this.vector.getX());
/* 182 */       VarInt.write(buf, this.vector.getY());
/* 183 */       VarInt.write(buf, this.vector.getZ());
/*     */     }
/*     */     
/*     */     private Vec3 position(Level level, PartialTickSupplier partialTick) {
/* 189 */       Objects.requireNonNull(level);
/* 189 */       return this.identifier.left().map(level::getEntity).map(e -> (e.blockPosition().distManhattan(this.vector) > 3) ? null : e.getEyePosition(partialTick.apply(e)))
/*     */         
/* 195 */         .orElseGet(() -> Vec3.atCenterOf(this.vector));
/*     */     }
/*     */     
/*     */     public double yawAngleToCamera(Level level, TrackedWaypoint.Camera camera, PartialTickSupplier partialTickSupplier) {
/* 200 */       Vec3 direction = camera.position().subtract(position(level, partialTickSupplier)).rotateClockwise90();
/* 201 */       float waypointAngle = (float)Mth.atan2(direction.z(), direction.x()) * 57.295776F;
/* 202 */       return Mth.degreesDifference(camera.yaw(), waypointAngle);
/*     */     }
/*     */     
/*     */     public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level level, TrackedWaypoint.Projector projector, PartialTickSupplier partialTickSupplier) {
/* 207 */       Vec3 pointOnScreen = projector.projectPointToScreen(position(level, partialTickSupplier));
/* 208 */       boolean isBehindCamera = (pointOnScreen.z > 1.0D);
/* 210 */       double yInFrontOfCamera = isBehindCamera ? -pointOnScreen.y : pointOnScreen.y;
/* 211 */       if (yInFrontOfCamera < -1.0D)
/* 212 */         return TrackedWaypoint.PitchDirection.DOWN; 
/* 214 */       if (yInFrontOfCamera > 1.0D)
/* 215 */         return TrackedWaypoint.PitchDirection.UP; 
/* 217 */       if (isBehindCamera) {
/* 218 */         if (pointOnScreen.y > 0.0D)
/* 219 */           return TrackedWaypoint.PitchDirection.UP; 
/* 221 */         if (pointOnScreen.y < 0.0D)
/* 222 */           return TrackedWaypoint.PitchDirection.DOWN; 
/*     */       } 
/* 225 */       return TrackedWaypoint.PitchDirection.NONE;
/*     */     }
/*     */     
/*     */     public double distanceSquared(Entity fromEntity) {
/* 230 */       return fromEntity.distanceToSqr(Vec3.atCenterOf(this.vector));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ChunkWaypoint extends TrackedWaypoint {
/*     */     private ChunkPos chunkPos;
/*     */     
/*     */     public ChunkWaypoint(UUID identifier, Waypoint.Icon icon, ChunkPos chunkPos) {
/* 238 */       super(Either.left(identifier), icon, TrackedWaypoint.Type.CHUNK);
/* 239 */       this.chunkPos = chunkPos;
/*     */     }
/*     */     
/*     */     public ChunkWaypoint(Either<UUID, String> identifier, Waypoint.Icon icon, FriendlyByteBuf byteBuf) {
/* 243 */       super(identifier, icon, TrackedWaypoint.Type.CHUNK);
/* 244 */       this
/*     */         
/* 246 */         .chunkPos = new ChunkPos(byteBuf.readVarInt(), byteBuf.readVarInt());
/*     */     }
/*     */     
/*     */     public void update(TrackedWaypoint other) {
/* 252 */       if (other instanceof ChunkWaypoint) {
/* 252 */         ChunkWaypoint chunkWaypoint = (ChunkWaypoint)other;
/* 253 */         this.chunkPos = chunkWaypoint.chunkPos;
/*     */       } else {
/* 255 */         TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass());
/*     */       } 
/*     */     }
/*     */     
/*     */     public void writeContents(ByteBuf buf) {
/* 261 */       VarInt.write(buf, this.chunkPos.x());
/* 262 */       VarInt.write(buf, this.chunkPos.z());
/*     */     }
/*     */     
/*     */     private Vec3 position(double positionY) {
/* 267 */       return Vec3.atCenterOf((Vec3i)this.chunkPos.getMiddleBlockPosition((int)positionY));
/*     */     }
/*     */     
/*     */     public double yawAngleToCamera(Level level, TrackedWaypoint.Camera camera, PartialTickSupplier partialTickSupplier) {
/* 272 */       Vec3 cameraPosition = camera.position();
/* 273 */       Vec3 direction = cameraPosition.subtract(position(cameraPosition.y())).rotateClockwise90();
/* 274 */       float waypointAngle = (float)Mth.atan2(direction.z(), direction.x()) * 57.295776F;
/* 275 */       return Mth.degreesDifference(camera.yaw(), waypointAngle);
/*     */     }
/*     */     
/*     */     public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level level, TrackedWaypoint.Projector projector, PartialTickSupplier partialTickSupplier) {
/* 280 */       double onScreenHorizon = projector.projectHorizonToScreen();
/* 281 */       if (onScreenHorizon < -1.0D)
/* 282 */         return TrackedWaypoint.PitchDirection.DOWN; 
/* 284 */       if (onScreenHorizon > 1.0D)
/* 285 */         return TrackedWaypoint.PitchDirection.UP; 
/* 287 */       return TrackedWaypoint.PitchDirection.NONE;
/*     */     }
/*     */     
/*     */     public double distanceSquared(Entity fromEntity) {
/* 292 */       return fromEntity.distanceToSqr(Vec3.atCenterOf((Vec3i)this.chunkPos.getMiddleBlockPosition(fromEntity.getBlockY())));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class AzimuthWaypoint extends TrackedWaypoint {
/*     */     private float angle;
/*     */     
/*     */     public AzimuthWaypoint(UUID identifier, Waypoint.Icon icon, float angle) {
/* 300 */       super(Either.left(identifier), icon, TrackedWaypoint.Type.AZIMUTH);
/* 301 */       this.angle = angle;
/*     */     }
/*     */     
/*     */     public AzimuthWaypoint(Either<UUID, String> identifier, Waypoint.Icon icon, FriendlyByteBuf byteBuf) {
/* 305 */       super(identifier, icon, TrackedWaypoint.Type.AZIMUTH);
/* 306 */       this.angle = byteBuf.readFloat();
/*     */     }
/*     */     
/*     */     public void update(TrackedWaypoint other) {
/* 311 */       if (other instanceof AzimuthWaypoint) {
/* 311 */         AzimuthWaypoint azimuthWaypoint = (AzimuthWaypoint)other;
/* 312 */         this.angle = azimuthWaypoint.angle;
/*     */       } else {
/* 314 */         TrackedWaypoint.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass());
/*     */       } 
/*     */     }
/*     */     
/*     */     public void writeContents(ByteBuf buf) {
/* 320 */       buf.writeFloat(this.angle);
/*     */     }
/*     */     
/*     */     public double yawAngleToCamera(Level level, TrackedWaypoint.Camera camera, PartialTickSupplier partialTickSupplier) {
/* 325 */       return Mth.degreesDifference(camera.yaw(), this.angle * 57.295776F);
/*     */     }
/*     */     
/*     */     public TrackedWaypoint.PitchDirection pitchDirectionToCamera(Level level, TrackedWaypoint.Projector projector, PartialTickSupplier partialTickSupplier) {
/* 330 */       double horizon = projector.projectHorizonToScreen();
/* 331 */       if (horizon < -1.0D)
/* 332 */         return TrackedWaypoint.PitchDirection.DOWN; 
/* 334 */       if (horizon > 1.0D)
/* 335 */         return TrackedWaypoint.PitchDirection.UP; 
/* 337 */       return TrackedWaypoint.PitchDirection.NONE;
/*     */     }
/*     */     
/*     */     public double distanceSquared(Entity fromEntity) {
/* 342 */       return Double.POSITIVE_INFINITY;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Camera {
/*     */     float yaw();
/*     */     
/*     */     Vec3 position();
/*     */   }
/*     */   
/*     */   public static interface Projector {
/*     */     Vec3 projectPointToScreen(Vec3 param1Vec3);
/*     */     
/*     */     double projectHorizonToScreen();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\waypoints\TrackedWaypoint.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */