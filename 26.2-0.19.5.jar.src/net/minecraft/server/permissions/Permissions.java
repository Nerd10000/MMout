/*    */ package net.minecraft.server.permissions;
/*    */ 
/*    */ import java.util.Set;
/*    */ 
/*    */ public class Permissions {
/*  6 */   public static final Permission COMMANDS_MODERATOR = new Permission.HasCommandLevel(PermissionLevel.MODERATORS);
/*    */   
/*  7 */   public static final Permission COMMANDS_GAMEMASTER = new Permission.HasCommandLevel(PermissionLevel.GAMEMASTERS);
/*    */   
/*  8 */   public static final Permission COMMANDS_ADMIN = new Permission.HasCommandLevel(PermissionLevel.ADMINS);
/*    */   
/*  9 */   public static final Permission COMMANDS_OWNER = new Permission.HasCommandLevel(PermissionLevel.OWNERS);
/*    */   
/* 11 */   public static final Permission COMMANDS_ENTITY_SELECTORS = Permission.Atom.create("commands/entity_selectors");
/*    */   
/* 13 */   public static final Permission CHAT_SEND_MESSAGES = Permission.Atom.create("chat/send_messages");
/*    */   
/* 14 */   public static final Permission CHAT_SEND_COMMANDS = Permission.Atom.create("chat/send_commands");
/*    */   
/* 15 */   public static final Permission CHAT_RECEIVE_PLAYER_MESSAGES = Permission.Atom.create("chat/receive_player_messages");
/*    */   
/* 16 */   public static final Permission CHAT_RECEIVE_SYSTEM_MESSAGES = Permission.Atom.create("chat/receive_system_messages");
/*    */   
/* 18 */   public static final Set<Permission> CHAT_PERMISSIONS = Set.of(CHAT_SEND_MESSAGES, CHAT_SEND_COMMANDS, CHAT_RECEIVE_PLAYER_MESSAGES, CHAT_RECEIVE_SYSTEM_MESSAGES);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\permissions\Permissions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */