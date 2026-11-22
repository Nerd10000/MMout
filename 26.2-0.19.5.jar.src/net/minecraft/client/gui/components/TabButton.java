/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.gui.components.tabs.Tab;
/*    */ import net.minecraft.client.gui.components.tabs.TabManager;
/*    */ import net.minecraft.client.gui.narration.NarratedElementType;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.client.sounds.SoundManager;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public abstract class TabButton extends AbstractWidget.WithInactiveMessage {
/*    */   private final TabManager tabManager;
/*    */   
/*    */   private final Tab tab;
/*    */   
/*    */   public TabButton(TabManager tabManager, Tab tab, int width, int height) {
/* 15 */     super(0, 0, width, height, tab.getTabTitle());
/* 16 */     this.tabManager = tabManager;
/* 17 */     this.tab = tab;
/*    */   }
/*    */   
/*    */   protected void updateWidgetNarration(NarrationElementOutput output) {
/* 22 */     output.add(NarratedElementType.TITLE, (Component)Component.translatable("gui.narrate.tab", new Object[] { this.tab.getTabTitle() }));
/* 23 */     output.add(NarratedElementType.HINT, tab().getTabExtraNarration());
/*    */   }
/*    */   
/*    */   public void playDownSound(SoundManager soundManager) {}
/*    */   
/*    */   public Tab tab() {
/* 31 */     return this.tab;
/*    */   }
/*    */   
/*    */   public boolean isSelected() {
/* 35 */     return (this.tabManager.getCurrentTab() == this.tab);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\TabButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */