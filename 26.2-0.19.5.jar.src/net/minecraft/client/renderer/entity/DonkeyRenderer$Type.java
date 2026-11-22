/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public enum Type {
/* 53 */   DONKEY(
/* 54 */     Identifier.withDefaultNamespace("textures/entity/horse/donkey.png"), ModelLayers.DONKEY),
/* 57 */   DONKEY_BABY(
/* 58 */     Identifier.withDefaultNamespace("textures/entity/horse/donkey_baby.png"), ModelLayers.DONKEY_BABY),
/* 61 */   MULE(
/* 62 */     Identifier.withDefaultNamespace("textures/entity/horse/mule.png"), ModelLayers.MULE),
/* 65 */   MULE_BABY(
/* 66 */     Identifier.withDefaultNamespace("textures/entity/horse/mule_baby.png"), ModelLayers.MULE_BABY);
/*    */   
/*    */   private final Identifier texture;
/*    */   
/*    */   private final ModelLayerLocation model;
/*    */   
/*    */   Type(Identifier texture, ModelLayerLocation model) {
/* 74 */     this.texture = texture;
/* 75 */     this.model = model;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\DonkeyRenderer$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */