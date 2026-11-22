/*    */ package net.minecraft.client.gui.components.tabs;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ 
/*    */ public class Builder extends TabNavigationBar.Builder {
/*    */   private Builder(TabManager tabManager, int width) {
/* 27 */     super(tabManager, 0, 0, width, 24);
/*    */   }
/*    */   
/*    */   public Builder addTab(Tab tab) {
/* 31 */     addTab(new MenuTabBar.MenuTabButton(this.tabManager, tab, 0, this.height), tab);
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public Builder addTabs(Tab... tabs) {
/* 36 */     for (Tab tab : tabs)
/* 37 */       addTab(tab); 
/* 39 */     return this;
/*    */   }
/*    */   
/*    */   public MenuTabBar build() {
/* 44 */     return new MenuTabBar(this.x, this.y, this.width, this.height, this.tabManager, ImmutableList.copyOf(this.tabButtons), ImmutableList.copyOf(this.tabs));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\tabs\MenuTabBar$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */