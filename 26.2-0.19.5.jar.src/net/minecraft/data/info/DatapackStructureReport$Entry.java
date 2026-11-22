/*     */ package net.minecraft.data.info;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ 
/*     */ final class Entry extends Record {
/*     */   private final boolean elements;
/*     */   
/*     */   private final boolean tags;
/*     */   
/*     */   private final boolean stable;
/*     */   
/*     */   public static final MapCodec<Entry> MAP_CODEC;
/*     */   
/*     */   private Entry(boolean elements, boolean tags, boolean stable) {
/* 103 */     this.elements = elements;
/* 103 */     this.tags = tags;
/* 103 */     this.stable = stable;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/info/DatapackStructureReport$Entry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #103	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Entry;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/info/DatapackStructureReport$Entry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #103	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Entry;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/info/DatapackStructureReport$Entry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #103	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/data/info/DatapackStructureReport$Entry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public boolean elements() {
/* 103 */     return this.elements;
/*     */   }
/*     */   
/*     */   public boolean tags() {
/* 103 */     return this.tags;
/*     */   }
/*     */   
/*     */   public boolean stable() {
/* 103 */     return this.stable;
/*     */   }
/*     */   
/*     */   static {
/* 108 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.fieldOf("elements").forGetter(Entry::elements), (App)Codec.BOOL.fieldOf("tags").forGetter(Entry::tags), (App)Codec.BOOL.fieldOf("stable").forGetter(Entry::stable)).apply((Applicative)i, Entry::new));
/*     */   }
/*     */   
/* 114 */   public static final Codec<Entry> CODEC = MAP_CODEC.codec();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\info\DatapackStructureReport$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */