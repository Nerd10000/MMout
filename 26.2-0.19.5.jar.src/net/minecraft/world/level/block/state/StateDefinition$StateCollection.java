/*     */ package net.minecraft.world.level.block.state;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ final class StateCollection<S extends StateHolder<?, ?>> extends Record {
/*     */   private final Map<List<Comparable<?>>, S> statesByValues;
/*     */   
/*     */   private final Map<List<Comparable<?>>, S[]> statesByPivotCache;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #225	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection<TS;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #225	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection<TS;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #225	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection<TS;>;
/*     */   }
/*     */   
/*     */   public Map<List<Comparable<?>>, S> statesByValues() {
/* 225 */     return this.statesByValues;
/*     */   }
/*     */   
/*     */   public Map<List<Comparable<?>>, S[]> statesByPivotCache() {
/* 225 */     return this.statesByPivotCache;
/*     */   }
/*     */   
/*     */   StateCollection(Map<List<Comparable<?>>, S> statesByValues, Map<List<Comparable<?>>, S[]> statesByPivotCache) {
/* 226 */     this.statesByValues = statesByValues;
/* 226 */     this.statesByPivotCache = statesByPivotCache;
/*     */   }
/*     */   
/*     */   public S[][] fillNeighborsForState(Property<?>[] propertyKeys, List<Comparable<?>> propertyValues) {
/* 232 */     StateHolder[][] arrayOfStateHolder = new StateHolder[propertyKeys.length][];
/* 233 */     List<Comparable<?>> valuesKey = new ArrayList<>(propertyValues);
/* 234 */     for (int i = 0; i < propertyKeys.length; i++)
/* 235 */       arrayOfStateHolder[i] = fillStatesForPivot(valuesKey, propertyKeys[i], i); 
/* 237 */     return (S[][])arrayOfStateHolder;
/*     */   }
/*     */   
/*     */   private <T extends Comparable<T>> S[] fillStatesForPivot(List<Comparable<?>> valuesKey, Property<T> pivot, int pivotIndex) {
/* 242 */     Comparable<?> ownPivotValue = valuesKey.set(pivotIndex, Wildcard.INSTANCE);
/*     */     try {
/* 245 */       StateHolder[] arrayOfStateHolder = (StateHolder[])this.statesByPivotCache.get(valuesKey);
/* 246 */       if (arrayOfStateHolder != null)
/* 247 */         return (S[])arrayOfStateHolder; 
/* 250 */       S[] neighbourStatesForPivot = computeStatesForPivot(valuesKey, pivot, pivotIndex);
/* 252 */       valuesKey.set(pivotIndex, Wildcard.INSTANCE);
/* 253 */       this.statesByPivotCache.put(List.copyOf(valuesKey), neighbourStatesForPivot);
/* 254 */       return neighbourStatesForPivot;
/*     */     } finally {
/* 257 */       valuesKey.set(pivotIndex, ownPivotValue);
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T extends Comparable<T>> S[] computeStatesForPivot(List<Comparable<?>> valuesKey, Property<T> pivot, int pivotIndex) {
/* 262 */     List<T> possiblePivotValues = pivot.getPossibleValues();
/* 263 */     int pivotValuesCount = possiblePivotValues.size();
/* 264 */     StateHolder[] arrayOfStateHolder = new StateHolder[pivotValuesCount];
/* 265 */     for (int pivotValueIndex = 0; pivotValueIndex < pivotValuesCount; pivotValueIndex++) {
/* 266 */       Comparable<?> comparable = (Comparable)possiblePivotValues.get(pivotValueIndex);
/* 267 */       assert pivot.getInternalIndex(comparable) == pivotValueIndex;
/* 268 */       valuesKey.set(pivotIndex, comparable);
/* 269 */       StateHolder stateHolder = Objects.<StateHolder>requireNonNull((StateHolder)this.statesByValues.get(valuesKey));
/* 270 */       arrayOfStateHolder[pivotValueIndex] = stateHolder;
/*     */     } 
/* 272 */     return (S[])arrayOfStateHolder;
/*     */   }
/*     */   
/*     */   private enum Wildcard {
/* 276 */     INSTANCE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\StateDefinition$StateCollection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */