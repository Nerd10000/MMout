/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.math.Axis;
/*    */ import com.mojang.math.Transformation;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.SignRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.StandingSignRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.PlainSignBlock;
/*    */ import net.minecraft.world.level.block.StandingSignBlock;
/*    */ import net.minecraft.world.level.block.WallSignBlock;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ import org.joml.Quaternionfc;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class StandingSignRenderer extends AbstractSignRenderer<StandingSignRenderState> {
/*    */   private static final float RENDER_SCALE = 0.6666667F;
/*    */   
/* 23 */   private static final Vector3fc TEXT_OFFSET = (Vector3fc)new Vector3f(0.0F, 0.33333334F, 0.046666667F);
/*    */   
/* 25 */   public static final WallAndGroundTransformations<SignRenderState.SignTransformations> TRANSFORMATIONS = new WallAndGroundTransformations<>(StandingSignRenderer::createWallTransformation, StandingSignRenderer::createGroundTransformation, 16);
/*    */   
/*    */   public StandingSignRenderer(BlockEntityRendererProvider.Context context) {
/* 32 */     super(context);
/*    */   }
/*    */   
/*    */   public StandingSignRenderState createRenderState() {
/* 37 */     return new StandingSignRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(SignBlockEntity blockEntity, StandingSignRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 42 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 43 */     BlockState blockState = blockEntity.getBlockState();
/* 44 */     state.attachmentType = PlainSignBlock.getAttachmentPoint(blockState);
/* 46 */     if (blockState.getBlock() instanceof WallSignBlock) {
/* 47 */       state.transformations = TRANSFORMATIONS.wallTransformation((Direction)blockState.getValue((Property)WallSignBlock.FACING));
/*    */     } else {
/* 49 */       state.transformations = TRANSFORMATIONS.freeTransformations(((Integer)blockState.getValue((Property)StandingSignBlock.ROTATION)).intValue());
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Transformation textTransformation(PlainSignBlock.Attachment attachmentType, float angle, boolean isFrontText) {
/* 56 */     Matrix4f result = (new Matrix4f()).translate(0.5F, 0.5F, 0.5F).rotate((Quaternionfc)Axis.YP.rotationDegrees(-angle));
/* 58 */     if (attachmentType == PlainSignBlock.Attachment.WALL)
/* 59 */       result.translate(0.0F, -0.3125F, -0.4375F); 
/* 62 */     if (!isFrontText)
/* 63 */       result.rotate((Quaternionfc)Axis.YP.rotationDegrees(180.0F)); 
/* 66 */     float s = 0.010416667F;
/* 67 */     return new Transformation((Matrix4fc)result
/* 68 */         .translate(TEXT_OFFSET)
/* 69 */         .scale(0.010416667F, -0.010416667F, 0.010416667F));
/*    */   }
/*    */   
/*    */   private static SignRenderState.SignTransformations createTransformations(PlainSignBlock.Attachment attachmentType, float angle) {
/* 74 */     return new SignRenderState.SignTransformations(
/* 75 */         textTransformation(attachmentType, angle, true), 
/* 76 */         textTransformation(attachmentType, angle, false));
/*    */   }
/*    */   
/*    */   private static SignRenderState.SignTransformations createGroundTransformation(int segment) {
/* 81 */     return createTransformations(PlainSignBlock.Attachment.GROUND, RotationSegment.convertToDegrees(segment));
/*    */   }
/*    */   
/*    */   private static SignRenderState.SignTransformations createWallTransformation(Direction direction) {
/* 85 */     return createTransformations(PlainSignBlock.Attachment.WALL, direction.toYRot());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\StandingSignRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */