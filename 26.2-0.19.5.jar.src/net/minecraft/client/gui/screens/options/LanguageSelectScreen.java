/*     */ package net.minecraft.client.gui.screens.options;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.SortedMap;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.resources.language.LanguageInfo;
/*     */ import net.minecraft.client.resources.language.LanguageManager;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class LanguageSelectScreen extends OptionsSubScreen {
/*  27 */   private static final Component WARNING_LABEL = (Component)Component.translatable("options.languageAccuracyWarning").withColor(-4539718);
/*     */   
/*     */   private static final int FOOTER_HEIGHT = 53;
/*     */   
/*  30 */   private static final Component SEARCH_HINT = (Component)Component.translatable("gui.language.search").withStyle(EditBox.SEARCH_HINT_STYLE);
/*     */   
/*     */   private static final int SEARCH_BOX_HEIGHT = 15;
/*     */   
/*     */   private final LanguageManager languageManager;
/*     */   
/*     */   private LanguageSelectionList languageSelectionList;
/*     */   
/*     */   private EditBox search;
/*     */   
/*     */   public LanguageSelectScreen(Screen lastScreen, Options options, LanguageManager languageManager) {
/*  39 */     super(lastScreen, options, (Component)Component.translatable("options.language.title"));
/*  40 */     this.languageManager = languageManager;
/*  41 */     this.layout.setFooterHeight(53);
/*     */   }
/*     */   
/*     */   protected void addTitle() {
/*  46 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical().spacing(4));
/*  47 */     header.defaultCellSetting().alignHorizontallyCenter();
/*  48 */     header.addChild((LayoutElement)new StringWidget(this.title, this.font));
/*  49 */     this.search = (EditBox)header.addChild((LayoutElement)new EditBox(this.font, 0, 0, 200, 15, (Component)Component.empty()));
/*  50 */     this.search.setHint(SEARCH_HINT);
/*  51 */     this.search.setResponder(string -> {
/*     */           if (this.languageSelectionList != null)
/*     */             this.languageSelectionList.filterEntries(string); 
/*     */         });
/*  56 */     Objects.requireNonNull(this.font);
/*  56 */     this.layout.setHeaderHeight((int)(12.0D + 9.0D + 15.0D));
/*     */   }
/*     */   
/*     */   protected void setInitialFocus() {
/*  61 */     if (this.search != null) {
/*  62 */       setInitialFocus((GuiEventListener)this.search);
/*     */     } else {
/*  64 */       super.setInitialFocus();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void addContents() {
/*  70 */     this.languageSelectionList = (LanguageSelectionList)this.layout.addToContents((LayoutElement)new LanguageSelectionList(this.minecraft));
/*     */   }
/*     */   
/*     */   protected void addOptions() {}
/*     */   
/*     */   protected void addFooter() {
/*  80 */     LinearLayout footer = ((LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.vertical())).spacing(8);
/*  81 */     footer.defaultCellSetting().alignHorizontallyCenter();
/*  82 */     footer.addChild((LayoutElement)new StringWidget(WARNING_LABEL, this.font));
/*  83 */     LinearLayout bottomButtons = (LinearLayout)footer.addChild((LayoutElement)LinearLayout.horizontal().spacing(8));
/*  84 */     bottomButtons.addChild((LayoutElement)Button.builder((Component)Component.translatable("options.font"), paramButton -> this.minecraft.gui.setScreen(new FontOptionsScreen(this, this.options))).build());
/*  85 */     bottomButtons.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onDone()).build());
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/*  90 */     super.repositionElements();
/*  91 */     if (this.languageSelectionList != null)
/*  92 */       this.languageSelectionList.updateSize(this.width, this.layout); 
/*     */   }
/*     */   
/*     */   private void onDone() {
/*  97 */     if (this.languageSelectionList != null && this.languageSelectionList.getSelected() != null) {
/*  98 */       LanguageSelectionList.Entry selectedEntry = (LanguageSelectionList.Entry)this.languageSelectionList.getSelected();
/*  99 */       if (!selectedEntry.code.equals(this.languageManager.getSelected())) {
/* 100 */         this.languageManager.setSelected(selectedEntry.code);
/* 101 */         this.options.languageCode = selectedEntry.code;
/* 102 */         this.minecraft.reloadResourcePacks();
/*     */       } 
/*     */     } 
/* 105 */     this.minecraft.gui.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   private class LanguageSelectionList extends ObjectSelectionList<LanguageSelectionList.Entry> {
/*     */     public LanguageSelectionList(Minecraft minecraft) {
/* 110 */       super(minecraft, LanguageSelectScreen.this.width, LanguageSelectScreen.this.height - 33 - 53, 33, 18);
/* 112 */       String selectedLanguage = LanguageSelectScreen.this.languageManager.getSelected();
/* 113 */       LanguageSelectScreen.this.languageManager.getLanguages().forEach((code, info) -> {
/*     */             Entry entry = new Entry(code, info);
/*     */             addEntry((AbstractSelectionList.Entry)entry);
/*     */             if (selectedLanguage.equals(code))
/*     */               setSelected((AbstractSelectionList.Entry)entry); 
/*     */           });
/* 121 */       if (getSelected() != null)
/* 122 */         centerScrollOn(getSelected()); 
/*     */     }
/*     */     
/*     */     private void filterEntries(String filter) {
/* 127 */       SortedMap<String, LanguageInfo> languages = LanguageSelectScreen.this.languageManager.getLanguages();
/* 135 */       List<Entry> filteredEntries = languages.entrySet().stream().filter(entry -> (filter.isEmpty() || ((LanguageInfo)entry.getValue()).name().toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT)) || ((LanguageInfo)entry.getValue()).region().toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT)))).map(entry -> new Entry((String)entry.getKey(), (LanguageInfo)entry.getValue())).toList();
/* 137 */       replaceEntries(filteredEntries);
/* 138 */       refreshScrollAmount();
/*     */     }
/*     */     
/*     */     public int getRowWidth() {
/* 143 */       return super.getRowWidth() + 50;
/*     */     }
/*     */     
/*     */     public class Entry extends ObjectSelectionList.Entry<Entry> {
/*     */       private final String code;
/*     */       
/*     */       private final Component language;
/*     */       
/*     */       public Entry(String code, LanguageInfo language) {
/* 151 */         this.code = code;
/* 152 */         this.language = language.toComponent();
/*     */       }
/*     */       
/*     */       public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 157 */         Objects.requireNonNull(LanguageSelectScreen.this.font);
/* 157 */         graphics.centeredText(LanguageSelectScreen.this.font, this.language, LanguageSelectScreen.LanguageSelectionList.this.width / 2, getContentYMiddle() - 9 / 2, -1);
/*     */       }
/*     */       
/*     */       public boolean keyPressed(KeyEvent event) {
/* 162 */         if (event.isSelection()) {
/* 163 */           select();
/* 164 */           LanguageSelectScreen.this.onDone();
/* 165 */           return true;
/*     */         } 
/* 167 */         return super.keyPressed(event);
/*     */       }
/*     */       
/*     */       public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 172 */         select();
/* 173 */         if (doubleClick)
/* 174 */           LanguageSelectScreen.this.onDone(); 
/* 176 */         return super.mouseClicked(event, doubleClick);
/*     */       }
/*     */       
/*     */       private void select() {
/* 180 */         LanguageSelectScreen.LanguageSelectionList.this.setSelected((AbstractSelectionList.Entry)this);
/*     */       }
/*     */       
/*     */       public Component getNarration() {
/* 185 */         return (Component)Component.translatable("narrator.select", new Object[] { this.language });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public class Entry extends ObjectSelectionList.Entry<LanguageSelectionList.Entry> {
/*     */     private final String code;
/*     */     
/*     */     private final Component language;
/*     */     
/*     */     public Entry(String code, LanguageInfo language) {
/*     */       this.code = code;
/*     */       this.language = language.toComponent();
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */       Objects.requireNonNull(LanguageSelectScreen.this.font);
/*     */       graphics.centeredText(LanguageSelectScreen.this.font, this.language, LanguageSelectScreen.LanguageSelectionList.this.width / 2, getContentYMiddle() - 9 / 2, -1);
/*     */     }
/*     */     
/*     */     public boolean keyPressed(KeyEvent event) {
/*     */       if (event.isSelection()) {
/*     */         select();
/*     */         LanguageSelectScreen.this.onDone();
/*     */         return true;
/*     */       } 
/*     */       return super.keyPressed(event);
/*     */     }
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/*     */       select();
/*     */       if (doubleClick)
/*     */         LanguageSelectScreen.this.onDone(); 
/*     */       return super.mouseClicked(event, doubleClick);
/*     */     }
/*     */     
/*     */     private void select() {
/*     */       LanguageSelectScreen.LanguageSelectionList.this.setSelected((AbstractSelectionList.Entry)this);
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/* 185 */       return (Component)Component.translatable("narrator.select", new Object[] { this.language });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\options\LanguageSelectScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */