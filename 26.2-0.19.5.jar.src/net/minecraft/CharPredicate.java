/*    */ package net.minecraft;
/*    */ 
/*    */ import java.util.Objects;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface CharPredicate {
/*    */   default CharPredicate and(CharPredicate other) {
/* 10 */     Objects.requireNonNull(other);
/* 11 */     return value -> (test(value) && other.test(value));
/*    */   }
/*    */   
/*    */   default CharPredicate negate() {
/* 15 */     return value -> !test(value);
/*    */   }
/*    */   
/*    */   default CharPredicate or(CharPredicate other) {
/* 19 */     Objects.requireNonNull(other);
/* 20 */     return value -> (test(value) || other.test(value));
/*    */   }
/*    */   
/*    */   boolean test(char paramChar);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\CharPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */