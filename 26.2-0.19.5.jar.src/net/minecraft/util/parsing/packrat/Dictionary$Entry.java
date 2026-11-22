/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ class Entry<S, T> implements NamedRule<S, T>, Supplier<String> {
/*    */   private final Atom<T> name;
/*    */   
/*    */   private Rule<S, T> value;
/*    */   
/*    */   private Entry(Atom<T> name) {
/* 79 */     this.name = name;
/*    */   }
/*    */   
/*    */   public Atom<T> name() {
/* 84 */     return this.name;
/*    */   }
/*    */   
/*    */   public Rule<S, T> value() {
/* 89 */     return Objects.<Rule<S, T>>requireNonNull(this.value, this);
/*    */   }
/*    */   
/*    */   public String get() {
/* 95 */     return "Unbound rule " + String.valueOf(this.name);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\Dictionary$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */