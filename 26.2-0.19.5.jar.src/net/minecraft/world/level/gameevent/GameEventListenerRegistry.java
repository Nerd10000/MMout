/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface GameEventListenerRegistry {
/*  7 */   public static final GameEventListenerRegistry NOOP = new GameEventListenerRegistry() {
/*    */       public boolean isEmpty() {
/* 10 */         return true;
/*    */       }
/*    */       
/*    */       public void register(GameEventListener listener) {}
/*    */       
/*    */       public void unregister(GameEventListener listener) {}
/*    */       
/*    */       public boolean visitInRangeListeners(Holder<GameEvent> event, Vec3 sourcePosition, GameEvent.Context context, GameEventListenerRegistry.ListenerVisitor action) {
/* 23 */         return false;
/*    */       }
/*    */     };
/*    */   
/*    */   boolean isEmpty();
/*    */   
/*    */   void register(GameEventListener paramGameEventListener);
/*    */   
/*    */   void unregister(GameEventListener paramGameEventListener);
/*    */   
/*    */   boolean visitInRangeListeners(Holder<GameEvent> paramHolder, Vec3 paramVec3, GameEvent.Context paramContext, ListenerVisitor paramListenerVisitor);
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface ListenerVisitor {
/*    */     void visit(GameEventListener param1GameEventListener, Vec3 param1Vec3);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\GameEventListenerRegistry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */