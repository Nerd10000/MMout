/*     */ package net.minecraft.util.datafix;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ class null extends DataFix {
/*     */   null(Schema outputSchema, boolean changesType) {
/* 711 */     super(outputSchema, changesType);
/*     */   }
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/* 715 */     Type<?> entityType = getInputSchema().getType(References.ENTITY);
/* 716 */     OpticFinder<String> idFinder = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/* 717 */     OpticFinder<?> customNameFinder = entityType.findField("CustomName");
/* 718 */     OpticFinder<Pair<String, String>> componentFinder = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/* 719 */     return fixTypeEverywhereTyped("Command block minecart custom name fix", entityType, input -> {
/*     */           String id = input.getOptional(idFinder).orElse("");
/*     */           return !"minecraft:commandblock_minecart".equals(id) ? input : input.updateTyped(customNameFinder, ());
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\DataFixers$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */