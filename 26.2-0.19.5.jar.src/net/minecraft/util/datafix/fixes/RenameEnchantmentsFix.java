/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class RenameEnchantmentsFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   private final Map<String, String> renames;
/*    */   
/*    */   public RenameEnchantmentsFix(Schema outputSchema, String name, Map<String, String> renames) {
/* 20 */     super(outputSchema, false);
/* 21 */     this.name = name;
/* 22 */     this.renames = renames;
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type<?> item = getInputSchema().getType(References.ITEM_STACK);
/* 28 */     OpticFinder<?> tagFinder = item.findField("tag");
/* 29 */     return fixTypeEverywhereTyped(this.name, item, input -> input.updateTyped(tagFinder, ()));
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixTag(Dynamic<?> tag) {
/* 33 */     tag = fixEnchantmentList(tag, "Enchantments");
/* 34 */     tag = fixEnchantmentList(tag, "StoredEnchantments");
/* 35 */     return tag;
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixEnchantmentList(Dynamic<?> itemStack, String field) {
/* 39 */     return itemStack.update(field, tag -> {
/*    */           Objects.requireNonNull(tag);
/*    */           return (Dynamic)tag.asStreamOpt().map(()).map(tag::createList).mapOrElse(Function.identity(), ());
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\RenameEnchantmentsFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */