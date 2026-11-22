package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.util.RandomSource;

abstract class FloorRoomCollection {
  public abstract String get1x1(RandomSource paramRandomSource);
  
  public abstract String get1x1Secret(RandomSource paramRandomSource);
  
  public abstract String get1x2SideEntrance(RandomSource paramRandomSource, boolean paramBoolean);
  
  public abstract String get1x2FrontEntrance(RandomSource paramRandomSource, boolean paramBoolean);
  
  public abstract String get1x2Secret(RandomSource paramRandomSource);
  
  public abstract String get2x2(RandomSource paramRandomSource);
  
  public abstract String get2x2Secret(RandomSource paramRandomSource);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces$FloorRoomCollection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */