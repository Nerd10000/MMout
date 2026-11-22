/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface GameEventListener {
/*    */   PositionSource getListenerSource();
/*    */   
/*    */   int getListenerRadius();
/*    */   
/*    */   boolean handleGameEvent(ServerLevel paramServerLevel, Holder<GameEvent> paramHolder, GameEvent.Context paramContext, Vec3 paramVec3);
/*    */   
/*    */   public enum DeliveryMode {
/*  9 */     UNSPECIFIED, BY_DISTANCE;
/*    */   }
/*    */   
/*    */   default DeliveryMode getDeliveryMode() {
/* 19 */     return DeliveryMode.UNSPECIFIED;
/*    */   }
/*    */   
/*    */   public static interface Provider<T extends GameEventListener> {
/*    */     T getListener();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\GameEventListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */