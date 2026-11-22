/*    */ package net.minecraft.client.multiplayer.chat;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.permissions.Permission;
/*    */ 
/*    */ enum null {
/*    */   public void modifyPermissions(Set<Permission> permissionSet) {
/* 23 */     ChatRestriction.disableCommands(permissionSet);
/* 24 */     ChatRestriction.disablePlayerMessages(permissionSet);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\ChatRestriction$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */