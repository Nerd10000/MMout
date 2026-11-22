/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public final class Positioned extends Record implements ChunkTrackingView {
/*     */   private final ChunkPos center;
/*     */   
/*     */   private final int viewDistance;
/*     */   
/*     */   public Positioned(ChunkPos center, int viewDistance) {
/*  88 */     this.center = center;
/*  88 */     this.viewDistance = viewDistance;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ChunkTrackingView$Positioned;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #88	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkTrackingView$Positioned;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ChunkTrackingView$Positioned;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #88	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkTrackingView$Positioned;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ChunkTrackingView$Positioned;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #88	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/level/ChunkTrackingView$Positioned;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ChunkPos center() {
/*  88 */     return this.center;
/*     */   }
/*     */   
/*     */   public int viewDistance() {
/*  88 */     return this.viewDistance;
/*     */   }
/*     */   
/*     */   private int minX() {
/*  90 */     return this.center.x() - this.viewDistance - 1;
/*     */   }
/*     */   
/*     */   private int minZ() {
/*  94 */     return this.center.z() - this.viewDistance - 1;
/*     */   }
/*     */   
/*     */   private int maxX() {
/*  98 */     return this.center.x() + this.viewDistance + 1;
/*     */   }
/*     */   
/*     */   private int maxZ() {
/* 102 */     return this.center.z() + this.viewDistance + 1;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   boolean squareIntersects(Positioned other) {
/* 108 */     return (minX() <= other.maxX() && 
/* 109 */       maxX() >= other.minX() && 
/* 110 */       minZ() <= other.maxZ() && 
/* 111 */       maxZ() >= other.minZ());
/*     */   }
/*     */   
/*     */   public boolean contains(int chunkX, int chunkZ, boolean includeNeighbors) {
/* 116 */     return ChunkTrackingView.isWithinDistance(this.center.x(), this.center.z(), this.viewDistance, chunkX, chunkZ, includeNeighbors);
/*     */   }
/*     */   
/*     */   public void forEach(Consumer<ChunkPos> consumer) {
/* 121 */     for (int x = minX(); x <= maxX(); x++) {
/* 122 */       for (int z = minZ(); z <= maxZ(); z++) {
/* 123 */         if (contains(x, z))
/* 124 */           consumer.accept(new ChunkPos(x, z)); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ChunkTrackingView$Positioned.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */