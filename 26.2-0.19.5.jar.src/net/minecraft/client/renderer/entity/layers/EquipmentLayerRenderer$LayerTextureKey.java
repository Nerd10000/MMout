/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ 
/*    */ final class LayerTextureKey extends Record {
/*    */   private final EquipmentClientInfo.LayerType layerType;
/*    */   
/*    */   private final EquipmentClientInfo.Layer layer;
/*    */   
/*    */   private LayerTextureKey(EquipmentClientInfo.LayerType layerType, EquipmentClientInfo.Layer layer) {
/* 86 */     this.layerType = layerType;
/* 86 */     this.layer = layer;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$LayerTextureKey;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #86	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$LayerTextureKey;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$LayerTextureKey;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #86	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$LayerTextureKey;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$LayerTextureKey;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #86	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$LayerTextureKey;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public EquipmentClientInfo.LayerType layerType() {
/* 86 */     return this.layerType;
/*    */   }
/*    */   
/*    */   public EquipmentClientInfo.Layer layer() {
/* 86 */     return this.layer;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\EquipmentLayerRenderer$LayerTextureKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */