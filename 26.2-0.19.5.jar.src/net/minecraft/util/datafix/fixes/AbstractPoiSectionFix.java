/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public abstract class AbstractPoiSectionFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public AbstractPoiSectionFix(Schema outputSchema, String name) {
/* 21 */     super(outputSchema, false);
/* 22 */     this.name = name;
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type<Pair<String, Dynamic<?>>> poiChunkType = DSL.named(References.POI_CHUNK.typeName(), DSL.remainderType());
/* 29 */     if (!Objects.equals(poiChunkType, getInputSchema().getType(References.POI_CHUNK)))
/* 30 */       throw new IllegalStateException("Poi type is not what was expected."); 
/* 32 */     return fixTypeEverywhere(this.name, poiChunkType, ops -> ());
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> cap(Dynamic<T> input) {
/* 36 */     return input.update("Sections", sections -> sections.updateMapValues(()));
/*    */   }
/*    */   
/*    */   private Dynamic<?> processSection(Dynamic<?> section) {
/* 42 */     return section.update("Records", this::processSectionRecords);
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> processSectionRecords(Dynamic<T> input) {
/* 46 */     return (Dynamic<T>)DataFixUtils.orElse(input.asStreamOpt().result().map(stream -> input.createList(processRecords(stream))), input);
/*    */   }
/*    */   
/*    */   protected abstract <T> Stream<Dynamic<T>> processRecords(Stream<Dynamic<T>> paramStream);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\AbstractPoiSectionFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */