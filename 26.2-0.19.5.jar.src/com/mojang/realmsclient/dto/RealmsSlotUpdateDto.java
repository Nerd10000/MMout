/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.annotations.SerializedName;
/*    */ 
/*    */ public final class RealmsSlotUpdateDto extends Record implements ReflectionBasedSerialization {
/*    */   @SerializedName("slotId")
/*    */   private final int slotId;
/*    */   
/*    */   @SerializedName("spawnProtection")
/*    */   private final int spawnProtection;
/*    */   
/*    */   @SerializedName("forceGameMode")
/*    */   private final boolean forceGameMode;
/*    */   
/*    */   @SerializedName("difficulty")
/*    */   private final int difficulty;
/*    */   
/*    */   @SerializedName("gameMode")
/*    */   private final int gameMode;
/*    */   
/*    */   @SerializedName("slotName")
/*    */   private final String slotName;
/*    */   
/*    */   @SerializedName("version")
/*    */   private final String version;
/*    */   
/*    */   @SerializedName("compatibility")
/*    */   private final RealmsServer.Compatibility compatibility;
/*    */   
/*    */   @SerializedName("worldTemplateId")
/*    */   private final long templateId;
/*    */   
/*    */   @SerializedName("worldTemplateImage")
/*    */   private final String templateImage;
/*    */   
/*    */   @SerializedName("hardcore")
/*    */   private final boolean hardcore;
/*    */   
/*    */   @SerializedName("hardcore")
/*    */   public boolean hardcore() {
/*  6 */     return this.hardcore;
/*    */   }
/*    */   
/*    */   @SerializedName("worldTemplateImage")
/*    */   public String templateImage() {
/*  6 */     return this.templateImage;
/*    */   }
/*    */   
/*    */   @SerializedName("worldTemplateId")
/*    */   public long templateId() {
/*  6 */     return this.templateId;
/*    */   }
/*    */   
/*    */   @SerializedName("compatibility")
/*    */   public RealmsServer.Compatibility compatibility() {
/*  6 */     return this.compatibility;
/*    */   }
/*    */   
/*    */   @SerializedName("version")
/*    */   public String version() {
/*  6 */     return this.version;
/*    */   }
/*    */   
/*    */   @SerializedName("slotName")
/*    */   public String slotName() {
/*  6 */     return this.slotName;
/*    */   }
/*    */   
/*    */   @SerializedName("gameMode")
/*    */   public int gameMode() {
/*  6 */     return this.gameMode;
/*    */   }
/*    */   
/*    */   @SerializedName("difficulty")
/*    */   public int difficulty() {
/*  6 */     return this.difficulty;
/*    */   }
/*    */   
/*    */   @SerializedName("forceGameMode")
/*    */   public boolean forceGameMode() {
/*  6 */     return this.forceGameMode;
/*    */   }
/*    */   
/*    */   @SerializedName("spawnProtection")
/*    */   public int spawnProtection() {
/*  6 */     return this.spawnProtection;
/*    */   }
/*    */   
/*    */   @SerializedName("slotId")
/*    */   public int slotId() {
/*  6 */     return this.slotId;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsSlotUpdateDto;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsSlotUpdateDto;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsSlotUpdateDto;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsSlotUpdateDto;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsSlotUpdateDto;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsSlotUpdateDto;
/*    */   }
/*    */   
/*    */   public RealmsSlotUpdateDto(int slotId, int spawnProtection, boolean forceGameMode, int difficulty, int gameMode, String slotName, String version, RealmsServer.Compatibility compatibility, long templateId, String templateImage, boolean hardcore) {
/*  6 */     this.slotId = slotId;
/*  6 */     this.spawnProtection = spawnProtection;
/*  6 */     this.forceGameMode = forceGameMode;
/*  6 */     this.difficulty = difficulty;
/*  6 */     this.gameMode = gameMode;
/*  6 */     this.slotName = slotName;
/*  6 */     this.version = version;
/*  6 */     this.compatibility = compatibility;
/*  6 */     this.templateId = templateId;
/*  6 */     this.templateImage = templateImage;
/*  6 */     this.hardcore = hardcore;
/*    */   }
/*    */   
/*    */   public RealmsSlotUpdateDto(int slotId, RealmsWorldOptions options, boolean hardcore) {
/* 21 */     this(slotId, options.spawnProtection, options.forceGameMode, options.difficulty, options.gameMode, options
/*    */         
/* 27 */         .getSlotName(slotId), options.version, options.compatibility, options.templateId, options.templateImage, hardcore);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RealmsSlotUpdateDto.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */