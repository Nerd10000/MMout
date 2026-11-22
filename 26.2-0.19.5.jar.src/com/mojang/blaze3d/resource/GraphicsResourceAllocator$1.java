/*    */ package com.mojang.blaze3d.resource;
/*    */ 
/*    */ class null implements GraphicsResourceAllocator {
/*    */   public <T> T acquire(ResourceDescriptor<T> descriptor) {
/*  7 */     T resource = descriptor.allocate();
/*  8 */     descriptor.prepare(resource);
/*  9 */     return resource;
/*    */   }
/*    */   
/*    */   public <T> void release(ResourceDescriptor<T> descriptor, T resource) {
/* 14 */     descriptor.free(resource);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\resource\GraphicsResourceAllocator$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */