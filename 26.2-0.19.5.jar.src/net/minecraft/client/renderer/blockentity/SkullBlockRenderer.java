/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import com.mojang.math.Transformation;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.object.skull.PiglinHeadModel;
/*     */ import net.minecraft.client.model.object.skull.SkullModelBase;
/*     */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.SkullBlockRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.resources.DefaultPlayerSkin;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ import net.minecraft.world.level.block.AbstractSkullBlock;
/*     */ import net.minecraft.world.level.block.SkullBlock;
/*     */ import net.minecraft.world.level.block.WallSkullBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RotationSegment;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class SkullBlockRenderer implements BlockEntityRenderer<SkullBlockEntity, SkullBlockRenderState> {
/*  42 */   public static final WallAndGroundTransformations<Transformation> TRANSFORMATIONS = new WallAndGroundTransformations<>(SkullBlockRenderer::createWallTransformation, SkullBlockRenderer::createGroundTransformation, 16);
/*     */   
/*     */   private final Function<SkullBlock.Type, SkullModelBase> modelByType;
/*     */   
/*     */   private static final Map<SkullBlock.Type, Identifier> SKIN_BY_TYPE;
/*     */   
/*     */   private final PlayerSkinRenderCache playerSkinRenderCache;
/*     */   
/*     */   static {
/*  50 */     SKIN_BY_TYPE = (Map<SkullBlock.Type, Identifier>)Util.make(Maps.newHashMap(), map -> {
/*     */           map.put(SkullBlock.Types.SKELETON, Identifier.withDefaultNamespace("textures/entity/skeleton/skeleton.png"));
/*     */           map.put(SkullBlock.Types.WITHER_SKELETON, Identifier.withDefaultNamespace("textures/entity/skeleton/wither_skeleton.png"));
/*     */           map.put(SkullBlock.Types.ZOMBIE, Identifier.withDefaultNamespace("textures/entity/zombie/zombie.png"));
/*     */           map.put(SkullBlock.Types.CREEPER, Identifier.withDefaultNamespace("textures/entity/creeper/creeper.png"));
/*     */           map.put(SkullBlock.Types.DRAGON, Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon.png"));
/*     */           map.put(SkullBlock.Types.PIGLIN, Identifier.withDefaultNamespace("textures/entity/piglin/piglin.png"));
/*     */           map.put(SkullBlock.Types.PLAYER, DefaultPlayerSkin.getDefaultTexture());
/*     */         });
/*     */   }
/*     */   
/*     */   public static SkullModelBase createModel(EntityModelSet modelSet, SkullBlock.Type type) {
/*  61 */     if (type instanceof SkullBlock.Types) {
/*  61 */       SkullBlock.Types vanillaType = (SkullBlock.Types)type;
/*  62 */       switch (vanillaType) {
/*     */         default:
/*  62 */           throw new MatchException(null, null);
/*     */         case SKELETON:
/*     */         
/*     */         case WITHER_SKELETON:
/*     */         
/*     */         case PLAYER:
/*     */         
/*     */         case ZOMBIE:
/*     */         
/*     */         case CREEPER:
/*     */         
/*     */         case DRAGON:
/*     */         
/*     */         case PIGLIN:
/*     */           break;
/*     */       } 
/*  62 */       return 
/*     */         
/*  69 */         (SkullModelBase)new PiglinHeadModel(modelSet.bakeLayer(ModelLayers.PIGLIN_HEAD));
/*     */     } 
/*  73 */     return null;
/*     */   }
/*     */   
/*     */   public SkullBlockRenderer(BlockEntityRendererProvider.Context context) {
/*  79 */     EntityModelSet modelSet = context.entityModelSet();
/*  80 */     this.playerSkinRenderCache = context.playerSkinRenderCache();
/*  81 */     this.modelByType = Util.memoize(type -> createModel(modelSet, type));
/*     */   }
/*     */   
/*     */   public SkullBlockRenderState createRenderState() {
/*  86 */     return new SkullBlockRenderState();
/*     */   }
/*     */   
/*     */   public void extractRenderState(SkullBlockEntity blockEntity, SkullBlockRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/*  91 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/*  92 */     state.animationProgress = blockEntity.getAnimation(partialTicks);
/*  93 */     BlockState blockState = blockEntity.getBlockState();
/*  94 */     if (blockState.getBlock() instanceof WallSkullBlock) {
/*  95 */       Direction facing = (Direction)blockState.getValue((Property)WallSkullBlock.FACING);
/*  96 */       state.transformation = TRANSFORMATIONS.wallTransformation(facing);
/*     */     } else {
/*  98 */       state.transformation = TRANSFORMATIONS.freeTransformations(((Integer)blockState.getValue((Property)SkullBlock.ROTATION)).intValue());
/*     */     } 
/* 100 */     state.skullType = ((AbstractSkullBlock)blockState.getBlock()).getType();
/* 101 */     state.renderType = resolveSkullRenderType(state.skullType, blockEntity);
/*     */   }
/*     */   
/*     */   public void submit(SkullBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 106 */     SkullModelBase model = this.modelByType.apply(state.skullType);
/* 108 */     poseStack.pushPose();
/* 109 */     poseStack.mulPose(state.transformation);
/* 110 */     submitSkull(state.animationProgress, poseStack, submitNodeCollector, state.lightCoords, model, state.renderType, 0, state.breakProgress);
/* 111 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public static void submitSkull(float animationValue, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SkullModelBase model, RenderType renderType, int outlineColor, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 115 */     SkullModelBase.State modelState = new SkullModelBase.State();
/* 116 */     modelState.animationPos = animationValue;
/* 117 */     submitNodeCollector.submitModel((Model)model, modelState, poseStack, renderType, lightCoords, OverlayTexture.NO_OVERLAY, outlineColor, breakProgress);
/*     */   }
/*     */   
/*     */   private RenderType resolveSkullRenderType(SkullBlock.Type type, SkullBlockEntity entity) {
/* 121 */     if (type == SkullBlock.Types.PLAYER) {
/* 122 */       ResolvableProfile ownerProfile = entity.getOwnerProfile();
/* 123 */       if (ownerProfile != null)
/* 124 */         return this.playerSkinRenderCache.getOrDefault(ownerProfile).renderType(); 
/*     */     } 
/* 128 */     return getSkullRenderType(type, null);
/*     */   }
/*     */   
/*     */   public static RenderType getSkullRenderType(SkullBlock.Type type, Identifier texture) {
/* 132 */     return RenderTypes.entityCutoutZOffset((texture != null) ? texture : SKIN_BY_TYPE.get(type));
/*     */   }
/*     */   
/*     */   public static RenderType getPlayerSkinRenderType(Identifier texture) {
/* 136 */     return RenderTypes.entityTranslucent(texture);
/*     */   }
/*     */   
/*     */   private static Transformation createWallTransformation(Direction wallDirection) {
/* 140 */     float offset = 0.25F;
/* 141 */     return new Transformation((Vector3fc)new Vector3f(0.5F - wallDirection
/* 142 */           .getStepX() * 0.25F, 0.25F, 0.5F - wallDirection.getStepZ() * 0.25F), (Quaternionfc)Axis.YP
/* 143 */         .rotationDegrees(-wallDirection.getOpposite().toYRot()), (Vector3fc)new Vector3f(-1.0F, -1.0F, 1.0F), null);
/*     */   }
/*     */   
/*     */   private static Transformation createGroundTransformation(int segment) {
/* 150 */     return new Transformation((Matrix4fc)(new Matrix4f())
/* 151 */         .translation(0.5F, 0.0F, 0.5F)
/* 152 */         .rotate((Quaternionfc)Axis.YP.rotationDegrees(-RotationSegment.convertToDegrees(segment)))
/* 153 */         .scale(-1.0F, -1.0F, 1.0F));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\SkullBlockRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */