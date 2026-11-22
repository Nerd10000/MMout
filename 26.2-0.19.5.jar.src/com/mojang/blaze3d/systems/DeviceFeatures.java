/*    */ package com.mojang.blaze3d.systems;
/*    */ 
/*    */ public final class DeviceFeatures extends Record {
/*    */   private final boolean shaderDrawParameters;
/*    */   
/*    */   private final boolean multiDrawDirectInterleaved;
/*    */   
/*    */   private final boolean multiDrawDirectSeparate;
/*    */   
/*    */   private final boolean multiDrawIndirect;
/*    */   
/*    */   private final boolean drawIndirect;
/*    */   
/*    */   private final boolean nonZeroFirstInstance;
/*    */   
/*    */   private final boolean persistentMapping;
/*    */   
/*    */   public DeviceFeatures(boolean shaderDrawParameters, boolean multiDrawDirectInterleaved, boolean multiDrawDirectSeparate, boolean multiDrawIndirect, boolean drawIndirect, boolean nonZeroFirstInstance, boolean persistentMapping) {
/* 12 */     this.shaderDrawParameters = shaderDrawParameters;
/* 12 */     this.multiDrawDirectInterleaved = multiDrawDirectInterleaved;
/* 12 */     this.multiDrawDirectSeparate = multiDrawDirectSeparate;
/* 12 */     this.multiDrawIndirect = multiDrawIndirect;
/* 12 */     this.drawIndirect = drawIndirect;
/* 12 */     this.nonZeroFirstInstance = nonZeroFirstInstance;
/* 12 */     this.persistentMapping = persistentMapping;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/systems/DeviceFeatures;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/DeviceFeatures;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/systems/DeviceFeatures;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/DeviceFeatures;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/systems/DeviceFeatures;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/systems/DeviceFeatures;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean shaderDrawParameters() {
/* 12 */     return this.shaderDrawParameters;
/*    */   }
/*    */   
/*    */   public boolean multiDrawDirectInterleaved() {
/* 12 */     return this.multiDrawDirectInterleaved;
/*    */   }
/*    */   
/*    */   public boolean multiDrawDirectSeparate() {
/* 12 */     return this.multiDrawDirectSeparate;
/*    */   }
/*    */   
/*    */   public boolean multiDrawIndirect() {
/* 12 */     return this.multiDrawIndirect;
/*    */   }
/*    */   
/*    */   public boolean drawIndirect() {
/* 12 */     return this.drawIndirect;
/*    */   }
/*    */   
/*    */   public boolean nonZeroFirstInstance() {
/* 12 */     return this.nonZeroFirstInstance;
/*    */   }
/*    */   
/*    */   public boolean persistentMapping() {
/* 12 */     return this.persistentMapping;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\DeviceFeatures.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */