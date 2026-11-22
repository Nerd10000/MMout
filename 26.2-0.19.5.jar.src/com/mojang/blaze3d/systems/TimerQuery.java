/*    */ package com.mojang.blaze3d.systems;
/*    */ 
/*    */ import java.util.OptionalLong;
/*    */ 
/*    */ public class TimerQuery implements AutoCloseable {
/*    */   private static final int ROTATIONS = 3;
/*    */   
/*    */   private CommandEncoder activeEncoder;
/*    */   
/*    */   private final GpuQueryPool queryPool;
/*    */   
/*    */   private int currentRotationIndex;
/*    */   
/* 13 */   private Status status = Status.NOT_RECORDING;
/*    */   
/* 14 */   private final long[] results = new long[3];
/*    */   
/*    */   public TimerQuery() {
/* 17 */     this.queryPool = RenderSystem.getDevice().createTimestampQueryPool(6);
/*    */   }
/*    */   
/*    */   public void beginProfile() {
/* 21 */     if (this.status != Status.NOT_RECORDING)
/* 22 */       throw new IllegalStateException("Current profile not ended"); 
/* 24 */     this.currentRotationIndex++;
/* 25 */     this.currentRotationIndex %= 3;
/* 27 */     this.activeEncoder = RenderSystem.getDevice().createCommandEncoder();
/* 28 */     this.activeEncoder.writeTimestamp(this.queryPool, this.currentRotationIndex * 2);
/* 29 */     this.status = Status.STARTED;
/*    */   }
/*    */   
/*    */   public void endProfile() {
/* 33 */     if (this.status != Status.STARTED || this.activeEncoder == null)
/* 34 */       throw new IllegalStateException("endProfile called before beginProfile"); 
/* 36 */     this.activeEncoder.writeTimestamp(this.queryPool, this.currentRotationIndex * 2 + 1);
/* 37 */     this.activeEncoder = null;
/* 38 */     this.status = Status.AWAITING_VALUES;
/*    */   }
/*    */   
/*    */   public long get() {
/* 42 */     long average = 0L;
/* 43 */     for (int i = 0; i < this.results.length; i++)
/* 44 */       average += this.results[i]; 
/* 46 */     return average / this.results.length;
/*    */   }
/*    */   
/*    */   public void close() {
/* 51 */     this.queryPool.close();
/*    */   }
/*    */   
/*    */   public Status getStatus() {
/* 55 */     if (this.status == Status.AWAITING_VALUES) {
/* 56 */       OptionalLong[] timestamps = this.queryPool.getValues(this.currentRotationIndex * 2, 2);
/* 57 */       OptionalLong startValue = timestamps[0];
/* 58 */       OptionalLong endValue = timestamps[1];
/* 59 */       if (startValue.isPresent() && endValue.isPresent()) {
/* 60 */         long delta = endValue.getAsLong() - startValue.getAsLong();
/* 61 */         this.results[this.currentRotationIndex] = (long)((float)delta * RenderSystem.getDevice().getDeviceInfo().timestampPeriod());
/* 62 */         this.status = Status.NOT_RECORDING;
/*    */       } 
/*    */     } 
/* 65 */     return this.status;
/*    */   }
/*    */   
/*    */   public enum Status {
/* 69 */     NOT_RECORDING, STARTED, AWAITING_VALUES;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\TimerQuery.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */