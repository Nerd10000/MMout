/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ class Immutable extends BufferStorage {
/*    */   public GlBuffer createBuffer(DirectStateAccess dsa, int usage, long size) {
/* 44 */     int buffer = dsa.createBuffer();
/* 45 */     dsa.bufferStorage(buffer, size, usage);
/* 46 */     return new GlBuffer.Direct(dsa, usage, size, buffer, true);
/*    */   }
/*    */   
/*    */   public GlBuffer createBuffer(DirectStateAccess dsa, int usage, ByteBuffer data) {
/* 51 */     int buffer = dsa.createBuffer();
/* 52 */     int size = data.remaining();
/* 53 */     dsa.bufferStorage(buffer, data, usage);
/* 54 */     return new GlBuffer.Direct(dsa, usage, size, buffer, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\BufferStorage$Immutable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */