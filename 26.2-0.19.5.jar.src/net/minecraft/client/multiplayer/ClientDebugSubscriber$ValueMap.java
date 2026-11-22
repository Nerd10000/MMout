/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.util.debug.DebugSubscription;
/*     */ 
/*     */ class ValueMap<K, V> {
/* 227 */   private final Map<K, ClientDebugSubscriber.ValueWrapper<V>> values = new HashMap<>();
/*     */   
/*     */   public void removeValues(Predicate<ClientDebugSubscriber.ValueWrapper<V>> predicate) {
/* 230 */     this.values.values().removeIf(predicate);
/*     */   }
/*     */   
/*     */   public void removeKey(K key) {
/* 234 */     this.values.remove(key);
/*     */   }
/*     */   
/*     */   public void removeKeys(Predicate<K> predicate) {
/* 238 */     this.values.keySet().removeIf(predicate);
/*     */   }
/*     */   
/*     */   public V getValue(K key) {
/* 242 */     ClientDebugSubscriber.ValueWrapper<V> result = this.values.get(key);
/* 243 */     return (result != null) ? result.value() : null;
/*     */   }
/*     */   
/*     */   public void apply(long gameTime, K key, DebugSubscription.Update<V> update) {
/* 247 */     if (update.value().isPresent()) {
/* 248 */       this.values.put(key, new ClientDebugSubscriber.ValueWrapper<>(update.value().get(), gameTime + update.subscription().expireAfterTicks()));
/*     */     } else {
/* 250 */       this.values.remove(key);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void forEach(BiConsumer<K, V> output) {
/* 255 */     this.values.forEach((k, v) -> output.accept(k, v.value()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientDebugSubscriber$ValueMap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */