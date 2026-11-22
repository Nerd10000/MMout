/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class Builder {
/* 60 */   private final ImmutableMap.Builder<String, String> values = ImmutableMap.builder();
/*    */   
/* 61 */   private final ImmutableSet.Builder<String> flags = ImmutableSet.builder();
/*    */   
/*    */   public Builder define(String key, String value) {
/* 67 */     if (value.isBlank())
/* 68 */       throw new IllegalArgumentException("Cannot define empty string"); 
/* 70 */     this.values.put(key, escapeNewLines(value));
/* 71 */     return this;
/*    */   }
/*    */   
/*    */   private static String escapeNewLines(String value) {
/* 75 */     return value.replaceAll("\n", "\\\\\n");
/*    */   }
/*    */   
/*    */   public Builder define(String key, float value) {
/* 79 */     this.values.put(key, String.valueOf(value));
/* 80 */     return this;
/*    */   }
/*    */   
/*    */   public Builder define(String key, int value) {
/* 84 */     this.values.put(key, String.valueOf(value));
/* 85 */     return this;
/*    */   }
/*    */   
/*    */   public Builder define(String key) {
/* 89 */     this.flags.add(key);
/* 90 */     return this;
/*    */   }
/*    */   
/*    */   public ShaderDefines build() {
/* 94 */     return new ShaderDefines((Map<String, String>)this.values.build(), (Set<String>)this.flags.build());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\ShaderDefines$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */