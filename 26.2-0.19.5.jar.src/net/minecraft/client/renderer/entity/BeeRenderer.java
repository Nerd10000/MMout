/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.animal.bee.AdultBeeModel;
/*    */ import net.minecraft.client.model.animal.bee.BabyBeeModel;
/*    */ import net.minecraft.client.model.animal.bee.BeeModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.BeeRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.bee.Bee;
/*    */ 
/*    */ public class BeeRenderer extends AgeableMobRenderer<Bee, BeeRenderState, BeeModel> {
/* 13 */   private static final Identifier ANGRY_BEE_TEXTURE = Identifier.withDefaultNamespace("textures/entity/bee/bee_angry.png");
/*    */   
/* 14 */   private static final Identifier ANGRY_NECTAR_BEE_TEXTURE = Identifier.withDefaultNamespace("textures/entity/bee/bee_angry_nectar.png");
/*    */   
/* 15 */   private static final Identifier BEE_TEXTURE = Identifier.withDefaultNamespace("textures/entity/bee/bee.png");
/*    */   
/* 16 */   private static final Identifier NECTAR_BEE_TEXTURE = Identifier.withDefaultNamespace("textures/entity/bee/bee_nectar.png");
/*    */   
/* 17 */   private static final Identifier ANGRY_BEE_BABY_TEXTURE = Identifier.withDefaultNamespace("textures/entity/bee/bee_angry_baby.png");
/*    */   
/* 18 */   private static final Identifier ANGRY_NECTAR_BEE_BABY_TEXTURE = Identifier.withDefaultNamespace("textures/entity/bee/bee_angry_nectar_baby.png");
/*    */   
/* 19 */   private static final Identifier BEE_BABY_TEXTURE = Identifier.withDefaultNamespace("textures/entity/bee/bee_baby.png");
/*    */   
/* 20 */   private static final Identifier NECTAR_BEE_BABY_TEXTURE = Identifier.withDefaultNamespace("textures/entity/bee/bee_nectar_baby.png");
/*    */   
/*    */   public BeeRenderer(EntityRendererProvider.Context context) {
/* 23 */     super(context, new AdultBeeModel(context.bakeLayer(ModelLayers.BEE)), new BabyBeeModel(context.bakeLayer(ModelLayers.BEE_BABY)), 0.4F);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(BeeRenderState state) {
/* 28 */     if (state.isAngry) {
/* 29 */       if (state.hasNectar)
/* 30 */         return state.isBaby ? ANGRY_NECTAR_BEE_BABY_TEXTURE : ANGRY_NECTAR_BEE_TEXTURE; 
/* 32 */       return state.isBaby ? ANGRY_BEE_BABY_TEXTURE : ANGRY_BEE_TEXTURE;
/*    */     } 
/* 33 */     if (state.hasNectar)
/* 34 */       return state.isBaby ? NECTAR_BEE_BABY_TEXTURE : NECTAR_BEE_TEXTURE; 
/* 36 */     return state.isBaby ? BEE_BABY_TEXTURE : BEE_TEXTURE;
/*    */   }
/*    */   
/*    */   public BeeRenderState createRenderState() {
/* 41 */     return new BeeRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Bee entity, BeeRenderState state, float partialTicks) {
/* 46 */     super.extractRenderState(entity, state, partialTicks);
/* 47 */     state.rollAmount = entity.getRollAmount(partialTicks);
/* 48 */     state.hasStinger = !entity.hasStung();
/* 49 */     state.isOnGround = (entity.onGround() && entity.getDeltaMovement().lengthSqr() < 1.0E-7D);
/* 50 */     state.isAngry = entity.isAngry();
/* 51 */     state.hasNectar = entity.hasNectar();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\BeeRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */