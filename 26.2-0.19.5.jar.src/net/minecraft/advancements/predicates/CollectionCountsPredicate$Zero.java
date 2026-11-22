/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ public class Zero<T, P extends Predicate<T>> implements CollectionCountsPredicate<T, P> {
/*    */   public boolean test(Iterable<? extends T> values) {
/* 35 */     return true;
/*    */   }
/*    */   
/*    */   public List<CollectionCountsPredicate.Entry<T, P>> unpack() {
/* 40 */     return List.of();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\CollectionCountsPredicate$Zero.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */