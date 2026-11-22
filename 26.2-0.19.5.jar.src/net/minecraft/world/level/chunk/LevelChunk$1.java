/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.entity.TickingBlockEntity;
/*    */ 
/*    */ class null implements TickingBlockEntity {
/*    */   public void tick() {}
/*    */   
/*    */   public boolean isRemoved() {
/* 75 */     return true;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 80 */     return BlockPos.ZERO;
/*    */   }
/*    */   
/*    */   public String getType() {
/* 85 */     return "<null>";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\LevelChunk$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */