/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ 
/*     */ public enum SpreadType {
/* 163 */   SAME_POSITION {
/*     */     public MultifaceSpreader.SpreadPos getSpreadPos(BlockPos pos, Direction spreadDirection, Direction fromFace) {
/* 167 */       return new MultifaceSpreader.SpreadPos(pos, spreadDirection);
/*     */     }
/*     */   },
/* 170 */   SAME_PLANE {
/*     */     public MultifaceSpreader.SpreadPos getSpreadPos(BlockPos pos, Direction spreadDirection, Direction fromFace) {
/* 174 */       return new MultifaceSpreader.SpreadPos(pos.relative(spreadDirection), fromFace);
/*     */     }
/*     */   },
/* 177 */   WRAP_AROUND {
/*     */     public MultifaceSpreader.SpreadPos getSpreadPos(BlockPos pos, Direction spreadDirection, Direction fromFace) {
/* 181 */       return new MultifaceSpreader.SpreadPos(pos.relative(spreadDirection).relative(fromFace), spreadDirection.getOpposite());
/*     */     }
/*     */   };
/*     */   
/*     */   public abstract MultifaceSpreader.SpreadPos getSpreadPos(BlockPos paramBlockPos, Direction paramDirection1, Direction paramDirection2);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\MultifaceSpreader$SpreadType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */