package net.minecraft.world.item;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;

@FunctionalInterface
public interface PositionFunction {
  Position getDispensePosition(BlockSource paramBlockSource, Direction paramDirection);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\ProjectileItem$PositionFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */