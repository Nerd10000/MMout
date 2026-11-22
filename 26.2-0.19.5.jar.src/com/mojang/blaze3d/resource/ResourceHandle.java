/*    */ package com.mojang.blaze3d.resource;
/*    */ 
/*    */ public interface ResourceHandle<T> {
/*    */   public static final ResourceHandle<?> INVALID_HANDLE = () -> {
/*    */       throw new IllegalStateException("Cannot dereference handle with no underlying resource");
/*    */     };
/*    */   
/*    */   T get();
/*    */   
/*    */   static <T> ResourceHandle<T> invalid() {
/* 10 */     return (ResourceHandle)INVALID_HANDLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\resource\ResourceHandle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */