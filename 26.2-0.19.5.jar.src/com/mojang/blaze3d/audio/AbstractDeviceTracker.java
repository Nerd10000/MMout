/*    */ package com.mojang.blaze3d.audio;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public abstract class AbstractDeviceTracker implements DeviceTracker {
/*    */   private volatile DeviceList deviceList;
/*    */   
/* 10 */   private final AtomicBoolean updatePending = new AtomicBoolean();
/*    */   
/*    */   public AbstractDeviceTracker(DeviceList deviceList) {
/* 13 */     this.deviceList = deviceList;
/*    */   }
/*    */   
/*    */   protected abstract boolean isUpdateRequested();
/*    */   
/*    */   protected abstract void discardUpdateRequest();
/*    */   
/*    */   public DeviceList currentDevices() {
/* 22 */     return this.deviceList;
/*    */   }
/*    */   
/*    */   public void forceRefresh() {
/* 27 */     discardUpdateRequest();
/* 28 */     this.deviceList = DeviceList.query();
/*    */   }
/*    */   
/*    */   public void tick() {
/* 35 */     if (isUpdateRequested()) {
/* 36 */       discardUpdateRequest();
/* 38 */       if (this.updatePending.compareAndSet(false, true))
/* 39 */         Util.ioPool().execute(() -> {
/*    */               this.deviceList = DeviceList.query();
/*    */               this.updatePending.set(false);
/*    */             }); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\audio\AbstractDeviceTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */