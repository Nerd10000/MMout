/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.entity.state.DisplayEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.TextDisplayEntityRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.entity.Display;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.joml.Matrix4f;
/*     */ 
/*     */ public class TextDisplayRenderer extends DisplayRenderer<Display.TextDisplay, Display.TextDisplay.TextRenderState, TextDisplayEntityRenderState> {
/*     */   private static final float TEXT_BACKGROUND_OFFSET = -0.01F;
/*     */   
/*     */   private final Font font;
/*     */   
/*     */   protected TextDisplayRenderer(EntityRendererProvider.Context context) {
/* 225 */     super(context);
/* 226 */     this.font = context.getFont();
/*     */   }
/*     */   
/*     */   public TextDisplayEntityRenderState createRenderState() {
/* 231 */     return new TextDisplayEntityRenderState();
/*     */   }
/*     */   
/*     */   public void extractRenderState(Display.TextDisplay entity, TextDisplayEntityRenderState state, float partialTicks) {
/* 236 */     super.extractRenderState(entity, state, partialTicks);
/* 237 */     state.textRenderState = entity.textRenderState();
/* 238 */     state.cachedInfo = entity.cacheDisplay(this::splitLines);
/*     */   }
/*     */   
/*     */   private Display.TextDisplay.CachedInfo splitLines(Component input, int width) {
/* 242 */     List<FormattedCharSequence> lines = this.font.split((FormattedText)input, width);
/* 243 */     List<Display.TextDisplay.CachedLine> result = new ArrayList<>(lines.size());
/* 245 */     int maxLineWidth = 0;
/* 246 */     for (FormattedCharSequence line : lines) {
/* 247 */       int lineWidth = this.font.width(line);
/* 248 */       maxLineWidth = Math.max(maxLineWidth, lineWidth);
/* 249 */       result.add(new Display.TextDisplay.CachedLine(line, lineWidth));
/*     */     } 
/* 252 */     return new Display.TextDisplay.CachedInfo(result, maxLineWidth);
/*     */   }
/*     */   
/*     */   public void submitInner(TextDisplayEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float interpolationProgress) {
/*     */     int backgroundColor;
/* 257 */     Display.TextDisplay.TextRenderState renderState = state.textRenderState;
/* 258 */     byte flags = renderState.flags();
/* 260 */     boolean seeThrough = ((flags & 0x2) != 0);
/* 261 */     boolean useDefaultBackground = ((flags & 0x4) != 0);
/* 262 */     boolean shadow = ((flags & 0x1) != 0);
/* 263 */     Display.TextDisplay.Align alignment = Display.TextDisplay.getAlign(flags);
/* 264 */     byte textOpacity = (byte)renderState.textOpacity().get(interpolationProgress);
/* 266 */     if (useDefaultBackground) {
/* 267 */       float backgroundAlpha = ((Minecraft.getInstance()).gameRenderer.gameRenderState()).optionsRenderState.getBackgroundOpacity(0.25F);
/* 268 */       backgroundColor = (int)(backgroundAlpha * 255.0F) << 24;
/*     */     } else {
/* 270 */       backgroundColor = renderState.backgroundColor().get(interpolationProgress);
/*     */     } 
/* 273 */     float y = 0.0F;
/* 275 */     Matrix4f pose = poseStack.last().pose();
/* 277 */     pose.rotate(3.1415927F, 0.0F, 1.0F, 0.0F);
/* 278 */     pose.scale(-0.025F, -0.025F, -0.025F);
/* 279 */     Display.TextDisplay.CachedInfo cachedInfo = state.cachedInfo;
/* 281 */     int lineSpacing = 1;
/* 282 */     Objects.requireNonNull(this.font);
/* 282 */     int lineHeight = 9 + 1;
/* 284 */     int width = cachedInfo.width();
/* 285 */     int height = cachedInfo.lines().size() * lineHeight - 1;
/* 286 */     pose.translate(1.0F - width / 2.0F, -height, 0.0F);
/* 288 */     if (backgroundColor != 0)
/* 289 */       submitNodeCollector.submitCustomGeometry(poseStack, seeThrough ? RenderTypes.textBackgroundSeeThrough() : RenderTypes.textBackground(), (lambdaPose, buffer) -> {
/*     */             buffer.addVertex(lambdaPose, -1.0F, -1.0F, -0.01F).setColor(backgroundColor).setLight(lightCoords);
/*     */             buffer.addVertex(lambdaPose, -1.0F, height, -0.01F).setColor(backgroundColor).setLight(lightCoords);
/*     */             buffer.addVertex(lambdaPose, width, height, -0.01F).setColor(backgroundColor).setLight(lightCoords);
/*     */             buffer.addVertex(lambdaPose, width, -1.0F, -0.01F).setColor(backgroundColor).setLight(lightCoords);
/*     */           }); 
/* 297 */     OrderedSubmitNodeCollector textCollector = submitNodeCollector.order((backgroundColor != 0) ? 1 : 0);
/* 298 */     for (Display.TextDisplay.CachedLine line : cachedInfo.lines()) {
/* 299 */       switch (DisplayRenderer.null.$SwitchMap$net$minecraft$world$entity$Display$TextDisplay$Align[alignment.ordinal()]) {
/*     */         default:
/* 299 */           throw new MatchException(null, null);
/*     */         case 1:
/*     */         
/*     */         case 2:
/*     */         
/*     */         case 3:
/*     */           break;
/*     */       } 
/* 299 */       float offset = 
/*     */         
/* 302 */         width / 2.0F - line.width() / 2.0F;
/* 304 */       textCollector.submitText(poseStack, offset, y, line.contents(), shadow, seeThrough ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.POLYGON_OFFSET, lightCoords, textOpacity << 24 | 0xFFFFFF, 0, 0);
/* 305 */       y += lineHeight;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\DisplayRenderer$TextDisplayRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */