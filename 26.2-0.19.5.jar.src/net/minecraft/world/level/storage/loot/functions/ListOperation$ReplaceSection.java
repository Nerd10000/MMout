/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class ReplaceSection extends Record implements ListOperation {
/*     */   private final int offset;
/*     */   
/*     */   private final Optional<Integer> size;
/*     */   
/*     */   public ReplaceSection(int offset, Optional<Integer> size) {
/*  85 */     this.offset = offset;
/*  85 */     this.size = size;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #85	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #85	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #85	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int offset() {
/*  85 */     return this.offset;
/*     */   }
/*     */   
/*     */   public Optional<Integer> size() {
/*  85 */     return this.size;
/*     */   }
/*     */   
/*  86 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final MapCodec<ReplaceSection> MAP_CODEC;
/*     */   
/*     */   static {
/*  88 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("offset", Integer.valueOf(0)).forGetter(ReplaceSection::offset), (App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("size").forGetter(ReplaceSection::size)).apply((Applicative)i, ReplaceSection::new));
/*     */   }
/*     */   
/*     */   public ReplaceSection(int offset) {
/*  94 */     this(offset, Optional.empty());
/*     */   }
/*     */   
/*     */   public ListOperation.Type mode() {
/*  99 */     return ListOperation.Type.REPLACE_SECTION;
/*     */   }
/*     */   
/*     */   public <T> List<T> apply(List<T> original, List<T> replacement, int maxSize) {
/* 104 */     int originalSize = original.size();
/* 105 */     if (this.offset > originalSize) {
/* 106 */       LOGGER.error("Cannot replace when offset is out of bounds");
/* 107 */       return original;
/*     */     } 
/* 110 */     ImmutableList.Builder<T> newList = ImmutableList.builder();
/* 111 */     newList.addAll(original.subList(0, this.offset));
/* 112 */     newList.addAll(replacement);
/* 113 */     int resumeIndex = this.offset + ((Integer)this.size.orElse(Integer.valueOf(replacement.size()))).intValue();
/* 114 */     if (resumeIndex < originalSize)
/* 115 */       newList.addAll(original.subList(resumeIndex, originalSize)); 
/* 117 */     ImmutableList immutableList = newList.build();
/* 118 */     if (immutableList.size() > maxSize) {
/* 119 */       LOGGER.error("Contents overflow in section replacement");
/* 120 */       return original;
/*     */     } 
/* 122 */     return (List<T>)immutableList;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\ListOperation$ReplaceSection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */