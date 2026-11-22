/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class BannerEntityCustomNameToOverrideComponentFix extends DataFix {
/*    */   public BannerEntityCustomNameToOverrideComponentFix(Schema outputSchema) {
/* 21 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 26 */     Type<?> blockEntityType = getInputSchema().getType(References.BLOCK_ENTITY);
/* 27 */     TaggedChoice.TaggedChoiceType<?> blockEntityIdFinder = getInputSchema().findChoiceType(References.BLOCK_ENTITY);
/* 28 */     OpticFinder<?> customNameFinder = blockEntityType.findField("CustomName");
/* 30 */     OpticFinder<Pair<String, String>> textComponentFinder = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/* 32 */     return fixTypeEverywhereTyped("Banner entity custom_name to item_name component fix", blockEntityType, input -> {
/*    */           Object blockEntityId = ((Pair)input.get(blockEntityIdFinder.finder())).getFirst();
/*    */           return blockEntityId.equals("minecraft:banner") ? fix(input, textComponentFinder, customNameFinder) : input;
/*    */         });
/*    */   }
/*    */   
/*    */   private Typed<?> fix(Typed<?> input, OpticFinder<Pair<String, String>> textComponentFinder, OpticFinder<?> customNameFinder) {
/* 44 */     Optional<String> customName = input.getOptionalTyped(customNameFinder).flatMap(name -> name.getOptional(textComponentFinder).map(Pair::getSecond));
/* 49 */     boolean isOminousBanner = customName.flatMap(LegacyComponentDataFixUtils::extractTranslationString).filter(e -> e.equals("block.minecraft.ominous_banner")).isPresent();
/* 51 */     if (isOminousBanner)
/* 52 */       return Util.writeAndReadTypedOrThrow(input, input.getType(), dynamic -> {
/*    */             Dynamic<?> components = dynamic.createMap(Map.of(dynamic.createString("minecraft:item_name"), dynamic.createString(customName.get()), dynamic.createString("minecraft:hide_additional_tooltip"), dynamic.emptyMap()));
/*    */             return dynamic.set("components", components).remove("CustomName");
/*    */           }); 
/* 60 */     return input;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\BannerEntityCustomNameToOverrideComponentFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */