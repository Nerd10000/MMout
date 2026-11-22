/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.MapRenderer;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.block.BlockModelResolver;
/*     */ import net.minecraft.client.renderer.block.model.BlockDisplayContext;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;
/*     */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.decoration.ItemFrame;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class ItemFrameRenderer<T extends ItemFrame> extends EntityRenderer<T, ItemFrameRenderState> {
/*  28 */   public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
/*     */   
/*     */   public static final int GLOW_FRAME_BRIGHTNESS = 5;
/*     */   
/*     */   public static final int BRIGHT_MAP_LIGHT_ADJUSTMENT = 30;
/*     */   
/*     */   private final BlockModelResolver blockModelResolver;
/*     */   
/*     */   private final ItemModelResolver itemModelResolver;
/*     */   
/*     */   private final MapRenderer mapRenderer;
/*     */   
/*     */   public ItemFrameRenderer(EntityRendererProvider.Context context) {
/*  38 */     super(context);
/*  39 */     this.blockModelResolver = context.getBlockModelResolver();
/*  40 */     this.itemModelResolver = context.getItemModelResolver();
/*  41 */     this.mapRenderer = context.getMapRenderer();
/*     */   }
/*     */   
/*     */   protected int getBlockLightLevel(T entity, BlockPos blockPos) {
/*  46 */     if (entity.is(EntityTypes.GLOW_ITEM_FRAME))
/*  47 */       return Math.max(5, super.getBlockLightLevel(entity, blockPos)); 
/*  49 */     return super.getBlockLightLevel(entity, blockPos);
/*     */   }
/*     */   
/*     */   public void submit(ItemFrameRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*     */     float xRot, yRot;
/*  54 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*  55 */     poseStack.pushPose();
/*  57 */     Direction direction = state.direction;
/*  59 */     Vec3 renderOffset = getRenderOffset(state);
/*  61 */     poseStack.translate(-renderOffset.x(), -renderOffset.y(), -renderOffset.z());
/*  63 */     double offs = 0.46875D;
/*  64 */     poseStack.translate(direction.getStepX() * 0.46875D, direction.getStepY() * 0.46875D, direction.getStepZ() * 0.46875D);
/*  68 */     if (direction.getAxis().isHorizontal()) {
/*  69 */       xRot = 0.0F;
/*  70 */       yRot = 180.0F - direction.toYRot();
/*     */     } else {
/*  72 */       xRot = (-90 * direction.getAxisDirection().getStep());
/*  73 */       yRot = 180.0F;
/*     */     } 
/*  75 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(xRot));
/*  76 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(yRot));
/*  78 */     if (!state.frameModel.isEmpty()) {
/*  79 */       poseStack.pushPose();
/*  80 */       poseStack.translate(-0.5F, -0.5F, -0.5F);
/*  81 */       state.frameModel.submitWithZOffset(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/*  82 */       poseStack.popPose();
/*     */     } 
/*  85 */     if (state.isInvisible) {
/*  86 */       poseStack.translate(0.0F, 0.0F, 0.5F);
/*     */     } else {
/*  88 */       poseStack.translate(0.0F, 0.0F, 0.4375F);
/*     */     } 
/*  91 */     if (state.mapId != null) {
/*  92 */       int rotation = state.rotation % 4 * 2;
/*  93 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(rotation * 360.0F / 8.0F));
/*  95 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(180.0F));
/*  96 */       float s = 0.0078125F;
/*  97 */       poseStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
/*  98 */       poseStack.translate(-64.0F, -64.0F, 0.0F);
/*  99 */       poseStack.translate(0.0F, 0.0F, -1.0F);
/* 100 */       int lightCoords = getLightCoords(state.isGlowFrame, 15728850, state.lightCoords);
/* 101 */       this.mapRenderer.render(state.mapRenderState, poseStack, submitNodeCollector, true, lightCoords);
/* 102 */     } else if (!state.item.isEmpty()) {
/* 103 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(state.rotation * 360.0F / 8.0F));
/* 105 */       int lightVal = getLightCoords(state.isGlowFrame, 15728880, state.lightCoords);
/* 107 */       poseStack.scale(0.5F, 0.5F, 0.5F);
/* 108 */       state.item.submit(poseStack, submitNodeCollector, lightVal, OverlayTexture.NO_OVERLAY, state.outlineColor);
/*     */     } 
/* 111 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private int getLightCoords(boolean isGlowFrame, int glowLightCoords, int originalLightCoords) {
/* 115 */     return isGlowFrame ? glowLightCoords : originalLightCoords;
/*     */   }
/*     */   
/*     */   public Vec3 getRenderOffset(ItemFrameRenderState state) {
/* 120 */     return new Vec3((state.direction.getStepX() * 0.3F), -0.25D, (state.direction.getStepZ() * 0.3F));
/*     */   }
/*     */   
/*     */   protected boolean shouldShowName(T entity, double distanceToCameraSq) {
/* 125 */     return (!(Minecraft.getInstance()).gui.hud.isHidden() && this.entityRenderDispatcher.crosshairPickEntity == entity && entity.getItem().getCustomName() != null);
/*     */   }
/*     */   
/*     */   protected Component getNameTag(T entity) {
/* 130 */     return entity.getItem().getHoverName();
/*     */   }
/*     */   
/*     */   public ItemFrameRenderState createRenderState() {
/* 135 */     return new ItemFrameRenderState();
/*     */   }
/*     */   
/*     */   public void extractRenderState(T entity, ItemFrameRenderState state, float partialTicks) {
/* 140 */     super.extractRenderState(entity, state, partialTicks);
/* 141 */     state.direction = entity.getDirection();
/* 142 */     ItemStack itemStack = entity.getItem();
/* 143 */     this.itemModelResolver.updateForNonLiving(state.item, itemStack, ItemDisplayContext.FIXED, (Entity)entity);
/* 144 */     state.rotation = entity.getRotation();
/* 145 */     state.isGlowFrame = entity.is(EntityTypes.GLOW_ITEM_FRAME);
/* 146 */     state.mapId = null;
/* 147 */     if (!itemStack.isEmpty()) {
/* 148 */       MapId framedMapId = entity.getFramedMapId(itemStack);
/* 149 */       if (framedMapId != null) {
/* 150 */         MapItemSavedData mapData = entity.level().getMapData(framedMapId);
/* 151 */         if (mapData != null) {
/* 152 */           this.mapRenderer.extractRenderState(framedMapId, mapData, state.mapRenderState);
/* 153 */           state.mapId = framedMapId;
/*     */         } 
/*     */       } 
/*     */     } 
/* 157 */     if (!state.isInvisible) {
/* 158 */       this.blockModelResolver.updateForItemFrame(state.frameModel, state.isGlowFrame, (state.mapId != null));
/*     */     } else {
/* 160 */       state.frameModel.clear();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\ItemFrameRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */