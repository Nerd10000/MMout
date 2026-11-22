/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.HoglinRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.monster.Zoglin;
/*    */ 
/*    */ public class ZoglinRenderer extends AbstractHoglinRenderer<Zoglin> {
/*  9 */   private static final Identifier ZOGLIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/hoglin/zoglin.png");
/*    */   
/* 10 */   private static final Identifier BABY_ZOGLIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/hoglin/zoglin_baby.png");
/*    */   
/*    */   public ZoglinRenderer(EntityRendererProvider.Context context) {
/* 13 */     super(context, ModelLayers.ZOGLIN, ModelLayers.ZOGLIN_BABY, 0.7F);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(HoglinRenderState state) {
/* 18 */     return state.isBaby ? BABY_ZOGLIN_LOCATION : ZOGLIN_LOCATION;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\ZoglinRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */