/*     */ package net.minecraft.client.resources.model.cuboid;
/*     */ 
/*     */ import net.minecraft.client.renderer.block.dispatch.ModelState;
/*     */ import net.minecraft.client.resources.model.ModelBaker;
/*     */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*     */ import net.minecraft.client.resources.model.geometry.QuadCollection;
/*     */ import net.minecraft.client.resources.model.sprite.Material;
/*     */ 
/*     */ final class ItemLayerKey extends Record implements ModelBaker.SharedOperationKey<QuadCollection> {
/*     */   private final Material.Baked material;
/*     */   
/*     */   private final ModelState modelState;
/*     */   
/*     */   private final int layerIndex;
/*     */   
/*     */   private ItemLayerKey(Material.Baked material, ModelState modelState, int layerIndex) {
/* 226 */     this.material = material;
/* 226 */     this.modelState = modelState;
/* 226 */     this.layerIndex = layerIndex;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$ItemLayerKey;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #226	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$ItemLayerKey;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$ItemLayerKey;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #226	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$ItemLayerKey;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$ItemLayerKey;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #226	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/resources/model/cuboid/ItemModelGenerator$ItemLayerKey;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Material.Baked material() {
/* 226 */     return this.material;
/*     */   }
/*     */   
/*     */   public ModelState modelState() {
/* 226 */     return this.modelState;
/*     */   }
/*     */   
/*     */   public int layerIndex() {
/* 226 */     return this.layerIndex;
/*     */   }
/*     */   
/*     */   public QuadCollection compute(ModelBaker modelBakery) {
/* 229 */     QuadCollection.Builder builder = new QuadCollection.Builder();
/* 231 */     BakedQuad.MaterialInfo materialInfo = modelBakery.interner().materialInfo(BakedQuad.MaterialInfo.of(this.material, this.material.sprite().transparency(), this.layerIndex, true, 0));
/* 232 */     ItemModelGenerator.bakeExtrudedSprite(builder, modelBakery.interner(), this.modelState, materialInfo);
/* 233 */     return builder.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\ItemModelGenerator$ItemLayerKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */