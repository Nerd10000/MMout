/*    */ package net.minecraft.client.model.monster.piglin;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ 
/*    */ public class BabyZombifiedPiglinModel extends ZombifiedPiglinModel {
/*    */   public BabyZombifiedPiglinModel(ModelPart root) {
/*  8 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 12 */     return BabyPiglinModel.createBodyLayer();
/*    */   }
/*    */   
/*    */   protected float getDefaultEarAngleInDegrees() {
/* 17 */     return 5.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\piglin\BabyZombifiedPiglinModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */