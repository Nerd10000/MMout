/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.StreamSupport;
/*     */ import org.apache.commons.lang3.math.NumberUtils;
/*     */ 
/*     */ public class LevelFlatGeneratorInfoFix extends DataFix {
/*     */   private static final String GENERATOR_OPTIONS = "generatorOptions";
/*     */   
/*     */   @VisibleForTesting
/*     */   static final String DEFAULT = "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village";
/*     */   
/*     */   public LevelFlatGeneratorInfoFix(Schema outputSchema, boolean changesType) {
/*  21 */     super(outputSchema, changesType);
/*     */   }
/*     */   
/*  29 */   private static final Splitter SPLITTER = Splitter.on(';').limit(5);
/*     */   
/*  30 */   private static final Splitter LAYER_SPLITTER = Splitter.on(',');
/*     */   
/*  31 */   private static final Splitter OLD_AMOUNT_SPLITTER = Splitter.on('x').limit(2);
/*     */   
/*  32 */   private static final Splitter AMOUNT_SPLITTER = Splitter.on('*').limit(2);
/*     */   
/*  33 */   private static final Splitter BLOCK_SPLITTER = Splitter.on(':').limit(3);
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/*  37 */     return fixTypeEverywhereTyped("LevelFlatGeneratorInfoFix", getInputSchema().getType(References.LEVEL), input -> input.update(DSL.remainderFinder(), this::fix));
/*     */   }
/*     */   
/*     */   private Dynamic<?> fix(Dynamic<?> input) {
/*  41 */     if (input.get("generatorName").asString("").equalsIgnoreCase("flat"))
/*  42 */       return input.update("generatorOptions", options -> {
/*     */             Objects.requireNonNull(options);
/*     */             return (Dynamic)DataFixUtils.orElse(options.asString().map(this::fixString).map(options::createString).result(), options);
/*     */           }); 
/*  44 */     return input;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   String fixString(String generatorOptions) {
/*     */     int version;
/*     */     String layerInfo;
/*  50 */     if (generatorOptions.isEmpty())
/*  51 */       return "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village"; 
/*  54 */     Iterator<String> parts = SPLITTER.split(generatorOptions).iterator();
/*  56 */     String firstPart = parts.next();
/*  59 */     if (parts.hasNext()) {
/*  60 */       version = NumberUtils.toInt(firstPart, 0);
/*  61 */       layerInfo = parts.next();
/*     */     } else {
/*  63 */       version = 0;
/*  64 */       layerInfo = firstPart;
/*     */     } 
/*  67 */     if (version < 0 || version > 3)
/*  68 */       return "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village"; 
/*  71 */     StringBuilder result = new StringBuilder();
/*  73 */     Splitter heightSplitter = (version < 3) ? OLD_AMOUNT_SPLITTER : AMOUNT_SPLITTER;
/*  75 */     result.append(StreamSupport.stream(LAYER_SPLITTER.split(layerInfo).spliterator(), false).map(layerString -> {
/*     */             int height;
/*     */             String layerType;
/*     */             List<String> list = heightSplitter.splitToList(layerString);
/*     */             if (list.size() == 2) {
/*     */               height = NumberUtils.toInt(list.get(0));
/*     */               layerType = list.get(1);
/*     */             } else {
/*     */               height = 1;
/*     */               layerType = list.get(0);
/*     */             } 
/*     */             List<String> layerParts = BLOCK_SPLITTER.splitToList(layerType);
/*     */             int nameIndex = ((String)layerParts.get(0)).equals("minecraft") ? 1 : 0;
/*     */             String blockString = layerParts.get(nameIndex);
/*     */             int blockId = (version == 3) ? EntityBlockStateFix.getBlockId("minecraft:" + blockString) : NumberUtils.toInt(blockString, 0);
/*     */             int dataIndex = nameIndex + 1;
/*     */             int data = (layerParts.size() > dataIndex) ? NumberUtils.toInt(layerParts.get(dataIndex), 0) : 0;
/*     */             return ((height == 1) ? "" : ("" + height + "*")) + ((height == 1) ? "" : ("" + height + "*"));
/*  96 */           }).collect(Collectors.joining(",")));
/*  98 */     while (parts.hasNext())
/*  99 */       result.append(';').append(parts.next()); 
/* 102 */     return result.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\LevelFlatGeneratorInfoFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */