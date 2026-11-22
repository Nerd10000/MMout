/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.List;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.List;
/*     */ import java.util.stream.LongStream;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class BitStorageAlignFix extends DataFix {
/*     */   private static final int BIT_TO_LONG_SHIFT = 6;
/*     */   
/*     */   private static final int HEIGHTMAP_BITS = 9;
/*     */   
/*     */   private static final int HEIGHTMAP_SIZE = 256;
/*     */   
/*     */   public BitStorageAlignFix(Schema schema) {
/*  27 */     super(schema, false);
/*     */   }
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  32 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/*  33 */     Type<?> levelType = chunkType.findFieldType("Level");
/*  35 */     OpticFinder<?> levelFinder = DSL.fieldFinder("Level", levelType);
/*  36 */     OpticFinder<?> sectionsFinder = levelFinder.type().findField("Sections");
/*  38 */     Type<?> sectionType = ((List.ListType)sectionsFinder.type()).getElement();
/*  39 */     OpticFinder<?> sectionFinder = DSL.typeFinder(sectionType);
/*  41 */     Type<Pair<String, Dynamic<?>>> blockStateType = DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType());
/*  42 */     OpticFinder<List<Pair<String, Dynamic<?>>>> paletteFinder = DSL.fieldFinder("Palette", (Type)DSL.list(blockStateType));
/*  44 */     return fixTypeEverywhereTyped("BitStorageAlignFix", chunkType, getOutputSchema().getType(References.CHUNK), chunk -> chunk.updateTyped(levelFinder, ()));
/*     */   }
/*     */   
/*     */   private Typed<?> updateHeightmaps(Typed<?> level) {
/*  52 */     return level.update(DSL.remainderFinder(), tag -> tag.update("Heightmaps", ()));
/*     */   }
/*     */   
/*     */   private static Typed<?> updateSections(OpticFinder<?> sectionsFinder, OpticFinder<?> sectionFinder, OpticFinder<List<Pair<String, Dynamic<?>>>> paletteFinder, Typed<?> level) {
/*  64 */     return level.updateTyped(sectionsFinder, sections -> sections.updateTyped(sectionFinder, ()));
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateBitStorage(Dynamic<?> tag, Dynamic<?> storage, int size, int bits) {
/*  80 */     long[] input = storage.asLongStream().toArray();
/*  81 */     long[] output = addPadding(size, bits, input);
/*  82 */     return tag.createLongList(LongStream.of(output));
/*     */   }
/*     */   
/*     */   public static long[] addPadding(int size, int bits, long[] data) {
/*  86 */     int dataLength = data.length;
/*  87 */     if (dataLength == 0)
/*  88 */       return data; 
/*  91 */     long mask = (1L << bits) - 1L;
/*  92 */     int valuesPerLong = 64 / bits;
/*  93 */     int requiredLength = (size + valuesPerLong - 1) / valuesPerLong;
/*  95 */     long[] result = new long[requiredLength];
/*  97 */     int outputDataIndex = 0;
/*  98 */     int outputStart = 0;
/*  99 */     long outputData = 0L;
/* 101 */     int currentIndex = 0;
/* 102 */     long current = data[0];
/* 103 */     long next = (dataLength > 1) ? data[1] : 0L;
/* 105 */     for (int index = 0; index < size; index++) {
/*     */       long valueToInsert;
/* 106 */       int position = index * bits;
/* 107 */       int startData = position >> 6;
/* 108 */       int endData = (index + 1) * bits - 1 >> 6;
/* 109 */       int startBit = position ^ startData << 6;
/* 111 */       if (startData != currentIndex) {
/* 112 */         current = next;
/* 113 */         next = (startData + 1 < dataLength) ? data[startData + 1] : 0L;
/* 114 */         currentIndex = startData;
/*     */       } 
/* 118 */       if (startData == endData) {
/* 119 */         valueToInsert = current >>> startBit & mask;
/*     */       } else {
/* 121 */         int shiftBits = 64 - startBit;
/* 122 */         valueToInsert = (current >>> startBit | next << shiftBits) & mask;
/*     */       } 
/* 125 */       int outputEnd = outputStart + bits;
/* 126 */       if (outputEnd >= 64) {
/* 127 */         result[outputDataIndex++] = outputData;
/* 128 */         outputData = valueToInsert;
/* 129 */         outputStart = bits;
/*     */       } else {
/* 131 */         outputData |= valueToInsert << outputStart;
/* 132 */         outputStart = outputEnd;
/*     */       } 
/*     */     } 
/* 135 */     if (outputData != 0L)
/* 136 */       result[outputDataIndex] = outputData; 
/* 139 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\BitStorageAlignFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */