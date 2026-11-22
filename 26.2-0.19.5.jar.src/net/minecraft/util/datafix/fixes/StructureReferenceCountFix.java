/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class StructureReferenceCountFix extends DataFix {
/*    */   public StructureReferenceCountFix(Schema outputSchema, boolean changesType) {
/* 12 */     super(outputSchema, changesType);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type<?> structureInfo = getInputSchema().getType(References.STRUCTURE_FEATURE);
/* 18 */     return fixTypeEverywhereTyped("Structure Reference Fix", structureInfo, input -> input.update(DSL.remainderFinder(), StructureReferenceCountFix::setCountToAtLeastOne));
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> setCountToAtLeastOne(Dynamic<T> structureTag) {
/* 24 */     return structureTag.update("references", references -> references.createInt(((Integer)references.asNumber().map(Number::intValue).result().filter(()).orElse(Integer.valueOf(1))).intValue()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\StructureReferenceCountFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */