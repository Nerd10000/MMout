/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.google.common.collect.Iterators;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ final class SimpleMap extends Record implements DataComponentMap {
/*     */   private final Reference2ObjectMap<DataComponentType<?>, Object> map;
/*     */   
/*     */   private SimpleMap(Reference2ObjectMap<DataComponentType<?>, Object> map) {
/* 192 */     this.map = map;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #192	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #192	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/core/component/DataComponentMap$Builder$SimpleMap;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Reference2ObjectMap<DataComponentType<?>, Object> map() {
/* 192 */     return this.map;
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 196 */     return (T)this.map.get(type);
/*     */   }
/*     */   
/*     */   public boolean has(DataComponentType<?> type) {
/* 201 */     return this.map.containsKey(type);
/*     */   }
/*     */   
/*     */   public Set<DataComponentType<?>> keySet() {
/* 206 */     return (Set<DataComponentType<?>>)this.map.keySet();
/*     */   }
/*     */   
/*     */   public Iterator<TypedDataComponent<?>> iterator() {
/* 211 */     return Iterators.transform((Iterator)Reference2ObjectMaps.fastIterator(this.map), TypedDataComponent::fromEntryUnchecked);
/*     */   }
/*     */   
/*     */   public int size() {
/* 216 */     return this.map.size();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 221 */     return this.map.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentMap$Builder$SimpleMap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */