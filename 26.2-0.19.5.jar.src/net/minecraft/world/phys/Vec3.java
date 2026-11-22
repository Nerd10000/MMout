/*     */ package net.minecraft.world.phys;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.LpVec3;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class Vec3 implements Position {
/*     */   public static final Codec<Vec3> CODEC;
/*     */   
/*     */   static {
/*  20 */     CODEC = Codec.DOUBLE.listOf().comapFlatMap(input -> Util.fixedSize(input, 3).map(()), pos -> List.of(Double.valueOf(pos.x()), Double.valueOf(pos.y()), Double.valueOf(pos.z())));
/*     */   }
/*     */   
/*  25 */   public static final StreamCodec<ByteBuf, Vec3> STREAM_CODEC = new StreamCodec<ByteBuf, Vec3>() {
/*     */       public Vec3 decode(ByteBuf input) {
/*  28 */         return new Vec3(input.readDouble(), input.readDouble(), input.readDouble());
/*     */       }
/*     */       
/*     */       public void encode(ByteBuf output, Vec3 value) {
/*  33 */         output.writeDouble(value.x());
/*  34 */         output.writeDouble(value.y());
/*  35 */         output.writeDouble(value.z());
/*     */       }
/*     */     };
/*     */   
/*  39 */   public static final StreamCodec<ByteBuf, Vec3> LP_STREAM_CODEC = StreamCodec.of(LpVec3::write, LpVec3::read);
/*     */   
/*  41 */   public static final Vec3 ZERO = new Vec3(0.0D, 0.0D, 0.0D);
/*     */   
/*  42 */   public static final Vec3 X_AXIS = new Vec3(1.0D, 0.0D, 0.0D);
/*     */   
/*  43 */   public static final Vec3 Y_AXIS = new Vec3(0.0D, 1.0D, 0.0D);
/*     */   
/*  44 */   public static final Vec3 Z_AXIS = new Vec3(0.0D, 0.0D, 1.0D);
/*     */   
/*     */   public final double x;
/*     */   
/*     */   public final double y;
/*     */   
/*     */   public final double z;
/*     */   
/*     */   public static Vec3 atLowerCornerOf(Vec3i pos) {
/*  50 */     return new Vec3(pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */   
/*     */   public static Vec3 atLowerCornerWithOffset(Vec3i pos, double x, double y, double z) {
/*  54 */     return new Vec3(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
/*     */   }
/*     */   
/*     */   public static Vec3 atCenterOf(Vec3i pos) {
/*  58 */     return atLowerCornerWithOffset(pos, 0.5D, 0.5D, 0.5D);
/*     */   }
/*     */   
/*     */   public static Vec3 atBottomCenterOf(Vec3i pos) {
/*  62 */     return atLowerCornerWithOffset(pos, 0.5D, 0.0D, 0.5D);
/*     */   }
/*     */   
/*     */   public static Vec3 upFromBottomCenterOf(Vec3i pos, double yOffset) {
/*  66 */     return atLowerCornerWithOffset(pos, 0.5D, yOffset, 0.5D);
/*     */   }
/*     */   
/*     */   public Vec3(double x, double y, double z) {
/*  70 */     this.x = x;
/*  71 */     this.y = y;
/*  72 */     this.z = z;
/*     */   }
/*     */   
/*     */   public Vec3(Vector3fc vec) {
/*  76 */     this(vec.x(), vec.y(), vec.z());
/*     */   }
/*     */   
/*     */   public Vec3(Vec3i vec) {
/*  80 */     this(vec.getX(), vec.getY(), vec.getZ());
/*     */   }
/*     */   
/*     */   public Vec3 vectorTo(Vec3 vec) {
/*  84 */     return new Vec3(vec.x - this.x, vec.y - this.y, vec.z - this.z);
/*     */   }
/*     */   
/*     */   public Vec3 normalize() {
/*  88 */     double dist = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
/*  89 */     if (dist < 9.999999747378752E-6D)
/*  90 */       return ZERO; 
/*  92 */     return new Vec3(this.x / dist, this.y / dist, this.z / dist);
/*     */   }
/*     */   
/*     */   public double dot(Vec3 vec) {
/*  96 */     return this.x * vec.x + this.y * vec.y + this.z * vec.z;
/*     */   }
/*     */   
/*     */   public Vec3 cross(Vec3 vec) {
/* 100 */     return new Vec3(this.y * vec.z - this.z * vec.y, this.z * vec.x - this.x * vec.z, this.x * vec.y - this.y * vec.x);
/*     */   }
/*     */   
/*     */   public Vec3 subtract(Vec3 vec) {
/* 104 */     return subtract(vec.x, vec.y, vec.z);
/*     */   }
/*     */   
/*     */   public Vec3 subtract(double value) {
/* 108 */     return subtract(value, value, value);
/*     */   }
/*     */   
/*     */   public Vec3 subtract(double x, double y, double z) {
/* 112 */     return add(-x, -y, -z);
/*     */   }
/*     */   
/*     */   public Vec3 add(double value) {
/* 116 */     return add(value, value, value);
/*     */   }
/*     */   
/*     */   public Vec3 add(Vec3 vec) {
/* 120 */     return add(vec.x, vec.y, vec.z);
/*     */   }
/*     */   
/*     */   public Vec3 add(double x, double y, double z) {
/* 124 */     return new Vec3(this.x + x, this.y + y, this.z + z);
/*     */   }
/*     */   
/*     */   public boolean closerThan(Position pos, double distance) {
/* 128 */     return (distanceToSqr(pos.x(), pos.y(), pos.z()) < distance * distance);
/*     */   }
/*     */   
/*     */   public double distanceTo(Vec3 vec) {
/* 132 */     double xd = vec.x - this.x;
/* 133 */     double yd = vec.y - this.y;
/* 134 */     double zd = vec.z - this.z;
/* 135 */     return Math.sqrt(xd * xd + yd * yd + zd * zd);
/*     */   }
/*     */   
/*     */   public double distanceToSqr(Vec3 vec) {
/* 139 */     double xd = vec.x - this.x;
/* 140 */     double yd = vec.y - this.y;
/* 141 */     double zd = vec.z - this.z;
/* 142 */     return xd * xd + yd * yd + zd * zd;
/*     */   }
/*     */   
/*     */   public double distanceToSqr(double x, double y, double z) {
/* 146 */     double xd = x - this.x;
/* 147 */     double yd = y - this.y;
/* 148 */     double zd = z - this.z;
/* 149 */     return xd * xd + yd * yd + zd * zd;
/*     */   }
/*     */   
/*     */   public boolean closerThan(Vec3 vec, double distanceXZ, double distanceY) {
/* 153 */     double dx = vec.x() - this.x;
/* 154 */     double dy = vec.y() - this.y;
/* 155 */     double dz = vec.z() - this.z;
/* 156 */     return (Mth.lengthSquared(dx, dz) < Mth.square(distanceXZ) && Math.abs(dy) < distanceY);
/*     */   }
/*     */   
/*     */   public Vec3 scale(double scale) {
/* 160 */     return multiply(scale, scale, scale);
/*     */   }
/*     */   
/*     */   public Vec3 reverse() {
/* 164 */     return scale(-1.0D);
/*     */   }
/*     */   
/*     */   public Vec3 multiply(Vec3 scale) {
/* 168 */     return multiply(scale.x, scale.y, scale.z);
/*     */   }
/*     */   
/*     */   public Vec3 multiply(double xScale, double yScale, double zScale) {
/* 172 */     return new Vec3(this.x * xScale, this.y * yScale, this.z * zScale);
/*     */   }
/*     */   
/*     */   public Vec3 horizontal() {
/* 176 */     return new Vec3(this.x, 0.0D, this.z);
/*     */   }
/*     */   
/*     */   public Vec3 offsetRandom(RandomSource random, float offset) {
/* 180 */     return add(((random.nextFloat() - 0.5F) * offset), ((random.nextFloat() - 0.5F) * offset), ((random.nextFloat() - 0.5F) * offset));
/*     */   }
/*     */   
/*     */   public Vec3 offsetRandomXZ(RandomSource random, float offset) {
/* 184 */     return add(((random.nextFloat() - 0.5F) * offset), 0.0D, ((random.nextFloat() - 0.5F) * offset));
/*     */   }
/*     */   
/*     */   public double length() {
/* 188 */     return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
/*     */   }
/*     */   
/*     */   public double lengthSqr() {
/* 192 */     return this.x * this.x + this.y * this.y + this.z * this.z;
/*     */   }
/*     */   
/*     */   public double horizontalDistance() {
/* 196 */     return Math.sqrt(this.x * this.x + this.z * this.z);
/*     */   }
/*     */   
/*     */   public double horizontalDistanceSqr() {
/* 200 */     return this.x * this.x + this.z * this.z;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*     */     Vec3 vec3;
/* 205 */     if (this == o)
/* 206 */       return true; 
/* 208 */     if (o instanceof Vec3) {
/* 208 */       vec3 = (Vec3)o;
/*     */     } else {
/* 209 */       return false;
/*     */     } 
/* 212 */     if (Double.compare(vec3.x, this.x) != 0)
/* 213 */       return false; 
/* 215 */     if (Double.compare(vec3.y, this.y) != 0)
/* 216 */       return false; 
/* 218 */     return (Double.compare(vec3.z, this.z) == 0);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 225 */     long temp = Double.doubleToLongBits(this.x);
/* 226 */     int result = (int)(temp ^ temp >>> 32L);
/* 227 */     temp = Double.doubleToLongBits(this.y);
/* 228 */     result = 31 * result + (int)(temp ^ temp >>> 32L);
/* 229 */     temp = Double.doubleToLongBits(this.z);
/* 230 */     result = 31 * result + (int)(temp ^ temp >>> 32L);
/* 231 */     return result;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 236 */     return "(" + this.x + ", " + this.y + ", " + this.z + ")";
/*     */   }
/*     */   
/*     */   public Vec3 lerp(Vec3 vec, double a) {
/* 240 */     return new Vec3(Mth.lerp(a, this.x, vec.x), Mth.lerp(a, this.y, vec.y), Mth.lerp(a, this.z, vec.z));
/*     */   }
/*     */   
/*     */   public Vec3 xRot(float radians) {
/* 244 */     float cos = Mth.cos(radians);
/* 245 */     float sin = Mth.sin(radians);
/* 247 */     double xx = this.x;
/* 248 */     double yy = this.y * cos + this.z * sin;
/* 249 */     double zz = this.z * cos - this.y * sin;
/* 251 */     return new Vec3(xx, yy, zz);
/*     */   }
/*     */   
/*     */   public Vec3 yRot(float radians) {
/* 255 */     float cos = Mth.cos(radians);
/* 256 */     float sin = Mth.sin(radians);
/* 258 */     double xx = this.x * cos + this.z * sin;
/* 259 */     double yy = this.y;
/* 260 */     double zz = this.z * cos - this.x * sin;
/* 262 */     return new Vec3(xx, yy, zz);
/*     */   }
/*     */   
/*     */   public Vec3 zRot(float radians) {
/* 266 */     float cos = Mth.cos(radians);
/* 267 */     float sin = Mth.sin(radians);
/* 269 */     double xx = this.x * cos + this.y * sin;
/* 270 */     double yy = this.y * cos - this.x * sin;
/* 271 */     double zz = this.z;
/* 273 */     return new Vec3(xx, yy, zz);
/*     */   }
/*     */   
/*     */   public Vec3 rotateClockwise90() {
/* 277 */     return new Vec3(-this.z, this.y, this.x);
/*     */   }
/*     */   
/*     */   public static Vec3 directionFromRotation(Vec2 rotation) {
/* 281 */     return directionFromRotation(rotation.x, rotation.y);
/*     */   }
/*     */   
/*     */   public static Vec3 directionFromRotation(float rotX, float rotY) {
/* 286 */     float yCos = Mth.cos((-rotY * 0.017453292F - 3.1415927F));
/* 287 */     float ySin = Mth.sin((-rotY * 0.017453292F - 3.1415927F));
/* 288 */     float xCos = -Mth.cos((-rotX * 0.017453292F));
/* 289 */     float xSin = Mth.sin((-rotX * 0.017453292F));
/* 291 */     return new Vec3((ySin * xCos), xSin, (yCos * xCos));
/*     */   }
/*     */   
/*     */   public Vec2 rotation() {
/* 295 */     float yaw = (float)Math.atan2(-this.x, this.z) * 57.295776F;
/* 296 */     float pitch = (float)Math.asin(-this.y / Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z)) * 57.295776F;
/* 297 */     return new Vec2(pitch, yaw);
/*     */   }
/*     */   
/*     */   public Vec3 align(EnumSet<Direction.Axis> axes) {
/* 301 */     double x = axes.contains(Direction.Axis.X) ? Mth.floor(this.x) : this.x;
/* 302 */     double y = axes.contains(Direction.Axis.Y) ? Mth.floor(this.y) : this.y;
/* 303 */     double z = axes.contains(Direction.Axis.Z) ? Mth.floor(this.z) : this.z;
/* 304 */     return new Vec3(x, y, z);
/*     */   }
/*     */   
/*     */   public double get(Direction.Axis axis) {
/* 308 */     return axis.choose(this.x, this.y, this.z);
/*     */   }
/*     */   
/*     */   public Vec3 with(Direction.Axis axis, double value) {
/* 312 */     double x = (axis == Direction.Axis.X) ? value : this.x;
/* 313 */     double y = (axis == Direction.Axis.Y) ? value : this.y;
/* 314 */     double z = (axis == Direction.Axis.Z) ? value : this.z;
/* 315 */     return new Vec3(x, y, z);
/*     */   }
/*     */   
/*     */   public Vec3 relative(Direction direction, double distance) {
/* 319 */     Vec3i normal = direction.getUnitVec3i();
/* 320 */     return new Vec3(this.x + distance * normal
/* 321 */         .getX(), this.y + distance * normal
/* 322 */         .getY(), this.z + distance * normal
/* 323 */         .getZ());
/*     */   }
/*     */   
/*     */   public final double x() {
/* 329 */     return this.x;
/*     */   }
/*     */   
/*     */   public final double y() {
/* 334 */     return this.y;
/*     */   }
/*     */   
/*     */   public final double z() {
/* 339 */     return this.z;
/*     */   }
/*     */   
/*     */   public Vector3f toVector3f() {
/* 343 */     return new Vector3f((float)this.x, (float)this.y, (float)this.z);
/*     */   }
/*     */   
/*     */   public Vec3 projectedOn(Vec3 onto) {
/* 347 */     if (onto.lengthSqr() == 0.0D)
/* 348 */       return onto; 
/* 350 */     return onto.scale(dot(onto)).scale(1.0D / onto.lengthSqr());
/*     */   }
/*     */   
/*     */   public static Vec3 applyLocalCoordinatesToRotation(Vec2 rotation, Vec3 direction) {
/* 354 */     float yCos = Mth.cos(((rotation.y + 90.0F) * 0.017453292F));
/* 355 */     float ySin = Mth.sin(((rotation.y + 90.0F) * 0.017453292F));
/* 356 */     float xCos = Mth.cos((-rotation.x * 0.017453292F));
/* 357 */     float xSin = Mth.sin((-rotation.x * 0.017453292F));
/* 358 */     float xCosUp = Mth.cos(((-rotation.x + 90.0F) * 0.017453292F));
/* 359 */     float xSinUp = Mth.sin(((-rotation.x + 90.0F) * 0.017453292F));
/* 360 */     Vec3 forwards = new Vec3((yCos * xCos), xSin, (ySin * xCos));
/* 361 */     Vec3 up = new Vec3((yCos * xCosUp), xSinUp, (ySin * xCosUp));
/* 362 */     Vec3 left = forwards.cross(up).scale(-1.0D);
/* 363 */     double xa = forwards.x * direction.z + up.x * direction.y + left.x * direction.x;
/* 364 */     double ya = forwards.y * direction.z + up.y * direction.y + left.y * direction.x;
/* 365 */     double za = forwards.z * direction.z + up.z * direction.y + left.z * direction.x;
/* 366 */     return new Vec3(xa, ya, za);
/*     */   }
/*     */   
/*     */   public Vec3 addLocalCoordinates(Vec3 direction) {
/* 370 */     return applyLocalCoordinatesToRotation(rotation(), direction);
/*     */   }
/*     */   
/*     */   public boolean isFinite() {
/* 374 */     return (Double.isFinite(this.x) && Double.isFinite(this.y) && Double.isFinite(this.z));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\Vec3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */