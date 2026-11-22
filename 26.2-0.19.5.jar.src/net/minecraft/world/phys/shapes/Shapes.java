/*     */ package net.minecraft.world.phys.shapes;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.math.DoubleMath;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.mojang.math.OctahedralGroup;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.AxisCycle;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class Shapes {
/*     */   public static final double EPSILON = 1.0E-7D;
/*     */   
/*     */   public static final double BIG_EPSILON = 1.0E-6D;
/*     */   
/*     */   private static final VoxelShape BLOCK;
/*     */   
/*     */   static {
/*  26 */     BLOCK = (VoxelShape)Util.make(() -> {
/*     */           DiscreteVoxelShape shape = new BitSetDiscreteVoxelShape(1, 1, 1);
/*     */           shape.fill(0, 0, 0);
/*     */           return new CubeVoxelShape(shape);
/*     */         });
/*     */   }
/*     */   
/*  32 */   private static final Vec3 BLOCK_CENTER = new Vec3(0.5D, 0.5D, 0.5D);
/*     */   
/*  34 */   public static final VoxelShape INFINITY = box(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
/*     */   
/*  39 */   private static final VoxelShape EMPTY = new ArrayVoxelShape(new BitSetDiscreteVoxelShape(0, 0, 0), (DoubleList)new DoubleArrayList(new double[] { 0.0D }, ), (DoubleList)new DoubleArrayList(new double[] { 0.0D }, ), (DoubleList)new DoubleArrayList(new double[] { 0.0D }));
/*     */   
/*     */   public static VoxelShape empty() {
/*  47 */     return EMPTY;
/*     */   }
/*     */   
/*     */   public static VoxelShape block() {
/*  51 */     return BLOCK;
/*     */   }
/*     */   
/*     */   public static VoxelShape box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
/*  55 */     if (minX > maxX || minY > maxY || minZ > maxZ)
/*  56 */       throw new IllegalArgumentException("The min values need to be smaller or equals to the max values"); 
/*  58 */     return create(minX, minY, minZ, maxX, maxY, maxZ);
/*     */   }
/*     */   
/*     */   public static VoxelShape create(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
/*  62 */     if (maxX - minX < 1.0E-7D || maxY - minY < 1.0E-7D || maxZ - minZ < 1.0E-7D)
/*  63 */       return empty(); 
/*  66 */     int xBits = findBits(minX, maxX);
/*  67 */     int yBits = findBits(minY, maxY);
/*  68 */     int zBits = findBits(minZ, maxZ);
/*  70 */     if (xBits < 0 || yBits < 0 || zBits < 0)
/*  71 */       return new ArrayVoxelShape(BLOCK.shape, 
/*     */           
/*  73 */           (DoubleList)DoubleArrayList.wrap(new double[] { minX, maxX }, ), (DoubleList)DoubleArrayList.wrap(new double[] { minY, maxY }, ), (DoubleList)DoubleArrayList.wrap(new double[] { minZ, maxZ })); 
/*  79 */     if (xBits == 0 && yBits == 0 && zBits == 0)
/*  80 */       return block(); 
/*  83 */     int xSize = 1 << xBits;
/*  84 */     int ySize = 1 << yBits;
/*  85 */     int zSize = 1 << zBits;
/*  87 */     BitSetDiscreteVoxelShape voxelShape = BitSetDiscreteVoxelShape.withFilledBounds(xSize, ySize, zSize, 
/*     */         
/*  91 */         (int)Math.round(minX * xSize), 
/*  92 */         (int)Math.round(minY * ySize), 
/*  93 */         (int)Math.round(minZ * zSize), 
/*  94 */         (int)Math.round(maxX * xSize), 
/*  95 */         (int)Math.round(maxY * ySize), 
/*  96 */         (int)Math.round(maxZ * zSize));
/*  98 */     return new CubeVoxelShape(voxelShape);
/*     */   }
/*     */   
/*     */   public static VoxelShape create(AABB aabb) {
/* 102 */     return create(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static int findBits(double min, double max) {
/* 108 */     if (min < -1.0E-7D || max > 1.0000001D)
/* 109 */       return -1; 
/* 111 */     for (int bits = 0; bits <= 3; bits++) {
/* 112 */       int intervals = 1 << bits;
/* 113 */       double shMin = min * intervals;
/* 114 */       double shMax = max * intervals;
/* 115 */       boolean foundMin = (Math.abs(shMin - Math.round(shMin)) < 1.0E-7D * intervals);
/* 116 */       boolean foundMax = (Math.abs(shMax - Math.round(shMax)) < 1.0E-7D * intervals);
/* 117 */       if (foundMin && foundMax)
/* 118 */         return bits; 
/*     */     } 
/* 121 */     return -1;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static long lcm(int first, int second) {
/* 127 */     return first * (second / IntMath.gcd(first, second));
/*     */   }
/*     */   
/*     */   public static VoxelShape or(VoxelShape first, VoxelShape second) {
/* 131 */     return join(first, second, BooleanOp.OR);
/*     */   }
/*     */   
/*     */   public static VoxelShape or(VoxelShape first, VoxelShape... tail) {
/* 135 */     return Arrays.<VoxelShape>stream(tail).reduce(first, Shapes::or);
/*     */   }
/*     */   
/*     */   public static VoxelShape join(VoxelShape first, VoxelShape second, BooleanOp op) {
/* 139 */     return joinUnoptimized(first, second, op).optimize();
/*     */   }
/*     */   
/*     */   public static VoxelShape joinUnoptimized(VoxelShape first, VoxelShape second, BooleanOp op) {
/* 143 */     if (op.apply(false, false))
/* 144 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException()); 
/* 146 */     if (first == second)
/* 147 */       return op.apply(true, true) ? first : empty(); 
/* 149 */     boolean firstOnlyMatters = op.apply(true, false);
/* 150 */     boolean secondOnlyMatters = op.apply(false, true);
/* 152 */     if (first.isEmpty())
/* 153 */       return secondOnlyMatters ? second : empty(); 
/* 155 */     if (second.isEmpty())
/* 156 */       return firstOnlyMatters ? first : empty(); 
/* 159 */     IndexMerger xMerger = createIndexMerger(1, first.getCoords(Direction.Axis.X), second.getCoords(Direction.Axis.X), firstOnlyMatters, secondOnlyMatters);
/* 160 */     IndexMerger yMerger = createIndexMerger(xMerger.size() - 1, first.getCoords(Direction.Axis.Y), second.getCoords(Direction.Axis.Y), firstOnlyMatters, secondOnlyMatters);
/* 161 */     IndexMerger zMerger = createIndexMerger((xMerger.size() - 1) * (yMerger.size() - 1), first.getCoords(Direction.Axis.Z), second.getCoords(Direction.Axis.Z), firstOnlyMatters, secondOnlyMatters);
/* 163 */     BitSetDiscreteVoxelShape voxelShape = BitSetDiscreteVoxelShape.join(first.shape, second.shape, xMerger, yMerger, zMerger, op);
/* 164 */     if (xMerger instanceof DiscreteCubeMerger && yMerger instanceof DiscreteCubeMerger && zMerger instanceof DiscreteCubeMerger)
/* 165 */       return new CubeVoxelShape(voxelShape); 
/* 167 */     return new ArrayVoxelShape(voxelShape, xMerger.getList(), yMerger.getList(), zMerger.getList());
/*     */   }
/*     */   
/*     */   public static boolean joinIsNotEmpty(VoxelShape first, VoxelShape second, BooleanOp op) {
/* 172 */     if (op.apply(false, false))
/* 173 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException()); 
/* 176 */     boolean firstEmpty = first.isEmpty();
/* 177 */     boolean secondEmpty = second.isEmpty();
/* 178 */     if (firstEmpty || secondEmpty)
/* 179 */       return op.apply(!firstEmpty, !secondEmpty); 
/* 181 */     if (first == second)
/* 182 */       return op.apply(true, true); 
/* 185 */     boolean firstOnlyMatters = op.apply(true, false);
/* 186 */     boolean secondOnlyMatters = op.apply(false, true);
/* 187 */     for (Direction.Axis axis : AxisCycle.AXIS_VALUES) {
/* 188 */       if (first.max(axis) < second.min(axis) - 1.0E-7D)
/* 189 */         return (firstOnlyMatters || secondOnlyMatters); 
/* 191 */       if (second.max(axis) < first.min(axis) - 1.0E-7D)
/* 192 */         return (firstOnlyMatters || secondOnlyMatters); 
/*     */     } 
/* 196 */     IndexMerger xMerger = createIndexMerger(1, first.getCoords(Direction.Axis.X), second.getCoords(Direction.Axis.X), firstOnlyMatters, secondOnlyMatters);
/* 197 */     IndexMerger yMerger = createIndexMerger(xMerger.size() - 1, first.getCoords(Direction.Axis.Y), second.getCoords(Direction.Axis.Y), firstOnlyMatters, secondOnlyMatters);
/* 198 */     IndexMerger zMerger = createIndexMerger((xMerger.size() - 1) * (yMerger.size() - 1), first.getCoords(Direction.Axis.Z), second.getCoords(Direction.Axis.Z), firstOnlyMatters, secondOnlyMatters);
/* 199 */     return joinIsNotEmpty(xMerger, yMerger, zMerger, first.shape, second.shape, op);
/*     */   }
/*     */   
/*     */   private static boolean joinIsNotEmpty(IndexMerger xMerger, IndexMerger yMerger, IndexMerger zMerger, DiscreteVoxelShape first, DiscreteVoxelShape second, BooleanOp op) {
/* 203 */     return !xMerger.forMergedIndexes((x1, x2, xr) -> yMerger.forMergedIndexes(()));
/*     */   }
/*     */   
/*     */   public static double collide(Direction.Axis axis, AABB moving, Iterable<VoxelShape> shapes, double distance) {
/* 213 */     for (VoxelShape shape : shapes) {
/* 214 */       if (Math.abs(distance) < 1.0E-7D)
/* 215 */         return 0.0D; 
/* 217 */       distance = shape.collide(axis, moving, distance);
/*     */     } 
/* 219 */     return distance;
/*     */   }
/*     */   
/*     */   public static boolean blockOccludes(VoxelShape shape, VoxelShape occluder, Direction direction) {
/* 224 */     if (shape == block() && occluder == block())
/* 225 */       return true; 
/* 227 */     if (occluder.isEmpty())
/* 228 */       return false; 
/* 230 */     Direction.Axis axis = direction.getAxis();
/* 231 */     Direction.AxisDirection sign = direction.getAxisDirection();
/* 233 */     VoxelShape first = (sign == Direction.AxisDirection.POSITIVE) ? shape : occluder;
/* 234 */     VoxelShape second = (sign == Direction.AxisDirection.POSITIVE) ? occluder : shape;
/* 235 */     BooleanOp op = (sign == Direction.AxisDirection.POSITIVE) ? BooleanOp.ONLY_FIRST : BooleanOp.ONLY_SECOND;
/* 237 */     return (DoubleMath.fuzzyEquals(first.max(axis), 1.0D, 1.0E-7D) && 
/* 238 */       DoubleMath.fuzzyEquals(second.min(axis), 0.0D, 1.0E-7D) && 
/* 239 */       !joinIsNotEmpty(new SliceShape(first, axis, first.shape.getSize(axis) - 1), new SliceShape(second, axis, 0), op));
/*     */   }
/*     */   
/*     */   public static boolean mergedFaceOccludes(VoxelShape shape, VoxelShape occluder, Direction direction) {
/* 244 */     if (shape == block() || occluder == block())
/* 245 */       return true; 
/* 248 */     Direction.Axis axis = direction.getAxis();
/* 249 */     Direction.AxisDirection sign = direction.getAxisDirection();
/* 251 */     VoxelShape first = (sign == Direction.AxisDirection.POSITIVE) ? shape : occluder;
/* 252 */     VoxelShape second = (sign == Direction.AxisDirection.POSITIVE) ? occluder : shape;
/* 254 */     if (!DoubleMath.fuzzyEquals(first.max(axis), 1.0D, 1.0E-7D))
/* 255 */       first = empty(); 
/* 257 */     if (!DoubleMath.fuzzyEquals(second.min(axis), 0.0D, 1.0E-7D))
/* 258 */       second = empty(); 
/* 261 */     return !joinIsNotEmpty(block(), joinUnoptimized(new SliceShape(first, axis, first.shape.getSize(axis) - 1), new SliceShape(second, axis, 0), BooleanOp.OR), BooleanOp.ONLY_FIRST);
/*     */   }
/*     */   
/*     */   public static boolean faceShapeOccludes(VoxelShape shape, VoxelShape occluder) {
/* 266 */     if (shape == block() || occluder == block())
/* 267 */       return true; 
/* 270 */     if (shape.isEmpty() && occluder.isEmpty())
/* 271 */       return false; 
/* 274 */     return !joinIsNotEmpty(
/* 275 */         block(), 
/* 276 */         joinUnoptimized(shape, occluder, BooleanOp.OR), BooleanOp.ONLY_FIRST);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static IndexMerger createIndexMerger(int cost, DoubleList first, DoubleList second, boolean firstOnlyMatters, boolean secondOnlyMatters) {
/* 288 */     int firstSize = first.size() - 1;
/* 289 */     int secondSize = second.size() - 1;
/* 290 */     if (first instanceof CubePointRange && second instanceof CubePointRange) {
/* 291 */       long size = lcm(firstSize, secondSize);
/* 292 */       if (cost * size <= 256L)
/* 293 */         return new DiscreteCubeMerger(firstSize, secondSize); 
/*     */     } 
/* 297 */     if (first.getDouble(firstSize) < second.getDouble(0) - 1.0E-7D)
/* 298 */       return new NonOverlappingMerger(first, second, false); 
/* 299 */     if (second.getDouble(secondSize) < first.getDouble(0) - 1.0E-7D)
/* 300 */       return new NonOverlappingMerger(second, first, true); 
/* 303 */     if (firstSize == secondSize && Objects.equals(first, second))
/* 304 */       return new IdenticalMerger(first); 
/* 307 */     return new IndirectMerger(first, second, firstOnlyMatters, secondOnlyMatters);
/*     */   }
/*     */   
/*     */   public static VoxelShape rotate(VoxelShape shape, OctahedralGroup rotation) {
/* 311 */     return rotate(shape, rotation, BLOCK_CENTER);
/*     */   }
/*     */   
/*     */   public static VoxelShape rotate(VoxelShape shape, OctahedralGroup rotation, Vec3 rotationPoint) {
/* 315 */     if (rotation == OctahedralGroup.IDENTITY)
/* 316 */       return shape; 
/* 319 */     DiscreteVoxelShape newDiscreteShape = shape.shape.rotate(rotation);
/* 320 */     if (shape instanceof CubeVoxelShape && BLOCK_CENTER.equals(rotationPoint))
/* 321 */       return new CubeVoxelShape(newDiscreteShape); 
/* 324 */     Direction.Axis newX = rotation.permutation().permuteAxis(Direction.Axis.X);
/* 325 */     Direction.Axis newY = rotation.permutation().permuteAxis(Direction.Axis.Y);
/* 326 */     Direction.Axis newZ = rotation.permutation().permuteAxis(Direction.Axis.Z);
/* 328 */     DoubleList newXs = shape.getCoords(newX);
/* 329 */     DoubleList newYs = shape.getCoords(newY);
/* 330 */     DoubleList newZs = shape.getCoords(newZ);
/* 332 */     boolean flipX = rotation.inverts(Direction.Axis.X);
/* 333 */     boolean flipY = rotation.inverts(Direction.Axis.Y);
/* 334 */     boolean flipZ = rotation.inverts(Direction.Axis.Z);
/* 336 */     return new ArrayVoxelShape(newDiscreteShape, 
/*     */         
/* 338 */         flipAxisIfNeeded(newXs, flipX, rotationPoint.get(newX), rotationPoint.x), 
/* 339 */         flipAxisIfNeeded(newYs, flipY, rotationPoint.get(newY), rotationPoint.y), 
/* 340 */         flipAxisIfNeeded(newZs, flipZ, rotationPoint.get(newZ), rotationPoint.z));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static DoubleList flipAxisIfNeeded(DoubleList newAxis, boolean flip, double newRelative, double oldRelative) {
/* 347 */     if (!flip && newRelative == oldRelative)
/* 348 */       return newAxis; 
/* 350 */     int size = newAxis.size();
/* 351 */     DoubleArrayList doubleArrayList = new DoubleArrayList(size);
/* 353 */     if (flip) {
/* 354 */       for (int i = size - 1; i >= 0; i--)
/* 355 */         doubleArrayList.add(-(newAxis.getDouble(i) - newRelative) + oldRelative); 
/*     */     } else {
/* 358 */       for (int i = 0; i >= 0 && i < size; i++)
/* 359 */         doubleArrayList.add(newAxis.getDouble(i) - newRelative + oldRelative); 
/*     */     } 
/* 362 */     return (DoubleList)doubleArrayList;
/*     */   }
/*     */   
/*     */   public static boolean equal(VoxelShape first, VoxelShape second) {
/* 366 */     return !joinIsNotEmpty(first, second, BooleanOp.NOT_SAME);
/*     */   }
/*     */   
/*     */   public static Map<Direction.Axis, VoxelShape> rotateHorizontalAxis(VoxelShape zAxis) {
/* 375 */     return rotateHorizontalAxis(zAxis, BLOCK_CENTER);
/*     */   }
/*     */   
/*     */   public static Map<Direction.Axis, VoxelShape> rotateHorizontalAxis(VoxelShape zAxis, Vec3 rotationCenter) {
/* 379 */     return Maps.newEnumMap(Map.of(Direction.Axis.Z, zAxis, Direction.Axis.X, 
/*     */           
/* 381 */           rotate(zAxis, OctahedralGroup.BLOCK_ROT_Y_90, rotationCenter)));
/*     */   }
/*     */   
/*     */   public static Map<Direction.Axis, VoxelShape> rotateAllAxis(VoxelShape north) {
/* 386 */     return rotateAllAxis(north, BLOCK_CENTER);
/*     */   }
/*     */   
/*     */   public static Map<Direction.Axis, VoxelShape> rotateAllAxis(VoxelShape north, Vec3 rotationCenter) {
/* 390 */     return Maps.newEnumMap(Map.of(Direction.Axis.Z, north, Direction.Axis.X, 
/*     */           
/* 392 */           rotate(north, OctahedralGroup.BLOCK_ROT_Y_90, rotationCenter), Direction.Axis.Y, 
/* 393 */           rotate(north, OctahedralGroup.BLOCK_ROT_X_90, rotationCenter)));
/*     */   }
/*     */   
/*     */   public static Map<Direction, VoxelShape> rotateHorizontal(VoxelShape north) {
/* 398 */     return rotateHorizontal(north, OctahedralGroup.IDENTITY, BLOCK_CENTER);
/*     */   }
/*     */   
/*     */   public static Map<Direction, VoxelShape> rotateHorizontal(VoxelShape north, OctahedralGroup initial) {
/* 402 */     return rotateHorizontal(north, initial, BLOCK_CENTER);
/*     */   }
/*     */   
/*     */   public static Map<Direction, VoxelShape> rotateHorizontal(VoxelShape north, OctahedralGroup initial, Vec3 rotationCenter) {
/* 406 */     return Maps.newEnumMap(Map.of(Direction.NORTH, 
/* 407 */           rotate(north, initial), Direction.EAST, 
/* 408 */           rotate(north, OctahedralGroup.BLOCK_ROT_Y_90.compose(initial), rotationCenter), Direction.SOUTH, 
/* 409 */           rotate(north, OctahedralGroup.BLOCK_ROT_Y_180.compose(initial), rotationCenter), Direction.WEST, 
/* 410 */           rotate(north, OctahedralGroup.BLOCK_ROT_Y_270.compose(initial), rotationCenter)));
/*     */   }
/*     */   
/*     */   public static Map<Direction, VoxelShape> rotateAll(VoxelShape north) {
/* 415 */     return rotateAll(north, OctahedralGroup.IDENTITY, BLOCK_CENTER);
/*     */   }
/*     */   
/*     */   public static Map<Direction, VoxelShape> rotateAll(VoxelShape north, Vec3 rotationCenter) {
/* 419 */     return rotateAll(north, OctahedralGroup.IDENTITY, rotationCenter);
/*     */   }
/*     */   
/*     */   public static Map<Direction, VoxelShape> rotateAll(VoxelShape north, OctahedralGroup initial, Vec3 rotationCenter) {
/* 423 */     return Maps.newEnumMap(Map.of(Direction.NORTH, 
/* 424 */           rotate(north, initial), Direction.EAST, 
/* 425 */           rotate(north, OctahedralGroup.BLOCK_ROT_Y_90.compose(initial), rotationCenter), Direction.SOUTH, 
/* 426 */           rotate(north, OctahedralGroup.BLOCK_ROT_Y_180.compose(initial), rotationCenter), Direction.WEST, 
/* 427 */           rotate(north, OctahedralGroup.BLOCK_ROT_Y_270.compose(initial), rotationCenter), Direction.UP, 
/* 428 */           rotate(north, OctahedralGroup.BLOCK_ROT_X_270.compose(initial), rotationCenter), Direction.DOWN, 
/* 429 */           rotate(north, OctahedralGroup.BLOCK_ROT_X_90.compose(initial), rotationCenter)));
/*     */   }
/*     */   
/*     */   public static Map<AttachFace, Map<Direction, VoxelShape>> rotateAttachFace(VoxelShape north) {
/* 434 */     return rotateAttachFace(north, OctahedralGroup.IDENTITY);
/*     */   }
/*     */   
/*     */   public static Map<AttachFace, Map<Direction, VoxelShape>> rotateAttachFace(VoxelShape north, OctahedralGroup initial) {
/* 438 */     return Map.of(AttachFace.WALL, 
/* 439 */         rotateHorizontal(north, initial), AttachFace.FLOOR, 
/* 440 */         rotateHorizontal(north, OctahedralGroup.BLOCK_ROT_X_270.compose(initial)), AttachFace.CEILING, 
/*     */         
/* 442 */         rotateHorizontal(north, OctahedralGroup.BLOCK_ROT_Y_180.compose(OctahedralGroup.BLOCK_ROT_X_90).compose(initial)));
/*     */   }
/*     */   
/*     */   public static interface DoubleLineConsumer {
/*     */     void consume(double param1Double1, double param1Double2, double param1Double3, double param1Double4, double param1Double5, double param1Double6);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\shapes\Shapes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */