/*     */ package net.minecraft.client.gui.screens.advancements;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.advancements.AdvancementNode;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.advancements.DisplayInfo;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.StringSplitter;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class AdvancementWidget {
/*  27 */   private static final Identifier TITLE_BOX_SPRITE = Identifier.withDefaultNamespace("advancements/title_box");
/*     */   
/*     */   private static final int HEIGHT = 26;
/*     */   
/*     */   private static final int BOX_X = 0;
/*     */   
/*     */   private static final int BOX_WIDTH = 200;
/*     */   
/*     */   private static final int FRAME_WIDTH = 26;
/*     */   
/*     */   private static final int ICON_X = 8;
/*     */   
/*     */   private static final int ICON_Y = 5;
/*     */   
/*     */   private static final int ICON_WIDTH = 26;
/*     */   
/*     */   private static final int TITLE_PADDING_LEFT = 3;
/*     */   
/*     */   private static final int TITLE_PADDING_RIGHT = 5;
/*     */   
/*     */   private static final int TITLE_X = 32;
/*     */   
/*     */   private static final int TITLE_PADDING_TOP = 9;
/*     */   
/*     */   private static final int TITLE_PADDING_BOTTOM = 8;
/*     */   
/*     */   private static final int TITLE_MAX_WIDTH = 163;
/*     */   
/*     */   private static final int TITLE_MIN_WIDTH = 80;
/*     */   
/*  42 */   private static final int[] TEST_SPLIT_OFFSETS = new int[] { 0, 10, -10, 25, -25 };
/*     */   
/*     */   private final AdvancementTab tab;
/*     */   
/*     */   private final AdvancementNode advancementNode;
/*     */   
/*     */   private final DisplayInfo display;
/*     */   
/*     */   private final ItemStack icon;
/*     */   
/*     */   private final List<FormattedCharSequence> titleLines;
/*     */   
/*     */   private final int width;
/*     */   
/*     */   private final List<FormattedCharSequence> description;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private AdvancementWidget parent;
/*     */   
/*  53 */   private final List<AdvancementWidget> children = Lists.newArrayList();
/*     */   
/*     */   private AdvancementProgress progress;
/*     */   
/*     */   private final int x;
/*     */   
/*     */   private final int y;
/*     */   
/*     */   public AdvancementWidget(AdvancementTab tab, Minecraft minecraft, AdvancementNode advancementNode, DisplayInfo display) {
/*  59 */     this.tab = tab;
/*  60 */     this.advancementNode = advancementNode;
/*  61 */     this.display = display;
/*  62 */     this.minecraft = minecraft;
/*  63 */     this.titleLines = minecraft.font.split((FormattedText)display.getTitle(), 163);
/*  64 */     this.x = Mth.floor(display.getX() * 28.0F);
/*  65 */     this.y = Mth.floor(display.getY() * 27.0F);
/*  67 */     Objects.requireNonNull(minecraft.font);
/*  67 */     int titleWidth = Math.max(this.titleLines.stream().mapToInt(minecraft.font::width).max().orElse(0), 80);
/*  69 */     int maxProgressWidth = getMaxProgressWidth();
/*  70 */     int longestDescLine = 29 + titleWidth + maxProgressWidth;
/*  71 */     this.description = Language.getInstance().getVisualOrder(findOptimalLines(ComponentUtils.mergeStyles(display.getDescription(), Style.EMPTY.withColor(display.getType().getChatColor())), longestDescLine));
/*  72 */     for (FormattedCharSequence line : this.description)
/*  73 */       longestDescLine = Math.max(longestDescLine, minecraft.font.width(line)); 
/*  75 */     this.width = longestDescLine + 3 + 5;
/*  77 */     this.icon = display.getIcon().create();
/*     */   }
/*     */   
/*     */   private int getMaxProgressWidth() {
/*  81 */     int maxCriteraRequired = this.advancementNode.advancement().requirements().size();
/*  82 */     if (maxCriteraRequired <= 1)
/*  83 */       return 0; 
/*  85 */     int spacing = 8;
/*  86 */     MutableComponent mutableComponent = Component.translatable("advancements.progress", new Object[] { Integer.valueOf(maxCriteraRequired), Integer.valueOf(maxCriteraRequired) });
/*  87 */     return this.minecraft.font.width((FormattedText)mutableComponent) + 8;
/*     */   }
/*     */   
/*     */   private static float getMaxWidth(StringSplitter splitter, List<FormattedText> input) {
/*  91 */     Objects.requireNonNull(splitter);
/*  91 */     return (float)input.stream().mapToDouble(splitter::stringWidth).max().orElse(0.0D);
/*     */   }
/*     */   
/*     */   private List<FormattedText> findOptimalLines(Component input, int preferredWidth) {
/*  95 */     StringSplitter splitter = this.minecraft.font.getSplitter();
/*  97 */     List<FormattedText> bestSplit = null;
/*  98 */     float bestDistance = Float.MAX_VALUE;
/* 100 */     for (int testMargin : TEST_SPLIT_OFFSETS) {
/* 101 */       List<FormattedText> testSplit = splitter.splitLines((FormattedText)input, preferredWidth - testMargin, Style.EMPTY);
/* 102 */       float distance = Math.abs(getMaxWidth(splitter, testSplit) - preferredWidth);
/* 103 */       if (distance <= 10.0F)
/* 104 */         return testSplit; 
/* 106 */       if (distance < bestDistance) {
/* 107 */         bestDistance = distance;
/* 108 */         bestSplit = testSplit;
/*     */       } 
/*     */     } 
/* 112 */     return bestSplit;
/*     */   }
/*     */   
/*     */   private AdvancementWidget getFirstVisibleParent(AdvancementNode node) {
/*     */     do {
/* 117 */       node = node.parent();
/* 118 */     } while (node != null && node.advancement().display().isEmpty());
/* 119 */     if (node == null || node.advancement().display().isEmpty())
/* 120 */       return null; 
/* 122 */     return this.tab.getWidget(node.holder());
/*     */   }
/*     */   
/*     */   public void extractConnectivity(GuiGraphicsExtractor graphics, int xo, int yo, boolean background) {
/* 126 */     if (this.parent != null) {
/* 127 */       int depX = xo + this.parent.x + 13;
/* 128 */       int splitX = xo + this.parent.x + 26 + 4;
/* 129 */       int depY = yo + this.parent.y + 13;
/* 130 */       int myX = xo + this.x + 13;
/* 131 */       int myY = yo + this.y + 13;
/* 132 */       int col = background ? -16777216 : -1;
/* 133 */       if (background) {
/* 134 */         graphics.horizontalLine(splitX, depX, depY - 1, col);
/* 135 */         graphics.horizontalLine(splitX + 1, depX, depY, col);
/* 136 */         graphics.horizontalLine(splitX, depX, depY + 1, col);
/* 137 */         graphics.horizontalLine(myX, splitX - 1, myY - 1, col);
/* 138 */         graphics.horizontalLine(myX, splitX - 1, myY, col);
/* 139 */         graphics.horizontalLine(myX, splitX - 1, myY + 1, col);
/* 140 */         graphics.verticalLine(splitX - 1, myY, depY, col);
/* 141 */         graphics.verticalLine(splitX + 1, myY, depY, col);
/*     */       } else {
/* 143 */         graphics.horizontalLine(splitX, depX, depY, col);
/* 144 */         graphics.horizontalLine(myX, splitX, myY, col);
/* 145 */         graphics.verticalLine(splitX, myY, depY, col);
/*     */       } 
/*     */     } 
/* 149 */     for (AdvancementWidget child : this.children)
/* 150 */       child.extractConnectivity(graphics, xo, yo, background); 
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int xo, int yo) {
/* 155 */     if (!this.display.isHidden() || (this.progress != null && this.progress.isDone())) {
/*     */       AdvancementWidgetType iconFrame;
/* 156 */       float amount = (this.progress == null) ? 0.0F : this.progress.getPercent();
/* 159 */       if (amount >= 1.0F) {
/* 160 */         iconFrame = AdvancementWidgetType.OBTAINED;
/*     */       } else {
/* 162 */         iconFrame = AdvancementWidgetType.UNOBTAINED;
/*     */       } 
/* 165 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, iconFrame.frameSprite(this.display.getType()), xo + this.x + 3, yo + this.y, 26, 26);
/* 166 */       graphics.fakeItem(this.icon, xo + this.x + 8, yo + this.y + 5);
/*     */     } 
/* 169 */     for (AdvancementWidget child : this.children)
/* 170 */       child.extractRenderState(graphics, xo, yo); 
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 175 */     return this.width;
/*     */   }
/*     */   
/*     */   public void setProgress(AdvancementProgress progress) {
/* 179 */     this.progress = progress;
/*     */   }
/*     */   
/*     */   public void addChild(AdvancementWidget widget) {
/* 183 */     this.children.add(widget);
/*     */   }
/*     */   
/*     */   public void extractHover(GuiGraphicsExtractor graphics, int xo, int yo, float fade, int screenxo, int screenyo) {
/*     */     AdvancementWidgetType firstHalf, secondHalf, iconFrame;
/*     */     int titleLeft;
/* 187 */     Font font = this.minecraft.font;
/* 188 */     Objects.requireNonNull(font);
/* 188 */     int titleBarHeight = 9 * this.titleLines.size() + 9 + 8;
/* 190 */     int titleTop = yo + this.y + (26 - titleBarHeight) / 2;
/* 191 */     int titleBarBottom = titleTop + titleBarHeight;
/* 192 */     Objects.requireNonNull(font);
/* 192 */     int descriptionTextHeight = this.description.size() * 9;
/* 193 */     int descriptionHeight = 6 + descriptionTextHeight;
/* 195 */     boolean leftSide = (screenxo + xo + this.x + this.width + 26 >= (this.tab.getScreen()).width);
/* 196 */     Component progressText = (this.progress == null) ? null : this.progress.getProgressText();
/* 197 */     int progressWidth = (progressText == null) ? 0 : font.width((FormattedText)progressText);
/* 198 */     boolean topSide = (titleBarBottom + descriptionHeight >= 113);
/* 199 */     float amount = (this.progress == null) ? 0.0F : this.progress.getPercent();
/* 203 */     int firstHalfWidth = Mth.floor(amount * this.width);
/* 205 */     if (amount >= 1.0F) {
/* 206 */       firstHalfWidth = this.width / 2;
/* 207 */       firstHalf = AdvancementWidgetType.OBTAINED;
/* 208 */       secondHalf = AdvancementWidgetType.OBTAINED;
/* 209 */       iconFrame = AdvancementWidgetType.OBTAINED;
/* 210 */     } else if (firstHalfWidth < 2) {
/* 211 */       firstHalfWidth = this.width / 2;
/* 212 */       firstHalf = AdvancementWidgetType.UNOBTAINED;
/* 213 */       secondHalf = AdvancementWidgetType.UNOBTAINED;
/* 214 */       iconFrame = AdvancementWidgetType.UNOBTAINED;
/* 215 */     } else if (firstHalfWidth > this.width - 2) {
/* 216 */       firstHalfWidth = this.width / 2;
/* 217 */       firstHalf = AdvancementWidgetType.OBTAINED;
/* 218 */       secondHalf = AdvancementWidgetType.OBTAINED;
/* 219 */       iconFrame = AdvancementWidgetType.UNOBTAINED;
/*     */     } else {
/* 221 */       firstHalf = AdvancementWidgetType.OBTAINED;
/* 222 */       secondHalf = AdvancementWidgetType.UNOBTAINED;
/* 223 */       iconFrame = AdvancementWidgetType.UNOBTAINED;
/*     */     } 
/* 225 */     int secondBarWidth = this.width - firstHalfWidth;
/* 228 */     if (leftSide) {
/* 229 */       titleLeft = xo + this.x - this.width + 26 + 6;
/*     */     } else {
/* 231 */       titleLeft = xo + this.x;
/*     */     } 
/* 234 */     int backgroundHeight = titleBarHeight + descriptionHeight;
/* 235 */     if (!this.description.isEmpty())
/* 236 */       if (topSide) {
/* 237 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TITLE_BOX_SPRITE, titleLeft, titleBarBottom - backgroundHeight, this.width, backgroundHeight);
/*     */       } else {
/* 239 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TITLE_BOX_SPRITE, titleLeft, titleTop, this.width, backgroundHeight);
/*     */       }  
/* 243 */     if (firstHalf != secondHalf) {
/* 244 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, firstHalf.boxSprite(), 200, titleBarHeight, 0, 0, titleLeft, titleTop, firstHalfWidth, titleBarHeight);
/* 245 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, secondHalf.boxSprite(), 200, titleBarHeight, 200 - secondBarWidth, 0, titleLeft + firstHalfWidth, titleTop, secondBarWidth, titleBarHeight);
/*     */     } else {
/* 247 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, firstHalf.boxSprite(), titleLeft, titleTop, this.width, titleBarHeight);
/*     */     } 
/* 250 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, iconFrame.frameSprite(this.display.getType()), xo + this.x + 3, yo + this.y, 26, 26);
/* 252 */     int descriptionLeft = titleLeft + 5;
/* 254 */     if (leftSide) {
/* 255 */       extractMultilineText(graphics, this.titleLines, descriptionLeft, titleTop + 9, -1);
/* 256 */       if (progressText != null)
/* 257 */         graphics.text(font, progressText, xo + this.x - progressWidth, titleTop + 9, -1); 
/*     */     } else {
/* 260 */       extractMultilineText(graphics, this.titleLines, xo + this.x + 32, titleTop + 9, -1);
/* 261 */       if (progressText != null)
/* 262 */         graphics.text(font, progressText, xo + this.x + this.width - progressWidth - 5, titleTop + 9, -1); 
/*     */     } 
/* 266 */     if (topSide) {
/* 267 */       extractMultilineText(graphics, this.description, descriptionLeft, titleTop - descriptionTextHeight + 1, -16711936);
/*     */     } else {
/* 269 */       extractMultilineText(graphics, this.description, descriptionLeft, titleBarBottom, -16711936);
/*     */     } 
/* 272 */     graphics.fakeItem(this.icon, xo + this.x + 8, yo + this.y + 5);
/*     */   }
/*     */   
/*     */   private void extractMultilineText(GuiGraphicsExtractor graphics, List<FormattedCharSequence> lines, int x, int y, int color) {
/* 276 */     Font font = this.minecraft.font;
/* 277 */     for (int i = 0; i < lines.size(); i++) {
/* 278 */       Objects.requireNonNull(font);
/* 278 */       graphics.text(font, lines.get(i), x, y + i * 9, color);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isMouseOver(int xo, int yo, int mouseX, int mouseY) {
/* 283 */     if (this.display.isHidden() && (this.progress == null || !this.progress.isDone()))
/* 284 */       return false; 
/* 286 */     int x0 = xo + this.x;
/* 287 */     int x1 = x0 + 26;
/* 288 */     int y0 = yo + this.y;
/* 289 */     int y1 = y0 + 26;
/* 290 */     return (mouseX >= x0 && mouseX <= x1 && mouseY >= y0 && mouseY <= y1);
/*     */   }
/*     */   
/*     */   public void attachToParent() {
/* 294 */     if (this.parent == null && this.advancementNode.parent() != null) {
/* 295 */       this.parent = getFirstVisibleParent(this.advancementNode);
/* 296 */       if (this.parent != null)
/* 297 */         this.parent.addChild(this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getY() {
/* 303 */     return this.y;
/*     */   }
/*     */   
/*     */   public int getX() {
/* 307 */     return this.x;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\advancements\AdvancementWidget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */