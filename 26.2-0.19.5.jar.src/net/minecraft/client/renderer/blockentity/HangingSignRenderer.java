/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.math.Axis;
/*    */ import com.mojang.math.Transformation;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.HangingSignRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.SignRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.CeilingHangingSignBlock;
/*    */ import net.minecraft.world.level.block.HangingSignBlock;
/*    */ import net.minecraft.world.level.block.WallHangingSignBlock;
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
/*    */ public class HangingSignRenderer extends AbstractSignRenderer<HangingSignRenderState> {
/*    */   private static final float TEXT_RENDER_SCALE = 0.9F;
/*    */   
/* 29 */   private static final Vector3fc TEXT_OFFSET = (Vector3fc)new Vector3f(0.0F, -0.32F, 0.073F);
/*    */   
/* 31 */   public static final WallAndGroundTransformations<SignRenderState.SignTransformations> TRANSFORMATIONS = new WallAndGroundTransformations<>(HangingSignRenderer::createWallTransformation, HangingSignRenderer::createGroundTransformation, 16);
/*    */   
/*    */   public HangingSignRenderer(BlockEntityRendererProvider.Context context) {
/* 38 */     super(context);
/*    */   }
/*    */   
/*    */   public HangingSignRenderState createRenderState() {
/* 43 */     return new HangingSignRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(SignBlockEntity blockEntity, HangingSignRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 48 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 49 */     BlockState blockState = blockEntity.getBlockState();
/* 50 */     state.attachmentType = HangingSignBlock.getAttachmentPoint(blockState);
/* 52 */     if (blockState.getBlock() instanceof WallHangingSignBlock) {
/* 53 */       state.transformations = TRANSFORMATIONS.wallTransformation((Direction)blockState.getValue((Property)WallHangingSignBlock.FACING));
/*    */     } else {
/* 55 */       state.transformations = TRANSFORMATIONS.freeTransformations(((Integer)blockState.getValue((Property)CeilingHangingSignBlock.ROTATION)).intValue());
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Transformation textTransformation(float angle, boolean isFrontText) {
/* 63 */     Matrix4f result = (new Matrix4f()).translation(0.5F, 0.9375F, 0.5F).rotate((Quaternionfc)Axis.YP.rotationDegrees(-angle)).translate(0.0F, -0.3125F, 0.0F);
/* 64 */     if (!isFrontText)
/* 65 */       result.rotate((Quaternionfc)Axis.YP.rotationDegrees(180.0F)); 
/* 68 */     float s = 0.0140625F;
/* 69 */     result.translate(TEXT_OFFSET);
/* 70 */     result.scale(0.0140625F, -0.0140625F, 0.0140625F);
/* 71 */     return new Transformation((Matrix4fc)result);
/*    */   }
/*    */   
/*    */   private static SignRenderState.SignTransformations createTransformations(float angle) {
/* 75 */     return new SignRenderState.SignTransformations(
/* 76 */         textTransformation(angle, true), 
/* 77 */         textTransformation(angle, false));
/*    */   }
/*    */   
/*    */   private static SignRenderState.SignTransformations createGroundTransformation(int segment) {
/* 82 */     return createTransformations(RotationSegment.convertToDegrees(segment));
/*    */   }
/*    */   
/*    */   private static SignRenderState.SignTransformations createWallTransformation(Direction direction) {
/* 86 */     return createTransformations(direction.toYRot());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\HangingSignRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */