/*     */ package net.minecraft.util;
/*     */ 
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ScopedCollector extends ProblemReporter.Collector implements AutoCloseable {
/*     */   private final Logger logger;
/*     */   
/*     */   public ScopedCollector(Logger logger) {
/* 240 */     this.logger = logger;
/*     */   }
/*     */   
/*     */   public ScopedCollector(ProblemReporter.PathElement root, Logger logger) {
/* 244 */     super(root);
/* 245 */     this.logger = logger;
/*     */   }
/*     */   
/*     */   public void close() {
/* 250 */     if (!isEmpty())
/* 252 */       this.logger.warn("[{}] Serialization errors:\n{}", this.logger.getName(), getTreeReport()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ProblemReporter$ScopedCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */