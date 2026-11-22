/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import net.minecraft.client.renderer.SectionBufferBuilderPack;
/*     */ import net.minecraft.core.BlockPos;
/*     */ 
/*     */ public abstract class SectionTask {
/*     */   public enum SectionTaskResult {
/* 586 */     SUCCESSFUL, CANCELLED;
/*     */   }
/*     */   
/* 590 */   protected final AtomicBoolean isCancelled = new AtomicBoolean(false);
/*     */   
/* 591 */   protected final AtomicBoolean isCompleted = new AtomicBoolean(false);
/*     */   
/*     */   private final boolean isRecompile;
/*     */   
/*     */   public SectionTask(boolean isRecompile) {
/* 595 */     this.isRecompile = isRecompile;
/*     */   }
/*     */   
/*     */   public abstract SectionTaskResult doTask(SectionBufferBuilderPack paramSectionBufferBuilderPack);
/*     */   
/*     */   public abstract void cancel();
/*     */   
/*     */   public boolean isRecompile() {
/* 603 */     return this.isRecompile;
/*     */   }
/*     */   
/*     */   public BlockPos getRenderOrigin() {
/* 608 */     return (BlockPos)SectionRenderDispatcher.RenderSection.this.renderOrigin;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\SectionRenderDispatcher$RenderSection$SectionTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */