/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class Plain extends Button {
/*    */   protected Plain(int x, int y, int width, int height, Component message, Button.OnPress onPress, Button.CreateNarration createNarration) {
/* 30 */     super(x, y, width, height, message, onPress, createNarration);
/*    */   }
/*    */   
/*    */   protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 35 */     extractDefaultSprite(graphics);
/* 36 */     extractDefaultLabel(graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\Button$Plain.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */