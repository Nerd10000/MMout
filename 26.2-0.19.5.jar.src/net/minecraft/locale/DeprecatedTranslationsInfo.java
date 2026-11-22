/*    */ package net.minecraft.locale;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.StrictJsonParser;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class DeprecatedTranslationsInfo extends Record {
/*    */   private final List<String> removed;
/*    */   
/*    */   private final Map<String, String> renamed;
/*    */   
/*    */   public DeprecatedTranslationsInfo(List<String> removed, Map<String, String> renamed) {
/* 18 */     this.removed = removed;
/* 18 */     this.renamed = renamed;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/locale/DeprecatedTranslationsInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/locale/DeprecatedTranslationsInfo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/locale/DeprecatedTranslationsInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/locale/DeprecatedTranslationsInfo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/locale/DeprecatedTranslationsInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/locale/DeprecatedTranslationsInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<String> removed() {
/* 18 */     return this.removed;
/*    */   }
/*    */   
/*    */   public Map<String, String> renamed() {
/* 18 */     return this.renamed;
/*    */   }
/*    */   
/* 22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 24 */   public static final DeprecatedTranslationsInfo EMPTY = new DeprecatedTranslationsInfo(List.of(), Map.of());
/*    */   
/*    */   public static final Codec<DeprecatedTranslationsInfo> CODEC;
/*    */   
/*    */   static {
/* 26 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.listOf().fieldOf("removed").forGetter(DeprecatedTranslationsInfo::removed), (App)Codec.unboundedMap((Codec)Codec.STRING, (Codec)Codec.STRING).fieldOf("renamed").forGetter(DeprecatedTranslationsInfo::renamed)).apply((Applicative)i, DeprecatedTranslationsInfo::new));
/*    */   }
/*    */   
/*    */   public static DeprecatedTranslationsInfo loadFromJson(InputStream stream) {
/* 32 */     JsonElement entries = StrictJsonParser.parse(new InputStreamReader(stream, StandardCharsets.UTF_8));
/* 33 */     return (DeprecatedTranslationsInfo)CODEC.parse((DynamicOps)JsonOps.INSTANCE, entries).getOrThrow(msg -> new IllegalStateException("Failed to parse deprecated language data: " + msg));
/*    */   }
/*    */   
/*    */   public static DeprecatedTranslationsInfo loadFromResource(String path) {
/*    */     try {
/* 37 */       InputStream stream = Language.class.getResourceAsStream(path);
/*    */       try {
/* 38 */         if (stream != null) {
/* 39 */           DeprecatedTranslationsInfo deprecatedTranslationsInfo = loadFromJson(stream);
/* 41 */           if (stream != null)
/* 41 */             stream.close(); 
/*    */           return deprecatedTranslationsInfo;
/*    */         } 
/* 41 */         if (stream != null)
/* 41 */           stream.close(); 
/*    */       } catch (Throwable throwable) {
/*    */         if (stream != null)
/*    */           try {
/*    */             stream.close();
/*    */           } catch (Throwable throwable1) {
/*    */             throwable.addSuppressed(throwable1);
/*    */           }  
/*    */         throw throwable;
/*    */       } 
/* 41 */     } catch (Exception e) {
/* 42 */       LOGGER.error("Failed to read {}", path, e);
/*    */     } 
/* 44 */     return EMPTY;
/*    */   }
/*    */   
/*    */   public static DeprecatedTranslationsInfo loadFromDefaultResource() {
/* 48 */     return loadFromResource("/assets/minecraft/lang/deprecated.json");
/*    */   }
/*    */   
/*    */   public void applyToMap(Map<String, String> translations) {
/* 52 */     for (String key : this.removed)
/* 53 */       translations.remove(key); 
/* 56 */     this.renamed.forEach((fromKey, toKey) -> {
/*    */           String value = (String)translations.remove(fromKey);
/*    */           if (value == null) {
/*    */             LOGGER.warn("Missing translation key for rename: {}", fromKey);
/*    */             translations.remove(toKey);
/*    */           } else {
/*    */             translations.put(toKey, value);
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\locale\DeprecatedTranslationsInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */