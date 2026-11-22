/*     */ package net.minecraft.client.gui.screens.options;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.SortedMap;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.resources.language.LanguageInfo;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ class LanguageSelectionList extends ObjectSelectionList<LanguageSelectScreen.LanguageSelectionList.Entry> {
/*     */   public LanguageSelectionList(Minecraft minecraft) {
/* 110 */     super(minecraft, paramLanguageSelectScreen.width, paramLanguageSelectScreen.height - 33 - 53, 33, 18);
/* 112 */     String selectedLanguage = paramLanguageSelectScreen.languageManager.getSelected();
/* 113 */     paramLanguageSelectScreen.languageManager.getLanguages().forEach((code, info) -> {
/*     */           Entry entry = new Entry(code, info);
/*     */           addEntry((AbstractSelectionList.Entry)entry);
/*     */           if (selectedLanguage.equals(code))
/*     */             setSelected((AbstractSelectionList.Entry)entry); 
/*     */         });
/* 121 */     if (getSelected() != null)
/* 122 */       centerScrollOn(getSelected()); 
/*     */   }
/*     */   
/*     */   private void filterEntries(String filter) {
/* 127 */     SortedMap<String, LanguageInfo> languages = LanguageSelectScreen.this.languageManager.getLanguages();
/* 135 */     List<Entry> filteredEntries = languages.entrySet().stream().filter(entry -> (filter.isEmpty() || ((LanguageInfo)entry.getValue()).name().toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT)) || ((LanguageInfo)entry.getValue()).region().toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT)))).map(entry -> new Entry((String)entry.getKey(), (LanguageInfo)entry.getValue())).toList();
/* 137 */     replaceEntries(filteredEntries);
/* 138 */     refreshScrollAmount();
/*     */   }
/*     */   
/*     */   public int getRowWidth() {
/* 143 */     return super.getRowWidth() + 50;
/*     */   }
/*     */   
/*     */   public class Entry extends ObjectSelectionList.Entry<Entry> {
/*     */     private final String code;
/*     */     
/*     */     private final Component language;
/*     */     
/*     */     public Entry(String code, LanguageInfo language) {
/* 151 */       this.code = code;
/* 152 */       this.language = language.toComponent();
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 157 */       Objects.requireNonNull(LanguageSelectScreen.access$200(this.this$1.this$0));
/* 157 */       graphics.centeredText(LanguageSelectScreen.access$000(this.this$1.this$0), this.language, LanguageSelectScreen.LanguageSelectionList.this.width / 2, getContentYMiddle() - 9 / 2, -1);
/*     */     }
/*     */     
/*     */     public boolean keyPressed(KeyEvent event) {
/* 162 */       if (event.isSelection()) {
/* 163 */         select();
/* 164 */         this.this$1.this$0.onDone();
/* 165 */         return true;
/*     */       } 
/* 167 */       return super.keyPressed(event);
/*     */     }
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 172 */       select();
/* 173 */       if (doubleClick)
/* 174 */         this.this$1.this$0.onDone(); 
/* 176 */       return super.mouseClicked(event, doubleClick);
/*     */     }
/*     */     
/*     */     private void select() {
/* 180 */       LanguageSelectScreen.LanguageSelectionList.this.setSelected((AbstractSelectionList.Entry)this);
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/* 185 */       return (Component)Component.translatable("narrator.select", new Object[] { this.language });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\options\LanguageSelectScreen$LanguageSelectionList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */