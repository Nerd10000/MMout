/*   */ package com.mojang.blaze3d.systems;
/*   */ 
/*   */ public final class HintsAndWorkarounds extends Record {
/*   */   private final boolean writeToBufferIsSlow;
/*   */   
/*   */   private final boolean anisotropyHasKnownIssues;
/*   */   
/*   */   public HintsAndWorkarounds(boolean writeToBufferIsSlow, boolean anisotropyHasKnownIssues) {
/* 9 */     this.writeToBufferIsSlow = writeToBufferIsSlow;
/* 9 */     this.anisotropyHasKnownIssues = anisotropyHasKnownIssues;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/systems/HintsAndWorkarounds;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #9	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/HintsAndWorkarounds;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/systems/HintsAndWorkarounds;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #9	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/HintsAndWorkarounds;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/systems/HintsAndWorkarounds;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #9	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/blaze3d/systems/HintsAndWorkarounds;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public boolean writeToBufferIsSlow() {
/* 9 */     return this.writeToBufferIsSlow;
/*   */   }
/*   */   
/*   */   public boolean anisotropyHasKnownIssues() {
/* 9 */     return this.anisotropyHasKnownIssues;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\HintsAndWorkarounds.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */