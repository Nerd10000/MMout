/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import net.minecraft.client.resources.model.ModelBaker;
/*    */ import net.minecraft.client.resources.model.ResolvedModel;
/*    */ import net.minecraft.client.resources.model.cuboid.ItemTransforms;
/*    */ import net.minecraft.client.resources.model.sprite.Material;
/*    */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ 
/*    */ public final class ModelRenderProperties extends Record {
/*    */   private final boolean usesBlockLight;
/*    */   
/*    */   private final Material.Baked particleMaterial;
/*    */   
/*    */   private final ItemTransforms transforms;
/*    */   
/*    */   public ModelRenderProperties(boolean usesBlockLight, Material.Baked particleMaterial, ItemTransforms transforms) {
/* 10 */     this.usesBlockLight = usesBlockLight;
/* 10 */     this.particleMaterial = particleMaterial;
/* 10 */     this.transforms = transforms;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/ModelRenderProperties;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/ModelRenderProperties;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/ModelRenderProperties;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/ModelRenderProperties;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/ModelRenderProperties;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/ModelRenderProperties;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean usesBlockLight() {
/* 10 */     return this.usesBlockLight;
/*    */   }
/*    */   
/*    */   public Material.Baked particleMaterial() {
/* 10 */     return this.particleMaterial;
/*    */   }
/*    */   
/*    */   public ItemTransforms transforms() {
/* 10 */     return this.transforms;
/*    */   }
/*    */   
/*    */   public static ModelRenderProperties fromResolvedModel(ModelBaker baker, ResolvedModel resolvedModel, TextureSlots textureSlots) {
/* 17 */     Material.Baked particleSprite = resolvedModel.resolveParticleMaterial(textureSlots, baker);
/* 19 */     return new ModelRenderProperties(resolvedModel
/* 20 */         .getTopGuiLight().lightLikeBlock(), particleSprite, resolvedModel
/*    */         
/* 22 */         .getTopTransforms());
/*    */   }
/*    */   
/*    */   public void applyToLayer(ItemStackRenderState.LayerRenderState layer, ItemDisplayContext displayContext) {
/* 27 */     layer.setUsesBlockLight(this.usesBlockLight);
/* 28 */     layer.setParticleMaterial(this.particleMaterial);
/* 29 */     layer.setItemTransform(this.transforms.getTransform(displayContext));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\ModelRenderProperties.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */