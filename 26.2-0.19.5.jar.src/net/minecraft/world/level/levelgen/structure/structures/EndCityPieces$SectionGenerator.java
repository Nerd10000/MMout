package net.minecraft.world.level.levelgen.structure.structures;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

interface SectionGenerator {
  void init();
  
  boolean generate(StructureTemplateManager paramStructureTemplateManager, int paramInt, EndCityPieces.EndCityPiece paramEndCityPiece, BlockPos paramBlockPos, List<StructurePiece> paramList, RandomSource paramRandomSource);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\EndCityPieces$SectionGenerator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */