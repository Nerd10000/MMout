/*     */ package net.minecraft.util.parsing.packrat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class Repeated<S, T> extends Record implements Term<S> {
/*     */   private final NamedRule<S, T> element;
/*     */   
/*     */   private final Atom<List<T>> listName;
/*     */   
/*     */   private final int minRepetitions;
/*     */   
/*     */   public Repeated(NamedRule<S, T> element, Atom<List<T>> listName, int minRepetitions) {
/*  95 */     this.element = element;
/*  95 */     this.listName = listName;
/*  95 */     this.minRepetitions = minRepetitions;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$Repeated;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #95	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated<TS;TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$Repeated;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #95	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated<TS;TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$Repeated;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #95	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated<TS;TT;>;
/*     */   }
/*     */   
/*     */   public NamedRule<S, T> element() {
/*  95 */     return this.element;
/*     */   }
/*     */   
/*     */   public Atom<List<T>> listName() {
/*  95 */     return this.listName;
/*     */   }
/*     */   
/*     */   public int minRepetitions() {
/*  95 */     return this.minRepetitions;
/*     */   }
/*     */   
/*     */   public boolean parse(ParseState<S> state, Scope scope, Control control) {
/*  98 */     int entryMark, mark = state.mark();
/*  99 */     List<T> elements = new ArrayList<>(this.minRepetitions);
/*     */     while (true) {
/* 102 */       entryMark = state.mark();
/* 103 */       T parsedElement = state.parse(this.element);
/* 104 */       if (parsedElement != null) {
/* 105 */         elements.add(parsedElement);
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 107 */     state.restore(entryMark);
/* 112 */     if (elements.size() < this.minRepetitions) {
/* 113 */       state.restore(mark);
/* 114 */       return false;
/*     */     } 
/* 116 */     scope.put(this.listName, elements);
/* 117 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\Term$Repeated.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */