/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ class ValueMaps<V> {
/* 260 */   private final ClientDebugSubscriber.ValueMap<ChunkPos, V> chunkValues = new ClientDebugSubscriber.ValueMap<>();
/*     */   
/* 261 */   private final ClientDebugSubscriber.ValueMap<BlockPos, V> blockValues = new ClientDebugSubscriber.ValueMap<>();
/*     */   
/* 262 */   private final ClientDebugSubscriber.ValueMap<UUID, V> entityValues = new ClientDebugSubscriber.ValueMap<>();
/*     */   
/* 263 */   private final List<ClientDebugSubscriber.ValueWrapper<V>> events = new ArrayList<>();
/*     */   
/*     */   public void purgeExpired(long gameTime) {
/* 266 */     Predicate<ClientDebugSubscriber.ValueWrapper<V>> expiredPredicate = v -> v.hasExpired(gameTime);
/* 267 */     this.chunkValues.removeValues(expiredPredicate);
/* 268 */     this.blockValues.removeValues(expiredPredicate);
/* 269 */     this.entityValues.removeValues(expiredPredicate);
/* 270 */     this.events.removeIf(expiredPredicate);
/*     */   }
/*     */   
/*     */   public void dropChunkAndBlocks(ChunkPos chunkPos) {
/* 274 */     this.chunkValues.removeKey(chunkPos);
/* 276 */     Objects.requireNonNull(chunkPos);
/* 276 */     this.blockValues.removeKeys(chunkPos::contains);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientDebugSubscriber$ValueMaps.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */