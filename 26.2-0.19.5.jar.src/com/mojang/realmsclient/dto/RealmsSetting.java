/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.annotations.SerializedName;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class RealmsSetting extends Record implements ReflectionBasedSerialization {
/*    */   @SerializedName("name")
/*    */   private final String name;
/*    */   
/*    */   @SerializedName("value")
/*    */   private final String value;
/*    */   
/*    */   public RealmsSetting(String name, String value) {
/*  7 */     this.name = name;
/*  7 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsSetting;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsSetting;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsSetting;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsSetting;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsSetting;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsSetting;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   @SerializedName("name")
/*    */   public String name() {
/*  7 */     return this.name;
/*    */   }
/*    */   
/*    */   @SerializedName("value")
/*    */   public String value() {
/*  7 */     return this.value;
/*    */   }
/*    */   
/*    */   public static RealmsSetting hardcoreSetting(boolean hardcore) {
/* 13 */     return new RealmsSetting("hardcore", Boolean.toString(hardcore));
/*    */   }
/*    */   
/*    */   public static boolean isHardcore(List<RealmsSetting> settings) {
/* 17 */     for (RealmsSetting setting : settings) {
/* 18 */       if (setting.name().equals("hardcore"))
/* 19 */         return Boolean.parseBoolean(setting.value()); 
/*    */     } 
/* 22 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RealmsSetting.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */