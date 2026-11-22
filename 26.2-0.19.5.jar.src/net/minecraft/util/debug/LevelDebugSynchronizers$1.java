/*    */ package net.minecraft.util.debug;
/*    */ 
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ class null implements DebugValueSource.Registration {
/*    */   public <T> void register(DebugSubscription<T> subscription, DebugValueSource.ValueGetter<T> getter) {
/* 94 */     LevelDebugSynchronizers.this.<T>getSourceSynchronizer(subscription).registerChunk(chunk.getPos(), getter);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\LevelDebugSynchronizers$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */