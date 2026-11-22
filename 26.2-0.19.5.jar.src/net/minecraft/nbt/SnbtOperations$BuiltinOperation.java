package net.minecraft.nbt;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.DynamicOps;
import java.util.List;
import net.minecraft.util.parsing.packrat.ParseState;

public interface BuiltinOperation {
  <T> T run(DynamicOps<T> paramDynamicOps, List<T> paramList, ParseState<StringReader> paramParseState);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\SnbtOperations$BuiltinOperation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */