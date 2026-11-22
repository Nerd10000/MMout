/*     */ package net.minecraft.client.gui.screens.achievement;
/*     */ 
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.tabs.GridLayoutTab;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ class StatisticsTab extends GridLayoutTab {
/*     */   protected final AbstractSelectionList<?> list;
/*     */   
/*     */   public StatisticsTab(Component title, AbstractSelectionList<?> list) {
/* 190 */     super(title);
/* 191 */     this.layout.addChild((LayoutElement)list, 1, 1);
/* 192 */     this.list = list;
/*     */   }
/*     */   
/*     */   public void doLayout(ScreenRectangle screenRectangle) {
/* 197 */     this.list.updateSizeAndPosition(StatsScreen.this.width, StatsScreen.this.layout.getContentHeight(), StatsScreen.this.layout.getHeaderHeight());
/* 198 */     super.doLayout(screenRectangle);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\achievement\StatsScreen$StatisticsTab.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */