/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.effects.SpinAttackEffectModel;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.player.PlayerModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.AvatarRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class SpinAttackEffectLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
/* 15 */   public static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/entity/trident/trident_riptide.png");
/*    */   
/*    */   private final SpinAttackEffectModel model;
/*    */   
/*    */   public SpinAttackEffectLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, EntityModelSet modelSet) {
/* 20 */     super(renderer);
/* 21 */     this.model = new SpinAttackEffectModel(modelSet.bakeLayer(ModelLayers.PLAYER_SPIN_ATTACK));
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
/* 26 */     if (!state.isAutoSpinAttack)
/*    */       return; 
/* 30 */     submitNodeCollector.submitModel((Model)this.model, state, poseStack, TEXTURE, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\SpinAttackEffectLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */