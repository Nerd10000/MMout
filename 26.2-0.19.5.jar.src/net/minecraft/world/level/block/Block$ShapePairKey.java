/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ final class ShapePairKey extends Record {
/*     */   private final VoxelShape first;
/*     */   
/*     */   private final VoxelShape second;
/*     */   
/*     */   private ShapePairKey(VoxelShape first, VoxelShape second) {
/* 297 */     this.first = first;
/* 297 */     this.second = second;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/Block$ShapePairKey;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #297	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/Block$ShapePairKey;
/*     */   }
/*     */   
/*     */   public VoxelShape first() {
/* 297 */     return this.first;
/*     */   }
/*     */   
/*     */   public VoxelShape second() {
/* 297 */     return this.second;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 301 */     if (o instanceof ShapePairKey) {
/* 301 */       ShapePairKey that = (ShapePairKey)o;
/* 301 */       if (this.first == that.first && this.second == that.second);
/*     */     } 
/* 301 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 306 */     return System.identityHashCode(this.first) * 31 + System.identityHashCode(this.second);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\Block$ShapePairKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */