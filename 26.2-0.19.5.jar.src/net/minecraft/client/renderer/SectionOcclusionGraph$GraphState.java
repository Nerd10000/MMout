/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
/*     */ 
/*     */ final class GraphState extends Record {
/*     */   private final SectionOcclusionGraph.GraphStorage storage;
/*     */   
/*     */   private final BlockingQueue<SectionRenderDispatcher.RenderSection> sectionsToPropagateFrom;
/*     */   
/*     */   public BlockingQueue<SectionRenderDispatcher.RenderSection> sectionsToPropagateFrom() {
/* 405 */     return this.sectionsToPropagateFrom;
/*     */   }
/*     */   
/*     */   public SectionOcclusionGraph.GraphStorage storage() {
/* 405 */     return this.storage;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #405	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #405	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #405	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/SectionOcclusionGraph$GraphState;
/*     */   }
/*     */   
/*     */   private GraphState(SectionOcclusionGraph.GraphStorage storage, BlockingQueue<SectionRenderDispatcher.RenderSection> sectionsToPropagateFrom) {
/* 405 */     this.storage = storage;
/* 405 */     this.sectionsToPropagateFrom = sectionsToPropagateFrom;
/*     */   }
/*     */   
/*     */   private GraphState(ViewArea viewArea) {
/* 410 */     this(new SectionOcclusionGraph.GraphStorage(viewArea), new LinkedBlockingQueue<>());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\SectionOcclusionGraph$GraphState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */