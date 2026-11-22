/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ public class EntitySpawnerItemVariantComponentFix extends DataFix {
/*     */   public EntitySpawnerItemVariantComponentFix(Schema outputSchema) {
/*  22 */     super(outputSchema, false);
/*     */   }
/*     */   
/*     */   public final TypeRewriteRule makeRule() {
/*  27 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/*  29 */     OpticFinder<Pair<String, String>> idFinder = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/*  31 */     OpticFinder<?> componentsFinder = itemStackType.findField("components");
/*  33 */     return fixTypeEverywhereTyped("ItemStack bucket_entity_data variants to separate components", itemStackType, input -> {
/*     */           String id = input.getOptional(idFinder).map(Pair::getSecond).orElse("");
/*     */           switch (id) {
/*     */             case "minecraft:salmon_bucket":
/*     */             
/*     */             case "minecraft:axolotl_bucket":
/*     */             
/*     */             case "minecraft:tropical_fish_bucket":
/*     */             
/*     */             case "minecraft:painting":
/*     */             
/*     */           } 
/*     */           return input;
/*     */         });
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface Fixer extends Function<Typed<?>, Typed<?>> {
/*     */     default Typed<?> apply(Typed<?> components) {
/*  56 */       return components.update(DSL.remainderFinder(), this::fixRemainder);
/*     */     }
/*     */     
/*     */     default <T> Dynamic<T> fixRemainder(Dynamic<T> remainder) {
/*  60 */       return remainder.get("minecraft:bucket_entity_data").result().map(bucketData -> fixRemainder(remainder, bucketData)).orElse(remainder);
/*     */     }
/*     */     
/*     */     <T> Dynamic<T> fixRemainder(Dynamic<T> param1Dynamic1, Dynamic<T> param1Dynamic2);
/*     */   }
/*     */   
/*     */   private static String getBaseColor(int packedVariant) {
/*  67 */     return ExtraDataFixUtils.dyeColorIdToName(packedVariant >> 16 & 0xFF);
/*     */   }
/*     */   
/*     */   private static String getPatternColor(int packedVariant) {
/*  71 */     return ExtraDataFixUtils.dyeColorIdToName(packedVariant >> 24 & 0xFF);
/*     */   }
/*     */   
/*     */   private static String getPattern(int packedVariant) {
/*  75 */     switch (packedVariant & 0xFFFF) {
/*     */       default:
/*     */       
/*     */       case 256:
/*     */       
/*     */       case 512:
/*     */       
/*     */       case 768:
/*     */       
/*     */       case 1024:
/*     */       
/*     */       case 1280:
/*     */       
/*     */       case 1:
/*     */       
/*     */       case 257:
/*     */       
/*     */       case 513:
/*     */       
/*     */       case 769:
/*     */       
/*     */       case 1025:
/*     */       
/*     */       case 1281:
/*     */         break;
/*     */     } 
/*  75 */     return 
/*     */       
/*  87 */       "clayfish";
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> fixTropicalFishBucket(Dynamic<T> remainder, Dynamic<T> bucketData) {
/*  92 */     Optional<Number> oldVariant = bucketData.get("BucketVariantTag").asNumber().result();
/*  93 */     if (oldVariant.isEmpty())
/*  94 */       return remainder; 
/*  97 */     int packedVariant = ((Number)oldVariant.get()).intValue();
/*  99 */     String pattern = getPattern(packedVariant);
/* 100 */     String baseColor = getBaseColor(packedVariant);
/* 101 */     String patternColor = getPatternColor(packedVariant);
/* 103 */     return remainder
/* 104 */       .update("minecraft:bucket_entity_data", b -> b.remove("BucketVariantTag"))
/* 105 */       .set("minecraft:tropical_fish/pattern", remainder.createString(pattern))
/* 106 */       .set("minecraft:tropical_fish/base_color", remainder.createString(baseColor))
/* 107 */       .set("minecraft:tropical_fish/pattern_color", remainder.createString(patternColor));
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> fixAxolotlBucket(Dynamic<T> remainder, Dynamic<T> bucketData) {
/* 111 */     Optional<Number> oldVariant = bucketData.get("Variant").asNumber().result();
/* 112 */     if (oldVariant.isEmpty())
/* 113 */       return remainder; 
/* 116 */     switch (((Number)oldVariant.get()).intValue()) {
/*     */       default:
/*     */       
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */       
/*     */       case 3:
/*     */       
/*     */       case 4:
/*     */         break;
/*     */     } 
/* 122 */     String newVariant = "blue";
/* 124 */     return remainder
/* 125 */       .update("minecraft:bucket_entity_data", b -> b.remove("Variant"))
/* 126 */       .set("minecraft:axolotl/variant", remainder.createString(newVariant));
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> fixSalmonBucket(Dynamic<T> remainder, Dynamic<T> bucketData) {
/* 130 */     Optional<Dynamic<T>> type = bucketData.get("type").result();
/* 131 */     if (type.isEmpty())
/* 132 */       return remainder; 
/* 135 */     return remainder
/* 136 */       .update("minecraft:bucket_entity_data", b -> b.remove("type"))
/* 137 */       .set("minecraft:salmon/size", type.get());
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> fixPainting(Dynamic<T> components) {
/* 141 */     Optional<Dynamic<T>> entityData = components.get("minecraft:entity_data").result();
/* 142 */     if (entityData.isEmpty())
/* 143 */       return components; 
/* 146 */     if (((Dynamic)entityData.get()).get("id").asString().result().filter(id -> id.equals("minecraft:painting")).isEmpty())
/* 147 */       return components; 
/* 150 */     Optional<Dynamic<T>> result = ((Dynamic)entityData.get()).get("variant").result();
/* 153 */     Dynamic<T> entityDataRemainder = ((Dynamic)entityData.get()).remove("variant");
/* 154 */     if (entityDataRemainder.remove("id").equals(entityDataRemainder.emptyMap())) {
/* 155 */       components = components.remove("minecraft:entity_data");
/*     */     } else {
/* 157 */       components = components.set("minecraft:entity_data", entityDataRemainder);
/*     */     } 
/* 160 */     if (result.isPresent())
/* 161 */       components = components.set("minecraft:painting/variant", result.get()); 
/* 164 */     return components;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntitySpawnerItemVariantComponentFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */