/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.PaintingRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.data.AtlasIds;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.LightCoordsUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.decoration.painting.Painting;
/*     */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class PaintingRenderer extends EntityRenderer<Painting, PaintingRenderState> {
/*  26 */   private static final Identifier BACK_SPRITE_LOCATION = Identifier.withDefaultNamespace("back");
/*     */   
/*     */   private final TextureAtlas paintingsAtlas;
/*     */   
/*     */   public PaintingRenderer(EntityRendererProvider.Context context) {
/*  31 */     super(context);
/*  32 */     this.paintingsAtlas = context.getAtlas(AtlasIds.PAINTINGS);
/*     */   }
/*     */   
/*     */   public void submit(PaintingRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  37 */     PaintingVariant variant = state.variant;
/*  38 */     if (variant == null)
/*     */       return; 
/*  42 */     poseStack.pushPose();
/*  43 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees((180 - state.direction.get2DDataValue() * 90)));
/*  45 */     TextureAtlasSprite frontSprite = this.paintingsAtlas.getSprite(variant.assetId());
/*  46 */     TextureAtlasSprite backSprite = this.paintingsAtlas.getSprite(BACK_SPRITE_LOCATION);
/*  48 */     renderPainting(poseStack, submitNodeCollector, RenderTypes.entitySolidZOffsetForward(backSprite.atlasLocation()), state.lightCoordsPerBlock, variant.width(), variant.height(), frontSprite, backSprite);
/*  49 */     poseStack.popPose();
/*  51 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*     */   }
/*     */   
/*     */   public PaintingRenderState createRenderState() {
/*  56 */     return new PaintingRenderState();
/*     */   }
/*     */   
/*     */   public void extractRenderState(Painting entity, PaintingRenderState state, float partialTicks) {
/*  61 */     super.extractRenderState(entity, state, partialTicks);
/*  63 */     Direction direction = entity.getDirection();
/*  64 */     PaintingVariant variant = (PaintingVariant)entity.getVariant().value();
/*  65 */     state.direction = direction;
/*  66 */     state.variant = variant;
/*  68 */     int width = variant.width();
/*  69 */     int height = variant.height();
/*  70 */     if (state.lightCoordsPerBlock.length != width * height)
/*  71 */       state.lightCoordsPerBlock = new int[width * height]; 
/*  74 */     float offsetX = -width / 2.0F;
/*  75 */     float offsetY = -height / 2.0F;
/*  77 */     Level level = entity.level();
/*  78 */     for (int segmentY = 0; segmentY < height; segmentY++) {
/*  79 */       for (int segmentX = 0; segmentX < width; segmentX++) {
/*  80 */         float segmentOffsetX = segmentX + offsetX + 0.5F;
/*  81 */         float segmentOffsetY = segmentY + offsetY + 0.5F;
/*  82 */         int x = entity.getBlockX();
/*  83 */         int y = Mth.floor(entity.getY() + segmentOffsetY);
/*  84 */         int z = entity.getBlockZ();
/*  85 */         switch (direction) {
/*     */           case NORTH:
/*  86 */             x = Mth.floor(entity.getX() + segmentOffsetX);
/*     */             break;
/*     */           case WEST:
/*  87 */             z = Mth.floor(entity.getZ() - segmentOffsetX);
/*     */             break;
/*     */           case SOUTH:
/*  88 */             x = Mth.floor(entity.getX() - segmentOffsetX);
/*     */             break;
/*     */           case EAST:
/*  89 */             z = Mth.floor(entity.getZ() + segmentOffsetX);
/*     */             break;
/*     */         } 
/*  92 */         state.lightCoordsPerBlock[segmentX + segmentY * width] = LightCoordsUtil.getLightCoords((BlockAndLightGetter)level, new BlockPos(x, y, z));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderPainting(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, RenderType renderType, int[] lightCoordsMap, int width, int height, TextureAtlasSprite front, TextureAtlasSprite back) {
/*  98 */     submitNodeCollector.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
/*     */           float offsetX = -width / 2.0F;
/*     */           float offsetY = -height / 2.0F;
/*     */           float edgeHalfWidth = 0.03125F;
/*     */           float backU0 = back.getU0();
/*     */           float backU1 = back.getU1();
/*     */           float backV0 = back.getV0();
/*     */           float backV1 = back.getV1();
/*     */           float topBottomU0 = back.getU0();
/*     */           float topBottomU1 = back.getU1();
/*     */           float topBottomV0 = back.getV0();
/*     */           float topBottomV1 = back.getV(0.0625F);
/*     */           float leftRightU0 = back.getU0();
/*     */           float leftRightU1 = back.getU(0.0625F);
/*     */           float leftRightV0 = back.getV0();
/*     */           float leftRightV1 = back.getV1();
/*     */           double deltaU = 1.0D / width;
/*     */           double deltaV = 1.0D / height;
/*     */           for (int segmentX = 0; segmentX < width; segmentX++) {
/*     */             for (int segmentY = 0; segmentY < height; segmentY++) {
/*     */               float x0 = offsetX + (segmentX + 1);
/*     */               float x1 = offsetX + segmentX;
/*     */               float y0 = offsetY + (segmentY + 1);
/*     */               float y1 = offsetY + segmentY;
/*     */               int lightCoords = lightCoordsMap[segmentX + segmentY * width];
/*     */               float frontU0 = front.getU((float)(deltaU * (width - segmentX)));
/*     */               float frontU1 = front.getU((float)(deltaU * (width - segmentX + 1)));
/*     */               float frontV0 = front.getV((float)(deltaV * (height - segmentY)));
/*     */               float frontV1 = front.getV((float)(deltaV * (height - segmentY + 1)));
/*     */               vertex(pose, buffer, x0, y1, frontU1, frontV0, -0.03125F, 0, 0, -1, lightCoords);
/*     */               vertex(pose, buffer, x1, y1, frontU0, frontV0, -0.03125F, 0, 0, -1, lightCoords);
/*     */               vertex(pose, buffer, x1, y0, frontU0, frontV1, -0.03125F, 0, 0, -1, lightCoords);
/*     */               vertex(pose, buffer, x0, y0, frontU1, frontV1, -0.03125F, 0, 0, -1, lightCoords);
/*     */               vertex(pose, buffer, x0, y0, backU1, backV0, 0.03125F, 0, 0, 1, lightCoords);
/*     */               vertex(pose, buffer, x1, y0, backU0, backV0, 0.03125F, 0, 0, 1, lightCoords);
/*     */               vertex(pose, buffer, x1, y1, backU0, backV1, 0.03125F, 0, 0, 1, lightCoords);
/*     */               vertex(pose, buffer, x0, y1, backU1, backV1, 0.03125F, 0, 0, 1, lightCoords);
/*     */               if (segmentY == height - 1) {
/*     */                 vertex(pose, buffer, x0, y0, topBottomU0, topBottomV0, -0.03125F, 0, 1, 0, lightCoords);
/*     */                 vertex(pose, buffer, x1, y0, topBottomU1, topBottomV0, -0.03125F, 0, 1, 0, lightCoords);
/*     */                 vertex(pose, buffer, x1, y0, topBottomU1, topBottomV1, 0.03125F, 0, 1, 0, lightCoords);
/*     */                 vertex(pose, buffer, x0, y0, topBottomU0, topBottomV1, 0.03125F, 0, 1, 0, lightCoords);
/*     */               } 
/*     */               if (segmentY == 0) {
/*     */                 vertex(pose, buffer, x0, y1, topBottomU0, topBottomV0, 0.03125F, 0, -1, 0, lightCoords);
/*     */                 vertex(pose, buffer, x1, y1, topBottomU1, topBottomV0, 0.03125F, 0, -1, 0, lightCoords);
/*     */                 vertex(pose, buffer, x1, y1, topBottomU1, topBottomV1, -0.03125F, 0, -1, 0, lightCoords);
/*     */                 vertex(pose, buffer, x0, y1, topBottomU0, topBottomV1, -0.03125F, 0, -1, 0, lightCoords);
/*     */               } 
/*     */               if (segmentX == width - 1) {
/*     */                 vertex(pose, buffer, x0, y0, leftRightU1, leftRightV0, 0.03125F, -1, 0, 0, lightCoords);
/*     */                 vertex(pose, buffer, x0, y1, leftRightU1, leftRightV1, 0.03125F, -1, 0, 0, lightCoords);
/*     */                 vertex(pose, buffer, x0, y1, leftRightU0, leftRightV1, -0.03125F, -1, 0, 0, lightCoords);
/*     */                 vertex(pose, buffer, x0, y0, leftRightU0, leftRightV0, -0.03125F, -1, 0, 0, lightCoords);
/*     */               } 
/*     */               if (segmentX == 0) {
/*     */                 vertex(pose, buffer, x1, y0, leftRightU1, leftRightV0, -0.03125F, 1, 0, 0, lightCoords);
/*     */                 vertex(pose, buffer, x1, y1, leftRightU1, leftRightV1, -0.03125F, 1, 0, 0, lightCoords);
/*     */                 vertex(pose, buffer, x1, y1, leftRightU0, leftRightV1, 0.03125F, 1, 0, 0, lightCoords);
/*     */                 vertex(pose, buffer, x1, y0, leftRightU0, leftRightV0, 0.03125F, 1, 0, 0, lightCoords);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float u, float v, float z, int nx, int ny, int nz, int lightCoords) {
/* 181 */     buffer.addVertex(pose, x, y, z).setColor(-1).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, nx, ny, nz);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\PaintingRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */