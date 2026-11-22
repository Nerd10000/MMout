/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ 
/*     */ import net.minecraft.client.gui.components.AbstractSliderButton;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ class SettingsSlider extends AbstractSliderButton {
/*     */   private final double minValue;
/*     */   
/*     */   private final double maxValue;
/*     */   
/*     */   public SettingsSlider(int x, int y, int width, int currentValue, float minValue, float maxValue) {
/* 185 */     super(x, y, width, 20, CommonComponents.EMPTY, 0.0D);
/* 187 */     this.minValue = minValue;
/* 188 */     this.maxValue = maxValue;
/* 190 */     this.value = ((Mth.clamp(currentValue, minValue, maxValue) - minValue) / (maxValue - minValue));
/* 192 */     updateMessage();
/*     */   }
/*     */   
/*     */   public void applyValue() {
/* 197 */     if (!RealmsSlotOptionsScreen.this.spawnProtectionButton.active)
/*     */       return; 
/* 201 */     RealmsSlotOptionsScreen.this.spawnProtection = (int)Mth.lerp(Mth.clamp(this.value, 0.0D, 1.0D), this.minValue, this.maxValue);
/*     */   }
/*     */   
/*     */   protected void updateMessage() {
/* 206 */     setMessage((Component)CommonComponents.optionNameValue(RealmsSlotOptionsScreen.SPAWN_PROTECTION_TEXT, (RealmsSlotOptionsScreen.this.spawnProtection == 0) ? CommonComponents.OPTION_OFF : (Component)Component.literal(String.valueOf(RealmsSlotOptionsScreen.this.spawnProtection))));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsSlotOptionsScreen$SettingsSlider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */