/*    */ package com.mojang.realmsclient.gui.screens.configuration;
/*    */ 
/*    */ import com.mojang.realmsclient.dto.RealmsRegion;
/*    */ import com.mojang.realmsclient.dto.RegionSelectionPreference;
/*    */ 
/*    */ public final class RegionSelection extends Record {
/*    */   private final RegionSelectionPreference preference;
/*    */   
/*    */   private final RealmsRegion region;
/*    */   
/*    */   public RegionSelection(RegionSelectionPreference preference, RealmsRegion region) {
/* 51 */     this.preference = preference;
/* 51 */     this.region = region;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/gui/screens/configuration/RealmsSettingsTab$RegionSelection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/gui/screens/configuration/RealmsSettingsTab$RegionSelection;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/gui/screens/configuration/RealmsSettingsTab$RegionSelection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/gui/screens/configuration/RealmsSettingsTab$RegionSelection;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/gui/screens/configuration/RealmsSettingsTab$RegionSelection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/gui/screens/configuration/RealmsSettingsTab$RegionSelection;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public RegionSelectionPreference preference() {
/* 51 */     return this.preference;
/*    */   }
/*    */   
/*    */   public RealmsRegion region() {
/* 51 */     return this.region;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsSettingsTab$RegionSelection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */