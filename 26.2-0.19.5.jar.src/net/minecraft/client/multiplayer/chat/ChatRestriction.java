/*    */ package net.minecraft.client.multiplayer.chat;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.screens.ConfirmLinkScreen;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.gui.screens.options.ChatOptionsScreen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.permissions.Permission;
/*    */ import net.minecraft.server.permissions.Permissions;
/*    */ import net.minecraft.util.CommonLinks;
/*    */ 
/*    */ public enum ChatRestriction {
/* 17 */   CHAT_AND_COMMANDS_DISABLED_BY_OPTIONS(
/* 18 */     (Component)Component.translatable("chat_restriction.chat_and_commands_disabled_by_options"), 
/* 19 */     Optional.of(Action.GO_TO_CHAT_SETTINGS)) {
/*    */     public void modifyPermissions(Set<Permission> permissionSet) {
/* 23 */       disableCommands(permissionSet);
/* 24 */       disablePlayerMessages(permissionSet);
/*    */     }
/*    */   },
/* 27 */   CHAT_DISABLED_BY_OPTIONS(
/* 28 */     (Component)Component.translatable("chat_restriction.chat_disabled_by_options"), 
/* 29 */     Optional.of(Action.GO_TO_CHAT_SETTINGS)) {
/*    */     public void modifyPermissions(Set<Permission> permissionSet) {
/* 33 */       disablePlayerMessages(permissionSet);
/*    */     }
/*    */   },
/* 36 */   DISABLED_BY_LAUNCHER(
/* 37 */     (Component)Component.translatable("chat_restriction.disabled_by_launcher"), 
/* 38 */     Optional.empty()) {
/*    */     public void modifyPermissions(Set<Permission> permissionSet) {
/* 43 */       disablePlayerMessages(permissionSet);
/*    */     }
/*    */   },
/* 46 */   DISABLED_BY_PROFILE(
/* 47 */     (Component)Component.translatable("chat_restriction.disabled_by_profile"), 
/* 48 */     Optional.of(Action.GO_TO_ACCOUNT)) {
/*    */     public void modifyPermissions(Set<Permission> permissionSet) {
/* 52 */       disablePlayerMessages(permissionSet);
/*    */     }
/*    */   };
/*    */   
/*    */   private final Component display;
/*    */   
/*    */   private final Optional<Action> action;
/*    */   
/*    */   ChatRestriction(Component display, Optional<Action> action) {
/* 62 */     this.display = display;
/* 63 */     this.action = action;
/*    */   }
/*    */   
/*    */   public Component display() {
/* 67 */     return this.display;
/*    */   }
/*    */   
/*    */   public Optional<Action> action() {
/* 71 */     return this.action;
/*    */   }
/*    */   
/*    */   private static void disablePlayerMessages(Set<Permission> permissionSet) {
/* 75 */     permissionSet.remove(Permissions.CHAT_SEND_MESSAGES);
/* 76 */     permissionSet.remove(Permissions.CHAT_RECEIVE_PLAYER_MESSAGES);
/*    */   }
/*    */   
/*    */   private static void disableCommands(Set<Permission> permissionSet) {
/* 80 */     permissionSet.remove(Permissions.CHAT_SEND_COMMANDS);
/* 81 */     permissionSet.remove(Permissions.CHAT_RECEIVE_SYSTEM_MESSAGES);
/*    */   }
/*    */   
/*    */   public abstract void modifyPermissions(Set<Permission> paramSet);
/*    */   
/*    */   public static final class Action extends Record {
/*    */     private final Component title;
/*    */     
/*    */     private final BiConsumer<Minecraft, Screen> runnable;
/*    */     
/*    */     public static final Action GO_TO_ACCOUNT;
/*    */     
/*    */     public static final Action GO_TO_CHAT_SETTINGS;
/*    */     
/*    */     public Action(Component title, BiConsumer<Minecraft, Screen> runnable) {
/* 86 */       this.title = title;
/* 86 */       this.runnable = runnable;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/ChatRestriction$Action;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #86	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/ChatRestriction$Action;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/ChatRestriction$Action;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #86	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/ChatRestriction$Action;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/ChatRestriction$Action;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #86	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/ChatRestriction$Action;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Component title() {
/* 86 */       return this.title;
/*    */     }
/*    */     
/*    */     public BiConsumer<Minecraft, Screen> runnable() {
/* 86 */       return this.runnable;
/*    */     }
/*    */     
/*    */     static {
/* 91 */       GO_TO_ACCOUNT = new Action((Component)Component.translatable("chat_restriction.disabled_by_profile.action"), (minecraft, screen) -> ConfirmLinkScreen.confirmLinkNow(screen, CommonLinks.ACCOUNT_SETTINGS, true));
/* 96 */       GO_TO_CHAT_SETTINGS = new Action((Component)Component.translatable("chat_restriction.chat_disabled_by_options.action"), (minecraft, screen) -> minecraft.gui.setScreen((Screen)new ChatOptionsScreen(screen, minecraft.options)));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\ChatRestriction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */