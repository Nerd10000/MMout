/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ 
/*     */ enum null {
/*     */   public MultifaceSpreader.SpreadPos getSpreadPos(BlockPos pos, Direction spreadDirection, Direction fromFace) {
/* 174 */     return new MultifaceSpreader.SpreadPos(pos.relative(spreadDirection), fromFace);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\MultifaceSpreader$SpreadType$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */