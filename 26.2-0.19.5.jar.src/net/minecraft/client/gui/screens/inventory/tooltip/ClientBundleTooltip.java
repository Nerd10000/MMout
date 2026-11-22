/*     */ package net.minecraft.client.gui.screens.inventory.tooltip;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.component.BundleContents;
/*     */ import org.apache.commons.lang3.math.Fraction;
/*     */ 
/*     */ public class ClientBundleTooltip implements ClientTooltipComponent {
/*  23 */   private static final Identifier PROGRESSBAR_BORDER_SPRITE = Identifier.withDefaultNamespace("container/bundle/bundle_progressbar_border");
/*     */   
/*  24 */   private static final Identifier PROGRESSBAR_FILL_SPRITE = Identifier.withDefaultNamespace("container/bundle/bundle_progressbar_fill");
/*     */   
/*  25 */   private static final Identifier PROGRESSBAR_FULL_SPRITE = Identifier.withDefaultNamespace("container/bundle/bundle_progressbar_full");
/*     */   
/*  26 */   private static final Identifier SLOT_HIGHLIGHT_BACK_SPRITE = Identifier.withDefaultNamespace("container/bundle/slot_highlight_back");
/*     */   
/*  27 */   private static final Identifier SLOT_HIGHLIGHT_FRONT_SPRITE = Identifier.withDefaultNamespace("container/bundle/slot_highlight_front");
/*     */   
/*  28 */   private static final Identifier SLOT_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("container/bundle/slot_background");
/*     */   
/*     */   private static final int SLOT_MARGIN = 4;
/*     */   
/*     */   private static final int SLOT_SIZE = 24;
/*     */   
/*     */   private static final int GRID_WIDTH = 96;
/*     */   
/*     */   private static final int PROGRESSBAR_HEIGHT = 13;
/*     */   
/*     */   private static final int PROGRESSBAR_WIDTH = 96;
/*     */   
/*     */   private static final int PROGRESSBAR_BORDER = 1;
/*     */   
/*     */   private static final int PROGRESSBAR_FILL_MAX = 94;
/*     */   
/*     */   private static final int PROGRESSBAR_MARGIN_Y = 4;
/*     */   
/*  38 */   private static final Component BUNDLE_FULL_TEXT = (Component)Component.translatable("item.minecraft.bundle.full");
/*     */   
/*  39 */   private static final Component BUNDLE_EMPTY_TEXT = (Component)Component.translatable("item.minecraft.bundle.empty");
/*     */   
/*  40 */   private static final Component BUNDLE_EMPTY_DESCRIPTION = (Component)Component.translatable("item.minecraft.bundle.empty.description");
/*     */   
/*     */   private final BundleContents contents;
/*     */   
/*     */   public ClientBundleTooltip(BundleContents contents) {
/*  45 */     this.contents = contents;
/*     */   }
/*     */   
/*     */   public int getHeight(Font font) {
/*  50 */     return this.contents.isEmpty() ? getEmptyBundleBackgroundHeight(font) : backgroundHeight();
/*     */   }
/*     */   
/*     */   public int getWidth(Font font) {
/*  55 */     return 96;
/*     */   }
/*     */   
/*     */   public boolean showTooltipWithItemInHand() {
/*  60 */     return true;
/*     */   }
/*     */   
/*     */   private static int getEmptyBundleBackgroundHeight(Font font) {
/*  64 */     return getEmptyBundleDescriptionTextHeight(font) + 13 + 8;
/*     */   }
/*     */   
/*     */   private int backgroundHeight() {
/*  68 */     return itemGridHeight() + 13 + 8;
/*     */   }
/*     */   
/*     */   private int itemGridHeight() {
/*  72 */     return gridSizeY() * 24;
/*     */   }
/*     */   
/*     */   private static int getContentXOffset(int tooltipWidth) {
/*  76 */     return (tooltipWidth - 96) / 2;
/*     */   }
/*     */   
/*     */   private int gridSizeY() {
/*  80 */     return Mth.positiveCeilDiv(slotCount(), 4);
/*     */   }
/*     */   
/*     */   private int slotCount() {
/*  84 */     return Math.min(12, this.contents.size());
/*     */   }
/*     */   
/*     */   public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
/*  89 */     DataResult<Fraction> weight = this.contents.weight();
/*  90 */     if (!weight.isError())
/*  91 */       if (this.contents.isEmpty()) {
/*  92 */         extractEmptyBundleTooltip(font, x, y, w, h, graphics);
/*     */       } else {
/*  94 */         extractBundleWithItemsTooltip(font, x, y, w, h, graphics, (Fraction)weight.getOrThrow());
/*     */       }  
/*     */   }
/*     */   
/*     */   private static void extractEmptyBundleTooltip(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
/* 100 */     int left = x + getContentXOffset(w);
/* 101 */     extractEmptyBundleDescriptionText(left, y, font, graphics);
/* 102 */     extractProgressbar(left, y + getEmptyBundleDescriptionTextHeight(font) + 4, font, graphics, Fraction.ZERO);
/*     */   }
/*     */   
/*     */   private void extractBundleWithItemsTooltip(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics, Fraction weight) {
/* 106 */     boolean isOverflowing = (this.contents.size() > 12);
/* 107 */     List<ItemStackTemplate> shownItems = getShownItems(this.contents.getNumberOfItemsToShow());
/* 108 */     int xStartPos = x + getContentXOffset(w) + 96;
/* 109 */     int yStartPos = y + gridSizeY() * 24;
/* 111 */     int slotNumber = 1;
/* 112 */     for (int rowNumber = 1; rowNumber <= gridSizeY(); rowNumber++) {
/* 113 */       for (int columnNumber = 1; columnNumber <= 4; columnNumber++) {
/* 114 */         int drawX = xStartPos - columnNumber * 24;
/* 115 */         int drawY = yStartPos - rowNumber * 24;
/* 117 */         if (shouldRenderSurplusText(isOverflowing, columnNumber, rowNumber)) {
/* 118 */           extractCount(drawX, drawY, getAmountOfHiddenItems(shownItems), font, graphics);
/* 119 */         } else if (shouldRenderItemSlot((List)shownItems, slotNumber)) {
/* 120 */           extractSlot(slotNumber, drawX, drawY, shownItems, slotNumber, font, graphics);
/* 121 */           slotNumber++;
/*     */         } 
/*     */       } 
/*     */     } 
/* 125 */     extractSelectedItemTooltip(font, graphics, x, y, w);
/* 126 */     extractProgressbar(x + getContentXOffset(w), y + itemGridHeight() + 4, font, graphics, weight);
/*     */   }
/*     */   
/*     */   private List<ItemStackTemplate> getShownItems(int amountOfItemsToShow) {
/* 130 */     int lastToDisplay = Math.min(this.contents.size(), amountOfItemsToShow);
/* 131 */     return this.contents.items().subList(0, lastToDisplay);
/*     */   }
/*     */   
/*     */   private static boolean shouldRenderSurplusText(boolean isOverflowing, int column, int row) {
/* 135 */     return (isOverflowing && column * row == 1);
/*     */   }
/*     */   
/*     */   private static boolean shouldRenderItemSlot(List<? extends ItemInstance> shownItems, int slotNumber) {
/* 139 */     return (shownItems.size() >= slotNumber);
/*     */   }
/*     */   
/*     */   private int getAmountOfHiddenItems(List<ItemStackTemplate> shownItems) {
/* 143 */     return this.contents.items()
/* 144 */       .stream()
/* 145 */       .skip(shownItems.size())
/* 146 */       .mapToInt(ItemInstance::count)
/* 147 */       .sum();
/*     */   }
/*     */   
/*     */   private void extractSlot(int slotNumber, int drawX, int drawY, List<ItemStackTemplate> shownItems, int slotIndex, Font font, GuiGraphicsExtractor graphics) {
/* 151 */     int itemVisualOrderIndex = shownItems.size() - slotNumber;
/* 152 */     boolean hasHighlight = (itemVisualOrderIndex == this.contents.getSelectedItemIndex());
/* 154 */     ItemStack item = ((ItemStackTemplate)shownItems.get(itemVisualOrderIndex)).create();
/* 155 */     if (hasHighlight) {
/* 156 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_BACK_SPRITE, drawX, drawY, 24, 24);
/*     */     } else {
/* 158 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_BACKGROUND_SPRITE, drawX, drawY, 24, 24);
/*     */     } 
/* 160 */     graphics.item(item, drawX + 4, drawY + 4, slotIndex);
/* 161 */     graphics.itemDecorations(font, item, drawX + 4, drawY + 4);
/* 163 */     if (hasHighlight)
/* 164 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_FRONT_SPRITE, drawX, drawY, 24, 24); 
/*     */   }
/*     */   
/*     */   private static void extractCount(int drawX, int drawY, int hiddenItemCount, Font font, GuiGraphicsExtractor graphics) {
/* 169 */     graphics.centeredText(font, "+" + hiddenItemCount, drawX + 12, drawY + 10, -1);
/*     */   }
/*     */   
/*     */   private void extractSelectedItemTooltip(Font font, GuiGraphicsExtractor graphics, int x, int y, int w) {
/* 173 */     ItemStackTemplate selectedItem = this.contents.getSelectedItem();
/* 174 */     if (selectedItem != null) {
/* 175 */       ItemStack itemStack = selectedItem.create();
/* 176 */       Component selectedItemName = itemStack.getStyledHoverName();
/* 177 */       int textWidth = font.width(selectedItemName.getVisualOrderText());
/* 178 */       int centerTooltip = x + w / 2 - 12;
/* 179 */       ClientTooltipComponent selectedItemNameTooltip = ClientTooltipComponent.create(selectedItemName.getVisualOrderText());
/* 180 */       graphics.tooltip(font, List.of(selectedItemNameTooltip), centerTooltip - textWidth / 2, y - 15, DefaultTooltipPositioner.INSTANCE, (Identifier)itemStack.get(DataComponents.TOOLTIP_STYLE));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void extractProgressbar(int x, int y, Font font, GuiGraphicsExtractor graphics, Fraction weight) {
/* 185 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getProgressBarTexture(weight), x + 1, y, getProgressBarFill(weight), 13);
/* 186 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESSBAR_BORDER_SPRITE, x, y, 96, 13);
/* 187 */     Component progressBarFillText = getProgressBarFillText(weight);
/* 188 */     if (progressBarFillText != null)
/* 189 */       graphics.centeredText(font, progressBarFillText, x + 48, y + 3, -1); 
/*     */   }
/*     */   
/*     */   private static void extractEmptyBundleDescriptionText(int x, int y, Font font, GuiGraphicsExtractor graphics) {
/* 194 */     graphics.textWithWordWrap(font, (FormattedText)BUNDLE_EMPTY_DESCRIPTION, x, y, 96, -5592406);
/*     */   }
/*     */   
/*     */   private static int getEmptyBundleDescriptionTextHeight(Font font) {
/* 198 */     Objects.requireNonNull(font);
/* 198 */     return font.split((FormattedText)BUNDLE_EMPTY_DESCRIPTION, 96).size() * 9;
/*     */   }
/*     */   
/*     */   private static int getProgressBarFill(Fraction weight) {
/* 202 */     return Mth.clamp(Mth.mulAndTruncate(weight, 94), 0, 94);
/*     */   }
/*     */   
/*     */   private static Identifier getProgressBarTexture(Fraction weight) {
/* 206 */     return (weight.compareTo(Fraction.ONE) >= 0) ? PROGRESSBAR_FULL_SPRITE : PROGRESSBAR_FILL_SPRITE;
/*     */   }
/*     */   
/*     */   private static Component getProgressBarFillText(Fraction weight) {
/* 210 */     if (weight.compareTo(Fraction.ZERO) == 0)
/* 211 */       return BUNDLE_EMPTY_TEXT; 
/* 212 */     if (weight.compareTo(Fraction.ONE) >= 0)
/* 213 */       return BUNDLE_FULL_TEXT; 
/* 215 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\tooltip\ClientBundleTooltip.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */