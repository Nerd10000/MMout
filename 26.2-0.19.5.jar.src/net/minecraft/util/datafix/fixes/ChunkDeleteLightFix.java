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
/*    */ public class ChunkDeleteLightFix extends DataFix {
/*    */   public ChunkDeleteLightFix(Schema outputSchema) {
/* 12 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/* 18 */     OpticFinder<?> sectionsFinder = chunkType.findField("sections");
/* 20 */     return fixTypeEverywhereTyped("ChunkDeleteLightFix for " + getOutputSchema().getVersionKey(), chunkType, chunk -> {
/*    */           chunk = chunk.update(DSL.remainderFinder(), ());
/*    */           return chunk.updateTyped(sectionsFinder, ());
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ChunkDeleteLightFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */