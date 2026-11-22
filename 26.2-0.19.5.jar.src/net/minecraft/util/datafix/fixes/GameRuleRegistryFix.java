/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class GameRuleRegistryFix extends DataFix {
/*     */   public GameRuleRegistryFix(Schema outputSchema) {
/*  12 */     super(outputSchema, false);
/*     */   }
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  17 */     return fixTypeEverywhereTyped("GameRuleRegistryFix", getInputSchema().getType(References.LEVEL), input -> input.update(DSL.remainderFinder(), ()));
/*     */   }
/*     */   
/*     */   private static Dynamic<?> convertInteger(Dynamic<?> oldValue) {
/* 106 */     return convertInteger(oldValue, -2147483648, 2147483647);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> convertInteger(Dynamic<?> oldValue, int min) {
/* 110 */     return convertInteger(oldValue, min, 2147483647);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> convertInteger(Dynamic<?> oldValue, int min, int max) {
/* 114 */     String stringValue = oldValue.asString("");
/*     */     try {
/* 116 */       int parsedValue = Integer.parseInt(stringValue);
/* 117 */       return oldValue.createInt(Mth.clamp(parsedValue, min, max));
/* 118 */     } catch (NumberFormatException ignored) {
/* 119 */       return oldValue;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Dynamic<?> convertBoolean(Dynamic<?> oldValue) {
/* 124 */     return oldValue.createBoolean(Boolean.parseBoolean(oldValue.asString("")));
/*     */   }
/*     */   
/*     */   private static Dynamic<?> convertBooleanInverted(Dynamic<?> oldValue) {
/* 128 */     return oldValue.createBoolean(!Boolean.parseBoolean(oldValue.asString("")));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\GameRuleRegistryFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */