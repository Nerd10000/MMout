/*    */ package net.minecraft.client;
/*    */ 
/*    */ import com.mojang.blaze3d.opengl.GlBackend;
/*    */ import com.mojang.blaze3d.systems.GpuBackend;
/*    */ import com.mojang.blaze3d.vulkan.VulkanBackend;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum PreferredGraphicsApi implements StringRepresentable {
/* 11 */   DEFAULT("default", "options.graphicsApi.default"),
/* 12 */   OPENGL("opengl", "options.graphicsApi.opengl"),
/* 13 */   VULKAN("vulkan", "options.graphicsApi.vulkan");
/*    */   
/*    */   public static final Codec<PreferredGraphicsApi> CODEC;
/*    */   
/*    */   private final String serializedName;
/*    */   
/*    */   private final Component key;
/*    */   
/*    */   static {
/* 16 */     CODEC = (Codec<PreferredGraphicsApi>)StringRepresentable.fromEnum(PreferredGraphicsApi::values);
/*    */   }
/*    */   
/*    */   PreferredGraphicsApi(String serializedName, String key) {
/* 22 */     this.serializedName = serializedName;
/* 23 */     this.key = (Component)Component.translatable(key);
/*    */   }
/*    */   
/*    */   public Component caption() {
/* 27 */     return this.key;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 32 */     return this.serializedName;
/*    */   }
/*    */   
/*    */   public GpuBackend[] getBackendsToTry() {
/* 36 */     GlBackend gl = new GlBackend();
/* 37 */     VulkanBackend vulkan = new VulkanBackend();
/* 38 */     if (this == VULKAN)
/* 39 */       return new GpuBackend[] { (GpuBackend)vulkan, (GpuBackend)gl }; 
/* 41 */     return new GpuBackend[] { (GpuBackend)gl, (GpuBackend)vulkan };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\PreferredGraphicsApi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */