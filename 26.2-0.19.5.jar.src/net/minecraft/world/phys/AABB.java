/*     */ package net.minecraft.world.phys;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class AABB {
/*     */   private static final double EPSILON = 1.0E-7D;
/*     */   
/*     */   public final double minX;
/*     */   
/*     */   public final double minY;
/*     */   
/*     */   public final double minZ;
/*     */   
/*     */   public final double maxX;
/*     */   
/*     */   public final double maxY;
/*     */   
/*     */   public final double maxZ;
/*     */   
/*     */   public AABB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
/*  28 */     this.minX = Math.min(minX, maxX);
/*  29 */     this.minY = Math.min(minY, maxY);
/*  30 */     this.minZ = Math.min(minZ, maxZ);
/*  31 */     this.maxX = Math.max(minX, maxX);
/*  32 */     this.maxY = Math.max(minY, maxY);
/*  33 */     this.maxZ = Math.max(minZ, maxZ);
/*     */   }
/*     */   
/*     */   public AABB(BlockPos pos) {
/*  37 */     this(pos.getX(), pos.getY(), pos.getZ(), (pos.getX() + 1), (pos.getY() + 1), (pos.getZ() + 1));
/*     */   }
/*     */   
/*     */   public AABB(Vec3 begin, Vec3 end) {
/*  41 */     this(begin.x, begin.y, begin.z, end.x, end.y, end.z);
/*     */   }
/*     */   
/*     */   public static AABB of(BoundingBox box) {
/*  45 */     return new AABB(box.minX(), box.minY(), box.minZ(), (box.maxX() + 1), (box.maxY() + 1), (box.maxZ() + 1));
/*     */   }
/*     */   
/*     */   public static AABB unitCubeFromLowerCorner(Vec3 pos) {
/*  49 */     return new AABB(pos.x, pos.y, pos.z, pos.x + 1.0D, pos.y + 1.0D, pos.z + 1.0D);
/*     */   }
/*     */   
/*     */   public static AABB encapsulatingFullBlocks(BlockPos pos0, BlockPos pos1) {
/*  53 */     return new AABB(Math.min(pos0.getX(), pos1.getX()), Math.min(pos0.getY(), pos1.getY()), Math.min(pos0.getZ(), pos1.getZ()), (Math.max(pos0.getX(), pos1.getX()) + 1), (Math.max(pos0.getY(), pos1.getY()) + 1), (Math.max(pos0.getZ(), pos1.getZ()) + 1));
/*     */   }
/*     */   
/*     */   public AABB setMinX(double minX) {
/*  57 */     return new AABB(minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
/*     */   }
/*     */   
/*     */   public AABB setMinY(double minY) {
/*  61 */     return new AABB(this.minX, minY, this.minZ, this.maxX, this.maxY, this.maxZ);
/*     */   }
/*     */   
/*     */   public AABB setMinZ(double minZ) {
/*  65 */     return new AABB(this.minX, this.minY, minZ, this.maxX, this.maxY, this.maxZ);
/*     */   }
/*     */   
/*     */   public AABB setMaxX(double maxX) {
/*  69 */     return new AABB(this.minX, this.minY, this.minZ, maxX, this.maxY, this.maxZ);
/*     */   }
/*     */   
/*     */   public AABB setMaxY(double maxY) {
/*  73 */     return new AABB(this.minX, this.minY, this.minZ, this.maxX, maxY, this.maxZ);
/*     */   }
/*     */   
/*     */   public AABB setMaxZ(double maxZ) {
/*  77 */     return new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, maxZ);
/*     */   }
/*     */   
/*     */   public double min(Direction.Axis axis) {
/*  81 */     return axis.choose(this.minX, this.minY, this.minZ);
/*     */   }
/*     */   
/*     */   public double max(Direction.Axis axis) {
/*  85 */     return axis.choose(this.maxX, this.maxY, this.maxZ);
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*     */     AABB aabb;
/*  90 */     if (this == o)
/*  91 */       return true; 
/*  93 */     if (o instanceof AABB) {
/*  93 */       aabb = (AABB)o;
/*     */     } else {
/*  94 */       return false;
/*     */     } 
/*  97 */     if (Double.compare(aabb.minX, this.minX) != 0)
/*  98 */       return false; 
/* 100 */     if (Double.compare(aabb.minY, this.minY) != 0)
/* 101 */       return false; 
/* 103 */     if (Double.compare(aabb.minZ, this.minZ) != 0)
/* 104 */       return false; 
/* 106 */     if (Double.compare(aabb.maxX, this.maxX) != 0)
/* 107 */       return false; 
/* 109 */     if (Double.compare(aabb.maxY, this.maxY) != 0)
/* 110 */       return false; 
/* 112 */     return (Double.compare(aabb.maxZ, this.maxZ) == 0);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 117 */     long temp = Double.doubleToLongBits(this.minX);
/* 118 */     int result = (int)(temp ^ temp >>> 32L);
/* 119 */     temp = Double.doubleToLongBits(this.minY);
/* 120 */     result = 31 * result + (int)(temp ^ temp >>> 32L);
/* 121 */     temp = Double.doubleToLongBits(this.minZ);
/* 122 */     result = 31 * result + (int)(temp ^ temp >>> 32L);
/* 123 */     temp = Double.doubleToLongBits(this.maxX);
/* 124 */     result = 31 * result + (int)(temp ^ temp >>> 32L);
/* 125 */     temp = Double.doubleToLongBits(this.maxY);
/* 126 */     result = 31 * result + (int)(temp ^ temp >>> 32L);
/* 127 */     temp = Double.doubleToLongBits(this.maxZ);
/* 128 */     result = 31 * result + (int)(temp ^ temp >>> 32L);
/* 129 */     return result;
/*     */   }
/*     */   
/*     */   public AABB contract(double xa, double ya, double za) {
/* 137 */     double minX = this.minX;
/* 138 */     double minY = this.minY;
/* 139 */     double minZ = this.minZ;
/* 140 */     double maxX = this.maxX;
/* 141 */     double maxY = this.maxY;
/* 142 */     double maxZ = this.maxZ;
/* 144 */     if (xa < 0.0D) {
/* 145 */       minX -= xa;
/* 146 */     } else if (xa > 0.0D) {
/* 147 */       maxX -= xa;
/*     */     } 
/* 150 */     if (ya < 0.0D) {
/* 151 */       minY -= ya;
/* 152 */     } else if (ya > 0.0D) {
/* 153 */       maxY -= ya;
/*     */     } 
/* 156 */     if (za < 0.0D) {
/* 157 */       minZ -= za;
/* 158 */     } else if (za > 0.0D) {
/* 159 */       maxZ -= za;
/*     */     } 
/* 162 */     return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
/*     */   }
/*     */   
/*     */   public AABB expandTowards(Vec3 delta) {
/* 166 */     return expandTowards(delta.x, delta.y, delta.z);
/*     */   }
/*     */   
/*     */   public AABB expandTowards(double xa, double ya, double za) {
/* 174 */     double minX = this.minX;
/* 175 */     double minY = this.minY;
/* 176 */     double minZ = this.minZ;
/* 177 */     double maxX = this.maxX;
/* 178 */     double maxY = this.maxY;
/* 179 */     double maxZ = this.maxZ;
/* 181 */     if (xa < 0.0D) {
/* 182 */       minX += xa;
/* 183 */     } else if (xa > 0.0D) {
/* 184 */       maxX += xa;
/*     */     } 
/* 187 */     if (ya < 0.0D) {
/* 188 */       minY += ya;
/* 189 */     } else if (ya > 0.0D) {
/* 190 */       maxY += ya;
/*     */     } 
/* 193 */     if (za < 0.0D) {
/* 194 */       minZ += za;
/* 195 */     } else if (za > 0.0D) {
/* 196 */       maxZ += za;
/*     */     } 
/* 199 */     return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
/*     */   }
/*     */   
/*     */   public AABB inflate(double xAdd, double yAdd, double zAdd) {
/* 207 */     double minX = this.minX - xAdd;
/* 208 */     double minY = this.minY - yAdd;
/* 209 */     double minZ = this.minZ - zAdd;
/* 210 */     double maxX = this.maxX + xAdd;
/* 211 */     double maxY = this.maxY + yAdd;
/* 212 */     double maxZ = this.maxZ + zAdd;
/* 214 */     return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
/*     */   }
/*     */   
/*     */   public AABB inflate(double amountToAddInAllDirections) {
/* 218 */     return inflate(amountToAddInAllDirections, amountToAddInAllDirections, amountToAddInAllDirections);
/*     */   }
/*     */   
/*     */   public AABB intersect(AABB other) {
/* 222 */     double minX = Math.max(this.minX, other.minX);
/* 223 */     double minY = Math.max(this.minY, other.minY);
/* 224 */     double minZ = Math.max(this.minZ, other.minZ);
/* 225 */     double maxX = Math.min(this.maxX, other.maxX);
/* 226 */     double maxY = Math.min(this.maxY, other.maxY);
/* 227 */     double maxZ = Math.min(this.maxZ, other.maxZ);
/* 229 */     return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
/*     */   }
/*     */   
/*     */   public AABB minmax(AABB other) {
/* 233 */     double minX = Math.min(this.minX, other.minX);
/* 234 */     double minY = Math.min(this.minY, other.minY);
/* 235 */     double minZ = Math.min(this.minZ, other.minZ);
/* 236 */     double maxX = Math.max(this.maxX, other.maxX);
/* 237 */     double maxY = Math.max(this.maxY, other.maxY);
/* 238 */     double maxZ = Math.max(this.maxZ, other.maxZ);
/* 240 */     return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
/*     */   }
/*     */   
/*     */   public AABB move(double xa, double ya, double za) {
/* 244 */     return new AABB(this.minX + xa, this.minY + ya, this.minZ + za, this.maxX + xa, this.maxY + ya, this.maxZ + za);
/*     */   }
/*     */   
/*     */   public AABB move(BlockPos pos) {
/* 248 */     return new AABB(this.minX + pos.getX(), this.minY + pos.getY(), this.minZ + pos.getZ(), this.maxX + pos.getX(), this.maxY + pos.getY(), this.maxZ + pos.getZ());
/*     */   }
/*     */   
/*     */   public AABB move(Vec3 pos) {
/* 252 */     return move(pos.x, pos.y, pos.z);
/*     */   }
/*     */   
/*     */   public AABB move(Vector3f pos) {
/* 256 */     return move(pos.x, pos.y, pos.z);
/*     */   }
/*     */   
/*     */   public boolean intersects(AABB aabb) {
/* 260 */     return intersects(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
/*     */   }
/*     */   
/*     */   public boolean intersects(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
/* 265 */     return (this.minX < maxX && this.maxX > minX && this.minY < maxY && this.maxY > minY && this.minZ < maxZ && this.maxZ > minZ);
/*     */   }
/*     */   
/*     */   public boolean intersects(Vec3 min, Vec3 max) {
/* 274 */     return intersects(Math.min(min.x, max.x), Math.min(min.y, max.y), Math.min(min.z, max.z), Math.max(min.x, max.x), Math.max(min.y, max.y), Math.max(min.z, max.z));
/*     */   }
/*     */   
/*     */   public boolean intersects(BlockPos pos) {
/* 278 */     return intersects(pos.getX(), pos.getY(), pos.getZ(), (pos.getX() + 1), (pos.getY() + 1), (pos.getZ() + 1));
/*     */   }
/*     */   
/*     */   public boolean contains(Vec3 vec) {
/* 282 */     return contains(vec.x, vec.y, vec.z);
/*     */   }
/*     */   
/*     */   public boolean contains(double x, double y, double z) {
/* 286 */     return (x >= this.minX && x < this.maxX && y >= this.minY && y < this.maxY && z >= this.minZ && z < this.maxZ);
/*     */   }
/*     */   
/*     */   public double getSize() {
/* 290 */     double xs = getXsize();
/* 291 */     double ys = getYsize();
/* 292 */     double zs = getZsize();
/* 293 */     return (xs + ys + zs) / 3.0D;
/*     */   }
/*     */   
/*     */   public double getXsize() {
/* 297 */     return this.maxX - this.minX;
/*     */   }
/*     */   
/*     */   public double getYsize() {
/* 301 */     return this.maxY - this.minY;
/*     */   }
/*     */   
/*     */   public double getZsize() {
/* 305 */     return this.maxZ - this.minZ;
/*     */   }
/*     */   
/*     */   public AABB deflate(double xSubstract, double ySubtract, double zSubtract) {
/* 315 */     return inflate(-xSubstract, -ySubtract, -zSubtract);
/*     */   }
/*     */   
/*     */   public AABB deflate(double amount) {
/* 319 */     return inflate(-amount);
/*     */   }
/*     */   
/*     */   public Optional<Vec3> clip(Vec3 from, Vec3 to) {
/* 323 */     return clip(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, from, to);
/*     */   }
/*     */   
/*     */   public static Optional<Vec3> clip(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Vec3 from, Vec3 to) {
/* 327 */     double[] scaleReference = { 1.0D };
/* 328 */     double dx = to.x - from.x;
/* 329 */     double dy = to.y - from.y;
/* 330 */     double dz = to.z - from.z;
/* 332 */     Direction direction = getDirection(minX, minY, minZ, maxX, maxY, maxZ, from, scaleReference, null, dx, dy, dz);
/* 333 */     if (direction == null)
/* 334 */       return Optional.empty(); 
/* 337 */     double scale = scaleReference[0];
/* 338 */     return Optional.of(from.add(scale * dx, scale * dy, scale * dz));
/*     */   }
/*     */   
/*     */   public static BlockHitResult clip(Iterable<AABB> aabBs, Vec3 from, Vec3 to, BlockPos pos) {
/* 342 */     double[] scaleReference = { 1.0D };
/* 343 */     Direction direction = null;
/* 345 */     double dx = to.x - from.x;
/* 346 */     double dy = to.y - from.y;
/* 347 */     double dz = to.z - from.z;
/* 349 */     for (AABB aabb : aabBs)
/* 350 */       direction = getDirection(aabb.move(pos), from, scaleReference, direction, dx, dy, dz); 
/* 353 */     if (direction == null)
/* 354 */       return null; 
/* 357 */     double scale = scaleReference[0];
/* 358 */     return new BlockHitResult(from.add(scale * dx, scale * dy, scale * dz), direction, pos, false);
/*     */   }
/*     */   
/*     */   private static Direction getDirection(AABB aabb, Vec3 from, double[] scaleReference, Direction direction, double dx, double dy, double dz) {
/* 362 */     return getDirection(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ, from, scaleReference, direction, dx, dy, dz);
/*     */   }
/*     */   
/*     */   private static Direction getDirection(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Vec3 from, double[] scaleReference, Direction direction, double dx, double dy, double dz) {
/* 366 */     if (dx > 1.0E-7D) {
/* 367 */       direction = clipPoint(scaleReference, direction, dx, dy, dz, minX, minY, maxY, minZ, maxZ, Direction.WEST, from.x, from.y, from.z);
/* 368 */     } else if (dx < -1.0E-7D) {
/* 369 */       direction = clipPoint(scaleReference, direction, dx, dy, dz, maxX, minY, maxY, minZ, maxZ, Direction.EAST, from.x, from.y, from.z);
/*     */     } 
/* 372 */     if (dy > 1.0E-7D) {
/* 373 */       direction = clipPoint(scaleReference, direction, dy, dz, dx, minY, minZ, maxZ, minX, maxX, Direction.DOWN, from.y, from.z, from.x);
/* 374 */     } else if (dy < -1.0E-7D) {
/* 375 */       direction = clipPoint(scaleReference, direction, dy, dz, dx, maxY, minZ, maxZ, minX, maxX, Direction.UP, from.y, from.z, from.x);
/*     */     } 
/* 378 */     if (dz > 1.0E-7D) {
/* 379 */       direction = clipPoint(scaleReference, direction, dz, dx, dy, minZ, minX, maxX, minY, maxY, Direction.NORTH, from.z, from.x, from.y);
/* 380 */     } else if (dz < -1.0E-7D) {
/* 381 */       direction = clipPoint(scaleReference, direction, dz, dx, dy, maxZ, minX, maxX, minY, maxY, Direction.SOUTH, from.z, from.x, from.y);
/*     */     } 
/* 383 */     return direction;
/*     */   }
/*     */   
/*     */   private static Direction clipPoint(double[] scaleReference, Direction direction, double da, double db, double dc, double point, double minB, double maxB, double minC, double maxC, Direction newDirection, double fromA, double fromB, double fromC) {
/* 387 */     double s = (point - fromA) / da;
/* 388 */     double pb = fromB + s * db;
/* 389 */     double pc = fromC + s * dc;
/* 390 */     if (0.0D < s && s < scaleReference[0] && minB - 1.0E-7D < pb && pb < maxB + 1.0E-7D && minC - 1.0E-7D < pc && pc < maxC + 1.0E-7D) {
/* 394 */       scaleReference[0] = s;
/* 395 */       return newDirection;
/*     */     } 
/* 397 */     return direction;
/*     */   }
/*     */   
/*     */   public boolean collidedAlongVector(Vec3 vector, List<AABB> aabbs) {
/* 401 */     Vec3 from = getCenter();
/* 402 */     Vec3 to = from.add(vector);
/* 403 */     for (AABB shapePart : aabbs) {
/* 405 */       AABB inflated = shapePart.inflate(
/* 406 */           getXsize() * 0.5D - 1.0E-7D, 
/* 407 */           getYsize() * 0.5D - 1.0E-7D, 
/* 408 */           getZsize() * 0.5D - 1.0E-7D);
/* 410 */       if (inflated.contains(to) || inflated.contains(from))
/* 411 */         return true; 
/* 412 */       if (inflated.clip(from, to).isPresent())
/* 413 */         return true; 
/*     */     } 
/* 416 */     return false;
/*     */   }
/*     */   
/*     */   public double distanceToSqr(Vec3 point) {
/* 420 */     double dx = Math.max(Math.max(this.minX - point.x, point.x - this.maxX), 0.0D);
/* 421 */     double dy = Math.max(Math.max(this.minY - point.y, point.y - this.maxY), 0.0D);
/* 422 */     double dz = Math.max(Math.max(this.minZ - point.z, point.z - this.maxZ), 0.0D);
/* 423 */     return Mth.lengthSquared(dx, dy, dz);
/*     */   }
/*     */   
/*     */   public double distanceToSqr(AABB boundingBox) {
/* 427 */     double dx = Math.max(Math.max(this.minX - boundingBox.maxX, boundingBox.minX - this.maxX), 0.0D);
/* 428 */     double dy = Math.max(Math.max(this.minY - boundingBox.maxY, boundingBox.minY - this.maxY), 0.0D);
/* 429 */     double dz = Math.max(Math.max(this.minZ - boundingBox.maxZ, boundingBox.minZ - this.maxZ), 0.0D);
/* 430 */     return Mth.lengthSquared(dx, dy, dz);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 435 */     return "AABB[" + this.minX + ", " + this.minY + ", " + this.minZ + "] -> [" + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
/*     */   }
/*     */   
/*     */   public boolean hasNaN() {
/* 439 */     return (Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ));
/*     */   }
/*     */   
/*     */   public Vec3 getCenter() {
/* 443 */     return new Vec3(Mth.lerp(0.5D, this.minX, this.maxX), Mth.lerp(0.5D, this.minY, this.maxY), Mth.lerp(0.5D, this.minZ, this.maxZ));
/*     */   }
/*     */   
/*     */   public Vec3 getBottomCenter() {
/* 447 */     return new Vec3(Mth.lerp(0.5D, this.minX, this.maxX), this.minY, Mth.lerp(0.5D, this.minZ, this.maxZ));
/*     */   }
/*     */   
/*     */   public Vec3 getMinPosition() {
/* 451 */     return new Vec3(this.minX, this.minY, this.minZ);
/*     */   }
/*     */   
/*     */   public Vec3 getMaxPosition() {
/* 455 */     return new Vec3(this.maxX, this.maxY, this.maxZ);
/*     */   }
/*     */   
/*     */   public static AABB ofSize(Vec3 center, double sizeX, double sizeY, double sizeZ) {
/* 459 */     return new AABB(center.x - sizeX / 2.0D, center.y - sizeY / 2.0D, center.z - sizeZ / 2.0D, center.x + sizeX / 2.0D, center.y + sizeY / 2.0D, center.z + sizeZ / 2.0D);
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 470 */     private float minX = Float.POSITIVE_INFINITY;
/*     */     
/* 471 */     private float minY = Float.POSITIVE_INFINITY;
/*     */     
/* 472 */     private float minZ = Float.POSITIVE_INFINITY;
/*     */     
/* 474 */     private float maxX = Float.NEGATIVE_INFINITY;
/*     */     
/* 475 */     private float maxY = Float.NEGATIVE_INFINITY;
/*     */     
/* 476 */     private float maxZ = Float.NEGATIVE_INFINITY;
/*     */     
/*     */     private boolean defined;
/*     */     
/*     */     public void include(Vector3fc v) {
/* 481 */       this.minX = Math.min(this.minX, v.x());
/* 482 */       this.minY = Math.min(this.minY, v.y());
/* 483 */       this.minZ = Math.min(this.minZ, v.z());
/* 485 */       this.maxX = Math.max(this.maxX, v.x());
/* 486 */       this.maxY = Math.max(this.maxY, v.y());
/* 487 */       this.maxZ = Math.max(this.maxZ, v.z());
/* 488 */       this.defined = true;
/*     */     }
/*     */     
/*     */     public boolean isDefined() {
/* 492 */       return this.defined;
/*     */     }
/*     */     
/*     */     public AABB build() {
/* 496 */       if (!this.defined)
/* 497 */         throw new IllegalStateException("Cannot build an undefined AABB. Include at least one point."); 
/* 499 */       return new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\AABB.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */