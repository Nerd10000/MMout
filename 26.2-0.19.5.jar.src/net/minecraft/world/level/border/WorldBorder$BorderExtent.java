package net.minecraft.world.level.border;

import net.minecraft.world.phys.shapes.VoxelShape;

interface BorderExtent {
  double getMinX(float paramFloat);
  
  double getMaxX(float paramFloat);
  
  double getMinZ(float paramFloat);
  
  double getMaxZ(float paramFloat);
  
  double getSize();
  
  double getLerpSpeed();
  
  long getLerpTime();
  
  double getLerpTarget();
  
  BorderStatus getStatus();
  
  void onAbsoluteMaxSizeChange();
  
  void onCenterChange();
  
  BorderExtent update();
  
  VoxelShape getCollisionShape();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\border\WorldBorder$BorderExtent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */