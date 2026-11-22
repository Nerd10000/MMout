/*    */ package com.mojang.blaze3d.systems;
/*    */ 
/*    */ import com.mojang.jtracy.GpuApi;
/*    */ import com.mojang.jtracy.GpuContext;
/*    */ import com.mojang.jtracy.TracyClient;
/*    */ import java.util.OptionalLong;
/*    */ 
/*    */ public class TracyGpuProfiler {
/*    */   private static final int MAX_QUERIES = 1024;
/*    */   
/*    */   private final GpuQueryPool queries;
/*    */   
/*    */   private final GpuContext context;
/*    */   
/* 16 */   private int head = 0;
/*    */   
/* 17 */   private int tail = 0;
/*    */   
/*    */   public TracyGpuProfiler(GpuDevice device) {
/* 20 */     this.queries = device.createTimestampQueryPool(1024);
/* 21 */     float period = device.getDeviceInfo().timestampPeriod();
/* 23 */     this.context = TracyClient.createGpuContext(GpuApi.OPENGL, device.getTimestampNow(), period);
/*    */   }
/*    */   
/*    */   public void close() {
/* 27 */     this.queries.close();
/*    */   }
/*    */   
/*    */   public void pushZone(CommandEncoder encoder, String name) {
/* 31 */     int queryId = nextQueryId();
/* 32 */     encoder.writeTimestamp(this.queries, queryId);
/* 33 */     this.context.beginZone(queryId, name, "", "", 0);
/*    */   }
/*    */   
/*    */   public void popZone(CommandEncoder encoder) {
/* 37 */     int queryId = nextQueryId();
/* 38 */     encoder.writeTimestamp(this.queries, queryId);
/* 39 */     this.context.endZone(queryId);
/*    */   }
/*    */   
/*    */   public void endFrame() {
/* 43 */     if (this.head < this.tail) {
/* 45 */       OptionalLong[] timestamps = this.queries.getValues(this.tail, 1024 - this.tail);
/* 46 */       for (int i = 0; i < timestamps.length; i++) {
/* 47 */         OptionalLong timestamp = timestamps[i];
/* 48 */         if (timestamp.isPresent()) {
/* 49 */           this.context.submitQueryTimestamp(this.tail, timestamp.getAsLong());
/* 50 */           this.tail = (this.tail + 1) % 1024;
/*    */         } else {
/*    */           return;
/*    */         } 
/*    */       } 
/*    */     } 
/* 57 */     if (this.tail < this.head) {
/* 58 */       OptionalLong[] timestamps = this.queries.getValues(this.tail, this.head - this.tail);
/* 59 */       for (int i = 0; i < timestamps.length; i++) {
/* 60 */         OptionalLong timestamp = timestamps[i];
/* 61 */         if (timestamp.isPresent()) {
/* 62 */           this.context.submitQueryTimestamp(this.tail, timestamp.getAsLong());
/* 63 */           this.tail = (this.tail + 1) % 1024;
/*    */         } else {
/*    */           return;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private int nextQueryId() {
/* 73 */     int id = this.head;
/* 74 */     this.head = (this.head + 1) % 1024;
/* 75 */     return id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\TracyGpuProfiler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */