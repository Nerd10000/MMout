/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ 
/*     */ final class CloneBlockEntityInfo extends Record {
/*     */   private final CompoundTag tag;
/*     */   
/*     */   private final DataComponentMap components;
/*     */   
/*     */   private CloneBlockEntityInfo(CompoundTag tag, DataComponentMap components) {
/* 302 */     this.tag = tag;
/* 302 */     this.components = components;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/commands/CloneCommands$CloneBlockEntityInfo;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #302	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/commands/CloneCommands$CloneBlockEntityInfo;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/commands/CloneCommands$CloneBlockEntityInfo;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #302	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/commands/CloneCommands$CloneBlockEntityInfo;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/commands/CloneCommands$CloneBlockEntityInfo;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #302	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/commands/CloneCommands$CloneBlockEntityInfo;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public CompoundTag tag() {
/* 302 */     return this.tag;
/*     */   }
/*     */   
/*     */   public DataComponentMap components() {
/* 302 */     return this.components;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\CloneCommands$CloneBlockEntityInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */