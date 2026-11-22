/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class DayTimeToClockFix extends DataFix {
/*    */   public DayTimeToClockFix(Schema outputSchema) {
/* 11 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("DayTimeToClockFix", getInputSchema().getType(References.LEVEL), typed -> typed.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */   
/*    */   private static Dynamic<?> createClock(Dynamic<?> input, long totalTicks) {
/* 27 */     return input.emptyMap()
/* 28 */       .set("total_ticks", input.createLong(totalTicks))
/* 29 */       .set("paused", input.createBoolean(false));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\DayTimeToClockFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */