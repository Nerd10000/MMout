package net.minecraft.client;

import com.mojang.serialization.Codec;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.client.gui.components.AbstractWidget;

public interface ValueSet<T> {
  Function<OptionInstance<T>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<T> paramTooltipSupplier, Options paramOptions, int paramInt1, int paramInt2, int paramInt3, OptionInstance.ValueUpdateListener<? super T> paramValueUpdateListener);
  
  Optional<T> validateValue(T paramT);
  
  Codec<T> codec();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$ValueSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */