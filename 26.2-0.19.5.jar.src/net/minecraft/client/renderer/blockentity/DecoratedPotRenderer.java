/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import com.mojang.math.Transformation;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.DecoratedPotRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteId;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotPattern;
/*     */ import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
/*     */ import net.minecraft.world.level.block.entity.PotDecorations;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class DecoratedPotRenderer implements BlockEntityRenderer<DecoratedPotBlockEntity, DecoratedPotRenderState> {
/*  52 */   private static final Map<Direction, Transformation> TRANSFORMATIONS = Util.makeEnumMap(Direction.class, DecoratedPotRenderer::createModelTransformation);
/*     */   
/*     */   private static final Map<ResourceKey<Item>, SpriteId> DECORATED_POT_SPRITES;
/*     */   
/*     */   private final SpriteGetter sprites;
/*     */   
/*     */   private static final String NECK = "neck";
/*     */   
/*     */   private static final String FRONT = "front";
/*     */   
/*     */   private static final String BACK = "back";
/*     */   
/*     */   private static final String LEFT = "left";
/*     */   
/*     */   private static final String RIGHT = "right";
/*     */   
/*     */   private static final String TOP = "top";
/*     */   
/*     */   private static final String BOTTOM = "bottom";
/*     */   
/*     */   private final ModelPart neck;
/*     */   
/*     */   private final ModelPart frontSide;
/*     */   
/*     */   private final ModelPart backSide;
/*     */   
/*     */   private final ModelPart leftSide;
/*     */   
/*     */   private final ModelPart rightSide;
/*     */   
/*     */   private final ModelPart top;
/*     */   
/*     */   private final ModelPart bottom;
/*     */   
/*     */   private static final float WOBBLE_AMPLITUDE = 0.125F;
/*     */   
/*     */   static {
/*  54 */     DECORATED_POT_SPRITES = (Map<ResourceKey<Item>, SpriteId>)Util.make(() -> {
/*     */           ImmutableMap.Builder<ResourceKey<Item>, SpriteId> builder = ImmutableMap.builder();
/*     */           DecoratedPotPatterns.itemToPatternMappings(());
/*     */           return builder.buildOrThrow();
/*     */         });
/*     */   }
/*     */   
/*     */   public DecoratedPotRenderer(BlockEntityRendererProvider.Context context) {
/*  83 */     this(context.entityModelSet(), context.sprites());
/*     */   }
/*     */   
/*     */   public DecoratedPotRenderer(SpecialModelRenderer.BakingContext context) {
/*  87 */     this(context.entityModelSet(), context.sprites());
/*     */   }
/*     */   
/*     */   public DecoratedPotRenderer(EntityModelSet entityModelSet, SpriteGetter sprites) {
/*  91 */     this.sprites = sprites;
/*  92 */     ModelPart baseRoot = entityModelSet.bakeLayer(ModelLayers.DECORATED_POT_BASE);
/*  93 */     this.neck = baseRoot.getChild("neck");
/*  94 */     this.top = baseRoot.getChild("top");
/*  95 */     this.bottom = baseRoot.getChild("bottom");
/*  97 */     ModelPart sidesRoot = entityModelSet.bakeLayer(ModelLayers.DECORATED_POT_SIDES);
/*  98 */     this.frontSide = sidesRoot.getChild("front");
/*  99 */     this.backSide = sidesRoot.getChild("back");
/* 100 */     this.leftSide = sidesRoot.getChild("left");
/* 101 */     this.rightSide = sidesRoot.getChild("right");
/*     */   }
/*     */   
/*     */   public static LayerDefinition createBaseLayer() {
/* 105 */     MeshDefinition mesh = new MeshDefinition();
/* 106 */     PartDefinition root = mesh.getRoot();
/* 108 */     CubeDeformation inflate = new CubeDeformation(0.2F);
/* 109 */     CubeDeformation deflate = new CubeDeformation(-0.1F);
/* 111 */     root.addOrReplaceChild("neck", 
/* 112 */         CubeListBuilder.create()
/* 113 */         .texOffs(0, 0).addBox(4.0F, 17.0F, 4.0F, 8.0F, 3.0F, 8.0F, deflate)
/* 114 */         .texOffs(0, 5).addBox(5.0F, 20.0F, 5.0F, 6.0F, 1.0F, 6.0F, inflate), 
/* 115 */         PartPose.offsetAndRotation(0.0F, 37.0F, 16.0F, 3.1415927F, 0.0F, 0.0F));
/* 118 */     CubeListBuilder topBottomPlane = CubeListBuilder.create().texOffs(-14, 13).addBox(0.0F, 0.0F, 0.0F, 14.0F, 0.0F, 14.0F);
/* 119 */     root.addOrReplaceChild("top", topBottomPlane, PartPose.offsetAndRotation(1.0F, 16.0F, 1.0F, 0.0F, 0.0F, 0.0F));
/* 120 */     root.addOrReplaceChild("bottom", topBottomPlane, PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));
/* 122 */     return LayerDefinition.create(mesh, 32, 32);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createSidesLayer() {
/* 126 */     MeshDefinition mesh = new MeshDefinition();
/* 127 */     PartDefinition root = mesh.getRoot();
/* 129 */     CubeListBuilder sidePlane = CubeListBuilder.create().texOffs(1, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 16.0F, 0.0F, EnumSet.of(Direction.NORTH));
/* 131 */     root.addOrReplaceChild("back", sidePlane, PartPose.offsetAndRotation(15.0F, 16.0F, 1.0F, 0.0F, 0.0F, 3.1415927F));
/* 132 */     root.addOrReplaceChild("left", sidePlane, PartPose.offsetAndRotation(1.0F, 16.0F, 1.0F, 0.0F, -1.5707964F, 3.1415927F));
/* 133 */     root.addOrReplaceChild("right", sidePlane, PartPose.offsetAndRotation(15.0F, 16.0F, 15.0F, 0.0F, 1.5707964F, 3.1415927F));
/* 134 */     root.addOrReplaceChild("front", sidePlane, PartPose.offsetAndRotation(1.0F, 16.0F, 15.0F, 3.1415927F, 0.0F, 0.0F));
/* 136 */     return LayerDefinition.create(mesh, 16, 16);
/*     */   }
/*     */   
/*     */   private static SpriteId getSideSprite(Optional<Item> item) {
/* 140 */     if (item.isPresent()) {
/* 141 */       SpriteId result = DECORATED_POT_SPRITES.get(((Item)item.get()).builtInRegistryHolder().key());
/* 142 */       if (result != null)
/* 143 */         return result; 
/*     */     } 
/* 146 */     return Sheets.DECORATED_POT_SIDE;
/*     */   }
/*     */   
/*     */   public DecoratedPotRenderState createRenderState() {
/* 153 */     return new DecoratedPotRenderState();
/*     */   }
/*     */   
/*     */   public void extractRenderState(DecoratedPotBlockEntity blockEntity, DecoratedPotRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 158 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 159 */     state.decorations = blockEntity.getDecorations();
/* 160 */     state.direction = blockEntity.getDirection();
/* 162 */     DecoratedPotBlockEntity.WobbleStyle wobbleStyle = blockEntity.lastWobbleStyle;
/* 163 */     if (wobbleStyle != null && blockEntity.getLevel() != null) {
/* 164 */       state.wobbleProgress = ((float)(blockEntity.getLevel().getGameTime() - blockEntity.wobbleStartedAtTick) + partialTicks) / wobbleStyle.duration;
/*     */     } else {
/* 166 */       state.wobbleProgress = 0.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void submit(DecoratedPotRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 172 */     poseStack.pushPose();
/* 173 */     poseStack.mulPose(modelTransformation(state.direction));
/* 175 */     if (state.wobbleProgress >= 0.0F && state.wobbleProgress <= 1.0F)
/* 178 */       if (state.wobbleStyle == DecoratedPotBlockEntity.WobbleStyle.POSITIVE) {
/* 181 */         float amplitude = 0.015625F;
/* 182 */         float deltaTime = state.wobbleProgress * 6.2831855F;
/* 184 */         float tiltX = -1.5F * (Mth.cos(deltaTime) + 0.5F) * Mth.sin((deltaTime / 2.0F));
/* 185 */         poseStack.rotateAround((Quaternionfc)Axis.XP.rotation(tiltX * 0.015625F), 0.5F, 0.0F, 0.5F);
/* 187 */         float tiltZ = Mth.sin(deltaTime);
/* 188 */         poseStack.rotateAround((Quaternionfc)Axis.ZP.rotation(tiltZ * 0.015625F), 0.5F, 0.0F, 0.5F);
/*     */       } else {
/* 192 */         float turnAngle = Mth.sin((-state.wobbleProgress * 3.0F * 3.1415927F)) * 0.125F;
/* 193 */         float linearDecayFactor = 1.0F - state.wobbleProgress;
/* 194 */         poseStack.rotateAround((Quaternionfc)Axis.YP.rotation(turnAngle * linearDecayFactor), 0.5F, 0.0F, 0.5F);
/*     */       }  
/* 198 */     submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.decorations, 0);
/* 199 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public static Transformation modelTransformation(Direction facing) {
/* 203 */     return TRANSFORMATIONS.get(facing);
/*     */   }
/*     */   
/*     */   private static Transformation createModelTransformation(Direction entityDirection) {
/* 207 */     return new Transformation((Matrix4fc)(new Matrix4f()).rotateAround((Quaternionfc)Axis.YP.rotationDegrees(180.0F - entityDirection.toYRot()), 0.5F, 0.5F, 0.5F));
/*     */   }
/*     */   
/*     */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, PotDecorations decorations, int outlineColor) {
/* 211 */     RenderType renderType = Sheets.DECORATED_POT_BASE.renderType(RenderTypes::entitySolid);
/* 212 */     TextureAtlasSprite sprite = this.sprites.get(Sheets.DECORATED_POT_BASE);
/* 214 */     submitNodeCollector.submitModelPart(this.neck, poseStack, renderType, lightCoords, overlayCoords, sprite, -1, null, outlineColor);
/* 215 */     submitNodeCollector.submitModelPart(this.top, poseStack, renderType, lightCoords, overlayCoords, sprite, -1, null, outlineColor);
/* 216 */     submitNodeCollector.submitModelPart(this.bottom, poseStack, renderType, lightCoords, overlayCoords, sprite, -1, null, outlineColor);
/* 218 */     SpriteId frontSprite = getSideSprite(decorations.front());
/* 219 */     submitNodeCollector.submitModelPart(this.frontSide, poseStack, frontSprite.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.sprites.get(frontSprite), -1, null, outlineColor);
/* 220 */     SpriteId backSprite = getSideSprite(decorations.back());
/* 221 */     submitNodeCollector.submitModelPart(this.backSide, poseStack, backSprite.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.sprites.get(backSprite), -1, null, outlineColor);
/* 222 */     SpriteId leftSprite = getSideSprite(decorations.left());
/* 223 */     submitNodeCollector.submitModelPart(this.leftSide, poseStack, leftSprite.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.sprites.get(leftSprite), -1, null, outlineColor);
/* 224 */     SpriteId rightSprite = getSideSprite(decorations.right());
/* 225 */     submitNodeCollector.submitModelPart(this.rightSide, poseStack, rightSprite.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, this.sprites.get(rightSprite), -1, null, outlineColor);
/*     */   }
/*     */   
/*     */   public void getExtents(Consumer<Vector3fc> output) {
/* 229 */     PoseStack poseStack = new PoseStack();
/* 230 */     this.neck.getExtentsForGui(poseStack, output);
/* 231 */     this.top.getExtentsForGui(poseStack, output);
/* 232 */     this.bottom.getExtentsForGui(poseStack, output);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\DecoratedPotRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */