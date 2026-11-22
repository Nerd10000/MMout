package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.server.packs.repository.Pack;

interface Inserter {
  void apply(List<Pack> paramList, Pack paramPack) throws CommandSyntaxException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\DataPackCommand$Inserter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */