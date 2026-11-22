/*    */ package net.minecraft.client.gui.render.pip;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Lighting;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.state.gui.pip.GuiSkinRenderState;
/*    */ import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.util.Unit;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class GuiSkinRenderer extends PictureInPictureRenderer<GuiSkinRenderState> {
/*    */   public Class<GuiSkinRenderState> getRenderStateClass() {
/* 19 */     return GuiSkinRenderState.class;
/*    */   }
/*    */   
/*    */   protected void renderToTexture(GuiSkinRenderState skinState, PoseStack modelStack, SubmitNodeCollector submitNodeCollector) {
/* 24 */     (Minecraft.getInstance()).gameRenderer.lighting().setupFor(Lighting.Entry.PLAYER_SKIN);
/* 26 */     int guiScale = ((Minecraft.getInstance()).gameRenderer.gameRenderState()).windowRenderState.guiScale;
/* 27 */     float scale = skinState.scale() * guiScale;
/* 28 */     RenderSystem.getModelViewStack().rotateAround((Quaternionfc)Axis.XP.rotationDegrees(skinState.rotationX()), 0.0F, scale * -skinState.pivotY(), 0.0F);
/* 30 */     modelStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-skinState.rotationY()));
/* 32 */     modelStack.translate(0.0F, -1.6010001F, 0.0F);
/* 34 */     submitNodeCollector.submitModel((Model)skinState.playerModel(), Unit.INSTANCE, modelStack, skinState.texture(), 15728880, OverlayTexture.NO_OVERLAY, 0, null);
/*    */   }
/*    */   
/*    */   protected String getTextureLabel() {
/* 40 */     return "player skin";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\render\pip\GuiSkinRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */