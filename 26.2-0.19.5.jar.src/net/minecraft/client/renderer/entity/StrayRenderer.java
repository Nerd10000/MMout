/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.skeleton.SkeletonModel;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SkeletonClothingLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.monster.skeleton.Stray;
/*    */ 
/*    */ public class StrayRenderer extends AbstractSkeletonRenderer<Stray, SkeletonRenderState> {
/* 10 */   private static final Identifier STRAY_SKELETON_LOCATION = Identifier.withDefaultNamespace("textures/entity/skeleton/stray.png");
/*    */   
/* 11 */   private static final Identifier STRAY_CLOTHES_LOCATION = Identifier.withDefaultNamespace("textures/entity/skeleton/stray_overlay.png");
/*    */   
/*    */   public StrayRenderer(EntityRendererProvider.Context context) {
/* 14 */     super(context, ModelLayers.STRAY, ModelLayers.STRAY_ARMOR);
/* 16 */     addLayer((RenderLayer<SkeletonRenderState, SkeletonModel<SkeletonRenderState>>)new SkeletonClothingLayer(this, context.getModelSet(), ModelLayers.STRAY_OUTER_LAYER, STRAY_CLOTHES_LOCATION));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(SkeletonRenderState state) {
/* 21 */     return STRAY_SKELETON_LOCATION;
/*    */   }
/*    */   
/*    */   public SkeletonRenderState createRenderState() {
/* 26 */     return new SkeletonRenderState();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\StrayRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */