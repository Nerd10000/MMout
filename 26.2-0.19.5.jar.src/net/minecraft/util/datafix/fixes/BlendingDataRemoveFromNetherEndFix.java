/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ 
/*    */ public class BlendingDataRemoveFromNetherEndFix extends DataFix {
/*    */   public BlendingDataRemoveFromNetherEndFix(Schema outputSchema) {
/* 13 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 18 */     Type<?> chunkType = getOutputSchema().getType(References.CHUNK);
/* 20 */     return fixTypeEverywhereTyped("BlendingDataRemoveFromNetherEndFix", chunkType, chunk -> chunk.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */   
/*    */   private static Dynamic<?> updateChunkTag(Dynamic<?> chunkTag, OptionalDynamic<?> contextTag) {
/* 26 */     boolean isOverworld = "minecraft:overworld".equals(contextTag.get("dimension").asString().result().orElse(""));
/* 27 */     return isOverworld ? chunkTag : chunkTag.remove("blending_data");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\BlendingDataRemoveFromNetherEndFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */