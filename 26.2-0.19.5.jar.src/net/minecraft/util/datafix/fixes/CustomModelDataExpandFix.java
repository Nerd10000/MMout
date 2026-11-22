/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class CustomModelDataExpandFix extends DataFix {
/*    */   public CustomModelDataExpandFix(Schema outputSchema) {
/* 14 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 19 */     Type<?> componentsType = getInputSchema().getType(References.DATA_COMPONENTS);
/* 21 */     return fixTypeEverywhereTyped("Custom Model Data expansion", componentsType, component -> component.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\CustomModelDataExpandFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */