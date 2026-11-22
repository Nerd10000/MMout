/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public abstract class SimpleEntityRenameFix extends EntityRenameFix {
/*    */   public SimpleEntityRenameFix(String name, Schema outputSchema, boolean changesType) {
/* 11 */     super(name, outputSchema, changesType);
/*    */   }
/*    */   
/*    */   protected Pair<String, Typed<?>> fix(String name, Typed<?> entity) {
/* 16 */     Pair<String, Dynamic<?>> pair = getNewNameAndTag(name, (Dynamic)entity.getOrCreate(DSL.remainderFinder()));
/* 17 */     return Pair.of(pair.getFirst(), entity.set(DSL.remainderFinder(), pair.getSecond()));
/*    */   }
/*    */   
/*    */   protected abstract Pair<String, Dynamic<?>> getNewNameAndTag(String paramString, Dynamic<?> paramDynamic);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\SimpleEntityRenameFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */