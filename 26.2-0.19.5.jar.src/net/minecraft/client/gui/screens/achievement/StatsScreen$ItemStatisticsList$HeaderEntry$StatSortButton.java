/*     */ package net.minecraft.client.gui.screens.achievement;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ImageButton;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.WidgetSprites;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class StatSortButton extends ImageButton {
/*     */   private final Identifier sprite;
/*     */   
/*     */   private StatSortButton(StatsScreen.ItemStatisticsList.HeaderEntry paramHeaderEntry, int column, Identifier sprite) {
/* 553 */     super(18, 18, new WidgetSprites(StatsScreen.HEADER_SPRITE, StatsScreen.SLOT_SPRITE), button -> this$2.this$1.sortByColumn(this$2.this$1.getColumn(column)), paramHeaderEntry.this$1
/* 554 */         .getColumn(column).getDisplayName());
/* 555 */     this.sprite = sprite;
/* 556 */     setTooltip(Tooltip.create(getMessage()));
/*     */   }
/*     */   
/*     */   public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 561 */     Identifier background = this.sprites.get(isActive(), isHoveredOrFocused());
/* 562 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, background, getX(), getY(), this.width, this.height);
/* 563 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX(), getY(), this.width, this.height);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\achievement\StatsScreen$ItemStatisticsList$HeaderEntry$StatSortButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */