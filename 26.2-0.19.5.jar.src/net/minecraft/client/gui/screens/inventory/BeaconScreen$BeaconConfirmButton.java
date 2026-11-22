/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.input.InputWithModifiers;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
/*     */ 
/*     */ class BeaconConfirmButton extends BeaconScreen.BeaconSpriteScreenButton {
/*     */   public BeaconConfirmButton(int x, int y) {
/* 282 */     super(x, y, BeaconScreen.CONFIRM_SPRITE, CommonComponents.GUI_DONE);
/*     */   }
/*     */   
/*     */   public void onPress(InputWithModifiers input) {
/* 287 */     BeaconScreen.access$000(BeaconScreen.this).getConnection().send((Packet)new ServerboundSetBeaconPacket(Optional.ofNullable(BeaconScreen.this.primary), Optional.ofNullable(BeaconScreen.this.secondary)));
/* 288 */     (BeaconScreen.access$100(BeaconScreen.this)).player.closeContainer();
/*     */   }
/*     */   
/*     */   public void updateStatus(int levels) {
/* 293 */     this.active = (BeaconScreen.this.menu.hasPayment() && BeaconScreen.this.primary != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\BeaconScreen$BeaconConfirmButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */