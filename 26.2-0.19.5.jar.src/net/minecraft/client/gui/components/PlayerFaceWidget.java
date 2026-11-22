/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.client.sounds.SoundManager;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ 
/*    */ public class PlayerFaceWidget extends AbstractWidget {
/*    */   private final ResolvableProfile skinProfile;
/*    */   
/*    */   public PlayerFaceWidget(int size, ResolvableProfile skinProfile) {
/* 13 */     super(0, 0, size, size, CommonComponents.EMPTY);
/* 14 */     this.skinProfile = skinProfile;
/* 15 */     this.active = false;
/*    */   }
/*    */   
/*    */   protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 20 */     PlayerFaceExtractor.extractRenderState(graphics, this.skinProfile, getX(), getY(), getWidth());
/*    */   }
/*    */   
/*    */   public void playDownSound(SoundManager soundManager) {}
/*    */   
/*    */   protected void updateWidgetNarration(NarrationElementOutput output) {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\PlayerFaceWidget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */