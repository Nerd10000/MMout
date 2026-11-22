/*     */ package com.mojang.realmsclient.gui.task;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ 
/*     */ public class Subscription {
/* 146 */   private final List<DataFetcher.SubscribedTask<?>> subscriptions = new ArrayList<>();
/*     */   
/*     */   public <T> void subscribe(DataFetcher.Task<T> task, Consumer<T> output) {
/* 149 */     DataFetcher.SubscribedTask<T> subscription = new DataFetcher.SubscribedTask<>(DataFetcher.this, task, output);
/* 150 */     this.subscriptions.add(subscription);
/* 151 */     subscription.runCallbackIfNeeded();
/*     */   }
/*     */   
/*     */   public void forceUpdate() {
/* 155 */     for (DataFetcher.SubscribedTask<?> subscription : this.subscriptions)
/* 156 */       subscription.runCallback(); 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 161 */     for (DataFetcher.SubscribedTask<?> subscription : this.subscriptions)
/* 162 */       subscription.update(DataFetcher.this.timeSource.get(DataFetcher.this.resolution)); 
/*     */   }
/*     */   
/*     */   public void reset() {
/* 167 */     for (DataFetcher.SubscribedTask<?> subscription : this.subscriptions)
/* 168 */       subscription.reset(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\task\DataFetcher$Subscription.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */