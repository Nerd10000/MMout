/*     */ package net.minecraft.world.level.levelgen.structure.pieces;
/*     */ 
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ public interface StructureTemplateType extends StructurePieceType {
/*     */   StructurePiece load(StructureTemplateManager paramStructureTemplateManager, CompoundTag paramCompoundTag);
/*     */   
/*     */   default StructurePiece load(StructurePieceSerializationContext context, CompoundTag tag) {
/* 101 */     return load(context.structureTemplateManager(), tag);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\pieces\StructurePieceType$StructureTemplateType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */