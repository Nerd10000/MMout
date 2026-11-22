/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.monster.illager.IllagerModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
/*    */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.IllagerRenderState;
/*    */ 
/*    */ class null extends ItemInHandLayer<IllagerRenderState, IllagerModel<IllagerRenderState>> {
/*    */   null(VindicatorRenderer this$0, RenderLayerParent<IllagerRenderState, IllagerModel<IllagerRenderState>> renderer) {
/* 18 */     super(renderer);
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, IllagerRenderState state, float yRot, float xRot) {
/* 21 */     if (state.isAggressive)
/* 22 */       super.submit(poseStack, submitNodeCollector, lightCoords, (ArmedEntityRenderState)state, yRot, xRot); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\VindicatorRenderer$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */