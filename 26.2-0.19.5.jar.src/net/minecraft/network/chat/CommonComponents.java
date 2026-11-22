/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ 
/*    */ public class CommonComponents {
/*  7 */   public static final Component EMPTY = Component.empty();
/*    */   
/*  9 */   public static final Component OPTION_ON = Component.translatable("options.on");
/*    */   
/* 10 */   public static final Component OPTION_OFF = Component.translatable("options.off");
/*    */   
/* 12 */   public static final Component GUI_DONE = Component.translatable("gui.done");
/*    */   
/* 13 */   public static final Component GUI_CANCEL = Component.translatable("gui.cancel");
/*    */   
/* 14 */   public static final Component GUI_YES = Component.translatable("gui.yes");
/*    */   
/* 15 */   public static final Component GUI_REMOVE = Component.translatable("gui.remove");
/*    */   
/* 16 */   public static final Component GUI_NO = Component.translatable("gui.no");
/*    */   
/* 17 */   public static final Component GUI_OK = Component.translatable("gui.ok");
/*    */   
/* 18 */   public static final Component GUI_PROCEED = Component.translatable("gui.proceed");
/*    */   
/* 19 */   public static final Component GUI_CONTINUE = Component.translatable("gui.continue");
/*    */   
/* 20 */   public static final Component GUI_BACK = Component.translatable("gui.back");
/*    */   
/* 21 */   public static final Component GUI_TO_TITLE = Component.translatable("gui.toTitle");
/*    */   
/* 22 */   public static final Component GUI_ACKNOWLEDGE = Component.translatable("gui.acknowledge");
/*    */   
/* 23 */   public static final Component GUI_OPEN_IN_BROWSER = Component.translatable("chat.link.open");
/*    */   
/* 24 */   public static final Component GUI_COPY_TO_CLIPBOARD = Component.translatable("chat.copy");
/*    */   
/* 25 */   public static final Component GUI_COPY_LINK_TO_CLIPBOARD = Component.translatable("gui.copy_link_to_clipboard");
/*    */   
/* 26 */   public static final Component GUI_DISCONNECT = Component.translatable("menu.disconnect");
/*    */   
/* 27 */   public static final Component GUI_RETURN_TO_MENU = Component.translatable("menu.returnToMenu");
/*    */   
/* 29 */   public static final Component TRANSFER_CONNECT_FAILED = Component.translatable("connect.failed.transfer");
/*    */   
/* 30 */   public static final Component CONNECT_FAILED = Component.translatable("connect.failed");
/*    */   
/* 32 */   public static final Component NEW_LINE = Component.literal("\n");
/*    */   
/* 33 */   public static final Component NARRATION_SEPARATOR = Component.literal(". ");
/*    */   
/* 35 */   public static final Component ELLIPSIS = Component.literal("...");
/*    */   
/* 36 */   public static final Component SPACE = space();
/*    */   
/*    */   public static MutableComponent space() {
/* 39 */     return Component.literal(" ");
/*    */   }
/*    */   
/*    */   public static MutableComponent days(long value) {
/* 43 */     return Component.translatable("gui.days", new Object[] { Long.valueOf(value) });
/*    */   }
/*    */   
/*    */   public static MutableComponent hours(long value) {
/* 47 */     return Component.translatable("gui.hours", new Object[] { Long.valueOf(value) });
/*    */   }
/*    */   
/*    */   public static MutableComponent minutes(long value) {
/* 51 */     return Component.translatable("gui.minutes", new Object[] { Long.valueOf(value) });
/*    */   }
/*    */   
/*    */   public static Component optionStatus(boolean value) {
/* 55 */     return value ? OPTION_ON : OPTION_OFF;
/*    */   }
/*    */   
/*    */   public static Component disconnectButtonLabel(boolean isLocalServer) {
/* 59 */     return isLocalServer ? GUI_RETURN_TO_MENU : GUI_DISCONNECT;
/*    */   }
/*    */   
/*    */   public static MutableComponent optionStatus(Component name, boolean value) {
/* 63 */     return Component.translatable(value ? "options.on.composed" : "options.off.composed", new Object[] { name });
/*    */   }
/*    */   
/*    */   public static MutableComponent optionNameValue(Component name, Component value) {
/* 67 */     return Component.translatable("options.generic_value", new Object[] { name, value });
/*    */   }
/*    */   
/*    */   public static MutableComponent joinForNarration(Component... components) {
/* 71 */     MutableComponent result = Component.empty();
/* 72 */     for (int i = 0; i < components.length; i++) {
/* 73 */       result.append(components[i]);
/* 74 */       if (i != components.length - 1)
/* 75 */         result.append(NARRATION_SEPARATOR); 
/*    */     } 
/* 78 */     return result;
/*    */   }
/*    */   
/*    */   public static Component joinLines(Component... lines) {
/* 82 */     return joinLines(Arrays.asList(lines));
/*    */   }
/*    */   
/*    */   public static Component joinLines(Collection<? extends Component> lines) {
/* 86 */     return ComponentUtils.formatList(lines, NEW_LINE);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\CommonComponents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */