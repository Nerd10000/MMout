/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ class null implements DebugValueSource.Registration {
/*     */   public <T> void register(DebugSubscription<T> subscription, DebugValueSource.ValueGetter<T> getter) {
/* 138 */     LevelDebugSynchronizers.this.<T>getSourceSynchronizer(subscription).registerEntity(entity.getUUID(), getter);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\LevelDebugSynchronizers$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */