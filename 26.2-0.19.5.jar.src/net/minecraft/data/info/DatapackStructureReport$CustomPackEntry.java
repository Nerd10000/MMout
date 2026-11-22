/*     */ package net.minecraft.data.info;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ final class CustomPackEntry extends Record {
/*     */   private final DatapackStructureReport.Format format;
/*     */   
/*     */   private final DatapackStructureReport.Entry entry;
/*     */   
/*     */   public static final Codec<CustomPackEntry> CODEC;
/*     */   
/*     */   private CustomPackEntry(DatapackStructureReport.Format format, DatapackStructureReport.Entry entry) {
/* 136 */     this.format = format;
/* 136 */     this.entry = entry;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #136	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #136	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #136	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/data/info/DatapackStructureReport$CustomPackEntry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public DatapackStructureReport.Format format() {
/* 136 */     return this.format;
/*     */   }
/*     */   
/*     */   public DatapackStructureReport.Entry entry() {
/* 136 */     return this.entry;
/*     */   }
/*     */   
/*     */   static {
/* 140 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)DatapackStructureReport.Format.CODEC.fieldOf("format").forGetter(CustomPackEntry::format), (App)DatapackStructureReport.Entry.MAP_CODEC.forGetter(CustomPackEntry::entry)).apply((Applicative)i, CustomPackEntry::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\info\DatapackStructureReport$CustomPackEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */