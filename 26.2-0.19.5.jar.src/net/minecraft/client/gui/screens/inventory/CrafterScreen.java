/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.cursor.CursorTypes;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.ContainerInput;
/*     */ import net.minecraft.world.inventory.CrafterMenu;
/*     */ import net.minecraft.world.inventory.CrafterSlot;
/*     */ import net.minecraft.world.inventory.Slot;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class CrafterScreen extends AbstractContainerScreen<CrafterMenu> {
/*  18 */   private static final Identifier DISABLED_SLOT_LOCATION_SPRITE = Identifier.withDefaultNamespace("container/crafter/disabled_slot");
/*     */   
/*  19 */   private static final Identifier POWERED_REDSTONE_LOCATION_SPRITE = Identifier.withDefaultNamespace("container/crafter/powered_redstone");
/*     */   
/*  20 */   private static final Identifier UNPOWERED_REDSTONE_LOCATION_SPRITE = Identifier.withDefaultNamespace("container/crafter/unpowered_redstone");
/*     */   
/*  21 */   private static final Identifier CONTAINER_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/crafter.png");
/*     */   
/*  22 */   private static final Component DISABLED_SLOT_TOOLTIP = (Component)Component.translatable("gui.togglable_slot");
/*     */   
/*     */   private final Player player;
/*     */   
/*     */   public CrafterScreen(CrafterMenu menu, Inventory inventory, Component title) {
/*  27 */     super(menu, inventory, title);
/*  28 */     this.player = inventory.player;
/*     */   }
/*     */   
/*     */   protected void init() {
/*  33 */     super.init();
/*  34 */     this.titleLabelX = (this.imageWidth - this.font.width((FormattedText)this.title)) / 2;
/*     */   }
/*     */   
/*     */   protected void slotClicked(Slot slot, int slotId, int buttonNum, ContainerInput containerInput) {
/*  39 */     if (slot instanceof CrafterSlot && !slot.hasItem() && !this.player.isSpectator()) {
/*     */       ItemStack playerInventoryItem;
/*  40 */       switch (containerInput) {
/*     */         case PICKUP:
/*  42 */           if (this.menu.isSlotDisabled(slotId)) {
/*  43 */             enableSlot(slotId);
/*     */             break;
/*     */           } 
/*  44 */           if (this.menu.getCarried().isEmpty())
/*  45 */             disableSlot(slotId); 
/*     */           break;
/*     */         case SWAP:
/*  49 */           playerInventoryItem = this.player.getInventory().getItem(buttonNum);
/*  50 */           if (this.menu.isSlotDisabled(slotId) && !playerInventoryItem.isEmpty())
/*  51 */             enableSlot(slotId); 
/*     */           break;
/*     */       } 
/*     */     } 
/*  57 */     super.slotClicked(slot, slotId, buttonNum, containerInput);
/*     */   }
/*     */   
/*     */   private void enableSlot(int slotId) {
/*  61 */     updateSlotState(slotId, true);
/*     */   }
/*     */   
/*     */   private void disableSlot(int slotId) {
/*  65 */     updateSlotState(slotId, false);
/*     */   }
/*     */   
/*     */   private void updateSlotState(int slotId, boolean enabled) {
/*  69 */     this.menu.setSlotState(slotId, enabled);
/*  70 */     handleSlotStateChanged(slotId, this.menu.containerId, enabled);
/*  71 */     float pitch = enabled ? 1.0F : 0.75F;
/*  72 */     this.player.playSound((SoundEvent)SoundEvents.UI_BUTTON_CLICK.value(), 0.4F, pitch);
/*     */   }
/*     */   
/*     */   public void extractSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY) {
/*  77 */     if (slot instanceof CrafterSlot) {
/*  77 */       CrafterSlot crafterSlot = (CrafterSlot)slot;
/*  78 */       if (this.menu.isSlotDisabled(slot.index)) {
/*  79 */         extractDisabledSlot(graphics, crafterSlot);
/*     */       } else {
/*  81 */         super.extractSlot(graphics, slot, mouseX, mouseY);
/*     */       } 
/*  83 */       int x0 = this.leftPos + crafterSlot.x - 2;
/*  84 */       int y0 = this.topPos + crafterSlot.y - 2;
/*  85 */       if (mouseX > x0 && mouseY > y0 && mouseX < x0 + 19 && mouseY < y0 + 19)
/*  86 */         graphics.requestCursor(CursorTypes.POINTING_HAND); 
/*     */     } else {
/*  89 */       super.extractSlot(graphics, slot, mouseX, mouseY);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractDisabledSlot(GuiGraphicsExtractor graphics, CrafterSlot cs) {
/*  94 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DISABLED_SLOT_LOCATION_SPRITE, cs.x - 1, cs.y - 1, 18, 18);
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*  99 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/* 100 */     extractRedstone(graphics);
/* 102 */     if (this.hoveredSlot instanceof CrafterSlot && !this.menu.isSlotDisabled(this.hoveredSlot.index) && this.menu
/* 103 */       .getCarried().isEmpty() && !this.hoveredSlot.hasItem() && !this.player.isSpectator())
/* 104 */       graphics.setTooltipForNextFrame(this.font, DISABLED_SLOT_TOOLTIP, mouseX, mouseY); 
/*     */   }
/*     */   
/*     */   private void extractRedstone(GuiGraphicsExtractor graphics) {
/*     */     Identifier redstoneArrowTexture;
/* 109 */     int xo = this.width / 2 + 9;
/* 110 */     int yo = this.height / 2 - 48;
/* 112 */     if (this.menu.isPowered()) {
/* 113 */       redstoneArrowTexture = POWERED_REDSTONE_LOCATION_SPRITE;
/*     */     } else {
/* 115 */       redstoneArrowTexture = UNPOWERED_REDSTONE_LOCATION_SPRITE;
/*     */     } 
/* 117 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, redstoneArrowTexture, xo, yo, 16, 16);
/*     */   }
/*     */   
/*     */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 122 */     super.extractBackground(graphics, mouseX, mouseY, a);
/* 123 */     int xo = (this.width - this.imageWidth) / 2;
/* 124 */     int yo = (this.height - this.imageHeight) / 2;
/* 125 */     graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\CrafterScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */