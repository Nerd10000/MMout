/*    */ package com.mojang.blaze3d.audio;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import org.lwjgl.openal.ALC10;
/*    */ import org.lwjgl.openal.ALUtil;
/*    */ 
/*    */ public final class DeviceList extends Record {
/*    */   private final String defaultDevice;
/*    */   
/*    */   private final List<String> allDevices;
/*    */   
/*    */   public DeviceList(String defaultDevice, List<String> allDevices) {
/* 11 */     this.defaultDevice = defaultDevice;
/* 11 */     this.allDevices = allDevices;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/audio/DeviceList;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/audio/DeviceList;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/audio/DeviceList;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/audio/DeviceList;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/audio/DeviceList;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/audio/DeviceList;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String defaultDevice() {
/* 11 */     return this.defaultDevice;
/*    */   }
/*    */   
/*    */   public List<String> allDevices() {
/* 11 */     return this.allDevices;
/*    */   }
/*    */   
/* 15 */   public static final DeviceList EMPTY = new DeviceList(null, List.of());
/*    */   
/*    */   public static DeviceList query() {
/* 19 */     if (!ALC10.alcIsExtensionPresent(0L, "ALC_ENUMERATE_ALL_EXT"))
/* 20 */       return EMPTY; 
/* 22 */     List<String> allDevices = Objects.<List<String>>requireNonNullElse(ALUtil.getStringList(0L, 4115), List.of());
/* 31 */     String defaultDevice = ALC10.alcGetString(0L, 4114);
/* 32 */     return new DeviceList(defaultDevice, allDevices);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\audio\DeviceList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */