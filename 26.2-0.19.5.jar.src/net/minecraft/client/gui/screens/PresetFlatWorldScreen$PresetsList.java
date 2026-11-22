/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.FlatLevelGeneratorPresetTags;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
/*     */ 
/*     */ class PresetsList extends ObjectSelectionList<PresetFlatWorldScreen.PresetsList.Entry> {
/*     */   public PresetsList(RegistryAccess access, FeatureFlagSet enabledFeatures) {
/* 242 */     super(paramPresetFlatWorldScreen.minecraft, paramPresetFlatWorldScreen.width, paramPresetFlatWorldScreen.height - 117, 80, 24);
/* 243 */     for (Holder<FlatLevelGeneratorPreset> preset : (Iterable<Holder<FlatLevelGeneratorPreset>>)access.lookupOrThrow(Registries.FLAT_LEVEL_GENERATOR_PRESET).getTagOrEmpty(FlatLevelGeneratorPresetTags.VISIBLE)) {
/* 244 */       Set<Block> disabledBlocks = (Set<Block>)((FlatLevelGeneratorPreset)preset.value()).settings().getLayersInfo().stream().map(p -> p.getBlockState().getBlock()).filter(b -> !b.isEnabled(enabledFeatures)).collect(Collectors.toSet());
/* 245 */       if (!disabledBlocks.isEmpty()) {
/* 246 */         PresetFlatWorldScreen.LOGGER.info("Discarding flat world preset {} since it contains experimental blocks {}", preset.unwrapKey().map(e -> e.identifier().toString()).orElse("<unknown>"), disabledBlocks);
/*     */         continue;
/*     */       } 
/* 248 */       addEntry((AbstractSelectionList.Entry)new Entry(preset));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setSelected(Entry selected) {
/* 255 */     super.setSelected((AbstractSelectionList.Entry)selected);
/* 256 */     PresetFlatWorldScreen.this.updateButtonValidity((selected != null));
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 261 */     if (super.keyPressed(event))
/* 262 */       return true; 
/* 264 */     if (event.isSelection() && 
/* 265 */       getSelected() != null)
/* 266 */       ((Entry)getSelected()).select(); 
/* 269 */     return false;
/*     */   }
/*     */   
/*     */   public class Entry extends ObjectSelectionList.Entry<Entry> {
/*     */     private final FlatLevelGeneratorPreset preset;
/*     */     
/*     */     private final Component name;
/*     */     
/*     */     public Entry(Holder<FlatLevelGeneratorPreset> preset) {
/* 278 */       this.preset = (FlatLevelGeneratorPreset)preset.value();
/* 279 */       this
/*     */         
/* 281 */         .name = preset.unwrapKey().map(key -> Component.translatable(key.identifier().toLanguageKey("flat_world_preset"))).orElse(PresetFlatWorldScreen.UNKNOWN_PRESET);
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 286 */       blitSlot(graphics, getContentX(), getContentY(), (Item)this.preset.displayItem().value());
/* 287 */       graphics.text(this.this$1.this$0.font, this.name, getContentX() + 18 + 5, getContentY() + 6, -1);
/*     */     }
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 292 */       select();
/* 293 */       return super.mouseClicked(event, doubleClick);
/*     */     }
/*     */     
/*     */     private void select() {
/* 297 */       PresetFlatWorldScreen.PresetsList.this.setSelected(this);
/* 298 */       this.this$1.this$0.settings = this.preset.settings();
/* 299 */       this.this$1.this$0.export.setValue(PresetFlatWorldScreen.save(this.this$1.this$0.settings));
/* 300 */       this.this$1.this$0.export.moveCursorToStart(false);
/*     */     }
/*     */     
/*     */     private void blitSlot(GuiGraphicsExtractor graphics, int x, int y, Item item) {
/* 304 */       blitSlotBg(graphics, x + 1, y + 1);
/* 306 */       graphics.fakeItem(new ItemStack((ItemLike)item), x + 2, y + 2);
/*     */     }
/*     */     
/*     */     private void blitSlotBg(GuiGraphicsExtractor graphics, int x, int y) {
/* 310 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, PresetFlatWorldScreen.SLOT_SPRITE, x, y, 18, 18);
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/* 315 */       return (Component)Component.translatable("narrator.select", new Object[] { this.name });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\PresetFlatWorldScreen$PresetsList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */