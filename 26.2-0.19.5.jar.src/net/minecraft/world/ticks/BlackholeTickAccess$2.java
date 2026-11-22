/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ class null implements LevelTickAccess<Object> {
/*    */   public void schedule(ScheduledTick<Object> tick) {}
/*    */   
/*    */   public boolean hasScheduledTick(BlockPos pos, Object type) {
/* 30 */     return false;
/*    */   }
/*    */   
/*    */   public boolean willTickThisTick(BlockPos pos, Object type) {
/* 35 */     return false;
/*    */   }
/*    */   
/*    */   public int count() {
/* 40 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\ticks\BlackholeTickAccess$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */