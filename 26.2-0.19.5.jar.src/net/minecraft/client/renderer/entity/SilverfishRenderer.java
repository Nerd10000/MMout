/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.silverfish.SilverfishModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.monster.Silverfish;
/*    */ 
/*    */ public class SilverfishRenderer extends MobRenderer<Silverfish, LivingEntityRenderState, SilverfishModel> {
/* 10 */   private static final Identifier SILVERFISH_LOCATION = Identifier.withDefaultNamespace("textures/entity/silverfish/silverfish.png");
/*    */   
/*    */   public SilverfishRenderer(EntityRendererProvider.Context context) {
/* 13 */     super(context, new SilverfishModel(context.bakeLayer(ModelLayers.SILVERFISH)), 0.3F);
/*    */   }
/*    */   
/*    */   protected float getFlipDegrees() {
/* 18 */     return 180.0F;
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(LivingEntityRenderState state) {
/* 23 */     return SILVERFISH_LOCATION;
/*    */   }
/*    */   
/*    */   public LivingEntityRenderState createRenderState() {
/* 28 */     return new LivingEntityRenderState();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\SilverfishRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */