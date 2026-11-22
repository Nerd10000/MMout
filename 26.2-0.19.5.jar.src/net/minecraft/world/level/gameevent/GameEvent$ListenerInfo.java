/*     */ package net.minecraft.world.level.gameevent;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class ListenerInfo implements Comparable<GameEvent.ListenerInfo> {
/*     */   private final Holder<GameEvent> gameEvent;
/*     */   
/*     */   private final Vec3 source;
/*     */   
/*     */   private final GameEvent.Context context;
/*     */   
/*     */   private final GameEventListener recipient;
/*     */   
/*     */   private final double distanceToRecipient;
/*     */   
/*     */   public ListenerInfo(Holder<GameEvent> gameEvent, Vec3 source, GameEvent.Context context, GameEventListener recipient, Vec3 recipientPos) {
/* 127 */     this.gameEvent = gameEvent;
/* 128 */     this.source = source;
/* 129 */     this.context = context;
/* 130 */     this.recipient = recipient;
/* 131 */     this.distanceToRecipient = source.distanceToSqr(recipientPos);
/*     */   }
/*     */   
/*     */   public int compareTo(ListenerInfo other) {
/* 136 */     return Double.compare(this.distanceToRecipient, other.distanceToRecipient);
/*     */   }
/*     */   
/*     */   public Holder<GameEvent> gameEvent() {
/* 140 */     return this.gameEvent;
/*     */   }
/*     */   
/*     */   public Vec3 source() {
/* 144 */     return this.source;
/*     */   }
/*     */   
/*     */   public GameEvent.Context context() {
/* 148 */     return this.context;
/*     */   }
/*     */   
/*     */   public GameEventListener recipient() {
/* 152 */     return this.recipient;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\GameEvent$ListenerInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */