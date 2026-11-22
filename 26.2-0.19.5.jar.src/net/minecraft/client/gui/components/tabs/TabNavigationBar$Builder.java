/*    */ package net.minecraft.client.gui.components.tabs;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.components.TabButton;
/*    */ 
/*    */ public class Builder {
/*    */   protected final int x;
/*    */   
/*    */   protected final int y;
/*    */   
/*    */   protected final int width;
/*    */   
/*    */   protected final int height;
/*    */   
/*    */   protected final TabManager tabManager;
/*    */   
/* 69 */   protected final List<TabButton> tabButtons = new ArrayList<>();
/*    */   
/* 70 */   protected final List<Tab> tabs = new ArrayList<>();
/*    */   
/*    */   protected Builder(TabManager tabManager, int x, int y, int width, int height) {
/* 73 */     this.tabManager = tabManager;
/* 74 */     this.x = x;
/* 75 */     this.y = y;
/* 76 */     this.width = width;
/* 77 */     this.height = height;
/*    */   }
/*    */   
/*    */   public Builder addTab(TabButton button, Tab tab) {
/* 81 */     this.tabButtons.add(button);
/* 82 */     this.tabs.add(tab);
/* 83 */     return this;
/*    */   }
/*    */   
/*    */   public TabNavigationBar build() {
/* 87 */     return new TabNavigationBar(this.x, this.y, this.width, this.height, this.tabManager, ImmutableList.copyOf(this.tabButtons), ImmutableList.copyOf(this.tabs));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\tabs\TabNavigationBar$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */