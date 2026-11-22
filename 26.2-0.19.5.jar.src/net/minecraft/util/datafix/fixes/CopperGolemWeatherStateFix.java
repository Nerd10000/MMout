/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class CopperGolemWeatherStateFix extends NamedEntityFix {
/*    */   public CopperGolemWeatherStateFix(Schema outputSchema) {
/* 10 */     super(outputSchema, false, "CopperGolemWeatherStateFix", References.ENTITY, "minecraft:copper_golem");
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 15 */     return entity.update(DSL.remainderFinder(), tag -> tag.update("weather_state", CopperGolemWeatherStateFix::fixWeatherState));
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixWeatherState(Dynamic<?> value) {
/* 19 */     switch (value.asInt(0)) {
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */       
/*    */     } 
/* 19 */     return 
/*    */       
/* 23 */       value.createString("unaffected");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\CopperGolemWeatherStateFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */