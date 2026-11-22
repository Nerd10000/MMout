/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.zombie.BabyDrownedModel;
/*    */ import net.minecraft.client.model.monster.zombie.DrownedModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class DrownedOuterLayer extends RenderLayer<ZombieRenderState, DrownedModel> {
/* 15 */   private static final Identifier DROWNED_OUTER_LAYER_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/drowned_outer_layer.png");
/*    */   
/* 16 */   private static final Identifier BABY_DROWNED_OUTER_LAYER_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/drowned_outer_layer_baby.png");
/*    */   
/*    */   private final DrownedModel model;
/*    */   
/*    */   private final DrownedModel babyModel;
/*    */   
/*    */   public DrownedOuterLayer(RenderLayerParent<ZombieRenderState, DrownedModel> renderer, EntityModelSet modelSet) {
/* 22 */     super(renderer);
/* 23 */     this.model = new DrownedModel(modelSet.bakeLayer(ModelLayers.DROWNED_OUTER_LAYER));
/* 24 */     this.babyModel = (DrownedModel)new BabyDrownedModel(modelSet.bakeLayer(ModelLayers.DROWNED_BABY_OUTER_LAYER));
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, ZombieRenderState state, float yRot, float xRot) {
/* 29 */     DrownedModel model = state.isBaby ? this.babyModel : this.model;
/* 30 */     Identifier layerLocation = state.isBaby ? BABY_DROWNED_OUTER_LAYER_LOCATION : DROWNED_OUTER_LAYER_LOCATION;
/* 31 */     coloredCutoutModelCopyLayerRender((Model<? super ZombieRenderState>)model, layerLocation, poseStack, submitNodeCollector, lightCoords, state, -1, 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\DrownedOuterLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */