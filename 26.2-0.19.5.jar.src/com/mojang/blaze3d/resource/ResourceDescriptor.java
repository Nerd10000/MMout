/*    */ package com.mojang.blaze3d.resource;
/*    */ 
/*    */ public interface ResourceDescriptor<T> {
/*    */   T allocate();
/*    */   
/*    */   default void prepare(T resource) {}
/*    */   
/*    */   void free(T paramT);
/*    */   
/*    */   default boolean canUsePhysicalResource(ResourceDescriptor<?> other) {
/* 12 */     return equals(other);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\resource\ResourceDescriptor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */