/*    */ package net.minecraft.commands.arguments.blocks;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public final class BlockResult extends Record {
/*    */   private final BlockState blockState;
/*    */   
/*    */   private final Map<Property<?>, Comparable<?>> properties;
/*    */   
/*    */   private final CompoundTag nbt;
/*    */   
/*    */   public BlockResult(BlockState blockState, Map<Property<?>, Comparable<?>> properties, CompoundTag nbt) {
/* 76 */     this.blockState = blockState;
/* 76 */     this.properties = properties;
/* 76 */     this.nbt = nbt;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #76	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #76	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #76	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/arguments/blocks/BlockStateParser$BlockResult;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockState blockState() {
/* 76 */     return this.blockState;
/*    */   }
/*    */   
/*    */   public Map<Property<?>, Comparable<?>> properties() {
/* 76 */     return this.properties;
/*    */   }
/*    */   
/*    */   public CompoundTag nbt() {
/* 76 */     return this.nbt;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\blocks\BlockStateParser$BlockResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */