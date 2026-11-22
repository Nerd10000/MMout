/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ 
/*     */ class null implements FrameGraphBuilder.Inspector {
/*     */   null(LevelRenderer this$0) {}
/*     */   
/*     */   public void beforeExecutePass(String name) {
/* 243 */     profiler.push(name);
/*     */   }
/*     */   
/*     */   public void afterExecutePass(String name) {
/* 248 */     profiler.pop();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\LevelRenderer$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */