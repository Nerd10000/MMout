/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ 
/*     */ class BeaconUpgradePowerButton extends BeaconScreen.BeaconPowerButton {
/*     */   public BeaconUpgradePowerButton(int x, int y, Holder<MobEffect> effect) {
/* 245 */     super(x, y, effect, false, 3);
/*     */   }
/*     */   
/*     */   protected MutableComponent createEffectDescription(Holder<MobEffect> effect) {
/* 250 */     return Component.translatable(((MobEffect)effect.value()).getDescriptionId()).append(" II");
/*     */   }
/*     */   
/*     */   public void updateStatus(int levels) {
/* 255 */     if (BeaconScreen.this.primary != null) {
/* 256 */       this.visible = true;
/* 257 */       setEffect(BeaconScreen.this.primary);
/* 258 */       super.updateStatus(levels);
/*     */     } else {
/* 260 */       this.visible = false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\BeaconScreen$BeaconUpgradePowerButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */