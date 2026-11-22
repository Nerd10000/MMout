/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.animal.turtle.AdultTurtleModel;
/*    */ import net.minecraft.client.model.animal.turtle.BabyTurtleModel;
/*    */ import net.minecraft.client.model.animal.turtle.TurtleModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.TurtleRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*    */ 
/*    */ public class TurtleRenderer extends AgeableMobRenderer<Turtle, TurtleRenderState, TurtleModel> {
/* 12 */   private static final Identifier TURTLE_LOCATION = Identifier.withDefaultNamespace("textures/entity/turtle/turtle.png");
/*    */   
/* 13 */   private static final Identifier BABY_TURTLE_LOCATION = Identifier.withDefaultNamespace("textures/entity/turtle/turtle_baby.png");
/*    */   
/*    */   public TurtleRenderer(EntityRendererProvider.Context context) {
/* 16 */     super(context, new AdultTurtleModel(context.bakeLayer(ModelLayers.TURTLE)), new BabyTurtleModel(context.bakeLayer(ModelLayers.TURTLE_BABY)), 0.7F);
/*    */   }
/*    */   
/*    */   protected float getShadowRadius(TurtleRenderState state) {
/* 21 */     float radius = super.getShadowRadius(state);
/* 22 */     if (state.isBaby)
/* 24 */       return radius * 0.83F; 
/* 26 */     return radius;
/*    */   }
/*    */   
/*    */   public TurtleRenderState createRenderState() {
/* 31 */     return new TurtleRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Turtle entity, TurtleRenderState state, float partialTicks) {
/* 36 */     super.extractRenderState(entity, state, partialTicks);
/* 37 */     state.isOnLand = (!entity.isInWater() && entity.onGround());
/* 38 */     state.isLayingEgg = entity.isLayingEgg();
/* 39 */     state.hasEgg = (!entity.isBaby() && entity.hasEgg());
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(TurtleRenderState state) {
/* 44 */     return state.isBaby ? BABY_TURTLE_LOCATION : TURTLE_LOCATION;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\TurtleRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */