/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
/*     */ 
/*     */ class DetailsList extends ObjectSelectionList<CreateFlatWorldScreen.DetailsList.Entry> {
/* 133 */   private static final Component LAYER_MATERIAL_TITLE = (Component)Component.translatable("createWorld.customize.flat.tile").withStyle(ChatFormatting.UNDERLINE);
/*     */   
/* 134 */   private static final Component HEIGHT_TITLE = (Component)Component.translatable("createWorld.customize.flat.height").withStyle(ChatFormatting.UNDERLINE);
/*     */   
/*     */   public DetailsList() {
/* 137 */     super(paramCreateFlatWorldScreen.minecraft, paramCreateFlatWorldScreen.width, paramCreateFlatWorldScreen.height - 103, 43, 24);
/* 139 */     populateList();
/*     */   }
/*     */   
/*     */   private void populateList() {
/* 143 */     Objects.requireNonNull(CreateFlatWorldScreen.this.font);
/* 143 */     addEntry((AbstractSelectionList.Entry)new HeaderEntry(CreateFlatWorldScreen.this.font), (int)(9.0D * 1.5D));
/* 144 */     List<FlatLayerInfo> layersInfo = CreateFlatWorldScreen.this.generator.getLayersInfo().reversed();
/* 145 */     for (int i = 0; i < layersInfo.size(); i++)
/* 146 */       addEntry((AbstractSelectionList.Entry)new LayerEntry(layersInfo.get(i), i)); 
/*     */   }
/*     */   
/*     */   public void setSelected(Entry selected) {
/* 152 */     super.setSelected((AbstractSelectionList.Entry)selected);
/* 153 */     CreateFlatWorldScreen.this.updateButtonValidity();
/*     */   }
/*     */   
/*     */   public void resetRows() {
/* 157 */     int index = children().indexOf(getSelected());
/* 158 */     clearEntries();
/* 159 */     populateList();
/* 161 */     List<Entry> children = children();
/* 162 */     if (index >= 0 && index < children.size())
/* 163 */       setSelected(children.get(index)); 
/*     */   }
/*     */   
/*     */   private void deleteLayer(LayerEntry selectedLayerEntry) {
/* 168 */     List<FlatLayerInfo> layersInfo = CreateFlatWorldScreen.this.generator.getLayersInfo();
/* 169 */     int deletedLayerIndex = children().indexOf(selectedLayerEntry);
/* 170 */     removeEntry((AbstractSelectionList.Entry)selectedLayerEntry);
/* 171 */     layersInfo.remove(selectedLayerEntry.layerInfo);
/* 172 */     setSelected(layersInfo.isEmpty() ? null : children().get(Math.min(deletedLayerIndex, layersInfo.size())));
/* 173 */     CreateFlatWorldScreen.this.generator.updateLayers();
/* 174 */     resetRows();
/* 175 */     CreateFlatWorldScreen.this.updateButtonValidity();
/*     */   }
/*     */   
/*     */   private static abstract class Entry extends ObjectSelectionList.Entry<Entry> {}
/*     */   
/*     */   private class LayerEntry extends Entry {
/*     */     private final FlatLayerInfo layerInfo;
/*     */     
/*     */     private final int index;
/*     */     
/*     */     public LayerEntry(FlatLayerInfo layerInfo, int index) {
/* 186 */       this.layerInfo = layerInfo;
/* 187 */       this.index = index;
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */       MutableComponent mutableComponent;
/* 192 */       BlockState blockState = this.layerInfo.getBlockState();
/* 194 */       ItemStack itemStack = getDisplayItem(blockState);
/* 195 */       blitSlot(graphics, getContentX(), getContentY(), itemStack);
/* 197 */       Objects.requireNonNull(this.this$1.this$0.font);
/* 197 */       int y = getContentYMiddle() - 9 / 2;
/* 198 */       graphics.text(this.this$1.this$0.font, itemStack.getHoverName(), getContentX() + 18 + 5, y, -1);
/* 201 */       if (this.index == 0) {
/* 202 */         mutableComponent = Component.translatable("createWorld.customize.flat.layer.top", new Object[] { Integer.valueOf(this.layerInfo.getHeight()) });
/* 203 */       } else if (this.index == this.this$1.this$0.generator.getLayersInfo().size() - 1) {
/* 204 */         mutableComponent = Component.translatable("createWorld.customize.flat.layer.bottom", new Object[] { Integer.valueOf(this.layerInfo.getHeight()) });
/*     */       } else {
/* 206 */         mutableComponent = Component.translatable("createWorld.customize.flat.layer", new Object[] { Integer.valueOf(this.layerInfo.getHeight()) });
/*     */       } 
/* 209 */       graphics.text(this.this$1.this$0.font, (Component)mutableComponent, getContentRight() - this.this$1.this$0.font.width((FormattedText)mutableComponent), y, -1);
/*     */     }
/*     */     
/*     */     private ItemStack getDisplayItem(BlockState blockState) {
/* 213 */       Item item = blockState.getBlock().asItem();
/* 214 */       if (item == Items.AIR)
/* 215 */         if (blockState.is(Blocks.WATER)) {
/* 216 */           item = Items.WATER_BUCKET;
/* 217 */         } else if (blockState.is(Blocks.LAVA)) {
/* 218 */           item = Items.LAVA_BUCKET;
/*     */         }  
/* 221 */       return new ItemStack((ItemLike)item);
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/* 226 */       ItemStack itemStack = getDisplayItem(this.layerInfo.getBlockState());
/* 227 */       if (!itemStack.isEmpty())
/* 228 */         return (Component)CommonComponents.joinForNarration(new Component[] { (Component)Component.translatable("narrator.select", new Object[] { itemStack.getHoverName() }), CreateFlatWorldScreen.DetailsList.HEIGHT_TITLE, 
/*     */               
/* 231 */               (Component)Component.literal(String.valueOf(this.layerInfo.getHeight())) }); 
/* 234 */       return CommonComponents.EMPTY;
/*     */     }
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 240 */       CreateFlatWorldScreen.DetailsList.this.setSelected(this);
/* 241 */       return super.mouseClicked(event, doubleClick);
/*     */     }
/*     */     
/*     */     private void blitSlot(GuiGraphicsExtractor graphics, int x, int y, ItemStack itemStack) {
/* 245 */       blitSlotBg(graphics, x + 1, y + 1);
/* 247 */       if (!itemStack.isEmpty())
/* 248 */         graphics.fakeItem(itemStack, x + 2, y + 2); 
/*     */     }
/*     */     
/*     */     private void blitSlotBg(GuiGraphicsExtractor graphics, int x, int y) {
/* 253 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, CreateFlatWorldScreen.SLOT_SPRITE, x, y, 18, 18);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class HeaderEntry extends Entry {
/*     */     private final Font font;
/*     */     
/*     */     public HeaderEntry(Font font) {
/* 261 */       this.font = font;
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 266 */       graphics.text(this.font, CreateFlatWorldScreen.DetailsList.LAYER_MATERIAL_TITLE, getContentX(), getContentY(), -1);
/* 267 */       graphics.text(this.font, CreateFlatWorldScreen.DetailsList.HEIGHT_TITLE, getContentRight() - this.font.width((FormattedText)CreateFlatWorldScreen.DetailsList.HEIGHT_TITLE), getContentY(), -1);
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/* 272 */       return (Component)CommonComponents.joinForNarration(new Component[] { CreateFlatWorldScreen.DetailsList.LAYER_MATERIAL_TITLE, CreateFlatWorldScreen.DetailsList.HEIGHT_TITLE });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\CreateFlatWorldScreen$DetailsList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */