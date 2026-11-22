/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import com.mojang.math.Transformation;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.object.banner.BannerFlagModel;
/*     */ import net.minecraft.client.model.object.banner.BannerModel;
/*     */ import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BannerRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteId;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.block.BannerBlock;
/*     */ import net.minecraft.world.level.block.WallBannerBlock;
/*     */ import net.minecraft.world.level.block.entity.BannerBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class BannerRenderer implements BlockEntityRenderer<BannerBlockEntity, BannerRenderState> {
/*     */   private static final int MAX_PATTERNS = 16;
/*     */   
/*     */   private static final float SIZE = 0.6666667F;
/*     */   
/*  44 */   private static final Vector3fc MODEL_SCALE = (Vector3fc)new Vector3f(0.6666667F, -0.6666667F, -0.6666667F);
/*     */   
/*  45 */   private static final Vector3fc MODEL_TRANSLATION = (Vector3fc)new Vector3f(0.5F, 0.0F, 0.5F);
/*     */   
/*  47 */   public static final WallAndGroundTransformations<Transformation> TRANSFORMATIONS = new WallAndGroundTransformations<>(BannerRenderer::createWallTransformation, BannerRenderer::createGroundTransformation, 16);
/*     */   
/*     */   private final SpriteGetter sprites;
/*     */   
/*     */   private final BannerModel standingModel;
/*     */   
/*     */   private final BannerModel wallModel;
/*     */   
/*     */   private final BannerFlagModel standingFlagModel;
/*     */   
/*     */   private final BannerFlagModel wallFlagModel;
/*     */   
/*     */   public BannerRenderer(BlockEntityRendererProvider.Context context) {
/*  61 */     this(context.entityModelSet(), context.sprites());
/*     */   }
/*     */   
/*     */   public BannerRenderer(SpecialModelRenderer.BakingContext context) {
/*  65 */     this(context.entityModelSet(), context.sprites());
/*     */   }
/*     */   
/*     */   public BannerRenderer(EntityModelSet modelSet, SpriteGetter sprites) {
/*  69 */     this.sprites = sprites;
/*  70 */     this.standingModel = new BannerModel(modelSet.bakeLayer(ModelLayers.STANDING_BANNER));
/*  71 */     this.wallModel = new BannerModel(modelSet.bakeLayer(ModelLayers.WALL_BANNER));
/*  72 */     this.standingFlagModel = new BannerFlagModel(modelSet.bakeLayer(ModelLayers.STANDING_BANNER_FLAG));
/*  73 */     this.wallFlagModel = new BannerFlagModel(modelSet.bakeLayer(ModelLayers.WALL_BANNER_FLAG));
/*     */   }
/*     */   
/*     */   public BannerRenderState createRenderState() {
/*  78 */     return new BannerRenderState();
/*     */   }
/*     */   
/*     */   public void extractRenderState(BannerBlockEntity blockEntity, BannerRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/*  83 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/*  84 */     state.baseColor = blockEntity.getBaseColor();
/*  85 */     state.patterns = blockEntity.getPatterns();
/*  87 */     BlockState blockState = blockEntity.getBlockState();
/*  88 */     if (blockState.getBlock() instanceof BannerBlock) {
/*  89 */       state.transformation = TRANSFORMATIONS.freeTransformations(((Integer)blockState.getValue((Property)BannerBlock.ROTATION)).intValue());
/*  90 */       state.attachmentType = BannerBlock.AttachmentType.GROUND;
/*     */     } else {
/*  92 */       state.transformation = TRANSFORMATIONS.wallTransformation((Direction)blockState.getValue((Property)WallBannerBlock.FACING));
/*  93 */       state.attachmentType = BannerBlock.AttachmentType.WALL;
/*     */     } 
/*  96 */     long gameTime = (blockEntity.getLevel() != null) ? blockEntity.getLevel().getGameTime() : 0L;
/*  97 */     BlockPos blockPos = blockEntity.getBlockPos();
/*  99 */     state.phase = ((float)Math.floorMod((blockPos.getX() * 7 + blockPos.getY() * 9 + blockPos.getZ() * 13) + gameTime, 100L) + partialTicks) / 100.0F;
/*     */   }
/*     */   
/*     */   private BannerModel bannerModel(BannerBlock.AttachmentType type) {
/* 103 */     switch (type) {
/*     */       default:
/* 103 */         throw new MatchException(null, null);
/*     */       case WALL:
/*     */       
/*     */       case GROUND:
/*     */         break;
/*     */     } 
/* 103 */     return 
/*     */       
/* 105 */       this.standingModel;
/*     */   }
/*     */   
/*     */   private BannerFlagModel flagModel(BannerBlock.AttachmentType type) {
/* 110 */     switch (type) {
/*     */       default:
/* 110 */         throw new MatchException(null, null);
/*     */       case WALL:
/*     */       
/*     */       case GROUND:
/*     */         break;
/*     */     } 
/* 110 */     return 
/*     */       
/* 112 */       this.standingFlagModel;
/*     */   }
/*     */   
/*     */   public void submit(BannerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 118 */     poseStack.pushPose();
/* 119 */     poseStack.mulPose(state.transformation);
/* 120 */     submitBanner(this.sprites, poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, bannerModel(state.attachmentType), flagModel(state.attachmentType), state.phase, state.baseColor, state.patterns, state.breakProgress, 0);
/* 121 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public void submitSpecial(BannerBlock.AttachmentType type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, DyeColor baseColor, BannerPatternLayers patterns, int outlineColor) {
/* 125 */     submitBanner(this.sprites, poseStack, submitNodeCollector, lightCoords, overlayCoords, bannerModel(type), flagModel(type), 0.0F, baseColor, patterns, null, outlineColor);
/*     */   }
/*     */   
/*     */   private static void submitBanner(SpriteGetter sprites, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, BannerModel model, BannerFlagModel flagModel, float phase, DyeColor baseColor, BannerPatternLayers patterns, ModelFeatureRenderer.CrumblingOverlay breakProgress, int outlineColor) {
/* 129 */     SpriteId sprite = Sheets.BANNER_BASE;
/* 130 */     submitNodeCollector.submitModel((Model)model, Unit.INSTANCE, poseStack, lightCoords, overlayCoords, -1, sprite, sprites, outlineColor, breakProgress);
/* 131 */     submitNodeCollector.submitModel((Model)flagModel, Float.valueOf(phase), poseStack, lightCoords, overlayCoords, -1, sprite, sprites, outlineColor, breakProgress);
/* 132 */     submitPatterns(sprites, poseStack, submitNodeCollector, lightCoords, overlayCoords, (Model<Float>)flagModel, Float.valueOf(phase), true, baseColor, patterns, breakProgress);
/*     */   }
/*     */   
/*     */   public static <S> void submitPatterns(SpriteGetter sprites, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, Model<S> model, S state, boolean banner, DyeColor baseColor, BannerPatternLayers patterns, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 136 */     submitPatternLayer(sprites, poseStack, (OrderedSubmitNodeCollector)submitNodeCollector, lightCoords, overlayCoords, model, state, banner ? Sheets.BANNER_PATTERN_BASE : Sheets.SHIELD_PATTERN_BASE, baseColor, breakProgress);
/* 138 */     for (int maskIndex = 0; maskIndex < 16 && maskIndex < patterns.layers().size(); maskIndex++) {
/* 139 */       BannerPatternLayers.Layer layer = patterns.layers().get(maskIndex);
/* 140 */       SpriteId sprite = banner ? Sheets.getBannerSprite(layer.pattern()) : Sheets.getShieldSprite(layer.pattern());
/* 141 */       submitPatternLayer(sprites, poseStack, submitNodeCollector.order(maskIndex + 1), lightCoords, overlayCoords, model, state, sprite, layer.color(), null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <S> void submitPatternLayer(SpriteGetter sprites, PoseStack poseStack, OrderedSubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, Model<S> model, S state, SpriteId sprite, DyeColor color, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 146 */     int diffuseColor = color.getTextureDiffuseColor();
/* 147 */     submitNodeCollector.submitModel(model, state, poseStack, sprite.renderType(RenderTypes::bannerPattern), lightCoords, overlayCoords, diffuseColor, sprites.get(sprite), 0, breakProgress);
/*     */   }
/*     */   
/*     */   public void getExtents(Consumer<Vector3fc> output) {
/* 151 */     PoseStack poseStack = new PoseStack();
/* 152 */     this.standingModel.root().getExtentsForGui(poseStack, output);
/* 153 */     this.standingFlagModel.setupAnim(Float.valueOf(0.0F));
/* 154 */     this.standingFlagModel.root().getExtentsForGui(poseStack, output);
/*     */   }
/*     */   
/*     */   private static Transformation modelTransformation(float angle) {
/* 158 */     return new Transformation(MODEL_TRANSLATION, (Quaternionfc)Axis.YP
/*     */         
/* 160 */         .rotationDegrees(-angle), MODEL_SCALE, null);
/*     */   }
/*     */   
/*     */   private static Transformation createGroundTransformation(int segment) {
/* 167 */     return modelTransformation(RotationSegment.convertToDegrees(segment));
/*     */   }
/*     */   
/*     */   private static Transformation createWallTransformation(Direction direction) {
/* 171 */     return modelTransformation(direction.toYRot());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\BannerRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */