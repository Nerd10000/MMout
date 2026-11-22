/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.feline.AbstractFelineModel;
/*    */ import net.minecraft.client.model.animal.feline.AdultCatModel;
/*    */ import net.minecraft.client.model.animal.feline.BabyCatModel;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.CatRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public class CatCollarLayer extends RenderLayer<CatRenderState, AbstractFelineModel<CatRenderState>> {
/* 16 */   private static final Identifier CAT_COLLAR_LOCATION = Identifier.withDefaultNamespace("textures/entity/cat/cat_collar.png");
/*    */   
/* 17 */   private static final Identifier CAT_BABY_COLLAR_LOCATION = Identifier.withDefaultNamespace("textures/entity/cat/cat_collar_baby.png");
/*    */   
/*    */   private final AdultCatModel adultModel;
/*    */   
/*    */   private final BabyCatModel babyModel;
/*    */   
/*    */   public CatCollarLayer(RenderLayerParent<CatRenderState, AbstractFelineModel<CatRenderState>> renderer, EntityModelSet modelSet) {
/* 23 */     super(renderer);
/* 24 */     this.adultModel = new AdultCatModel(modelSet.bakeLayer(ModelLayers.CAT_COLLAR));
/* 25 */     this.babyModel = new BabyCatModel(modelSet.bakeLayer(ModelLayers.CAT_BABY_COLLAR));
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CatRenderState state, float yRot, float xRot) {
/* 30 */     DyeColor collarColor = state.collarColor;
/* 31 */     if (collarColor == null)
/*    */       return; 
/* 34 */     int color = collarColor.getTextureDiffuseColor();
/* 35 */     AbstractFelineModel<CatRenderState> model = state.isBaby ? (AbstractFelineModel<CatRenderState>)this.babyModel : (AbstractFelineModel<CatRenderState>)this.adultModel;
/* 36 */     Identifier texture = state.isBaby ? CAT_BABY_COLLAR_LOCATION : CAT_COLLAR_LOCATION;
/* 37 */     coloredCutoutModelCopyLayerRender((Model<? super CatRenderState>)model, texture, poseStack, submitNodeCollector, lightCoords, state, color, 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\CatCollarLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */