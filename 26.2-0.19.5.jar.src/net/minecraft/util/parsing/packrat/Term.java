/*     */ package net.minecraft.util.parsing.packrat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public interface Term<S> {
/*     */   boolean parse(ParseState<S> paramParseState, Scope paramScope, Control paramControl);
/*     */   
/*     */   static <S, T> Term<S> marker(Atom<T> name, T value) {
/*  10 */     return new Marker<>(name, value);
/*     */   }
/*     */   
/*     */   public static final class Marker<S, T> extends Record implements Term<S> {
/*     */     private final Atom<T> name;
/*     */     
/*     */     private final T value;
/*     */     
/*     */     public Marker(Atom<T> name, T value) {
/*  13 */       this.name = name;
/*  13 */       this.value = value;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$Marker;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #13	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker<TS;TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$Marker;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #13	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker<TS;TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$Marker;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #13	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker<TS;TT;>;
/*     */     }
/*     */     
/*     */     public Atom<T> name() {
/*  13 */       return this.name;
/*     */     }
/*     */     
/*     */     public T value() {
/*  13 */       return this.value;
/*     */     }
/*     */     
/*     */     public boolean parse(ParseState<S> state, Scope scope, Control control) {
/*  16 */       scope.put(this.name, this.value);
/*  17 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   static <S> Term<S> sequence(Term<S>... terms) {
/*  23 */     return new Sequence<>(terms);
/*     */   }
/*     */   
/*     */   public static final class Sequence<S> extends Record implements Term<S> {
/*     */     private final Term<S>[] elements;
/*     */     
/*     */     public Sequence(Term<S>[] elements) {
/*  26 */       this.elements = elements;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$Sequence;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #26	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence<TS;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$Sequence;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #26	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence<TS;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$Sequence;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #26	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence<TS;>;
/*     */     }
/*     */     
/*     */     public Term<S>[] elements() {
/*  26 */       return this.elements;
/*     */     }
/*     */     
/*     */     public boolean parse(ParseState<S> state, Scope scope, Control control) {
/*  29 */       int mark = state.mark();
/*  30 */       for (Term<S> element : this.elements) {
/*  31 */         if (!element.parse(state, scope, control)) {
/*  32 */           state.restore(mark);
/*  33 */           return false;
/*     */         } 
/*     */       } 
/*  36 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   static <S> Term<S> alternative(Term<S>... terms) {
/*  42 */     return new Alternative<>(terms);
/*     */   }
/*     */   
/*     */   public static final class Alternative<S> extends Record implements Term<S> {
/*     */     private final Term<S>[] elements;
/*     */     
/*     */     public Alternative(Term<S>[] elements) {
/*  45 */       this.elements = elements;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$Alternative;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative<TS;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$Alternative;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative<TS;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$Alternative;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative<TS;>;
/*     */     }
/*     */     
/*     */     public Term<S>[] elements() {
/*  45 */       return this.elements;
/*     */     }
/*     */     
/*     */     public boolean parse(ParseState<S> state, Scope scope, Control control) {
/*  48 */       Control controlForThis = state.acquireControl();
/*     */       try {
/*  50 */         int mark = state.mark();
/*  51 */         scope.splitFrame();
/*  52 */         for (Term<S> element : this.elements) {
/*  53 */           if (element.parse(state, scope, controlForThis)) {
/*  54 */             scope.mergeFrame();
/*  55 */             return true;
/*     */           } 
/*  57 */           scope.clearFrameValues();
/*  58 */           state.restore(mark);
/*  60 */           if (controlForThis.hasCut())
/*     */             break; 
/*     */         } 
/*  64 */         scope.popFrame();
/*  65 */         return false;
/*     */       } finally {
/*  67 */         state.releaseControl();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static <S> Term<S> optional(Term<S> term) {
/*  73 */     return new Maybe<>(term);
/*     */   }
/*     */   
/*     */   public static final class Maybe<S> extends Record implements Term<S> {
/*     */     private final Term<S> term;
/*     */     
/*     */     public Maybe(Term<S> term) {
/*  76 */       this.term = term;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$Maybe;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Maybe;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Maybe<TS;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$Maybe;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Maybe;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Maybe<TS;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$Maybe;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Maybe;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Maybe<TS;>;
/*     */     }
/*     */     
/*     */     public Term<S> term() {
/*  76 */       return this.term;
/*     */     }
/*     */     
/*     */     public boolean parse(ParseState<S> state, Scope scope, Control control) {
/*  79 */       int mark = state.mark();
/*  80 */       if (!this.term.parse(state, scope, control))
/*  81 */         state.restore(mark); 
/*  83 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   static <S, T> Term<S> repeated(NamedRule<S, T> element, Atom<List<T>> listName) {
/*  88 */     return repeated(element, listName, 0);
/*     */   }
/*     */   
/*     */   static <S, T> Term<S> repeated(NamedRule<S, T> element, Atom<List<T>> listName, int minRepetitions) {
/*  92 */     return new Repeated<>(element, listName, minRepetitions);
/*     */   }
/*     */   
/*     */   public static final class Repeated<S, T> extends Record implements Term<S> {
/*     */     private final NamedRule<S, T> element;
/*     */     
/*     */     private final Atom<List<T>> listName;
/*     */     
/*     */     private final int minRepetitions;
/*     */     
/*     */     public Repeated(NamedRule<S, T> element, Atom<List<T>> listName, int minRepetitions) {
/*  95 */       this.element = element;
/*  95 */       this.listName = listName;
/*  95 */       this.minRepetitions = minRepetitions;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$Repeated;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #95	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated<TS;TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$Repeated;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #95	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated<TS;TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$Repeated;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #95	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated<TS;TT;>;
/*     */     }
/*     */     
/*     */     public NamedRule<S, T> element() {
/*  95 */       return this.element;
/*     */     }
/*     */     
/*     */     public Atom<List<T>> listName() {
/*  95 */       return this.listName;
/*     */     }
/*     */     
/*     */     public int minRepetitions() {
/*  95 */       return this.minRepetitions;
/*     */     }
/*     */     
/*     */     public boolean parse(ParseState<S> state, Scope scope, Control control) {
/*  98 */       int entryMark, mark = state.mark();
/*  99 */       List<T> elements = new ArrayList<>(this.minRepetitions);
/*     */       while (true) {
/* 102 */         entryMark = state.mark();
/* 103 */         T parsedElement = state.parse(this.element);
/* 104 */         if (parsedElement != null) {
/* 105 */           elements.add(parsedElement);
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 107 */       state.restore(entryMark);
/* 112 */       if (elements.size() < this.minRepetitions) {
/* 113 */         state.restore(mark);
/* 114 */         return false;
/*     */       } 
/* 116 */       scope.put(this.listName, elements);
/* 117 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   static <S, T> Term<S> repeatedWithTrailingSeparator(NamedRule<S, T> element, Atom<List<T>> listName, Term<S> separator) {
/* 122 */     return repeatedWithTrailingSeparator(element, listName, separator, 0);
/*     */   }
/*     */   
/*     */   static <S, T> Term<S> repeatedWithTrailingSeparator(NamedRule<S, T> element, Atom<List<T>> listName, Term<S> separator, int minRepetitions) {
/* 126 */     return new RepeatedWithSeparator<>(element, listName, separator, minRepetitions, true);
/*     */   }
/*     */   
/*     */   static <S, T> Term<S> repeatedWithoutTrailingSeparator(NamedRule<S, T> element, Atom<List<T>> listName, Term<S> separator) {
/* 130 */     return repeatedWithoutTrailingSeparator(element, listName, separator, 0);
/*     */   }
/*     */   
/*     */   static <S, T> Term<S> repeatedWithoutTrailingSeparator(NamedRule<S, T> element, Atom<List<T>> listName, Term<S> separator, int minRepetitions) {
/* 134 */     return new RepeatedWithSeparator<>(element, listName, separator, minRepetitions, false);
/*     */   }
/*     */   
/*     */   public static final class RepeatedWithSeparator<S, T> extends Record implements Term<S> {
/*     */     private final NamedRule<S, T> element;
/*     */     
/*     */     private final Atom<List<T>> listName;
/*     */     
/*     */     private final Term<S> separator;
/*     */     
/*     */     private final int minRepetitions;
/*     */     
/*     */     private final boolean allowTrailingSeparator;
/*     */     
/*     */     public RepeatedWithSeparator(NamedRule<S, T> element, Atom<List<T>> listName, Term<S> separator, int minRepetitions, boolean allowTrailingSeparator) {
/* 138 */       this.element = element;
/* 138 */       this.listName = listName;
/* 138 */       this.separator = separator;
/* 138 */       this.minRepetitions = minRepetitions;
/* 138 */       this.allowTrailingSeparator = allowTrailingSeparator;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator<TS;TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator<TS;TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator<TS;TT;>;
/*     */     }
/*     */     
/*     */     public NamedRule<S, T> element() {
/* 138 */       return this.element;
/*     */     }
/*     */     
/*     */     public Atom<List<T>> listName() {
/* 138 */       return this.listName;
/*     */     }
/*     */     
/*     */     public Term<S> separator() {
/* 138 */       return this.separator;
/*     */     }
/*     */     
/*     */     public int minRepetitions() {
/* 138 */       return this.minRepetitions;
/*     */     }
/*     */     
/*     */     public boolean allowTrailingSeparator() {
/* 138 */       return this.allowTrailingSeparator;
/*     */     }
/*     */     
/*     */     public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 141 */       int listMark = state.mark();
/* 142 */       List<T> elements = new ArrayList<>(this.minRepetitions);
/* 144 */       boolean first = true;
/*     */       while (true) {
/* 146 */         int markBeforeSeparator = state.mark();
/* 147 */         if (!first && 
/* 148 */           !this.separator.parse(state, scope, control)) {
/* 149 */           state.restore(markBeforeSeparator);
/*     */           break;
/*     */         } 
/* 154 */         int markAfterSeparator = state.mark();
/* 155 */         T parsedElement = state.parse(this.element);
/* 156 */         if (parsedElement != null) {
/* 157 */           elements.add(parsedElement);
/*     */         } else {
/* 159 */           if (first) {
/* 161 */             state.restore(markAfterSeparator);
/*     */             break;
/*     */           } 
/* 166 */           if (this.allowTrailingSeparator) {
/* 167 */             state.restore(markAfterSeparator);
/*     */             break;
/*     */           } 
/* 172 */           state.restore(listMark);
/* 173 */           return false;
/*     */         } 
/* 177 */         first = false;
/*     */       } 
/* 180 */       if (elements.size() < this.minRepetitions) {
/* 181 */         state.restore(listMark);
/* 182 */         return false;
/*     */       } 
/* 184 */       scope.put(this.listName, elements);
/* 185 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   static <S> Term<S> positiveLookahead(Term<S> term) {
/* 190 */     return new LookAhead<>(term, true);
/*     */   }
/*     */   
/*     */   static <S> Term<S> negativeLookahead(Term<S> term) {
/* 194 */     return new LookAhead<>(term, false);
/*     */   }
/*     */   
/*     */   public static final class LookAhead<S> extends Record implements Term<S> {
/*     */     private final Term<S> term;
/*     */     
/*     */     private final boolean positive;
/*     */     
/*     */     public LookAhead(Term<S> term, boolean positive) {
/* 197 */       this.term = term;
/* 197 */       this.positive = positive;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$LookAhead;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #197	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead<TS;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$LookAhead;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #197	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead<TS;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$LookAhead;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #197	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead<TS;>;
/*     */     }
/*     */     
/*     */     public Term<S> term() {
/* 197 */       return this.term;
/*     */     }
/*     */     
/*     */     public boolean positive() {
/* 197 */       return this.positive;
/*     */     }
/*     */     
/*     */     public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 203 */       int mark = state.mark();
/* 205 */       boolean result = this.term.parse(state.silent(), scope, control);
/* 206 */       state.restore(mark);
/* 207 */       return (this.positive == result);
/*     */     }
/*     */   }
/*     */   
/*     */   static <S> Term<S> cut() {
/* 213 */     return new Term<S>() {
/*     */         public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 216 */           control.cut();
/* 217 */           return true;
/*     */         }
/*     */         
/*     */         public String toString() {
/* 222 */           return "↑";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <S> Term<S> empty() {
/* 228 */     return new Term<S>() {
/*     */         public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 231 */           return true;
/*     */         }
/*     */         
/*     */         public String toString() {
/* 236 */           return "ε";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <S> Term<S> fail(final Object message) {
/* 242 */     return new Term<S>() {
/*     */         public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 245 */           state.errorCollector().store(state.mark(), message);
/* 246 */           return false;
/*     */         }
/*     */         
/*     */         public String toString() {
/* 251 */           return "fail";
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\Term.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */