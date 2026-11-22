/*      */ package net.minecraft.client.data.models;
/*      */ 
/*      */ import net.minecraft.client.data.models.model.ModelTemplate;
/*      */ 
/*      */ final class BookSlotModelCacheKey extends Record {
/*      */   private final ModelTemplate template;
/*      */   
/*      */   private final String modelSuffix;
/*      */   
/*      */   private BookSlotModelCacheKey(ModelTemplate template, String modelSuffix) {
/* 3579 */     this.template = template;
/* 3579 */     this.modelSuffix = modelSuffix;
/*      */   }
/*      */   
/*      */   public final String toString() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #3579	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey;
/*      */   }
/*      */   
/*      */   public final int hashCode() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #3579	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey;
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #3579	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/client/data/models/BlockModelGenerators$BookSlotModelCacheKey;
/*      */     //   0	8	1	o	Ljava/lang/Object;
/*      */   }
/*      */   
/*      */   public ModelTemplate template() {
/* 3579 */     return this.template;
/*      */   }
/*      */   
/*      */   public String modelSuffix() {
/* 3579 */     return this.modelSuffix;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\BlockModelGenerators$BookSlotModelCacheKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */