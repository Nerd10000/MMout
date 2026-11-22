/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import net.minecraft.nbt.NbtFormatException;
/*    */ 
/*    */ public class WorldGenSettingsDisallowOldCustomWorldsFix extends DataFix {
/*    */   public WorldGenSettingsDisallowOldCustomWorldsFix(Schema outputSchema) {
/* 12 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type<?> worldGenSettingsType = getInputSchema().getType(References.WORLD_GEN_SETTINGS);
/* 18 */     OpticFinder<?> dimensionsFinder = worldGenSettingsType.findField("dimensions");
/* 20 */     return fixTypeEverywhereTyped("WorldGenSettingsDisallowOldCustomWorldsFix_" + getOutputSchema().getVersionKey(), worldGenSettingsType, input -> input.updateTyped(dimensionsFinder, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\WorldGenSettingsDisallowOldCustomWorldsFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */