/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.slime.SlimeModel;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SlimeRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.monster.cubemob.Slime;
/*    */ 
/*    */ public class SlimeRenderer extends AbstractCubeMobRenderer<Slime, SlimeRenderState, SlimeModel> {
/* 12 */   public static final Identifier SLIME_LOCATION = Identifier.withDefaultNamespace("textures/entity/slime/slime.png");
/*    */   
/*    */   public SlimeRenderer(EntityRendererProvider.Context context) {
/* 15 */     super(context, new SlimeModel(context.bakeLayer(ModelLayers.SLIME)));
/* 17 */     addLayer((RenderLayer<SlimeRenderState, SlimeModel>)new SlimeOuterLayer(this, context.getModelSet()));
/*    */   }
/*    */   
/*    */   protected void scale(SlimeRenderState state, PoseStack poseStack) {
/* 22 */     downscaleSlightly(poseStack);
/* 23 */     super.scale(state, poseStack);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(SlimeRenderState state) {
/* 28 */     return SLIME_LOCATION;
/*    */   }
/*    */   
/*    */   public SlimeRenderState createRenderState() {
/* 33 */     return new SlimeRenderState();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\SlimeRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */