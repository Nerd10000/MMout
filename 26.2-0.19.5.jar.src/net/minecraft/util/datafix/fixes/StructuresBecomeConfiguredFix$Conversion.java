/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ final class Conversion extends Record {
/*    */   private final Map<String, String> biomeMapping;
/*    */   
/*    */   private final String fallback;
/*    */   
/*    */   private Conversion(Map<String, String> biomeMapping, String fallback) {
/* 32 */     this.biomeMapping = biomeMapping;
/* 32 */     this.fallback = fallback;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<String, String> biomeMapping() {
/* 32 */     return this.biomeMapping;
/*    */   }
/*    */   
/*    */   public String fallback() {
/* 32 */     return this.fallback;
/*    */   }
/*    */   
/*    */   public static Conversion trivial(String result) {
/* 34 */     return new Conversion(Map.of(), result);
/*    */   }
/*    */   
/*    */   public static Conversion biomeMapped(Map<List<String>, String> mapping, String fallback) {
/* 38 */     return new Conversion(unpack(mapping), fallback);
/*    */   }
/*    */   
/*    */   private static Map<String, String> unpack(Map<List<String>, String> packed) {
/* 42 */     ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
/* 43 */     for (Iterator<Map.Entry<List<String>, String>> iterator = packed.entrySet().iterator(); iterator.hasNext(); ) {
/* 43 */       Map.Entry<List<String>, String> entry = iterator.next();
/* 44 */       ((List)entry.getKey()).forEach(k -> builder.put(k, entry.getValue()));
/*    */     } 
/* 46 */     return (Map<String, String>)builder.build();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\StructuresBecomeConfiguredFix$Conversion.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */