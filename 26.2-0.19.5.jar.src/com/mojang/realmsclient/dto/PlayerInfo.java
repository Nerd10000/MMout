/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.annotations.JsonAdapter;
/*    */ import com.google.gson.annotations.SerializedName;
/*    */ import com.mojang.util.UUIDTypeAdapter;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class PlayerInfo extends ValueObject implements ReflectionBasedSerialization {
/*    */   @SerializedName("name")
/*    */   public final String name;
/*    */   
/*    */   @SerializedName("uuid")
/*    */   @JsonAdapter(UUIDTypeAdapter.class)
/*    */   public final UUID uuid;
/*    */   
/*    */   @SerializedName("operator")
/*    */   public boolean operator;
/*    */   
/*    */   @SerializedName("accepted")
/*    */   public final boolean accepted;
/*    */   
/*    */   @SerializedName("online")
/*    */   public final boolean online;
/*    */   
/*    */   public PlayerInfo(String name, UUID uuid, boolean operator, boolean accepted, boolean online) {
/* 27 */     this.name = name;
/* 28 */     this.uuid = uuid;
/* 29 */     this.operator = operator;
/* 30 */     this.accepted = accepted;
/* 31 */     this.online = online;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\PlayerInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */