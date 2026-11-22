/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.Date;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class UserBanListEntry extends BanListEntry<NameAndId> {
/* 10 */   private static final Component MESSAGE_UNKNOWN_USER = (Component)Component.translatable("commands.banlist.entry.unknown");
/*    */   
/*    */   public UserBanListEntry(NameAndId user) {
/* 13 */     this(user, null, null, null, null);
/*    */   }
/*    */   
/*    */   public UserBanListEntry(NameAndId user, Date created, String source, Date expires, String reason) {
/* 17 */     super(user, created, source, expires, reason);
/*    */   }
/*    */   
/*    */   public UserBanListEntry(JsonObject object) {
/* 21 */     super(NameAndId.fromJson(object), object);
/*    */   }
/*    */   
/*    */   protected void serialize(JsonObject object) {
/* 26 */     if (getUser() == null)
/*    */       return; 
/* 29 */     getUser().appendTo(object);
/* 30 */     super.serialize(object);
/*    */   }
/*    */   
/*    */   public Component getDisplayName() {
/* 35 */     NameAndId user = getUser();
/* 36 */     return (user != null) ? (Component)Component.literal(user.name()) : MESSAGE_UNKNOWN_USER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\players\UserBanListEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */