/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ import java.lang.invoke.MethodHandle;
/*    */ import java.lang.invoke.MethodHandles;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ public class DebugMemoryUntracker {
/*    */   private static final MethodHandle UNTRACK;
/*    */   
/*    */   static {
/* 12 */     UNTRACK = GLX.<MethodHandle>make(() -> {
/*    */           try {
/*    */             MethodHandles.Lookup lookup = MethodHandles.lookup();
/*    */             Class<?> debugAllocator = Class.forName("org.lwjgl.system.MemoryManage$DebugAllocator");
/*    */             Method reflectionUntrack = debugAllocator.getDeclaredMethod("untrack", new Class[] { long.class });
/*    */             reflectionUntrack.setAccessible(true);
/*    */             Field allocatorField = Class.forName("org.lwjgl.system.MemoryUtil$LazyInit").getDeclaredField("ALLOCATOR");
/*    */             allocatorField.setAccessible(true);
/*    */             Object allocator = allocatorField.get(null);
/*    */             return debugAllocator.isInstance(allocator) ? lookup.unreflect(reflectionUntrack) : null;
/* 28 */           } catch (ClassNotFoundException|NoSuchMethodException|NoSuchFieldException|IllegalAccessException e) {
/*    */             throw new RuntimeException(e);
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   public static void untrack(long address) {
/* 34 */     if (UNTRACK == null)
/*    */       return; 
/*    */     try {
/* 38 */       UNTRACK.invoke(address);
/* 39 */     } catch (Throwable throwable) {
/* 40 */       throw new RuntimeException(throwable);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\DebugMemoryUntracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */