/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ 
/*     */ import com.mojang.realmsclient.dto.WorldTemplate;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ 
/*     */ class WorldTemplateList extends ObjectSelectionList<RealmsSelectWorldTemplateScreen.Entry> {
/*     */   public WorldTemplateList(RealmsSelectWorldTemplateScreen paramRealmsSelectWorldTemplateScreen) {
/* 237 */     this(Collections.emptyList());
/*     */   }
/*     */   
/*     */   public WorldTemplateList(Iterable<WorldTemplate> templates) {
/* 241 */     super(Minecraft.getInstance(), paramRealmsSelectWorldTemplateScreen.width, paramRealmsSelectWorldTemplateScreen.layout.getContentHeight(), paramRealmsSelectWorldTemplateScreen.layout.getHeaderHeight(), 46);
/* 242 */     templates.forEach(this::addEntry);
/*     */   }
/*     */   
/*     */   public void addEntry(WorldTemplate template) {
/* 246 */     addEntry((AbstractSelectionList.Entry)new RealmsSelectWorldTemplateScreen.Entry(RealmsSelectWorldTemplateScreen.this, template));
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 251 */     if (RealmsSelectWorldTemplateScreen.this.currentLink != null) {
/* 252 */       ConfirmLinkScreen.confirmLinkNow((Screen)RealmsSelectWorldTemplateScreen.this, RealmsSelectWorldTemplateScreen.this.currentLink);
/* 253 */       return true;
/*     */     } 
/* 255 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   public void setSelected(RealmsSelectWorldTemplateScreen.Entry selected) {
/* 260 */     super.setSelected((AbstractSelectionList.Entry)selected);
/* 261 */     RealmsSelectWorldTemplateScreen.this.selectedTemplate = (selected == null) ? null : selected.template;
/* 262 */     RealmsSelectWorldTemplateScreen.this.updateButtonStates();
/*     */   }
/*     */   
/*     */   public int getRowWidth() {
/* 267 */     return 300;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 271 */     return (getItemCount() == 0);
/*     */   }
/*     */   
/*     */   public List<WorldTemplate> getTemplates() {
/* 275 */     return (List<WorldTemplate>)children().stream().map(c -> c.template).collect(Collectors.toList());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsSelectWorldTemplateScreen$WorldTemplateList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */