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
/*    */ 
/*    */ public class LegacyDimensionIdFix extends DataFix {
/*    */   public LegacyDimensionIdFix(Schema outputSchema) {
/* 14 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 19 */     TypeRewriteRule playerRule = fixTypeEverywhereTyped("PlayerLegacyDimensionFix", getInputSchema().getType(References.PLAYER), input -> input.update(DSL.remainderFinder(), this::fixPlayer));
/* 23 */     Type<?> dataType = getInputSchema().getType(References.SAVED_DATA_MAP_DATA);
/* 24 */     OpticFinder<?> mapDataF = dataType.findField("data");
/* 26 */     TypeRewriteRule mapRule = fixTypeEverywhereTyped("MapLegacyDimensionFix", dataType, input -> input.updateTyped(mapDataF, ()));
/* 32 */     return TypeRewriteRule.seq(playerRule, mapRule);
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fixMap(Dynamic<T> remainder) {
/* 36 */     return remainder.update("dimension", this::fixDimensionId);
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fixPlayer(Dynamic<T> remainder) {
/* 40 */     return remainder.update("Dimension", this::fixDimensionId);
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fixDimensionId(Dynamic<T> id) {
/* 44 */     return (Dynamic<T>)DataFixUtils.orElse(id
/* 45 */         .asNumber().result().map(legacyId -> {
/*    */             switch (legacyId.intValue()) {
/*    */               case -1:
/*    */               
/*    */               case 1:
/*    */               
/*    */             } 
/*    */             return id.createString("minecraft:overworld");
/* 45 */           }), id);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\LegacyDimensionIdFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */