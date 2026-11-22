/*    */ package com.mojang.blaze3d.systems;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class BackendCreationException extends Exception {
/*    */   private final Reason reason;
/*    */   
/*    */   private final List<String> missingCapabilities;
/*    */   
/*    */   public enum Reason {
/*  8 */     GLFW_ERROR("glfw_error"),
/*  9 */     VULKAN_LOADER_MISSING("vulkan_loader_missing"),
/* 10 */     VULKAN_INSTANCE_CREATION_FAILED("vulkan_instance_creation_failed"),
/* 11 */     VULKAN_NO_DEVICE("vulkan_no_device"),
/* 12 */     VULKAN_DEVICE_VERSION_TOO_LOW("vulkan_device_version_too_low"),
/* 13 */     VULKAN_NO_GRAPHICS_QUEUE("vulkan_no_graphics_queue"),
/* 14 */     VULKAN_MISSING_EXTENSION("vulkan_missing_extension"),
/* 15 */     VULKAN_MISSING_FEATURE("vulkan_missing_feature"),
/* 16 */     OPENGL_MISSING("opengl_missing"),
/* 17 */     OTHER("other");
/*    */     
/*    */     private final String displayName;
/*    */     
/*    */     Reason(String key) {
/* 23 */       this.displayName = key;
/*    */     }
/*    */     
/*    */     public String displayName() {
/* 27 */       return this.displayName;
/*    */     }
/*    */   }
/*    */   
/*    */   public BackendCreationException(String message, Reason reason, List<String> missingCapabilities) {
/* 35 */     super(message);
/* 36 */     this.reason = reason;
/* 37 */     this.missingCapabilities = List.copyOf(missingCapabilities);
/*    */   }
/*    */   
/*    */   public BackendCreationException(String message, Reason reason) {
/* 41 */     this(message, reason, List.of());
/*    */   }
/*    */   
/*    */   public Reason getReason() {
/* 45 */     return this.reason;
/*    */   }
/*    */   
/*    */   public List<String> getMissingCapabilities() {
/* 49 */     return this.missingCapabilities;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\BackendCreationException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */