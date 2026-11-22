package net.minecraft.commands.arguments;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;

public interface Result<T, O> {
  Holder<T> parse(ImmutableStringReader paramImmutableStringReader, HolderLookup.Provider paramProvider, DynamicOps<O> paramDynamicOps, Codec<T> paramCodec, HolderLookup.RegistryLookup<T> paramRegistryLookup) throws CommandSyntaxException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceOrIdArgument$Result.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */