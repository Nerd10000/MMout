/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public final class StructureBlockInfo extends Record {
/*     */   private final BlockPos pos;
/*     */   
/*     */   private final BlockState state;
/*     */   
/*     */   private final CompoundTag nbt;
/*     */   
/*     */   public StructureBlockInfo(BlockPos pos, BlockState state, CompoundTag nbt) {
/* 756 */     this.pos = pos;
/* 756 */     this.state = state;
/* 756 */     this.nbt = nbt;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #756	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #756	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public BlockPos pos() {
/* 756 */     return this.pos;
/*     */   }
/*     */   
/*     */   public BlockState state() {
/* 756 */     return this.state;
/*     */   }
/*     */   
/*     */   public CompoundTag nbt() {
/* 756 */     return this.nbt;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 759 */     return String.format(Locale.ROOT, "<StructureBlockInfo | %s | %s | %s>", new Object[] { this.pos, this.state, this.nbt });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureTemplate$StructureBlockInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */