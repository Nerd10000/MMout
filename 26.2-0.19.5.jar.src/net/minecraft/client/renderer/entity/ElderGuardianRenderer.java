/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.GuardianRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ElderGuardianRenderer extends GuardianRenderer {
/*  8 */   public static final Identifier GUARDIAN_ELDER_LOCATION = Identifier.withDefaultNamespace("textures/entity/guardian/guardian_elder.png");
/*    */   
/*    */   public ElderGuardianRenderer(EntityRendererProvider.Context context) {
/* 11 */     super(context, 1.2F, ModelLayers.ELDER_GUARDIAN);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(GuardianRenderState state) {
/* 16 */     return GUARDIAN_ELDER_LOCATION;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\ElderGuardianRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */