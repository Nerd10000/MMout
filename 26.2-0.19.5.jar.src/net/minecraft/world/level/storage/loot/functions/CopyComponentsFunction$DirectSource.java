/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.level.storage.loot.LootContextArg;
/*     */ 
/*     */ final class DirectSource<T extends DataComponentGetter> extends Record implements LootContextArg.Getter<T, DataComponentGetter> {
/*     */   private final ContextKey<? extends T> contextParam;
/*     */   
/*     */   private DirectSource(ContextKey<? extends T> contextParam) {
/* 145 */     this.contextParam = contextParam;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #145	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #145	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #145	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource<TT;>;
/*     */   }
/*     */   
/*     */   public ContextKey<? extends T> contextParam() {
/* 145 */     return this.contextParam;
/*     */   }
/*     */   
/*     */   public DataComponentGetter get(T value) {
/* 150 */     return (DataComponentGetter)value;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\CopyComponentsFunction$DirectSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */