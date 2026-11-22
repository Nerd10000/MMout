/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.state.ArrowRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.TippableArrowRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*    */ import net.minecraft.world.entity.projectile.arrow.Arrow;
/*    */ 
/*    */ public class TippableArrowRenderer extends ArrowRenderer<Arrow, TippableArrowRenderState> {
/*  8 */   public static final Identifier NORMAL_ARROW_LOCATION = Identifier.withDefaultNamespace("textures/entity/projectiles/arrow.png");
/*    */   
/*  9 */   public static final Identifier TIPPED_ARROW_LOCATION = Identifier.withDefaultNamespace("textures/entity/projectiles/arrow_tipped.png");
/*    */   
/*    */   public TippableArrowRenderer(EntityRendererProvider.Context context) {
/* 12 */     super(context);
/*    */   }
/*    */   
/*    */   protected Identifier getTextureLocation(TippableArrowRenderState state) {
/* 17 */     return state.isTipped ? TIPPED_ARROW_LOCATION : NORMAL_ARROW_LOCATION;
/*    */   }
/*    */   
/*    */   public TippableArrowRenderState createRenderState() {
/* 22 */     return new TippableArrowRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Arrow entity, TippableArrowRenderState state, float partialTicks) {
/* 27 */     super.extractRenderState(entity, state, partialTicks);
/* 28 */     state.isTipped = (entity.getColor() > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\TippableArrowRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */