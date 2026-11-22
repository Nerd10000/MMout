/*    */ package net.minecraft.server.players;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ 
/*    */ public abstract class StoredUserEntry<T> {
/*    */   private final T user;
/*    */   
/*    */   public StoredUserEntry(T user) {
/* 10 */     this.user = user;
/*    */   }
/*    */   
/*    */   public T getUser() {
/* 14 */     return this.user;
/*    */   }
/*    */   
/*    */   public boolean hasExpired() {
/* 18 */     return false;
/*    */   }
/*    */   
/*    */   protected abstract void serialize(JsonObject paramJsonObject);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\players\StoredUserEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */