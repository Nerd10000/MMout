/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.annotations.JsonAdapter;
/*    */ import com.google.gson.annotations.SerializedName;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class RealmsJoinInformation extends Record implements ReflectionBasedSerialization {
/*    */   @SerializedName("address")
/*    */   private final String address;
/*    */   
/*    */   @SerializedName("resourcePackUrl")
/*    */   private final String resourcePackUrl;
/*    */   
/*    */   @SerializedName("resourcePackHash")
/*    */   private final String resourcePackHash;
/*    */   
/*    */   @SerializedName("sessionRegionData")
/*    */   private final RegionData regionData;
/*    */   
/*    */   public RealmsJoinInformation(String address, String resourcePackUrl, String resourcePackHash, RegionData regionData) {
/*  9 */     this.address = address;
/*  9 */     this.resourcePackUrl = resourcePackUrl;
/*  9 */     this.resourcePackHash = resourcePackHash;
/*  9 */     this.regionData = regionData;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsJoinInformation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsJoinInformation;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsJoinInformation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsJoinInformation;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsJoinInformation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsJoinInformation;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   @SerializedName("address")
/*    */   public String address() {
/*  9 */     return this.address;
/*    */   }
/*    */   
/*    */   @SerializedName("resourcePackUrl")
/*    */   public String resourcePackUrl() {
/*  9 */     return this.resourcePackUrl;
/*    */   }
/*    */   
/*    */   @SerializedName("resourcePackHash")
/*    */   public String resourcePackHash() {
/*  9 */     return this.resourcePackHash;
/*    */   }
/*    */   
/*    */   @SerializedName("sessionRegionData")
/*    */   public RegionData regionData() {
/*  9 */     return this.regionData;
/*    */   }
/*    */   
/* 15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 16 */   private static final RealmsJoinInformation EMPTY = new RealmsJoinInformation(null, null, null, null);
/*    */   
/*    */   public static final class RegionData extends Record implements ReflectionBasedSerialization {
/*    */     @SerializedName("regionName")
/*    */     @JsonAdapter(RealmsRegion.RealmsRegionJsonAdapter.class)
/*    */     private final RealmsRegion region;
/*    */     
/*    */     @SerializedName("serviceQuality")
/*    */     @JsonAdapter(ServiceQuality.RealmsServiceQualityJsonAdapter.class)
/*    */     private final ServiceQuality serviceQuality;
/*    */     
/*    */     public RegionData(RealmsRegion region, ServiceQuality serviceQuality) {
/* 18 */       this.region = region;
/* 18 */       this.serviceQuality = serviceQuality;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsJoinInformation$RegionData;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsJoinInformation$RegionData;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsJoinInformation$RegionData;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsJoinInformation$RegionData;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsJoinInformation$RegionData;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsJoinInformation$RegionData;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     @SerializedName("regionName")
/*    */     public RealmsRegion region() {
/* 18 */       return this.region;
/*    */     }
/*    */     
/*    */     @SerializedName("serviceQuality")
/*    */     public ServiceQuality serviceQuality() {
/* 18 */       return this.serviceQuality;
/*    */     }
/*    */   }
/*    */   
/*    */   public static RealmsJoinInformation parse(GuardedSerializer gson, String json) {
/*    */     try {
/* 29 */       RealmsJoinInformation server = gson.<RealmsJoinInformation>fromJson(json, RealmsJoinInformation.class);
/* 30 */       if (server == null) {
/* 31 */         LOGGER.error("Could not parse RealmsServerAddress: {}", json);
/* 32 */         return EMPTY;
/*    */       } 
/* 34 */       return server;
/* 35 */     } catch (Exception e) {
/* 36 */       LOGGER.error("Could not parse RealmsServerAddress", e);
/* 37 */       return EMPTY;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RealmsJoinInformation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */