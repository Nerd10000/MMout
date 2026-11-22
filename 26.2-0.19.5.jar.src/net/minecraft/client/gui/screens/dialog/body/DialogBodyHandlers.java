/*    */ package net.minecraft.client.gui.screens.dialog.body;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*    */ import net.minecraft.client.gui.components.ItemDisplayWidget;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.screens.dialog.DialogScreen;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.server.dialog.body.DialogBody;
/*    */ import net.minecraft.server.dialog.body.ItemBody;
/*    */ import net.minecraft.server.dialog.body.PlainMessage;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DialogBodyHandlers {
/* 26 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 28 */   private static final Map<MapCodec<? extends DialogBody>, DialogBodyHandler<?>> HANDLERS = new HashMap<>();
/*    */   
/*    */   private static <B extends DialogBody> void register(MapCodec<B> type, DialogBodyHandler<? super B> handler) {
/* 31 */     HANDLERS.put(type, handler);
/*    */   }
/*    */   
/*    */   private static <B extends DialogBody> DialogBodyHandler<B> getHandler(B body) {
/* 36 */     return (DialogBodyHandler<B>)HANDLERS.get(body.mapCodec());
/*    */   }
/*    */   
/*    */   public static <B extends DialogBody> LayoutElement createBodyElement(DialogScreen<?> screen, B body) {
/* 40 */     DialogBodyHandler<B> handler = getHandler(body);
/* 41 */     if (handler == null) {
/* 42 */       LOGGER.warn("Unrecognized dialog body {}", body);
/* 43 */       return null;
/*    */     } 
/* 46 */     return handler.createControls(screen, body);
/*    */   }
/*    */   
/*    */   public static void bootstrap() {
/* 50 */     register(PlainMessage.MAP_CODEC, new PlainMessageHandler());
/* 51 */     register(ItemBody.MAP_CODEC, new ItemHandler());
/*    */   }
/*    */   
/*    */   private static void runActionOnParent(DialogScreen<?> parent, Style clickedStyle) {
/* 55 */     if (clickedStyle != null) {
/* 56 */       ClickEvent clickEvent = clickedStyle.getClickEvent();
/* 57 */       if (clickEvent != null)
/* 58 */         parent.runAction(Optional.of(clickEvent)); 
/*    */     } 
/*    */   }
/*    */   
/*    */   private static class PlainMessageHandler implements DialogBodyHandler<PlainMessage> {
/*    */     public LayoutElement createControls(DialogScreen<?> parent, PlainMessage message) {
/* 66 */       return (LayoutElement)FocusableTextWidget.builder(message.contents(), parent.getFont()).maxWidth(message.width()).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.NEVER).build()
/* 67 */         .setCentered(true)
/* 68 */         .setComponentClickHandler(style -> DialogBodyHandlers.runActionOnParent(parent, style));
/*    */     }
/*    */   }
/*    */   
/*    */   private static class ItemHandler implements DialogBodyHandler<ItemBody> {
/*    */     public LayoutElement createControls(DialogScreen<?> parent, ItemBody item) {
/* 75 */       ItemStack displayStack = item.item().create();
/* 76 */       if (item.description().isPresent()) {
/* 77 */         PlainMessage description = item.description().get();
/* 78 */         LinearLayout layout = LinearLayout.horizontal().spacing(2);
/* 79 */         layout.defaultCellSetting().alignVerticallyMiddle();
/* 80 */         ItemDisplayWidget itemWidget = new ItemDisplayWidget(Minecraft.getInstance(), 0, 0, item.width(), item.height(), CommonComponents.EMPTY, displayStack, item.showDecorations(), item.showTooltip());
/* 81 */         layout.addChild((LayoutElement)itemWidget);
/* 83 */         layout.addChild((LayoutElement)FocusableTextWidget.builder(description.contents(), parent.getFont()).maxWidth(description.width()).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.NEVER).build()
/* 84 */             .setComponentClickHandler(style -> DialogBodyHandlers.runActionOnParent(parent, style)));
/* 85 */         return (LayoutElement)layout;
/*    */       } 
/* 87 */       return (LayoutElement)new ItemDisplayWidget(Minecraft.getInstance(), 0, 0, item.width(), item.height(), displayStack.getHoverName(), displayStack, item.showDecorations(), item.showTooltip());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\body\DialogBodyHandlers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */