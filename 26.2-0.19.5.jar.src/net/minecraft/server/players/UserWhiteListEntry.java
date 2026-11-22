/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ 
/*    */ public class UserWhiteListEntry extends StoredUserEntry<NameAndId> {
/*    */   public UserWhiteListEntry(NameAndId user) {
/*  7 */     super(user);
/*    */   }
/*    */   
/*    */   public UserWhiteListEntry(JsonObject object) {
/* 11 */     super(NameAndId.fromJson(object));
/*    */   }
/*    */   
/*    */   protected void serialize(JsonObject object) {
/* 16 */     if (getUser() == null)
/*    */       return; 
/* 19 */     getUser().appendTo(object);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\players\UserWhiteListEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */