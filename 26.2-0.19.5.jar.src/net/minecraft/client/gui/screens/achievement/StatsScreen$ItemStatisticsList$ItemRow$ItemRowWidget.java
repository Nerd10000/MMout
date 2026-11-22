/*     */ package net.minecraft.client.gui.screens.achievement;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.ItemDisplayWidget;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ class ItemRowWidget extends ItemDisplayWidget {
/*     */   private ItemRowWidget(ItemStack itemStack) {
/* 473 */     super(StatsScreen.ItemStatisticsList.access$800(paramItemRow.this$1), 1, 1, 18, 18, itemStack.getHoverName(), itemStack, false, true);
/*     */   }
/*     */   
/*     */   protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 478 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, StatsScreen.SLOT_SPRITE, StatsScreen.ItemStatisticsList.ItemRow.this.getContentX(), StatsScreen.ItemStatisticsList.ItemRow.this.getContentY(), 18, 18);
/* 479 */     super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   protected void extractTooltip(GuiGraphicsExtractor graphics, int x, int y) {
/* 484 */     super.extractTooltip(graphics, StatsScreen.ItemStatisticsList.ItemRow.this.getContentX() + 18, StatsScreen.ItemStatisticsList.ItemRow.this.getContentY() + 18);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\achievement\StatsScreen$ItemStatisticsList$ItemRow$ItemRowWidget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */