/*    */ package com.mojang.blaze3d.audio;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.HexFormat;
/*    */ import org.lwjgl.openal.ALC10;
/*    */ import org.lwjgl.openal.SOFTSystemEventProcI;
/*    */ import org.lwjgl.openal.SOFTSystemEvents;
/*    */ import org.lwjgl.system.MemoryUtil;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class CallbackDeviceTracker extends AbstractDeviceTracker {
/* 13 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private volatile boolean updateRequested;
/*    */   
/* 17 */   private static final int[] SUBSCRIBED_EVENT_TYPES = new int[] { 6614, 6615, 6616 };
/*    */   
/* 23 */   public static final HexFormat HEX_FORMAT = HexFormat.of();
/*    */   
/*    */   public CallbackDeviceTracker(DeviceList deviceList) {
/* 26 */     super(deviceList);
/*    */   }
/*    */   
/*    */   protected boolean isUpdateRequested() {
/* 31 */     return this.updateRequested;
/*    */   }
/*    */   
/*    */   protected void discardUpdateRequest() {
/* 36 */     this.updateRequested = false;
/*    */   }
/*    */   
/*    */   public static boolean isSupported() {
/* 41 */     if (!ALC10.alcIsExtensionPresent(0L, "ALC_SOFT_system_events"))
/* 42 */       return false; 
/* 44 */     for (int eventType : SUBSCRIBED_EVENT_TYPES) {
/* 45 */       if (!isSupportedForPlaybackDevice(eventType))
/* 46 */         return false; 
/*    */     } 
/* 49 */     return true;
/*    */   }
/*    */   
/*    */   public static CallbackDeviceTracker createAndInstall(DeviceList deviceList) {
/* 53 */     CallbackDeviceTracker result = new CallbackDeviceTracker(deviceList);
/* 54 */     SOFTSystemEvents.alcEventControlSOFT(SUBSCRIBED_EVENT_TYPES, true);
/* 55 */     SOFTSystemEvents.alcEventCallbackSOFT(result.createCallback(), 0L);
/* 56 */     return result;
/*    */   }
/*    */   
/*    */   private SOFTSystemEventProcI createCallback() {
/* 60 */     return (eventType, deviceType, device, messageLength, messagePtr, userParam) -> {
/*    */         String deviceTypeString = deviceTypeToString(deviceType);
/*    */         String message = MemoryUtil.memASCII(messagePtr, messageLength);
/*    */         switch (eventType) {
/*    */           case 6614:
/*    */             LOGGER.debug("Default {} device changed: {}", deviceTypeString, message);
/*    */             break;
/*    */           case 6615:
/*    */             LOGGER.debug("Added new {} device: {}", deviceTypeString, message);
/*    */             break;
/*    */           case 6616:
/*    */             LOGGER.debug("Removed {} device: {}", deviceTypeString, message);
/*    */             break;
/*    */         } 
/*    */         if (deviceType == 6612)
/*    */           this.updateRequested = true; 
/*    */       };
/*    */   }
/*    */   
/*    */   private static boolean isSupportedForPlaybackDevice(int eventType) {
/* 79 */     int result = SOFTSystemEvents.alcEventIsSupportedSOFT(eventType, 6612);
/* 80 */     if (result == 0) {
/* 81 */       int error = ALC10.alcGetError(0L);
/* 82 */       LOGGER.warn("Failed to check event {}, error: {}", HEX_FORMAT.toHexDigits(eventType), HEX_FORMAT.toHexDigits(error));
/* 83 */       return false;
/*    */     } 
/* 85 */     return (result == 6617);
/*    */   }
/*    */   
/*    */   private static String deviceTypeToString(int deviceType) {
/* 89 */     switch (deviceType) {
/*    */       case 6613:
/*    */       
/*    */       case 6612:
/*    */       
/*    */     } 
/* 92 */     return "unknown (0x" + HEX_FORMAT.toHexDigits(deviceType) + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\audio\CallbackDeviceTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */