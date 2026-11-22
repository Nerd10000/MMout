/*     */ package net.minecraft.client.gui.font;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.gui.font.providers.GlyphProviderDefinition;
/*     */ 
/*     */ final class FontDefinitionFile extends Record {
/*     */   private final List<GlyphProviderDefinition.Conditional> providers;
/*     */   
/*     */   public static final Codec<FontDefinitionFile> CODEC;
/*     */   
/*     */   private FontDefinitionFile(List<GlyphProviderDefinition.Conditional> providers) {
/* 363 */     this.providers = providers;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/FontManager$FontDefinitionFile;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #363	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$FontDefinitionFile;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/FontManager$FontDefinitionFile;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #363	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$FontDefinitionFile;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/FontManager$FontDefinitionFile;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #363	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/font/FontManager$FontDefinitionFile;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public List<GlyphProviderDefinition.Conditional> providers() {
/* 363 */     return this.providers;
/*     */   }
/*     */   
/*     */   static {
/* 364 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)GlyphProviderDefinition.Conditional.CODEC.listOf().fieldOf("providers").forGetter(FontDefinitionFile::providers)).apply((Applicative)i, FontDefinitionFile::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\FontManager$FontDefinitionFile.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */