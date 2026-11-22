/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import java.util.Objects;
/*     */ 
/*     */ class ValueSource<T> {
/*     */   private final DebugValueSource.ValueGetter<T> getter;
/*     */   
/*     */   private T lastSyncedValue;
/*     */   
/*     */   private ValueSource(DebugValueSource.ValueGetter<T> getter) {
/* 225 */     this.getter = getter;
/*     */   }
/*     */   
/*     */   public DebugSubscription.Update<T> pollUpdate(DebugSubscription<T> subscription) {
/* 229 */     T newValue = this.getter.get();
/* 230 */     if (!Objects.equals(newValue, this.lastSyncedValue)) {
/* 231 */       this.lastSyncedValue = newValue;
/* 232 */       return subscription.packUpdate(newValue);
/*     */     } 
/* 234 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\TrackingDebugSynchronizer$ValueSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */