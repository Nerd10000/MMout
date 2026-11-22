/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ 
/*    */ public abstract class AbstractOptionSliderButton extends AbstractSliderButton {
/*    */   protected final Options options;
/*    */   
/*    */   protected AbstractOptionSliderButton(Options options, int x, int y, int width, int height, double initialValue) {
/* 10 */     super(x, y, width, height, CommonComponents.EMPTY, initialValue);
/* 11 */     this.options = options;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\AbstractOptionSliderButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */