package net.minecraft.server.commands.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;

public interface DataAccessor {
  void setData(CompoundTag paramCompoundTag) throws CommandSyntaxException;
  
  CompoundTag getData() throws CommandSyntaxException;
  
  Component getModifiedSuccess();
  
  Component getPrintSuccess(Tag paramTag);
  
  Component getPrintSuccess(NbtPathArgument.NbtPath paramNbtPath, double paramDouble, int paramInt);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\data\DataAccessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */