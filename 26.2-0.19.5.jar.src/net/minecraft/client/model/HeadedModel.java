/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ 
/*    */ public interface HeadedModel {
/*    */   ModelPart getHead();
/*    */   
/*    */   default void translateToHead(PoseStack poseStack) {
/* 10 */     getHead().translateAndRotate(poseStack);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\HeadedModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */