/*    */ package com.mojang.blaze3d.systems;
/*    */ 
/*    */ import java.util.Set;
/*    */ 
/*    */ public final class DeviceInfo extends Record {
/*    */   private final String name;
/*    */   
/*    */   private final String vendorName;
/*    */   
/*    */   private final String driverInfo;
/*    */   
/*    */   private final boolean isZZeroToOne;
/*    */   
/*    */   private final String backendName;
/*    */   
/*    */   private final float timestampPeriod;
/*    */   
/*    */   private final DeviceLimits limits;
/*    */   
/*    */   private final DeviceFeatures features;
/*    */   
/*    */   private final Set<String> underlyingExtensions;
/*    */   
/*    */   private final HintsAndWorkarounds hintsAndWorkarounds;
/*    */   
/*    */   private final DeviceType type;
/*    */   
/*    */   public DeviceInfo(String name, String vendorName, String driverInfo, boolean isZZeroToOne, String backendName, float timestampPeriod, DeviceLimits limits, DeviceFeatures features, Set<String> underlyingExtensions, HintsAndWorkarounds hintsAndWorkarounds, DeviceType type) {
/* 18 */     this.name = name;
/* 18 */     this.vendorName = vendorName;
/* 18 */     this.driverInfo = driverInfo;
/* 18 */     this.isZZeroToOne = isZZeroToOne;
/* 18 */     this.backendName = backendName;
/* 18 */     this.timestampPeriod = timestampPeriod;
/* 18 */     this.limits = limits;
/* 18 */     this.features = features;
/* 18 */     this.underlyingExtensions = underlyingExtensions;
/* 18 */     this.hintsAndWorkarounds = hintsAndWorkarounds;
/* 18 */     this.type = type;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/systems/DeviceInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/DeviceInfo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/systems/DeviceInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/DeviceInfo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/systems/DeviceInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/systems/DeviceInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String name() {
/* 18 */     return this.name;
/*    */   }
/*    */   
/*    */   public String vendorName() {
/* 18 */     return this.vendorName;
/*    */   }
/*    */   
/*    */   public String driverInfo() {
/* 18 */     return this.driverInfo;
/*    */   }
/*    */   
/*    */   public boolean isZZeroToOne() {
/* 18 */     return this.isZZeroToOne;
/*    */   }
/*    */   
/*    */   public String backendName() {
/* 18 */     return this.backendName;
/*    */   }
/*    */   
/*    */   public float timestampPeriod() {
/* 18 */     return this.timestampPeriod;
/*    */   }
/*    */   
/*    */   public DeviceLimits limits() {
/* 18 */     return this.limits;
/*    */   }
/*    */   
/*    */   public DeviceFeatures features() {
/* 18 */     return this.features;
/*    */   }
/*    */   
/*    */   public Set<String> underlyingExtensions() {
/* 18 */     return this.underlyingExtensions;
/*    */   }
/*    */   
/*    */   public HintsAndWorkarounds hintsAndWorkarounds() {
/* 18 */     return this.hintsAndWorkarounds;
/*    */   }
/*    */   
/*    */   public DeviceType type() {
/* 18 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\DeviceInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */