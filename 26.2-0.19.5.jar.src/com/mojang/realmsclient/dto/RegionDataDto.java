/*   */ package com.mojang.realmsclient.dto;
/*   */ 
/*   */ import com.google.gson.annotations.JsonAdapter;
/*   */ import com.google.gson.annotations.SerializedName;
/*   */ 
/*   */ public final class RegionDataDto extends Record implements ReflectionBasedSerialization {
/*   */   @SerializedName("regionName")
/*   */   @JsonAdapter(RealmsRegion.RealmsRegionJsonAdapter.class)
/*   */   private final RealmsRegion region;
/*   */   
/*   */   @SerializedName("serviceQuality")
/*   */   @JsonAdapter(ServiceQuality.RealmsServiceQualityJsonAdapter.class)
/*   */   private final ServiceQuality serviceQuality;
/*   */   
/*   */   public RegionDataDto(RealmsRegion region, ServiceQuality serviceQuality) {
/* 6 */     this.region = region;
/* 6 */     this.serviceQuality = serviceQuality;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RegionDataDto;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RegionDataDto;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RegionDataDto;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RegionDataDto;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RegionDataDto;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RegionDataDto;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   @SerializedName("regionName")
/*   */   public RealmsRegion region() {
/* 6 */     return this.region;
/*   */   }
/*   */   
/*   */   @SerializedName("serviceQuality")
/*   */   public ServiceQuality serviceQuality() {
/* 6 */     return this.serviceQuality;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RegionDataDto.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */