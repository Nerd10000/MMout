/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.animal.sheep.BabySheepModel;
/*    */ import net.minecraft.client.model.animal.sheep.SheepModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SheepWoolUndercoatLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SheepRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.sheep.Sheep;
/*    */ 
/*    */ public class SheepRenderer extends AgeableMobRenderer<Sheep, SheepRenderState, SheepModel> {
/* 13 */   private static final Identifier SHEEP_LOCATION = Identifier.withDefaultNamespace("textures/entity/sheep/sheep.png");
/*    */   
/* 14 */   private static final Identifier SHEEP_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/sheep/sheep_baby.png");
/*    */   
/*    */   public SheepRenderer(EntityRendererProvider.Context context) {
/* 17 */     super(context, new SheepModel(context.bakeLayer(ModelLayers.SHEEP)), new BabySheepModel(context.bakeLayer(ModelLayers.SHEEP_BABY)), 0.7F);
/* 19 */     addLayer((RenderLayer<SheepRenderState, SheepModel>)new SheepWoolUndercoatLayer(this, context.getModelSet()));
/* 20 */     addLayer((RenderLayer<SheepRenderState, SheepModel>)new SheepWoolLayer(this, context.getModelSet()));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(SheepRenderState state) {
/* 25 */     return state.isBaby ? SHEEP_BABY_LOCATION : SHEEP_LOCATION;
/*    */   }
/*    */   
/*    */   public SheepRenderState createRenderState() {
/* 30 */     return new SheepRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Sheep entity, SheepRenderState state, float partialTicks) {
/* 35 */     super.extractRenderState(entity, state, partialTicks);
/* 36 */     state.headEatAngleScale = entity.getHeadEatAngleScale(partialTicks);
/* 37 */     state.headEatPositionScale = entity.getHeadEatPositionScale(partialTicks);
/* 38 */     state.isSheared = entity.isSheared();
/* 39 */     state.woolColor = entity.getColor();
/* 41 */     state.isJebSheep = checkMagicName((Entity)entity, "jeb_");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\SheepRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */