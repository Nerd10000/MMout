/*     */ package net.minecraft.client.gui.screens.achievement;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class HeaderEntry extends StatsScreen.ItemStatisticsList.Entry {
/* 490 */   private static final Identifier BLOCK_MINED_SPRITE = Identifier.withDefaultNamespace("statistics/block_mined");
/*     */   
/* 491 */   private static final Identifier ITEM_BROKEN_SPRITE = Identifier.withDefaultNamespace("statistics/item_broken");
/*     */   
/* 492 */   private static final Identifier ITEM_CRAFTED_SPRITE = Identifier.withDefaultNamespace("statistics/item_crafted");
/*     */   
/* 493 */   private static final Identifier ITEM_USED_SPRITE = Identifier.withDefaultNamespace("statistics/item_used");
/*     */   
/* 494 */   private static final Identifier ITEM_PICKED_UP_SPRITE = Identifier.withDefaultNamespace("statistics/item_picked_up");
/*     */   
/* 495 */   private static final Identifier ITEM_DROPPED_SPRITE = Identifier.withDefaultNamespace("statistics/item_dropped");
/*     */   
/*     */   private final StatSortButton blockMined;
/*     */   
/*     */   private final StatSortButton itemBroken;
/*     */   
/*     */   private final StatSortButton itemCrafted;
/*     */   
/*     */   private final StatSortButton itemUsed;
/*     */   
/*     */   private final StatSortButton itemPickedUp;
/*     */   
/*     */   private final StatSortButton itemDropped;
/*     */   
/* 504 */   private final List<AbstractWidget> children = new ArrayList<>();
/*     */   
/*     */   private HeaderEntry() {
/* 507 */     this.blockMined = new StatSortButton(this, 0, BLOCK_MINED_SPRITE);
/* 508 */     this.itemBroken = new StatSortButton(this, 1, ITEM_BROKEN_SPRITE);
/* 509 */     this.itemCrafted = new StatSortButton(this, 2, ITEM_CRAFTED_SPRITE);
/* 510 */     this.itemUsed = new StatSortButton(this, 3, ITEM_USED_SPRITE);
/* 511 */     this.itemPickedUp = new StatSortButton(this, 4, ITEM_PICKED_UP_SPRITE);
/* 512 */     this.itemDropped = new StatSortButton(this, 5, ITEM_DROPPED_SPRITE);
/* 513 */     this.children.addAll((Collection)List.of(this.blockMined, this.itemBroken, this.itemCrafted, this.itemUsed, this.itemPickedUp, this.itemDropped));
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 518 */     this.blockMined.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(0) - 18, getContentY() + 1);
/* 519 */     this.blockMined.extractRenderState(graphics, mouseX, mouseY, a);
/* 520 */     this.itemBroken.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(1) - 18, getContentY() + 1);
/* 521 */     this.itemBroken.extractRenderState(graphics, mouseX, mouseY, a);
/* 522 */     this.itemCrafted.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(2) - 18, getContentY() + 1);
/* 523 */     this.itemCrafted.extractRenderState(graphics, mouseX, mouseY, a);
/* 524 */     this.itemUsed.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(3) - 18, getContentY() + 1);
/* 525 */     this.itemUsed.extractRenderState(graphics, mouseX, mouseY, a);
/* 526 */     this.itemPickedUp.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(4) - 18, getContentY() + 1);
/* 527 */     this.itemPickedUp.extractRenderState(graphics, mouseX, mouseY, a);
/* 528 */     this.itemDropped.setPosition(getContentX() + StatsScreen.ItemStatisticsList.this.getColumnX(5) - 18, getContentY() + 1);
/* 529 */     this.itemDropped.extractRenderState(graphics, mouseX, mouseY, a);
/* 531 */     if (StatsScreen.ItemStatisticsList.this.sortColumn != null) {
/* 532 */       int offset = StatsScreen.ItemStatisticsList.this.getColumnX(StatsScreen.ItemStatisticsList.this.getColumnIndex(StatsScreen.ItemStatisticsList.this.sortColumn)) - 36;
/* 534 */       Identifier sprite = (StatsScreen.ItemStatisticsList.this.sortOrder == 1) ? StatsScreen.SORT_UP_SPRITE : StatsScreen.SORT_DOWN_SPRITE;
/* 535 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getContentX() + offset, getContentY() + 1, 18, 18);
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 541 */     return (List)this.children;
/*     */   }
/*     */   
/*     */   public List<? extends NarratableEntry> narratables() {
/* 546 */     return (List)this.children;
/*     */   }
/*     */   
/*     */   private class StatSortButton extends ImageButton {
/*     */     private final Identifier sprite;
/*     */     
/*     */     private StatSortButton(StatsScreen.ItemStatisticsList.HeaderEntry this$0, int column, Identifier sprite) {
/* 553 */       super(18, 18, new WidgetSprites(StatsScreen.HEADER_SPRITE, StatsScreen.SLOT_SPRITE), button -> this$2.this$1.sortByColumn(this$2.this$1.getColumn(column)), this$0.this$1
/* 554 */           .getColumn(column).getDisplayName());
/* 555 */       this.sprite = sprite;
/* 556 */       setTooltip(Tooltip.create(getMessage()));
/*     */     }
/*     */     
/*     */     public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 561 */       Identifier background = this.sprites.get(isActive(), isHoveredOrFocused());
/* 562 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, background, getX(), getY(), this.width, this.height);
/* 563 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX(), getY(), this.width, this.height);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\achievement\StatsScreen$ItemStatisticsList$HeaderEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */