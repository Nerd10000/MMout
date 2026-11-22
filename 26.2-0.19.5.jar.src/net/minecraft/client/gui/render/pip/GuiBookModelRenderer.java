/*    */ package net.minecraft.client.gui.render.pip;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Lighting;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.object.book.BookModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.state.gui.pip.GuiBookModelRenderState;
/*    */ import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.util.Mth;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class GuiBookModelRenderer extends PictureInPictureRenderer<GuiBookModelRenderState> {
/*    */   public Class<GuiBookModelRenderState> getRenderStateClass() {
/* 18 */     return GuiBookModelRenderState.class;
/*    */   }
/*    */   
/*    */   protected void renderToTexture(GuiBookModelRenderState bookModelState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
/* 23 */     (Minecraft.getInstance()).gameRenderer.lighting().setupFor(Lighting.Entry.ENTITY_IN_UI);
/* 25 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F));
/* 26 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(25.0F));
/* 27 */     float open = bookModelState.open();
/* 28 */     poseStack.translate((1.0F - open) * 0.2F, (1.0F - open) * 0.1F, (1.0F - open) * 0.25F);
/* 29 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-(1.0F - open) * 90.0F - 90.0F));
/* 30 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(180.0F));
/* 32 */     float flip = bookModelState.flip();
/* 33 */     float pageFlip1 = Mth.clamp(Mth.frac(flip + 0.25F) * 1.6F - 0.3F, 0.0F, 1.0F);
/* 34 */     float pageFlip2 = Mth.clamp(Mth.frac(flip + 0.75F) * 1.6F - 0.3F, 0.0F, 1.0F);
/* 36 */     BookModel.State state = BookModel.State.forAnimation(0.0F, pageFlip1, pageFlip2, open);
/* 37 */     submitNodeCollector.submitModel((Model)bookModelState.bookModel(), state, poseStack, bookModelState.texture(), 15728880, OverlayTexture.NO_OVERLAY, 0, null);
/*    */   }
/*    */   
/*    */   protected float getTranslateY(int height, int guiScale) {
/* 42 */     return (17 * guiScale);
/*    */   }
/*    */   
/*    */   protected String getTextureLabel() {
/* 47 */     return "book model";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\render\pip\GuiBookModelRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */