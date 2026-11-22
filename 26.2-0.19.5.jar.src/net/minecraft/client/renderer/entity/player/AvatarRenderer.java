/*     */ package net.minecraft.client.renderer.entity.player;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.entity.ClientAvatarEntity;
/*     */ import net.minecraft.client.entity.ClientAvatarState;
/*     */ import net.minecraft.client.model.EntityModel;
/*     */ import net.minecraft.client.model.HumanoidModel;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.model.player.PlayerModel;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.entity.ArmorModelSet;
/*     */ import net.minecraft.client.renderer.entity.EntityRendererProvider;
/*     */ import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
/*     */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*     */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*     */ import net.minecraft.client.renderer.entity.layers.ArrowLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.BeeStingerLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.CapeLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.ParrotOnShoulderLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.WingsLayer;
/*     */ import net.minecraft.client.renderer.entity.state.AvatarRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.Avatar;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.PlayerModelPart;
/*     */ import net.minecraft.world.item.CrossbowItem;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUseAnimation;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.SwingAnimationType;
/*     */ import net.minecraft.world.item.component.SwingAnimation;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class AvatarRenderer<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {
/*     */   public AvatarRenderer(EntityRendererProvider.Context context, boolean slimSteve) {
/*  50 */     super(context, (EntityModel)new PlayerModel(context.bakeLayer(slimSteve ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slimSteve), 0.5F);
/*  52 */     addLayer((RenderLayer)new HumanoidArmorLayer((RenderLayerParent)this, 
/*  53 */           ArmorModelSet.bake(slimSteve ? ModelLayers.PLAYER_SLIM_ARMOR : ModelLayers.PLAYER_ARMOR, context.getModelSet(), part -> new PlayerModel(part, slimSteve)), context
/*  54 */           .getEquipmentRenderer()));
/*  56 */     addLayer((RenderLayer)new PlayerItemInHandLayer((RenderLayerParent)this));
/*  57 */     addLayer((RenderLayer)new ArrowLayer(this, context));
/*  58 */     addLayer((RenderLayer)new Deadmau5EarsLayer((RenderLayerParent)this, context.getModelSet()));
/*  59 */     addLayer((RenderLayer)new CapeLayer((RenderLayerParent)this, context.getModelSet(), context.getEquipmentAssets()));
/*  60 */     addLayer((RenderLayer)new CustomHeadLayer((RenderLayerParent)this, context.getModelSet(), context.getPlayerSkinRenderCache()));
/*  61 */     addLayer((RenderLayer)new WingsLayer((RenderLayerParent)this, context.getModelSet(), context.getEquipmentRenderer()));
/*  62 */     addLayer((RenderLayer)new ParrotOnShoulderLayer((RenderLayerParent)this, context.getModelSet()));
/*  63 */     addLayer((RenderLayer)new SpinAttackEffectLayer((RenderLayerParent)this, context.getModelSet()));
/*  64 */     addLayer((RenderLayer)new BeeStingerLayer(this, context));
/*     */   }
/*     */   
/*     */   protected boolean shouldRenderLayers(AvatarRenderState state) {
/*  69 */     return !state.isSpectator;
/*     */   }
/*     */   
/*     */   public Vec3 getRenderOffset(AvatarRenderState state) {
/*  74 */     Vec3 offset = super.getRenderOffset((EntityRenderState)state);
/*  75 */     if (state.isCrouching)
/*  76 */       return offset.add(0.0D, (state.scale * -2.0F) / 16.0D, 0.0D); 
/*  79 */     return offset;
/*     */   }
/*     */   
/*     */   private static HumanoidModel.ArmPose getArmPose(Avatar avatar, HumanoidArm arm) {
/*  83 */     ItemStack mainHandItem = avatar.getItemInHand(InteractionHand.MAIN_HAND);
/*  84 */     ItemStack offHandItem = avatar.getItemInHand(InteractionHand.OFF_HAND);
/*  86 */     HumanoidModel.ArmPose mainHandPose = getArmPose(avatar, mainHandItem, InteractionHand.MAIN_HAND);
/*  87 */     HumanoidModel.ArmPose offHandPose = getArmPose(avatar, offHandItem, InteractionHand.OFF_HAND);
/*  90 */     if (mainHandPose.isTwoHanded())
/*  91 */       offHandPose = offHandItem.isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM; 
/*  94 */     if (avatar.getMainArm() == arm)
/*  95 */       return mainHandPose; 
/*  97 */     return offHandPose;
/*     */   }
/*     */   
/*     */   private static HumanoidModel.ArmPose getArmPose(Avatar avatar, ItemStack itemInHand, InteractionHand hand) {
/* 103 */     if (itemInHand.isEmpty())
/* 104 */       return HumanoidModel.ArmPose.EMPTY; 
/* 107 */     if (!avatar.swinging && itemInHand.is(Items.CROSSBOW) && CrossbowItem.isCharged(itemInHand))
/* 108 */       return HumanoidModel.ArmPose.CROSSBOW_HOLD; 
/* 111 */     if (avatar.getUsedItemHand() == hand && avatar.getUseItemRemainingTicks() > 0) {
/* 112 */       ItemUseAnimation anim = itemInHand.getUseAnimation();
/* 113 */       if (anim == ItemUseAnimation.BLOCK)
/* 114 */         return HumanoidModel.ArmPose.BLOCK; 
/* 115 */       if (anim == ItemUseAnimation.BOW)
/* 116 */         return HumanoidModel.ArmPose.BOW_AND_ARROW; 
/* 117 */       if (anim == ItemUseAnimation.TRIDENT)
/* 118 */         return HumanoidModel.ArmPose.THROW_TRIDENT; 
/* 119 */       if (anim == ItemUseAnimation.CROSSBOW)
/* 120 */         return HumanoidModel.ArmPose.CROSSBOW_CHARGE; 
/* 121 */       if (anim == ItemUseAnimation.SPYGLASS)
/* 122 */         return HumanoidModel.ArmPose.SPYGLASS; 
/* 123 */       if (anim == ItemUseAnimation.TOOT_HORN)
/* 124 */         return HumanoidModel.ArmPose.TOOT_HORN; 
/* 125 */       if (anim == ItemUseAnimation.BRUSH)
/* 126 */         return HumanoidModel.ArmPose.BRUSH; 
/* 127 */       if (anim == ItemUseAnimation.SPEAR)
/* 128 */         return HumanoidModel.ArmPose.SPEAR; 
/*     */     } 
/* 131 */     SwingAnimation attack = (SwingAnimation)itemInHand.get(DataComponents.SWING_ANIMATION);
/* 132 */     if (attack != null && attack.type() == SwingAnimationType.STAB && avatar.swinging)
/* 133 */       return HumanoidModel.ArmPose.SPEAR; 
/* 135 */     if (itemInHand.is(ItemTags.SPEARS))
/* 136 */       return HumanoidModel.ArmPose.SPEAR; 
/* 138 */     return HumanoidModel.ArmPose.ITEM;
/*     */   }
/*     */   
/*     */   public Identifier getTextureLocation(AvatarRenderState state) {
/* 143 */     return state.skin.body().texturePath();
/*     */   }
/*     */   
/*     */   protected void scale(AvatarRenderState state, PoseStack poseStack) {
/* 148 */     float s = 0.9375F;
/* 149 */     poseStack.scale(0.9375F, 0.9375F, 0.9375F);
/*     */   }
/*     */   
/*     */   protected void submitNameDisplay(AvatarRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 154 */     poseStack.pushPose();
/* 155 */     int offset = state.showExtraEars ? -10 : 0;
/* 156 */     submitNameDisplay((EntityRenderState)state, poseStack, submitNodeCollector, camera, offset);
/* 157 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public AvatarRenderState createRenderState() {
/* 162 */     return new AvatarRenderState();
/*     */   }
/*     */   
/*     */   public void extractRenderState(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks) {
/* 167 */     super.extractRenderState((LivingEntity)entity, (LivingEntityRenderState)state, partialTicks);
/* 168 */     HumanoidMobRenderer.extractHumanoidRenderState((LivingEntity)entity, (HumanoidRenderState)state, partialTicks, this.itemModelResolver);
/* 169 */     state.leftArmPose = getArmPose((Avatar)entity, HumanoidArm.LEFT);
/* 170 */     state.rightArmPose = getArmPose((Avatar)entity, HumanoidArm.RIGHT);
/* 171 */     state.skin = ((ClientAvatarEntity)entity).getSkin();
/* 172 */     state.arrowCount = entity.getArrowCount();
/* 173 */     state.stingerCount = entity.getStingerCount();
/* 174 */     state.isSpectator = entity.isSpectator();
/* 175 */     state.showHat = entity.isModelPartShown(PlayerModelPart.HAT);
/* 176 */     state.showJacket = entity.isModelPartShown(PlayerModelPart.JACKET);
/* 177 */     state.showLeftPants = entity.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
/* 178 */     state.showRightPants = entity.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
/* 179 */     state.showLeftSleeve = entity.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
/* 180 */     state.showRightSleeve = entity.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
/* 181 */     state.showCape = entity.isModelPartShown(PlayerModelPart.CAPE);
/* 182 */     extractFlightData(entity, state, partialTicks);
/* 183 */     extractCapeState(entity, state, partialTicks);
/* 185 */     state.parrotOnLeftShoulder = ((ClientAvatarEntity)entity).getParrotVariantOnShoulder(true);
/* 186 */     state.parrotOnRightShoulder = ((ClientAvatarEntity)entity).getParrotVariantOnShoulder(false);
/* 187 */     state.id = entity.getId();
/* 188 */     state.showExtraEars = ((ClientAvatarEntity)entity).showExtraEars();
/* 190 */     state.heldOnHead.clear();
/* 191 */     if (state.isUsingItem) {
/* 192 */       ItemStack useItem = entity.getItemInHand(state.useItemHand);
/* 193 */       if (useItem.is(Items.SPYGLASS))
/* 194 */         this.itemModelResolver.updateForLiving(state.heldOnHead, useItem, ItemDisplayContext.HEAD, (LivingEntity)entity); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean shouldShowName(AvatarlikeEntity entity, double distanceToCameraSq) {
/* 201 */     return (super.shouldShowName((LivingEntity)entity, distanceToCameraSq) && (entity.shouldShowName() || (entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity)));
/*     */   }
/*     */   
/*     */   private void extractFlightData(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks) {
/* 205 */     state.fallFlyingTimeInTicks = entity.getFallFlyingTicks() + partialTicks;
/* 206 */     Vec3 lookAngle = entity.getViewVector(partialTicks);
/* 207 */     Vec3 movement = ((ClientAvatarEntity)entity).avatarState().deltaMovementOnPreviousTick().lerp(entity.getDeltaMovement(), partialTicks);
/* 208 */     if (movement.horizontalDistanceSqr() > 9.999999747378752E-6D && lookAngle.horizontalDistanceSqr() > 9.999999747378752E-6D) {
/* 209 */       state.shouldApplyFlyingYRot = true;
/* 210 */       double dot = movement.horizontal().normalize().dot(lookAngle.horizontal().normalize());
/* 211 */       double sign = movement.x * lookAngle.z - movement.z * lookAngle.x;
/* 212 */       state.flyingYRot = (float)(Math.signum(sign) * Math.acos(Math.min(1.0D, Math.abs(dot))));
/*     */     } else {
/* 214 */       state.shouldApplyFlyingYRot = false;
/* 215 */       state.flyingYRot = 0.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractCapeState(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks) {
/* 220 */     ClientAvatarState clientState = ((ClientAvatarEntity)entity).avatarState();
/* 221 */     double deltaX = clientState.getInterpolatedCloakX(partialTicks) - Mth.lerp(partialTicks, ((Avatar)entity).xo, entity.getX());
/* 222 */     double deltaY = clientState.getInterpolatedCloakY(partialTicks) - Mth.lerp(partialTicks, ((Avatar)entity).yo, entity.getY());
/* 223 */     double deltaZ = clientState.getInterpolatedCloakZ(partialTicks) - Mth.lerp(partialTicks, ((Avatar)entity).zo, entity.getZ());
/* 225 */     float yBodyRot = Mth.rotLerp(partialTicks, ((Avatar)entity).yBodyRotO, ((Avatar)entity).yBodyRot);
/* 227 */     double forwardX = Mth.sin((yBodyRot * 0.017453292F));
/* 228 */     double forwardZ = -Mth.cos((yBodyRot * 0.017453292F));
/* 230 */     state.capeFlap = (float)deltaY * 10.0F;
/* 231 */     state.capeFlap = Mth.clamp(state.capeFlap, -6.0F, 32.0F);
/* 232 */     state.capeLean = (float)(deltaX * forwardX + deltaZ * forwardZ) * 100.0F;
/* 233 */     state.capeLean *= 1.0F - state.fallFlyingScale();
/* 234 */     state.capeLean = Mth.clamp(state.capeLean, 0.0F, 150.0F);
/* 235 */     state.capeLean2 = (float)(deltaX * forwardZ - deltaZ * forwardX) * 100.0F;
/* 236 */     state.capeLean2 = Mth.clamp(state.capeLean2, -20.0F, 20.0F);
/* 238 */     float pow = clientState.getInterpolatedBob(partialTicks);
/* 239 */     float walkDistance = clientState.getInterpolatedWalkDistance(partialTicks);
/* 240 */     state.capeFlap += Mth.sin((walkDistance * 6.0F)) * 32.0F * pow;
/*     */   }
/*     */   
/*     */   public void renderRightHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier skinTexture, boolean hasSleeve) {
/* 244 */     renderHand(poseStack, submitNodeCollector, lightCoords, skinTexture, ((PlayerModel)this.model).rightArm, hasSleeve);
/*     */   }
/*     */   
/*     */   public void renderLeftHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier skinTexture, boolean hasSleeve) {
/* 248 */     renderHand(poseStack, submitNodeCollector, lightCoords, skinTexture, ((PlayerModel)this.model).leftArm, hasSleeve);
/*     */   }
/*     */   
/*     */   private void renderHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier skinTexture, ModelPart arm, boolean hasSleeve) {
/* 254 */     PlayerModel model = (PlayerModel)getModel();
/* 255 */     arm.resetPose();
/* 256 */     arm.visible = true;
/* 257 */     model.leftSleeve.visible = hasSleeve;
/* 258 */     model.rightSleeve.visible = hasSleeve;
/* 259 */     model.leftArm.zRot = -0.1F;
/* 260 */     model.rightArm.zRot = 0.1F;
/* 261 */     submitNodeCollector.submitModelPart(arm, poseStack, RenderTypes.entityTranslucent(skinTexture), lightCoords, OverlayTexture.NO_OVERLAY, null);
/*     */   }
/*     */   
/*     */   protected void setupRotations(AvatarRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 266 */     float swimAmount = state.swimAmount;
/* 267 */     float xRot = state.xRot;
/* 269 */     if (state.isFallFlying) {
/* 270 */       super.setupRotations((LivingEntityRenderState)state, poseStack, bodyRot, entityScale);
/* 272 */       float scale = state.fallFlyingScale();
/* 274 */       if (!state.isAutoSpinAttack)
/* 275 */         poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(scale * (-90.0F - xRot))); 
/* 278 */       if (state.shouldApplyFlyingYRot)
/* 279 */         poseStack.mulPose((Quaternionfc)Axis.YP.rotation(state.flyingYRot)); 
/* 281 */     } else if (swimAmount > 0.0F) {
/* 282 */       super.setupRotations((LivingEntityRenderState)state, poseStack, bodyRot, entityScale);
/* 286 */       float targetXRot = state.isInWater ? (-90.0F - xRot) : -90.0F;
/* 289 */       float xAngle = Mth.lerp(swimAmount, 0.0F, targetXRot);
/* 290 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(xAngle));
/* 292 */       if (state.isVisuallySwimming)
/* 294 */         poseStack.translate(0.0F, -1.0F, 0.3F); 
/*     */     } else {
/* 297 */       super.setupRotations((LivingEntityRenderState)state, poseStack, bodyRot, entityScale);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isEntityUpsideDown(AvatarlikeEntity mob) {
/* 304 */     if (mob.isModelPartShown(PlayerModelPart.CAPE)) {
/* 305 */       if (mob instanceof Player) {
/* 305 */         Player player = (Player)mob;
/* 306 */         return isPlayerUpsideDown(player);
/*     */       } 
/* 308 */       return super.isEntityUpsideDown((LivingEntity)mob);
/*     */     } 
/* 311 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isPlayerUpsideDown(Player player) {
/* 315 */     return isUpsideDownName(player.getGameProfile().name());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\player\AvatarRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */