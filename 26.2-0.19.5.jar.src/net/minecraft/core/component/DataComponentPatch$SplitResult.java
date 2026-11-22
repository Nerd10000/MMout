/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import java.util.Set;
/*     */ 
/*     */ public final class SplitResult extends Record {
/*     */   private final DataComponentMap added;
/*     */   
/*     */   private final Set<DataComponentType<?>> removed;
/*     */   
/*     */   public SplitResult(DataComponentMap added, Set<DataComponentType<?>> removed) {
/* 206 */     this.added = added;
/* 206 */     this.removed = removed;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/DataComponentPatch$SplitResult;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #206	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentPatch$SplitResult;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentPatch$SplitResult;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #206	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentPatch$SplitResult;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentPatch$SplitResult;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #206	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/core/component/DataComponentPatch$SplitResult;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public DataComponentMap added() {
/* 206 */     return this.added;
/*     */   }
/*     */   
/*     */   public Set<DataComponentType<?>> removed() {
/* 206 */     return this.removed;
/*     */   }
/*     */   
/* 207 */   public static final SplitResult EMPTY = new SplitResult(DataComponentMap.EMPTY, Set.of());
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentPatch$SplitResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */