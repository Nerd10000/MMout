/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.Hud;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.input.InputWithModifiers;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ 
/*     */ class BeaconPowerButton extends BeaconScreen.BeaconScreenButton {
/*     */   private final boolean isPrimary;
/*     */   
/*     */   protected final int tier;
/*     */   
/*     */   private Holder<MobEffect> effect;
/*     */   
/*     */   private Identifier sprite;
/*     */   
/*     */   public BeaconPowerButton(int x, int y, Holder<MobEffect> effect, boolean isPrimary, int tier) {
/* 193 */     super(x, y);
/* 194 */     this.isPrimary = isPrimary;
/* 195 */     this.tier = tier;
/* 196 */     setEffect(effect);
/*     */   }
/*     */   
/*     */   protected void setEffect(Holder<MobEffect> effect) {
/* 200 */     this.effect = effect;
/* 201 */     this.sprite = Hud.getMobEffectSprite(effect);
/* 202 */     setTooltip(Tooltip.create((Component)createEffectDescription(effect), null));
/*     */   }
/*     */   
/*     */   protected MutableComponent createEffectDescription(Holder<MobEffect> effect) {
/* 206 */     return Component.translatable(((MobEffect)effect.value()).getDescriptionId());
/*     */   }
/*     */   
/*     */   public void onPress(InputWithModifiers input) {
/* 211 */     if (isSelected())
/*     */       return; 
/* 215 */     if (this.isPrimary) {
/* 216 */       BeaconScreen.this.primary = this.effect;
/* 217 */       if (!Objects.equals(BeaconScreen.this.secondary, this.effect))
/* 218 */         BeaconScreen.this.secondary = null; 
/*     */     } else {
/* 221 */       BeaconScreen.this.secondary = this.effect;
/*     */     } 
/* 223 */     BeaconScreen.this.updateButtons();
/*     */   }
/*     */   
/*     */   protected void extractIcon(GuiGraphicsExtractor graphics) {
/* 228 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX() + 2, getY() + 2, 18, 18);
/*     */   }
/*     */   
/*     */   public void updateStatus(int levels) {
/* 233 */     this.active = (this.tier < levels);
/* 234 */     setSelected(this.effect.equals(this.isPrimary ? BeaconScreen.this.primary : BeaconScreen.this.secondary));
/*     */   }
/*     */   
/*     */   protected MutableComponent createNarrationMessage() {
/* 239 */     return createEffectDescription(this.effect);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\BeaconScreen$BeaconPowerButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */