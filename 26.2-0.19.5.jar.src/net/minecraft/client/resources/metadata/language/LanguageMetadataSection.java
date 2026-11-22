/*    */ package net.minecraft.client.resources.metadata.language;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.resources.language.LanguageInfo;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ 
/*    */ public final class LanguageMetadataSection extends Record {
/*    */   private final Map<String, LanguageInfo> languages;
/*    */   
/*    */   public LanguageMetadataSection(Map<String, LanguageInfo> languages) {
/* 10 */     this.languages = languages;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/language/LanguageMetadataSection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/language/LanguageMetadataSection;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/language/LanguageMetadataSection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/language/LanguageMetadataSection;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/language/LanguageMetadataSection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/metadata/language/LanguageMetadataSection;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<String, LanguageInfo> languages() {
/* 10 */     return this.languages;
/*    */   }
/*    */   
/* 11 */   public static final Codec<String> LANGUAGE_CODE_CODEC = Codec.string(1, 16);
/*    */   
/* 13 */   public static final Codec<LanguageMetadataSection> CODEC = Codec.unboundedMap(LANGUAGE_CODE_CODEC, LanguageInfo.CODEC)
/* 14 */     .xmap(LanguageMetadataSection::new, LanguageMetadataSection::languages);
/*    */   
/* 16 */   public static final MetadataSectionType<LanguageMetadataSection> TYPE = new MetadataSectionType("language", CODEC);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\metadata\language\LanguageMetadataSection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */