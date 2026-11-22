/*     */ package net.minecraft.world.level.block.state.properties;
/*     */ 
/*     */ public final class Value<T extends Comparable<T>> extends Record {
/*     */   private final Property<T> property;
/*     */   
/*     */   private final T value;
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/state/properties/Property$Value;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #106	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/properties/Property$Value;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/state/properties/Property$Value<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/state/properties/Property$Value;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #106	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/state/properties/Property$Value;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/state/properties/Property$Value<TT;>;
/*     */   }
/*     */   
/*     */   public Property<T> property() {
/* 106 */     return this.property;
/*     */   }
/*     */   
/*     */   public T value() {
/* 106 */     return this.value;
/*     */   }
/*     */   
/*     */   public Value(Property<T> property, T value) {
/* 111 */     if (!property.getPossibleValues().contains(value))
/* 112 */       throw new IllegalArgumentException("Value " + String.valueOf(value) + " does not belong to property " + String.valueOf(property)); 
/*     */     this.property = property;
/*     */     this.value = value;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 118 */     return this.property.getName() + "=" + this.property.getName();
/*     */   }
/*     */   
/*     */   public String valueName() {
/* 122 */     return this.property.getName(this.value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\properties\Property$Value.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */