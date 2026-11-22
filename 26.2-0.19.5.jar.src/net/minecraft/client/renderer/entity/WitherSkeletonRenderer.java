/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
/*    */ 
/*    */ public class WitherSkeletonRenderer extends AbstractSkeletonRenderer<WitherSkeleton, SkeletonRenderState> {
/*  9 */   private static final Identifier WITHER_SKELETON_LOCATION = Identifier.withDefaultNamespace("textures/entity/skeleton/wither_skeleton.png");
/*    */   
/*    */   public WitherSkeletonRenderer(EntityRendererProvider.Context context) {
/* 12 */     super(context, ModelLayers.WITHER_SKELETON, ModelLayers.WITHER_SKELETON_ARMOR);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(SkeletonRenderState state) {
/* 17 */     return WITHER_SKELETON_LOCATION;
/*    */   }
/*    */   
/*    */   public SkeletonRenderState createRenderState() {
/* 22 */     return new SkeletonRenderState();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\WitherSkeletonRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */