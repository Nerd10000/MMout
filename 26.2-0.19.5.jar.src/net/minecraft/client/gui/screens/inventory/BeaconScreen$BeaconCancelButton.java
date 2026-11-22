/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import net.minecraft.client.input.InputWithModifiers;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ 
/*     */ class BeaconCancelButton extends BeaconScreen.BeaconSpriteScreenButton {
/*     */   public BeaconCancelButton(int x, int y) {
/* 299 */     super(x, y, BeaconScreen.CANCEL_SPRITE, CommonComponents.GUI_CANCEL);
/*     */   }
/*     */   
/*     */   public void onPress(InputWithModifiers input) {
/* 304 */     (BeaconScreen.access$200(BeaconScreen.this)).player.closeContainer();
/*     */   }
/*     */   
/*     */   public void updateStatus(int levels) {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\BeaconScreen$BeaconCancelButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */