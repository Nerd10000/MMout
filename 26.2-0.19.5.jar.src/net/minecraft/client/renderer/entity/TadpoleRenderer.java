/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.animal.frog.TadpoleModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.animal.frog.Tadpole;
/*    */ 
/*    */ public class TadpoleRenderer extends MobRenderer<Tadpole, LivingEntityRenderState, TadpoleModel> {
/* 10 */   private static final Identifier TADPOLE_TEXTURE = Identifier.withDefaultNamespace("textures/entity/tadpole/tadpole.png");
/*    */   
/*    */   public TadpoleRenderer(EntityRendererProvider.Context context) {
/* 13 */     super(context, new TadpoleModel(context.bakeLayer(ModelLayers.TADPOLE)), 0.14F);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(LivingEntityRenderState state) {
/* 18 */     return TADPOLE_TEXTURE;
/*    */   }
/*    */   
/*    */   public LivingEntityRenderState createRenderState() {
/* 23 */     return new LivingEntityRenderState();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\TadpoleRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */