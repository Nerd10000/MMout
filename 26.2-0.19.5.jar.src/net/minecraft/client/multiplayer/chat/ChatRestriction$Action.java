/*    */ package net.minecraft.client.multiplayer.chat;
/*    */ 
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.gui.screens.options.ChatOptionsScreen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.CommonLinks;
/*    */ 
/*    */ public final class Action extends Record {
/*    */   private final Component title;
/*    */   
/*    */   private final BiConsumer<Minecraft, Screen> runnable;
/*    */   
/*    */   public static final Action GO_TO_ACCOUNT;
/*    */   
/*    */   public static final Action GO_TO_CHAT_SETTINGS;
/*    */   
/*    */   public Action(Component title, BiConsumer<Minecraft, Screen> runnable) {
/* 86 */     this.title = title;
/* 86 */     this.runnable = runnable;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/ChatRestriction$Action;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #86	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/ChatRestriction$Action;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/ChatRestriction$Action;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #86	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/ChatRestriction$Action;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/ChatRestriction$Action;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #86	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/ChatRestriction$Action;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component title() {
/* 86 */     return this.title;
/*    */   }
/*    */   
/*    */   public BiConsumer<Minecraft, Screen> runnable() {
/* 86 */     return this.runnable;
/*    */   }
/*    */   
/*    */   static {
/* 91 */     GO_TO_ACCOUNT = new Action((Component)Component.translatable("chat_restriction.disabled_by_profile.action"), (minecraft, screen) -> ConfirmLinkScreen.confirmLinkNow(screen, CommonLinks.ACCOUNT_SETTINGS, true));
/* 96 */     GO_TO_CHAT_SETTINGS = new Action((Component)Component.translatable("chat_restriction.chat_disabled_by_options.action"), (minecraft, screen) -> minecraft.gui.setScreen((Screen)new ChatOptionsScreen(screen, minecraft.options)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\ChatRestriction$Action.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */