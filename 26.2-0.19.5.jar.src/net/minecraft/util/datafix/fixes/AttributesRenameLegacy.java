/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.function.UnaryOperator;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class AttributesRenameLegacy extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   private final UnaryOperator<String> renames;
/*    */   
/*    */   public AttributesRenameLegacy(Schema outputSchema, String name, UnaryOperator<String> renames) {
/* 20 */     super(outputSchema, false);
/* 21 */     this.name = name;
/* 22 */     this.renames = renames;
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/* 28 */     OpticFinder<?> tagF = itemStackType.findField("tag");
/* 29 */     return TypeRewriteRule.seq(
/* 30 */         fixTypeEverywhereTyped(this.name + " (ItemStack)", itemStackType, itemStack -> itemStack.updateTyped(tagF, this::fixItemStackTag)), new TypeRewriteRule[] { fixTypeEverywhereTyped(this.name + " (Entity)", getInputSchema().getType(References.ENTITY), this::fixEntity), 
/* 34 */           fixTypeEverywhereTyped(this.name + " (Player)", getInputSchema().getType(References.PLAYER), this::fixEntity) });
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixName(Dynamic<?> name) {
/* 39 */     Objects.requireNonNull(name);
/* 39 */     return (Dynamic)DataFixUtils.orElse(name.asString().result().map(this.renames).map(name::createString), name);
/*    */   }
/*    */   
/*    */   private Typed<?> fixItemStackTag(Typed<?> itemStack) {
/* 43 */     return itemStack.update(DSL.remainderFinder(), tag -> tag.update("AttributeModifiers", ()));
/*    */   }
/*    */   
/*    */   private Typed<?> fixEntity(Typed<?> entity) {
/* 51 */     return entity.update(DSL.remainderFinder(), tag -> tag.update("Attributes", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\AttributesRenameLegacy.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */