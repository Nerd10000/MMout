/*     */ package net.minecraft.client.gui.screens.achievement;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.ItemDisplayWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.stats.Stat;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ class ItemRow extends StatsScreen.ItemStatisticsList.Entry {
/*     */   private final Item item;
/*     */   
/*     */   private final ItemRowWidget itemRowWidget;
/*     */   
/*     */   private ItemRow(Item item) {
/* 426 */     this.item = item;
/* 427 */     this.itemRowWidget = new ItemRowWidget(item.getDefaultInstance());
/*     */   }
/*     */   
/*     */   protected Item getItem() {
/* 431 */     return this.item;
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 436 */     this.itemRowWidget.setPosition(getContentX(), getContentY());
/* 437 */     this.itemRowWidget.extractRenderState(graphics, mouseX, mouseY, a);
/* 439 */     StatsScreen.ItemStatisticsList itemStatsList = StatsScreen.ItemStatisticsList.this;
/* 440 */     int index = itemStatsList.children().indexOf(this);
/*     */     int col;
/* 441 */     for (col = 0; col < itemStatsList.blockColumns.size(); col++) {
/*     */       Stat<Block> stat;
/* 443 */       Item item = this.item;
/* 443 */       if (item instanceof BlockItem) {
/* 443 */         BlockItem blockItem = (BlockItem)item;
/* 444 */         stat = ((StatType)itemStatsList.blockColumns.get(col)).get(blockItem.getBlock());
/*     */       } else {
/* 446 */         stat = null;
/*     */       } 
/* 448 */       Objects.requireNonNull(StatsScreen.access$400(this.this$1.this$0));
/* 448 */       extractStat(graphics, stat, getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(col), getContentYMiddle() - 9 / 2, (index % 2 == 0));
/*     */     } 
/* 450 */     for (col = 0; col < itemStatsList.itemColumns.size(); 
/* 451 */       extractStat(graphics, ((StatType)itemStatsList.itemColumns.get(col)).get(this.item), getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(col + itemStatsList.blockColumns.size()), 
/* 452 */         getContentYMiddle() - 9 / 2, (index % 2 == 0)), col++)
/* 452 */       Objects.requireNonNull(StatsScreen.access$500(this.this$1.this$0)); 
/*     */   }
/*     */   
/*     */   protected void extractStat(GuiGraphicsExtractor graphics, Stat<?> stat, int x, int y, boolean shaded) {
/* 457 */     Component msg = (stat == null) ? StatsScreen.NO_VALUE_DISPLAY : (Component)Component.literal(stat.format(this.this$1.this$0.stats.getValue(stat)));
/* 458 */     graphics.text(StatsScreen.access$600(this.this$1.this$0), msg, x - StatsScreen.access$700(this.this$1.this$0).width((FormattedText)msg), y, shaded ? -1 : -4539718);
/*     */   }
/*     */   
/*     */   public List<? extends NarratableEntry> narratables() {
/* 463 */     return (List)List.of(this.itemRowWidget);
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 468 */     return (List)List.of(this.itemRowWidget);
/*     */   }
/*     */   
/*     */   private class ItemRowWidget extends ItemDisplayWidget {
/*     */     private ItemRowWidget(ItemStack itemStack) {
/* 473 */       super(StatsScreen.ItemStatisticsList.access$800(this$0.this$1), 1, 1, 18, 18, itemStack.getHoverName(), itemStack, false, true);
/*     */     }
/*     */     
/*     */     protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 478 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, StatsScreen.SLOT_SPRITE, StatsScreen.ItemStatisticsList.ItemRow.this.getContentX(), StatsScreen.ItemStatisticsList.ItemRow.this.getContentY(), 18, 18);
/* 479 */       super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
/*     */     }
/*     */     
/*     */     protected void extractTooltip(GuiGraphicsExtractor graphics, int x, int y) {
/* 484 */       super.extractTooltip(graphics, StatsScreen.ItemStatisticsList.ItemRow.this.getContentX() + 18, StatsScreen.ItemStatisticsList.ItemRow.this.getContentY() + 18);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\achievement\StatsScreen$ItemStatisticsList$ItemRow.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */