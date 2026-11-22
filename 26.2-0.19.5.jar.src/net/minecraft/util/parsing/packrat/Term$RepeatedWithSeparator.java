/*     */ package net.minecraft.util.parsing.packrat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class RepeatedWithSeparator<S, T> extends Record implements Term<S> {
/*     */   private final NamedRule<S, T> element;
/*     */   
/*     */   private final Atom<List<T>> listName;
/*     */   
/*     */   private final Term<S> separator;
/*     */   
/*     */   private final int minRepetitions;
/*     */   
/*     */   private final boolean allowTrailingSeparator;
/*     */   
/*     */   public RepeatedWithSeparator(NamedRule<S, T> element, Atom<List<T>> listName, Term<S> separator, int minRepetitions, boolean allowTrailingSeparator) {
/* 138 */     this.element = element;
/* 138 */     this.listName = listName;
/* 138 */     this.separator = separator;
/* 138 */     this.minRepetitions = minRepetitions;
/* 138 */     this.allowTrailingSeparator = allowTrailingSeparator;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator<TS;TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator<TS;TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator<TS;TT;>;
/*     */   }
/*     */   
/*     */   public NamedRule<S, T> element() {
/* 138 */     return this.element;
/*     */   }
/*     */   
/*     */   public Atom<List<T>> listName() {
/* 138 */     return this.listName;
/*     */   }
/*     */   
/*     */   public Term<S> separator() {
/* 138 */     return this.separator;
/*     */   }
/*     */   
/*     */   public int minRepetitions() {
/* 138 */     return this.minRepetitions;
/*     */   }
/*     */   
/*     */   public boolean allowTrailingSeparator() {
/* 138 */     return this.allowTrailingSeparator;
/*     */   }
/*     */   
/*     */   public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 141 */     int listMark = state.mark();
/* 142 */     List<T> elements = new ArrayList<>(this.minRepetitions);
/* 144 */     boolean first = true;
/*     */     while (true) {
/* 146 */       int markBeforeSeparator = state.mark();
/* 147 */       if (!first && 
/* 148 */         !this.separator.parse(state, scope, control)) {
/* 149 */         state.restore(markBeforeSeparator);
/*     */         break;
/*     */       } 
/* 154 */       int markAfterSeparator = state.mark();
/* 155 */       T parsedElement = state.parse(this.element);
/* 156 */       if (parsedElement != null) {
/* 157 */         elements.add(parsedElement);
/*     */       } else {
/* 159 */         if (first) {
/* 161 */           state.restore(markAfterSeparator);
/*     */           break;
/*     */         } 
/* 166 */         if (this.allowTrailingSeparator) {
/* 167 */           state.restore(markAfterSeparator);
/*     */           break;
/*     */         } 
/* 172 */         state.restore(listMark);
/* 173 */         return false;
/*     */       } 
/* 177 */       first = false;
/*     */     } 
/* 180 */     if (elements.size() < this.minRepetitions) {
/* 181 */       state.restore(listMark);
/* 182 */       return false;
/*     */     } 
/* 184 */     scope.put(this.listName, elements);
/* 185 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\Term$RepeatedWithSeparator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */