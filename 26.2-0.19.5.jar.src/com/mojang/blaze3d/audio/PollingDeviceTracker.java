/*    */ package com.mojang.blaze3d.audio;
/*    */ 
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class PollingDeviceTracker extends AbstractDeviceTracker {
/*    */   private static final long DEFAULT_DEVICE_CHECK_INTERVAL_MS = 1000L;
/*    */   
/*    */   private long lastDeviceCheckTime;
/*    */   
/*    */   public PollingDeviceTracker(DeviceList deviceList) {
/* 11 */     super(deviceList);
/*    */   }
/*    */   
/*    */   protected boolean isUpdateRequested() {
/* 16 */     return (Util.getMillis() - this.lastDeviceCheckTime >= 1000L);
/*    */   }
/*    */   
/*    */   protected void discardUpdateRequest() {
/* 21 */     this.lastDeviceCheckTime = Util.getMillis();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\audio\PollingDeviceTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */