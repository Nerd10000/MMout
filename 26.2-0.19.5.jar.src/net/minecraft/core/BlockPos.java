/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Iterator;
/*     */ import java.util.Optional;
/*     */ import java.util.Queue;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import javax.annotation.concurrent.Immutable;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ @Immutable
/*     */ public class BlockPos extends Vec3i {
/*     */   public static final Codec<BlockPos> CODEC;
/*     */   
/*     */   static {
/*  38 */     CODEC = Codec.INT_STREAM.comapFlatMap(input -> Util.fixedSize(input, 3).map(()), pos -> IntStream.of(new int[] { pos.getX(), pos.getY(), pos.getZ() })).stable();
/*     */   }
/*     */   
/*  40 */   public static final StreamCodec<ByteBuf, BlockPos> STREAM_CODEC = new StreamCodec<ByteBuf, BlockPos>() {
/*     */       public BlockPos decode(ByteBuf input) {
/*  43 */         return FriendlyByteBuf.readBlockPos(input);
/*     */       }
/*     */       
/*     */       public void encode(ByteBuf output, BlockPos value) {
/*  48 */         FriendlyByteBuf.writeBlockPos(output, value);
/*     */       }
/*     */     };
/*     */   
/*  52 */   public static final BlockPos ZERO = new BlockPos(0, 0, 0);
/*     */   
/*  55 */   public static final int PACKED_HORIZONTAL_LENGTH = 1 + Mth.log2(Mth.smallestEncompassingPowerOfTwo(30000000));
/*     */   
/*  57 */   public static final int PACKED_Y_LENGTH = 64 - 2 * PACKED_HORIZONTAL_LENGTH;
/*     */   
/*  59 */   private static final long PACKED_X_MASK = (1L << PACKED_HORIZONTAL_LENGTH) - 1L;
/*     */   
/*  60 */   private static final long PACKED_Y_MASK = (1L << PACKED_Y_LENGTH) - 1L;
/*     */   
/*  61 */   private static final long PACKED_Z_MASK = (1L << PACKED_HORIZONTAL_LENGTH) - 1L;
/*     */   
/*     */   private static final int Y_OFFSET = 0;
/*     */   
/*  64 */   private static final int Z_OFFSET = PACKED_Y_LENGTH;
/*     */   
/*  65 */   private static final int X_OFFSET = PACKED_Y_LENGTH + PACKED_HORIZONTAL_LENGTH;
/*     */   
/*  67 */   public static final int MAX_HORIZONTAL_COORDINATE = (1 << PACKED_HORIZONTAL_LENGTH) / 2 - 1;
/*     */   
/*     */   public BlockPos(int x, int y, int z) {
/*  70 */     super(x, y, z);
/*     */   }
/*     */   
/*     */   public BlockPos(Vec3i vec3i) {
/*  74 */     this(vec3i.getX(), vec3i.getY(), vec3i.getZ());
/*     */   }
/*     */   
/*     */   public static long offset(long blockNode, Direction offset) {
/*  78 */     return offset(blockNode, offset.getStepX(), offset.getStepY(), offset.getStepZ());
/*     */   }
/*     */   
/*     */   public static long offset(long blockNode, int stepX, int stepY, int stepZ) {
/*  82 */     return asLong(getX(blockNode) + stepX, getY(blockNode) + stepY, getZ(blockNode) + stepZ);
/*     */   }
/*     */   
/*     */   public static int getX(long blockNode) {
/*  86 */     return (int)(blockNode << 64 - X_OFFSET - PACKED_HORIZONTAL_LENGTH >> 64 - PACKED_HORIZONTAL_LENGTH);
/*     */   }
/*     */   
/*     */   public static int getY(long blockNode) {
/*  90 */     return (int)(blockNode << 64 - PACKED_Y_LENGTH >> 64 - PACKED_Y_LENGTH);
/*     */   }
/*     */   
/*     */   public static int getZ(long blockNode) {
/*  94 */     return (int)(blockNode << 64 - Z_OFFSET - PACKED_HORIZONTAL_LENGTH >> 64 - PACKED_HORIZONTAL_LENGTH);
/*     */   }
/*     */   
/*     */   public static BlockPos of(long blockNode) {
/*  98 */     return new BlockPos(getX(blockNode), getY(blockNode), getZ(blockNode));
/*     */   }
/*     */   
/*     */   public static BlockPos containing(double x, double y, double z) {
/* 102 */     return new BlockPos(Mth.floor(x), Mth.floor(y), Mth.floor(z));
/*     */   }
/*     */   
/*     */   public static BlockPos containing(Position pos) {
/* 106 */     return containing(pos.x(), pos.y(), pos.z());
/*     */   }
/*     */   
/*     */   public static BlockPos min(BlockPos a, BlockPos b) {
/* 110 */     return new BlockPos(
/* 111 */         Math.min(a.getX(), b.getX()), 
/* 112 */         Math.min(a.getY(), b.getY()), 
/* 113 */         Math.min(a.getZ(), b.getZ()));
/*     */   }
/*     */   
/*     */   public static BlockPos max(BlockPos a, BlockPos b) {
/* 118 */     return new BlockPos(
/* 119 */         Math.max(a.getX(), b.getX()), 
/* 120 */         Math.max(a.getY(), b.getY()), 
/* 121 */         Math.max(a.getZ(), b.getZ()));
/*     */   }
/*     */   
/*     */   public long asLong() {
/* 126 */     return asLong(getX(), getY(), getZ());
/*     */   }
/*     */   
/*     */   public static long asLong(int x, int y, int z) {
/* 130 */     long node = 0L;
/* 131 */     node |= (x & PACKED_X_MASK) << X_OFFSET;
/* 132 */     node |= (y & PACKED_Y_MASK) << 0L;
/* 133 */     node |= (z & PACKED_Z_MASK) << Z_OFFSET;
/* 134 */     return node;
/*     */   }
/*     */   
/*     */   public static long getFlatIndex(long neighborBlockNode) {
/* 140 */     return neighborBlockNode & 0xFFFFFFFFFFFFFFF0L;
/*     */   }
/*     */   
/*     */   public BlockPos offset(int x, int y, int z) {
/* 145 */     if (x == 0 && y == 0 && z == 0)
/* 146 */       return this; 
/* 148 */     return new BlockPos(getX() + x, getY() + y, getZ() + z);
/*     */   }
/*     */   
/*     */   public BlockPos offset(Vec3i vec) {
/* 153 */     return offset(vec.getX(), vec.getY(), vec.getZ());
/*     */   }
/*     */   
/*     */   public BlockPos subtract(Vec3i vec) {
/* 158 */     return offset(-vec.getX(), -vec.getY(), -vec.getZ());
/*     */   }
/*     */   
/*     */   public BlockPos multiply(int scale) {
/* 163 */     if (scale == 1)
/* 164 */       return this; 
/* 165 */     if (scale == 0)
/* 166 */       return ZERO; 
/* 168 */     return new BlockPos(getX() * scale, getY() * scale, getZ() * scale);
/*     */   }
/*     */   
/*     */   public BlockPos above() {
/* 173 */     return relative(Direction.UP);
/*     */   }
/*     */   
/*     */   public BlockPos above(int steps) {
/* 178 */     return relative(Direction.UP, steps);
/*     */   }
/*     */   
/*     */   public BlockPos below() {
/* 183 */     return relative(Direction.DOWN);
/*     */   }
/*     */   
/*     */   public BlockPos below(int steps) {
/* 188 */     return relative(Direction.DOWN, steps);
/*     */   }
/*     */   
/*     */   public BlockPos north() {
/* 193 */     return relative(Direction.NORTH);
/*     */   }
/*     */   
/*     */   public BlockPos north(int steps) {
/* 198 */     return relative(Direction.NORTH, steps);
/*     */   }
/*     */   
/*     */   public BlockPos south() {
/* 203 */     return relative(Direction.SOUTH);
/*     */   }
/*     */   
/*     */   public BlockPos south(int steps) {
/* 208 */     return relative(Direction.SOUTH, steps);
/*     */   }
/*     */   
/*     */   public BlockPos west() {
/* 213 */     return relative(Direction.WEST);
/*     */   }
/*     */   
/*     */   public BlockPos west(int steps) {
/* 218 */     return relative(Direction.WEST, steps);
/*     */   }
/*     */   
/*     */   public BlockPos east() {
/* 223 */     return relative(Direction.EAST);
/*     */   }
/*     */   
/*     */   public BlockPos east(int steps) {
/* 228 */     return relative(Direction.EAST, steps);
/*     */   }
/*     */   
/*     */   public BlockPos relative(Direction direction) {
/* 233 */     return new BlockPos(getX() + direction.getStepX(), getY() + direction.getStepY(), getZ() + direction.getStepZ());
/*     */   }
/*     */   
/*     */   public BlockPos relative(Direction direction, int steps) {
/* 238 */     if (steps == 0)
/* 239 */       return this; 
/* 241 */     return new BlockPos(getX() + direction.getStepX() * steps, getY() + direction.getStepY() * steps, getZ() + direction.getStepZ() * steps);
/*     */   }
/*     */   
/*     */   public BlockPos relative(Direction.Axis axis, int steps) {
/* 246 */     if (steps == 0)
/* 247 */       return this; 
/* 249 */     int xStep = (axis == Direction.Axis.X) ? steps : 0;
/* 250 */     int yStep = (axis == Direction.Axis.Y) ? steps : 0;
/* 251 */     int zStep = (axis == Direction.Axis.Z) ? steps : 0;
/* 252 */     return new BlockPos(getX() + xStep, getY() + yStep, getZ() + zStep);
/*     */   }
/*     */   
/*     */   public BlockPos rotate(Rotation rotation) {
/* 256 */     switch (rotation) {
/*     */       default:
/* 256 */         throw new MatchException(null, null);
/*     */       case X:
/*     */       
/*     */       case Y:
/*     */       
/*     */       case Z:
/*     */       
/*     */       case null:
/*     */         break;
/*     */     } 
/* 256 */     return 
/*     */       
/* 260 */       this;
/*     */   }
/*     */   
/*     */   public BlockPos cross(Vec3i upVector) {
/* 266 */     return new BlockPos(getY() * upVector.getZ() - getZ() * upVector.getY(), getZ() * upVector.getX() - getX() * upVector.getZ(), getX() * upVector.getY() - getY() * upVector.getX());
/*     */   }
/*     */   
/*     */   public BlockPos atY(int y) {
/* 270 */     return new BlockPos(getX(), y, getZ());
/*     */   }
/*     */   
/*     */   public BlockPos immutable() {
/* 274 */     return this;
/*     */   }
/*     */   
/*     */   public MutableBlockPos mutable() {
/* 278 */     return new MutableBlockPos(getX(), getY(), getZ());
/*     */   }
/*     */   
/*     */   public Vec3 clampLocationWithin(Vec3 location) {
/* 282 */     return new Vec3(
/* 283 */         Mth.clamp(location.x, (getX() + 1.0E-5F), getX() + 1.0D - 9.999999747378752E-6D), 
/* 284 */         Mth.clamp(location.y, (getY() + 1.0E-5F), getY() + 1.0D - 9.999999747378752E-6D), 
/* 285 */         Mth.clamp(location.z, (getZ() + 1.0E-5F), getZ() + 1.0D - 9.999999747378752E-6D));
/*     */   }
/*     */   
/*     */   public static class MutableBlockPos extends BlockPos {
/*     */     public MutableBlockPos() {
/* 290 */       this(0, 0, 0);
/*     */     }
/*     */     
/*     */     public MutableBlockPos(int x, int y, int z) {
/* 294 */       super(x, y, z);
/*     */     }
/*     */     
/*     */     public MutableBlockPos(double x, double y, double z) {
/* 298 */       this(Mth.floor(x), Mth.floor(y), Mth.floor(z));
/*     */     }
/*     */     
/*     */     public BlockPos offset(int x, int y, int z) {
/* 303 */       return super.offset(x, y, z).immutable();
/*     */     }
/*     */     
/*     */     public BlockPos multiply(int scale) {
/* 308 */       return super.multiply(scale).immutable();
/*     */     }
/*     */     
/*     */     public BlockPos relative(Direction direction, int steps) {
/* 313 */       return super.relative(direction, steps).immutable();
/*     */     }
/*     */     
/*     */     public BlockPos relative(Direction.Axis axis, int steps) {
/* 318 */       return super.relative(axis, steps).immutable();
/*     */     }
/*     */     
/*     */     public BlockPos rotate(Rotation rotation) {
/* 323 */       return super.rotate(rotation).immutable();
/*     */     }
/*     */     
/*     */     public MutableBlockPos set(int x, int y, int z) {
/* 327 */       setX(x);
/* 328 */       setY(y);
/* 329 */       setZ(z);
/* 330 */       return this;
/*     */     }
/*     */     
/*     */     public MutableBlockPos set(double x, double y, double z) {
/* 334 */       return set(Mth.floor(x), Mth.floor(y), Mth.floor(z));
/*     */     }
/*     */     
/*     */     public MutableBlockPos set(Vec3i vec) {
/* 338 */       return set(vec.getX(), vec.getY(), vec.getZ());
/*     */     }
/*     */     
/*     */     public MutableBlockPos set(long pos) {
/* 342 */       return set(getX(pos), getY(pos), getZ(pos));
/*     */     }
/*     */     
/*     */     public MutableBlockPos set(AxisCycle transform, int x, int y, int z) {
/* 346 */       return set(transform
/* 347 */           .cycle(x, y, z, Direction.Axis.X), transform
/* 348 */           .cycle(x, y, z, Direction.Axis.Y), transform
/* 349 */           .cycle(x, y, z, Direction.Axis.Z));
/*     */     }
/*     */     
/*     */     public MutableBlockPos setWithOffset(Vec3i pos, Direction direction) {
/* 354 */       return set(pos.getX() + direction.getStepX(), pos.getY() + direction.getStepY(), pos.getZ() + direction.getStepZ());
/*     */     }
/*     */     
/*     */     public MutableBlockPos setWithOffset(Vec3i pos, int x, int y, int z) {
/* 358 */       return set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
/*     */     }
/*     */     
/*     */     public MutableBlockPos setWithOffset(Vec3i pos, Vec3i offset) {
/* 362 */       return set(pos.getX() + offset.getX(), pos.getY() + offset.getY(), pos.getZ() + offset.getZ());
/*     */     }
/*     */     
/*     */     public MutableBlockPos move(Direction direction) {
/* 366 */       return move(direction, 1);
/*     */     }
/*     */     
/*     */     public MutableBlockPos move(Direction direction, int steps) {
/* 370 */       return set(getX() + direction.getStepX() * steps, getY() + direction.getStepY() * steps, getZ() + direction.getStepZ() * steps);
/*     */     }
/*     */     
/*     */     public MutableBlockPos move(int x, int y, int z) {
/* 374 */       return set(getX() + x, getY() + y, getZ() + z);
/*     */     }
/*     */     
/*     */     public MutableBlockPos move(Vec3i pos) {
/* 378 */       return set(getX() + pos.getX(), getY() + pos.getY(), getZ() + pos.getZ());
/*     */     }
/*     */     
/*     */     public MutableBlockPos clamp(Direction.Axis axis, int minimum, int maximum) {
/* 382 */       switch (axis) {
/*     */         default:
/* 382 */           throw new MatchException(null, null);
/*     */         case X:
/*     */         
/*     */         case Y:
/*     */         
/*     */         case Z:
/*     */           break;
/*     */       } 
/* 382 */       return 
/*     */         
/* 385 */         set(getX(), getY(), Mth.clamp(getZ(), minimum, maximum));
/*     */     }
/*     */     
/*     */     public MutableBlockPos setX(int x) {
/* 391 */       super.setX(x);
/* 392 */       return this;
/*     */     }
/*     */     
/*     */     public MutableBlockPos setY(int y) {
/* 397 */       super.setY(y);
/* 398 */       return this;
/*     */     }
/*     */     
/*     */     public MutableBlockPos setZ(int z) {
/* 403 */       super.setZ(z);
/* 404 */       return this;
/*     */     }
/*     */     
/*     */     public BlockPos immutable() {
/* 409 */       return new BlockPos(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Iterable<BlockPos> randomInCube(RandomSource random, int limit, BlockPos center, int sizeToScanInAllDirections) {
/* 414 */     return randomBetweenClosed(random, limit, center.getX() - sizeToScanInAllDirections, center.getY() - sizeToScanInAllDirections, center.getZ() - sizeToScanInAllDirections, center.getX() + sizeToScanInAllDirections, center.getY() + sizeToScanInAllDirections, center.getZ() + sizeToScanInAllDirections);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static Stream<BlockPos> squareOutSouthEast(BlockPos from) {
/* 424 */     return Stream.of(new BlockPos[] { from, from
/*     */           
/* 426 */           .south(), from
/* 427 */           .east(), from
/* 428 */           .south().east() });
/*     */   }
/*     */   
/*     */   public static Iterable<BlockPos> randomBetweenClosed(final RandomSource random, final int limit, final int minX, final int minY, final int minZ, int maxX, int maxY, int maxZ) {
/* 434 */     final int width = maxX - minX + 1;
/* 435 */     final int height = maxY - minY + 1;
/* 436 */     final int depth = maxZ - minZ + 1;
/* 438 */     return () -> new AbstractIterator<BlockPos>() {
/* 439 */         private final BlockPos.MutableBlockPos nextPos = new BlockPos.MutableBlockPos();
/*     */         
/* 440 */         private int counter = limit;
/*     */         
/*     */         protected BlockPos computeNext() {
/* 444 */           if (this.counter <= 0)
/* 445 */             return (BlockPos)endOfData(); 
/* 448 */           BlockPos next = this.nextPos.set(minX + random
/* 449 */               .nextInt(width), minY + random
/* 450 */               .nextInt(height), minZ + random
/* 451 */               .nextInt(depth));
/* 453 */           this.counter--;
/* 454 */           return next;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static Iterable<BlockPos> withinManhattan(BlockPos origin, final int reachX, final int reachY, final int reachZ) {
/* 460 */     final int maxDepth = reachX + reachY + reachZ;
/* 461 */     final int originX = origin.getX();
/* 462 */     final int originY = origin.getY();
/* 463 */     final int originZ = origin.getZ();
/* 465 */     return () -> new AbstractIterator<BlockPos>() {
/* 466 */         private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
/*     */         
/*     */         private int currentDepth;
/*     */         
/*     */         private int maxX;
/*     */         
/*     */         private int maxY;
/*     */         
/*     */         private int x;
/*     */         
/*     */         private int y;
/*     */         
/*     */         private boolean zMirror;
/*     */         
/*     */         protected BlockPos computeNext() {
/* 479 */           if (this.zMirror) {
/* 480 */             this.zMirror = false;
/* 481 */             this.cursor.setZ(originZ - this.cursor.getZ() - originZ);
/* 482 */             return this.cursor;
/*     */           } 
/* 485 */           BlockPos found = null;
/* 486 */           while (found == null) {
/* 487 */             if (this.y > this.maxY) {
/* 488 */               this.x++;
/* 489 */               if (this.x > this.maxX) {
/* 490 */                 this.currentDepth++;
/* 491 */                 if (this.currentDepth > maxDepth)
/* 492 */                   return (BlockPos)endOfData(); 
/* 494 */                 this.maxX = Math.min(reachX, this.currentDepth);
/* 495 */                 this.x = -this.maxX;
/*     */               } 
/* 497 */               this.maxY = Math.min(reachY, this.currentDepth - Math.abs(this.x));
/* 498 */               this.y = -this.maxY;
/*     */             } 
/* 501 */             int xx = this.x;
/* 502 */             int yy = this.y;
/* 503 */             int zz = this.currentDepth - Math.abs(xx) - Math.abs(yy);
/* 504 */             if (zz <= reachZ) {
/* 505 */               this.zMirror = (zz != 0);
/* 506 */               found = this.cursor.set(originX + xx, originY + yy, originZ + zz);
/*     */             } 
/* 508 */             this.y++;
/*     */           } 
/* 510 */           return found;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static Optional<BlockPos> findClosestMatch(BlockPos startPos, int horizontalSearchRadius, int verticalSearchRadius, Predicate<BlockPos> predicate) {
/* 516 */     for (BlockPos blockPos : withinManhattan(startPos, horizontalSearchRadius, verticalSearchRadius, horizontalSearchRadius)) {
/* 517 */       if (predicate.test(blockPos))
/* 518 */         return Optional.of(blockPos); 
/*     */     } 
/* 521 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static Stream<BlockPos> withinManhattanStream(BlockPos origin, int reachX, int reachY, int reachZ) {
/* 525 */     return StreamSupport.stream(withinManhattan(origin, reachX, reachY, reachZ).spliterator(), false);
/*     */   }
/*     */   
/*     */   public static Iterable<BlockPos> betweenClosed(AABB box) {
/* 529 */     BlockPos startPos = containing(box.minX, box.minY, box.minZ);
/* 530 */     BlockPos endPos = containing(box.maxX, box.maxY, box.maxZ);
/* 531 */     return betweenClosed(startPos, endPos);
/*     */   }
/*     */   
/*     */   public static Iterable<BlockPos> betweenClosed(BlockPos a, BlockPos b) {
/* 535 */     return betweenClosed(
/* 536 */         Math.min(a.getX(), b.getX()), 
/* 537 */         Math.min(a.getY(), b.getY()), 
/* 538 */         Math.min(a.getZ(), b.getZ()), 
/* 539 */         Math.max(a.getX(), b.getX()), 
/* 540 */         Math.max(a.getY(), b.getY()), 
/* 541 */         Math.max(a.getZ(), b.getZ()));
/*     */   }
/*     */   
/*     */   public static Stream<BlockPos> betweenClosedStream(BlockPos a, BlockPos b) {
/* 546 */     return StreamSupport.stream(betweenClosed(a, b).spliterator(), false);
/*     */   }
/*     */   
/*     */   public static Stream<BlockPos> betweenClosedStream(BoundingBox boundingBox) {
/* 550 */     return betweenClosedStream(
/* 551 */         Math.min(boundingBox.minX(), boundingBox.maxX()), 
/* 552 */         Math.min(boundingBox.minY(), boundingBox.maxY()), 
/* 553 */         Math.min(boundingBox.minZ(), boundingBox.maxZ()), 
/* 554 */         Math.max(boundingBox.minX(), boundingBox.maxX()), 
/* 555 */         Math.max(boundingBox.minY(), boundingBox.maxY()), 
/* 556 */         Math.max(boundingBox.minZ(), boundingBox.maxZ()));
/*     */   }
/*     */   
/*     */   public static Stream<BlockPos> betweenClosedStream(AABB box) {
/* 561 */     return betweenClosedStream(Mth.floor(box.minX), Mth.floor(box.minY), Mth.floor(box.minZ), Mth.floor(box.maxX), Mth.floor(box.maxY), Mth.floor(box.maxZ));
/*     */   }
/*     */   
/*     */   public static Stream<BlockPos> betweenClosedStream(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
/* 565 */     return StreamSupport.stream(betweenClosed(minX, minY, minZ, maxX, maxY, maxZ).spliterator(), false);
/*     */   }
/*     */   
/*     */   public static Iterable<BlockPos> betweenClosed(final int minX, final int minY, final int minZ, int maxX, int maxY, int maxZ) {
/* 570 */     final int width = maxX - minX + 1;
/* 571 */     final int height = maxY - minY + 1;
/* 572 */     int depth = maxZ - minZ + 1;
/* 573 */     final int end = width * height * depth;
/* 575 */     return () -> new AbstractIterator<BlockPos>() {
/* 576 */         private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
/*     */         
/*     */         private int index;
/*     */         
/*     */         protected BlockPos computeNext() {
/* 581 */           if (this.index == end)
/* 582 */             return (BlockPos)endOfData(); 
/* 585 */           int x = this.index % width;
/* 586 */           int slice = this.index / width;
/* 587 */           int y = slice % height;
/* 588 */           int z = slice / height;
/* 590 */           this.index++;
/* 591 */           return this.cursor.set(minX + x, minY + y, minZ + z);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static Iterable<BlockPos> neighborColumn(final int startX, final int startY, final int startZ, int endY) {
/* 597 */     final int yDirection = (endY > startY) ? 1 : -1;
/* 598 */     final int height = Math.abs(endY - startY) + 1;
/* 604 */     final Vec3i[] steps = { new Vec3i(0, 0, 0), Direction.NORTH.getUnitVec3i(), Direction.EAST.getUnitVec3i(), Direction.SOUTH.getUnitVec3i(), Direction.WEST.getUnitVec3i() };
/* 606 */     final int stepCount = steps.length * height;
/* 608 */     return () -> new AbstractIterator<BlockPos>() {
/* 609 */         private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
/*     */         
/*     */         private int index;
/*     */         
/*     */         protected BlockPos computeNext() {
/* 614 */           if (this.index == stepCount)
/* 615 */             return (BlockPos)endOfData(); 
/* 618 */           int y = this.index % height;
/* 619 */           int stepIndex = this.index / height;
/* 620 */           Vec3i step = steps[stepIndex];
/* 621 */           this.index++;
/* 622 */           return this.cursor.set(startX + step.getX(), startY + y * yDirection, startZ + step.getZ());
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static Iterable<MutableBlockPos> spiralAround(final BlockPos center, final int radius, final Direction firstDirection, final Direction secondDirection) {
/* 628 */     Validate.validState((firstDirection.getAxis() != secondDirection.getAxis()), "The two directions cannot be on the same axis", new Object[0]);
/* 630 */     return () -> new AbstractIterator<MutableBlockPos>() {
/* 631 */         private final Direction[] directions = new Direction[] { this.val$firstDirection, this.val$secondDirection, this.val$firstDirection
/*     */             
/* 634 */             .getOpposite(), this.val$secondDirection
/* 635 */             .getOpposite() };
/*     */         
/* 637 */         private final BlockPos.MutableBlockPos cursor = center.mutable().move(secondDirection);
/*     */         
/* 638 */         private final int legs = 4 * radius;
/*     */         
/* 639 */         private int leg = -1;
/*     */         
/*     */         private int legSize;
/*     */         
/*     */         private int legIndex;
/*     */         
/* 643 */         private int lastX = this.cursor.getX();
/*     */         
/* 644 */         private int lastY = this.cursor.getY();
/*     */         
/* 645 */         private int lastZ = this.cursor.getZ();
/*     */         
/*     */         protected BlockPos.MutableBlockPos computeNext() {
/* 649 */           this.cursor.set(this.lastX, this.lastY, this.lastZ).move(this.directions[(this.leg + 4) % 4]);
/* 651 */           this.lastX = this.cursor.getX();
/* 652 */           this.lastY = this.cursor.getY();
/* 653 */           this.lastZ = this.cursor.getZ();
/* 655 */           if (this.legIndex >= this.legSize) {
/* 656 */             if (this.leg >= this.legs)
/* 657 */               return (BlockPos.MutableBlockPos)endOfData(); 
/* 659 */             this.leg++;
/* 660 */             this.legIndex = 0;
/* 661 */             this.legSize = this.leg / 2 + 1;
/*     */           } 
/* 663 */           this.legIndex++;
/* 665 */           return this.cursor;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public enum TraversalNodeStatus {
/* 671 */     ACCEPT, SKIP, STOP;
/*     */   }
/*     */   
/*     */   public static int breadthFirstTraversal(BlockPos startPos, int maxDepth, int maxCount, BiConsumer<BlockPos, Consumer<BlockPos>> neighbourProvider, Function<BlockPos, TraversalNodeStatus> nodeProcessor) {
/*     */     static final class Node extends Record {
/*     */       private final BlockPos pos;
/*     */       
/*     */       private final int depth;
/*     */       
/*     */       Node(BlockPos pos, int depth) {
/* 687 */         this.pos = pos;
/* 687 */         this.depth = depth;
/*     */       }
/*     */       
/*     */       public final String toString() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/core/BlockPos$1Node;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #687	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/core/BlockPos$1Node;
/*     */       }
/*     */       
/*     */       public final int hashCode() {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/BlockPos$1Node;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #687	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/core/BlockPos$1Node;
/*     */       }
/*     */       
/*     */       public final boolean equals(Object o) {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/core/BlockPos$1Node;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #687	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/core/BlockPos$1Node;
/*     */         //   0	8	1	o	Ljava/lang/Object;
/*     */       }
/*     */       
/*     */       public BlockPos pos() {
/* 687 */         return this.pos;
/*     */       }
/*     */       
/*     */       public int depth() {
/* 687 */         return this.depth;
/*     */       }
/*     */     };
/* 689 */     Queue<Node> nodes = new ArrayDeque<>();
/* 690 */     LongOpenHashSet longOpenHashSet = new LongOpenHashSet();
/* 691 */     nodes.add(new Node(startPos, 0));
/* 692 */     int count = 0;
/* 693 */     while (!nodes.isEmpty()) {
/* 694 */       Node node = nodes.poll();
/* 695 */       BlockPos currentPos = node.pos;
/* 696 */       int depth = node.depth;
/* 697 */       long currentPosLong = currentPos.asLong();
/* 698 */       if (!longOpenHashSet.add(currentPosLong))
/*     */         continue; 
/* 701 */       TraversalNodeStatus next = nodeProcessor.apply(currentPos);
/* 702 */       if (next == TraversalNodeStatus.SKIP)
/*     */         continue; 
/* 704 */       if (next == TraversalNodeStatus.STOP)
/*     */         break; 
/* 707 */       count++;
/* 708 */       if (count >= maxCount)
/* 709 */         return count; 
/* 711 */       if (depth >= maxDepth)
/*     */         continue; 
/* 714 */       neighbourProvider.accept(currentPos, pos -> nodes.add(new Node(pos, depth + 1)));
/*     */     } 
/* 716 */     return count;
/*     */   }
/*     */   
/*     */   public static Iterable<BlockPos> betweenCornersInDirection(AABB aabb, Vec3 direction) {
/* 720 */     Vec3 minCorner = aabb.getMinPosition();
/* 721 */     int firstCornerX = Mth.floor(minCorner.x());
/* 722 */     int firstCornerY = Mth.floor(minCorner.y());
/* 723 */     int firstCornerZ = Mth.floor(minCorner.z());
/* 725 */     Vec3 maxCorner = aabb.getMaxPosition();
/* 726 */     int secondCornerX = Mth.floor(maxCorner.x());
/* 727 */     int secondCornerY = Mth.floor(maxCorner.y());
/* 728 */     int secondCornerZ = Mth.floor(maxCorner.z());
/* 729 */     return betweenCornersInDirection(firstCornerX, firstCornerY, firstCornerZ, secondCornerX, secondCornerY, secondCornerZ, direction);
/*     */   }
/*     */   
/*     */   public static Iterable<BlockPos> betweenCornersInDirection(BlockPos firstCorner, BlockPos secondCorner, Vec3 direction) {
/* 733 */     return betweenCornersInDirection(firstCorner.getX(), firstCorner.getY(), firstCorner.getZ(), secondCorner.getX(), secondCorner.getY(), secondCorner.getZ(), direction);
/*     */   }
/*     */   
/*     */   public static Iterable<BlockPos> betweenCornersInDirection(int firstCornerX, int firstCornerY, int firstCornerZ, int secondCornerX, int secondCornerY, int secondCornerZ, Vec3 direction) {
/* 740 */     int minCornerX = Math.min(firstCornerX, secondCornerX);
/* 741 */     int minCornerY = Math.min(firstCornerY, secondCornerY);
/* 742 */     int minCornerZ = Math.min(firstCornerZ, secondCornerZ);
/* 744 */     int maxCornerX = Math.max(firstCornerX, secondCornerX);
/* 745 */     int maxCornerY = Math.max(firstCornerY, secondCornerY);
/* 746 */     int maxCornerZ = Math.max(firstCornerZ, secondCornerZ);
/* 748 */     int diffX = maxCornerX - minCornerX;
/* 749 */     int diffY = maxCornerY - minCornerY;
/* 750 */     int diffZ = maxCornerZ - minCornerZ;
/* 752 */     final int startCornerX = (direction.x >= 0.0D) ? minCornerX : maxCornerX;
/* 753 */     final int startCornerY = (direction.y >= 0.0D) ? minCornerY : maxCornerY;
/* 754 */     final int startCornerZ = (direction.z >= 0.0D) ? minCornerZ : maxCornerZ;
/* 756 */     ImmutableList<Direction.Axis> immutableList = Direction.axisStepOrder(direction);
/* 757 */     Direction.Axis firstVisitAxis = immutableList.get(0);
/* 758 */     Direction.Axis secondVisitAxis = immutableList.get(1);
/* 759 */     Direction.Axis thirdVisitAxis = immutableList.get(2);
/* 761 */     final Direction firstVisitDir = (direction.get(firstVisitAxis) >= 0.0D) ? firstVisitAxis.getPositive() : firstVisitAxis.getNegative();
/* 762 */     final Direction secondVisitDir = (direction.get(secondVisitAxis) >= 0.0D) ? secondVisitAxis.getPositive() : secondVisitAxis.getNegative();
/* 763 */     final Direction thirdVisitDir = (direction.get(thirdVisitAxis) >= 0.0D) ? thirdVisitAxis.getPositive() : thirdVisitAxis.getNegative();
/* 765 */     final int firstMax = firstVisitAxis.choose(diffX, diffY, diffZ);
/* 766 */     final int secondMax = secondVisitAxis.choose(diffX, diffY, diffZ);
/* 767 */     final int thirdMax = thirdVisitAxis.choose(diffX, diffY, diffZ);
/* 769 */     return () -> new AbstractIterator<BlockPos>() {
/* 770 */         private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
/*     */         
/*     */         private int firstIndex;
/*     */         
/*     */         private int secondIndex;
/*     */         
/*     */         private int thirdIndex;
/*     */         
/*     */         private boolean end;
/*     */         
/* 776 */         private final int firstDirX = firstVisitDir.getStepX();
/*     */         
/* 777 */         private final int firstDirY = firstVisitDir.getStepY();
/*     */         
/* 778 */         private final int firstDirZ = firstVisitDir.getStepZ();
/*     */         
/* 780 */         private final int secondDirX = secondVisitDir.getStepX();
/*     */         
/* 781 */         private final int secondDirY = secondVisitDir.getStepY();
/*     */         
/* 782 */         private final int secondDirZ = secondVisitDir.getStepZ();
/*     */         
/* 784 */         private final int thirdDirX = thirdVisitDir.getStepX();
/*     */         
/* 785 */         private final int thirdDirY = thirdVisitDir.getStepY();
/*     */         
/* 786 */         private final int thirdDirZ = thirdVisitDir.getStepZ();
/*     */         
/*     */         protected BlockPos computeNext() {
/* 790 */           if (this.end)
/* 791 */             return (BlockPos)endOfData(); 
/* 794 */           this.cursor.set(startCornerX + this.firstDirX * this.firstIndex + this.secondDirX * this.secondIndex + this.thirdDirX * this.thirdIndex, startCornerY + this.firstDirY * this.firstIndex + this.secondDirY * this.secondIndex + this.thirdDirY * this.thirdIndex, startCornerZ + this.firstDirZ * this.firstIndex + this.secondDirZ * this.secondIndex + this.thirdDirZ * this.thirdIndex);
/* 800 */           if (this.thirdIndex < thirdMax) {
/* 801 */             this.thirdIndex++;
/* 802 */           } else if (this.secondIndex < secondMax) {
/* 803 */             this.secondIndex++;
/* 804 */             this.thirdIndex = 0;
/* 805 */           } else if (this.firstIndex < firstMax) {
/* 806 */             this.firstIndex++;
/* 807 */             this.thirdIndex = 0;
/* 808 */             this.secondIndex = 0;
/*     */           } else {
/* 810 */             this.end = true;
/*     */           } 
/* 813 */           return this.cursor;
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\BlockPos.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */