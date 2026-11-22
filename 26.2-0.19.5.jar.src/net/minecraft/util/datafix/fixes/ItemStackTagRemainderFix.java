/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ public abstract class ItemStackTagRemainderFix extends ItemStackTagFix {
/*    */   public ItemStackTagRemainderFix(Schema outputSchema, String name, Predicate<String> idFilter) {
/* 12 */     super(outputSchema, name, idFilter);
/*    */   }
/*    */   
/*    */   protected abstract <T> Dynamic<T> fixItemStackTag(Dynamic<T> paramDynamic);
/*    */   
/*    */   protected final Typed<?> fixItemStackTag(Typed<?> tag) {
/* 19 */     return tag.update(DSL.remainderFinder(), this::fixItemStackTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ItemStackTagRemainderFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */