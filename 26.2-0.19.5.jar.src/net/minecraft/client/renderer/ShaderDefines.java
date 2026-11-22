/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ 
/*    */ public final class ShaderDefines extends Record {
/*    */   private final Map<String, String> values;
/*    */   
/*    */   private final Set<String> flags;
/*    */   
/*    */   public ShaderDefines(Map<String, String> values, Set<String> flags) {
/* 12 */     this.values = values;
/* 12 */     this.flags = flags;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/ShaderDefines;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/ShaderDefines;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/ShaderDefines;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/ShaderDefines;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/ShaderDefines;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/ShaderDefines;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<String, String> values() {
/* 12 */     return this.values;
/*    */   }
/*    */   
/*    */   public Set<String> flags() {
/* 12 */     return this.flags;
/*    */   }
/*    */   
/* 16 */   public static final ShaderDefines EMPTY = new ShaderDefines(Map.of(), Set.of());
/*    */   
/*    */   public static final Codec<ShaderDefines> CODEC;
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.unboundedMap((Codec)Codec.STRING, (Codec)Codec.STRING).optionalFieldOf("values", Map.of()).forGetter(ShaderDefines::values), (App)Codec.STRING.listOf().xmap(Set::copyOf, List::copyOf).optionalFieldOf("flags", Set.of()).forGetter(ShaderDefines::flags)).apply((Applicative)i, ShaderDefines::new));
/*    */   }
/*    */   
/*    */   public static Builder builder() {
/* 24 */     return new Builder();
/*    */   }
/*    */   
/*    */   public ShaderDefines withOverrides(ShaderDefines defines) {
/* 28 */     if (isEmpty())
/* 29 */       return defines; 
/* 30 */     if (defines.isEmpty())
/* 31 */       return this; 
/* 33 */     ImmutableMap.Builder<String, String> newValues = ImmutableMap.builderWithExpectedSize(this.values.size() + defines.values.size());
/* 34 */     newValues.putAll(this.values);
/* 35 */     newValues.putAll(defines.values);
/* 36 */     ImmutableSet.Builder<String> newFlags = ImmutableSet.builderWithExpectedSize(this.flags.size() + defines.flags.size());
/* 37 */     newFlags.addAll(this.flags);
/* 38 */     newFlags.addAll(defines.flags);
/* 39 */     return new ShaderDefines((Map<String, String>)newValues.buildKeepingLast(), (Set<String>)newFlags.build());
/*    */   }
/*    */   
/*    */   public String asSourceDirectives() {
/* 43 */     StringBuilder directives = new StringBuilder();
/* 44 */     for (Map.Entry<String, String> entry : this.values.entrySet()) {
/* 45 */       String key = entry.getKey();
/* 46 */       String value = entry.getValue();
/* 47 */       directives.append("#define ").append(key).append(" ").append(value).append('\n');
/*    */     } 
/* 49 */     for (String flag : this.flags)
/* 50 */       directives.append("#define ").append(flag).append('\n'); 
/* 52 */     return directives.toString();
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 56 */     return (this.values.isEmpty() && this.flags.isEmpty());
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 60 */     private final ImmutableMap.Builder<String, String> values = ImmutableMap.builder();
/*    */     
/* 61 */     private final ImmutableSet.Builder<String> flags = ImmutableSet.builder();
/*    */     
/*    */     public Builder define(String key, String value) {
/* 67 */       if (value.isBlank())
/* 68 */         throw new IllegalArgumentException("Cannot define empty string"); 
/* 70 */       this.values.put(key, escapeNewLines(value));
/* 71 */       return this;
/*    */     }
/*    */     
/*    */     private static String escapeNewLines(String value) {
/* 75 */       return value.replaceAll("\n", "\\\\\n");
/*    */     }
/*    */     
/*    */     public Builder define(String key, float value) {
/* 79 */       this.values.put(key, String.valueOf(value));
/* 80 */       return this;
/*    */     }
/*    */     
/*    */     public Builder define(String key, int value) {
/* 84 */       this.values.put(key, String.valueOf(value));
/* 85 */       return this;
/*    */     }
/*    */     
/*    */     public Builder define(String key) {
/* 89 */       this.flags.add(key);
/* 90 */       return this;
/*    */     }
/*    */     
/*    */     public ShaderDefines build() {
/* 94 */       return new ShaderDefines((Map<String, String>)this.values.build(), (Set<String>)this.flags.build());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\ShaderDefines.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */