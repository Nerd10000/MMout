package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
interface Callback {
  void accept(List<ItemStack> paramList) throws CommandSyntaxException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\LootCommand$Callback.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */