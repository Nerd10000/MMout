/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.thread.ConsecutiveExecutor;
/*     */ 
/*     */ public class PlayerContext implements TextFilter {
/*     */   protected final GameProfile profile;
/*     */   
/*     */   protected final Executor streamExecutor;
/*     */   
/*     */   protected PlayerContext(GameProfile profile) {
/* 205 */     this.profile = profile;
/* 206 */     ConsecutiveExecutor streamProcessor = new ConsecutiveExecutor(ServerTextFilter.this.workerPool, "chat stream for " + profile.name());
/* 207 */     Objects.requireNonNull(streamProcessor);
/* 207 */     this.streamExecutor = streamProcessor::schedule;
/*     */   }
/*     */   
/*     */   public CompletableFuture<List<FilteredText>> processMessageBundle(List<String> messages) {
/* 215 */     List<CompletableFuture<FilteredText>> requests = (List<CompletableFuture<FilteredText>>)messages.stream().map(message -> ServerTextFilter.this.requestMessageProcessing(this.profile, message, ServerTextFilter.this.chatIgnoreStrategy, this.streamExecutor)).collect(ImmutableList.toImmutableList());
/* 217 */     return Util.sequenceFailFast(requests)
/*     */       
/* 219 */       .exceptionally(e -> ImmutableList.of());
/*     */   }
/*     */   
/*     */   public CompletableFuture<FilteredText> processStreamMessage(String message) {
/* 225 */     return ServerTextFilter.this.requestMessageProcessing(this.profile, message, ServerTextFilter.this.chatIgnoreStrategy, this.streamExecutor);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\ServerTextFilter$PlayerContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */