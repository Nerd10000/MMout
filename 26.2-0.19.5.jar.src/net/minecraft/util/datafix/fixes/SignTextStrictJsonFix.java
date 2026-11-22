/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class SignTextStrictJsonFix extends NamedEntityFix {
/* 13 */   private static final List<String> LINE_FIELDS = List.of("Text1", "Text2", "Text3", "Text4");
/*    */   
/*    */   public SignTextStrictJsonFix(Schema outputSchema) {
/* 16 */     super(outputSchema, false, "SignTextStrictJsonFix", References.BLOCK_ENTITY, "Sign");
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 21 */     for (String lineField : LINE_FIELDS) {
/* 22 */       OpticFinder<?> lineF = entity.getType().findField(lineField);
/* 24 */       OpticFinder<Pair<String, String>> textComponentF = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/* 25 */       entity = entity.updateTyped(lineF, line -> line.update(textComponentF, ()));
/*    */     } 
/* 29 */     return entity;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\SignTextStrictJsonFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */