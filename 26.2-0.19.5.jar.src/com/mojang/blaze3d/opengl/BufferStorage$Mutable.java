/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ class Mutable extends BufferStorage {
/*    */   public GlBuffer createBuffer(DirectStateAccess dsa, int usage, long size) {
/* 27 */     int buffer = dsa.createBuffer();
/* 28 */     dsa.bufferData(buffer, size, usage);
/* 29 */     return new GlBuffer.Direct(dsa, usage, size, buffer, false);
/*    */   }
/*    */   
/*    */   public GlBuffer createBuffer(DirectStateAccess dsa, int usage, ByteBuffer data) {
/* 34 */     int buffer = dsa.createBuffer();
/* 35 */     int size = data.remaining();
/* 36 */     dsa.bufferData(buffer, data, usage);
/* 37 */     return new GlBuffer.Direct(dsa, usage, size, buffer, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\BufferStorage$Mutable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */