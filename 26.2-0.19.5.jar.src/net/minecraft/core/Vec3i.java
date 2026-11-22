/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.stream.IntStream;
/*     */ import javax.annotation.concurrent.Immutable;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import org.joml.Vector3i;
/*     */ 
/*     */ @Immutable
/*     */ public class Vec3i implements Comparable<Vec3i> {
/*     */   public static final Codec<Vec3i> CODEC;
/*     */   
/*     */   static {
/*  18 */     CODEC = Codec.INT_STREAM.comapFlatMap(input -> Util.fixedSize(input, 3).map(()), pos -> IntStream.of(new int[] { pos.getX(), pos.getY(), pos.getZ() }));
/*     */   }
/*     */   
/*  23 */   public static final StreamCodec<ByteBuf, Vec3i> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, Vec3i::getX, ByteBufCodecs.VAR_INT, Vec3i::getY, ByteBufCodecs.VAR_INT, Vec3i::getZ, Vec3i::new);
/*     */   
/*     */   public static Codec<Vec3i> offsetCodec(int maxOffsetPerAxis) {
/*  32 */     return CODEC.validate(value -> 
/*  33 */         (Math.abs(value.getX()) < maxOffsetPerAxis && Math.abs(value.getY()) < maxOffsetPerAxis && Math.abs(value.getZ()) < maxOffsetPerAxis) ? DataResult.success(value) : DataResult.error(()));
/*     */   }
/*     */   
/*  41 */   public static final Vec3i ZERO = new Vec3i(0, 0, 0);
/*     */   
/*     */   private int x;
/*     */   
/*     */   private int y;
/*     */   
/*     */   private int z;
/*     */   
/*     */   public Vec3i(int x, int y, int z) {
/*  48 */     this.x = x;
/*  49 */     this.y = y;
/*  50 */     this.z = z;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*     */     Vec3i vec3i;
/*  55 */     if (this == o)
/*  56 */       return true; 
/*  58 */     if (o instanceof Vec3i) {
/*  58 */       vec3i = (Vec3i)o;
/*     */     } else {
/*  59 */       return false;
/*     */     } 
/*  61 */     return (getX() == vec3i.getX() && getY() == vec3i.getY() && getZ() == vec3i.getZ());
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  66 */     return (getY() + getZ() * 31) * 31 + getX();
/*     */   }
/*     */   
/*     */   public int compareTo(Vec3i pos) {
/*  71 */     if (getY() == pos.getY()) {
/*  72 */       if (getZ() == pos.getZ())
/*  73 */         return getX() - pos.getX(); 
/*  75 */       return getZ() - pos.getZ();
/*     */     } 
/*  77 */     return getY() - pos.getY();
/*     */   }
/*     */   
/*     */   public int getX() {
/*  81 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getY() {
/*  85 */     return this.y;
/*     */   }
/*     */   
/*     */   public int getZ() {
/*  89 */     return this.z;
/*     */   }
/*     */   
/*     */   protected Vec3i setX(int x) {
/*  93 */     this.x = x;
/*  94 */     return this;
/*     */   }
/*     */   
/*     */   protected Vec3i setY(int y) {
/*  98 */     this.y = y;
/*  99 */     return this;
/*     */   }
/*     */   
/*     */   protected Vec3i setZ(int z) {
/* 103 */     this.z = z;
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public Vec3i offset(int x, int y, int z) {
/* 108 */     if (x == 0 && y == 0 && z == 0)
/* 109 */       return this; 
/* 111 */     return new Vec3i(getX() + x, getY() + y, getZ() + z);
/*     */   }
/*     */   
/*     */   public Vec3i offset(Vec3i vec) {
/* 115 */     return offset(vec.getX(), vec.getY(), vec.getZ());
/*     */   }
/*     */   
/*     */   public Vec3i subtract(Vec3i vec) {
/* 119 */     return offset(-vec.getX(), -vec.getY(), -vec.getZ());
/*     */   }
/*     */   
/*     */   public Vec3i multiply(int scale) {
/* 123 */     if (scale == 1)
/* 124 */       return this; 
/* 125 */     if (scale == 0)
/* 126 */       return ZERO; 
/* 128 */     return new Vec3i(getX() * scale, getY() * scale, getZ() * scale);
/*     */   }
/*     */   
/*     */   public Vec3i multiply(int xScale, int yScale, int zScale) {
/* 132 */     return new Vec3i(getX() * xScale, getY() * yScale, getZ() * zScale);
/*     */   }
/*     */   
/*     */   public Vec3i above() {
/* 136 */     return above(1);
/*     */   }
/*     */   
/*     */   public Vec3i above(int steps) {
/* 140 */     return relative(Direction.UP, steps);
/*     */   }
/*     */   
/*     */   public Vec3i below() {
/* 144 */     return below(1);
/*     */   }
/*     */   
/*     */   public Vec3i below(int steps) {
/* 148 */     return relative(Direction.DOWN, steps);
/*     */   }
/*     */   
/*     */   public Vec3i north() {
/* 152 */     return north(1);
/*     */   }
/*     */   
/*     */   public Vec3i north(int steps) {
/* 156 */     return relative(Direction.NORTH, steps);
/*     */   }
/*     */   
/*     */   public Vec3i south() {
/* 160 */     return south(1);
/*     */   }
/*     */   
/*     */   public Vec3i south(int steps) {
/* 164 */     return relative(Direction.SOUTH, steps);
/*     */   }
/*     */   
/*     */   public Vec3i west() {
/* 168 */     return west(1);
/*     */   }
/*     */   
/*     */   public Vec3i west(int steps) {
/* 172 */     return relative(Direction.WEST, steps);
/*     */   }
/*     */   
/*     */   public Vec3i east() {
/* 176 */     return east(1);
/*     */   }
/*     */   
/*     */   public Vec3i east(int steps) {
/* 180 */     return relative(Direction.EAST, steps);
/*     */   }
/*     */   
/*     */   public Vec3i relative(Direction direction) {
/* 184 */     return relative(direction, 1);
/*     */   }
/*     */   
/*     */   public Vec3i relative(Direction direction, int steps) {
/* 188 */     if (steps == 0)
/* 189 */       return this; 
/* 191 */     return new Vec3i(getX() + direction.getStepX() * steps, getY() + direction.getStepY() * steps, getZ() + direction.getStepZ() * steps);
/*     */   }
/*     */   
/*     */   public Vec3i relative(Direction.Axis axis, int steps) {
/* 195 */     if (steps == 0)
/* 196 */       return this; 
/* 198 */     int xStep = (axis == Direction.Axis.X) ? steps : 0;
/* 199 */     int yStep = (axis == Direction.Axis.Y) ? steps : 0;
/* 200 */     int zStep = (axis == Direction.Axis.Z) ? steps : 0;
/* 201 */     return new Vec3i(getX() + xStep, getY() + yStep, getZ() + zStep);
/*     */   }
/*     */   
/*     */   public Vec3i cross(Vec3i upVector) {
/* 205 */     return new Vec3i(getY() * upVector.getZ() - getZ() * upVector.getY(), getZ() * upVector.getX() - getX() * upVector.getZ(), getX() * upVector.getY() - getY() * upVector.getX());
/*     */   }
/*     */   
/*     */   public boolean closerThan(Vec3i pos, double distance) {
/* 209 */     return (distSqr(pos) < Mth.square(distance));
/*     */   }
/*     */   
/*     */   public boolean closerToCenterThan(Position pos, double distance) {
/* 213 */     return (distToCenterSqr(pos) < Mth.square(distance));
/*     */   }
/*     */   
/*     */   public double distSqr(Vec3i pos) {
/* 217 */     return distToLowCornerSqr(pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */   
/*     */   public double distToCenterSqr(Position pos) {
/* 221 */     return distToCenterSqr(pos.x(), pos.y(), pos.z());
/*     */   }
/*     */   
/*     */   public double distToCenterSqr(double x, double y, double z) {
/* 225 */     double dx = getX() + 0.5D - x;
/* 226 */     double dy = getY() + 0.5D - y;
/* 227 */     double dz = getZ() + 0.5D - z;
/* 228 */     return dx * dx + dy * dy + dz * dz;
/*     */   }
/*     */   
/*     */   public double distToLowCornerSqr(double x, double y, double z) {
/* 232 */     double dx = getX() - x;
/* 233 */     double dy = getY() - y;
/* 234 */     double dz = getZ() - z;
/* 235 */     return dx * dx + dy * dy + dz * dz;
/*     */   }
/*     */   
/*     */   public int distManhattan(Vec3i pos) {
/* 239 */     float xd = Math.abs(pos.getX() - getX());
/* 240 */     float yd = Math.abs(pos.getY() - getY());
/* 241 */     float zd = Math.abs(pos.getZ() - getZ());
/* 242 */     return (int)(xd + yd + zd);
/*     */   }
/*     */   
/*     */   public int distChessboard(Vec3i pos) {
/* 246 */     int xd = Math.abs(getX() - pos.getX());
/* 247 */     int yd = Math.abs(getY() - pos.getY());
/* 248 */     int zd = Math.abs(getZ() - pos.getZ());
/* 249 */     return Math.max(Math.max(xd, yd), zd);
/*     */   }
/*     */   
/*     */   public int get(Direction.Axis axis) {
/* 253 */     return axis.choose(this.x, this.y, this.z);
/*     */   }
/*     */   
/*     */   public Vector3i toMutable() {
/* 257 */     return new Vector3i(this.x, this.y, this.z);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 262 */     return MoreObjects.toStringHelper(this)
/* 263 */       .add("x", getX())
/* 264 */       .add("y", getY())
/* 265 */       .add("z", getZ())
/* 266 */       .toString();
/*     */   }
/*     */   
/*     */   public String toShortString() {
/* 270 */     return "" + getX() + ", " + getX() + ", " + getY();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\Vec3i.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */