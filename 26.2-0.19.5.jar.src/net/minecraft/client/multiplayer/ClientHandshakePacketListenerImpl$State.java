/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import java.util.Set;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ enum State {
/* 245 */   CONNECTING((Component)Component.translatable("connect.connecting"), Set.of()),
/* 246 */   AUTHORIZING((Component)Component.translatable("connect.authorizing"), Set.of(CONNECTING)),
/* 247 */   ENCRYPTING((Component)Component.translatable("connect.encrypting"), Set.of(AUTHORIZING)),
/* 249 */   JOINING((Component)Component.translatable("connect.joining"), Set.of(ENCRYPTING, CONNECTING));
/*     */   
/*     */   private final Component message;
/*     */   
/*     */   private final Set<State> fromStates;
/*     */   
/*     */   State(Component message, Set<State> fromStates) {
/* 256 */     this.message = message;
/* 257 */     this.fromStates = fromStates;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientHandshakePacketListenerImpl$State.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */