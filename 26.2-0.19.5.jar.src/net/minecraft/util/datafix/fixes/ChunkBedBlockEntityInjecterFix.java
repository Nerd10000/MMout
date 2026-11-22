/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.common.collect.Streams;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.List;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class ChunkBedBlockEntityInjecterFix extends DataFix {
/*    */   public ChunkBedBlockEntityInjecterFix(Schema outputSchema, boolean changesType) {
/* 24 */     super(outputSchema, changesType);
/*    */   }
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 29 */     Type<?> chunkType = getOutputSchema().getType(References.CHUNK);
/* 30 */     Type<?> levelType = chunkType.findFieldType("Level");
/* 31 */     Type<?> tileEntitiesType = levelType.findFieldType("TileEntities");
/* 32 */     if (!(tileEntitiesType instanceof List.ListType))
/* 33 */       throw new IllegalStateException("Tile entity type is not a list type."); 
/* 35 */     List.ListType<?> tileEntityListType = (List.ListType)tileEntitiesType;
/* 37 */     return cap(levelType, tileEntityListType);
/*    */   }
/*    */   
/*    */   private <TE> TypeRewriteRule cap(Type<?> levelType, List.ListType<TE> tileEntityListType) {
/* 41 */     Type<TE> tileEntityType = tileEntityListType.getElement();
/* 42 */     OpticFinder<?> levelF = DSL.fieldFinder("Level", levelType);
/* 43 */     OpticFinder<List<TE>> tileEntitiesF = DSL.fieldFinder("TileEntities", (Type)tileEntityListType);
/* 46 */     int bedId = 416;
/* 48 */     return TypeRewriteRule.seq(
/* 49 */         fixTypeEverywhere("InjectBedBlockEntityType", (Type)getInputSchema().findChoiceType(References.BLOCK_ENTITY), (Type)getOutputSchema().findChoiceType(References.BLOCK_ENTITY), ops -> ()), 
/* 50 */         fixTypeEverywhereTyped("BedBlockEntityInjecter", getOutputSchema().getType(References.CHUNK), input -> {
/*    */             Typed<?> level = input.getTyped(levelF);
/*    */             Dynamic<?> levelTag = (Dynamic)level.get(DSL.remainderFinder());
/*    */             int chunkX = levelTag.get("xPos").asInt(0);
/*    */             int chunkZ = levelTag.get("zPos").asInt(0);
/*    */             List<TE> tileEntities = Lists.newArrayList((Iterable)level.getOrCreate(tileEntitiesF));
/*    */             List<? extends Dynamic<?>> sectionTags = levelTag.get("Sections").asList(Function.identity());
/*    */             for (Dynamic<?> sectionTag : sectionTags) {
/*    */               int pos = sectionTag.get("Y").asInt(0);
/*    */               Streams.mapWithIndex(sectionTag.get("Blocks").asIntStream(), ()).forEachOrdered(());
/*    */             } 
/*    */             return !tileEntities.isEmpty() ? input.set(levelF, level.set(tileEntitiesF, tileEntities)) : input;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ChunkBedBlockEntityInjecterFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */