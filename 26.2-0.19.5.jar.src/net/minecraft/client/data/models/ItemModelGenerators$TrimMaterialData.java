/*    */ package net.minecraft.client.data.models;
/*    */ 
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
/*    */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*    */ 
/*    */ public final class TrimMaterialData extends Record {
/*    */   private final MaterialAssetGroup assets;
/*    */   
/*    */   private final ResourceKey<TrimMaterial> materialKey;
/*    */   
/*    */   public TrimMaterialData(MaterialAssetGroup assets, ResourceKey<TrimMaterial> materialKey) {
/* 83 */     this.assets = assets;
/* 83 */     this.materialKey = materialKey;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #83	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #83	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #83	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/data/models/ItemModelGenerators$TrimMaterialData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public MaterialAssetGroup assets() {
/* 83 */     return this.assets;
/*    */   }
/*    */   
/*    */   public ResourceKey<TrimMaterial> materialKey() {
/* 83 */     return this.materialKey;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\ItemModelGenerators$TrimMaterialData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */