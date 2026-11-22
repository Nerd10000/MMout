/*   */ package com.mojang.realmsclient.dto;
/*   */ 
/*   */ import com.google.gson.annotations.SerializedName;
/*   */ import java.util.List;
/*   */ 
/*   */ public final class PingResult extends Record implements ReflectionBasedSerialization {
/*   */   @SerializedName("pingResults")
/*   */   private final List<RegionPingResult> pingResults;
/*   */   
/*   */   @SerializedName("worldIds")
/*   */   private final List<Long> realmIds;
/*   */   
/*   */   public PingResult(List<RegionPingResult> pingResults, List<Long> realmIds) {
/* 7 */     this.pingResults = pingResults;
/* 7 */     this.realmIds = realmIds;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/PingResult;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/PingResult;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/PingResult;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/PingResult;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/PingResult;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/PingResult;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   @SerializedName("pingResults")
/*   */   public List<RegionPingResult> pingResults() {
/* 7 */     return this.pingResults;
/*   */   }
/*   */   
/*   */   @SerializedName("worldIds")
/*   */   public List<Long> realmIds() {
/* 7 */     return this.realmIds;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\PingResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */