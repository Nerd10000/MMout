/*   */ package com.mojang.blaze3d.shaders;
/*   */ 
/*   */ public final class GpuDebugOptions extends Record {
/*   */   private final int logLevel;
/*   */   
/*   */   private final boolean synchronousLogs;
/*   */   
/*   */   private final boolean useLabels;
/*   */   
/*   */   private final boolean useValidationLayers;
/*   */   
/*   */   public GpuDebugOptions(int logLevel, boolean synchronousLogs, boolean useLabels, boolean useValidationLayers) {
/* 3 */     this.logLevel = logLevel;
/* 3 */     this.synchronousLogs = synchronousLogs;
/* 3 */     this.useLabels = useLabels;
/* 3 */     this.useValidationLayers = useValidationLayers;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/shaders/GpuDebugOptions;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/blaze3d/shaders/GpuDebugOptions;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/shaders/GpuDebugOptions;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/blaze3d/shaders/GpuDebugOptions;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/shaders/GpuDebugOptions;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/blaze3d/shaders/GpuDebugOptions;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public int logLevel() {
/* 3 */     return this.logLevel;
/*   */   }
/*   */   
/*   */   public boolean synchronousLogs() {
/* 3 */     return this.synchronousLogs;
/*   */   }
/*   */   
/*   */   public boolean useLabels() {
/* 3 */     return this.useLabels;
/*   */   }
/*   */   
/*   */   public boolean useValidationLayers() {
/* 3 */     return this.useValidationLayers;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\shaders\GpuDebugOptions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */