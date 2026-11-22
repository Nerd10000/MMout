/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.breeze.BreezeModel;
/*    */ import net.minecraft.client.renderer.entity.layers.BreezeEyesLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.BreezeWindLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.BreezeRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.breeze.Breeze;
/*    */ 
/*    */ public class BreezeRenderer extends MobRenderer<Breeze, BreezeRenderState, BreezeModel> {
/* 13 */   private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/breeze/breeze.png");
/*    */   
/*    */   public BreezeRenderer(EntityRendererProvider.Context context) {
/* 16 */     super(context, new BreezeModel(context.bakeLayer(ModelLayers.BREEZE)), 0.5F);
/* 17 */     addLayer((RenderLayer<BreezeRenderState, BreezeModel>)new BreezeWindLayer(this, context.getModelSet()));
/* 18 */     addLayer((RenderLayer<BreezeRenderState, BreezeModel>)new BreezeEyesLayer(this, context.getModelSet()));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(BreezeRenderState state) {
/* 23 */     return TEXTURE_LOCATION;
/*    */   }
/*    */   
/*    */   public BreezeRenderState createRenderState() {
/* 28 */     return new BreezeRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Breeze entity, BreezeRenderState state, float partialTicks) {
/* 33 */     super.extractRenderState(entity, state, partialTicks);
/* 34 */     state.idle.copyFrom(entity.idle);
/* 35 */     state.shoot.copyFrom(entity.shoot);
/* 36 */     state.slide.copyFrom(entity.slide);
/* 37 */     state.slideBack.copyFrom(entity.slideBack);
/* 38 */     state.inhale.copyFrom(entity.inhale);
/* 39 */     state.longJump.copyFrom(entity.longJump);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\BreezeRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */