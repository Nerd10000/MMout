/*    */ package net.minecraft.world.entity.ai.memory;
/*    */ 
/*    */ public final class Value<U> extends Record {
/*    */   private final MemoryModuleType<U> type;
/*    */   
/*    */   private final ExpirableValue<U> value;
/*    */   
/*    */   public Value(MemoryModuleType<U> type, ExpirableValue<U> value) {
/* 65 */     this.type = type;
/* 65 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #65	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value<TU;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #65	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value<TU;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #65	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value<TU;>;
/*    */   }
/*    */   
/*    */   public MemoryModuleType<U> type() {
/* 65 */     return this.type;
/*    */   }
/*    */   
/*    */   public ExpirableValue<U> value() {
/* 65 */     return this.value;
/*    */   }
/*    */   
/*    */   public static <U> Value<U> createUnchecked(MemoryModuleType<U> type, ExpirableValue<?> value) {
/* 68 */     return new Value<>(type, (ExpirableValue)value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\memory\MemoryMap$Value.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */