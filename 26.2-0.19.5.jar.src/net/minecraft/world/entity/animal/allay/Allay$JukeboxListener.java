/*     */ package net.minecraft.world.entity.animal.allay;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class JukeboxListener implements GameEventListener {
/*     */   private final PositionSource listenerSource;
/*     */   
/*     */   private final int listenerRadius;
/*     */   
/*     */   public JukeboxListener(PositionSource listenerSource, int listenerRadius) {
/* 572 */     this.listenerSource = listenerSource;
/* 573 */     this.listenerRadius = listenerRadius;
/*     */   }
/*     */   
/*     */   public PositionSource getListenerSource() {
/* 578 */     return this.listenerSource;
/*     */   }
/*     */   
/*     */   public int getListenerRadius() {
/* 583 */     return this.listenerRadius;
/*     */   }
/*     */   
/*     */   public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 sourcePosition) {
/* 588 */     if (event.is((Holder)GameEvent.JUKEBOX_PLAY)) {
/* 589 */       Allay.this.setJukeboxPlaying(BlockPos.containing((Position)sourcePosition), true);
/* 590 */       return true;
/*     */     } 
/* 593 */     if (event.is((Holder)GameEvent.JUKEBOX_STOP_PLAY)) {
/* 594 */       Allay.this.setJukeboxPlaying(BlockPos.containing((Position)sourcePosition), false);
/* 595 */       return true;
/*     */     } 
/* 598 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\allay\Allay$JukeboxListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */