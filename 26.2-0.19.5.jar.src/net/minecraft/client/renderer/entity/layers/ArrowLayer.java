/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.projectile.ArrowModel;
/*    */ import net.minecraft.client.model.player.PlayerModel;
/*    */ import net.minecraft.client.renderer.entity.EntityRendererProvider;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.TippableArrowRenderer;
/*    */ import net.minecraft.client.renderer.entity.state.ArrowRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.AvatarRenderState;
/*    */ 
/*    */ public class ArrowLayer<M extends PlayerModel> extends StuckInBodyLayer<M, ArrowRenderState> {
/*    */   public ArrowLayer(LivingEntityRenderer<?, AvatarRenderState, M> renderer, EntityRendererProvider.Context context) {
/* 14 */     super(renderer, (Model<ArrowRenderState>)new ArrowModel(context.bakeLayer(ModelLayers.ARROW)), new ArrowRenderState(), TippableArrowRenderer.NORMAL_ARROW_LOCATION, StuckInBodyLayer.PlacementStyle.IN_CUBE);
/*    */   }
/*    */   
/*    */   protected int numStuck(AvatarRenderState state) {
/* 19 */     return state.arrowCount;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\ArrowLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */