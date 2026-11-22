package net.minecraft.world.item.component;

import java.util.function.Consumer;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

public interface TooltipProvider {
  void addToTooltip(Item.TooltipContext paramTooltipContext, Consumer<Component> paramConsumer, TooltipFlag paramTooltipFlag, DataComponentGetter paramDataComponentGetter);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\TooltipProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */