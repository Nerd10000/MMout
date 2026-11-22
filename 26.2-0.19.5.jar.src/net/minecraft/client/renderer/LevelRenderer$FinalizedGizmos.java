/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives;
/*     */ 
/*     */ final class FinalizedGizmos extends Record {
/*     */   private final DrawableGizmoPrimitives standardPrimitives;
/*     */   
/*     */   private final DrawableGizmoPrimitives alwaysOnTopPrimitives;
/*     */   
/*     */   private FinalizedGizmos(DrawableGizmoPrimitives standardPrimitives, DrawableGizmoPrimitives alwaysOnTopPrimitives) {
/* 943 */     this.standardPrimitives = standardPrimitives;
/* 943 */     this.alwaysOnTopPrimitives = alwaysOnTopPrimitives;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #943	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #943	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #943	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public DrawableGizmoPrimitives standardPrimitives() {
/* 943 */     return this.standardPrimitives;
/*     */   }
/*     */   
/*     */   public DrawableGizmoPrimitives alwaysOnTopPrimitives() {
/* 943 */     return this.alwaysOnTopPrimitives;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\LevelRenderer$FinalizedGizmos.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */