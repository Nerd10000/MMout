/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Transformation;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.ShulkerBoxRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteId;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.block.ShulkerBoxBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class ShulkerBoxRenderer implements BlockEntityRenderer<ShulkerBoxBlockEntity, ShulkerBoxRenderState> {
/*  37 */   private static final Map<Direction, Transformation> TRANSFORMATIONS = Util.makeEnumMap(Direction.class, ShulkerBoxRenderer::createModelTransform);
/*     */   
/*     */   private final SpriteGetter sprites;
/*     */   
/*     */   private final ShulkerBoxModel model;
/*     */   
/*     */   public ShulkerBoxRenderer(BlockEntityRendererProvider.Context context) {
/*  43 */     this(context.entityModelSet(), context.sprites());
/*     */   }
/*     */   
/*     */   public ShulkerBoxRenderer(SpecialModelRenderer.BakingContext context) {
/*  47 */     this(context.entityModelSet(), context.sprites());
/*     */   }
/*     */   
/*     */   public ShulkerBoxRenderer(EntityModelSet context, SpriteGetter sprites) {
/*  51 */     this.sprites = sprites;
/*  52 */     this.model = new ShulkerBoxModel(context.bakeLayer(ModelLayers.SHULKER_BOX));
/*     */   }
/*     */   
/*     */   public ShulkerBoxRenderState createRenderState() {
/*  57 */     return new ShulkerBoxRenderState();
/*     */   }
/*     */   
/*     */   public void extractRenderState(ShulkerBoxBlockEntity blockEntity, ShulkerBoxRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/*  62 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/*  63 */     state.direction = (Direction)blockEntity.getBlockState().getValueOrElse((Property)ShulkerBoxBlock.FACING, (Comparable)Direction.UP);
/*  64 */     state.color = blockEntity.getColor();
/*  65 */     state.progress = blockEntity.getProgress(partialTicks);
/*     */   }
/*     */   
/*     */   public void submit(ShulkerBoxRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*     */     SpriteId sprite;
/*  70 */     DyeColor color = state.color;
/*  72 */     if (color == null) {
/*  73 */       sprite = Sheets.DEFAULT_SHULKER_TEXTURE_LOCATION;
/*     */     } else {
/*  75 */       sprite = Sheets.getShulkerBoxSprite(color);
/*     */     } 
/*  77 */     submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.direction, state.progress, state.breakProgress, sprite, 0);
/*     */   }
/*     */   
/*     */   private void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, Direction direction, float progress, ModelFeatureRenderer.CrumblingOverlay breakProgress, SpriteId sprite, int outlineColor) {
/*  81 */     poseStack.pushPose();
/*  82 */     poseStack.mulPose(modelTransform(direction));
/*  83 */     submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, progress, breakProgress, sprite, outlineColor);
/*  84 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, float progress, ModelFeatureRenderer.CrumblingOverlay breakProgress, SpriteId sprite, int outlineColor) {
/*  88 */     this.model.setupAnim(Float.valueOf(progress));
/*  89 */     submitNodeCollector.submitModel(this.model, Float.valueOf(progress), poseStack, lightCoords, overlayCoords, -1, sprite, this.sprites, outlineColor, breakProgress);
/*     */   }
/*     */   
/*     */   private static Transformation createModelTransform(Direction direction) {
/*  93 */     float scale = 0.9995F;
/*  94 */     return new Transformation((Matrix4fc)(new Matrix4f())
/*  95 */         .translation(0.5F, 0.5F, 0.5F)
/*  96 */         .scale(0.9995F, 0.9995F, 0.9995F)
/*  97 */         .rotate((Quaternionfc)direction.getRotation())
/*  98 */         .scale(1.0F, -1.0F, -1.0F)
/*  99 */         .translate(0.0F, -1.0F, 0.0F));
/*     */   }
/*     */   
/*     */   public static Transformation modelTransform(Direction direction) {
/* 104 */     return TRANSFORMATIONS.get(direction);
/*     */   }
/*     */   
/*     */   public void getExtents(float progress, Consumer<Vector3fc> output) {
/* 108 */     PoseStack poseStack = new PoseStack();
/* 109 */     this.model.setupAnim(Float.valueOf(progress));
/* 110 */     this.model.root().getExtentsForGui(poseStack, output);
/*     */   }
/*     */   
/*     */   private static class ShulkerBoxModel extends Model<Float> {
/*     */     private final ModelPart lid;
/*     */     
/*     */     public ShulkerBoxModel(ModelPart root) {
/* 117 */       super(root, RenderTypes::entityCutout);
/* 118 */       this.lid = root.getChild("lid");
/*     */     }
/*     */     
/*     */     public void setupAnim(Float progress) {
/* 123 */       super.setupAnim(progress);
/* 124 */       this.lid.setPos(0.0F, 24.0F - progress.floatValue() * 0.5F * 16.0F, 0.0F);
/* 125 */       this.lid.yRot = 270.0F * progress.floatValue() * 0.017453292F;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\ShulkerBoxRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */