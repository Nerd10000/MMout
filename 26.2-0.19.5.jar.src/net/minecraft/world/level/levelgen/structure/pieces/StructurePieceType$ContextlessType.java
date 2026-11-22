/*    */ package net.minecraft.world.level.levelgen.structure.pieces;
/*    */ 
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ 
/*    */ public interface ContextlessType extends StructurePieceType {
/*    */   StructurePiece load(CompoundTag paramCompoundTag);
/*    */   
/*    */   default StructurePiece load(StructurePieceSerializationContext context, CompoundTag tag) {
/* 92 */     return load(tag);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\pieces\StructurePieceType$ContextlessType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */