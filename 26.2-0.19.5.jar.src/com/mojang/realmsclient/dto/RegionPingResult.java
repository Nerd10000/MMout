/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.annotations.SerializedName;
/*    */ import java.util.Locale;
/*    */ 
/*    */ public final class RegionPingResult extends Record implements ReflectionBasedSerialization {
/*    */   @SerializedName("regionName")
/*    */   private final String regionName;
/*    */   
/*    */   @SerializedName("ping")
/*    */   private final int ping;
/*    */   
/*    */   public RegionPingResult(String regionName, int ping) {
/*  7 */     this.regionName = regionName;
/*  7 */     this.ping = ping;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RegionPingResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RegionPingResult;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RegionPingResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RegionPingResult;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   @SerializedName("regionName")
/*    */   public String regionName() {
/*  7 */     return this.regionName;
/*    */   }
/*    */   
/*    */   @SerializedName("ping")
/*    */   public int ping() {
/*  7 */     return this.ping;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 14 */     return String.format(Locale.ROOT, "%s --> %.2f ms", new Object[] { this.regionName, Float.valueOf(this.ping) });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RegionPingResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */