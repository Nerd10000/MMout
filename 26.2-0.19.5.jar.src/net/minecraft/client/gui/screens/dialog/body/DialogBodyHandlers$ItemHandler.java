/*    */ package net.minecraft.client.gui.screens.dialog.body;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*    */ import net.minecraft.client.gui.components.ItemDisplayWidget;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.screens.dialog.DialogScreen;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.server.dialog.body.DialogBody;
/*    */ import net.minecraft.server.dialog.body.ItemBody;
/*    */ import net.minecraft.server.dialog.body.PlainMessage;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ class ItemHandler implements DialogBodyHandler<ItemBody> {
/*    */   public LayoutElement createControls(DialogScreen<?> parent, ItemBody item) {
/* 75 */     ItemStack displayStack = item.item().create();
/* 76 */     if (item.description().isPresent()) {
/* 77 */       PlainMessage description = item.description().get();
/* 78 */       LinearLayout layout = LinearLayout.horizontal().spacing(2);
/* 79 */       layout.defaultCellSetting().alignVerticallyMiddle();
/* 80 */       ItemDisplayWidget itemWidget = new ItemDisplayWidget(Minecraft.getInstance(), 0, 0, item.width(), item.height(), CommonComponents.EMPTY, displayStack, item.showDecorations(), item.showTooltip());
/* 81 */       layout.addChild((LayoutElement)itemWidget);
/* 83 */       layout.addChild((LayoutElement)FocusableTextWidget.builder(description.contents(), parent.getFont()).maxWidth(description.width()).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.NEVER).build()
/* 84 */           .setComponentClickHandler(style -> DialogBodyHandlers.runActionOnParent(parent, style)));
/* 85 */       return (LayoutElement)layout;
/*    */     } 
/* 87 */     return (LayoutElement)new ItemDisplayWidget(Minecraft.getInstance(), 0, 0, item.width(), item.height(), displayStack.getHoverName(), displayStack, item.showDecorations(), item.showTooltip());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\body\DialogBodyHandlers$ItemHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */