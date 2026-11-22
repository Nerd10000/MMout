/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class AddFlagIfNotPresentFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   private final boolean flagValue;
/*    */   
/*    */   private final String flagKey;
/*    */   
/*    */   private final DSL.TypeReference typeReference;
/*    */   
/*    */   public AddFlagIfNotPresentFix(Schema outputSchema, DSL.TypeReference typeReference, String flagKey, boolean flagValue) {
/* 17 */     super(outputSchema, true);
/* 18 */     this.flagValue = flagValue;
/* 19 */     this.flagKey = flagKey;
/* 20 */     this.name = "AddFlagIfNotPresentFix_" + this.flagKey + "=" + this.flagValue + " for " + outputSchema.getVersionKey();
/* 21 */     this.typeReference = typeReference;
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 26 */     Type<?> worldGenSettingsType = getInputSchema().getType(this.typeReference);
/* 28 */     return fixTypeEverywhereTyped(this.name, worldGenSettingsType, settings -> settings.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\AddFlagIfNotPresentFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */