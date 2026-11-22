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
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SheepRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public class SheepWoolUndercoatLayer extends RenderLayer<SheepRenderState, SheepModel> {
/* 16 */   private static final Identifier SHEEP_WOOL_UNDERCOAT_LOCATION = Identifier.withDefaultNamespace("textures/entity/sheep/sheep_wool_undercoat.png");
/*    */   
/*    */   private final EntityModel<SheepRenderState> model;
/*    */   
/*    */   public SheepWoolUndercoatLayer(RenderLayerParent<SheepRenderState, SheepModel> renderer, EntityModelSet modelSet) {
/* 21 */     super(renderer);
/* 22 */     this.model = (EntityModel<SheepRenderState>)new SheepFurModel(modelSet.bakeLayer(ModelLayers.SHEEP_WOOL_UNDERCOAT));
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SheepRenderState state, float yRot, float xRot) {
/* 27 */     if (state.isInvisible || (!state.isJebSheep && state.woolColor == DyeColor.WHITE) || state.isBaby)
/*    */       return; 
/* 31 */     coloredCutoutModelCopyLayerRender((Model<? super SheepRenderState>)this.model, SHEEP_WOOL_UNDERCOAT_LOCATION, poseStack, submitNodeCollector, lightCoords, state, state.getWoolColor(), 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\SheepWoolUndercoatLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */