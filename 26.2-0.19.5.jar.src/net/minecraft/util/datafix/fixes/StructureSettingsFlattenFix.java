/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class StructureSettingsFlattenFix extends DataFix {
/*    */   public StructureSettingsFlattenFix(Schema outputSchema) {
/* 15 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 20 */     Type<?> worldGenSettingsType = getInputSchema().getType(References.WORLD_GEN_SETTINGS);
/* 22 */     OpticFinder<?> dimensions = worldGenSettingsType.findField("dimensions");
/* 24 */     return fixTypeEverywhereTyped("StructureSettingsFlatten", worldGenSettingsType, input -> input.updateTyped(dimensions, ()));
/*    */   }
/*    */   
/*    */   private static Pair<Dynamic<?>, Dynamic<?>> fixDimension(Pair<Dynamic<?>, Dynamic<?>> entry) {
/* 32 */     Dynamic<?> dimension = (Dynamic)entry.getSecond();
/* 33 */     return Pair.of(entry.getFirst(), dimension
/* 34 */         .update("generator", g -> g.update("settings", ())));
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixStructures(Dynamic<?> input) {
/* 42 */     Dynamic<?> structures = input.get("structures").orElseEmptyMap().updateMapValues(p -> p.mapSecond(()));
/* 45 */     return (Dynamic)DataFixUtils.orElse(input
/* 46 */         .get("stronghold").result().map(stronghold -> structures.set("minecraft:stronghold", stronghold.set("type", input.createString("minecraft:concentric_rings")))), structures);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\StructureSettingsFlattenFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */