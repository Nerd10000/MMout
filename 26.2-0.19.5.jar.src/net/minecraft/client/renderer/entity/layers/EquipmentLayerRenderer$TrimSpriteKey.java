/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.equipment.EquipmentAsset;
/*    */ import net.minecraft.world.item.equipment.trim.ArmorTrim;
/*    */ 
/*    */ final class TrimSpriteKey extends Record {
/*    */   private final ArmorTrim trim;
/*    */   
/*    */   private final EquipmentClientInfo.LayerType layerType;
/*    */   
/*    */   private final ResourceKey<EquipmentAsset> equipmentAssetId;
/*    */   
/*    */   private TrimSpriteKey(ArmorTrim trim, EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> equipmentAssetId) {
/* 89 */     this.trim = trim;
/* 89 */     this.layerType = layerType;
/* 89 */     this.equipmentAssetId = equipmentAssetId;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$TrimSpriteKey;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #89	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$TrimSpriteKey;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$TrimSpriteKey;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #89	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$TrimSpriteKey;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$TrimSpriteKey;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #89	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$TrimSpriteKey;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ArmorTrim trim() {
/* 89 */     return this.trim;
/*    */   }
/*    */   
/*    */   public EquipmentClientInfo.LayerType layerType() {
/* 89 */     return this.layerType;
/*    */   }
/*    */   
/*    */   public ResourceKey<EquipmentAsset> equipmentAssetId() {
/* 89 */     return this.equipmentAssetId;
/*    */   }
/*    */   
/*    */   public Identifier spriteId() {
/* 91 */     return this.trim.layerAssetId(this.layerType.trimAssetPrefix(), this.equipmentAssetId);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\EquipmentLayerRenderer$TrimSpriteKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */