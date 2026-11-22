/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import com.ibm.icu.text.Collator;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ 
/*     */ class BiomeList extends ObjectSelectionList<CreateBuffetWorldScreen.BiomeList.Entry> {
/*     */   private BiomeList() {
/* 101 */     super(paramCreateBuffetWorldScreen.minecraft, paramCreateBuffetWorldScreen.width, paramCreateBuffetWorldScreen.layout.getContentHeight(), paramCreateBuffetWorldScreen.layout.getHeaderHeight(), 15);
/* 102 */     filterEntries("");
/*     */   }
/*     */   
/*     */   private void filterEntries(String filter) {
/* 106 */     Collator localeCollator = Collator.getInstance(Locale.getDefault());
/* 107 */     String lowercaseFilter = filter.toLowerCase(Locale.ROOT);
/* 112 */     List<Entry> list = CreateBuffetWorldScreen.this.biomes.listElements().map(x$0 -> new Entry(x$0)).sorted(Comparator.comparing(e -> e.name.getString(), (Comparator<?>)localeCollator)).filter(entry -> (filter.isEmpty() || entry.name.getString().toLowerCase(Locale.ROOT).contains(lowercaseFilter))).toList();
/* 114 */     replaceEntries(list);
/* 115 */     refreshScrollAmount();
/*     */   }
/*     */   
/*     */   public void setSelected(Entry selected) {
/* 120 */     super.setSelected((AbstractSelectionList.Entry)selected);
/* 122 */     if (selected != null)
/* 123 */       CreateBuffetWorldScreen.this.biome = (Holder<Biome>)selected.biome; 
/* 125 */     CreateBuffetWorldScreen.this.updateButtonValidity();
/*     */   }
/*     */   
/*     */   private class Entry extends ObjectSelectionList.Entry<Entry> {
/*     */     private final Holder.Reference<Biome> biome;
/*     */     
/*     */     private final Component name;
/*     */     
/*     */     public Entry(Holder.Reference<Biome> biome) {
/* 133 */       this.biome = biome;
/* 134 */       Identifier id = biome.key().identifier();
/* 136 */       String translationKey = id.toLanguageKey("biome");
/* 137 */       if (Language.getInstance().has(translationKey)) {
/* 138 */         this.name = (Component)Component.translatable(translationKey);
/*     */       } else {
/* 140 */         this.name = (Component)Component.literal(id.toString());
/*     */       } 
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/* 146 */       return (Component)Component.translatable("narrator.select", new Object[] { this.name });
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 151 */       graphics.text(this.this$1.this$0.font, this.name, getContentX() + 5, getContentY() + 2, -1);
/*     */     }
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 158 */       CreateBuffetWorldScreen.BiomeList.this.setSelected(this);
/* 159 */       return super.mouseClicked(event, doubleClick);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\CreateBuffetWorldScreen$BiomeList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */