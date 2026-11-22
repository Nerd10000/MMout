package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.util.RandomSource;

interface Formula {
  int calculateNewCount(RandomSource paramRandomSource, int paramInt1, int paramInt2);
  
  ApplyBonusCount.FormulaType getType();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\ApplyBonusCount$Formula.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */