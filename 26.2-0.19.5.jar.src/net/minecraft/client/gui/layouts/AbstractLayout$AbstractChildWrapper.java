/*    */ package net.minecraft.client.gui.layouts;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class AbstractChildWrapper {
/*    */   public final LayoutElement child;
/*    */   
/*    */   public final LayoutSettings.LayoutSettingsImpl layoutSettings;
/*    */   
/*    */   protected AbstractChildWrapper(LayoutElement child, LayoutSettings layoutSettings) {
/* 61 */     this.child = child;
/* 62 */     this.layoutSettings = layoutSettings.getExposed();
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 66 */     return this.child.getHeight() + this.layoutSettings.paddingTop + this.layoutSettings.paddingBottom;
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 70 */     return this.child.getWidth() + this.layoutSettings.paddingLeft + this.layoutSettings.paddingRight;
/*    */   }
/*    */   
/*    */   public void setX(int x, int availableSpace) {
/* 74 */     float leastOffset = this.layoutSettings.paddingLeft;
/* 75 */     float mostOffset = (availableSpace - this.child.getWidth() - this.layoutSettings.paddingRight);
/* 76 */     int offset = (int)Mth.lerp(this.layoutSettings.xAlignment, leastOffset, mostOffset);
/* 77 */     this.child.setX(offset + x);
/*    */   }
/*    */   
/*    */   public void setY(int y, int availableSpace) {
/* 81 */     float leastOffset = this.layoutSettings.paddingTop;
/* 82 */     float mostOffset = (availableSpace - this.child.getHeight() - this.layoutSettings.paddingBottom);
/* 83 */     int offset = Math.round(Mth.lerp(this.layoutSettings.yAlignment, leastOffset, mostOffset));
/* 84 */     this.child.setY(offset + y);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\layouts\AbstractLayout$AbstractChildWrapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */