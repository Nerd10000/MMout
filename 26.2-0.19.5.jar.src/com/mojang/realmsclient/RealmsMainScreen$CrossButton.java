/*      */ package com.mojang.realmsclient;
/*      */ 
/*      */ import net.minecraft.client.gui.components.Button;
/*      */ import net.minecraft.client.gui.components.ImageButton;
/*      */ import net.minecraft.client.gui.components.Tooltip;
/*      */ import net.minecraft.client.gui.components.WidgetSprites;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.resources.Identifier;
/*      */ 
/*      */ class CrossButton extends ImageButton {
/* 1270 */   private static final WidgetSprites SPRITES = new WidgetSprites(
/* 1271 */       Identifier.withDefaultNamespace("widget/cross_button"), 
/* 1272 */       Identifier.withDefaultNamespace("widget/cross_button_highlighted"));
/*      */   
/*      */   protected CrossButton(Button.OnPress onPress, Component tooltip) {
/* 1276 */     super(0, 0, 14, 14, SPRITES, onPress);
/* 1277 */     setTooltip(Tooltip.create(tooltip));
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\RealmsMainScreen$CrossButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */