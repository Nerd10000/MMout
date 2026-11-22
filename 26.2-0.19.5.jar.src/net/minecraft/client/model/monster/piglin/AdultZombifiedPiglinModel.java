/*    */ package net.minecraft.client.model.monster.piglin;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ 
/*    */ public class AdultZombifiedPiglinModel extends ZombifiedPiglinModel {
/*    */   public AdultZombifiedPiglinModel(ModelPart root) {
/*  9 */     super(root);
/*    */   }
/*    */   
/*    */   protected float getDefaultEarAngleInDegrees() {
/* 14 */     return 30.0F;
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer() {
/* 18 */     return AdultPiglinModel.createBodyLayer();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\piglin\AdultZombifiedPiglinModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */