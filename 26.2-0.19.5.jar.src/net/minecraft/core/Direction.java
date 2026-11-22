/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public enum Direction implements StringRepresentable {
/*  34 */   DOWN(0, 1, -1, "down", AxisDirection.NEGATIVE, Axis.Y, new Vec3i(0, -1, 0)),
/*  35 */   UP(1, 0, -1, "up", AxisDirection.POSITIVE, Axis.Y, new Vec3i(0, 1, 0)),
/*  36 */   NORTH(2, 3, 2, "north", AxisDirection.NEGATIVE, Axis.Z, new Vec3i(0, 0, -1)),
/*  37 */   SOUTH(3, 2, 0, "south", AxisDirection.POSITIVE, Axis.Z, new Vec3i(0, 0, 1)),
/*  38 */   WEST(4, 5, 1, "west", AxisDirection.NEGATIVE, Axis.X, new Vec3i(-1, 0, 0)),
/*  39 */   EAST(5, 4, 3, "east", AxisDirection.POSITIVE, Axis.X, new Vec3i(1, 0, 0));
/*     */   
/*     */   public static final StringRepresentable.EnumCodec<Direction> CODEC;
/*     */   
/*     */   public static final Codec<Direction> VERTICAL_CODEC;
/*     */   
/*     */   public static final IntFunction<Direction> BY_ID;
/*     */   
/*     */   public static final StreamCodec<ByteBuf, Direction> STREAM_CODEC;
/*     */   
/*     */   @Deprecated
/*     */   public static final Codec<Direction> LEGACY_ID_CODEC;
/*     */   
/*     */   @Deprecated
/*     */   public static final Codec<Direction> LEGACY_ID_CODEC_2D;
/*     */   
/*     */   private static final ImmutableList<Axis> YXZ_AXIS_ORDER;
/*     */   
/*     */   private static final ImmutableList<Axis> YZX_AXIS_ORDER;
/*     */   
/*     */   private final int data3d;
/*     */   
/*     */   private final int oppositeIndex;
/*     */   
/*     */   private final int data2d;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final Axis axis;
/*     */   
/*     */   private final AxisDirection axisDirection;
/*     */   
/*     */   private final Vec3i normal;
/*     */   
/*     */   private final Vec3 normalVec3;
/*     */   
/*     */   private final Vector3fc normalVec3f;
/*     */   
/*     */   private static final Direction[] VALUES;
/*     */   
/*     */   private static final Direction[] BY_3D_DATA;
/*     */   
/*     */   private static final Direction[] BY_2D_DATA;
/*     */   
/*     */   static {
/*  42 */     CODEC = StringRepresentable.fromEnum(Direction::values);
/*  43 */     VERTICAL_CODEC = CODEC.validate(Direction::verifyVertical);
/*  45 */     BY_ID = ByIdMap.continuous(Direction::get3DDataValue, (Object[])values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/*  47 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Direction::get3DDataValue);
/*  50 */     LEGACY_ID_CODEC = Codec.BYTE.xmap(Direction::from3DDataValue, d -> Byte.valueOf((byte)d.get3DDataValue()));
/*  52 */     LEGACY_ID_CODEC_2D = Codec.BYTE.xmap(Direction::from2DDataValue, d -> Byte.valueOf((byte)d.get2DDataValue()));
/*  54 */     YXZ_AXIS_ORDER = ImmutableList.of(Axis.Y, Axis.X, Axis.Z);
/*  55 */     YZX_AXIS_ORDER = ImmutableList.of(Axis.Y, Axis.Z, Axis.X);
/*  67 */     VALUES = values();
/*  69 */     BY_3D_DATA = (Direction[])Arrays.<Direction>stream(VALUES).sorted(Comparator.comparingInt(d -> d.data3d)).toArray(x$0 -> new Direction[x$0]);
/*  70 */     BY_2D_DATA = (Direction[])Arrays.<Direction>stream(VALUES).filter(d -> d.getAxis().isHorizontal()).sorted(Comparator.comparingInt(d -> d.data2d)).toArray(x$0 -> new Direction[x$0]);
/*     */   }
/*     */   
/*     */   Direction(int data3d, int oppositeIndex, int data2d, String name, AxisDirection axisDirection, Axis axis, Vec3i normal) {
/*  73 */     this.data3d = data3d;
/*  74 */     this.data2d = data2d;
/*  75 */     this.oppositeIndex = oppositeIndex;
/*  76 */     this.name = name;
/*  77 */     this.axis = axis;
/*  78 */     this.axisDirection = axisDirection;
/*  79 */     this.normal = normal;
/*  80 */     this.normalVec3 = Vec3.atLowerCornerOf(normal);
/*  81 */     this.normalVec3f = (Vector3fc)new Vector3f(normal.getX(), normal.getY(), normal.getZ());
/*     */   }
/*     */   
/*     */   public static Direction[] orderedByNearest(Entity entity) {
/*  85 */     float pitch = entity.getViewXRot(1.0F) * 0.017453292F;
/*  86 */     float yaw = -entity.getViewYRot(1.0F) * 0.017453292F;
/*  88 */     float pitchSin = Mth.sin(pitch);
/*  89 */     float pitchCos = Mth.cos(pitch);
/*  90 */     float yawSin = Mth.sin(yaw);
/*  91 */     float yawCos = Mth.cos(yaw);
/*  93 */     boolean xPos = (yawSin > 0.0F);
/*  94 */     boolean yPos = (pitchSin < 0.0F);
/*  95 */     boolean zPos = (yawCos > 0.0F);
/*  97 */     float xYaw = xPos ? yawSin : -yawSin;
/*  98 */     float yMag = yPos ? -pitchSin : pitchSin;
/*  99 */     float zYaw = zPos ? yawCos : -yawCos;
/* 101 */     float xMag = xYaw * pitchCos;
/* 102 */     float zMag = zYaw * pitchCos;
/* 104 */     Direction axisX = xPos ? EAST : WEST;
/* 105 */     Direction axisY = yPos ? UP : DOWN;
/* 106 */     Direction axisZ = zPos ? SOUTH : NORTH;
/* 108 */     if (xYaw > zYaw) {
/* 109 */       if (yMag > xMag)
/* 110 */         return makeDirectionArray(axisY, axisX, axisZ); 
/* 111 */       if (zMag > yMag)
/* 112 */         return makeDirectionArray(axisX, axisZ, axisY); 
/* 114 */       return makeDirectionArray(axisX, axisY, axisZ);
/*     */     } 
/* 117 */     if (yMag > zMag)
/* 118 */       return makeDirectionArray(axisY, axisZ, axisX); 
/* 119 */     if (xMag > yMag)
/* 120 */       return makeDirectionArray(axisZ, axisX, axisY); 
/* 122 */     return makeDirectionArray(axisZ, axisY, axisX);
/*     */   }
/*     */   
/*     */   private static Direction[] makeDirectionArray(Direction axis1, Direction axis2, Direction axis3) {
/* 128 */     return new Direction[] { axis1, axis2, axis3, axis3.getOpposite(), axis2.getOpposite(), axis1.getOpposite() };
/*     */   }
/*     */   
/*     */   public static Direction rotate(Matrix4fc matrix, Direction facing) {
/* 132 */     Vector3f vec = matrix.transformDirection(facing.normalVec3f, new Vector3f());
/* 133 */     return getApproximateNearest(vec.x(), vec.y(), vec.z());
/*     */   }
/*     */   
/*     */   public static Collection<Direction> allShuffled(RandomSource random) {
/* 137 */     return Util.shuffledCopy((Object[])values(), random);
/*     */   }
/*     */   
/*     */   public static Stream<Direction> stream() {
/* 141 */     return Stream.of(VALUES);
/*     */   }
/*     */   
/*     */   public static float getYRot(Direction direction) {
/* 145 */     switch (direction.ordinal()) {
/*     */       case 2:
/*     */       
/*     */       case 3:
/*     */       
/*     */       case 4:
/*     */       
/*     */       case 5:
/*     */       
/*     */     } 
/* 150 */     throw new IllegalStateException("No y-Rot for vertical axis: " + String.valueOf(direction));
/*     */   }
/*     */   
/*     */   public Quaternionf getRotation() {
/* 155 */     switch (ordinal()) {
/*     */       default:
/* 155 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */       
/*     */       case 3:
/*     */       
/*     */       case 4:
/*     */       
/*     */       case 5:
/*     */         break;
/*     */     } 
/* 155 */     return (
/*     */       
/* 161 */       new Quaternionf()).rotationXYZ(1.5707964F, 0.0F, -1.5707964F);
/*     */   }
/*     */   
/*     */   public int get3DDataValue() {
/* 166 */     return this.data3d;
/*     */   }
/*     */   
/*     */   public int get2DDataValue() {
/* 170 */     return this.data2d;
/*     */   }
/*     */   
/*     */   public AxisDirection getAxisDirection() {
/* 174 */     return this.axisDirection;
/*     */   }
/*     */   
/*     */   public static Direction getFacingAxis(Entity entity, Axis axis) {
/* 179 */     switch (axis.ordinal()) {
/*     */       default:
/* 179 */         throw new MatchException(null, null);
/*     */       case 0:
/* 179 */         return 
/* 180 */           EAST.isFacingAngle(entity.getViewYRot(1.0F)) ? EAST : WEST;
/*     */       case 2:
/* 181 */         return SOUTH.isFacingAngle(entity.getViewYRot(1.0F)) ? SOUTH : NORTH;
/*     */       case 1:
/*     */         break;
/*     */     } 
/* 182 */     return (entity.getViewXRot(1.0F) < 0.0F) ? UP : DOWN;
/*     */   }
/*     */   
/*     */   public Direction getOpposite() {
/* 187 */     return from3DDataValue(this.oppositeIndex);
/*     */   }
/*     */   
/*     */   public Direction getClockWise(Axis axis) {
/* 191 */     switch (axis.ordinal()) {
/*     */       default:
/* 191 */         throw new MatchException(null, null);
/*     */       case 0:
/* 191 */         return 
/* 192 */           (this == WEST || this == EAST) ? this : getClockWiseX();
/*     */       case 1:
/* 193 */         return (this == UP || this == DOWN) ? this : getClockWise();
/*     */       case 2:
/*     */         break;
/*     */     } 
/* 194 */     return (this == NORTH || this == SOUTH) ? this : getClockWiseZ();
/*     */   }
/*     */   
/*     */   public Direction getCounterClockWise(Axis axis) {
/* 199 */     switch (axis.ordinal()) {
/*     */       default:
/* 199 */         throw new MatchException(null, null);
/*     */       case 0:
/* 199 */         return 
/* 200 */           (this == WEST || this == EAST) ? this : getCounterClockWiseX();
/*     */       case 1:
/* 201 */         return (this == UP || this == DOWN) ? this : getCounterClockWise();
/*     */       case 2:
/*     */         break;
/*     */     } 
/* 202 */     return (this == NORTH || this == SOUTH) ? this : getCounterClockWiseZ();
/*     */   }
/*     */   
/*     */   public Direction getClockWise() {
/* 207 */     switch (ordinal()) {
/*     */       case 2:
/*     */       
/*     */       case 5:
/*     */       
/*     */       case 3:
/*     */       
/*     */       case 4:
/*     */       
/*     */     } 
/* 212 */     throw new IllegalStateException("Unable to get Y-rotated facing of " + String.valueOf(this));
/*     */   }
/*     */   
/*     */   private Direction getClockWiseX() {
/* 217 */     switch (ordinal()) {
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */       
/*     */       case 0:
/*     */       
/*     */       case 3:
/*     */       
/*     */     } 
/* 222 */     throw new IllegalStateException("Unable to get X-rotated facing of " + String.valueOf(this));
/*     */   }
/*     */   
/*     */   private Direction getCounterClockWiseX() {
/* 227 */     switch (ordinal()) {
/*     */       case 1:
/*     */       
/*     */       case 3:
/*     */       
/*     */       case 0:
/*     */       
/*     */       case 2:
/*     */       
/*     */     } 
/* 232 */     throw new IllegalStateException("Unable to get X-rotated facing of " + String.valueOf(this));
/*     */   }
/*     */   
/*     */   private Direction getClockWiseZ() {
/* 237 */     switch (ordinal()) {
/*     */       case 1:
/*     */       
/*     */       case 5:
/*     */       
/*     */       case 0:
/*     */       
/*     */       case 4:
/*     */       
/*     */     } 
/* 242 */     throw new IllegalStateException("Unable to get Z-rotated facing of " + String.valueOf(this));
/*     */   }
/*     */   
/*     */   private Direction getCounterClockWiseZ() {
/* 247 */     switch (ordinal()) {
/*     */       case 1:
/*     */       
/*     */       case 4:
/*     */       
/*     */       case 0:
/*     */       
/*     */       case 5:
/*     */       
/*     */     } 
/* 252 */     throw new IllegalStateException("Unable to get Z-rotated facing of " + String.valueOf(this));
/*     */   }
/*     */   
/*     */   public Direction getCounterClockWise() {
/* 257 */     switch (ordinal()) {
/*     */       case 2:
/*     */       
/*     */       case 5:
/*     */       
/*     */       case 3:
/*     */       
/*     */       case 4:
/*     */       
/*     */     } 
/* 262 */     throw new IllegalStateException("Unable to get CCW facing of " + String.valueOf(this));
/*     */   }
/*     */   
/*     */   public int getStepX() {
/* 267 */     return this.normal.getX();
/*     */   }
/*     */   
/*     */   public int getStepY() {
/* 271 */     return this.normal.getY();
/*     */   }
/*     */   
/*     */   public int getStepZ() {
/* 275 */     return this.normal.getZ();
/*     */   }
/*     */   
/*     */   public Vector3f step() {
/* 279 */     return new Vector3f(this.normalVec3f);
/*     */   }
/*     */   
/*     */   public String getName() {
/* 283 */     return this.name;
/*     */   }
/*     */   
/*     */   public Axis getAxis() {
/* 287 */     return this.axis;
/*     */   }
/*     */   
/*     */   public static Direction byName(String name) {
/* 291 */     return (Direction)CODEC.byName(name);
/*     */   }
/*     */   
/*     */   public static Direction from3DDataValue(int data) {
/* 295 */     return BY_3D_DATA[Mth.abs(data % BY_3D_DATA.length)];
/*     */   }
/*     */   
/*     */   public static Direction from2DDataValue(int data) {
/* 299 */     return BY_2D_DATA[Mth.abs(data % BY_2D_DATA.length)];
/*     */   }
/*     */   
/*     */   public static Direction fromYRot(double yRot) {
/* 303 */     return from2DDataValue(Mth.floor(yRot / 90.0D + 0.5D) & 0x3);
/*     */   }
/*     */   
/*     */   public static Direction fromAxisAndDirection(Axis axis, AxisDirection direction) {
/* 307 */     switch (axis.ordinal()) {
/*     */       default:
/* 307 */         throw new MatchException(null, null);
/*     */       case 0:
/* 307 */         return 
/* 308 */           (direction == AxisDirection.POSITIVE) ? EAST : WEST;
/*     */       case 1:
/* 309 */         return (direction == AxisDirection.POSITIVE) ? UP : DOWN;
/*     */       case 2:
/*     */         break;
/*     */     } 
/* 310 */     return (direction == AxisDirection.POSITIVE) ? SOUTH : NORTH;
/*     */   }
/*     */   
/*     */   public float toYRot() {
/* 315 */     return ((this.data2d & 0x3) * 90);
/*     */   }
/*     */   
/*     */   public static Direction getRandom(RandomSource random) {
/* 319 */     return (Direction)Util.getRandom((Object[])VALUES, random);
/*     */   }
/*     */   
/*     */   public static Direction getApproximateNearest(double dx, double dy, double dz) {
/* 323 */     return getApproximateNearest((float)dx, (float)dy, (float)dz);
/*     */   }
/*     */   
/*     */   public static Direction getApproximateNearest(float dx, float dy, float dz) {
/* 327 */     Direction result = NORTH;
/* 328 */     float highestDot = Float.MIN_VALUE;
/* 329 */     for (Direction direction : VALUES) {
/* 330 */       float dot = dx * direction.normal.getX() + dy * direction.normal.getY() + dz * direction.normal.getZ();
/* 332 */       if (dot > highestDot) {
/* 333 */         highestDot = dot;
/* 334 */         result = direction;
/*     */       } 
/*     */     } 
/* 337 */     return result;
/*     */   }
/*     */   
/*     */   public static Direction getApproximateNearest(Vec3 vec) {
/* 341 */     return getApproximateNearest(vec.x, vec.y, vec.z);
/*     */   }
/*     */   
/*     */   @Contract("_,_,_,!null->!null;_,_,_,_->_")
/*     */   public static Direction getNearest(int x, int y, int z, Direction orElse) {
/* 346 */     int absX = Math.abs(x);
/* 347 */     int absY = Math.abs(y);
/* 348 */     int absZ = Math.abs(z);
/* 349 */     if (absX > absZ && absX > absY)
/* 350 */       return (x < 0) ? WEST : EAST; 
/* 351 */     if (absZ > absX && absZ > absY)
/* 352 */       return (z < 0) ? NORTH : SOUTH; 
/* 353 */     if (absY > absX && absY > absZ)
/* 354 */       return (y < 0) ? DOWN : UP; 
/* 356 */     return orElse;
/*     */   }
/*     */   
/*     */   @Contract("_,!null->!null;_,_->_")
/*     */   public static Direction getNearest(Vec3i vec, Direction orElse) {
/* 361 */     return getNearest(vec.getX(), vec.getY(), vec.getZ(), orElse);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 366 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 371 */     return this.name;
/*     */   }
/*     */   
/*     */   private static DataResult<Direction> verifyVertical(Direction v) {
/* 375 */     return v.getAxis().isVertical() ? DataResult.success(v) : DataResult.error(() -> "Expected a vertical direction");
/*     */   }
/*     */   
/*     */   public static Direction get(AxisDirection axisDirection, Axis axis) {
/* 379 */     for (Direction direction : VALUES) {
/* 380 */       if (direction.getAxisDirection() == axisDirection && direction.getAxis() == axis)
/* 381 */         return direction; 
/*     */     } 
/* 384 */     throw new IllegalArgumentException("No such direction: " + String.valueOf(axisDirection) + " " + String.valueOf(axis));
/*     */   }
/*     */   
/*     */   public static ImmutableList<Axis> axisStepOrder(Vec3 movement) {
/* 388 */     if (Math.abs(movement.x) < Math.abs(movement.z))
/* 389 */       return YZX_AXIS_ORDER; 
/* 391 */     return YXZ_AXIS_ORDER;
/*     */   }
/*     */   
/*     */   public enum Axis implements Predicate<Direction>, StringRepresentable {
/* 395 */     X("x") {
/*     */       public int choose(int x, int y, int z) {
/* 398 */         return x;
/*     */       }
/*     */       
/*     */       public boolean choose(boolean x, boolean y, boolean z) {
/* 403 */         return x;
/*     */       }
/*     */       
/*     */       public double choose(double x, double y, double z) {
/* 408 */         return x;
/*     */       }
/*     */       
/*     */       public Direction getPositive() {
/* 413 */         return Direction.EAST;
/*     */       }
/*     */       
/*     */       public Direction getNegative() {
/* 418 */         return Direction.WEST;
/*     */       }
/*     */     },
/* 421 */     Y("y") {
/*     */       public int choose(int x, int y, int z) {
/* 424 */         return y;
/*     */       }
/*     */       
/*     */       public double choose(double x, double y, double z) {
/* 429 */         return y;
/*     */       }
/*     */       
/*     */       public boolean choose(boolean x, boolean y, boolean z) {
/* 434 */         return y;
/*     */       }
/*     */       
/*     */       public Direction getPositive() {
/* 439 */         return Direction.UP;
/*     */       }
/*     */       
/*     */       public Direction getNegative() {
/* 444 */         return Direction.DOWN;
/*     */       }
/*     */     },
/* 447 */     Z("z") {
/*     */       public int choose(int x, int y, int z) {
/* 450 */         return z;
/*     */       }
/*     */       
/*     */       public double choose(double x, double y, double z) {
/* 455 */         return z;
/*     */       }
/*     */       
/*     */       public boolean choose(boolean x, boolean y, boolean z) {
/* 460 */         return z;
/*     */       }
/*     */       
/*     */       public Direction getPositive() {
/* 465 */         return Direction.SOUTH;
/*     */       }
/*     */       
/*     */       public Direction getNegative() {
/* 470 */         return Direction.NORTH;
/*     */       }
/*     */     };
/*     */     
/* 475 */     public static final Axis[] VALUES = values();
/*     */     
/* 477 */     public static final StringRepresentable.EnumCodec<Axis> CODEC = StringRepresentable.fromEnum(Axis::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     Axis(String name) {
/* 482 */       this.name = name;
/*     */     }
/*     */     
/*     */     public static Axis byName(String name) {
/* 486 */       return (Axis)CODEC.byName(name);
/*     */     }
/*     */     
/*     */     public String getName() {
/* 490 */       return this.name;
/*     */     }
/*     */     
/*     */     public boolean isVertical() {
/* 494 */       return (this == Y);
/*     */     }
/*     */     
/*     */     public boolean isHorizontal() {
/* 498 */       return (this == X || this == Z);
/*     */     }
/*     */     
/*     */     public Direction[] getDirections() {
/* 506 */       return new Direction[] { getPositive(), getNegative() };
/*     */     }
/*     */     
/*     */     public String toString() {
/* 511 */       return this.name;
/*     */     }
/*     */     
/*     */     public static Axis getRandom(RandomSource random) {
/* 515 */       return (Axis)Util.getRandom((Object[])VALUES, random);
/*     */     }
/*     */     
/*     */     public boolean test(Direction input) {
/* 520 */       return (input != null && input.getAxis() == this);
/*     */     }
/*     */     
/*     */     public Direction.Plane getPlane() {
/* 524 */       switch (ordinal()) {
/*     */         default:
/* 524 */           throw new MatchException(null, null);
/*     */         case 0:
/*     */         case 2:
/*     */         
/*     */         case 1:
/*     */           break;
/*     */       } 
/* 524 */       return 
/*     */         
/* 526 */         Direction.Plane.VERTICAL;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 532 */       return this.name;
/*     */     }
/*     */     
/*     */     public abstract Direction getPositive();
/*     */     
/*     */     public abstract Direction getNegative();
/*     */     
/*     */     public abstract int choose(int param1Int1, int param1Int2, int param1Int3);
/*     */     
/*     */     public abstract double choose(double param1Double1, double param1Double2, double param1Double3);
/*     */     
/*     */     public abstract boolean choose(boolean param1Boolean1, boolean param1Boolean2, boolean param1Boolean3);
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public int choose(int x, int y, int z) {
/*     */       return x;
/*     */     }
/*     */     
/*     */     public boolean choose(boolean x, boolean y, boolean z) {
/*     */       return x;
/*     */     }
/*     */     
/*     */     public double choose(double x, double y, double z) {
/*     */       return x;
/*     */     }
/*     */     
/*     */     public Direction getPositive() {
/*     */       return Direction.EAST;
/*     */     }
/*     */     
/*     */     public Direction getNegative() {
/*     */       return Direction.WEST;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public int choose(int x, int y, int z) {
/*     */       return y;
/*     */     }
/*     */     
/*     */     public double choose(double x, double y, double z) {
/*     */       return y;
/*     */     }
/*     */     
/*     */     public boolean choose(boolean x, boolean y, boolean z) {
/*     */       return y;
/*     */     }
/*     */     
/*     */     public Direction getPositive() {
/*     */       return Direction.UP;
/*     */     }
/*     */     
/*     */     public Direction getNegative() {
/*     */       return Direction.DOWN;
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public int choose(int x, int y, int z) {
/*     */       return z;
/*     */     }
/*     */     
/*     */     public double choose(double x, double y, double z) {
/*     */       return z;
/*     */     }
/*     */     
/*     */     public boolean choose(boolean x, boolean y, boolean z) {
/*     */       return z;
/*     */     }
/*     */     
/*     */     public Direction getPositive() {
/*     */       return Direction.SOUTH;
/*     */     }
/*     */     
/*     */     public Direction getNegative() {
/*     */       return Direction.NORTH;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum AxisDirection {
/* 543 */     POSITIVE(1, "Towards positive"),
/* 544 */     NEGATIVE(-1, "Towards negative");
/*     */     
/*     */     private final int step;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     AxisDirection(int step, String name) {
/* 551 */       this.step = step;
/* 552 */       this.name = name;
/*     */     }
/*     */     
/*     */     public int getStep() {
/* 556 */       return this.step;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 560 */       return this.name;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 565 */       return this.name;
/*     */     }
/*     */     
/*     */     public AxisDirection opposite() {
/* 569 */       return (this == POSITIVE) ? NEGATIVE : POSITIVE;
/*     */     }
/*     */   }
/*     */   
/*     */   public Vec3i getUnitVec3i() {
/* 574 */     return this.normal;
/*     */   }
/*     */   
/*     */   public Vec3 getUnitVec3() {
/* 578 */     return this.normalVec3;
/*     */   }
/*     */   
/*     */   public Vector3fc getUnitVec3f() {
/* 582 */     return this.normalVec3f;
/*     */   }
/*     */   
/*     */   public boolean isFacingAngle(float yAngle) {
/* 586 */     float radians = yAngle * 0.017453292F;
/* 587 */     float dx = -Mth.sin(radians);
/* 588 */     float dz = Mth.cos(radians);
/* 589 */     return (this.normal.getX() * dx + this.normal.getZ() * dz > 0.0F);
/*     */   }
/*     */   
/*     */   public enum Plane implements Predicate<Direction>, Iterable<Direction> {
/* 593 */     HORIZONTAL((String)new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST }, new Direction.Axis[] { Direction.Axis.X, Direction.Axis.Z }),
/* 594 */     VERTICAL((String)new Direction[] { Direction.UP, Direction.DOWN }, new Direction.Axis[] { Direction.Axis.Y });
/*     */     
/*     */     private final Direction[] faces;
/*     */     
/*     */     private final Direction.Axis[] axis;
/*     */     
/*     */     Plane(Direction[] faces, Direction.Axis[] axis) {
/* 601 */       this.faces = faces;
/* 602 */       this.axis = axis;
/*     */     }
/*     */     
/*     */     public Direction getRandomDirection(RandomSource random) {
/* 606 */       return (Direction)Util.getRandom((Object[])this.faces, random);
/*     */     }
/*     */     
/*     */     public Direction.Axis getRandomAxis(RandomSource random) {
/* 610 */       return (Direction.Axis)Util.getRandom((Object[])this.axis, random);
/*     */     }
/*     */     
/*     */     public boolean test(Direction input) {
/* 615 */       return (input != null && input.getAxis().getPlane() == this);
/*     */     }
/*     */     
/*     */     public Iterator<Direction> iterator() {
/* 620 */       return (Iterator<Direction>)Iterators.forArray((Object[])this.faces);
/*     */     }
/*     */     
/*     */     public Stream<Direction> stream() {
/* 624 */       return Arrays.stream(this.faces);
/*     */     }
/*     */     
/*     */     public List<Direction> shuffledCopy(RandomSource random) {
/* 628 */       return Util.shuffledCopy((Object[])this.faces, random);
/*     */     }
/*     */     
/*     */     public int length() {
/* 632 */       return this.faces.length;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\Direction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */