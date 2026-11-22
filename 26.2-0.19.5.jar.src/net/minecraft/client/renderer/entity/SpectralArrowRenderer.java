/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.state.ArrowRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.projectile.arrow.SpectralArrow;
/*    */ 
/*    */ public class SpectralArrowRenderer extends ArrowRenderer<SpectralArrow, ArrowRenderState> {
/*  8 */   public static final Identifier SPECTRAL_ARROW_LOCATION = Identifier.withDefaultNamespace("textures/entity/projectiles/arrow_spectral.png");
/*    */   
/*    */   public SpectralArrowRenderer(EntityRendererProvider.Context context) {
/* 11 */     super(context);
/*    */   }
/*    */   
/*    */   protected Identifier getTextureLocation(ArrowRenderState state) {
/* 16 */     return SPECTRAL_ARROW_LOCATION;
/*    */   }
/*    */   
/*    */   public ArrowRenderState createRenderState() {
/* 21 */     return new ArrowRenderState();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\SpectralArrowRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */