/*    */ package com.mojang.blaze3d.shaders;
/*    */ 
/*    */ import net.minecraft.resources.FileToIdConverter;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public enum ShaderType {
/*  9 */   VERTEX("vertex", ".vsh"),
/* 10 */   FRAGMENT("fragment", ".fsh");
/*    */   
/*    */   private static final ShaderType[] TYPES;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final String extension;
/*    */   
/*    */   static {
/* 12 */     TYPES = values();
/*    */   }
/*    */   
/*    */   ShaderType(String name, String extension) {
/* 18 */     this.name = name;
/* 19 */     this.extension = extension;
/*    */   }
/*    */   
/*    */   public static ShaderType byLocation(Identifier location) {
/* 23 */     for (ShaderType type : TYPES) {
/* 24 */       if (location.getPath().endsWith(type.extension))
/* 25 */         return type; 
/*    */     } 
/* 28 */     return null;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 32 */     return this.name;
/*    */   }
/*    */   
/*    */   public FileToIdConverter idConverter() {
/* 36 */     return new FileToIdConverter("shaders", this.extension);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\shaders\ShaderType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */