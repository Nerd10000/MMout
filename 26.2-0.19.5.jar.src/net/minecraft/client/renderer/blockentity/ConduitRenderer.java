/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Transformation;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeDeformation;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.SpriteMapper;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.ConduitRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteId;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ConduitBlockEntity;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class ConduitRenderer implements BlockEntityRenderer<ConduitBlockEntity, ConduitRenderState> {
/*  34 */   public static final Transformation DEFAULT_TRANSFORMATION = new Transformation((Vector3fc)new Vector3f(0.5F, 0.5F, 0.5F), null, null, null);
/*     */   
/*  36 */   public static final SpriteMapper MAPPER = new SpriteMapper(TextureAtlas.LOCATION_BLOCKS, "entity/conduit");
/*     */   
/*  38 */   public static final SpriteId SHELL_TEXTURE = MAPPER.defaultNamespaceApply("base");
/*     */   
/*  39 */   public static final SpriteId ACTIVE_SHELL_TEXTURE = MAPPER.defaultNamespaceApply("cage");
/*     */   
/*  40 */   public static final SpriteId WIND_TEXTURE = MAPPER.defaultNamespaceApply("wind");
/*     */   
/*  41 */   public static final SpriteId VERTICAL_WIND_TEXTURE = MAPPER.defaultNamespaceApply("wind_vertical");
/*     */   
/*  42 */   public static final SpriteId OPEN_EYE_TEXTURE = MAPPER.defaultNamespaceApply("open_eye");
/*     */   
/*  43 */   public static final SpriteId CLOSED_EYE_TEXTURE = MAPPER.defaultNamespaceApply("closed_eye");
/*     */   
/*     */   private final SpriteGetter sprites;
/*     */   
/*     */   private final ModelPart eye;
/*     */   
/*     */   private final ModelPart wind;
/*     */   
/*     */   private final ModelPart shell;
/*     */   
/*     */   private final ModelPart cage;
/*     */   
/*     */   public ConduitRenderer(BlockEntityRendererProvider.Context context) {
/*  53 */     this.sprites = context.sprites();
/*  54 */     this.eye = context.bakeLayer(ModelLayers.CONDUIT_EYE);
/*  55 */     this.wind = context.bakeLayer(ModelLayers.CONDUIT_WIND);
/*  56 */     this.shell = context.bakeLayer(ModelLayers.CONDUIT_SHELL);
/*  57 */     this.cage = context.bakeLayer(ModelLayers.CONDUIT_CAGE);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createEyeLayer() {
/*  61 */     MeshDefinition mesh = new MeshDefinition();
/*  62 */     PartDefinition root = mesh.getRoot();
/*  63 */     root.addOrReplaceChild("eye", 
/*  64 */         CubeListBuilder.create()
/*  65 */         .texOffs(0, 0).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.ZERO);
/*  68 */     return LayerDefinition.create(mesh, 16, 16);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createWindLayer() {
/*  72 */     MeshDefinition mesh = new MeshDefinition();
/*  73 */     PartDefinition root = mesh.getRoot();
/*  74 */     root.addOrReplaceChild("wind", 
/*  75 */         CubeListBuilder.create()
/*  76 */         .texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.ZERO);
/*  79 */     return LayerDefinition.create(mesh, 64, 32);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createShellLayer() {
/*  83 */     MeshDefinition mesh = new MeshDefinition();
/*  84 */     PartDefinition root = mesh.getRoot();
/*  85 */     root.addOrReplaceChild("shell", 
/*  86 */         CubeListBuilder.create()
/*  87 */         .texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.ZERO);
/*  90 */     return LayerDefinition.create(mesh, 32, 16);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createCageLayer() {
/*  94 */     MeshDefinition mesh = new MeshDefinition();
/*  95 */     PartDefinition root = mesh.getRoot();
/*  96 */     root.addOrReplaceChild("shell", 
/*  97 */         CubeListBuilder.create()
/*  98 */         .texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
/* 101 */     return LayerDefinition.create(mesh, 32, 16);
/*     */   }
/*     */   
/*     */   public ConduitRenderState createRenderState() {
/* 106 */     return new ConduitRenderState();
/*     */   }
/*     */   
/*     */   public void extractRenderState(ConduitBlockEntity blockEntity, ConduitRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 111 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 112 */     state.isActive = blockEntity.isActive();
/* 113 */     state.activeRotation = blockEntity.getActiveRotation(blockEntity.isActive() ? partialTicks : 0.0F);
/* 114 */     state.animTime = blockEntity.tickCount + partialTicks;
/* 115 */     state.animationPhase = blockEntity.tickCount / 66 % 3;
/* 116 */     state.isHunting = blockEntity.isHunting();
/*     */   }
/*     */   
/*     */   public void submit(ConduitRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 121 */     if (!state.isActive) {
/* 122 */       poseStack.pushPose();
/* 123 */       poseStack.translate(0.5F, 0.5F, 0.5F);
/* 124 */       poseStack.mulPose((Quaternionfc)(new Quaternionf()).rotationY(state.activeRotation * 0.017453292F));
/* 125 */       submitNodeCollector.submitModelPart(this.shell, poseStack, SHELL_TEXTURE.renderType(RenderTypes::entitySolid), state.lightCoords, OverlayTexture.NO_OVERLAY, this.sprites.get(SHELL_TEXTURE), -1, state.breakProgress);
/* 126 */       poseStack.popPose();
/*     */       return;
/*     */     } 
/* 130 */     float rotation = state.activeRotation * 57.295776F;
/* 131 */     float hh = Mth.sin((state.animTime * 0.1F)) / 2.0F + 0.5F;
/* 132 */     hh = hh * hh + hh;
/* 134 */     poseStack.pushPose();
/* 135 */     poseStack.translate(0.5F, 0.3F + hh * 0.2F, 0.5F);
/* 136 */     Vector3f axis = (new Vector3f(0.5F, 1.0F, 0.5F)).normalize();
/* 137 */     poseStack.mulPose((Quaternionfc)(new Quaternionf()).rotationAxis(rotation * 0.017453292F, (Vector3fc)axis));
/* 138 */     submitNodeCollector.submitModelPart(this.cage, poseStack, ACTIVE_SHELL_TEXTURE.renderType(RenderTypes::entityCutout), state.lightCoords, OverlayTexture.NO_OVERLAY, this.sprites.get(ACTIVE_SHELL_TEXTURE), -1, state.breakProgress);
/* 139 */     poseStack.popPose();
/* 141 */     poseStack.pushPose();
/* 142 */     poseStack.translate(0.5F, 0.5F, 0.5F);
/* 144 */     if (state.animationPhase == 1) {
/* 145 */       poseStack.mulPose((Quaternionfc)(new Quaternionf()).rotationX(1.5707964F));
/* 146 */     } else if (state.animationPhase == 2) {
/* 147 */       poseStack.mulPose((Quaternionfc)(new Quaternionf()).rotationZ(1.5707964F));
/*     */     } 
/* 152 */     SpriteId windSpriteId = (state.animationPhase == 1) ? VERTICAL_WIND_TEXTURE : WIND_TEXTURE;
/* 153 */     RenderType windRenderType = windSpriteId.renderType(RenderTypes::entityCutout);
/* 154 */     TextureAtlasSprite windSprite = this.sprites.get(windSpriteId);
/* 155 */     submitNodeCollector.submitModelPart(this.wind, poseStack, windRenderType, state.lightCoords, OverlayTexture.NO_OVERLAY, windSprite);
/* 156 */     poseStack.popPose();
/* 157 */     poseStack.pushPose();
/* 158 */     poseStack.translate(0.5F, 0.5F, 0.5F);
/* 159 */     poseStack.scale(0.875F, 0.875F, 0.875F);
/* 160 */     poseStack.mulPose((Quaternionfc)(new Quaternionf()).rotationXYZ(3.1415927F, 0.0F, 3.1415927F));
/* 161 */     submitNodeCollector.submitModelPart(this.wind, poseStack, windRenderType, state.lightCoords, OverlayTexture.NO_OVERLAY, windSprite);
/* 162 */     poseStack.popPose();
/* 164 */     poseStack.pushPose();
/* 165 */     poseStack.translate(0.5F, 0.3F + hh * 0.2F, 0.5F);
/* 166 */     poseStack.scale(0.5F, 0.5F, 0.5F);
/* 167 */     poseStack.mulPose((Quaternionfc)camera.orientation);
/* 168 */     poseStack.mulPose((Quaternionfc)(new Quaternionf()).rotationZ(3.1415927F).rotateY(3.1415927F));
/* 169 */     float scale = 1.3333334F;
/* 170 */     poseStack.scale(1.3333334F, 1.3333334F, 1.3333334F);
/* 171 */     SpriteId eyeSprite = state.isHunting ? OPEN_EYE_TEXTURE : CLOSED_EYE_TEXTURE;
/* 172 */     submitNodeCollector.submitModelPart(this.eye, poseStack, eyeSprite.renderType(RenderTypes::entityCutout), state.lightCoords, OverlayTexture.NO_OVERLAY, this.sprites.get(eyeSprite));
/* 173 */     poseStack.popPose();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\ConduitRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */