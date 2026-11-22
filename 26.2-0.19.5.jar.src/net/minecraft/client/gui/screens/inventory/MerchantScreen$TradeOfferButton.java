/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ 
/*     */ class TradeOfferButton extends Button.Plain {
/*     */   private final int index;
/*     */   
/*     */   public TradeOfferButton(int x, int y, int index, Button.OnPress onPress) {
/* 332 */     super(x, y, 88, 20, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
/* 333 */     this.index = index;
/* 334 */     this.visible = false;
/*     */   }
/*     */   
/*     */   public int getIndex() {
/* 338 */     return this.index;
/*     */   }
/*     */   
/*     */   public void extractToolTip(GuiGraphicsExtractor graphics, int xm, int ym) {
/* 342 */     if (this.isHovered && MerchantScreen.this.menu.getOffers().size() > this.index + MerchantScreen.this.scrollOff)
/* 343 */       if (xm < getX() + 20) {
/* 344 */         ItemStack item = ((MerchantOffer)MerchantScreen.this.menu.getOffers().get(this.index + MerchantScreen.this.scrollOff)).getCostA();
/* 345 */         graphics.setTooltipForNextFrame(MerchantScreen.access$000(MerchantScreen.this), item, xm, ym);
/* 346 */       } else if (xm < getX() + 50 && xm > getX() + 30) {
/* 347 */         ItemStack item = ((MerchantOffer)MerchantScreen.this.menu.getOffers().get(this.index + MerchantScreen.this.scrollOff)).getCostB();
/* 348 */         if (!item.isEmpty())
/* 349 */           graphics.setTooltipForNextFrame(MerchantScreen.access$100(MerchantScreen.this), item, xm, ym); 
/* 351 */       } else if (xm > getX() + 65) {
/* 352 */         ItemStack item = ((MerchantOffer)MerchantScreen.this.menu.getOffers().get(this.index + MerchantScreen.this.scrollOff)).getResult();
/* 353 */         graphics.setTooltipForNextFrame(MerchantScreen.access$200(MerchantScreen.this), item, xm, ym);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\MerchantScreen$TradeOfferButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */