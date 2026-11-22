/*   */ package com.mojang.realmsclient.dto;
/*   */ 
/*   */ import com.google.gson.annotations.SerializedName;
/*   */ import java.util.List;
/*   */ 
/*   */ public final class RealmsConfigurationDto extends Record implements ReflectionBasedSerialization {
/*   */   @SerializedName("options")
/*   */   private final RealmsSlotUpdateDto options;
/*   */   
/*   */   @SerializedName("settings")
/*   */   private final List<RealmsSetting> settings;
/*   */   
/*   */   @SerializedName("regionSelectionPreference")
/*   */   private final RegionSelectionPreferenceDto regionSelectionPreference;
/*   */   
/*   */   @SerializedName("description")
/*   */   private final RealmsDescriptionDto description;
/*   */   
/*   */   public RealmsConfigurationDto(RealmsSlotUpdateDto options, List<RealmsSetting> settings, RegionSelectionPreferenceDto regionSelectionPreference, RealmsDescriptionDto description) {
/* 8 */     this.options = options;
/* 8 */     this.settings = settings;
/* 8 */     this.regionSelectionPreference = regionSelectionPreference;
/* 8 */     this.description = description;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsConfigurationDto;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsConfigurationDto;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsConfigurationDto;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsConfigurationDto;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsConfigurationDto;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsConfigurationDto;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   @SerializedName("options")
/*   */   public RealmsSlotUpdateDto options() {
/* 8 */     return this.options;
/*   */   }
/*   */   
/*   */   @SerializedName("settings")
/*   */   public List<RealmsSetting> settings() {
/* 8 */     return this.settings;
/*   */   }
/*   */   
/*   */   @SerializedName("regionSelectionPreference")
/*   */   public RegionSelectionPreferenceDto regionSelectionPreference() {
/* 8 */     return this.regionSelectionPreference;
/*   */   }
/*   */   
/*   */   @SerializedName("description")
/*   */   public RealmsDescriptionDto description() {
/* 8 */     return this.description;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RealmsConfigurationDto.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */