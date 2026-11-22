/*    */ package net.minecraft.core.component;
/*    */ 
/*    */ import com.google.common.collect.Multimap;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ 
/*    */ final class ComponentStorage<C, T> extends Record {
/*    */   private final Multimap<C, Holder<T>> valueToComponent;
/*    */   
/*    */   private ComponentStorage(Multimap<C, Holder<T>> valueToComponent) {
/* 72 */     this.valueToComponent = valueToComponent;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #72	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage<TC;TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #72	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage<TC;TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #72	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage<TC;TT;>;
/*    */   }
/*    */   
/*    */   public Multimap<C, Holder<T>> valueToComponent() {
/* 72 */     return this.valueToComponent;
/*    */   }
/*    */   
/*    */   public Collection<Holder<T>> findAll(C value) {
/* 76 */     return this.valueToComponent.get(value);
/*    */   }
/*    */   
/*    */   public Stream<Holder<T>> findMatching(Predicate<C> predicate) {
/* 80 */     if (this.valueToComponent.isEmpty())
/* 81 */       return Stream.empty(); 
/* 83 */     return this.valueToComponent.entries().stream().filter(e -> predicate.test(e.getKey())).map(Map.Entry::getValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentLookup$ComponentStorage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */