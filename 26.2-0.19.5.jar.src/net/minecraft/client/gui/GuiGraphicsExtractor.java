/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.platform.Window;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorType;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Renderable;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.render.TextureSetup;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
/*     */ import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
/*     */ import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
/*     */ import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.object.banner.BannerFlagModel;
/*     */ import net.minecraft.client.model.object.book.BookModel;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*     */ import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
/*     */ import net.minecraft.client.renderer.state.MapRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.BlitRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.ColoredRectangleRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.GuiItemRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.GuiRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.GuiTextRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.TiledBlitRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.pip.GuiBannerResultRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.pip.GuiBookModelRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.pip.GuiEntityRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.pip.GuiProfilerChartRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.pip.GuiSkinRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.metadata.gui.GuiMetadataSection;
/*     */ import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
/*     */ import net.minecraft.client.resources.model.sprite.AtlasManager;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteId;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.data.AtlasIds;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.ResultField;
/*     */ import net.minecraft.world.entity.ItemOwner;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*     */ import org.joml.Matrix3x2f;
/*     */ import org.joml.Matrix3x2fStack;
/*     */ import org.joml.Matrix3x2fc;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector2ic;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class GuiGraphicsExtractor {
/*     */   private static final int EXTRA_SPACE_AFTER_FIRST_TOOLTIP_LINE = 2;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final Matrix3x2fStack pose;
/*     */   
/*     */   private final ScissorStack scissorStack;
/*     */   
/*     */   private final SpriteGetter sprites;
/*     */   
/*     */   private final TextureAtlas guiSprites;
/*     */   
/*     */   private final GuiRenderState guiRenderState;
/*     */   
/*  98 */   private CursorType pendingCursor = CursorType.DEFAULT;
/*     */   
/*     */   private final int mouseX;
/*     */   
/*     */   private final int mouseY;
/*     */   
/*     */   private Runnable deferredTooltip;
/*     */   
/*     */   private Style hoveredTextStyle;
/*     */   
/*     */   private Style clickableTextStyle;
/*     */   
/*     */   private Renderable preeditOverlay;
/*     */   
/*     */   private GuiGraphicsExtractor(Minecraft minecraft, Matrix3x2fStack pose, GuiRenderState guiRenderState, int mouseX, int mouseY) {
/* 110 */     this.minecraft = minecraft;
/* 111 */     this.pose = pose;
/* 112 */     this.mouseX = mouseX;
/* 113 */     this.mouseY = mouseY;
/* 114 */     AtlasManager atlasManager = minecraft.getAtlasManager();
/* 115 */     this.sprites = (SpriteGetter)atlasManager;
/* 116 */     this.guiSprites = atlasManager.getAtlasOrThrow(AtlasIds.GUI);
/* 117 */     this.guiRenderState = guiRenderState;
/* 118 */     this.scissorStack = new ScissorStack(new ScreenRectangle(0, 0, guiWidth(), guiHeight()));
/*     */   }
/*     */   
/*     */   public GuiGraphicsExtractor(Minecraft minecraft, GuiRenderState guiRenderState, int mouseX, int mouseY) {
/* 122 */     this(minecraft, new Matrix3x2fStack(16), guiRenderState, mouseX, mouseY);
/*     */   }
/*     */   
/*     */   public void requestCursor(CursorType cursorType) {
/* 126 */     this.pendingCursor = cursorType;
/*     */   }
/*     */   
/*     */   public void applyCursor(Window window) {
/* 130 */     window.selectCursor(this.pendingCursor);
/*     */   }
/*     */   
/*     */   public int guiWidth() {
/* 134 */     return this.minecraft.getWindow().getGuiScaledWidth();
/*     */   }
/*     */   
/*     */   public int guiHeight() {
/* 138 */     return this.minecraft.getWindow().getGuiScaledHeight();
/*     */   }
/*     */   
/*     */   public Matrix3x2fStack pose() {
/* 142 */     return this.pose;
/*     */   }
/*     */   
/*     */   public void nextStratum() {
/* 146 */     this.guiRenderState.nextStratum();
/*     */   }
/*     */   
/*     */   public void blurBeforeThisStratum() {
/* 150 */     this.guiRenderState.blurBeforeThisStratum();
/*     */   }
/*     */   
/*     */   public void enableScissor(int x0, int y0, int x1, int y1) {
/* 155 */     ScreenRectangle rectangle = (new ScreenRectangle(x0, y0, x1 - x0, y1 - y0)).transformAxisAligned((Matrix3x2fc)this.pose);
/* 156 */     this.scissorStack.push(rectangle);
/*     */   }
/*     */   
/*     */   public void disableScissor() {
/* 160 */     this.scissorStack.pop();
/*     */   }
/*     */   
/*     */   public boolean containsPointInScissor(int x, int y) {
/* 164 */     return this.scissorStack.containsPoint(x, y);
/*     */   }
/*     */   
/*     */   public void horizontalLine(int x0, int x1, int y, int col) {
/* 168 */     if (x1 < x0) {
/* 169 */       int tmp = x0;
/* 170 */       x0 = x1;
/* 171 */       x1 = tmp;
/*     */     } 
/* 173 */     fill(x0, y, x1 + 1, y + 1, col);
/*     */   }
/*     */   
/*     */   public void verticalLine(int x, int y0, int y1, int col) {
/* 177 */     if (y1 < y0) {
/* 178 */       int tmp = y0;
/* 179 */       y0 = y1;
/* 180 */       y1 = tmp;
/*     */     } 
/* 182 */     fill(x, y0 + 1, x + 1, y1, col);
/*     */   }
/*     */   
/*     */   public void fill(int x0, int y0, int x1, int y1, int col) {
/* 186 */     fill(RenderPipelines.GUI, x0, y0, x1, y1, col);
/*     */   }
/*     */   
/*     */   public void fill(RenderPipeline pipeline, int x0, int y0, int x1, int y1, int col) {
/* 190 */     if (x0 < x1) {
/* 191 */       int tmp = x0;
/* 192 */       x0 = x1;
/* 193 */       x1 = tmp;
/*     */     } 
/* 195 */     if (y0 < y1) {
/* 196 */       int tmp = y0;
/* 197 */       y0 = y1;
/* 198 */       y1 = tmp;
/*     */     } 
/* 200 */     innerFill(pipeline, TextureSetup.noTexture(), x0, y0, x1, y1, col, null);
/*     */   }
/*     */   
/*     */   public void fillGradient(int x0, int y0, int x1, int y1, int col1, int col2) {
/* 204 */     innerFill(RenderPipelines.GUI, TextureSetup.noTexture(), x0, y0, x1, y1, col1, Integer.valueOf(col2));
/*     */   }
/*     */   
/*     */   public void fill(RenderPipeline renderPipeline, TextureSetup textureSetup, int x0, int y0, int x1, int y1) {
/* 208 */     innerFill(renderPipeline, textureSetup, x0, y0, x1, y1, -1, null);
/*     */   }
/*     */   
/*     */   public void outline(int x, int y, int width, int height, int color) {
/* 212 */     fill(x, y, x + width, y + 1, color);
/* 213 */     fill(x, y + height - 1, x + width, y + height, color);
/* 214 */     fill(x, y + 1, x + 1, y + height - 1, color);
/* 215 */     fill(x + width - 1, y + 1, x + width, y + height - 1, color);
/*     */   }
/*     */   
/*     */   private void innerFill(RenderPipeline renderPipeline, TextureSetup textureSetup, int x0, int y0, int x1, int y1, int color1, Integer color2) {
/* 219 */     this.guiRenderState.addGuiElement((GuiElementRenderState)new ColoredRectangleRenderState(renderPipeline, textureSetup, (Matrix3x2fc)new Matrix3x2f((Matrix3x2fc)this.pose), x0, y0, x1, y1, color1, 
/*     */           
/* 225 */           (color2 != null) ? color2.intValue() : color1, this.scissorStack
/* 226 */           .peek()));
/*     */   }
/*     */   
/*     */   public void textHighlight(int x0, int y0, int x1, int y1, boolean invertText) {
/* 231 */     if (invertText)
/* 232 */       fill(RenderPipelines.GUI_INVERT, x0, y0, x1, y1, -1); 
/* 234 */     fill(RenderPipelines.GUI_TEXT_HIGHLIGHT, x0, y0, x1, y1, -16776961);
/*     */   }
/*     */   
/*     */   public void text(Font font, String str, int x, int y, int color) {
/* 238 */     text(font, str, x, y, color, true);
/*     */   }
/*     */   
/*     */   public void text(Font font, String str, int x, int y, int color, boolean dropShadow) {
/* 242 */     if (str == null)
/*     */       return; 
/* 245 */     text(font, Language.getInstance().getVisualOrder(FormattedText.of(str)), x, y, color, dropShadow);
/*     */   }
/*     */   
/*     */   public void text(Font font, FormattedCharSequence str, int x, int y, int color) {
/* 249 */     text(font, str, x, y, color, true);
/*     */   }
/*     */   
/*     */   public void text(Font font, FormattedCharSequence str, int x, int y, int color, boolean dropShadow) {
/* 253 */     if (ARGB.alpha(color) == 0)
/*     */       return; 
/* 256 */     this.guiRenderState.addText(new GuiTextRenderState(font, str, (Matrix3x2fc)new Matrix3x2f((Matrix3x2fc)this.pose), x, y, color, 0, dropShadow, false, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   public void text(Font font, Component str, int x, int y, int color) {
/* 260 */     text(font, str, x, y, color, true);
/*     */   }
/*     */   
/*     */   public void text(Font font, Component str, int x, int y, int color, boolean dropShadow) {
/* 264 */     text(font, str.getVisualOrderText(), x, y, color, dropShadow);
/*     */   }
/*     */   
/*     */   public void centeredText(Font font, String str, int x, int y, int color) {
/* 268 */     text(font, str, x - font.width(str) / 2, y, color);
/*     */   }
/*     */   
/*     */   public void centeredText(Font font, Component text, int x, int y, int color) {
/* 272 */     FormattedCharSequence toRender = text.getVisualOrderText();
/* 273 */     text(font, toRender, x - font.width(toRender) / 2, y, color);
/*     */   }
/*     */   
/*     */   public void centeredText(Font font, FormattedCharSequence text, int x, int y, int color) {
/* 277 */     text(font, text, x - font.width(text) / 2, y, color);
/*     */   }
/*     */   
/*     */   public void textWithWordWrap(Font font, FormattedText string, int x, int y, int width, int col) {
/* 281 */     textWithWordWrap(font, string, x, y, width, col, true);
/*     */   }
/*     */   
/*     */   public void textWithWordWrap(Font font, FormattedText string, int x, int y, int width, int col, boolean dropShadow) {
/* 285 */     for (FormattedCharSequence line : font.split(string, width)) {
/* 286 */       text(font, line, x, y, col, dropShadow);
/* 287 */       Objects.requireNonNull(font);
/* 287 */       y += 9;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void textWithBackdrop(Font font, Component str, int textX, int textY, int textWidth, int textColor) {
/* 292 */     int backgroundColor = this.minecraft.options.getBackgroundColor(0.0F);
/* 293 */     if (backgroundColor != 0) {
/* 294 */       int padding = 2;
/* 295 */       Objects.requireNonNull(font);
/* 295 */       fill(textX - 2, textY - 2, textX + textWidth + 2, textY + 9 + 2, ARGB.multiply(backgroundColor, textColor));
/*     */     } 
/* 297 */     text(font, str, textX, textY, textColor, true);
/*     */   }
/*     */   
/*     */   public void blit(RenderPipeline renderPipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color) {
/* 301 */     blit(renderPipeline, texture, x, y, u, v, width, height, width, height, textureWidth, textureHeight, color);
/*     */   }
/*     */   
/*     */   public void blit(RenderPipeline renderPipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
/* 305 */     blit(renderPipeline, texture, x, y, u, v, width, height, width, height, textureWidth, textureHeight);
/*     */   }
/*     */   
/*     */   public void blit(RenderPipeline renderPipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int srcWidth, int srcHeight, int textureWidth, int textureHeight) {
/* 309 */     blit(renderPipeline, texture, x, y, u, v, width, height, srcWidth, srcHeight, textureWidth, textureHeight, -1);
/*     */   }
/*     */   
/*     */   public void blit(RenderPipeline renderPipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int srcWidth, int srcHeight, int textureWidth, int textureHeight, int color) {
/* 313 */     innerBlit(renderPipeline, texture, x, x + width, y, y + height, (u + 0.0F) / textureWidth, (u + srcWidth) / textureWidth, (v + 0.0F) / textureHeight, (v + srcHeight) / textureHeight, color);
/*     */   }
/*     */   
/*     */   public void blit(Identifier location, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1) {
/* 323 */     innerBlit(RenderPipelines.GUI_TEXTURED, location, x0, x1, y0, y1, u0, u1, v0, v1, -1);
/*     */   }
/*     */   
/*     */   public void blit(GpuTextureView textureView, GpuSampler sampler, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1) {
/* 327 */     innerBlit(RenderPipelines.GUI_TEXTURED, textureView, sampler, x0, y0, x1, y1, u0, u1, v0, v1, -1);
/*     */   }
/*     */   
/*     */   public void blitSprite(RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height) {
/* 331 */     blitSprite(renderPipeline, location, x, y, width, height, -1);
/*     */   }
/*     */   
/*     */   public void blitSprite(RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height, float alpha) {
/* 335 */     blitSprite(renderPipeline, location, x, y, width, height, ARGB.white(alpha));
/*     */   }
/*     */   
/*     */   public void blitSprite(RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height, int color) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield guiSprites : Lnet/minecraft/client/renderer/texture/TextureAtlas;
/*     */     //   4: aload_2
/*     */     //   5: invokevirtual getSprite : (Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;
/*     */     //   8: astore #8
/*     */     //   10: aload #8
/*     */     //   12: invokestatic getSpriteScaling : (Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling;
/*     */     //   15: astore #9
/*     */     //   17: aload #9
/*     */     //   19: dup
/*     */     //   20: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   23: pop
/*     */     //   24: astore #10
/*     */     //   26: iconst_0
/*     */     //   27: istore #11
/*     */     //   29: aload #10
/*     */     //   31: iload #11
/*     */     //   33: <illegal opcode> typeSwitch : (Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling;I)I
/*     */     //   38: tableswitch default -> 166, 0 -> 64, 1 -> 90, 2 -> 138
/*     */     //   64: aload #10
/*     */     //   66: checkcast net/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Stretch
/*     */     //   69: astore #12
/*     */     //   71: aload_0
/*     */     //   72: aload_1
/*     */     //   73: aload #8
/*     */     //   75: iload_3
/*     */     //   76: iload #4
/*     */     //   78: iload #5
/*     */     //   80: iload #6
/*     */     //   82: iload #7
/*     */     //   84: invokevirtual blitSprite : (Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;IIIII)V
/*     */     //   87: goto -> 166
/*     */     //   90: aload #10
/*     */     //   92: checkcast net/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile
/*     */     //   95: astore #13
/*     */     //   97: aload_0
/*     */     //   98: aload_1
/*     */     //   99: aload #8
/*     */     //   101: iload_3
/*     */     //   102: iload #4
/*     */     //   104: iload #5
/*     */     //   106: iload #6
/*     */     //   108: iconst_0
/*     */     //   109: iconst_0
/*     */     //   110: aload #13
/*     */     //   112: invokevirtual width : ()I
/*     */     //   115: aload #13
/*     */     //   117: invokevirtual height : ()I
/*     */     //   120: aload #13
/*     */     //   122: invokevirtual width : ()I
/*     */     //   125: aload #13
/*     */     //   127: invokevirtual height : ()I
/*     */     //   130: iload #7
/*     */     //   132: invokevirtual blitTiledSprite : (Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;IIIIIIIIIII)V
/*     */     //   135: goto -> 166
/*     */     //   138: aload #10
/*     */     //   140: checkcast net/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice
/*     */     //   143: astore #14
/*     */     //   145: aload_0
/*     */     //   146: aload_1
/*     */     //   147: aload #8
/*     */     //   149: aload #14
/*     */     //   151: iload_3
/*     */     //   152: iload #4
/*     */     //   154: iload #5
/*     */     //   156: iload #6
/*     */     //   158: iload #7
/*     */     //   160: invokevirtual blitNineSlicedSprite : (Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice;IIIII)V
/*     */     //   163: goto -> 166
/*     */     //   166: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #339	-> 0
/*     */     //   #340	-> 10
/*     */     //   #341	-> 17
/*     */     //   #342	-> 64
/*     */     //   #343	-> 71
/*     */     //   #344	-> 90
/*     */     //   #345	-> 97
/*     */     //   #346	-> 138
/*     */     //   #347	-> 145
/*     */     //   #351	-> 166
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   71	19	12	stretch	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Stretch;
/*     */     //   97	41	13	tile	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$Tile;
/*     */     //   145	21	14	nineSlice	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling$NineSlice;
/*     */     //   0	167	0	this	Lnet/minecraft/client/gui/GuiGraphicsExtractor;
/*     */     //   0	167	1	renderPipeline	Lcom/mojang/blaze3d/pipeline/RenderPipeline;
/*     */     //   0	167	2	location	Lnet/minecraft/resources/Identifier;
/*     */     //   0	167	3	x	I
/*     */     //   0	167	4	y	I
/*     */     //   0	167	5	width	I
/*     */     //   0	167	6	height	I
/*     */     //   0	167	7	color	I
/*     */     //   10	157	8	sprite	Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;
/*     */     //   17	150	9	scaling	Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling;
/*     */   }
/*     */   
/*     */   public void blitSprite(RenderPipeline renderPipeline, Identifier location, int spriteWidth, int spriteHeight, int textureX, int textureY, int x, int y, int width, int height) {
/* 354 */     blitSprite(renderPipeline, location, spriteWidth, spriteHeight, textureX, textureY, x, y, width, height, -1);
/*     */   }
/*     */   
/*     */   public void blitSprite(RenderPipeline renderPipeline, Identifier location, int spriteWidth, int spriteHeight, int textureX, int textureY, int x, int y, int width, int height, int color) {
/* 358 */     TextureAtlasSprite sprite = this.guiSprites.getSprite(location);
/* 359 */     GuiSpriteScaling scaling = getSpriteScaling(sprite);
/* 360 */     if (scaling instanceof GuiSpriteScaling.Stretch) {
/* 361 */       blitSprite(renderPipeline, sprite, spriteWidth, spriteHeight, textureX, textureY, x, y, width, height, color);
/*     */     } else {
/* 364 */       enableScissor(x, y, x + width, y + height);
/* 365 */       blitSprite(renderPipeline, location, x - textureX, y - textureY, spriteWidth, spriteHeight, color);
/* 366 */       disableScissor();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void blitSprite(RenderPipeline renderPipeline, TextureAtlasSprite sprite, int x, int y, int width, int height) {
/* 371 */     blitSprite(renderPipeline, sprite, x, y, width, height, -1);
/*     */   }
/*     */   
/*     */   public void blitSprite(RenderPipeline renderPipeline, TextureAtlasSprite sprite, int x, int y, int width, int height, int color) {
/* 375 */     if (width == 0 || height == 0)
/*     */       return; 
/* 378 */     innerBlit(renderPipeline, sprite
/* 379 */         .atlasLocation(), x, x + width, y, y + height, sprite
/*     */         
/* 382 */         .getU0(), sprite.getU1(), sprite
/* 383 */         .getV0(), sprite.getV1(), color);
/*     */   }
/*     */   
/*     */   private void blitSprite(RenderPipeline renderPipeline, TextureAtlasSprite sprite, int spriteWidth, int spriteHeight, int textureX, int textureY, int x, int y, int width, int height, int color) {
/* 389 */     if (width == 0 || height == 0)
/*     */       return; 
/* 392 */     innerBlit(renderPipeline, sprite
/* 393 */         .atlasLocation(), x, x + width, y, y + height, sprite
/*     */         
/* 396 */         .getU(textureX / spriteWidth), sprite
/* 397 */         .getU((textureX + width) / spriteWidth), sprite
/* 398 */         .getV(textureY / spriteHeight), sprite
/* 399 */         .getV((textureY + height) / spriteHeight), color);
/*     */   }
/*     */   
/*     */   private void blitNineSlicedSprite(RenderPipeline renderPipeline, TextureAtlasSprite sprite, GuiSpriteScaling.NineSlice nineSlice, int x, int y, int width, int height, int color) {
/* 405 */     GuiSpriteScaling.NineSlice.Border border = nineSlice.border();
/* 406 */     int borderLeft = Math.min(border.left(), width / 2);
/* 407 */     int borderRight = Math.min(border.right(), width / 2);
/* 408 */     int borderTop = Math.min(border.top(), height / 2);
/* 409 */     int borderBottom = Math.min(border.bottom(), height / 2);
/* 410 */     if (width == nineSlice.width() && height == nineSlice.height()) {
/* 411 */       blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, width, height, color);
/*     */       return;
/*     */     } 
/* 413 */     if (height == nineSlice.height()) {
/* 414 */       blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, borderLeft, height, color);
/* 415 */       blitNineSliceInnerSegment(renderPipeline, nineSlice, sprite, x + borderLeft, y, width - borderRight - borderLeft, height, borderLeft, 0, nineSlice.width() - borderRight - borderLeft, nineSlice.height(), nineSlice.width(), nineSlice.height(), color);
/* 416 */       blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - borderRight, 0, x + width - borderRight, y, borderRight, height, color);
/*     */       return;
/*     */     } 
/* 418 */     if (width == nineSlice.width()) {
/* 419 */       blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, width, borderTop, color);
/* 420 */       blitNineSliceInnerSegment(renderPipeline, nineSlice, sprite, x, y + borderTop, width, height - borderBottom - borderTop, 0, borderTop, nineSlice.width(), nineSlice.height() - borderBottom - borderTop, nineSlice.width(), nineSlice.height(), color);
/* 421 */       blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), 0, nineSlice.height() - borderBottom, x, y + height - borderBottom, width, borderBottom, color);
/*     */       return;
/*     */     } 
/* 426 */     blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), 0, 0, x, y, borderLeft, borderTop, color);
/* 428 */     blitNineSliceInnerSegment(renderPipeline, nineSlice, sprite, x + borderLeft, y, width - borderRight - borderLeft, borderTop, borderLeft, 0, nineSlice.width() - borderRight - borderLeft, borderTop, nineSlice.width(), nineSlice.height(), color);
/* 430 */     blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - borderRight, 0, x + width - borderRight, y, borderRight, borderTop, color);
/* 432 */     blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), 0, nineSlice.height() - borderBottom, x, y + height - borderBottom, borderLeft, borderBottom, color);
/* 434 */     blitNineSliceInnerSegment(renderPipeline, nineSlice, sprite, x + borderLeft, y + height - borderBottom, width - borderRight - borderLeft, borderBottom, borderLeft, nineSlice.height() - borderBottom, nineSlice.width() - borderRight - borderLeft, borderBottom, nineSlice.width(), nineSlice.height(), color);
/* 436 */     blitSprite(renderPipeline, sprite, nineSlice.width(), nineSlice.height(), nineSlice.width() - borderRight, nineSlice.height() - borderBottom, x + width - borderRight, y + height - borderBottom, borderRight, borderBottom, color);
/* 438 */     blitNineSliceInnerSegment(renderPipeline, nineSlice, sprite, x, y + borderTop, borderLeft, height - borderBottom - borderTop, 0, borderTop, borderLeft, nineSlice.height() - borderBottom - borderTop, nineSlice.width(), nineSlice.height(), color);
/* 440 */     blitNineSliceInnerSegment(renderPipeline, nineSlice, sprite, x + borderLeft, y + borderTop, width - borderRight - borderLeft, height - borderBottom - borderTop, borderLeft, borderTop, nineSlice.width() - borderRight - borderLeft, nineSlice.height() - borderBottom - borderTop, nineSlice.width(), nineSlice.height(), color);
/* 442 */     blitNineSliceInnerSegment(renderPipeline, nineSlice, sprite, x + width - borderRight, y + borderTop, borderRight, height - borderBottom - borderTop, nineSlice.width() - borderRight, borderTop, borderRight, nineSlice.height() - borderBottom - borderTop, nineSlice.width(), nineSlice.height(), color);
/*     */   }
/*     */   
/*     */   private void blitNineSliceInnerSegment(RenderPipeline renderPipeline, GuiSpriteScaling.NineSlice nineSlice, TextureAtlasSprite sprite, int x, int y, int width, int height, int textureX, int textureY, int textureWidth, int textureHeight, int spriteWidth, int spriteHeight, int color) {
/* 446 */     if (width <= 0 || height <= 0)
/*     */       return; 
/* 449 */     if (nineSlice.stretchInner()) {
/* 450 */       innerBlit(renderPipeline, sprite
/* 451 */           .atlasLocation(), x, x + width, y, y + height, sprite
/*     */           
/* 454 */           .getU(textureX / spriteWidth), sprite
/* 455 */           .getU((textureX + textureWidth) / spriteWidth), sprite
/* 456 */           .getV(textureY / spriteHeight), sprite
/* 457 */           .getV((textureY + textureHeight) / spriteHeight), color);
/*     */     } else {
/* 461 */       blitTiledSprite(renderPipeline, sprite, x, y, width, height, textureX, textureY, textureWidth, textureHeight, spriteWidth, spriteHeight, color);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void blitTiledSprite(RenderPipeline renderPipeline, TextureAtlasSprite sprite, int x, int y, int width, int height, int textureX, int textureY, int tileWidth, int tileHeight, int spriteWidth, int spriteHeight, int color) {
/* 466 */     if (width <= 0 || height <= 0)
/*     */       return; 
/* 469 */     if (tileWidth <= 0 || tileHeight <= 0)
/* 470 */       throw new IllegalArgumentException("Tile size must be positive, got " + tileWidth + "x" + tileHeight); 
/* 472 */     AbstractTexture spriteTexture = this.minecraft.getTextureManager().getTexture(sprite.atlasLocation());
/* 473 */     GpuTextureView texture = spriteTexture.getTextureView();
/* 474 */     innerTiledBlit(renderPipeline, texture, spriteTexture.getSampler(), tileWidth, tileHeight, x, y, x + width, y + height, sprite
/*     */         
/* 477 */         .getU(textureX / spriteWidth), sprite
/* 478 */         .getU((textureX + tileWidth) / spriteWidth), sprite
/* 479 */         .getV(textureY / spriteHeight), sprite
/* 480 */         .getV((textureY + tileHeight) / spriteHeight), color);
/*     */   }
/*     */   
/*     */   private void innerBlit(RenderPipeline renderPipeline, Identifier location, int x0, int x1, int y0, int y1, float u0, float u1, float v0, float v1, int color) {
/* 485 */     AbstractTexture texture = this.minecraft.getTextureManager().getTexture(location);
/* 486 */     innerBlit(renderPipeline, texture.getTextureView(), texture.getSampler(), x0, y0, x1, y1, u0, u1, v0, v1, color);
/*     */   }
/*     */   
/*     */   private void innerBlit(RenderPipeline pipeline, GpuTextureView textureView, GpuSampler sampler, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1, int color) {
/* 490 */     this.guiRenderState.addGuiElement((GuiElementRenderState)new BlitRenderState(pipeline, TextureSetup.singleTexture(textureView, sampler), new Matrix3x2f((Matrix3x2fc)this.pose), x0, y0, x1, y1, u0, u1, v0, v1, color, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   private void innerTiledBlit(RenderPipeline pipeline, GpuTextureView textureView, GpuSampler sampler, int tileWidth, int tileHeight, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1, int color) {
/* 494 */     this.guiRenderState.addGuiElement((GuiElementRenderState)new TiledBlitRenderState(pipeline, TextureSetup.singleTexture(textureView, sampler), new Matrix3x2f((Matrix3x2fc)this.pose), tileWidth, tileHeight, x0, y0, x1, y1, u0, u1, v0, v1, color, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   private static GuiSpriteScaling getSpriteScaling(TextureAtlasSprite sprite) {
/* 498 */     return ((GuiMetadataSection)sprite.contents().getAdditionalMetadata(GuiMetadataSection.TYPE).orElse(GuiMetadataSection.DEFAULT)).scaling();
/*     */   }
/*     */   
/*     */   public void item(ItemStack itemStack, int x, int y) {
/* 502 */     item((LivingEntity)this.minecraft.player, (Level)this.minecraft.level, itemStack, x, y, 0);
/*     */   }
/*     */   
/*     */   public void item(ItemStack itemStack, int x, int y, int seed) {
/* 506 */     item((LivingEntity)this.minecraft.player, (Level)this.minecraft.level, itemStack, x, y, seed);
/*     */   }
/*     */   
/*     */   public void item(LivingEntity owner, ItemStack itemStack, int x, int y, int seed) {
/* 510 */     item(owner, owner.level(), itemStack, x, y, seed);
/*     */   }
/*     */   
/*     */   private void item(LivingEntity owner, Level level, ItemStack itemStack, int x, int y, int seed) {
/* 514 */     if (itemStack.isEmpty())
/*     */       return; 
/* 518 */     TrackingItemStackRenderState itemStackRenderState = new TrackingItemStackRenderState();
/* 519 */     this.minecraft.getItemModelResolver().updateForTopItem((ItemStackRenderState)itemStackRenderState, itemStack, ItemDisplayContext.GUI, level, (ItemOwner)owner, seed);
/*     */     try {
/* 521 */       this.guiRenderState.addItem(new GuiItemRenderState(new Matrix3x2f((Matrix3x2fc)this.pose), itemStackRenderState, x, y, this.scissorStack
/*     */             
/* 525 */             .peek()));
/* 527 */     } catch (Throwable t) {
/* 528 */       CrashReport report = CrashReport.forThrowable(t, "Rendering item");
/* 529 */       CrashReportCategory category = report.addCategory("Item being rendered");
/* 531 */       category.setDetail("Item Type", () -> String.valueOf(itemStack.getItem()));
/* 532 */       category.setDetail("Item Components", () -> String.valueOf(itemStack.getComponents()));
/* 533 */       category.setDetail("Item Foil", () -> String.valueOf(itemStack.hasFoil()));
/* 535 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void fakeItem(ItemStack itemStack, int x, int y) {
/* 540 */     fakeItem(itemStack, x, y, 0);
/*     */   }
/*     */   
/*     */   public void fakeItem(ItemStack itemStack, int x, int y, int seed) {
/* 544 */     item(null, (Level)this.minecraft.level, itemStack, x, y, seed);
/*     */   }
/*     */   
/*     */   public void itemDecorations(Font font, ItemStack itemStack, int x, int y) {
/* 548 */     itemDecorations(font, itemStack, x, y, null);
/*     */   }
/*     */   
/*     */   public void itemDecorations(Font font, ItemStack itemStack, int x, int y, String countText) {
/* 552 */     if (itemStack.isEmpty())
/*     */       return; 
/* 555 */     this.pose.pushMatrix();
/* 556 */     itemBar(itemStack, x, y);
/* 557 */     itemCooldown(itemStack, x, y);
/* 558 */     itemCount(font, itemStack, x, y, countText);
/* 559 */     this.pose.popMatrix();
/*     */   }
/*     */   
/*     */   private void itemBar(ItemStack itemStack, int x, int y) {
/* 563 */     if (itemStack.isBarVisible()) {
/* 564 */       int left = x + 2;
/* 565 */       int top = y + 13;
/* 566 */       fill(RenderPipelines.GUI, left, top, left + 13, top + 2, -16777216);
/* 567 */       fill(RenderPipelines.GUI, left, top, left + itemStack.getBarWidth(), top + 1, ARGB.opaque(itemStack.getBarColor()));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void itemCount(Font font, ItemStack itemStack, int x, int y, String countText) {
/* 572 */     if (itemStack.getCount() != 1 || countText != null) {
/* 573 */       String amount = (countText == null) ? String.valueOf(itemStack.getCount()) : countText;
/* 574 */       text(font, amount, x + 19 - 2 - font.width(amount), y + 6 + 3, -1, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void itemCooldown(ItemStack itemStack, int x, int y) {
/* 579 */     LocalPlayer player = this.minecraft.player;
/* 580 */     float cooldown = (player == null) ? 0.0F : player.getCooldowns().getCooldownPercent(itemStack, this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true));
/* 581 */     if (cooldown > 0.0F) {
/* 582 */       int top = y + Mth.floor(16.0F * (1.0F - cooldown));
/* 583 */       int bottom = top + Mth.ceil(16.0F * cooldown);
/* 584 */       fill(RenderPipelines.GUI, x, top, x + 16, bottom, 2147483647);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void map(MapRenderState mapRenderState) {
/* 589 */     Minecraft minecraft = Minecraft.getInstance();
/* 590 */     TextureManager textureManager = minecraft.getTextureManager();
/* 591 */     AbstractTexture texture = textureManager.getTexture(mapRenderState.texture);
/* 593 */     innerBlit(RenderPipelines.GUI_TEXTURED, texture.getTextureView(), texture.getSampler(), 0, 0, 128, 128, 0.0F, 1.0F, 0.0F, 1.0F, -1);
/* 595 */     for (MapRenderState.MapDecorationRenderState decoration : mapRenderState.decorations) {
/* 596 */       if (!decoration.renderOnFrame)
/*     */         continue; 
/* 599 */       this.pose.pushMatrix();
/* 600 */       this.pose.translate(decoration.x / 2.0F + 64.0F, decoration.y / 2.0F + 64.0F);
/* 601 */       this.pose.rotate(0.017453292F * decoration.rot * 360.0F / 16.0F);
/* 602 */       this.pose.scale(4.0F, 4.0F);
/* 603 */       this.pose.translate(-0.125F, 0.125F);
/* 605 */       TextureAtlasSprite atlasSprite = decoration.atlasSprite;
/* 606 */       if (atlasSprite != null) {
/* 607 */         AbstractTexture decorationTexture = textureManager.getTexture(atlasSprite.atlasLocation());
/* 608 */         innerBlit(RenderPipelines.GUI_TEXTURED, decorationTexture.getTextureView(), decorationTexture.getSampler(), -1, -1, 1, 1, atlasSprite.getU0(), atlasSprite.getU1(), atlasSprite.getV1(), atlasSprite.getV0(), -1);
/*     */       } 
/* 610 */       this.pose.popMatrix();
/* 612 */       if (decoration.name != null) {
/* 613 */         Font font = minecraft.font;
/* 614 */         float width = font.width((FormattedText)decoration.name);
/* 615 */         Objects.requireNonNull(font);
/* 615 */         float scale = Mth.clamp(25.0F / width, 0.0F, 6.0F / 9.0F);
/* 617 */         this.pose.pushMatrix();
/* 618 */         this.pose.translate(decoration.x / 2.0F + 64.0F - width * scale / 2.0F, decoration.y / 2.0F + 64.0F + 4.0F);
/* 619 */         this.pose.scale(scale, scale);
/* 620 */         this.guiRenderState.addText(new GuiTextRenderState(font, decoration.name.getVisualOrderText(), (Matrix3x2fc)new Matrix3x2f((Matrix3x2fc)this.pose), 0, 0, -1, -2147483648, false, false, this.scissorStack.peek()));
/* 621 */         this.pose.popMatrix();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void entity(EntityRenderState renderState, float scale, Vector3fc translation, Quaternionfc rotation, Quaternionfc overrideCameraAngle, int x0, int y0, int x1, int y1) {
/* 627 */     renderState.lightCoords = 15728880;
/* 628 */     this.guiRenderState.addPicturesInPictureState((PictureInPictureRenderState)new GuiEntityRenderState(renderState, translation, rotation, overrideCameraAngle, x0, y0, x1, y1, scale, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   public void skin(Model.Simple playerModel, Identifier texture, float scale, float rotationX, float rotationY, float pivotY, int x0, int y0, int x1, int y1) {
/* 632 */     this.guiRenderState.addPicturesInPictureState((PictureInPictureRenderState)new GuiSkinRenderState(playerModel, texture, rotationX, rotationY, pivotY, x0, y0, x1, y1, scale, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   public void book(BookModel bookModel, Identifier texture, float scale, float open, float flip, int x0, int y0, int x1, int y1) {
/* 636 */     this.guiRenderState.addPicturesInPictureState((PictureInPictureRenderState)new GuiBookModelRenderState(bookModel, texture, open, flip, x0, y0, x1, y1, scale, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   public void bannerPattern(BannerFlagModel flag, DyeColor baseColor, BannerPatternLayers resultBannerPatterns, int x0, int y0, int x1, int y1) {
/* 640 */     this.guiRenderState.addPicturesInPictureState((PictureInPictureRenderState)new GuiBannerResultRenderState(flag, baseColor, resultBannerPatterns, x0, y0, x1, y1, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   public void profilerChart(List<ResultField> chartData, int x0, int y0, int x1, int y1) {
/* 644 */     this.guiRenderState.addPicturesInPictureState((PictureInPictureRenderState)new GuiProfilerChartRenderState(chartData, x0, y0, x1, y1, this.scissorStack.peek()));
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Component component, int x, int y) {
/* 648 */     setTooltipForNextFrame(List.of(component.getVisualOrderText()), x, y);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(List<FormattedCharSequence> formattedCharSequences, int x, int y) {
/* 652 */     setTooltipForNextFrame(this.minecraft.font, formattedCharSequences, DefaultTooltipPositioner.INSTANCE, x, y, false);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, ItemStack itemStack, int xo, int yo) {
/* 656 */     setTooltipForNextFrame(font, Screen.getTooltipFromItem(this.minecraft, itemStack), itemStack.getTooltipImage(), xo, yo, (Identifier)itemStack.get(DataComponents.TOOLTIP_STYLE));
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, List<Component> texts, Optional<TooltipComponent> optionalImage, int xo, int yo) {
/* 660 */     setTooltipForNextFrame(font, texts, optionalImage, xo, yo, (Identifier)null);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, List<Component> texts, Optional<TooltipComponent> optionalImage, int xo, int yo, Identifier style) {
/* 667 */     List<ClientTooltipComponent> components = (List<ClientTooltipComponent>)texts.stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).collect(Util.toMutableList());
/* 668 */     optionalImage.ifPresent(image -> components.add(components.isEmpty() ? 0 : 1, ClientTooltipComponent.create(image)));
/* 669 */     setTooltipForNextFrameInternal(font, components, xo, yo, DefaultTooltipPositioner.INSTANCE, style, false);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, List<FormattedCharSequence> tooltip, Optional<TooltipComponent> component, ClientTooltipPositioner positioner, int xo, int yo, boolean replaceExisting, Identifier style) {
/* 673 */     List<ClientTooltipComponent> components = (List<ClientTooltipComponent>)tooltip.stream().map(ClientTooltipComponent::create).collect(Collectors.toList());
/* 674 */     component.ifPresent(tooltipComponent -> components.add(components.isEmpty() ? 0 : 1, ClientTooltipComponent.create(tooltipComponent)));
/* 675 */     setTooltipForNextFrameInternal(font, components, xo, yo, positioner, style, replaceExisting);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, Component text, int xo, int yo) {
/* 679 */     setTooltipForNextFrame(font, text, xo, yo, (Identifier)null);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, Component text, int xo, int yo, Identifier style) {
/* 683 */     setTooltipForNextFrame(font, List.of(text.getVisualOrderText()), xo, yo, style);
/*     */   }
/*     */   
/*     */   public void setComponentTooltipForNextFrame(Font font, List<Component> lines, int xo, int yo) {
/* 687 */     setComponentTooltipForNextFrame(font, lines, xo, yo, null);
/*     */   }
/*     */   
/*     */   public void setComponentTooltipForNextFrame(Font font, List<Component> lines, int xo, int yo, Identifier style) {
/* 691 */     setTooltipForNextFrameInternal(font, lines.stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).toList(), xo, yo, DefaultTooltipPositioner.INSTANCE, style, false);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, List<? extends FormattedCharSequence> lines, int xo, int yo) {
/* 695 */     setTooltipForNextFrame(font, lines, xo, yo, (Identifier)null);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, List<? extends FormattedCharSequence> lines, int xo, int yo, Identifier style) {
/* 699 */     setTooltipForNextFrameInternal(font, (List<ClientTooltipComponent>)lines.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), xo, yo, DefaultTooltipPositioner.INSTANCE, style, false);
/*     */   }
/*     */   
/*     */   public void setTooltipForNextFrame(Font font, List<FormattedCharSequence> tooltip, ClientTooltipPositioner positioner, int xo, int yo, boolean replaceExisting) {
/* 703 */     setTooltipForNextFrameInternal(font, (List<ClientTooltipComponent>)tooltip.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), xo, yo, positioner, null, replaceExisting);
/*     */   }
/*     */   
/*     */   private void setTooltipForNextFrameInternal(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, Identifier style, boolean replaceExisting) {
/* 707 */     if (lines.isEmpty())
/*     */       return; 
/* 710 */     if (this.deferredTooltip == null || replaceExisting)
/* 711 */       this.deferredTooltip = (() -> tooltip(font, lines, xo, yo, positioner, style)); 
/*     */   }
/*     */   
/*     */   public void tooltip(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, Identifier style) {
/* 717 */     int textWidth = 0;
/* 718 */     int tempHeight = (lines.size() == 1) ? -2 : 0;
/* 719 */     for (ClientTooltipComponent line : lines) {
/* 720 */       int lineWidth = line.getWidth(font);
/* 721 */       if (lineWidth > textWidth)
/* 722 */         textWidth = lineWidth; 
/* 724 */       tempHeight += line.getHeight(font);
/*     */     } 
/* 727 */     int w = textWidth;
/* 728 */     int h = tempHeight;
/* 730 */     Vector2ic positionedTooltip = positioner.positionTooltip(guiWidth(), guiHeight(), xo, yo, w, h);
/* 731 */     int x = positionedTooltip.x();
/* 732 */     int y = positionedTooltip.y();
/* 734 */     this.pose.pushMatrix();
/* 736 */     TooltipRenderUtil.extractTooltipBackground(this, x, y, w, h, style);
/* 739 */     int localY = y;
/*     */     int i;
/* 740 */     for (i = 0; i < lines.size(); i++) {
/* 741 */       ClientTooltipComponent line = lines.get(i);
/* 742 */       line.extractText(this, font, x, localY);
/* 743 */       localY += line.getHeight(font) + ((i == 0) ? 2 : 0);
/*     */     } 
/* 747 */     localY = y;
/* 748 */     for (i = 0; i < lines.size(); i++) {
/* 749 */       ClientTooltipComponent line = lines.get(i);
/* 750 */       line.extractImage(font, x, localY, w, h, this);
/* 751 */       localY += line.getHeight(font) + ((i == 0) ? 2 : 0);
/*     */     } 
/* 754 */     this.pose.popMatrix();
/*     */   }
/*     */   
/*     */   public void setPreeditOverlay(Renderable preeditOverlay) {
/* 758 */     this.preeditOverlay = preeditOverlay;
/*     */   }
/*     */   
/*     */   public void extractDeferredElements(int mouseX, int mouseY, float a) {
/* 762 */     if (this.hoveredTextStyle != null)
/* 763 */       componentHoverEffect(this.minecraft.font, this.hoveredTextStyle, mouseX, mouseY); 
/* 766 */     if (this.clickableTextStyle != null && this.clickableTextStyle.getClickEvent() != null)
/* 767 */       requestCursor(CursorTypes.POINTING_HAND); 
/* 770 */     if (this.preeditOverlay != null) {
/* 771 */       nextStratum();
/* 772 */       this.preeditOverlay.extractRenderState(this, mouseX, mouseY, a);
/*     */     } 
/* 775 */     if (this.deferredTooltip != null) {
/* 776 */       nextStratum();
/* 777 */       this.deferredTooltip.run();
/* 778 */       this.deferredTooltip = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void componentHoverEffect(Font font, Style hoveredStyle, int xMouse, int yMouse) {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: invokevirtual getHoverEvent : ()Lnet/minecraft/network/chat/HoverEvent;
/*     */     //   4: ifnull -> 204
/*     */     //   7: aload_2
/*     */     //   8: invokevirtual getHoverEvent : ()Lnet/minecraft/network/chat/HoverEvent;
/*     */     //   11: dup
/*     */     //   12: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   15: pop
/*     */     //   16: astore #5
/*     */     //   18: iconst_0
/*     */     //   19: istore #6
/*     */     //   21: aload #5
/*     */     //   23: iload #6
/*     */     //   25: <illegal opcode> typeSwitch : (Lnet/minecraft/network/chat/HoverEvent;I)I
/*     */     //   30: tableswitch default -> 184, 0 -> 56, 1 -> 90, 2 -> 137
/*     */     //   56: aload #5
/*     */     //   58: checkcast net/minecraft/network/chat/HoverEvent$ShowItem
/*     */     //   61: astore #7
/*     */     //   63: aload #7
/*     */     //   65: invokevirtual item : ()Lnet/minecraft/world/item/ItemStackTemplate;
/*     */     //   68: astore #9
/*     */     //   70: aload #9
/*     */     //   72: astore #8
/*     */     //   74: aload_0
/*     */     //   75: aload_1
/*     */     //   76: aload #8
/*     */     //   78: invokevirtual create : ()Lnet/minecraft/world/item/ItemStack;
/*     */     //   81: iload_3
/*     */     //   82: iload #4
/*     */     //   84: invokevirtual setTooltipForNextFrame : (Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V
/*     */     //   87: goto -> 184
/*     */     //   90: aload #5
/*     */     //   92: checkcast net/minecraft/network/chat/HoverEvent$ShowEntity
/*     */     //   95: astore #9
/*     */     //   97: aload #9
/*     */     //   99: invokevirtual entity : ()Lnet/minecraft/network/chat/HoverEvent$EntityTooltipInfo;
/*     */     //   102: astore #11
/*     */     //   104: aload #11
/*     */     //   106: astore #10
/*     */     //   108: aload_0
/*     */     //   109: getfield minecraft : Lnet/minecraft/client/Minecraft;
/*     */     //   112: getfield options : Lnet/minecraft/client/Options;
/*     */     //   115: getfield advancedItemTooltips : Z
/*     */     //   118: ifeq -> 184
/*     */     //   121: aload_0
/*     */     //   122: aload_1
/*     */     //   123: aload #10
/*     */     //   125: invokevirtual getTooltipLines : ()Ljava/util/List;
/*     */     //   128: iload_3
/*     */     //   129: iload #4
/*     */     //   131: invokevirtual setComponentTooltipForNextFrame : (Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V
/*     */     //   134: goto -> 184
/*     */     //   137: aload #5
/*     */     //   139: checkcast net/minecraft/network/chat/HoverEvent$ShowText
/*     */     //   142: astore #11
/*     */     //   144: aload #11
/*     */     //   146: invokevirtual value : ()Lnet/minecraft/network/chat/Component;
/*     */     //   149: astore #13
/*     */     //   151: aload #13
/*     */     //   153: astore #12
/*     */     //   155: aload_0
/*     */     //   156: aload_1
/*     */     //   157: aload_1
/*     */     //   158: aload #12
/*     */     //   160: aload_0
/*     */     //   161: invokevirtual guiWidth : ()I
/*     */     //   164: iconst_2
/*     */     //   165: idiv
/*     */     //   166: sipush #200
/*     */     //   169: invokestatic max : (II)I
/*     */     //   172: invokevirtual split : (Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;
/*     */     //   175: iload_3
/*     */     //   176: iload #4
/*     */     //   178: invokevirtual setTooltipForNextFrame : (Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V
/*     */     //   181: goto -> 184
/*     */     //   184: goto -> 204
/*     */     //   187: astore #5
/*     */     //   189: new java/lang/MatchException
/*     */     //   192: dup
/*     */     //   193: aload #5
/*     */     //   195: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   198: aload #5
/*     */     //   200: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   203: athrow
/*     */     //   204: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #783	-> 0
/*     */     //   #784	-> 7
/*     */     //   #785	-> 56
/*     */     //   #786	-> 74
/*     */     //   #787	-> 90
/*     */     //   #788	-> 108
/*     */     //   #789	-> 121
/*     */     //   #792	-> 137
/*     */     //   #793	-> 155
/*     */     //   #795	-> 184
/*     */     //   #792	-> 187
/*     */     //   #798	-> 204
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   74	16	8	item	Lnet/minecraft/world/item/ItemStackTemplate;
/*     */     //   108	29	10	entity	Lnet/minecraft/network/chat/HoverEvent$EntityTooltipInfo;
/*     */     //   155	29	12	text	Lnet/minecraft/network/chat/Component;
/*     */     //   0	205	0	this	Lnet/minecraft/client/gui/GuiGraphicsExtractor;
/*     */     //   0	205	1	font	Lnet/minecraft/client/gui/Font;
/*     */     //   0	205	2	hoveredStyle	Lnet/minecraft/network/chat/Style;
/*     */     //   0	205	3	xMouse	I
/*     */     //   0	205	4	yMouse	I
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   65	68	187	java/lang/Throwable
/*     */     //   99	102	187	java/lang/Throwable
/*     */     //   146	149	187	java/lang/Throwable
/*     */   }
/*     */   
/*     */   public TextureAtlasSprite getSprite(SpriteId sprite) {
/* 801 */     return this.sprites.get(sprite);
/*     */   }
/*     */   
/*     */   public ActiveTextCollector textRendererForWidget(AbstractWidget owner, HoveredTextEffects hoveredTextEffects) {
/* 805 */     return new RenderingTextCollector(createDefaultTextParameters(owner.getAlpha()), hoveredTextEffects, null);
/*     */   }
/*     */   
/*     */   public ActiveTextCollector textRenderer() {
/* 810 */     return textRenderer(HoveredTextEffects.TOOLTIP_ONLY);
/*     */   }
/*     */   
/*     */   public ActiveTextCollector textRenderer(HoveredTextEffects hoveredTextEffects) {
/* 814 */     return textRenderer(hoveredTextEffects, null);
/*     */   }
/*     */   
/*     */   public ActiveTextCollector textRenderer(HoveredTextEffects hoveredTextEffects, Consumer<Style> additionalHoverStyleConsumer) {
/* 818 */     return new RenderingTextCollector(createDefaultTextParameters(1.0F), hoveredTextEffects, additionalHoverStyleConsumer);
/*     */   }
/*     */   
/*     */   private ActiveTextCollector.Parameters createDefaultTextParameters(float opacity) {
/* 823 */     return new ActiveTextCollector.Parameters((Matrix3x2fc)new Matrix3x2f((Matrix3x2fc)this.pose), opacity, this.scissorStack.peek());
/*     */   }
/*     */   
/*     */   private static class ScissorStack {
/*     */     private final ScreenRectangle screenSize;
/*     */     
/* 828 */     private final Deque<ScreenRectangle> stack = new ArrayDeque<>();
/*     */     
/*     */     private ScissorStack(ScreenRectangle screenSize) {
/* 831 */       this.screenSize = screenSize;
/*     */     }
/*     */     
/*     */     public void push(ScreenRectangle rectangle) {
/* 835 */       ScreenRectangle lastRectangle = Objects.<ScreenRectangle>requireNonNullElse(this.stack.peekLast(), this.screenSize);
/* 836 */       ScreenRectangle intersection = Objects.<ScreenRectangle>requireNonNullElse(rectangle.intersection(lastRectangle), ScreenRectangle.empty());
/* 837 */       this.stack.addLast(intersection);
/*     */     }
/*     */     
/*     */     public void pop() {
/* 841 */       if (this.stack.isEmpty())
/* 842 */         throw new IllegalStateException("Scissor stack underflow"); 
/* 844 */       this.stack.removeLast();
/*     */     }
/*     */     
/*     */     public ScreenRectangle peek() {
/* 848 */       return this.stack.peekLast();
/*     */     }
/*     */     
/*     */     public boolean containsPoint(int x, int y) {
/* 852 */       if (this.stack.isEmpty())
/* 853 */         return true; 
/* 855 */       return ((ScreenRectangle)this.stack.peek()).containsPoint(x, y);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum HoveredTextEffects {
/* 860 */     NONE(false, false),
/* 861 */     TOOLTIP_ONLY(true, false),
/* 862 */     TOOLTIP_AND_CURSOR(true, true);
/*     */     
/*     */     public final boolean allowTooltip;
/*     */     
/*     */     public final boolean allowCursorChanges;
/*     */     
/*     */     HoveredTextEffects(boolean allowTooltip, boolean allowCursorChanges) {
/* 869 */       this.allowTooltip = allowTooltip;
/* 870 */       this.allowCursorChanges = allowCursorChanges;
/*     */     }
/*     */     
/*     */     public static HoveredTextEffects notClickable(boolean canTooltip) {
/* 874 */       return canTooltip ? TOOLTIP_ONLY : NONE;
/*     */     }
/*     */   }
/*     */   
/*     */   private class RenderingTextCollector implements ActiveTextCollector, Consumer<Style> {
/*     */     private ActiveTextCollector.Parameters defaultParameters;
/*     */     
/*     */     private final GuiGraphicsExtractor.HoveredTextEffects hoveredTextEffects;
/*     */     
/*     */     private final Consumer<Style> additionalConsumer;
/*     */     
/*     */     private RenderingTextCollector(ActiveTextCollector.Parameters initialParameters, GuiGraphicsExtractor.HoveredTextEffects hoveredTextEffects, Consumer<Style> additonalConsumer) {
/* 884 */       this.defaultParameters = initialParameters;
/* 885 */       this.hoveredTextEffects = hoveredTextEffects;
/* 886 */       this.additionalConsumer = additonalConsumer;
/*     */     }
/*     */     
/*     */     public ActiveTextCollector.Parameters defaultParameters() {
/* 891 */       return this.defaultParameters;
/*     */     }
/*     */     
/*     */     public void defaultParameters(ActiveTextCollector.Parameters newParameters) {
/* 896 */       this.defaultParameters = newParameters;
/*     */     }
/*     */     
/*     */     public void accept(Style style) {
/* 901 */       if (this.hoveredTextEffects.allowTooltip && style.getHoverEvent() != null)
/* 902 */         GuiGraphicsExtractor.this.hoveredTextStyle = style; 
/* 905 */       if (this.hoveredTextEffects.allowCursorChanges && style.getClickEvent() != null)
/* 906 */         GuiGraphicsExtractor.this.clickableTextStyle = style; 
/* 909 */       if (this.additionalConsumer != null)
/* 910 */         this.additionalConsumer.accept(style); 
/*     */     }
/*     */     
/*     */     public void accept(TextAlignment alignment, int anchorX, int y, ActiveTextCollector.Parameters parameters, FormattedCharSequence text) {
/* 916 */       boolean needsFullStyleScan = (this.hoveredTextEffects.allowCursorChanges || this.hoveredTextEffects.allowTooltip || this.additionalConsumer != null);
/* 917 */       int leftX = alignment.calculateLeft(anchorX, GuiGraphicsExtractor.this.minecraft.font, text);
/* 918 */       GuiTextRenderState renderState = new GuiTextRenderState(GuiGraphicsExtractor.this.minecraft.font, text, parameters.pose(), leftX, y, ARGB.white(parameters.opacity()), 0, true, needsFullStyleScan, parameters.scissor());
/* 920 */       if (ARGB.as8BitChannel(parameters.opacity()) != 0)
/* 921 */         GuiGraphicsExtractor.this.guiRenderState.addText(renderState); 
/* 924 */       if (needsFullStyleScan)
/* 925 */         ActiveTextCollector.findElementUnderCursor(renderState, GuiGraphicsExtractor.this.mouseX, GuiGraphicsExtractor.this.mouseY, this); 
/*     */     }
/*     */     
/*     */     public void acceptScrolling(Component message, int centerX, int left, int right, int top, int bottom, ActiveTextCollector.Parameters parameters) {
/* 931 */       int lineWidth = GuiGraphicsExtractor.this.minecraft.font.width((FormattedText)message);
/* 932 */       Objects.requireNonNull(GuiGraphicsExtractor.this.minecraft.font);
/* 932 */       int lineHeight = 9;
/* 933 */       defaultScrollingHelper(message, centerX, left, right, top, bottom, lineWidth, lineHeight, parameters);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\GuiGraphicsExtractor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */