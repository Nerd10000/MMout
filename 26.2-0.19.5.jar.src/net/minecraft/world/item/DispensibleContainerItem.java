package net.minecraft.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public interface DispensibleContainerItem {
  default void checkExtraContent(LivingEntity user, Level level, ItemStack itemStack, BlockPos pos) {}
  
  boolean emptyContents(LivingEntity paramLivingEntity, Level paramLevel, BlockPos paramBlockPos, BlockHitResult paramBlockHitResult);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\DispensibleContainerItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */