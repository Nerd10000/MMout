/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import com.mojang.math.Transformation;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.statue.CopperGolemStatueModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.CopperGolemStatueRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolemOxidationLevels;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*    */ import net.minecraft.world.level.block.WeatheringCopper;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.CopperGolemStatueBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class CopperGolemStatueBlockRenderer implements BlockEntityRenderer<CopperGolemStatueBlockEntity, CopperGolemStatueRenderState> {
/* 31 */   private static final Map<Direction, Transformation> TRANSFORMATIONS = Util.makeEnumMap(Direction.class, CopperGolemStatueBlockRenderer::createModelTransformation);
/*    */   
/* 33 */   private final Map<CopperGolemStatueBlock.Pose, CopperGolemStatueModel> models = new HashMap<>();
/*    */   
/*    */   public CopperGolemStatueBlockRenderer(BlockEntityRendererProvider.Context context) {
/* 36 */     EntityModelSet modelSet = context.entityModelSet();
/* 37 */     this.models.put(CopperGolemStatueBlock.Pose.STANDING, new CopperGolemStatueModel(modelSet.bakeLayer(ModelLayers.COPPER_GOLEM)));
/* 38 */     this.models.put(CopperGolemStatueBlock.Pose.RUNNING, new CopperGolemStatueModel(modelSet.bakeLayer(ModelLayers.COPPER_GOLEM_RUNNING)));
/* 39 */     this.models.put(CopperGolemStatueBlock.Pose.SITTING, new CopperGolemStatueModel(modelSet.bakeLayer(ModelLayers.COPPER_GOLEM_SITTING)));
/* 40 */     this.models.put(CopperGolemStatueBlock.Pose.STAR, new CopperGolemStatueModel(modelSet.bakeLayer(ModelLayers.COPPER_GOLEM_STAR)));
/*    */   }
/*    */   
/*    */   public CopperGolemStatueRenderState createRenderState() {
/* 45 */     return new CopperGolemStatueRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(CopperGolemStatueBlockEntity blockEntity, CopperGolemStatueRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 50 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 51 */     BlockState blockState = blockEntity.getBlockState();
/* 52 */     state.direction = (Direction)blockState.getValue((Property)CopperGolemStatueBlock.FACING);
/* 53 */     state.pose = (CopperGolemStatueBlock.Pose)blockState.getValue((Property)CopperGolemStatueBlock.POSE);
/* 54 */     Block block = blockState.getBlock();
/* 54 */     CopperGolemStatueBlock copperGolemStatueBlock = (CopperGolemStatueBlock)block;
/* 54 */     state.oxidationState = (block instanceof CopperGolemStatueBlock) ? copperGolemStatueBlock.getWeatheringState() : WeatheringCopper.WeatherState.UNAFFECTED;
/*    */   }
/*    */   
/*    */   public void submit(CopperGolemStatueRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 59 */     poseStack.pushPose();
/* 60 */     poseStack.mulPose(modelTransformation(state.direction));
/* 61 */     CopperGolemStatueModel model = this.models.get(state.pose);
/* 62 */     submitNodeCollector.submitModel((Model)model, Unit.INSTANCE, poseStack, CopperGolemOxidationLevels.getOxidationLevel(state.oxidationState).texture(), state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
/* 63 */     poseStack.popPose();
/*    */   }
/*    */   
/*    */   public static Transformation modelTransformation(Direction facing) {
/* 67 */     return TRANSFORMATIONS.get(facing);
/*    */   }
/*    */   
/*    */   private static Transformation createModelTransformation(Direction entityDirection) {
/* 71 */     return new Transformation((Matrix4fc)(new Matrix4f())
/*    */         
/* 73 */         .translation(0.5F, 0.0F, 0.5F)
/* 74 */         .rotate((Quaternionfc)Axis.YP.rotationDegrees(-entityDirection.getOpposite().toYRot())));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\CopperGolemStatueBlockRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */