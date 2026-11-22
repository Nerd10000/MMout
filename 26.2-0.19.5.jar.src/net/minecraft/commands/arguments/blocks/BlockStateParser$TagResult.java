/*    */ package net.minecraft.commands.arguments.blocks;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public final class TagResult extends Record {
/*    */   private final HolderSet<Block> tag;
/*    */   
/*    */   private final Map<String, String> vagueProperties;
/*    */   
/*    */   private final CompoundTag nbt;
/*    */   
/*    */   public TagResult(HolderSet<Block> tag, Map<String, String> vagueProperties, CompoundTag nbt) {
/* 78 */     this.tag = tag;
/* 78 */     this.vagueProperties = vagueProperties;
/* 78 */     this.nbt = nbt;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #78	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$TagResult;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HolderSet<Block> tag() {
/* 78 */     return this.tag;
/*    */   }
/*    */   
/*    */   public Map<String, String> vagueProperties() {
/* 78 */     return this.vagueProperties;
/*    */   }
/*    */   
/*    */   public CompoundTag nbt() {
/* 78 */     return this.nbt;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\blocks\BlockStateParser$TagResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */