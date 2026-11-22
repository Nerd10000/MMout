/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public enum Axis implements Predicate<Direction>, StringRepresentable {
/* 395 */   X("x") {
/*     */     public int choose(int x, int y, int z) {
/* 398 */       return x;
/*     */     }
/*     */     
/*     */     public boolean choose(boolean x, boolean y, boolean z) {
/* 403 */       return x;
/*     */     }
/*     */     
/*     */     public double choose(double x, double y, double z) {
/* 408 */       return x;
/*     */     }
/*     */     
/*     */     public Direction getPositive() {
/* 413 */       return Direction.EAST;
/*     */     }
/*     */     
/*     */     public Direction getNegative() {
/* 418 */       return Direction.WEST;
/*     */     }
/*     */   },
/* 421 */   Y("y") {
/*     */     public int choose(int x, int y, int z) {
/* 424 */       return y;
/*     */     }
/*     */     
/*     */     public double choose(double x, double y, double z) {
/* 429 */       return y;
/*     */     }
/*     */     
/*     */     public boolean choose(boolean x, boolean y, boolean z) {
/* 434 */       return y;
/*     */     }
/*     */     
/*     */     public Direction getPositive() {
/* 439 */       return Direction.UP;
/*     */     }
/*     */     
/*     */     public Direction getNegative() {
/* 444 */       return Direction.DOWN;
/*     */     }
/*     */   },
/* 447 */   Z("z") {
/*     */     public int choose(int x, int y, int z) {
/* 450 */       return z;
/*     */     }
/*     */     
/*     */     public double choose(double x, double y, double z) {
/* 455 */       return z;
/*     */     }
/*     */     
/*     */     public boolean choose(boolean x, boolean y, boolean z) {
/* 460 */       return z;
/*     */     }
/*     */     
/*     */     public Direction getPositive() {
/* 465 */       return Direction.SOUTH;
/*     */     }
/*     */     
/*     */     public Direction getNegative() {
/* 470 */       return Direction.NORTH;
/*     */     }
/*     */   };
/*     */   
/*     */   public static final Axis[] VALUES;
/*     */   
/*     */   public static final StringRepresentable.EnumCodec<Axis> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/* 475 */     VALUES = values();
/* 477 */     CODEC = StringRepresentable.fromEnum(Axis::values);
/*     */   }
/*     */   
/*     */   Axis(String name) {
/* 482 */     this.name = name;
/*     */   }
/*     */   
/*     */   public static Axis byName(String name) {
/* 486 */     return (Axis)CODEC.byName(name);
/*     */   }
/*     */   
/*     */   public String getName() {
/* 490 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isVertical() {
/* 494 */     return (this == Y);
/*     */   }
/*     */   
/*     */   public boolean isHorizontal() {
/* 498 */     return (this == X || this == Z);
/*     */   }
/*     */   
/*     */   public Direction[] getDirections() {
/* 506 */     return new Direction[] { getPositive(), getNegative() };
/*     */   }
/*     */   
/*     */   public String toString() {
/* 511 */     return this.name;
/*     */   }
/*     */   
/*     */   public static Axis getRandom(RandomSource random) {
/* 515 */     return (Axis)Util.getRandom((Object[])VALUES, random);
/*     */   }
/*     */   
/*     */   public boolean test(Direction input) {
/* 520 */     return (input != null && input.getAxis() == this);
/*     */   }
/*     */   
/*     */   public Direction.Plane getPlane() {
/* 524 */     switch (ordinal()) {
/*     */       default:
/* 524 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       case 2:
/*     */       
/*     */       case 1:
/*     */         break;
/*     */     } 
/* 524 */     return 
/*     */       
/* 526 */       Direction.Plane.VERTICAL;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 532 */     return this.name;
/*     */   }
/*     */   
/*     */   public abstract Direction getPositive();
/*     */   
/*     */   public abstract Direction getNegative();
/*     */   
/*     */   public abstract int choose(int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   public abstract double choose(double paramDouble1, double paramDouble2, double paramDouble3);
/*     */   
/*     */   public abstract boolean choose(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\Direction$Axis.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */