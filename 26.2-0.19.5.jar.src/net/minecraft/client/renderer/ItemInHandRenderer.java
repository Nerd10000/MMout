/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.model.effects.SpearAnimations;
/*     */ import net.minecraft.client.player.AbstractClientPlayer;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*     */ import net.minecraft.client.renderer.entity.player.AvatarRenderer;
/*     */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*     */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.MapRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.ItemOwner;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.PlayerModelPart;
/*     */ import net.minecraft.world.item.CrossbowItem;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUseAnimation;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.item.SwingAnimationType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class ItemInHandRenderer {
/*  45 */   private static final RenderType MAP_BACKGROUND = RenderTypes.text(Identifier.withDefaultNamespace("textures/map/map_background.png"));
/*     */   
/*  46 */   private static final RenderType MAP_BACKGROUND_CHECKERBOARD = RenderTypes.text(Identifier.withDefaultNamespace("textures/map/map_background_checkerboard.png"));
/*     */   
/*     */   private static final float ITEM_SWING_X_POS_SCALE = -0.4F;
/*     */   
/*     */   private static final float ITEM_SWING_Y_POS_SCALE = 0.2F;
/*     */   
/*     */   private static final float ITEM_SWING_Z_POS_SCALE = -0.2F;
/*     */   
/*     */   private static final float ITEM_HEIGHT_SCALE = -0.6F;
/*     */   
/*     */   private static final float ITEM_POS_X = 0.56F;
/*     */   
/*     */   private static final float ITEM_POS_Y = -0.52F;
/*     */   
/*     */   private static final float ITEM_POS_Z = -0.72F;
/*     */   
/*     */   private static final float ITEM_PRESWING_ROT_Y = 45.0F;
/*     */   
/*     */   private static final float ITEM_SWING_X_ROT_AMOUNT = -80.0F;
/*     */   
/*     */   private static final float ITEM_SWING_Y_ROT_AMOUNT = -20.0F;
/*     */   
/*     */   private static final float ITEM_SWING_Z_ROT_AMOUNT = -20.0F;
/*     */   
/*     */   private static final float EAT_JIGGLE_X_ROT_AMOUNT = 10.0F;
/*     */   
/*     */   private static final float EAT_JIGGLE_Y_ROT_AMOUNT = 90.0F;
/*     */   
/*     */   private static final float EAT_JIGGLE_Z_ROT_AMOUNT = 30.0F;
/*     */   
/*     */   private static final float EAT_JIGGLE_X_POS_SCALE = 0.6F;
/*     */   
/*     */   private static final float EAT_JIGGLE_Y_POS_SCALE = -0.5F;
/*     */   
/*     */   private static final float EAT_JIGGLE_Z_POS_SCALE = 0.0F;
/*     */   
/*     */   private static final double EAT_JIGGLE_EXPONENT = 27.0D;
/*     */   
/*     */   private static final float EAT_EXTRA_JIGGLE_CUTOFF = 0.8F;
/*     */   
/*     */   private static final float EAT_EXTRA_JIGGLE_SCALE = 0.1F;
/*     */   
/*     */   private static final float ARM_SWING_X_POS_SCALE = -0.3F;
/*     */   
/*     */   private static final float ARM_SWING_Y_POS_SCALE = 0.4F;
/*     */   
/*     */   private static final float ARM_SWING_Z_POS_SCALE = -0.4F;
/*     */   
/*     */   private static final float ARM_SWING_Y_ROT_AMOUNT = 70.0F;
/*     */   
/*     */   private static final float ARM_SWING_Z_ROT_AMOUNT = -20.0F;
/*     */   
/*     */   private static final float ARM_HEIGHT_SCALE = -0.6F;
/*     */   
/*     */   private static final float ARM_POS_SCALE = 0.8F;
/*     */   
/*     */   private static final float ARM_POS_X = 0.8F;
/*     */   
/*     */   private static final float ARM_POS_Y = -0.75F;
/*     */   
/*     */   private static final float ARM_POS_Z = -0.9F;
/*     */   
/*     */   private static final float ARM_PRESWING_ROT_Y = 45.0F;
/*     */   
/*     */   private static final float ARM_PREROTATION_X_OFFSET = -1.0F;
/*     */   
/*     */   private static final float ARM_PREROTATION_Y_OFFSET = 3.6F;
/*     */   
/*     */   private static final float ARM_PREROTATION_Z_OFFSET = 3.5F;
/*     */   
/*     */   private static final float ARM_POSTROTATION_X_OFFSET = 5.6F;
/*     */   
/*     */   private static final int ARM_ROT_X = 200;
/*     */   
/*     */   private static final int ARM_ROT_Y = -135;
/*     */   
/*     */   private static final int ARM_ROT_Z = 120;
/*     */   
/*     */   private static final float MAP_SWING_X_POS_SCALE = -0.4F;
/*     */   
/*     */   private static final float MAP_SWING_Z_POS_SCALE = -0.2F;
/*     */   
/*     */   private static final float MAP_HANDS_POS_X = 0.0F;
/*     */   
/*     */   private static final float MAP_HANDS_POS_Y = 0.04F;
/*     */   
/*     */   private static final float MAP_HANDS_POS_Z = -0.72F;
/*     */   
/*     */   private static final float MAP_HANDS_HEIGHT_SCALE = -1.2F;
/*     */   
/*     */   private static final float MAP_HANDS_TILT_SCALE = -0.5F;
/*     */   
/*     */   private static final float MAP_PLAYER_PITCH_SCALE = 45.0F;
/*     */   
/*     */   private static final float MAP_HANDS_Z_ROT_AMOUNT = -85.0F;
/*     */   
/*     */   private static final float MAPHAND_X_ROT_AMOUNT = 45.0F;
/*     */   
/*     */   private static final float MAPHAND_Y_ROT_AMOUNT = 92.0F;
/*     */   
/*     */   private static final float MAPHAND_Z_ROT_AMOUNT = -41.0F;
/*     */   
/*     */   private static final float MAP_HAND_X_POS = 0.3F;
/*     */   
/*     */   private static final float MAP_HAND_Y_POS = -1.1F;
/*     */   
/*     */   private static final float MAP_HAND_Z_POS = 0.45F;
/*     */   
/*     */   private static final float MAP_SWING_X_ROT_AMOUNT = 20.0F;
/*     */   
/*     */   private static final float MAP_PRE_ROT_SCALE = 0.38F;
/*     */   
/*     */   private static final float MAP_GLOBAL_X_POS = -0.5F;
/*     */   
/*     */   private static final float MAP_GLOBAL_Y_POS = -0.5F;
/*     */   
/*     */   private static final float MAP_GLOBAL_Z_POS = 0.0F;
/*     */   
/*     */   private static final float MAP_FINAL_SCALE = 0.0078125F;
/*     */   
/*     */   private static final int MAP_BORDER = 7;
/*     */   
/*     */   private static final int MAP_HEIGHT = 128;
/*     */   
/*     */   private static final int MAP_WIDTH = 128;
/*     */   
/*     */   private static final float BOW_CHARGE_X_POS_SCALE = 0.0F;
/*     */   
/*     */   private static final float BOW_CHARGE_Y_POS_SCALE = 0.0F;
/*     */   
/*     */   private static final float BOW_CHARGE_Z_POS_SCALE = 0.04F;
/*     */   
/*     */   private static final float BOW_CHARGE_SHAKE_X_SCALE = 0.0F;
/*     */   
/*     */   private static final float BOW_CHARGE_SHAKE_Y_SCALE = 0.004F;
/*     */   
/*     */   private static final float BOW_CHARGE_SHAKE_Z_SCALE = 0.0F;
/*     */   
/*     */   private static final float BOW_CHARGE_Z_SCALE = 0.2F;
/*     */   
/*     */   private static final float BOW_MIN_SHAKE_CHARGE = 0.1F;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/* 156 */   private final MapRenderState mapRenderState = new MapRenderState();
/*     */   
/* 157 */   private ItemStack mainHandItem = ItemStack.EMPTY;
/*     */   
/* 158 */   private ItemStack offHandItem = ItemStack.EMPTY;
/*     */   
/*     */   private float mainHandHeight;
/*     */   
/*     */   private float oMainHandHeight;
/*     */   
/*     */   private float offHandHeight;
/*     */   
/*     */   private float oOffHandHeight;
/*     */   
/*     */   private final EntityRenderDispatcher entityRenderDispatcher;
/*     */   
/*     */   private final ItemModelResolver itemModelResolver;
/*     */   
/*     */   public ItemInHandRenderer(Minecraft minecraft, EntityRenderDispatcher entityRenderDispatcher, ItemModelResolver itemModelResolver) {
/* 167 */     this.minecraft = minecraft;
/* 168 */     this.entityRenderDispatcher = entityRenderDispatcher;
/* 169 */     this.itemModelResolver = itemModelResolver;
/*     */   }
/*     */   
/*     */   public void renderItem(LivingEntity mob, ItemStack itemStack, ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
/* 173 */     if (itemStack.isEmpty())
/*     */       return; 
/* 177 */     ItemStackRenderState renderState = new ItemStackRenderState();
/* 178 */     this.itemModelResolver.updateForTopItem(renderState, itemStack, type, mob.level(), (ItemOwner)mob, mob.getId() + type.ordinal());
/* 179 */     renderState.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, 0);
/*     */   }
/*     */   
/*     */   private float calculateMapTilt(float xRot) {
/* 183 */     float tilt = 1.0F - xRot / 45.0F + 0.1F;
/* 184 */     tilt = Mth.clamp(tilt, 0.0F, 1.0F);
/* 185 */     tilt = -Mth.cos((tilt * 3.1415927F)) * 0.5F + 0.5F;
/* 186 */     return tilt;
/*     */   }
/*     */   
/*     */   private void renderMapHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, HumanoidArm arm) {
/* 190 */     AvatarRenderer<AbstractClientPlayer> avatarRenderer = this.entityRenderDispatcher.getPlayerRenderer((AbstractClientPlayer)this.minecraft.player);
/* 192 */     poseStack.pushPose();
/* 194 */     float invert = (arm == HumanoidArm.RIGHT) ? 1.0F : -1.0F;
/* 196 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(92.0F));
/* 197 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(45.0F));
/* 198 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * -41.0F));
/* 199 */     poseStack.translate(invert * 0.3F, -1.1F, 0.45F);
/* 201 */     Identifier skinTexture = this.minecraft.player.getSkin().body().texturePath();
/* 202 */     if (arm == HumanoidArm.RIGHT) {
/* 203 */       avatarRenderer.renderRightHand(poseStack, submitNodeCollector, lightCoords, skinTexture, this.minecraft.player.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE));
/*     */     } else {
/* 205 */       avatarRenderer.renderLeftHand(poseStack, submitNodeCollector, lightCoords, skinTexture, this.minecraft.player.isModelPartShown(PlayerModelPart.LEFT_SLEEVE));
/*     */     } 
/* 207 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private void renderOneHandedMap(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float inverseArmHeight, HumanoidArm arm, float attackValue, ItemStack map) {
/* 211 */     float invert = (arm == HumanoidArm.RIGHT) ? 1.0F : -1.0F;
/* 213 */     poseStack.translate(invert * 0.125F, -0.125F, 0.0F);
/* 215 */     if (!this.minecraft.player.isInvisible()) {
/* 216 */       poseStack.pushPose();
/* 217 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * 10.0F));
/* 218 */       renderPlayerArm(poseStack, submitNodeCollector, lightCoords, inverseArmHeight, attackValue, arm);
/* 219 */       poseStack.popPose();
/*     */     } 
/* 222 */     poseStack.pushPose();
/* 223 */     poseStack.translate(invert * 0.51F, -0.08F + inverseArmHeight * -1.2F, -0.75F);
/* 225 */     float sqrtAttackValue = Mth.sqrt(attackValue);
/* 226 */     float xSwing = Mth.sin((sqrtAttackValue * 3.1415927F));
/* 227 */     float xSwingPosition = -0.5F * xSwing;
/* 228 */     float ySwingPosition = 0.4F * Mth.sin((sqrtAttackValue * 6.2831855F));
/* 229 */     float zSwingPosition = -0.3F * Mth.sin((attackValue * 3.1415927F));
/* 230 */     poseStack.translate(invert * xSwingPosition, ySwingPosition - 0.3F * xSwing, zSwingPosition);
/* 232 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(xSwing * -45.0F));
/* 233 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * xSwing * -30.0F));
/* 234 */     renderMap(poseStack, submitNodeCollector, lightCoords, map);
/* 235 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private void renderTwoHandedMap(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float xRot, float inverseArmHeight, float attackValue) {
/* 239 */     float sqrtAttackValue = Mth.sqrt(attackValue);
/* 240 */     float ySwingPosition = -0.2F * Mth.sin((attackValue * 3.1415927F));
/* 241 */     float zSwingPosition = -0.4F * Mth.sin((sqrtAttackValue * 3.1415927F));
/* 242 */     poseStack.translate(0.0F, -ySwingPosition / 2.0F, zSwingPosition);
/* 244 */     float mapTilt = calculateMapTilt(xRot);
/* 245 */     poseStack.translate(0.0F, 0.04F + inverseArmHeight * -1.2F + mapTilt * -0.5F, -0.72F);
/* 246 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(mapTilt * -85.0F));
/* 248 */     if (!this.minecraft.player.isInvisible()) {
/* 249 */       poseStack.pushPose();
/* 250 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(90.0F));
/* 252 */       renderMapHand(poseStack, submitNodeCollector, lightCoords, HumanoidArm.RIGHT);
/* 253 */       renderMapHand(poseStack, submitNodeCollector, lightCoords, HumanoidArm.LEFT);
/* 255 */       poseStack.popPose();
/*     */     } 
/* 258 */     float xzSwingRotation = Mth.sin((sqrtAttackValue * 3.1415927F));
/* 259 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(xzSwingRotation * 20.0F));
/* 261 */     poseStack.scale(2.0F, 2.0F, 2.0F);
/* 263 */     renderMap(poseStack, submitNodeCollector, lightCoords, this.mainHandItem);
/*     */   }
/*     */   
/*     */   private void renderMap(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, ItemStack itemStack) {
/* 267 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F));
/* 268 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(180.0F));
/* 270 */     poseStack.scale(0.38F, 0.38F, 0.38F);
/* 272 */     poseStack.translate(-0.5F, -0.5F, 0.0F);
/* 273 */     poseStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
/* 275 */     MapId id = (MapId)itemStack.get(DataComponents.MAP_ID);
/* 276 */     MapItemSavedData data = MapItem.getSavedData(id, (Level)this.minecraft.level);
/* 278 */     RenderType renderType = (data == null) ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD;
/* 280 */     submitNodeCollector.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
/*     */           buffer.addVertex(pose, -7.0F, 135.0F, 0.0F).setColor(-1).setUv(0.0F, 1.0F).setLight(lightCoords);
/*     */           buffer.addVertex(pose, 135.0F, 135.0F, 0.0F).setColor(-1).setUv(1.0F, 1.0F).setLight(lightCoords);
/*     */           buffer.addVertex(pose, 135.0F, -7.0F, 0.0F).setColor(-1).setUv(1.0F, 0.0F).setLight(lightCoords);
/*     */           buffer.addVertex(pose, -7.0F, -7.0F, 0.0F).setColor(-1).setUv(0.0F, 0.0F).setLight(lightCoords);
/*     */         });
/* 288 */     if (data != null) {
/* 289 */       MapRenderer mapRenderer = this.minecraft.getMapRenderer();
/* 290 */       mapRenderer.extractRenderState(id, data, this.mapRenderState);
/* 291 */       mapRenderer.render(this.mapRenderState, poseStack, submitNodeCollector, false, lightCoords);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderPlayerArm(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float inverseArmHeight, float attackValue, HumanoidArm arm) {
/* 296 */     boolean isRightArm = (arm != HumanoidArm.LEFT);
/* 297 */     float invert = isRightArm ? 1.0F : -1.0F;
/* 298 */     float sqrtAttackValue = Mth.sqrt(attackValue);
/* 299 */     float xSwingPosition = -0.3F * Mth.sin((sqrtAttackValue * 3.1415927F));
/* 300 */     float ySwingPosition = 0.4F * Mth.sin((sqrtAttackValue * 6.2831855F));
/* 301 */     float zSwingPosition = -0.4F * Mth.sin((attackValue * 3.1415927F));
/* 302 */     poseStack.translate(invert * (xSwingPosition + 0.64000005F), ySwingPosition + -0.6F + inverseArmHeight * -0.6F, zSwingPosition + -0.71999997F);
/* 308 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * 45.0F));
/* 310 */     float zSwingRotation = Mth.sin((attackValue * attackValue * 3.1415927F));
/* 311 */     float ySwingRotation = Mth.sin((sqrtAttackValue * 3.1415927F));
/* 312 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * ySwingRotation * 70.0F));
/* 313 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * zSwingRotation * -20.0F));
/* 315 */     LocalPlayer localPlayer = this.minecraft.player;
/* 317 */     poseStack.translate(invert * -1.0F, 3.6F, 3.5F);
/* 318 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * 120.0F));
/* 319 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(200.0F));
/* 320 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * -135.0F));
/* 321 */     poseStack.translate(invert * 5.6F, 0.0F, 0.0F);
/* 323 */     AvatarRenderer<AbstractClientPlayer> avatarRenderer = this.entityRenderDispatcher.getPlayerRenderer((AbstractClientPlayer)localPlayer);
/* 324 */     Identifier skinTexture = localPlayer.getSkin().body().texturePath();
/* 325 */     if (isRightArm) {
/* 326 */       avatarRenderer.renderRightHand(poseStack, submitNodeCollector, lightCoords, skinTexture, localPlayer.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE));
/*     */     } else {
/* 328 */       avatarRenderer.renderLeftHand(poseStack, submitNodeCollector, lightCoords, skinTexture, localPlayer.isModelPartShown(PlayerModelPart.LEFT_SLEEVE));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void applyEatTransform(PoseStack poseStack, float frameInterp, HumanoidArm arm, ItemStack itemStack, Player player) {
/* 333 */     float currUsageTime = player.getUseItemRemainingTicks() - frameInterp + 1.0F;
/* 334 */     float scaledUsageTime = currUsageTime / itemStack.getUseDuration((LivingEntity)player);
/* 336 */     if (scaledUsageTime < 0.8F) {
/* 337 */       float extraHeightOffset = Mth.abs(Mth.cos((currUsageTime / 4.0F * 3.1415927F)) * 0.1F);
/* 338 */       poseStack.translate(0.0F, extraHeightOffset, 0.0F);
/*     */     } 
/* 341 */     float eatJiggle = 1.0F - (float)Math.pow(scaledUsageTime, 27.0D);
/* 343 */     int invert = (arm == HumanoidArm.RIGHT) ? 1 : -1;
/* 344 */     poseStack.translate(eatJiggle * 0.6F * invert, eatJiggle * -0.5F, eatJiggle * 0.0F);
/* 346 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * eatJiggle * 90.0F));
/* 347 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(eatJiggle * 10.0F));
/* 348 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * eatJiggle * 30.0F));
/*     */   }
/*     */   
/*     */   private void applyBrushTransform(PoseStack poseStack, float frameInterp, HumanoidArm arm, Player player) {
/* 352 */     float brushAnimationRemainingTicks = (player.getUseItemRemainingTicks() % 10);
/* 353 */     float deltaSinceLastUpdate = brushAnimationRemainingTicks - frameInterp + 1.0F;
/* 354 */     float scaledUsageTime = 1.0F - deltaSinceLastUpdate / 10.0F;
/* 356 */     float minSwipeAngle = -90.0F;
/* 357 */     float maxSwipeAngle = 60.0F;
/* 358 */     float swipeRange = 150.0F;
/* 359 */     float swipeCenter = -15.0F;
/* 361 */     int swipeSpeed = 2;
/* 363 */     float currentSwipeAngle = -15.0F + 75.0F * Mth.cos((scaledUsageTime * 2.0F * 3.1415927F));
/* 364 */     if (arm != HumanoidArm.RIGHT) {
/* 365 */       poseStack.translate(0.1D, 0.83D, 0.35D);
/* 367 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-80.0F));
/* 368 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-90.0F));
/* 370 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(currentSwipeAngle));
/* 371 */       poseStack.translate(-0.3D, 0.22D, 0.35D);
/*     */     } else {
/* 373 */       poseStack.translate(-0.25D, 0.22D, 0.35D);
/* 375 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-80.0F));
/* 376 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(90.0F));
/* 377 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(0.0F));
/* 379 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(currentSwipeAngle));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void applyItemArmAttackTransform(PoseStack poseStack, HumanoidArm arm, float attackValue) {
/* 384 */     int invert = (arm == HumanoidArm.RIGHT) ? 1 : -1;
/* 386 */     float ySwingRotation = Mth.sin((attackValue * attackValue * 3.1415927F));
/* 387 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * (45.0F + ySwingRotation * -20.0F)));
/* 389 */     float xzSwingRotation = Mth.sin((Mth.sqrt(attackValue) * 3.1415927F));
/* 390 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * xzSwingRotation * -20.0F));
/* 391 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(xzSwingRotation * -80.0F));
/* 392 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * -45.0F));
/*     */   }
/*     */   
/*     */   private void applyItemArmTransform(PoseStack poseStack, HumanoidArm arm, float inverseArmHeight) {
/* 396 */     int invert = (arm == HumanoidArm.RIGHT) ? 1 : -1;
/* 397 */     poseStack.translate(invert * 0.56F, -0.52F + inverseArmHeight * -0.6F, -0.72F);
/*     */   }
/*     */   
/*     */   public void submitHandsWithItems(float frameInterp, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LocalPlayer player, int lightCoords) {
/* 401 */     float attackValue = player.getAttackAnim(frameInterp);
/* 402 */     InteractionHand attackHand = (InteractionHand)MoreObjects.firstNonNull(player.swingingArm, InteractionHand.MAIN_HAND);
/* 403 */     float xRot = player.getXRot(frameInterp);
/* 405 */     HandRenderSelection handRenderSelection = evaluateWhichHandsToRender(player);
/* 407 */     float xBob = Mth.lerp(frameInterp, player.xBobO, player.xBob);
/* 408 */     float yBob = Mth.lerp(frameInterp, player.yBobO, player.yBob);
/* 409 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees((player.getViewXRot(frameInterp) - xBob) * 0.1F));
/* 410 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees((player.getViewYRot(frameInterp) - yBob) * 0.1F));
/* 412 */     if (handRenderSelection.renderMainHand) {
/* 413 */       float mainHandAttack = (attackHand == InteractionHand.MAIN_HAND) ? attackValue : 0.0F;
/* 414 */       float mainhandInverseArmHeight = this.itemModelResolver.swapAnimationScale(this.mainHandItem) * (1.0F - Mth.lerp(frameInterp, this.oMainHandHeight, this.mainHandHeight));
/* 415 */       submitArmWithItem((AbstractClientPlayer)player, frameInterp, xRot, InteractionHand.MAIN_HAND, mainHandAttack, this.mainHandItem, mainhandInverseArmHeight, poseStack, submitNodeCollector, lightCoords);
/*     */     } 
/* 418 */     if (handRenderSelection.renderOffHand) {
/* 419 */       float offHandAttack = (attackHand == InteractionHand.OFF_HAND) ? attackValue : 0.0F;
/* 420 */       float offhandInverseArmHeight = this.itemModelResolver.swapAnimationScale(this.offHandItem) * (1.0F - Mth.lerp(frameInterp, this.oOffHandHeight, this.offHandHeight));
/* 421 */       submitArmWithItem((AbstractClientPlayer)player, frameInterp, xRot, InteractionHand.OFF_HAND, offHandAttack, this.offHandItem, offhandInverseArmHeight, poseStack, submitNodeCollector, lightCoords);
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   enum HandRenderSelection {
/* 428 */     RENDER_BOTH_HANDS(true, true),
/* 429 */     RENDER_MAIN_HAND_ONLY(true, false),
/* 430 */     RENDER_OFF_HAND_ONLY(false, true);
/*     */     
/*     */     public final boolean renderMainHand;
/*     */     
/*     */     public final boolean renderOffHand;
/*     */     
/*     */     HandRenderSelection(boolean renderMainHand, boolean renderOffHand) {
/* 435 */       this.renderMainHand = renderMainHand;
/* 436 */       this.renderOffHand = renderOffHand;
/*     */     }
/*     */     
/*     */     public static HandRenderSelection onlyForHand(InteractionHand hand) {
/* 440 */       return (hand == InteractionHand.MAIN_HAND) ? RENDER_MAIN_HAND_ONLY : RENDER_OFF_HAND_ONLY;
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static HandRenderSelection evaluateWhichHandsToRender(LocalPlayer player) {
/* 448 */     ItemStack mainHandItem = player.getMainHandItem();
/* 449 */     ItemStack offhandItem = player.getOffhandItem();
/* 451 */     boolean holdsBow = (mainHandItem.is(Items.BOW) || offhandItem.is(Items.BOW));
/* 452 */     boolean holdsCrossbow = (mainHandItem.is(Items.CROSSBOW) || offhandItem.is(Items.CROSSBOW));
/* 454 */     if (!holdsBow && !holdsCrossbow)
/* 455 */       return HandRenderSelection.RENDER_BOTH_HANDS; 
/* 458 */     if (player.isUsingItem())
/* 459 */       return selectionUsingItemWhileHoldingBowLike(player); 
/* 462 */     if (isChargedCrossbow(mainHandItem))
/* 463 */       return HandRenderSelection.RENDER_MAIN_HAND_ONLY; 
/* 466 */     return HandRenderSelection.RENDER_BOTH_HANDS;
/*     */   }
/*     */   
/*     */   private static HandRenderSelection selectionUsingItemWhileHoldingBowLike(LocalPlayer player) {
/* 470 */     ItemStack usedItemStack = player.getUseItem();
/* 471 */     InteractionHand usedHand = player.getUsedItemHand();
/* 473 */     if (usedItemStack.is(Items.BOW) || usedItemStack.is(Items.CROSSBOW))
/* 474 */       return HandRenderSelection.onlyForHand(usedHand); 
/* 478 */     return (usedHand == InteractionHand.MAIN_HAND && isChargedCrossbow(player.getOffhandItem())) ? 
/* 479 */       HandRenderSelection.RENDER_MAIN_HAND_ONLY : 
/* 480 */       HandRenderSelection.RENDER_BOTH_HANDS;
/*     */   }
/*     */   
/*     */   private static boolean isChargedCrossbow(ItemStack item) {
/* 484 */     return (item.is(Items.CROSSBOW) && CrossbowItem.isCharged(item));
/*     */   }
/*     */   
/*     */   private void submitArmWithItem(AbstractClientPlayer player, float frameInterp, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
/* 488 */     if (player.isScoping())
/*     */       return; 
/* 492 */     boolean isMainHand = (hand == InteractionHand.MAIN_HAND);
/* 493 */     HumanoidArm arm = isMainHand ? player.getMainArm() : player.getMainArm().getOpposite();
/* 495 */     poseStack.pushPose();
/* 496 */     if (itemStack.isEmpty()) {
/* 497 */       if (isMainHand && !player.isInvisible())
/* 498 */         renderPlayerArm(poseStack, submitNodeCollector, lightCoords, inverseArmHeight, attack, arm); 
/* 501 */     } else if (itemStack.has(DataComponents.MAP_ID)) {
/* 502 */       if (isMainHand && this.offHandItem.isEmpty()) {
/* 503 */         renderTwoHandedMap(poseStack, submitNodeCollector, lightCoords, xRot, inverseArmHeight, attack);
/*     */       } else {
/* 505 */         renderOneHandedMap(poseStack, submitNodeCollector, lightCoords, inverseArmHeight, arm, attack, itemStack);
/*     */       } 
/* 507 */     } else if (itemStack.is(Items.CROSSBOW)) {
/* 508 */       applyItemArmTransform(poseStack, arm, inverseArmHeight);
/* 510 */       boolean charged = CrossbowItem.isCharged(itemStack);
/* 511 */       boolean isRightArm = (arm == HumanoidArm.RIGHT);
/* 512 */       int invert = isRightArm ? 1 : -1;
/* 514 */       if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == hand && !charged) {
/* 515 */         poseStack.translate(invert * -0.4785682F, -0.094387F, 0.05731531F);
/* 516 */         poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-11.935F));
/* 517 */         poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * 65.3F));
/* 518 */         poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * -9.785F));
/* 520 */         float timeHeld = itemStack.getUseDuration((LivingEntity)player) - player.getUseItemRemainingTicks() - frameInterp + 1.0F;
/* 521 */         float power = timeHeld / CrossbowItem.getChargeDuration(itemStack, (LivingEntity)player);
/* 522 */         if (power > 1.0F)
/* 523 */           power = 1.0F; 
/* 525 */         if (power > 0.1F) {
/* 526 */           float shakeOffset = Mth.sin(((timeHeld - 0.1F) * 1.3F));
/* 527 */           float shakeIntensity = power - 0.1F;
/* 528 */           float shake = shakeOffset * shakeIntensity;
/* 529 */           poseStack.translate(shake * 0.0F, shake * 0.004F, shake * 0.0F);
/*     */         } 
/* 531 */         poseStack.translate(power * 0.0F, power * 0.0F, power * 0.04F);
/* 533 */         poseStack.scale(1.0F, 1.0F, 1.0F + power * 0.2F);
/* 534 */         poseStack.mulPose((Quaternionfc)Axis.YN.rotationDegrees(invert * 45.0F));
/*     */       } else {
/* 536 */         swingArm(attack, poseStack, invert, arm);
/* 539 */         if (charged && attack < 0.001F && isMainHand) {
/* 540 */           poseStack.translate(invert * -0.641864F, 0.0F, 0.0F);
/* 541 */           poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * 10.0F));
/*     */         } 
/*     */       } 
/* 545 */       renderItem((LivingEntity)player, itemStack, isRightArm ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, poseStack, submitNodeCollector, lightCoords);
/*     */     } else {
/* 547 */       boolean isRightArm = (arm == HumanoidArm.RIGHT);
/* 548 */       int invert = isRightArm ? 1 : -1;
/* 550 */       if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == hand) {
/*     */         float timeHeld, power;
/* 551 */         ItemUseAnimation useAnimation = itemStack.getUseAnimation();
/* 552 */         if (!useAnimation.hasCustomArmTransform())
/* 553 */           applyItemArmTransform(poseStack, arm, inverseArmHeight); 
/* 555 */         switch (useAnimation) {
/*     */           case WHACK:
/*     */           case STAB:
/* 559 */             applyEatTransform(poseStack, frameInterp, arm, itemStack, (Player)player);
/* 560 */             applyItemArmTransform(poseStack, arm, inverseArmHeight);
/*     */             break;
/*     */           case null:
/* 563 */             if (!(itemStack.getItem() instanceof net.minecraft.world.item.ShieldItem)) {
/* 564 */               poseStack.translate(invert * -0.14142136F, 0.08F, 0.14142136F);
/* 565 */               poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-102.25F));
/* 566 */               poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * 13.365F));
/* 567 */               poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * 78.05F));
/*     */             } 
/*     */             break;
/*     */           case null:
/* 571 */             poseStack.translate(invert * -0.2785682F, 0.18344387F, 0.15731531F);
/* 572 */             poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-13.935F));
/* 573 */             poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * 35.3F));
/* 574 */             poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * -9.785F));
/* 576 */             timeHeld = itemStack.getUseDuration((LivingEntity)player) - player.getUseItemRemainingTicks() - frameInterp + 1.0F;
/* 577 */             power = timeHeld / 20.0F;
/* 578 */             power = (power * power + power * 2.0F) / 3.0F;
/* 579 */             if (power > 1.0F)
/* 580 */               power = 1.0F; 
/* 582 */             if (power > 0.1F) {
/* 583 */               float shakeOffset = Mth.sin(((timeHeld - 0.1F) * 1.3F));
/* 584 */               float shakeIntensity = power - 0.1F;
/* 585 */               float shake = shakeOffset * shakeIntensity;
/* 586 */               poseStack.translate(shake * 0.0F, shake * 0.004F, shake * 0.0F);
/*     */             } 
/* 588 */             poseStack.translate(power * 0.0F, power * 0.0F, power * 0.04F);
/* 590 */             poseStack.scale(1.0F, 1.0F, 1.0F + power * 0.2F);
/* 591 */             poseStack.mulPose((Quaternionfc)Axis.YN.rotationDegrees(invert * 45.0F));
/*     */             break;
/*     */           case null:
/* 594 */             poseStack.translate(invert * -0.5F, 0.7F, 0.1F);
/* 595 */             poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-55.0F));
/* 596 */             poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * 35.3F));
/* 597 */             poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * -9.785F));
/* 599 */             timeHeld = itemStack.getUseDuration((LivingEntity)player) - player.getUseItemRemainingTicks() - frameInterp + 1.0F;
/* 600 */             power = timeHeld / 10.0F;
/* 601 */             if (power > 1.0F)
/* 602 */               power = 1.0F; 
/* 604 */             if (power > 0.1F) {
/* 605 */               float shakeOffset = Mth.sin(((timeHeld - 0.1F) * 1.3F));
/* 606 */               float shakeIntensity = power - 0.1F;
/* 607 */               float shake = shakeOffset * shakeIntensity;
/* 608 */               poseStack.translate(shake * 0.0F, shake * 0.004F, shake * 0.0F);
/*     */             } 
/* 610 */             poseStack.translate(0.0F, 0.0F, power * 0.2F);
/* 612 */             poseStack.scale(1.0F, 1.0F, 1.0F + power * 0.2F);
/* 613 */             poseStack.mulPose((Quaternionfc)Axis.YN.rotationDegrees(invert * 45.0F));
/*     */             break;
/*     */           case null:
/* 615 */             applyBrushTransform(poseStack, frameInterp, arm, (Player)player);
/*     */             break;
/*     */           case null:
/* 616 */             swingArm(attack, poseStack, invert, arm);
/*     */             break;
/*     */           case null:
/* 618 */             poseStack.translate(invert * 0.56F, -0.52F, -0.72F);
/* 619 */             timeHeld = itemStack.getUseDuration((LivingEntity)player) - player.getUseItemRemainingTicks() - frameInterp + 1.0F;
/* 620 */             SpearAnimations.firstPersonUse(player.getTicksSinceLastKineticHitFeedback(frameInterp), poseStack, timeHeld, arm, itemStack);
/*     */             break;
/*     */         } 
/* 623 */       } else if (player.isAutoSpinAttack()) {
/* 624 */         applyItemArmTransform(poseStack, arm, inverseArmHeight);
/* 625 */         poseStack.translate(invert * -0.4F, 0.8F, 0.3F);
/* 626 */         poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(invert * 65.0F));
/* 627 */         poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(invert * -85.0F));
/*     */       } else {
/* 629 */         applyItemArmTransform(poseStack, arm, inverseArmHeight);
/* 630 */         switch (itemStack.getSwingAnimation().type()) {
/*     */           case WHACK:
/* 633 */             swingArm(attack, poseStack, invert, arm);
/*     */             break;
/*     */           case STAB:
/* 634 */             SpearAnimations.firstPersonAttack(attack, poseStack, invert, arm);
/*     */             break;
/*     */         } 
/*     */       } 
/* 637 */       renderItem((LivingEntity)player, itemStack, isRightArm ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, poseStack, submitNodeCollector, lightCoords);
/*     */     } 
/* 640 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private void swingArm(float attack, PoseStack poseStack, int invert, HumanoidArm arm) {
/* 644 */     float xSwingPosition = -0.4F * Mth.sin((Mth.sqrt(attack) * 3.1415927F));
/* 645 */     float ySwingPosition = 0.2F * Mth.sin((Mth.sqrt(attack) * 6.2831855F));
/* 646 */     float zSwingPosition = -0.2F * Mth.sin((attack * 3.1415927F));
/* 647 */     poseStack.translate(invert * xSwingPosition, ySwingPosition, zSwingPosition);
/* 648 */     applyItemArmAttackTransform(poseStack, arm, attack);
/*     */   }
/*     */   
/*     */   private boolean shouldInstantlyReplaceVisibleItem(ItemStack currentlyVisibleItem, ItemStack expectedItem) {
/* 652 */     if (ItemStack.matchesIgnoringComponents(currentlyVisibleItem, expectedItem, DataComponentType::ignoreSwapAnimation))
/* 653 */       return true; 
/* 656 */     return !this.itemModelResolver.shouldPlaySwapAnimation(expectedItem);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 661 */     this.oMainHandHeight = this.mainHandHeight;
/* 662 */     this.oOffHandHeight = this.offHandHeight;
/* 664 */     LocalPlayer player = this.minecraft.player;
/* 665 */     ItemStack nextMainHand = player.getMainHandItem();
/* 666 */     ItemStack nextOffHand = player.getOffhandItem();
/* 669 */     if (shouldInstantlyReplaceVisibleItem(this.mainHandItem, nextMainHand))
/* 670 */       this.mainHandItem = nextMainHand; 
/* 672 */     if (shouldInstantlyReplaceVisibleItem(this.offHandItem, nextOffHand))
/* 673 */       this.offHandItem = nextOffHand; 
/* 676 */     if (player.isHandsBusy()) {
/* 677 */       this.mainHandHeight = Mth.clamp(this.mainHandHeight - 0.4F, 0.0F, 1.0F);
/* 678 */       this.offHandHeight = Mth.clamp(this.offHandHeight - 0.4F, 0.0F, 1.0F);
/*     */     } else {
/* 680 */       float attackAnim = player.getItemSwapScale(1.0F);
/* 682 */       float mainHandTargetHeight = (this.mainHandItem != nextMainHand) ? 0.0F : (attackAnim * attackAnim * attackAnim);
/* 683 */       float offHandTargetHeight = (this.offHandItem != nextOffHand) ? 0.0F : 1.0F;
/* 685 */       this.mainHandHeight += Mth.clamp(mainHandTargetHeight - this.mainHandHeight, -0.4F, 0.4F);
/* 686 */       this.offHandHeight += Mth.clamp(offHandTargetHeight - this.offHandHeight, -0.4F, 0.4F);
/*     */     } 
/* 690 */     if (this.mainHandHeight < 0.1F)
/* 691 */       this.mainHandItem = nextMainHand; 
/* 694 */     if (this.offHandHeight < 0.1F)
/* 695 */       this.offHandItem = nextOffHand; 
/*     */   }
/*     */   
/*     */   public void itemUsed(InteractionHand hand) {
/* 700 */     if (hand == InteractionHand.MAIN_HAND) {
/* 701 */       this.mainHandHeight = 0.0F;
/*     */     } else {
/* 703 */       this.offHandHeight = 0.0F;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\ItemInHandRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */