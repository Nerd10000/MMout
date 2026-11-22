/*    */ package net.minecraft.client.gui.components.tabs;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*    */ import net.minecraft.client.resources.sounds.SoundInstance;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ 
/*    */ public class TabManager {
/*    */   private final Consumer<AbstractWidget> addWidget;
/*    */   
/*    */   private final Consumer<AbstractWidget> removeWidget;
/*    */   
/*    */   private final Consumer<Tab> onSelected;
/*    */   
/*    */   private final Consumer<Tab> onDeselected;
/*    */   
/*    */   private Tab currentTab;
/*    */   
/*    */   private ScreenRectangle tabArea;
/*    */   
/*    */   public TabManager(Consumer<AbstractWidget> addWidget, Consumer<AbstractWidget> removeWidget) {
/* 22 */     this(addWidget, removeWidget, t -> {
/*    */         
/*    */         }t -> {
/*    */         
/*    */         });
/*    */   }
/*    */   
/*    */   public TabManager(Consumer<AbstractWidget> addWidget, Consumer<AbstractWidget> removeWidget, Consumer<Tab> onSelected, Consumer<Tab> onDeselected) {
/* 26 */     this.addWidget = addWidget;
/* 27 */     this.removeWidget = removeWidget;
/* 28 */     this.onSelected = onSelected;
/* 29 */     this.onDeselected = onDeselected;
/*    */   }
/*    */   
/*    */   public void setTabArea(ScreenRectangle tabArea) {
/* 33 */     this.tabArea = tabArea;
/* 34 */     Tab tab = getCurrentTab();
/* 35 */     if (tab != null)
/* 36 */       tab.doLayout(tabArea); 
/*    */   }
/*    */   
/*    */   public void setCurrentTab(Tab tab, boolean playSound) {
/* 41 */     setCurrentTab(tab, playSound, true);
/*    */   }
/*    */   
/*    */   public void setCurrentTab(Tab tab, boolean playSound, boolean addWidget) {
/* 45 */     if (!Objects.equals(this.currentTab, tab)) {
/* 46 */       if (this.currentTab != null)
/* 47 */         this.currentTab.visitChildren(this.removeWidget); 
/* 49 */       Tab oldTab = this.currentTab;
/* 50 */       this.currentTab = tab;
/* 51 */       if (addWidget)
/* 52 */         tab.visitChildren(this.addWidget); 
/* 54 */       if (this.tabArea != null)
/* 55 */         tab.doLayout(this.tabArea); 
/* 57 */       if (playSound)
/* 58 */         Minecraft.getInstance().getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F)); 
/* 60 */       this.onDeselected.accept(oldTab);
/* 61 */       this.onSelected.accept(this.currentTab);
/*    */     } 
/*    */   }
/*    */   
/*    */   public Tab getCurrentTab() {
/* 66 */     return this.currentTab;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\tabs\TabManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */