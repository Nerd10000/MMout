/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.sheep.SheepFurModel;
/*    */ import net.minecraft.client.model.animal.sheep.SheepModel;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SheepRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class SheepWoolLayer extends RenderLayer<SheepRenderState, SheepModel> {
/* 18 */   private static final Identifier SHEEP_WOOL_LOCATION = Identifier.withDefaultNamespace("textures/entity/sheep/sheep_wool.png");
/*    */   
/* 19 */   private static final Identifier BABY_SHEEP_WOOL_LOCATION = Identifier.withDefaultNamespace("textures/entity/sheep/sheep_wool_baby.png");
/*    */   
/*    */   private final EntityModel<SheepRenderState> adultModel;
/*    */   
/*    */   private final EntityModel<SheepRenderState> babyModel;
/*    */   
/*    */   public SheepWoolLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityModelSet modelSet) {
/* 25 */     super(renderer);
/* 26 */     this.adultModel = (EntityModel<SheepRenderState>)new SheepFurModel(modelSet.bakeLayer(ModelLayers.SHEEP_WOOL));
/* 27 */     this.babyModel = (EntityModel<SheepRenderState>)new SheepFurModel(modelSet.bakeLayer(ModelLayers.SHEEP_BABY_WOOL));
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SheepRenderState state, float yRot, float xRot) {
/* 32 */     if (state.isSheared)
/*    */       return; 
/* 36 */     EntityModel<SheepRenderState> model = state.isBaby ? this.babyModel : this.adultModel;
/* 37 */     Identifier location = state.isBaby ? BABY_SHEEP_WOOL_LOCATION : SHEEP_WOOL_LOCATION;
/* 39 */     if (state.isInvisible) {
/* 40 */       if (state.appearsGlowing())
/* 41 */         submitNodeCollector.submitModel((Model)model, state, poseStack, RenderTypes.outline(location), lightCoords, LivingEntityRenderer.getOverlayCoords((LivingEntityRenderState)state, 0.0F), -16777216, null, state.outlineColor, null); 
/*    */       return;
/*    */     } 
/* 47 */     coloredCutoutModelCopyLayerRender((Model<? super SheepRenderState>)model, location, poseStack, submitNodeCollector, lightCoords, state, state.getWoolColor(), state.isBaby ? 1 : 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\SheepWoolLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */