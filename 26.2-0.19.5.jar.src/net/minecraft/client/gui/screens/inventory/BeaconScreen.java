/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.Hud;
/*     */ import net.minecraft.client.gui.components.AbstractButton;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.input.InputWithModifiers;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.BeaconMenu;
/*     */ import net.minecraft.world.inventory.ContainerListener;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.entity.BeaconBlockEntity;
/*     */ 
/*     */ public class BeaconScreen extends AbstractContainerScreen<BeaconMenu> {
/*  34 */   private static final Identifier BEACON_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/beacon.png");
/*     */   
/*  35 */   private static final Identifier BUTTON_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/beacon/button_disabled");
/*     */   
/*  36 */   private static final Identifier BUTTON_SELECTED_SPRITE = Identifier.withDefaultNamespace("container/beacon/button_selected");
/*     */   
/*  37 */   private static final Identifier BUTTON_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("container/beacon/button_highlighted");
/*     */   
/*  38 */   private static final Identifier BUTTON_SPRITE = Identifier.withDefaultNamespace("container/beacon/button");
/*     */   
/*  39 */   private static final Identifier CONFIRM_SPRITE = Identifier.withDefaultNamespace("container/beacon/confirm");
/*     */   
/*  40 */   private static final Identifier CANCEL_SPRITE = Identifier.withDefaultNamespace("container/beacon/cancel");
/*     */   
/*  41 */   private static final Component PRIMARY_EFFECT_LABEL = (Component)Component.translatable("block.minecraft.beacon.primary");
/*     */   
/*  42 */   private static final Component SECONDARY_EFFECT_LABEL = (Component)Component.translatable("block.minecraft.beacon.secondary");
/*     */   
/*  44 */   private final List<BeaconButton> beaconButtons = Lists.newArrayList();
/*     */   
/*     */   private Holder<MobEffect> primary;
/*     */   
/*     */   private Holder<MobEffect> secondary;
/*     */   
/*     */   public BeaconScreen(final BeaconMenu menu, Inventory inventory, Component title) {
/*  49 */     super(menu, inventory, title, 230, 219);
/*  51 */     menu.addSlotListener(new ContainerListener() {
/*     */           public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) {}
/*     */           
/*     */           public void dataChanged(AbstractContainerMenu container, int id, int value) {
/*  58 */             BeaconScreen.this.primary = menu.getPrimaryEffect();
/*  59 */             BeaconScreen.this.secondary = menu.getSecondaryEffect();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private <T extends net.minecraft.client.gui.components.AbstractWidget & BeaconButton> void addBeaconButton(T beaconButton) {
/*  65 */     addRenderableWidget((GuiEventListener)beaconButton);
/*  66 */     this.beaconButtons.add((BeaconButton)beaconButton);
/*     */   }
/*     */   
/*     */   protected void init() {
/*  71 */     super.init();
/*  73 */     this.beaconButtons.clear();
/*     */     int tier;
/*  75 */     for (tier = 0; tier <= 2; tier++) {
/*  76 */       int i = ((List)BeaconBlockEntity.BEACON_EFFECTS.get(tier)).size();
/*  77 */       int j = i * 22 + (i - 1) * 2;
/*  79 */       for (int k = 0; k < i; k++) {
/*  80 */         Holder<MobEffect> effect = ((List<Holder<MobEffect>>)BeaconBlockEntity.BEACON_EFFECTS.get(tier)).get(k);
/*  81 */         BeaconPowerButton beaconPowerButton1 = new BeaconPowerButton(this.leftPos + 76 + k * 24 - j / 2, this.topPos + 22 + tier * 25, effect, true, tier);
/*  82 */         beaconPowerButton1.active = false;
/*  83 */         addBeaconButton(beaconPowerButton1);
/*     */       } 
/*     */     } 
/*  86 */     tier = 3;
/*  88 */     int count = ((List)BeaconBlockEntity.BEACON_EFFECTS.get(3)).size() + 1;
/*  89 */     int totalWidth = count * 22 + (count - 1) * 2;
/*  91 */     for (int c = 0; c < count - 1; c++) {
/*  92 */       Holder<MobEffect> effect = ((List<Holder<MobEffect>>)BeaconBlockEntity.BEACON_EFFECTS.get(3)).get(c);
/*  93 */       BeaconPowerButton beaconPowerButton1 = new BeaconPowerButton(this.leftPos + 167 + c * 24 - totalWidth / 2, this.topPos + 47, effect, false, 3);
/*  94 */       beaconPowerButton1.active = false;
/*  95 */       addBeaconButton(beaconPowerButton1);
/*     */     } 
/*  99 */     Holder<MobEffect> dummyEffect = ((List<Holder<MobEffect>>)BeaconBlockEntity.BEACON_EFFECTS.get(0)).get(0);
/* 100 */     BeaconPowerButton beaconPowerButton = new BeaconUpgradePowerButton(this.leftPos + 167 + (count - 1) * 24 - totalWidth / 2, this.topPos + 47, dummyEffect);
/* 101 */     beaconPowerButton.visible = false;
/* 102 */     addBeaconButton(beaconPowerButton);
/* 104 */     addBeaconButton(new BeaconConfirmButton(this.leftPos + 164, this.topPos + 107));
/* 105 */     addBeaconButton(new BeaconCancelButton(this.leftPos + 190, this.topPos + 107));
/*     */   }
/*     */   
/*     */   public void containerTick() {
/* 110 */     super.containerTick();
/* 111 */     updateButtons();
/*     */   }
/*     */   
/*     */   private void updateButtons() {
/* 115 */     int levels = this.menu.getLevels();
/* 116 */     this.beaconButtons.forEach(b -> b.updateStatus(levels));
/*     */   }
/*     */   
/*     */   protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
/* 121 */     graphics.centeredText(this.font, PRIMARY_EFFECT_LABEL, 62, 10, -2039584);
/* 122 */     graphics.centeredText(this.font, SECONDARY_EFFECT_LABEL, 169, 10, -2039584);
/*     */   }
/*     */   
/*     */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 127 */     super.extractBackground(graphics, mouseX, mouseY, a);
/* 128 */     int xo = (this.width - this.imageWidth) / 2;
/* 129 */     int yo = (this.height - this.imageHeight) / 2;
/* 130 */     graphics.blit(RenderPipelines.GUI_TEXTURED, BEACON_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/* 131 */     graphics.item(new ItemStack((ItemLike)Items.NETHERITE_INGOT), xo + 20, yo + 109);
/* 132 */     graphics.item(new ItemStack((ItemLike)Items.EMERALD), xo + 41, yo + 109);
/* 133 */     graphics.item(new ItemStack((ItemLike)Items.DIAMOND), xo + 41 + 22, yo + 109);
/* 134 */     graphics.item(new ItemStack((ItemLike)Items.GOLD_INGOT), xo + 42 + 44, yo + 109);
/* 135 */     graphics.item(new ItemStack((ItemLike)Items.IRON_INGOT), xo + 42 + 66, yo + 109);
/*     */   }
/*     */   
/*     */   private static abstract class BeaconScreenButton extends AbstractButton implements BeaconButton {
/*     */     private boolean selected;
/*     */     
/*     */     protected BeaconScreenButton(int x, int y) {
/* 146 */       super(x, y, 22, 22, CommonComponents.EMPTY);
/*     */     }
/*     */     
/*     */     protected BeaconScreenButton(int x, int y, Component component) {
/* 150 */       super(x, y, 22, 22, component);
/*     */     }
/*     */     
/*     */     public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*     */       Identifier sprite;
/* 156 */       if (!this.active) {
/* 157 */         sprite = BeaconScreen.BUTTON_DISABLED_SPRITE;
/* 158 */       } else if (this.selected) {
/* 159 */         sprite = BeaconScreen.BUTTON_SELECTED_SPRITE;
/* 160 */       } else if (isHoveredOrFocused()) {
/* 161 */         sprite = BeaconScreen.BUTTON_HIGHLIGHTED_SPRITE;
/*     */       } else {
/* 163 */         sprite = BeaconScreen.BUTTON_SPRITE;
/*     */       } 
/* 166 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), this.width, this.height);
/* 167 */       extractIcon(graphics);
/*     */     }
/*     */     
/*     */     protected abstract void extractIcon(GuiGraphicsExtractor param1GuiGraphicsExtractor);
/*     */     
/*     */     public boolean isSelected() {
/* 173 */       return this.selected;
/*     */     }
/*     */     
/*     */     public void setSelected(boolean selected) {
/* 177 */       this.selected = selected;
/*     */     }
/*     */     
/*     */     public void updateWidgetNarration(NarrationElementOutput output) {
/* 182 */       defaultButtonNarrationText(output);
/*     */     }
/*     */   }
/*     */   
/*     */   private class BeaconPowerButton extends BeaconScreenButton {
/*     */     private final boolean isPrimary;
/*     */     
/*     */     protected final int tier;
/*     */     
/*     */     private Holder<MobEffect> effect;
/*     */     
/*     */     private Identifier sprite;
/*     */     
/*     */     public BeaconPowerButton(int x, int y, Holder<MobEffect> effect, boolean isPrimary, int tier) {
/* 193 */       super(x, y);
/* 194 */       this.isPrimary = isPrimary;
/* 195 */       this.tier = tier;
/* 196 */       setEffect(effect);
/*     */     }
/*     */     
/*     */     protected void setEffect(Holder<MobEffect> effect) {
/* 200 */       this.effect = effect;
/* 201 */       this.sprite = Hud.getMobEffectSprite(effect);
/* 202 */       setTooltip(Tooltip.create((Component)createEffectDescription(effect), null));
/*     */     }
/*     */     
/*     */     protected MutableComponent createEffectDescription(Holder<MobEffect> effect) {
/* 206 */       return Component.translatable(((MobEffect)effect.value()).getDescriptionId());
/*     */     }
/*     */     
/*     */     public void onPress(InputWithModifiers input) {
/* 211 */       if (isSelected())
/*     */         return; 
/* 215 */       if (this.isPrimary) {
/* 216 */         BeaconScreen.this.primary = this.effect;
/* 217 */         if (!Objects.equals(BeaconScreen.this.secondary, this.effect))
/* 218 */           BeaconScreen.this.secondary = null; 
/*     */       } else {
/* 221 */         BeaconScreen.this.secondary = this.effect;
/*     */       } 
/* 223 */       BeaconScreen.this.updateButtons();
/*     */     }
/*     */     
/*     */     protected void extractIcon(GuiGraphicsExtractor graphics) {
/* 228 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX() + 2, getY() + 2, 18, 18);
/*     */     }
/*     */     
/*     */     public void updateStatus(int levels) {
/* 233 */       this.active = (this.tier < levels);
/* 234 */       setSelected(this.effect.equals(this.isPrimary ? BeaconScreen.this.primary : BeaconScreen.this.secondary));
/*     */     }
/*     */     
/*     */     protected MutableComponent createNarrationMessage() {
/* 239 */       return createEffectDescription(this.effect);
/*     */     }
/*     */   }
/*     */   
/*     */   private class BeaconUpgradePowerButton extends BeaconPowerButton {
/*     */     public BeaconUpgradePowerButton(int x, int y, Holder<MobEffect> effect) {
/* 245 */       super(x, y, effect, false, 3);
/*     */     }
/*     */     
/*     */     protected MutableComponent createEffectDescription(Holder<MobEffect> effect) {
/* 250 */       return Component.translatable(((MobEffect)effect.value()).getDescriptionId()).append(" II");
/*     */     }
/*     */     
/*     */     public void updateStatus(int levels) {
/* 255 */       if (BeaconScreen.this.primary != null) {
/* 256 */         this.visible = true;
/* 257 */         setEffect(BeaconScreen.this.primary);
/* 258 */         super.updateStatus(levels);
/*     */       } else {
/* 260 */         this.visible = false;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class BeaconSpriteScreenButton extends BeaconScreenButton {
/*     */     private final Identifier sprite;
/*     */     
/*     */     protected BeaconSpriteScreenButton(int x, int y, Identifier sprite, Component label) {
/* 269 */       super(x, y, label);
/* 270 */       setTooltip(Tooltip.create(label));
/* 271 */       this.sprite = sprite;
/*     */     }
/*     */     
/*     */     protected void extractIcon(GuiGraphicsExtractor graphics) {
/* 276 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, getX() + 2, getY() + 2, 18, 18);
/*     */     }
/*     */   }
/*     */   
/*     */   private class BeaconConfirmButton extends BeaconSpriteScreenButton {
/*     */     public BeaconConfirmButton(int x, int y) {
/* 282 */       super(x, y, BeaconScreen.CONFIRM_SPRITE, CommonComponents.GUI_DONE);
/*     */     }
/*     */     
/*     */     public void onPress(InputWithModifiers input) {
/* 287 */       BeaconScreen.this.minecraft.getConnection().send((Packet)new ServerboundSetBeaconPacket(Optional.ofNullable(BeaconScreen.this.primary), Optional.ofNullable(BeaconScreen.this.secondary)));
/* 288 */       BeaconScreen.this.minecraft.player.closeContainer();
/*     */     }
/*     */     
/*     */     public void updateStatus(int levels) {
/* 293 */       this.active = (BeaconScreen.this.menu.hasPayment() && BeaconScreen.this.primary != null);
/*     */     }
/*     */   }
/*     */   
/*     */   private class BeaconCancelButton extends BeaconSpriteScreenButton {
/*     */     public BeaconCancelButton(int x, int y) {
/* 299 */       super(x, y, BeaconScreen.CANCEL_SPRITE, CommonComponents.GUI_CANCEL);
/*     */     }
/*     */     
/*     */     public void onPress(InputWithModifiers input) {
/* 304 */       BeaconScreen.this.minecraft.player.closeContainer();
/*     */     }
/*     */     
/*     */     public void updateStatus(int levels) {}
/*     */   }
/*     */   
/*     */   private static interface BeaconButton {
/*     */     void updateStatus(int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\BeaconScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */