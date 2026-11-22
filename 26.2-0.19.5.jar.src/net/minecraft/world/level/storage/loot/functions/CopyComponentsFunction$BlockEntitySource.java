/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.storage.loot.LootContextArg;
/*     */ 
/*     */ final class BlockEntitySource extends Record implements LootContextArg.Getter<BlockEntity, DataComponentGetter> {
/*     */   private final ContextKey<? extends BlockEntity> contextParam;
/*     */   
/*     */   private BlockEntitySource(ContextKey<? extends BlockEntity> contextParam) {
/* 154 */     this.contextParam = contextParam;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$BlockEntitySource;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #154	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$BlockEntitySource;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$BlockEntitySource;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #154	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$BlockEntitySource;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$BlockEntitySource;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #154	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$BlockEntitySource;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ContextKey<? extends BlockEntity> contextParam() {
/* 154 */     return this.contextParam;
/*     */   }
/*     */   
/*     */   public DataComponentGetter get(BlockEntity blockEntity) {
/* 159 */     return (DataComponentGetter)blockEntity.collectComponents();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\CopyComponentsFunction$BlockEntitySource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */