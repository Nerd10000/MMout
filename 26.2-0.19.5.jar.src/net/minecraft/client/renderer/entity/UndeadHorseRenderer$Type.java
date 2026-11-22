/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public enum Type {
/* 55 */   SKELETON(
/* 56 */     Identifier.withDefaultNamespace("textures/entity/horse/horse_skeleton.png"), ModelLayers.SKELETON_HORSE),
/* 59 */   SKELETON_BABY(
/* 60 */     Identifier.withDefaultNamespace("textures/entity/horse/horse_skeleton_baby.png"), ModelLayers.SKELETON_HORSE_BABY),
/* 63 */   ZOMBIE(
/* 64 */     Identifier.withDefaultNamespace("textures/entity/horse/horse_zombie.png"), ModelLayers.ZOMBIE_HORSE),
/* 67 */   ZOMBIE_BABY(
/* 68 */     Identifier.withDefaultNamespace("textures/entity/horse/horse_zombie_baby.png"), ModelLayers.ZOMBIE_HORSE_BABY);
/*    */   
/*    */   private final Identifier texture;
/*    */   
/*    */   private final ModelLayerLocation model;
/*    */   
/*    */   Type(Identifier texture, ModelLayerLocation model) {
/* 77 */     this.texture = texture;
/* 78 */     this.model = model;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\UndeadHorseRenderer$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */