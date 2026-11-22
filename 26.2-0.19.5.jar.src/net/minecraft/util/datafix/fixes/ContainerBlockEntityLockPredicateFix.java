/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ContainerBlockEntityLockPredicateFix extends DataFix {
/*    */   public ContainerBlockEntityLockPredicateFix(Schema outputSchema) {
/* 11 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("ContainerBlockEntityLockPredicateFix", (Type)getInputSchema().findChoiceType(References.BLOCK_ENTITY), ContainerBlockEntityLockPredicateFix::fixBlockEntity);
/*    */   }
/*    */   
/*    */   private static Typed<?> fixBlockEntity(Typed<?> entity) {
/* 21 */     return entity.update(DSL.remainderFinder(), tag -> tag.renameAndFixField("Lock", "lock", LockComponentPredicateFix::fixLock));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ContainerBlockEntityLockPredicateFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */