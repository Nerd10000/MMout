/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.monster.skeleton.SkeletonModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class SkeletonClothingLayer<S extends SkeletonRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
/*    */   private final SkeletonModel<S> layerModel;
/*    */   
/*    */   private final Identifier clothesLocation;
/*    */   
/*    */   public SkeletonClothingLayer(RenderLayerParent<S, M> renderer, EntityModelSet models, ModelLayerLocation layerLocation, Identifier clothesLocation) {
/* 19 */     super(renderer);
/* 20 */     this.clothesLocation = clothesLocation;
/* 21 */     this.layerModel = new SkeletonModel(models.bakeLayer(layerLocation));
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/* 26 */     coloredCutoutModelCopyLayerRender((Model<? super S>)this.layerModel, this.clothesLocation, poseStack, submitNodeCollector, lightCoords, state, -1, 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\SkeletonClothingLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */