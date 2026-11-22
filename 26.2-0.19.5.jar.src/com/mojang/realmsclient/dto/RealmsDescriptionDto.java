/*   */ package com.mojang.realmsclient.dto;
/*   */ 
/*   */ import com.google.gson.annotations.SerializedName;
/*   */ 
/*   */ public final class RealmsDescriptionDto extends Record implements ReflectionBasedSerialization {
/*   */   @SerializedName("name")
/*   */   private final String name;
/*   */   
/*   */   @SerializedName("description")
/*   */   private final String description;
/*   */   
/*   */   public RealmsDescriptionDto(String name, String description) {
/* 6 */     this.name = name;
/* 6 */     this.description = description;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsDescriptionDto;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsDescriptionDto;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsDescriptionDto;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsDescriptionDto;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsDescriptionDto;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsDescriptionDto;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   @SerializedName("name")
/*   */   public String name() {
/* 6 */     return this.name;
/*   */   }
/*   */   
/*   */   @SerializedName("description")
/*   */   public String description() {
/* 6 */     return this.description;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RealmsDescriptionDto.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */