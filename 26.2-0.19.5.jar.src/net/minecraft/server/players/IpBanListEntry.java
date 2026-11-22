/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.Date;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class IpBanListEntry extends BanListEntry<String> {
/*    */   public IpBanListEntry(String address) {
/* 11 */     this(address, null, null, null, null);
/*    */   }
/*    */   
/*    */   public IpBanListEntry(String address, Date created, String source, Date expires, String reason) {
/* 15 */     super(address, created, source, expires, reason);
/*    */   }
/*    */   
/*    */   public Component getDisplayName() {
/* 20 */     return (Component)Component.literal(String.valueOf(getUser()));
/*    */   }
/*    */   
/*    */   public IpBanListEntry(JsonObject object) {
/* 24 */     super(createIpInfo(object), object);
/*    */   }
/*    */   
/*    */   private static String createIpInfo(JsonObject object) {
/* 28 */     return object.has("ip") ? object.get("ip").getAsString() : null;
/*    */   }
/*    */   
/*    */   protected void serialize(JsonObject object) {
/* 33 */     if (getUser() == null)
/*    */       return; 
/* 36 */     object.addProperty("ip", getUser());
/* 37 */     super.serialize(object);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\players\IpBanListEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */