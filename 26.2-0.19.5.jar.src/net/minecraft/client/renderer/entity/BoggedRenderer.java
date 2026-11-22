/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.skeleton.BoggedModel;
/*    */ import net.minecraft.client.model.monster.skeleton.SkeletonModel;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SkeletonClothingLayer;
/*    */ import net.minecraft.client.renderer.entity.state.BoggedRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
/*    */ import net.minecraft.world.entity.monster.skeleton.Bogged;
/*    */ 
/*    */ public class BoggedRenderer extends AbstractSkeletonRenderer<Bogged, BoggedRenderState> {
/* 11 */   private static final Identifier BOGGED_SKELETON_LOCATION = Identifier.withDefaultNamespace("textures/entity/skeleton/bogged.png");
/*    */   
/* 12 */   private static final Identifier BOGGED_OUTER_LAYER_LOCATION = Identifier.withDefaultNamespace("textures/entity/skeleton/bogged_overlay.png");
/*    */   
/*    */   public BoggedRenderer(EntityRendererProvider.Context context) {
/* 15 */     super(context, ModelLayers.BOGGED_ARMOR, (SkeletonModel<BoggedRenderState>)new BoggedModel(context.bakeLayer(ModelLayers.BOGGED)));
/* 17 */     addLayer((RenderLayer<BoggedRenderState, SkeletonModel<BoggedRenderState>>)new SkeletonClothingLayer(this, context.getModelSet(), ModelLayers.BOGGED_OUTER_LAYER, BOGGED_OUTER_LAYER_LOCATION));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(BoggedRenderState state) {
/* 22 */     return BOGGED_SKELETON_LOCATION;
/*    */   }
/*    */   
/*    */   public BoggedRenderState createRenderState() {
/* 27 */     return new BoggedRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Bogged entity, BoggedRenderState state, float partialTicks) {
/* 32 */     super.extractRenderState(entity, state, partialTicks);
/* 33 */     state.isSheared = entity.isSheared();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\BoggedRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */