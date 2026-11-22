/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ public final class Marker<S, T> extends Record implements Term<S> {
/*    */   private final Atom<T> name;
/*    */   
/*    */   private final T value;
/*    */   
/*    */   public Marker(Atom<T> name, T value) {
/* 13 */     this.name = name;
/* 13 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$Marker;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker<TS;TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$Marker;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker<TS;TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$Marker;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker<TS;TT;>;
/*    */   }
/*    */   
/*    */   public Atom<T> name() {
/* 13 */     return this.name;
/*    */   }
/*    */   
/*    */   public T value() {
/* 13 */     return this.value;
/*    */   }
/*    */   
/*    */   public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 16 */     scope.put(this.name, this.value);
/* 17 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\parsing\packrat\Term$Marker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */