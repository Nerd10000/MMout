/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class Dictionary<S> {
/* 12 */   private final Map<Atom<?>, Entry<S, ?>> terms = new IdentityHashMap<>();
/*    */   
/*    */   public <T> NamedRule<S, T> put(Atom<T> name, Rule<S, T> entry) {
/* 15 */     Entry<S, T> holder = (Entry<S, T>)this.terms.computeIfAbsent(name, Entry::new);
/* 16 */     if (holder.value != null)
/* 17 */       throw new IllegalArgumentException("Trying to override rule: " + String.valueOf(name)); 
/* 20 */     holder.value = entry;
/* 21 */     return holder;
/*    */   }
/*    */   
/*    */   public <T> NamedRule<S, T> putComplex(Atom<T> name, Term<S> term, Rule.RuleAction<S, T> action) {
/* 26 */     return put(name, Rule.fromTerm(term, action));
/*    */   }
/*    */   
/*    */   public <T> NamedRule<S, T> put(Atom<T> name, Term<S> term, Rule.SimpleRuleAction<S, T> action) {
/* 30 */     return put(name, Rule.fromTerm(term, action));
/*    */   }
/*    */   
/*    */   public void checkAllBound() {
/* 34 */     List<? extends Atom<?>> unboundNames = this.terms.entrySet().stream().filter(e -> (((Entry)e.getValue()).value == null)).map(Map.Entry::getKey).toList();
/* 35 */     if (!unboundNames.isEmpty())
/* 36 */       throw new IllegalStateException("Unbound names: " + String.valueOf(unboundNames)); 
/*    */   }
/*    */   
/*    */   public <T> NamedRule<S, T> getOrThrow(Atom<T> name) {
/* 42 */     return Objects.<NamedRule<S, T>>requireNonNull((Entry)this.terms.get(name), () -> "No rule called " + String.valueOf(name));
/*    */   }
/*    */   
/*    */   public <T> NamedRule<S, T> forward(Atom<T> name) {
/* 46 */     return getOrCreateEntry(name);
/*    */   }
/*    */   
/*    */   private <T> Entry<S, T> getOrCreateEntry(Atom<T> name) {
/* 51 */     return (Entry<S, T>)this.terms.computeIfAbsent(name, Entry::new);
/*    */   }
/*    */   
/*    */   public <T> Term<S> named(Atom<T> name) {
/* 55 */     return new Reference<>(getOrCreateEntry(name), name);
/*    */   }
/*    */   
/*    */   public <T> Term<S> namedWithAlias(Atom<T> nameToParse, Atom<T> nameToStore) {
/* 59 */     return new Reference<>(getOrCreateEntry(nameToParse), nameToStore);
/*    */   }
/*    */   
/*    */   private static final class Reference<S, T> extends Record implements Term<S> {
/*    */     private final Dictionary.Entry<S, T> ruleToParse;
/*    */     
/*    */     private final Atom<T> nameToStore;
/*    */     
/*    */     private Reference(Dictionary.Entry<S, T> ruleToParse, Atom<T> nameToStore) {
/* 62 */       this.ruleToParse = ruleToParse;
/* 62 */       this.nameToStore = nameToStore;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference<TS;TT;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference<TS;TT;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference<TS;TT;>;
/*    */     }
/*    */     
/*    */     public Dictionary.Entry<S, T> ruleToParse() {
/* 62 */       return this.ruleToParse;
/*    */     }
/*    */     
/*    */     public Atom<T> nameToStore() {
/* 62 */       return this.nameToStore;
/*    */     }
/*    */     
/*    */     public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 65 */       T result = state.parse(this.ruleToParse);
/* 66 */       if (result == null)
/* 67 */         return false; 
/* 69 */       scope.put(this.nameToStore, result);
/* 70 */       return true;
/*    */     }
/*    */   }
/*    */   
/*    */   private static class Entry<S, T> implements NamedRule<S, T>, Supplier<String> {
/*    */     private final Atom<T> name;
/*    */     
/*    */     private Rule<S, T> value;
/*    */     
/*    */     private Entry(Atom<T> name) {
/* 79 */       this.name = name;
/*    */     }
/*    */     
/*    */     public Atom<T> name() {
/* 84 */       return this.name;
/*    */     }
/*    */     
/*    */     public Rule<S, T> value() {
/* 89 */       return Objects.<Rule<S, T>>requireNonNull(this.value, this);
/*    */     }
/*    */     
/*    */     public String get() {
/* 95 */       return "Unbound rule " + String.valueOf(this.name);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\Dictionary.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */