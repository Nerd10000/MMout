/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ 
/*     */ class Entry extends ObjectSelectionList.Entry<CreateBuffetWorldScreen.BiomeList.Entry> {
/*     */   private final Holder.Reference<Biome> biome;
/*     */   
/*     */   private final Component name;
/*     */   
/*     */   public Entry(Holder.Reference<Biome> biome) {
/* 133 */     this.biome = biome;
/* 134 */     Identifier id = biome.key().identifier();
/* 136 */     String translationKey = id.toLanguageKey("biome");
/* 137 */     if (Language.getInstance().has(translationKey)) {
/* 138 */       this.name = (Component)Component.translatable(translationKey);
/*     */     } else {
/* 140 */       this.name = (Component)Component.literal(id.toString());
/*     */     } 
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 146 */     return (Component)Component.translatable("narrator.select", new Object[] { this.name });
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 151 */     graphics.text(this.this$1.this$0.font, this.name, getContentX() + 5, getContentY() + 2, -1);
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 158 */     CreateBuffetWorldScreen.BiomeList.this.setSelected(this);
/* 159 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\CreateBuffetWorldScreen$BiomeList$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */