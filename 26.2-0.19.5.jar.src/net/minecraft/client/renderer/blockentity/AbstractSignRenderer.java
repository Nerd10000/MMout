/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.SignRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SignText;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractSignRenderer<S extends SignRenderState> implements BlockEntityRenderer<SignBlockEntity, S> {
/*     */   private static final int BLACK_TEXT_OUTLINE_COLOR = -988212;
/*     */   
/*  27 */   private static final int OUTLINE_RENDER_DISTANCE = Mth.square(16);
/*     */   
/*     */   private final Font font;
/*     */   
/*     */   public AbstractSignRenderer(BlockEntityRendererProvider.Context context) {
/*  32 */     this.font = context.font();
/*     */   }
/*     */   
/*     */   public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  37 */     if (((SignRenderState)state).frontText != null) {
/*  38 */       poseStack.pushPose();
/*  39 */       poseStack.mulPose(((SignRenderState)state).transformations.frontText());
/*  40 */       submitSignText(state, poseStack, submitNodeCollector, ((SignRenderState)state).frontText);
/*  41 */       poseStack.popPose();
/*     */     } 
/*  44 */     if (((SignRenderState)state).backText != null) {
/*  45 */       poseStack.pushPose();
/*  46 */       poseStack.mulPose(((SignRenderState)state).transformations.backText());
/*  47 */       submitSignText(state, poseStack, submitNodeCollector, ((SignRenderState)state).backText);
/*  48 */       poseStack.popPose();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void submitSignText(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, SignText signText) {
/*     */     int textColor;
/*     */     boolean drawOutline;
/*  53 */     int lightVal, darkColor = getDarkColor(signText);
/*  54 */     int signMidpoint = 4 * ((SignRenderState)state).textLineHeight / 2;
/*  55 */     FormattedCharSequence[] formattedLines = signText.getRenderMessages(((SignRenderState)state).isTextFilteringEnabled, input -> {
/*     */           List<FormattedCharSequence> components = this.font.split((FormattedText)input, state.maxTextLineWidth);
/*     */           return components.isEmpty() ? FormattedCharSequence.EMPTY : components.get(0);
/*     */         });
/*  63 */     if (signText.hasGlowingText()) {
/*  64 */       textColor = signText.getColor().getTextColor();
/*  65 */       drawOutline = (textColor == DyeColor.BLACK.getTextColor() || ((SignRenderState)state).drawOutline);
/*  66 */       lightVal = 15728880;
/*     */     } else {
/*  68 */       textColor = darkColor;
/*  69 */       drawOutline = false;
/*  70 */       lightVal = ((SignRenderState)state).lightCoords;
/*     */     } 
/*  73 */     for (int i = 0; i < 4; i++) {
/*  74 */       FormattedCharSequence actualLine = formattedLines[i];
/*  75 */       float x1 = (-this.font.width(actualLine) / 2);
/*  77 */       submitNodeCollector.submitText(poseStack, x1, (i * ((SignRenderState)state).textLineHeight - signMidpoint), actualLine, false, Font.DisplayMode.POLYGON_OFFSET, lightVal, textColor, 0, drawOutline ? darkColor : 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isOutlineVisible(BlockPos pos) {
/*  82 */     Minecraft minecraft = Minecraft.getInstance();
/*  83 */     LocalPlayer player = minecraft.player;
/*  84 */     if (player != null && minecraft.options.getCameraType().isFirstPerson() && player.isScoping())
/*  85 */       return true; 
/*  88 */     Entity camera = minecraft.getCameraEntity();
/*  89 */     if (camera != null && camera.distanceToSqr(Vec3.atCenterOf((Vec3i)pos)) < OUTLINE_RENDER_DISTANCE)
/*  90 */       return true; 
/*  93 */     return false;
/*     */   }
/*     */   
/*     */   public static int getDarkColor(SignText signText) {
/*  97 */     int color = signText.getColor().getTextColor();
/*  99 */     if (color == DyeColor.BLACK.getTextColor() && signText.hasGlowingText())
/* 100 */       return -988212; 
/* 103 */     return ARGB.scaleRGB(color, 0.4F);
/*     */   }
/*     */   
/*     */   public void extractRenderState(SignBlockEntity blockEntity, S state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 108 */     super.extractRenderState(blockEntity, (BlockEntityRenderState)state, partialTicks, cameraPosition, breakProgress);
/* 109 */     ((SignRenderState)state).maxTextLineWidth = blockEntity.getMaxTextLineWidth();
/* 110 */     ((SignRenderState)state).textLineHeight = blockEntity.getTextLineHeight();
/* 111 */     ((SignRenderState)state).frontText = blockEntity.getFrontText();
/* 112 */     ((SignRenderState)state).backText = blockEntity.getBackText();
/* 113 */     ((SignRenderState)state).isTextFilteringEnabled = Minecraft.getInstance().isTextFilteringEnabled();
/* 114 */     ((SignRenderState)state).drawOutline = isOutlineVisible(blockEntity.getBlockPos());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\AbstractSignRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */