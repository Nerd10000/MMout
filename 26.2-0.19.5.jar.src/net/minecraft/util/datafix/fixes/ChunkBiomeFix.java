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
/*    */ import java.util.Arrays;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.IntStream;
/*    */ 
/*    */ public class ChunkBiomeFix extends DataFix {
/*    */   public ChunkBiomeFix(Schema outputSchema, boolean changesType) {
/* 16 */     super(outputSchema, changesType);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 21 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/* 22 */     OpticFinder<?> levelFinder = chunkType.findField("Level");
/* 24 */     return fixTypeEverywhereTyped("Leaves fix", chunkType, chunk -> chunk.updateTyped(levelFinder, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ChunkBiomeFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */