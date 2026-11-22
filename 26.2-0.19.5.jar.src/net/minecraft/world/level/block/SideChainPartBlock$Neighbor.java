package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.SideChainPart;

public interface Neighbor {
  BlockPos pos();
  
  boolean isConnectable();
  
  boolean isUnconnectableOrChainEnd();
  
  boolean connectsTowards(SideChainPart paramSideChainPart);
  
  default void connectToTheRight() {}
  
  default void connectToTheLeft() {}
  
  default void disconnectFromRight() {}
  
  default void disconnectFromLeft() {}
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SideChainPartBlock$Neighbor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */