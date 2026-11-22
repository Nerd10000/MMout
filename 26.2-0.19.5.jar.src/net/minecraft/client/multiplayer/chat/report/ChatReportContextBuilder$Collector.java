/*     */ package net.minecraft.client.multiplayer.chat.report;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.network.chat.MessageSignature;
/*     */ import net.minecraft.network.chat.PlayerChatMessage;
/*     */ 
/*     */ class Collector {
/*     */   private final Set<MessageSignature> lastSeenSignatures;
/*     */   
/*     */   private PlayerChatMessage lastChainMessage;
/*     */   
/*     */   private boolean collectingChain = true;
/*     */   
/*     */   private int count;
/*     */   
/*     */   private Collector(PlayerChatMessage fromMessage) {
/*  80 */     this.lastSeenSignatures = (Set<MessageSignature>)new ObjectOpenHashSet(fromMessage.signedBody().lastSeen().entries());
/*  81 */     this.lastChainMessage = fromMessage;
/*     */   }
/*     */   
/*     */   private boolean accept(PlayerChatMessage message) {
/*  86 */     if (message.equals(this.lastChainMessage))
/*  87 */       return false; 
/*  89 */     boolean selected = this.lastSeenSignatures.remove(message.signature());
/*  90 */     if (this.collectingChain && this.lastChainMessage.sender().equals(message.sender()))
/*  91 */       if (this.lastChainMessage.link().isDescendantOf(message.link())) {
/*  92 */         selected = true;
/*  93 */         this.lastChainMessage = message;
/*     */       } else {
/*  95 */         this.collectingChain = false;
/*     */       }  
/*  98 */     if (selected)
/*  99 */       this.count++; 
/* 101 */     return selected;
/*     */   }
/*     */   
/*     */   private boolean isComplete() {
/* 105 */     return (this.count >= ChatReportContextBuilder.this.leadingCount || (!this.collectingChain && this.lastSeenSignatures.isEmpty()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\report\ChatReportContextBuilder$Collector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */