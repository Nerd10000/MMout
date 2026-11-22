/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
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
/*     */ class LayerEntry extends CreateFlatWorldScreen.DetailsList.Entry {
/*     */   private final FlatLayerInfo layerInfo;
/*     */   
/*     */   private final int index;
/*     */   
/*     */   public LayerEntry(FlatLayerInfo layerInfo, int index) {
/* 186 */     this.layerInfo = layerInfo;
/* 187 */     this.index = index;
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */     MutableComponent mutableComponent;
/* 192 */     BlockState blockState = this.layerInfo.getBlockState();
/* 194 */     ItemStack itemStack = getDisplayItem(blockState);
/* 195 */     blitSlot(graphics, getContentX(), getContentY(), itemStack);
/* 197 */     Objects.requireNonNull(this.this$1.this$0.font);
/* 197 */     int y = getContentYMiddle() - 9 / 2;
/* 198 */     graphics.text(this.this$1.this$0.font, itemStack.getHoverName(), getContentX() + 18 + 5, y, -1);
/* 201 */     if (this.index == 0) {
/* 202 */       mutableComponent = Component.translatable("createWorld.customize.flat.layer.top", new Object[] { Integer.valueOf(this.layerInfo.getHeight()) });
/* 203 */     } else if (this.index == this.this$1.this$0.generator.getLayersInfo().size() - 1) {
/* 204 */       mutableComponent = Component.translatable("createWorld.customize.flat.layer.bottom", new Object[] { Integer.valueOf(this.layerInfo.getHeight()) });
/*     */     } else {
/* 206 */       mutableComponent = Component.translatable("createWorld.customize.flat.layer", new Object[] { Integer.valueOf(this.layerInfo.getHeight()) });
/*     */     } 
/* 209 */     graphics.text(this.this$1.this$0.font, (Component)mutableComponent, getContentRight() - this.this$1.this$0.font.width((FormattedText)mutableComponent), y, -1);
/*     */   }
/*     */   
/*     */   private ItemStack getDisplayItem(BlockState blockState) {
/* 213 */     Item item = blockState.getBlock().asItem();
/* 214 */     if (item == Items.AIR)
/* 215 */       if (blockState.is(Blocks.WATER)) {
/* 216 */         item = Items.WATER_BUCKET;
/* 217 */       } else if (blockState.is(Blocks.LAVA)) {
/* 218 */         item = Items.LAVA_BUCKET;
/*     */       }  
/* 221 */     return new ItemStack((ItemLike)item);
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 226 */     ItemStack itemStack = getDisplayItem(this.layerInfo.getBlockState());
/* 227 */     if (!itemStack.isEmpty())
/* 228 */       return (Component)CommonComponents.joinForNarration(new Component[] { (Component)Component.translatable("narrator.select", new Object[] { itemStack.getHoverName() }), CreateFlatWorldScreen.DetailsList.HEIGHT_TITLE, 
/*     */             
/* 231 */             (Component)Component.literal(String.valueOf(this.layerInfo.getHeight())) }); 
/* 234 */     return CommonComponents.EMPTY;
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 240 */     CreateFlatWorldScreen.DetailsList.this.setSelected(this);
/* 241 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   private void blitSlot(GuiGraphicsExtractor graphics, int x, int y, ItemStack itemStack) {
/* 245 */     blitSlotBg(graphics, x + 1, y + 1);
/* 247 */     if (!itemStack.isEmpty())
/* 248 */       graphics.fakeItem(itemStack, x + 2, y + 2); 
/*     */   }
/*     */   
/*     */   private void blitSlotBg(GuiGraphicsExtractor graphics, int x, int y) {
/* 253 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, CreateFlatWorldScreen.SLOT_SPRITE, x, y, 18, 18);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\CreateFlatWorldScreen$DetailsList$LayerEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */