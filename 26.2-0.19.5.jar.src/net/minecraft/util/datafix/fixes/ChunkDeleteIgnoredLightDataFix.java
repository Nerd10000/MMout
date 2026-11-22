/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ChunkDeleteIgnoredLightDataFix extends DataFix {
/*    */   public ChunkDeleteIgnoredLightDataFix(Schema outputSchema) {
/* 12 */     super(outputSchema, true);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/* 18 */     OpticFinder<?> sectionsFinder = chunkType.findField("sections");
/* 20 */     return fixTypeEverywhereTyped("ChunkDeleteIgnoredLightDataFix", chunkType, chunk -> {
/*    */           boolean isLightOn = ((Dynamic)chunk.get(DSL.remainderFinder())).get("isLightOn").asBoolean(false);
/*    */           return !isLightOn ? chunk.updateTyped(sectionsFinder, ()) : chunk;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ChunkDeleteIgnoredLightDataFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */