/*   */ package com.mojang.realmsclient.dto;
/*   */ 
/*   */ import com.google.gson.annotations.SerializedName;
/*   */ import java.util.Set;
/*   */ 
/*   */ public final class RealmsWorldResetDto extends Record implements ReflectionBasedSerialization {
/*   */   @SerializedName("seed")
/*   */   private final String seed;
/*   */   
/*   */   @SerializedName("worldTemplateId")
/*   */   private final long worldTemplateId;
/*   */   
/*   */   @SerializedName("levelType")
/*   */   private final int levelType;
/*   */   
/*   */   @SerializedName("generateStructures")
/*   */   private final boolean generateStructures;
/*   */   
/*   */   @SerializedName("experiments")
/*   */   private final Set<String> experiments;
/*   */   
/*   */   public RealmsWorldResetDto(String seed, long worldTemplateId, int levelType, boolean generateStructures, Set<String> experiments) {
/* 7 */     this.seed = seed;
/* 7 */     this.worldTemplateId = worldTemplateId;
/* 7 */     this.levelType = levelType;
/* 7 */     this.generateStructures = generateStructures;
/* 7 */     this.experiments = experiments;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsWorldResetDto;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsWorldResetDto;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsWorldResetDto;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsWorldResetDto;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsWorldResetDto;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsWorldResetDto;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   @SerializedName("seed")
/*   */   public String seed() {
/* 7 */     return this.seed;
/*   */   }
/*   */   
/*   */   @SerializedName("worldTemplateId")
/*   */   public long worldTemplateId() {
/* 7 */     return this.worldTemplateId;
/*   */   }
/*   */   
/*   */   @SerializedName("levelType")
/*   */   public int levelType() {
/* 7 */     return this.levelType;
/*   */   }
/*   */   
/*   */   @SerializedName("generateStructures")
/*   */   public boolean generateStructures() {
/* 7 */     return this.generateStructures;
/*   */   }
/*   */   
/*   */   @SerializedName("experiments")
/*   */   public Set<String> experiments() {
/* 7 */     return this.experiments;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RealmsWorldResetDto.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */