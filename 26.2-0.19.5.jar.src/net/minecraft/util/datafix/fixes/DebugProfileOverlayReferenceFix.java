/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class DebugProfileOverlayReferenceFix extends DataFix {
/*    */   public DebugProfileOverlayReferenceFix(Schema outputSchema) {
/* 10 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 15 */     return fixTypeEverywhereTyped("DebugProfileOverlayReferenceFix", 
/* 16 */         getInputSchema().getType(References.DEBUG_PROFILE), typed -> typed.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\DebugProfileOverlayReferenceFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */