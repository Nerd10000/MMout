package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.levelgen.WorldgenRandom;

@FunctionalInterface
public interface PieceConstructor {
  StructurePiece construct(WorldgenRandom paramWorldgenRandom, int paramInt1, int paramInt2);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\SinglePieceStructure$PieceConstructor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */