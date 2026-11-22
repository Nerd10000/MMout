/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ class null extends CacheLoader<VoxelShape, Boolean> {
/*     */   public Boolean load(VoxelShape shape) {
/* 102 */     return Boolean.valueOf(!Shapes.joinIsNotEmpty(Shapes.block(), shape, BooleanOp.NOT_SAME));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\Block$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */