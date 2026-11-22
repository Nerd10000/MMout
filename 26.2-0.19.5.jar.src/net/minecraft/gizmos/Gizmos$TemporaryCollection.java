/*     */ package net.minecraft.gizmos;
/*     */ 
/*     */ public class TemporaryCollection implements AutoCloseable {
/* 112 */   private final GizmoCollector old = Gizmos.collector.get();
/*     */   
/*     */   private boolean closed;
/*     */   
/*     */   public void close() {
/* 117 */     if (!this.closed) {
/* 118 */       this.closed = true;
/* 119 */       Gizmos.collector.set(this.old);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gizmos\Gizmos$TemporaryCollection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */