/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.renderer.FaceInfo;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.EndPortalRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public abstract class AbstractEndPortalRenderer<T extends TheEndPortalBlockEntity, S extends EndPortalRenderState> implements BlockEntityRenderer<T, S> {
/* 24 */   private static final Vector3fc FROM = (Vector3fc)new Vector3f(0.0F, 0.0F, 0.0F);
/*    */   
/* 25 */   private static final Vector3fc TO = (Vector3fc)new Vector3f(1.0F, 1.0F, 1.0F);
/*    */   
/*    */   private static final Map<Direction, List<Vector3fc>> FACES;
/*    */   
/*    */   static {
/* 27 */     FACES = Util.makeEnumMap(Direction.class, direction -> {
/*    */           FaceInfo faceInfo = FaceInfo.fromFacing(direction);
/*    */           return List.of(faceInfo.getVertexInfo(0).select(FROM, TO), faceInfo.getVertexInfo(1).select(FROM, TO), faceInfo.getVertexInfo(2).select(FROM, TO), faceInfo.getVertexInfo(3).select(FROM, TO));
/*    */         });
/*    */   }
/*    */   
/* 37 */   public static final Identifier END_SKY_LOCATION = Identifier.withDefaultNamespace("textures/environment/end_sky.png");
/*    */   
/* 38 */   public static final Identifier END_PORTAL_LOCATION = Identifier.withDefaultNamespace("textures/entity/end_portal/end_portal.png");
/*    */   
/* 39 */   private static final List<Direction> ALL_FACES = List.of(Direction.values());
/*    */   
/*    */   public void extractRenderState(T blockEntity, S state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 43 */     super.extractRenderState((BlockEntity)blockEntity, (BlockEntityRenderState)state, partialTicks, cameraPosition, breakProgress);
/* 44 */     ((EndPortalRenderState)state).facesToShow.clear();
/* 45 */     for (Direction direction : Direction.values()) {
/* 46 */       if (blockEntity.shouldRenderFace(direction))
/* 47 */         ((EndPortalRenderState)state).facesToShow.add(direction); 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected static void submitCube(Collection<Direction> facesToShow, RenderType renderType, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
/* 53 */     if (facesToShow.isEmpty())
/*    */       return; 
/* 56 */     submitNodeCollector.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
/*    */           for (Direction direction : facesToShow) {
/*    */             List<Vector3fc> faceVertices = FACES.get(direction);
/*    */             for (Vector3fc faceVertex : faceVertices)
/*    */               buffer.addVertex(pose, faceVertex); 
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   public static void submitSpecial(RenderType renderType, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
/* 67 */     submitCube(ALL_FACES, renderType, poseStack, submitNodeCollector);
/*    */   }
/*    */   
/*    */   public static void getExtents(Consumer<Vector3fc> output) {
/* 71 */     FACES.values().forEach(vertices -> vertices.forEach(output));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\AbstractEndPortalRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */