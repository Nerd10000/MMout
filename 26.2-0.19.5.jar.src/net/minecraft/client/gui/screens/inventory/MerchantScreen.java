/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerData;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.MerchantMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ 
/*     */ public class MerchantScreen extends AbstractContainerScreen<MerchantMenu> {
/*  22 */   private static final Identifier OUT_OF_STOCK_SPRITE = Identifier.withDefaultNamespace("container/villager/out_of_stock");
/*     */   
/*  23 */   private static final Identifier EXPERIENCE_BAR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("container/villager/experience_bar_background");
/*     */   
/*  24 */   private static final Identifier EXPERIENCE_BAR_CURRENT_SPRITE = Identifier.withDefaultNamespace("container/villager/experience_bar_current");
/*     */   
/*  25 */   private static final Identifier EXPERIENCE_BAR_RESULT_SPRITE = Identifier.withDefaultNamespace("container/villager/experience_bar_result");
/*     */   
/*  26 */   private static final Identifier SCROLLER_SPRITE = Identifier.withDefaultNamespace("container/villager/scroller");
/*     */   
/*  27 */   private static final Identifier SCROLLER_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/villager/scroller_disabled");
/*     */   
/*  28 */   private static final Identifier TRADE_ARROW_OUT_OF_STOCK_SPRITE = Identifier.withDefaultNamespace("container/villager/trade_arrow_out_of_stock");
/*     */   
/*  29 */   private static final Identifier TRADE_ARROW_SPRITE = Identifier.withDefaultNamespace("container/villager/trade_arrow");
/*     */   
/*  30 */   private static final Identifier DISCOUNT_STRIKETHRUOGH_SPRITE = Identifier.withDefaultNamespace("container/villager/discount_strikethrough");
/*     */   
/*  31 */   private static final Identifier VILLAGER_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/villager.png");
/*     */   
/*     */   private static final int TEXTURE_WIDTH = 512;
/*     */   
/*     */   private static final int TEXTURE_HEIGHT = 256;
/*     */   
/*     */   private static final int MERCHANT_MENU_PART_X = 99;
/*     */   
/*     */   private static final int PROGRESS_BAR_X = 136;
/*     */   
/*     */   private static final int PROGRESS_BAR_Y = 16;
/*     */   
/*     */   private static final int SELL_ITEM_1_X = 5;
/*     */   
/*     */   private static final int SELL_ITEM_2_X = 35;
/*     */   
/*     */   private static final int BUY_ITEM_X = 68;
/*     */   
/*     */   private static final int LABEL_Y = 6;
/*     */   
/*     */   private static final int NUMBER_OF_OFFER_BUTTONS = 7;
/*     */   
/*     */   private static final int TRADE_BUTTON_X = 5;
/*     */   
/*     */   private static final int TRADE_BUTTON_HEIGHT = 20;
/*     */   
/*     */   private static final int TRADE_BUTTON_WIDTH = 88;
/*     */   
/*     */   private static final int SCROLLER_HEIGHT = 27;
/*     */   
/*     */   private static final int SCROLLER_WIDTH = 6;
/*     */   
/*     */   private static final int SCROLL_BAR_HEIGHT = 139;
/*     */   
/*     */   private static final int SCROLL_BAR_TOP_POS_Y = 18;
/*     */   
/*     */   private static final int SCROLL_BAR_START_X = 94;
/*     */   
/*  55 */   private static final Component TRADES_LABEL = (Component)Component.translatable("merchant.trades");
/*     */   
/*  56 */   private static final Component DEPRECATED_TOOLTIP = (Component)Component.translatable("merchant.deprecated");
/*     */   
/*     */   private int shopItem;
/*     */   
/*  59 */   private final TradeOfferButton[] tradeOfferButtons = new TradeOfferButton[7];
/*     */   
/*     */   private int scrollOff;
/*     */   
/*     */   private boolean isDragging;
/*     */   
/*     */   public MerchantScreen(MerchantMenu menu, Inventory inventory, Component title) {
/*  64 */     super(menu, inventory, title, 276, 166);
/*  65 */     this.inventoryLabelX = 107;
/*     */   }
/*     */   
/*     */   private void postButtonClick() {
/*  69 */     this.menu.setSelectionHint(this.shopItem);
/*  70 */     this.menu.tryMoveItems(this.shopItem);
/*  71 */     this.minecraft.getConnection().send((Packet)new ServerboundSelectTradePacket(this.shopItem));
/*     */   }
/*     */   
/*     */   protected void init() {
/*  76 */     super.init();
/*  78 */     int xo = (this.width - this.imageWidth) / 2;
/*  79 */     int yo = (this.height - this.imageHeight) / 2;
/*  81 */     int buttonY = yo + 16 + 2;
/*  82 */     for (int i = 0; i < 7; i++) {
/*  83 */       this.tradeOfferButtons[i] = (TradeOfferButton)addRenderableWidget((GuiEventListener)new TradeOfferButton(xo + 5, buttonY, i, button -> {
/*     */               if (button instanceof TradeOfferButton) {
/*     */                 TradeOfferButton tradeOfferButton = (TradeOfferButton)button;
/*     */                 this.shopItem = tradeOfferButton.getIndex() + this.scrollOff;
/*     */                 postButtonClick();
/*     */               } 
/*     */             }));
/*  90 */       buttonY += 20;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
/*  96 */     int traderLevel = this.menu.getTraderLevel();
/*  97 */     if (traderLevel > 0 && traderLevel <= 5 && this.menu.showProgressBar()) {
/*  98 */       MutableComponent mutableComponent = Component.translatable("merchant.title", new Object[] { this.title, Component.translatable("merchant.level." + traderLevel) });
/*  99 */       int totalWidth = this.font.width((FormattedText)mutableComponent);
/* 100 */       int startX = 49 + this.imageWidth / 2 - totalWidth / 2;
/* 101 */       graphics.text(this.font, (Component)mutableComponent, startX, 6, -12566464, false);
/*     */     } else {
/* 103 */       graphics.text(this.font, this.title, 49 + this.imageWidth / 2 - this.font.width((FormattedText)this.title) / 2, 6, -12566464, false);
/*     */     } 
/* 105 */     graphics.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
/* 107 */     int textWidth = this.font.width((FormattedText)TRADES_LABEL);
/* 108 */     graphics.text(this.font, TRADES_LABEL, 5 - textWidth / 2 + 48, 6, -12566464, false);
/*     */   }
/*     */   
/*     */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 113 */     super.extractBackground(graphics, mouseX, mouseY, a);
/* 114 */     int xo = (this.width - this.imageWidth) / 2;
/* 115 */     int yo = (this.height - this.imageHeight) / 2;
/* 117 */     graphics.blit(RenderPipelines.GUI_TEXTURED, VILLAGER_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 512, 256);
/* 119 */     MerchantOffers offers = this.menu.getOffers();
/* 120 */     if (!offers.isEmpty()) {
/* 121 */       int itemIndex = this.shopItem;
/* 122 */       if (itemIndex < 0 || itemIndex >= offers.size())
/*     */         return; 
/* 126 */       MerchantOffer offer = (MerchantOffer)offers.get(itemIndex);
/* 127 */       if (offer.isOutOfStock())
/* 128 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, OUT_OF_STOCK_SPRITE, this.leftPos + 83 + 99, this.topPos + 35, 28, 21); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractProgressBar(GuiGraphicsExtractor graphics, int xo, int yo, MerchantOffer offer) {
/* 134 */     int traderLevel = this.menu.getTraderLevel();
/* 135 */     int traderXp = this.menu.getTraderXp();
/* 137 */     if (traderLevel >= 5)
/*     */       return; 
/* 142 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EXPERIENCE_BAR_BACKGROUND_SPRITE, xo + 136, yo + 16, 102, 5);
/* 144 */     int minXp = VillagerData.getMinXpPerLevel(traderLevel);
/* 145 */     if (traderXp < minXp || !VillagerData.canLevelUp(traderLevel))
/*     */       return; 
/* 149 */     int progressLength = 102;
/* 150 */     float multiplier = 102.0F / (VillagerData.getMaxXpPerLevel(traderLevel) - minXp);
/* 151 */     int w = Math.min(Mth.floor(multiplier * (traderXp - minXp)), 102);
/* 154 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EXPERIENCE_BAR_CURRENT_SPRITE, 102, 5, 0, 0, xo + 136, yo + 16, w, 5);
/* 155 */     int futureXp = this.menu.getFutureTraderXp();
/* 156 */     if (futureXp > 0) {
/* 157 */       int futureXpWidth = Math.min(Mth.floor(futureXp * multiplier), 102 - w);
/* 158 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EXPERIENCE_BAR_RESULT_SPRITE, 102, 5, w, 0, xo + 136 + w, yo + 16, futureXpWidth, 5);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractScroller(GuiGraphicsExtractor graphics, int xo, int yo, int mouseX, int mouseY, MerchantOffers offers) {
/* 163 */     int steps = offers.size() + 1 - 7;
/* 164 */     if (steps > 1) {
/* 165 */       int leftOver = 139 - 27 + (steps - 1) * 139 / steps;
/* 166 */       int stepHeight = 1 + leftOver / steps + 139 / steps;
/* 167 */       int maxScrollerOff = 113;
/* 168 */       int scrollerYOff = Math.min(113, this.scrollOff * stepHeight);
/* 169 */       if (this.scrollOff == steps - 1)
/* 171 */         scrollerYOff = 113; 
/* 173 */       int scrollerX = xo + 94;
/* 174 */       int scrollerY = yo + 18 + scrollerYOff;
/* 175 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SCROLLER_SPRITE, scrollerX, scrollerY, 6, 27);
/* 176 */       if (mouseX >= scrollerX && mouseX < xo + 94 + 6 && mouseY >= scrollerY && mouseY <= scrollerY + 27)
/* 177 */         graphics.requestCursor(this.isDragging ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND); 
/*     */     } else {
/* 181 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SCROLLER_DISABLED_SPRITE, xo + 94, yo + 18, 6, 27);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 187 */     super.extractContents(graphics, mouseX, mouseY, a);
/* 189 */     MerchantOffers offers = this.menu.getOffers();
/* 190 */     if (!offers.isEmpty()) {
/* 191 */       int xo = (this.width - this.imageWidth) / 2;
/* 192 */       int yo = (this.height - this.imageHeight) / 2;
/* 194 */       int offerY = yo + 16 + 1;
/* 195 */       int sellItem1X = xo + 5 + 5;
/* 197 */       extractScroller(graphics, xo, yo, mouseX, mouseY, offers);
/* 199 */       int currentOfferIndex = 0;
/* 200 */       for (MerchantOffer offer : offers) {
/* 201 */         if (canScroll(offers.size()) && (currentOfferIndex < this.scrollOff || currentOfferIndex >= 7 + this.scrollOff)) {
/* 202 */           currentOfferIndex++;
/*     */           continue;
/*     */         } 
/* 206 */         ItemStack baseCostA = offer.getBaseCostA();
/* 207 */         ItemStack costA = offer.getCostA();
/* 208 */         ItemStack costB = offer.getCostB();
/* 209 */         ItemStack result = offer.getResult();
/* 211 */         int decorHeight = offerY + 2;
/* 213 */         extractAndDecorateCostA(graphics, costA, baseCostA, sellItem1X, decorHeight);
/* 215 */         if (!costB.isEmpty()) {
/* 216 */           graphics.fakeItem(costB, xo + 5 + 35, decorHeight);
/* 217 */           graphics.itemDecorations(this.font, costB, xo + 5 + 35, decorHeight);
/*     */         } 
/* 220 */         extractButtonArrows(graphics, offer, xo, decorHeight);
/* 222 */         graphics.fakeItem(result, xo + 5 + 68, decorHeight);
/* 223 */         graphics.itemDecorations(this.font, result, xo + 5 + 68, decorHeight);
/* 225 */         offerY += 20;
/* 226 */         currentOfferIndex++;
/*     */       } 
/* 229 */       int itemIndex = this.shopItem;
/* 230 */       MerchantOffer selectedOffer = (MerchantOffer)offers.get(itemIndex);
/* 232 */       if (this.menu.showProgressBar())
/* 233 */         extractProgressBar(graphics, xo, yo, selectedOffer); 
/* 236 */       if (selectedOffer.isOutOfStock() && isHovering(186, 35, 22, 21, mouseX, mouseY) && this.menu.canRestock())
/* 237 */         graphics.setTooltipForNextFrame(this.font, DEPRECATED_TOOLTIP, mouseX, mouseY); 
/* 240 */       for (TradeOfferButton button : this.tradeOfferButtons) {
/* 241 */         if (button.isHoveredOrFocused())
/* 242 */           button.extractToolTip(graphics, mouseX, mouseY); 
/* 244 */         button.visible = (button.index < this.menu.getOffers().size());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractButtonArrows(GuiGraphicsExtractor graphics, MerchantOffer offer, int xo, int decorHeight) {
/* 250 */     if (offer.isOutOfStock()) {
/* 252 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TRADE_ARROW_OUT_OF_STOCK_SPRITE, xo + 5 + 35 + 20, decorHeight + 3, 10, 9);
/*     */     } else {
/* 255 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TRADE_ARROW_SPRITE, xo + 5 + 35 + 20, decorHeight + 3, 10, 9);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractAndDecorateCostA(GuiGraphicsExtractor graphics, ItemStack costA, ItemStack baseCostA, int sellItem1X, int decorHeight) {
/* 260 */     graphics.fakeItem(costA, sellItem1X, decorHeight);
/* 261 */     if (baseCostA.getCount() == costA.getCount()) {
/* 262 */       graphics.itemDecorations(this.font, costA, sellItem1X, decorHeight);
/*     */     } else {
/* 264 */       graphics.itemDecorations(this.font, baseCostA, sellItem1X, decorHeight, (baseCostA.getCount() == 1) ? "1" : null);
/* 265 */       graphics.itemDecorations(this.font, costA, sellItem1X + 14, decorHeight, (costA.getCount() == 1) ? "1" : null);
/* 267 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DISCOUNT_STRIKETHRUOGH_SPRITE, sellItem1X + 7, decorHeight + 12, 9, 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canScroll(int numberOfOffers) {
/* 272 */     return (numberOfOffers > 7);
/*     */   }
/*     */   
/*     */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 277 */     if (super.mouseScrolled(x, y, scrollX, scrollY))
/* 278 */       return true; 
/* 281 */     int numberOfOffers = this.menu.getOffers().size();
/* 282 */     if (canScroll(numberOfOffers)) {
/* 283 */       int maxScrollOff = numberOfOffers - 7;
/* 284 */       this.scrollOff = Mth.clamp((int)(this.scrollOff - scrollY), 0, maxScrollOff);
/*     */     } 
/* 286 */     return true;
/*     */   }
/*     */   
/*     */   public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
/* 291 */     int numberOfOffers = this.menu.getOffers().size();
/* 293 */     if (this.isDragging) {
/* 294 */       int fullScrollTopPos = this.topPos + 18;
/* 295 */       int fullScrollBottomPos = fullScrollTopPos + 139;
/* 296 */       int maxScrollOff = numberOfOffers - 7;
/* 298 */       float scrolling = ((float)event.y() - fullScrollTopPos - 13.5F) / ((fullScrollBottomPos - fullScrollTopPos) - 27.0F);
/* 299 */       scrolling = scrolling * maxScrollOff + 0.5F;
/* 301 */       this.scrollOff = Mth.clamp((int)scrolling, 0, maxScrollOff);
/* 303 */       return true;
/*     */     } 
/* 305 */     return super.mouseDragged(event, dx, dy);
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 310 */     int xo = (this.width - this.imageWidth) / 2;
/* 311 */     int yo = (this.height - this.imageHeight) / 2;
/* 313 */     if (canScroll(this.menu.getOffers().size()) && event
/* 314 */       .x() > (xo + 94) && event.x() < (xo + 94 + 6) && event
/* 315 */       .y() > (yo + 18) && event.y() <= (yo + 18 + 139 + 1))
/* 317 */       this.isDragging = true; 
/* 319 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   public boolean mouseReleased(MouseButtonEvent event) {
/* 324 */     this.isDragging = false;
/* 325 */     return super.mouseReleased(event);
/*     */   }
/*     */   
/*     */   private class TradeOfferButton extends Button.Plain {
/*     */     private final int index;
/*     */     
/*     */     public TradeOfferButton(int x, int y, int index, Button.OnPress onPress) {
/* 332 */       super(x, y, 88, 20, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
/* 333 */       this.index = index;
/* 334 */       this.visible = false;
/*     */     }
/*     */     
/*     */     public int getIndex() {
/* 338 */       return this.index;
/*     */     }
/*     */     
/*     */     public void extractToolTip(GuiGraphicsExtractor graphics, int xm, int ym) {
/* 342 */       if (this.isHovered && MerchantScreen.this.menu.getOffers().size() > this.index + MerchantScreen.this.scrollOff)
/* 343 */         if (xm < getX() + 20) {
/* 344 */           ItemStack item = ((MerchantOffer)MerchantScreen.this.menu.getOffers().get(this.index + MerchantScreen.this.scrollOff)).getCostA();
/* 345 */           graphics.setTooltipForNextFrame(MerchantScreen.this.font, item, xm, ym);
/* 346 */         } else if (xm < getX() + 50 && xm > getX() + 30) {
/* 347 */           ItemStack item = ((MerchantOffer)MerchantScreen.this.menu.getOffers().get(this.index + MerchantScreen.this.scrollOff)).getCostB();
/* 348 */           if (!item.isEmpty())
/* 349 */             graphics.setTooltipForNextFrame(MerchantScreen.this.font, item, xm, ym); 
/* 351 */         } else if (xm > getX() + 65) {
/* 352 */           ItemStack item = ((MerchantOffer)MerchantScreen.this.menu.getOffers().get(this.index + MerchantScreen.this.scrollOff)).getResult();
/* 353 */           graphics.setTooltipForNextFrame(MerchantScreen.this.font, item, xm, ym);
/*     */         }  
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\MerchantScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */