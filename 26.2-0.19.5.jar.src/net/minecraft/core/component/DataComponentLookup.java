/*    */ package net.minecraft.core.component;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMultimap;
/*    */ import com.google.common.collect.Multimap;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class DataComponentLookup<T> {
/*    */   private final Iterable<? extends Holder<T>> elements;
/*    */   
/* 16 */   private volatile Map<DataComponentType<?>, ComponentStorage<?, T>> cache = Map.of();
/*    */   
/*    */   public DataComponentLookup(Iterable<? extends Holder<T>> elements) {
/* 19 */     this.elements = elements;
/*    */   }
/*    */   
/*    */   private <C> ComponentStorage<C, T> getFromCache(DataComponentType<C> type) {
/* 24 */     return (ComponentStorage<C, T>)this.cache.get(type);
/*    */   }
/*    */   
/*    */   private <C> ComponentStorage<C, T> getOrCreateStorage(DataComponentType<C> type) {
/* 28 */     ComponentStorage<C, T> existingStorage = getFromCache(type);
/* 29 */     if (existingStorage != null)
/* 30 */       return existingStorage; 
/* 34 */     ComponentStorage<C, T> newStorage = scanForComponents(type);
/* 36 */     synchronized (this) {
/* 38 */       ComponentStorage<C, T> foreignStorage = getFromCache(type);
/* 39 */       if (foreignStorage != null)
/* 40 */         return foreignStorage; 
/* 44 */       this.cache = Util.copyAndPut(this.cache, type, newStorage);
/* 45 */       return newStorage;
/*    */     } 
/*    */   }
/*    */   
/*    */   private <C> ComponentStorage<C, T> scanForComponents(DataComponentType<C> type) {
/* 50 */     ImmutableMultimap.Builder<C, Holder<T>> results = ImmutableMultimap.builder();
/* 51 */     for (Holder<T> element : this.elements) {
/* 52 */       C componentValue = element.components().get(type);
/* 53 */       if (componentValue != null)
/* 54 */         results.put(componentValue, element); 
/*    */     } 
/* 57 */     return new ComponentStorage<>((Multimap<C, Holder<T>>)results.build());
/*    */   }
/*    */   
/*    */   public <C> Stream<Holder<T>> findMatching(DataComponentType<C> type, Predicate<C> predicate) {
/* 61 */     return getOrCreateStorage(type).findMatching(predicate);
/*    */   }
/*    */   
/*    */   public <C> Collection<Holder<T>> findAll(DataComponentType<C> type, C value) {
/* 65 */     return getOrCreateStorage(type).findAll(value);
/*    */   }
/*    */   
/*    */   public <C> Collection<Holder<T>> findAll(DataComponentType<C> type) {
/* 69 */     return (getOrCreateStorage(type)).valueToComponent.values();
/*    */   }
/*    */   
/*    */   private static final class ComponentStorage<C, T> extends Record {
/*    */     private final Multimap<C, Holder<T>> valueToComponent;
/*    */     
/*    */     private ComponentStorage(Multimap<C, Holder<T>> valueToComponent) {
/* 72 */       this.valueToComponent = valueToComponent;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #72	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage<TC;TT;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #72	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage<TC;TT;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #72	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/core/component/DataComponentLookup$ComponentStorage<TC;TT;>;
/*    */     }
/*    */     
/*    */     public Multimap<C, Holder<T>> valueToComponent() {
/* 72 */       return this.valueToComponent;
/*    */     }
/*    */     
/*    */     public Collection<Holder<T>> findAll(C value) {
/* 76 */       return this.valueToComponent.get(value);
/*    */     }
/*    */     
/*    */     public Stream<Holder<T>> findMatching(Predicate<C> predicate) {
/* 80 */       if (this.valueToComponent.isEmpty())
/* 81 */         return Stream.empty(); 
/* 83 */       return this.valueToComponent.entries().stream().filter(e -> predicate.test(e.getKey())).map(Map.Entry::getValue);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentLookup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */