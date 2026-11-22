/*    */ package net.minecraft.world.level.chunk.status;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.Locale;
/*    */ 
/*    */ public final class ChunkDependencies {
/*    */   private final ImmutableList<ChunkStatus> dependencyByRadius;
/*    */   
/*    */   private final int[] radiusByDependency;
/*    */   
/*    */   public ChunkDependencies(ImmutableList<ChunkStatus> dependencyByRadius) {
/* 21 */     this.dependencyByRadius = dependencyByRadius;
/* 22 */     int size = dependencyByRadius.isEmpty() ? 0 : (((ChunkStatus)dependencyByRadius.getFirst()).getIndex() + 1);
/* 23 */     this.radiusByDependency = new int[size];
/* 24 */     for (int radius = 0; radius < dependencyByRadius.size(); radius++) {
/* 25 */       ChunkStatus dependency = (ChunkStatus)dependencyByRadius.get(radius);
/* 26 */       int index = dependency.getIndex();
/* 27 */       for (int statusIndex = 0; statusIndex <= index; statusIndex++)
/* 28 */         this.radiusByDependency[statusIndex] = radius; 
/*    */     } 
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public ImmutableList<ChunkStatus> asList() {
/* 35 */     return this.dependencyByRadius;
/*    */   }
/*    */   
/*    */   public int size() {
/* 39 */     return this.dependencyByRadius.size();
/*    */   }
/*    */   
/*    */   public int getRadiusOf(ChunkStatus status) {
/* 43 */     int index = status.getIndex();
/* 44 */     if (index >= this.radiusByDependency.length)
/* 45 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "Requesting a ChunkStatus(%s) outside of dependency range(%s)", new Object[] { status, this.dependencyByRadius })); 
/* 47 */     return this.radiusByDependency[index];
/*    */   }
/*    */   
/*    */   public int getRadius() {
/* 51 */     return Math.max(0, this.dependencyByRadius.size() - 1);
/*    */   }
/*    */   
/*    */   public ChunkStatus get(int distance) {
/* 55 */     return (ChunkStatus)this.dependencyByRadius.get(distance);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 60 */     return this.dependencyByRadius.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\status\ChunkDependencies.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */