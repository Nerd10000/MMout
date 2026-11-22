/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ class null implements GameEventListenerRegistry {
/*    */   public boolean isEmpty() {
/* 10 */     return true;
/*    */   }
/*    */   
/*    */   public void register(GameEventListener listener) {}
/*    */   
/*    */   public void unregister(GameEventListener listener) {}
/*    */   
/*    */   public boolean visitInRangeListeners(Holder<GameEvent> event, Vec3 sourcePosition, GameEvent.Context context, GameEventListenerRegistry.ListenerVisitor action) {
/* 23 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\GameEventListenerRegistry$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */