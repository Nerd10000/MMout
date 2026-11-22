/*    */ package net.minecraft.client.model.monster.zombie;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ 
/*    */ public class BabyDrownedModel extends DrownedModel {
/*    */   public BabyDrownedModel(ModelPart root) {
/* 10 */     super(root);
/*    */   }
/*    */   
/*    */   public static LayerDefinition createBodyLayer(CubeDeformation g) {
/* 14 */     return BabyZombieModel.createBodyLayer(g);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\monster\zombie\BabyDrownedModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */