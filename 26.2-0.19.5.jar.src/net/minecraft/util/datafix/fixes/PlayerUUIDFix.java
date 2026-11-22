/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class PlayerUUIDFix extends AbstractUUIDFix {
/*    */   public PlayerUUIDFix(Schema outputSchema) {
/* 11 */     super(outputSchema, References.PLAYER);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("PlayerUUIDFix", getInputSchema().getType(this.typeReference), input -> {
/*    */           OpticFinder<?> rootVehicleFinder = input.getType().findField("RootVehicle");
/*    */           return input.updateTyped(rootVehicleFinder, rootVehicleFinder.type(), ()).update(DSL.remainderFinder(), ());
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\PlayerUUIDFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */