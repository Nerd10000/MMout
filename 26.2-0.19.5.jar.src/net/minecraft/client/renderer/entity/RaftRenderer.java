/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.object.boat.RaftModel;
/*    */ import net.minecraft.client.renderer.entity.state.BoatRenderState;
/*    */ 
/*    */ public class RaftRenderer extends AbstractBoatRenderer {
/*    */   private final EntityModel<BoatRenderState> model;
/*    */   
/*    */   public RaftRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelId) {
/* 12 */     super(context, modelId.model().withPath(p -> "textures/entity/" + p + ".png"));
/* 13 */     this.model = (EntityModel<BoatRenderState>)new RaftModel(context.bakeLayer(modelId));
/*    */   }
/*    */   
/*    */   protected EntityModel<BoatRenderState> model() {
/* 18 */     return this.model;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\RaftRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */