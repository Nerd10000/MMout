package net.minecraft.client.tutorial;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.ClientInput;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

public interface TutorialStepInstance {
  default void clear() {}
  
  default void tick() {}
  
  default void onInput(ClientInput input) {}
  
  default void onMouse(double xd, double yd) {}
  
  default void onLookAt(ClientLevel level, HitResult hit) {}
  
  default void onDestroyBlock(ClientLevel level, BlockPos pos, BlockState state, float percent) {}
  
  default void onOpenInventory() {}
  
  default void onGetItem(ItemStack itemStack) {}
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\tutorial\TutorialStepInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */