/*    */ package net.minecraft.util.context;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ContextKey<T> {
/*    */   private final Identifier name;
/*    */   
/*    */   public ContextKey(Identifier name) {
/* 10 */     this.name = name;
/*    */   }
/*    */   
/*    */   public static <T> ContextKey<T> vanilla(String name) {
/* 14 */     return new ContextKey<>(Identifier.withDefaultNamespace(name));
/*    */   }
/*    */   
/*    */   public Identifier name() {
/* 18 */     return this.name;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 23 */     return "<parameter " + String.valueOf(this.name) + ">";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\context\ContextKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */