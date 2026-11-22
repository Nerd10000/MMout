/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.debug.DebugSubscription;
/*     */ import net.minecraft.util.debug.DebugValueAccess;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ class null implements DebugValueAccess {
/*     */   public <T> void forEachChunk(DebugSubscription<T> subscription, BiConsumer<ChunkPos, T> consumer) {
/* 121 */     ClientDebugSubscriber.this.forEachValue(subscription, ClientDebugSubscriber.chunks(), consumer);
/*     */   }
/*     */   
/*     */   public <T> T getChunkValue(DebugSubscription<T> subscription, ChunkPos chunkPos) {
/* 126 */     return ClientDebugSubscriber.this.getValue(subscription, chunkPos, ClientDebugSubscriber.chunks());
/*     */   }
/*     */   
/*     */   public <T> void forEachBlock(DebugSubscription<T> subscription, BiConsumer<BlockPos, T> consumer) {
/* 131 */     ClientDebugSubscriber.this.forEachValue(subscription, ClientDebugSubscriber.blocks(), consumer);
/*     */   }
/*     */   
/*     */   public <T> T getBlockValue(DebugSubscription<T> subscription, BlockPos blockPos) {
/* 136 */     return ClientDebugSubscriber.this.getValue(subscription, blockPos, ClientDebugSubscriber.blocks());
/*     */   }
/*     */   
/*     */   public <T> void forEachEntity(DebugSubscription<T> subscription, BiConsumer<Entity, T> consumer) {
/* 141 */     ClientDebugSubscriber.this.forEachValue(subscription, ClientDebugSubscriber.entities(), (entityId, value) -> {
/*     */           Entity entity = level.getEntity(entityId);
/*     */           if (entity != null)
/*     */             consumer.accept(entity, value); 
/*     */         });
/*     */   }
/*     */   
/*     */   public <T> T getEntityValue(DebugSubscription<T> subscription, Entity entity) {
/* 151 */     return ClientDebugSubscriber.this.getValue(subscription, entity.getUUID(), ClientDebugSubscriber.entities());
/*     */   }
/*     */   
/*     */   public <T> void forEachEvent(DebugSubscription<T> subscription, DebugValueAccess.EventVisitor<T> visitor) {
/* 156 */     ClientDebugSubscriber.ValueMaps<T> values = ClientDebugSubscriber.this.getValueMaps(subscription);
/* 157 */     if (values == null)
/*     */       return; 
/* 160 */     long gameTime = level.getGameTime();
/* 161 */     for (ClientDebugSubscriber.ValueWrapper<T> event : values.events) {
/* 162 */       int remainingTicks = (int)(event.expiresAfterTime() - gameTime);
/* 163 */       int totalLifetime = subscription.expireAfterTicks();
/* 164 */       visitor.accept(event.value(), remainingTicks, totalLifetime);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientDebugSubscriber$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */