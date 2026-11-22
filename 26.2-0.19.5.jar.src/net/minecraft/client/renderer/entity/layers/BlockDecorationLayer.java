/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.block.BlockModelRenderState;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class BlockDecorationLayer<S extends EntityRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
/* 18 */   private static final Matrix4fc UNIT_CUBE_BOTTOM_CENTER_TO_ANTENNA_CENTER = (Matrix4fc)(new Matrix4f())
/* 19 */     .translation(-0.5F, 0.0F, -0.5F)
/* 20 */     .rotateAround((Quaternionfc)Axis.ZP.rotationDegrees(180.0F), 0.5F, 0.5F, 0.5F);
/*    */   
/*    */   private final Function<S, BlockModelRenderState> blockModel;
/*    */   
/*    */   private final Consumer<PoseStack> transform;
/*    */   
/*    */   public BlockDecorationLayer(RenderLayerParent<S, M> renderer, Function<S, BlockModelRenderState> blockModel, Consumer<PoseStack> transform) {
/* 26 */     super(renderer);
/* 27 */     this.blockModel = blockModel;
/* 28 */     this.transform = transform;
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/* 33 */     BlockModelRenderState blockModel = this.blockModel.apply(state);
/* 34 */     if (blockModel.isEmpty())
/*    */       return; 
/* 38 */     poseStack.pushPose();
/* 40 */     this.transform.accept(poseStack);
/* 44 */     poseStack.mulPose(UNIT_CUBE_BOTTOM_CENTER_TO_ANTENNA_CENTER);
/* 46 */     blockModel.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, ((EntityRenderState)state).outlineColor);
/* 48 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\BlockDecorationLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */