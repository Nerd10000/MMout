/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
/*     */ 
/*     */ public class Entry extends ObjectSelectionList.Entry<PresetFlatWorldScreen.PresetsList.Entry> {
/*     */   private final FlatLevelGeneratorPreset preset;
/*     */   
/*     */   private final Component name;
/*     */   
/*     */   public Entry(Holder<FlatLevelGeneratorPreset> preset) {
/* 278 */     this.preset = (FlatLevelGeneratorPreset)preset.value();
/* 279 */     this
/*     */       
/* 281 */       .name = preset.unwrapKey().map(key -> Component.translatable(key.identifier().toLanguageKey("flat_world_preset"))).orElse(PresetFlatWorldScreen.UNKNOWN_PRESET);
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 286 */     blitSlot(graphics, getContentX(), getContentY(), (Item)this.preset.displayItem().value());
/* 287 */     graphics.text(this.this$1.this$0.font, this.name, getContentX() + 18 + 5, getContentY() + 6, -1);
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 292 */     select();
/* 293 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   private void select() {
/* 297 */     PresetFlatWorldScreen.PresetsList.this.setSelected(this);
/* 298 */     this.this$1.this$0.settings = this.preset.settings();
/* 299 */     this.this$1.this$0.export.setValue(PresetFlatWorldScreen.save(this.this$1.this$0.settings));
/* 300 */     this.this$1.this$0.export.moveCursorToStart(false);
/*     */   }
/*     */   
/*     */   private void blitSlot(GuiGraphicsExtractor graphics, int x, int y, Item item) {
/* 304 */     blitSlotBg(graphics, x + 1, y + 1);
/* 306 */     graphics.fakeItem(new ItemStack((ItemLike)item), x + 2, y + 2);
/*     */   }
/*     */   
/*     */   private void blitSlotBg(GuiGraphicsExtractor graphics, int x, int y) {
/* 310 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PresetFlatWorldScreen.SLOT_SPRITE, x, y, 18, 18);
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 315 */     return (Component)Component.translatable("narrator.select", new Object[] { this.name });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\PresetFlatWorldScreen$PresetsList$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */