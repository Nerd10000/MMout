/*   */ package net.minecraft.tags;
/*   */ 
/*   */ import com.mojang.datafixers.kinds.App;
/*   */ import com.mojang.datafixers.kinds.Applicative;
/*   */ import com.mojang.serialization.Codec;
/*   */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*   */ import java.util.List;
/*   */ import java.util.function.BiFunction;
/*   */ 
/*   */ public final class TagFile extends Record {
/*   */   private final List<TagEntry> entries;
/*   */   
/*   */   private final boolean replace;
/*   */   
/*   */   public static final Codec<TagFile> CODEC;
/*   */   
/*   */   public TagFile(List<TagEntry> entries, boolean replace) {
/* 8 */     this.entries = entries;
/* 8 */     this.replace = replace;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/tags/TagFile;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/tags/TagFile;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/tags/TagFile;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/tags/TagFile;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/tags/TagFile;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/tags/TagFile;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public List<TagEntry> entries() {
/* 8 */     return this.entries;
/*   */   }
/*   */   
/*   */   public boolean replace() {
/* 8 */     return this.replace;
/*   */   }
/*   */   
/*   */   static {
/* 9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)TagEntry.CODEC.listOf().fieldOf("values").forGetter(TagFile::entries), (App)Codec.BOOL.optionalFieldOf("replace", Boolean.valueOf(false)).forGetter(TagFile::replace)).apply((Applicative)i, TagFile::new));
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\TagFile.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */