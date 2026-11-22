/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class WorldGenTickAccess<T> implements LevelTickAccess<T> {
/*    */   private final Function<BlockPos, TickContainerAccess<T>> containerGetter;
/*    */   
/*    */   public WorldGenTickAccess(Function<BlockPos, TickContainerAccess<T>> containerGetter) {
/* 11 */     this.containerGetter = containerGetter;
/*    */   }
/*    */   
/*    */   public boolean hasScheduledTick(BlockPos pos, T type) {
/* 16 */     return ((TickContainerAccess<T>)this.containerGetter.apply(pos)).hasScheduledTick(pos, type);
/*    */   }
/*    */   
/*    */   public void schedule(ScheduledTick<T> tick) {
/* 21 */     ((TickContainerAccess<T>)this.containerGetter.apply(tick.pos())).schedule(tick);
/*    */   }
/*    */   
/*    */   public boolean willTickThisTick(BlockPos pos, T type) {
/* 26 */     return false;
/*    */   }
/*    */   
/*    */   public int count() {
/* 32 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\ticks\WorldGenTickAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */