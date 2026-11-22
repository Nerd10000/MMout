/*     */ package net.minecraft.world.phys.shapes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.math.DoubleMath;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.AxisCycle;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public abstract class VoxelShape {
/*     */   protected final DiscreteVoxelShape shape;
/*     */   
/*     */   private VoxelShape[] faces;
/*     */   
/*     */   protected VoxelShape(DiscreteVoxelShape shape) {
/*  28 */     this.shape = shape;
/*     */   }
/*     */   
/*     */   public double min(Direction.Axis axis) {
/*  32 */     int i = this.shape.firstFull(axis);
/*  33 */     if (i >= this.shape.getSize(axis))
/*  34 */       return Double.POSITIVE_INFINITY; 
/*  36 */     return get(axis, i);
/*     */   }
/*     */   
/*     */   public double max(Direction.Axis axis) {
/*  41 */     int i = this.shape.lastFull(axis);
/*  42 */     if (i <= 0)
/*  43 */       return Double.NEGATIVE_INFINITY; 
/*  45 */     return get(axis, i);
/*     */   }
/*     */   
/*     */   public AABB bounds() {
/*  50 */     if (isEmpty())
/*  51 */       throw (UnsupportedOperationException)Util.pauseInIde(new UnsupportedOperationException("No bounds for empty shape.")); 
/*  53 */     return new AABB(min(Direction.Axis.X), min(Direction.Axis.Y), min(Direction.Axis.Z), max(Direction.Axis.X), max(Direction.Axis.Y), max(Direction.Axis.Z));
/*     */   }
/*     */   
/*     */   public VoxelShape singleEncompassing() {
/*  57 */     if (isEmpty())
/*  58 */       return Shapes.empty(); 
/*  60 */     return Shapes.box(min(Direction.Axis.X), min(Direction.Axis.Y), min(Direction.Axis.Z), max(Direction.Axis.X), max(Direction.Axis.Y), max(Direction.Axis.Z));
/*     */   }
/*     */   
/*     */   protected double get(Direction.Axis axis, int i) {
/*  64 */     return getCoords(axis).getDouble(i);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  70 */     return this.shape.isEmpty();
/*     */   }
/*     */   
/*     */   public VoxelShape move(Vec3 delta) {
/*  74 */     return move(delta.x, delta.y, delta.z);
/*     */   }
/*     */   
/*     */   public VoxelShape move(Vec3i delta) {
/*  78 */     return move(delta.getX(), delta.getY(), delta.getZ());
/*     */   }
/*     */   
/*     */   public VoxelShape move(double dx, double dy, double dz) {
/*  83 */     if (isEmpty())
/*  84 */       return Shapes.empty(); 
/*  86 */     return new ArrayVoxelShape(this.shape, (DoubleList)new OffsetDoubleList(
/*     */           
/*  88 */           getCoords(Direction.Axis.X), dx), (DoubleList)new OffsetDoubleList(
/*  89 */           getCoords(Direction.Axis.Y), dy), (DoubleList)new OffsetDoubleList(
/*  90 */           getCoords(Direction.Axis.Z), dz));
/*     */   }
/*     */   
/*     */   public VoxelShape optimize() {
/*  95 */     VoxelShape[] result = { Shapes.empty() };
/*  96 */     forAllBoxes((x1, y1, z1, x2, y2, z2) -> result[0] = Shapes.joinUnoptimized(result[0], Shapes.box(x1, y1, z1, x2, y2, z2), BooleanOp.OR));
/*  99 */     return result[0];
/*     */   }
/*     */   
/*     */   public void forAllEdges(Shapes.DoubleLineConsumer consumer) {
/* 103 */     this.shape.forAllEdges((xi1, yi1, zi1, xi2, yi2, zi2) -> consumer.consume(get(Direction.Axis.X, xi1), get(Direction.Axis.Y, yi1), get(Direction.Axis.Z, zi1), get(Direction.Axis.X, xi2), get(Direction.Axis.Y, yi2), get(Direction.Axis.Z, zi2)), true);
/*     */   }
/*     */   
/*     */   public void forAllBoxes(Shapes.DoubleLineConsumer consumer) {
/* 107 */     DoubleList xCoords = getCoords(Direction.Axis.X);
/* 108 */     DoubleList yCoords = getCoords(Direction.Axis.Y);
/* 109 */     DoubleList zCoords = getCoords(Direction.Axis.Z);
/* 111 */     this.shape.forAllBoxes((xi1, yi1, zi1, xi2, yi2, zi2) -> consumer.consume(xCoords.getDouble(xi1), yCoords.getDouble(yi1), zCoords.getDouble(zi1), xCoords.getDouble(xi2), yCoords.getDouble(yi2), zCoords.getDouble(zi2)), true);
/*     */   }
/*     */   
/*     */   public List<AABB> toAabbs() {
/* 115 */     List<AABB> list = Lists.newArrayList();
/* 116 */     forAllBoxes((x1, y1, z1, x2, y2, z2) -> list.add(new AABB(x1, y1, z1, x2, y2, z2)));
/* 117 */     return list;
/*     */   }
/*     */   
/*     */   public double min(Direction.Axis aAxis, double b, double c) {
/* 121 */     Direction.Axis bAxis = AxisCycle.FORWARD.cycle(aAxis);
/* 122 */     Direction.Axis cAxis = AxisCycle.BACKWARD.cycle(aAxis);
/* 123 */     int bi = findIndex(bAxis, b);
/* 124 */     int ci = findIndex(cAxis, c);
/* 125 */     int i = this.shape.firstFull(aAxis, bi, ci);
/* 126 */     if (i >= this.shape.getSize(aAxis))
/* 127 */       return Double.POSITIVE_INFINITY; 
/* 129 */     return get(aAxis, i);
/*     */   }
/*     */   
/*     */   public double max(Direction.Axis aAxis, double b, double c) {
/* 134 */     Direction.Axis bAxis = AxisCycle.FORWARD.cycle(aAxis);
/* 135 */     Direction.Axis cAxis = AxisCycle.BACKWARD.cycle(aAxis);
/* 136 */     int bi = findIndex(bAxis, b);
/* 137 */     int ci = findIndex(cAxis, c);
/* 138 */     int i = this.shape.lastFull(aAxis, bi, ci);
/* 139 */     if (i <= 0)
/* 140 */       return Double.NEGATIVE_INFINITY; 
/* 142 */     return get(aAxis, i);
/*     */   }
/*     */   
/*     */   protected int findIndex(Direction.Axis axis, double coord) {
/* 149 */     return Mth.binarySearch(0, this.shape.getSize(axis) + 1, index -> (coord < get(axis, index))) - 1;
/*     */   }
/*     */   
/*     */   public BlockHitResult clip(Vec3 from, Vec3 to, BlockPos pos) {
/* 153 */     if (isEmpty())
/* 154 */       return null; 
/* 158 */     Vec3 diff = to.subtract(from);
/* 159 */     if (diff.lengthSqr() < 1.0E-7D)
/* 160 */       return null; 
/* 163 */     Vec3 testPoint = from.add(diff.scale(0.001D));
/* 166 */     if (this.shape.isFullWide(findIndex(Direction.Axis.X, testPoint.x - pos.getX()), findIndex(Direction.Axis.Y, testPoint.y - pos.getY()), findIndex(Direction.Axis.Z, testPoint.z - pos.getZ())))
/* 167 */       return new BlockHitResult(testPoint, Direction.getApproximateNearest(diff.x, diff.y, diff.z).getOpposite(), pos, true); 
/* 171 */     return AABB.clip(toAabbs(), from, to, pos);
/*     */   }
/*     */   
/*     */   public Optional<Vec3> closestPointTo(Vec3 point) {
/* 175 */     if (isEmpty())
/* 176 */       return Optional.empty(); 
/* 178 */     MutableObject<Vec3> closest = new MutableObject();
/* 179 */     forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
/*     */           double x = Mth.clamp(point.x(), x1, x2);
/*     */           double y = Mth.clamp(point.y(), y1, y2);
/*     */           double z = Mth.clamp(point.z(), z1, z2);
/*     */           Vec3 currentClosest = (Vec3)closest.get();
/*     */           if (currentClosest == null || point.distanceToSqr(x, y, z) < point.distanceToSqr(currentClosest))
/*     */             closest.setValue(new Vec3(x, y, z)); 
/*     */         });
/* 188 */     return Optional.of(Objects.<Vec3>requireNonNull((Vec3)closest.get()));
/*     */   }
/*     */   
/*     */   public VoxelShape getFaceShape(Direction direction) {
/* 193 */     if (isEmpty() || this == Shapes.block())
/* 194 */       return this; 
/* 197 */     if (this.faces != null) {
/* 198 */       VoxelShape voxelShape = this.faces[direction.ordinal()];
/* 199 */       if (voxelShape != null)
/* 200 */         return voxelShape; 
/*     */     } else {
/* 203 */       this.faces = new VoxelShape[6];
/*     */     } 
/* 206 */     VoxelShape face = calculateFace(direction);
/* 207 */     this.faces[direction.ordinal()] = face;
/* 208 */     return face;
/*     */   }
/*     */   
/*     */   private VoxelShape calculateFace(Direction direction) {
/* 212 */     Direction.Axis axis = direction.getAxis();
/* 213 */     if (isCubeLikeAlong(axis))
/* 214 */       return this; 
/* 217 */     Direction.AxisDirection sign = direction.getAxisDirection();
/* 218 */     int index = findIndex(axis, (sign == Direction.AxisDirection.POSITIVE) ? 0.9999999D : 1.0E-7D);
/* 219 */     SliceShape slice = new SliceShape(this, axis, index);
/* 222 */     if (slice.isEmpty())
/* 223 */       return Shapes.empty(); 
/* 224 */     if (slice.isCubeLike())
/* 225 */       return Shapes.block(); 
/* 228 */     return slice;
/*     */   }
/*     */   
/*     */   protected boolean isCubeLike() {
/* 232 */     for (Direction.Axis axis : Direction.Axis.VALUES) {
/* 233 */       if (!isCubeLikeAlong(axis))
/* 234 */         return false; 
/*     */     } 
/* 237 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isCubeLikeAlong(Direction.Axis axis) {
/* 241 */     DoubleList coords = getCoords(axis);
/* 242 */     return (coords.size() == 2 && DoubleMath.fuzzyEquals(coords.getDouble(0), 0.0D, 1.0E-7D) && DoubleMath.fuzzyEquals(coords.getDouble(1), 1.0D, 1.0E-7D));
/*     */   }
/*     */   
/*     */   public double collide(Direction.Axis axis, AABB moving, double distance) {
/* 246 */     return collideX(AxisCycle.between(axis, Direction.Axis.X), moving, distance);
/*     */   }
/*     */   
/*     */   protected double collideX(AxisCycle transform, AABB moving, double distance) {
/* 250 */     if (isEmpty())
/* 251 */       return distance; 
/* 253 */     if (Math.abs(distance) < 1.0E-7D)
/* 254 */       return 0.0D; 
/* 257 */     AxisCycle inverse = transform.inverse();
/* 258 */     Direction.Axis aAxis = inverse.cycle(Direction.Axis.X);
/* 259 */     Direction.Axis bAxis = inverse.cycle(Direction.Axis.Y);
/* 260 */     Direction.Axis cAxis = inverse.cycle(Direction.Axis.Z);
/* 262 */     double maxA = moving.max(aAxis);
/* 263 */     double minA = moving.min(aAxis);
/* 265 */     int aMin = findIndex(aAxis, minA + 1.0E-7D);
/* 266 */     int aMax = findIndex(aAxis, maxA - 1.0E-7D);
/* 268 */     int bMin = Math.max(0, findIndex(bAxis, moving.min(bAxis) + 1.0E-7D));
/* 269 */     int bMax = Math.min(this.shape.getSize(bAxis), findIndex(bAxis, moving.max(bAxis) - 1.0E-7D) + 1);
/* 271 */     int cMin = Math.max(0, findIndex(cAxis, moving.min(cAxis) + 1.0E-7D));
/* 272 */     int cMax = Math.min(this.shape.getSize(cAxis), findIndex(cAxis, moving.max(cAxis) - 1.0E-7D) + 1);
/* 274 */     int aSize = this.shape.getSize(aAxis);
/* 276 */     if (distance > 0.0D) {
/* 277 */       for (int a = aMax + 1; a < aSize; a++) {
/* 278 */         for (int b = bMin; b < bMax; b++) {
/* 279 */           for (int c = cMin; c < cMax; c++) {
/* 280 */             if (this.shape.isFullWide(inverse, a, b, c)) {
/* 281 */               double newDistance = get(aAxis, a) - maxA;
/* 282 */               if (newDistance >= -1.0E-7D)
/* 283 */                 distance = Math.min(distance, newDistance); 
/* 285 */               return distance;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 290 */     } else if (distance < 0.0D) {
/* 291 */       for (int a = aMin - 1; a >= 0; a--) {
/* 292 */         for (int b = bMin; b < bMax; b++) {
/* 293 */           for (int c = cMin; c < cMax; c++) {
/* 294 */             if (this.shape.isFullWide(inverse, a, b, c)) {
/* 295 */               double newDistance = get(aAxis, a + 1) - minA;
/* 296 */               if (newDistance <= 1.0E-7D)
/* 297 */                 distance = Math.max(distance, newDistance); 
/* 299 */               return distance;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 305 */     return distance;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 312 */     return super.equals(obj);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 317 */     return isEmpty() ? "EMPTY" : ("VoxelShape[" + String.valueOf(bounds()) + "]");
/*     */   }
/*     */   
/*     */   public abstract DoubleList getCoords(Direction.Axis paramAxis);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\shapes\VoxelShape.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */