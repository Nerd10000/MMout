/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.Set;
/*    */ import org.lwjgl.opengl.GLCapabilities;
/*    */ 
/*    */ public abstract class BufferStorage {
/*    */   public static BufferStorage create(GLCapabilities capabilities, Set<String> enabledExtensions) {
/* 13 */     if (capabilities.GL_ARB_buffer_storage && GlDevice.USE_GL_ARB_buffer_storage) {
/* 14 */       enabledExtensions.add("GL_ARB_buffer_storage");
/* 15 */       return new Immutable();
/*    */     } 
/* 17 */     return new Mutable();
/*    */   }
/*    */   
/*    */   public abstract GlBuffer createBuffer(DirectStateAccess paramDirectStateAccess, int paramInt, long paramLong);
/*    */   
/*    */   public abstract GlBuffer createBuffer(DirectStateAccess paramDirectStateAccess, int paramInt, ByteBuffer paramByteBuffer);
/*    */   
/*    */   private static class Mutable extends BufferStorage {
/*    */     public GlBuffer createBuffer(DirectStateAccess dsa, int usage, long size) {
/* 27 */       int buffer = dsa.createBuffer();
/* 28 */       dsa.bufferData(buffer, size, usage);
/* 29 */       return new GlBuffer.Direct(dsa, usage, size, buffer, false);
/*    */     }
/*    */     
/*    */     public GlBuffer createBuffer(DirectStateAccess dsa, int usage, ByteBuffer data) {
/* 34 */       int buffer = dsa.createBuffer();
/* 35 */       int size = data.remaining();
/* 36 */       dsa.bufferData(buffer, data, usage);
/* 37 */       return new GlBuffer.Direct(dsa, usage, size, buffer, false);
/*    */     }
/*    */   }
/*    */   
/*    */   private static class Immutable extends BufferStorage {
/*    */     public GlBuffer createBuffer(DirectStateAccess dsa, int usage, long size) {
/* 44 */       int buffer = dsa.createBuffer();
/* 45 */       dsa.bufferStorage(buffer, size, usage);
/* 46 */       return new GlBuffer.Direct(dsa, usage, size, buffer, true);
/*    */     }
/*    */     
/*    */     public GlBuffer createBuffer(DirectStateAccess dsa, int usage, ByteBuffer data) {
/* 51 */       int buffer = dsa.createBuffer();
/* 52 */       int size = data.remaining();
/* 53 */       dsa.bufferStorage(buffer, data, usage);
/* 54 */       return new GlBuffer.Direct(dsa, usage, size, buffer, true);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\BufferStorage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */