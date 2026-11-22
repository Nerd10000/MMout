/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.Iterators;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public enum Plane implements Predicate<Direction>, Iterable<Direction> {
/* 593 */   HORIZONTAL(new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST }, new Direction.Axis[] { Direction.Axis.X, Direction.Axis.Z }),
/* 594 */   VERTICAL(new Direction[] { Direction.UP, Direction.DOWN }, new Direction.Axis[] { Direction.Axis.Y });
/*     */   
/*     */   private final Direction[] faces;
/*     */   
/*     */   private final Direction.Axis[] axis;
/*     */   
/*     */   Plane(Direction[] faces, Direction.Axis[] axis) {
/* 601 */     this.faces = faces;
/* 602 */     this.axis = axis;
/*     */   }
/*     */   
/*     */   public Direction getRandomDirection(RandomSource random) {
/* 606 */     return (Direction)Util.getRandom((Object[])this.faces, random);
/*     */   }
/*     */   
/*     */   public Direction.Axis getRandomAxis(RandomSource random) {
/* 610 */     return (Direction.Axis)Util.getRandom((Object[])this.axis, random);
/*     */   }
/*     */   
/*     */   public boolean test(Direction input) {
/* 615 */     return (input != null && input.getAxis().getPlane() == this);
/*     */   }
/*     */   
/*     */   public Iterator<Direction> iterator() {
/* 620 */     return (Iterator<Direction>)Iterators.forArray((Object[])this.faces);
/*     */   }
/*     */   
/*     */   public Stream<Direction> stream() {
/* 624 */     return Arrays.stream(this.faces);
/*     */   }
/*     */   
/*     */   public List<Direction> shuffledCopy(RandomSource random) {
/* 628 */     return Util.shuffledCopy((Object[])this.faces, random);
/*     */   }
/*     */   
/*     */   public int length() {
/* 632 */     return this.faces.length;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\Direction$Plane.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */