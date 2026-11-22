/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class BlockUtil {
/*     */   public static class IntBounds {
/*     */     public final int min;
/*     */     
/*     */     public final int max;
/*     */     
/*     */     public IntBounds(int min, int max) {
/*  23 */       this.min = min;
/*  24 */       this.max = max;
/*     */     }
/*     */     
/*     */     public String toString() {
/*  29 */       return "IntBounds{min=" + this.min + ", max=" + this.max + "}";
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FoundRectangle {
/*     */     public final BlockPos minCorner;
/*     */     
/*     */     public final int axis1Size;
/*     */     
/*     */     public final int axis2Size;
/*     */     
/*     */     public FoundRectangle(BlockPos minCorner, int axis1Size, int axis2Size) {
/*  42 */       this.minCorner = minCorner;
/*  43 */       this.axis1Size = axis1Size;
/*  44 */       this.axis2Size = axis2Size;
/*     */     }
/*     */   }
/*     */   
/*     */   public static FoundRectangle getLargestRectangleAround(BlockPos center, Direction.Axis axis1, int limit1, Direction.Axis axis2, int limit2, Predicate<BlockPos> test) {
/*  62 */     BlockPos.MutableBlockPos pos = center.mutable();
/*  64 */     Direction negativeDirection1 = Direction.get(Direction.AxisDirection.NEGATIVE, axis1);
/*  65 */     Direction positiveDirection1 = negativeDirection1.getOpposite();
/*  67 */     Direction negativeDirection2 = Direction.get(Direction.AxisDirection.NEGATIVE, axis2);
/*  68 */     Direction positiveDirection2 = negativeDirection2.getOpposite();
/*  84 */     int negativeDelta1 = getLimit(test, pos.set((Vec3i)center), negativeDirection1, limit1);
/*  85 */     int positiveDelta1 = getLimit(test, pos.set((Vec3i)center), positiveDirection1, limit1);
/*  87 */     int centerIndex1 = negativeDelta1;
/*  88 */     IntBounds[] boundsByAxis1 = new IntBounds[centerIndex1 + 1 + positiveDelta1];
/* 104 */     boundsByAxis1[centerIndex1] = new IntBounds(
/* 105 */         getLimit(test, pos.set((Vec3i)center), negativeDirection2, limit2), 
/* 106 */         getLimit(test, pos.set((Vec3i)center), positiveDirection2, limit2));
/* 109 */     int centerIndex2 = (boundsByAxis1[centerIndex1]).min;
/*     */     int i;
/* 129 */     for (i = 1; i <= negativeDelta1; i++) {
/* 130 */       IntBounds lastBounds = boundsByAxis1[centerIndex1 - i - 1];
/* 131 */       boundsByAxis1[centerIndex1 - i] = new IntBounds(
/* 132 */           getLimit(test, pos.set((Vec3i)center).move(negativeDirection1, i), negativeDirection2, lastBounds.min), 
/* 133 */           getLimit(test, pos.set((Vec3i)center).move(negativeDirection1, i), positiveDirection2, lastBounds.max));
/*     */     } 
/* 137 */     for (i = 1; i <= positiveDelta1; i++) {
/* 138 */       IntBounds lastBounds = boundsByAxis1[centerIndex1 + i - 1];
/* 139 */       boundsByAxis1[centerIndex1 + i] = new IntBounds(
/* 140 */           getLimit(test, pos.set((Vec3i)center).move(positiveDirection1, i), negativeDirection2, lastBounds.min), 
/* 141 */           getLimit(test, pos.set((Vec3i)center).move(positiveDirection1, i), positiveDirection2, lastBounds.max));
/*     */     } 
/* 159 */     int minAxis1 = 0;
/* 160 */     int minAxis2 = 0;
/* 161 */     int sizeAxis1 = 0;
/* 162 */     int sizeAxis2 = 0;
/* 164 */     int[] columns = new int[boundsByAxis1.length];
/* 166 */     for (int i2 = centerIndex2; i2 >= 0; i2--) {
/* 167 */       for (int i1 = 0; i1 < boundsByAxis1.length; i1++) {
/* 168 */         IntBounds bounds2 = boundsByAxis1[i1];
/* 169 */         int min2 = centerIndex2 - bounds2.min;
/* 170 */         int max2 = centerIndex2 + bounds2.max;
/* 172 */         columns[i1] = (i2 >= min2 && i2 <= max2) ? (max2 + 1 - i2) : 0;
/*     */       } 
/* 175 */       Pair<IntBounds, Integer> rectangle = getMaxRectangleLocation(columns);
/* 176 */       IntBounds boundsAxis1 = (IntBounds)rectangle.getFirst();
/* 177 */       int newSizeAxis1 = 1 + boundsAxis1.max - boundsAxis1.min;
/* 178 */       int newSizeAxis2 = ((Integer)rectangle.getSecond()).intValue();
/* 180 */       if (newSizeAxis1 * newSizeAxis2 > sizeAxis1 * sizeAxis2) {
/* 181 */         minAxis1 = boundsAxis1.min;
/* 182 */         minAxis2 = i2;
/* 183 */         sizeAxis1 = newSizeAxis1;
/* 184 */         sizeAxis2 = newSizeAxis2;
/*     */       } 
/*     */     } 
/* 188 */     return new FoundRectangle(center
/* 189 */         .relative(axis1, minAxis1 - centerIndex1).relative(axis2, minAxis2 - centerIndex2), sizeAxis1, sizeAxis2);
/*     */   }
/*     */   
/*     */   private static int getLimit(Predicate<BlockPos> test, BlockPos.MutableBlockPos pos, Direction direction, int limit) {
/* 196 */     int max = 0;
/* 197 */     while (max < limit && test.test(pos.move(direction)))
/* 198 */       max++; 
/* 200 */     return max;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static Pair<IntBounds, Integer> getMaxRectangleLocation(int[] columns) {
/* 206 */     int maxStart = 0;
/* 207 */     int maxEnd = 0;
/* 208 */     int maxHeight = 0;
/* 210 */     IntArrayList intArrayList = new IntArrayList();
/* 211 */     intArrayList.push(0);
/* 212 */     for (int column = 1; column <= columns.length; column++) {
/* 213 */       int height = (column == columns.length) ? 0 : columns[column];
/* 214 */       while (!intArrayList.isEmpty()) {
/* 215 */         int stackHeight = columns[intArrayList.topInt()];
/* 216 */         if (height >= stackHeight) {
/* 217 */           intArrayList.push(column);
/*     */           break;
/*     */         } 
/* 221 */         intArrayList.popInt();
/* 222 */         int start = intArrayList.isEmpty() ? 0 : (intArrayList.topInt() + 1);
/* 224 */         if (stackHeight * (column - start) > maxHeight * (maxEnd - maxStart)) {
/* 225 */           maxEnd = column;
/* 226 */           maxStart = start;
/* 227 */           maxHeight = stackHeight;
/*     */         } 
/*     */       } 
/* 231 */       if (intArrayList.isEmpty())
/* 232 */         intArrayList.push(column); 
/*     */     } 
/* 236 */     return new Pair(new IntBounds(maxStart, maxEnd - 1), Integer.valueOf(maxHeight));
/*     */   }
/*     */   
/*     */   public static Optional<BlockPos> getTopConnectedBlock(BlockGetter level, BlockPos pos, Block bodyBlock, Direction growthDirection, Block headBlock) {
/*     */     BlockState forwardState;
/* 242 */     BlockPos.MutableBlockPos forwardPos = pos.mutable();
/*     */     do {
/* 245 */       forwardPos.move(growthDirection);
/* 246 */       forwardState = level.getBlockState((BlockPos)forwardPos);
/* 247 */     } while (forwardState.is(bodyBlock));
/* 249 */     if (forwardState.is(headBlock))
/* 250 */       return (Optional)Optional.of(forwardPos); 
/* 252 */     return Optional.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\BlockUtil.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */