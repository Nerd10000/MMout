/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class WrittenBookPagesStrictJsonFix extends ItemStackTagFix {
/*    */   public WrittenBookPagesStrictJsonFix(Schema outputSchema) {
/* 13 */     super(outputSchema, "WrittenBookPagesStrictJsonFix", id -> id.equals("minecraft:written_book"));
/*    */   }
/*    */   
/*    */   protected Typed<?> fixItemStackTag(Typed<?> tag) {
/* 19 */     Type<Pair<String, String>> textComponentType = getInputSchema().getType(References.TEXT_COMPONENT);
/* 21 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/* 22 */     OpticFinder<?> tagF = itemStackType.findField("tag");
/* 23 */     OpticFinder<?> pagesF = tagF.type().findField("pages");
/* 24 */     OpticFinder<Pair<String, String>> pageF = DSL.typeFinder(textComponentType);
/* 26 */     return tag.updateTyped(pagesF, pages -> pages.update(pageF, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\WrittenBookPagesStrictJsonFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */