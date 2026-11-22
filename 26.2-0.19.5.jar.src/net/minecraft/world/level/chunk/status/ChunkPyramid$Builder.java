/*     */ package net.minecraft.world.level.chunk.status;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ public class Builder {
/* 100 */   private final List<ChunkStep> steps = new ArrayList<>();
/*     */   
/*     */   public ChunkPyramid build() {
/* 103 */     return new ChunkPyramid(ImmutableList.copyOf(this.steps));
/*     */   }
/*     */   
/*     */   public Builder step(ChunkStatus status, UnaryOperator<ChunkStep.Builder> operator) {
/*     */     ChunkStep.Builder stepBuilder;
/* 108 */     if (this.steps.isEmpty()) {
/* 109 */       stepBuilder = new ChunkStep.Builder(status);
/*     */     } else {
/* 111 */       stepBuilder = new ChunkStep.Builder(status, this.steps.getLast());
/*     */     } 
/* 113 */     this.steps.add(((ChunkStep.Builder)operator.apply(stepBuilder)).build());
/* 114 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\status\ChunkPyramid$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */