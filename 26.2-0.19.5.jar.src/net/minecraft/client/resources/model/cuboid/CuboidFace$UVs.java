/*    */ package net.minecraft.client.resources.model.cuboid;
/*    */ 
/*    */ public final class UVs extends Record {
/*    */   private final float minU;
/*    */   
/*    */   private final float minV;
/*    */   
/*    */   private final float maxU;
/*    */   
/*    */   private final float maxV;
/*    */   
/*    */   public UVs(float minU, float minV, float maxU, float maxV) {
/* 42 */     this.minU = minU;
/* 42 */     this.minV = minV;
/* 42 */     this.maxU = maxU;
/* 42 */     this.maxV = maxV;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/cuboid/CuboidFace$UVs;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidFace$UVs;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/cuboid/CuboidFace$UVs;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidFace$UVs;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/cuboid/CuboidFace$UVs;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidFace$UVs;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float minU() {
/* 42 */     return this.minU;
/*    */   }
/*    */   
/*    */   public float minV() {
/* 42 */     return this.minV;
/*    */   }
/*    */   
/*    */   public float maxU() {
/* 42 */     return this.maxU;
/*    */   }
/*    */   
/*    */   public float maxV() {
/* 42 */     return this.maxV;
/*    */   }
/*    */   
/*    */   public float getVertexU(int index) {
/* 50 */     return (index == 0 || index == 1) ? this.minU : this.maxU;
/*    */   }
/*    */   
/*    */   public float getVertexV(int index) {
/* 55 */     return (index == 0 || index == 3) ? this.minV : this.maxV;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\CuboidFace$UVs.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */