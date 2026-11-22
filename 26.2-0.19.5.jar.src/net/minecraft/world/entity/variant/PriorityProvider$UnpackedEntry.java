/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public final class UnpackedEntry<C, T> extends Record {
/*    */   private final T entry;
/*    */   
/*    */   private final int priority;
/*    */   
/*    */   private final PriorityProvider.SelectorCondition<C> condition;
/*    */   
/*    */   public UnpackedEntry(T entry, int priority, PriorityProvider.SelectorCondition<C> condition) {
/* 48 */     this.entry = entry;
/* 48 */     this.priority = priority;
/* 48 */     this.condition = condition;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry<TC;TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry<TC;TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/PriorityProvider$UnpackedEntry<TC;TT;>;
/*    */   }
/*    */   
/*    */   public T entry() {
/* 48 */     return this.entry;
/*    */   }
/*    */   
/*    */   public int priority() {
/* 48 */     return this.priority;
/*    */   }
/*    */   
/*    */   public PriorityProvider.SelectorCondition<C> condition() {
/* 48 */     return this.condition;
/*    */   }
/*    */   
/* 49 */   public static final Comparator<UnpackedEntry<?, ?>> HIGHEST_PRIORITY_FIRST = Comparator.<UnpackedEntry<?, ?>>comparingInt(UnpackedEntry::priority).reversed();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\variant\PriorityProvider$UnpackedEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */