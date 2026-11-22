/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.animal.goat.BabyGoatModel;
/*    */ import net.minecraft.client.model.animal.goat.GoatModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.GoatRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.goat.Goat;
/*    */ 
/*    */ public class GoatRenderer extends AgeableMobRenderer<Goat, GoatRenderState, GoatModel> {
/* 11 */   private static final Identifier GOAT_LOCATION = Identifier.withDefaultNamespace("textures/entity/goat/goat.png");
/*    */   
/* 12 */   private static final Identifier BABY_GOAT_LOCATION = Identifier.withDefaultNamespace("textures/entity/goat/goat_baby.png");
/*    */   
/*    */   public GoatRenderer(EntityRendererProvider.Context context) {
/* 15 */     super(context, new GoatModel(context.bakeLayer(ModelLayers.GOAT)), new BabyGoatModel(context.bakeLayer(ModelLayers.GOAT_BABY)), 0.7F);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(GoatRenderState state) {
/* 20 */     return state.isBaby ? BABY_GOAT_LOCATION : GOAT_LOCATION;
/*    */   }
/*    */   
/*    */   public GoatRenderState createRenderState() {
/* 25 */     return new GoatRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Goat entity, GoatRenderState state, float partialTicks) {
/* 30 */     super.extractRenderState(entity, state, partialTicks);
/* 31 */     state.hasLeftHorn = entity.hasLeftHorn();
/* 32 */     state.hasRightHorn = entity.hasRightHorn();
/* 33 */     state.rammingXHeadRot = entity.getRammingXHeadRot();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\GoatRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */