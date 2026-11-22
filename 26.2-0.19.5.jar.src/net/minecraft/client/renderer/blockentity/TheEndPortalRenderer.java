/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Transformation;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.EndPortalRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class TheEndPortalRenderer extends AbstractEndPortalRenderer<TheEndPortalBlockEntity, EndPortalRenderState> {
/*    */   private static final float BOTTOM = 0.375F;
/*    */   
/*    */   private static final float TOP = 0.75F;
/*    */   
/* 16 */   public static final Transformation TRANSFORMATION = new Transformation((Vector3fc)new Vector3f(0.0F, 0.375F, 0.0F), null, (Vector3fc)new Vector3f(1.0F, 0.375F, 1.0F), null);
/*    */   
/*    */   public EndPortalRenderState createRenderState() {
/* 26 */     return new EndPortalRenderState();
/*    */   }
/*    */   
/*    */   public void submit(EndPortalRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 31 */     poseStack.pushPose();
/* 32 */     poseStack.mulPose(TRANSFORMATION);
/* 33 */     submitCube(state.facesToShow, RenderTypes.endPortal(), poseStack, submitNodeCollector);
/* 34 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\TheEndPortalRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */