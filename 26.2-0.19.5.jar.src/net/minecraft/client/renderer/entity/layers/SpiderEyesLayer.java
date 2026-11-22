/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.model.monster.spider.SpiderModel;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class SpiderEyesLayer<M extends SpiderModel> extends EyesLayer<LivingEntityRenderState, M> {
/* 11 */   private static final RenderType SPIDER_EYES = RenderTypes.eyes(Identifier.withDefaultNamespace("textures/entity/spider/spider_eyes.png"));
/*    */   
/*    */   public SpiderEyesLayer(RenderLayerParent<LivingEntityRenderState, M> renderer) {
/* 14 */     super(renderer);
/*    */   }
/*    */   
/*    */   public RenderType renderType() {
/* 19 */     return SPIDER_EYES;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\SpiderEyesLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */