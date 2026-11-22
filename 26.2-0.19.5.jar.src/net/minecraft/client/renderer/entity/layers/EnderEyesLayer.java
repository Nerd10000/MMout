/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.model.monster.enderman.EndermanModel;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EndermanRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class EnderEyesLayer extends EyesLayer<EndermanRenderState, EndermanModel<EndermanRenderState>> {
/* 11 */   private static final RenderType ENDERMAN_EYES = RenderTypes.eyes(Identifier.withDefaultNamespace("textures/entity/enderman/enderman_eyes.png"));
/*    */   
/*    */   public EnderEyesLayer(RenderLayerParent<EndermanRenderState, EndermanModel<EndermanRenderState>> renderer) {
/* 14 */     super(renderer);
/*    */   }
/*    */   
/*    */   public RenderType renderType() {
/* 19 */     return ENDERMAN_EYES;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\EnderEyesLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */