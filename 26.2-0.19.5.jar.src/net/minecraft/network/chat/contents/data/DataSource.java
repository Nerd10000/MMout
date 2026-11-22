package net.minecraft.network.chat.contents.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import java.util.stream.Stream;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;

public interface DataSource {
  Stream<CompoundTag> getData(CommandSourceStack paramCommandSourceStack) throws CommandSyntaxException;
  
  MapCodec<? extends DataSource> codec();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\data\DataSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */