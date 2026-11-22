/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class ItemLoreFix extends DataFix {
/*    */   public ItemLoreFix(Schema outputSchema) {
/* 14 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 19 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/* 21 */     Type<Pair<String, String>> textComponentType = getInputSchema().getType(References.TEXT_COMPONENT);
/* 22 */     OpticFinder<?> tagFinder = itemStackType.findField("tag");
/* 23 */     OpticFinder<?> displayFinder = tagFinder.type().findField("display");
/* 24 */     OpticFinder<?> loreFinder = displayFinder.type().findField("Lore");
/* 25 */     OpticFinder<Pair<String, String>> textComponentFinder = DSL.typeFinder(textComponentType);
/* 27 */     return fixTypeEverywhereTyped("Item Lore componentize", itemStackType, itemStack -> itemStack.updateTyped(tagFinder, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ItemLoreFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */