/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class ErrorScreen extends Screen {
/*    */   private final Component message;
/*    */   
/*    */   public ErrorScreen(Component title, Component message) {
/* 13 */     super(title);
/* 14 */     this.message = message;
/*    */   }
/*    */   
/*    */   protected void init() {
/* 19 */     super.init();
/* 21 */     addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> this.minecraft.gui.setScreen(null)).bounds(this.width / 2 - 100, 140, 200, 20).build());
/*    */   }
/*    */   
/*    */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 26 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/* 28 */     graphics.centeredText(this.font, this.title, this.width / 2, 90, -1);
/* 29 */     graphics.centeredText(this.font, this.message, this.width / 2, 110, -1);
/*    */   }
/*    */   
/*    */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 34 */     graphics.fillGradient(0, 0, this.width, this.height, -12574688, -11530224);
/*    */   }
/*    */   
/*    */   public boolean shouldCloseOnEsc() {
/* 39 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\ErrorScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */