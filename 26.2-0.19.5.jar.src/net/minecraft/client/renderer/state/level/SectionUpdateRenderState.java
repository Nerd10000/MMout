/*   */ package net.minecraft.client.renderer.state.level;
/*   */ 
/*   */ import net.minecraft.client.renderer.chunk.RenderSectionRegion;
/*   */ 
/*   */ public final class SectionUpdateRenderState extends Record {
/*   */   private final long sectionNode;
/*   */   
/*   */   private final boolean playerChanged;
/*   */   
/*   */   private final RenderSectionRegion region;
/*   */   
/*   */   public SectionUpdateRenderState(long sectionNode, boolean playerChanged, RenderSectionRegion region) {
/* 5 */     this.sectionNode = sectionNode;
/* 5 */     this.playerChanged = playerChanged;
/* 5 */     this.region = region;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/state/level/SectionUpdateRenderState;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/level/SectionUpdateRenderState;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/state/level/SectionUpdateRenderState;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/level/SectionUpdateRenderState;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/state/level/SectionUpdateRenderState;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/renderer/state/level/SectionUpdateRenderState;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public long sectionNode() {
/* 5 */     return this.sectionNode;
/*   */   }
/*   */   
/*   */   public boolean playerChanged() {
/* 5 */     return this.playerChanged;
/*   */   }
/*   */   
/*   */   public RenderSectionRegion region() {
/* 5 */     return this.region;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\level\SectionUpdateRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */