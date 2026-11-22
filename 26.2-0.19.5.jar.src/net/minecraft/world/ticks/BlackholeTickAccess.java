/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class BlackholeTickAccess {
/*  7 */   private static final TickContainerAccess<Object> CONTAINER_BLACKHOLE = new TickContainerAccess() {
/*    */       public void schedule(ScheduledTick<Object> tick) {}
/*    */       
/*    */       public boolean hasScheduledTick(BlockPos pos, Object type) {
/* 14 */         return false;
/*    */       }
/*    */       
/*    */       public int count() {
/* 19 */         return 0;
/*    */       }
/*    */     };
/*    */   
/* 23 */   private static final LevelTickAccess<Object> LEVEL_BLACKHOLE = new LevelTickAccess() {
/*    */       public void schedule(ScheduledTick<Object> tick) {}
/*    */       
/*    */       public boolean hasScheduledTick(BlockPos pos, Object type) {
/* 30 */         return false;
/*    */       }
/*    */       
/*    */       public boolean willTickThisTick(BlockPos pos, Object type) {
/* 35 */         return false;
/*    */       }
/*    */       
/*    */       public int count() {
/* 40 */         return 0;
/*    */       }
/*    */     };
/*    */   
/*    */   public static <T> TickContainerAccess<T> emptyContainer() {
/* 46 */     return (TickContainerAccess)CONTAINER_BLACKHOLE;
/*    */   }
/*    */   
/*    */   public static <T> LevelTickAccess<T> emptyLevelList() {
/* 51 */     return (LevelTickAccess)LEVEL_BLACKHOLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\ticks\BlackholeTickAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */