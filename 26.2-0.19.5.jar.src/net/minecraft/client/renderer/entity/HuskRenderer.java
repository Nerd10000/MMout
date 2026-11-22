/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class HuskRenderer extends ZombieRenderer {
/*  8 */   private static final Identifier HUSK_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/husk.png");
/*    */   
/*  9 */   private static final Identifier BABY_HUSK_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/husk_baby.png");
/*    */   
/*    */   public HuskRenderer(EntityRendererProvider.Context context) {
/* 12 */     super(context, ModelLayers.HUSK, ModelLayers.HUSK_BABY, ModelLayers.HUSK_ARMOR, ModelLayers.HUSK_BABY_ARMOR);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(ZombieRenderState state) {
/* 17 */     return state.isBaby ? BABY_HUSK_LOCATION : HUSK_LOCATION;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\HuskRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */