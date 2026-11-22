/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ public final class Transforms extends Record {
/*    */   private final float yOffset;
/*    */   
/*    */   private final float skullYOffset;
/*    */   
/*    */   private final float horizontalScale;
/*    */   
/*    */   public Transforms(float yOffset, float skullYOffset, float horizontalScale) {
/* 87 */     this.yOffset = yOffset;
/* 87 */     this.skullYOffset = skullYOffset;
/* 87 */     this.horizontalScale = horizontalScale;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/layers/CustomHeadLayer$Transforms;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #87	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/layers/CustomHeadLayer$Transforms;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/layers/CustomHeadLayer$Transforms;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #87	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/layers/CustomHeadLayer$Transforms;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/layers/CustomHeadLayer$Transforms;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #87	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/entity/layers/CustomHeadLayer$Transforms;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float yOffset() {
/* 87 */     return this.yOffset;
/*    */   }
/*    */   
/*    */   public float skullYOffset() {
/* 87 */     return this.skullYOffset;
/*    */   }
/*    */   
/*    */   public float horizontalScale() {
/* 87 */     return this.horizontalScale;
/*    */   }
/*    */   
/* 88 */   public static final Transforms DEFAULT = new Transforms(0.0F, 0.0F, 1.0F);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\CustomHeadLayer$Transforms.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */