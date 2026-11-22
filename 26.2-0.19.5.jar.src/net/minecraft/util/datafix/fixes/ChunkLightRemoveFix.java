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
/*    */ public class ChunkLightRemoveFix extends DataFix {
/*    */   public ChunkLightRemoveFix(Schema schema, boolean changesType) {
/* 12 */     super(schema, changesType);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/* 18 */     Type<?> levelType = chunkType.findFieldType("Level");
/* 20 */     OpticFinder<?> levelF = DSL.fieldFinder("Level", levelType);
/* 22 */     return fixTypeEverywhereTyped("ChunkLightRemoveFix", chunkType, getOutputSchema().getType(References.CHUNK), input -> input.updateTyped(levelF, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ChunkLightRemoveFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */