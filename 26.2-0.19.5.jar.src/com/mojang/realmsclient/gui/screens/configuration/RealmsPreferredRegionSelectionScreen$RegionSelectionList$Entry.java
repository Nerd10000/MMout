/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ 
/*     */ import com.mojang.realmsclient.dto.RealmsRegion;
/*     */ import com.mojang.realmsclient.dto.RegionSelectionPreference;
/*     */ import com.mojang.realmsclient.dto.ServiceQuality;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ class Entry extends ObjectSelectionList.Entry<RealmsPreferredRegionSelectionScreen.RegionSelectionList.Entry> {
/*     */   private final RealmsSettingsTab.RegionSelection regionSelection;
/*     */   
/*     */   private final Component name;
/*     */   
/*     */   public Entry(RealmsPreferredRegionSelectionScreen.RegionSelectionList paramRegionSelectionList, RegionSelectionPreference preference, RealmsRegion region) {
/* 122 */     this(new RealmsSettingsTab.RegionSelection(preference, region));
/*     */   }
/*     */   
/*     */   public Entry(RealmsSettingsTab.RegionSelection regionSelection) {
/* 126 */     this.regionSelection = regionSelection;
/* 127 */     if (regionSelection.preference() == RegionSelectionPreference.MANUAL) {
/* 128 */       if (regionSelection.region() != null) {
/* 129 */         this.name = (Component)Component.translatable((regionSelection.region()).translationKey);
/*     */       } else {
/* 131 */         this.name = (Component)Component.empty();
/*     */       } 
/*     */     } else {
/* 134 */       this.name = (Component)Component.translatable((regionSelection.preference()).translationKey);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 140 */     return (Component)Component.translatable("narrator.select", new Object[] { this.name });
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 145 */     graphics.text(RealmsPreferredRegionSelectionScreen.access$100(this.this$1.this$0), this.name, getContentX() + 5, getContentY() + 2, -1);
/* 146 */     if (this.regionSelection.region() != null && this.this$1.this$0.regionServiceQuality.containsKey(this.regionSelection.region())) {
/* 147 */       ServiceQuality serviceQuality = this.this$1.this$0.regionServiceQuality.getOrDefault(this.regionSelection.region(), ServiceQuality.UNKNOWN);
/* 148 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, serviceQuality.getIcon(), getContentRight() - 18, getContentY() + 2, 10, 8);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 154 */     RealmsPreferredRegionSelectionScreen.RegionSelectionList.this.setSelected(this);
/* 155 */     if (doubleClick) {
/* 156 */       RealmsPreferredRegionSelectionScreen.RegionSelectionList.this.playDownSound(RealmsPreferredRegionSelectionScreen.RegionSelectionList.access$200(RealmsPreferredRegionSelectionScreen.RegionSelectionList.this).getSoundManager());
/* 157 */       this.this$1.this$0.onDone();
/* 158 */       return true;
/*     */     } 
/* 160 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 165 */     if (event.isSelection()) {
/* 166 */       RealmsPreferredRegionSelectionScreen.RegionSelectionList.this.playDownSound(RealmsPreferredRegionSelectionScreen.RegionSelectionList.access$300(RealmsPreferredRegionSelectionScreen.RegionSelectionList.this).getSoundManager());
/* 167 */       this.this$1.this$0.onDone();
/* 168 */       return true;
/*     */     } 
/* 170 */     return super.keyPressed(event);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsPreferredRegionSelectionScreen$RegionSelectionList$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */