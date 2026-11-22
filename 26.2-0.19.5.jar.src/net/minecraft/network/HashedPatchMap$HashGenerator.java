package net.minecraft.network;

import java.util.function.Function;
import net.minecraft.core.component.TypedDataComponent;

@FunctionalInterface
public interface HashGenerator extends Function<TypedDataComponent<?>, Integer> {}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\HashedPatchMap$HashGenerator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */