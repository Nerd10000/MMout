/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.wolf.WolfModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.WolfRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public class WolfCollarLayer extends RenderLayer<WolfRenderState, WolfModel> {
/* 14 */   private static final Identifier WOLF_COLLAR_LOCATION = Identifier.withDefaultNamespace("textures/entity/wolf/wolf_collar.png");
/*    */   
/* 15 */   private static final Identifier WOLF_BABY_COLLAR_LOCATION = Identifier.withDefaultNamespace("textures/entity/wolf/wolf_collar_baby.png");
/*    */   
/*    */   public WolfCollarLayer(RenderLayerParent<WolfRenderState, WolfModel> renderer) {
/* 18 */     super(renderer);
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, WolfRenderState state, float yRot, float xRot) {
/* 24 */     DyeColor collarColor = state.collarColor;
/* 25 */     if (collarColor == null || state.isInvisible)
/*    */       return; 
/* 29 */     int color = collarColor.getTextureDiffuseColor();
/* 30 */     Identifier collarLocation = state.isBaby ? WOLF_BABY_COLLAR_LOCATION : WOLF_COLLAR_LOCATION;
/* 31 */     submitNodeCollector.order(1).submitModel((Model)getParentModel(), state, poseStack, RenderTypes.entityCutout(collarLocation), lightCoords, OverlayTexture.NO_OVERLAY, color, null, state.outlineColor, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\WolfCollarLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */