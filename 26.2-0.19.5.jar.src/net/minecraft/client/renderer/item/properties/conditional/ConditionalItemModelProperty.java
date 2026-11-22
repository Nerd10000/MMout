package net.minecraft.client.renderer.item.properties.conditional;

import com.mojang.serialization.MapCodec;

public interface ConditionalItemModelProperty extends ItemModelPropertyTest {
  MapCodec<? extends ConditionalItemModelProperty> type();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\conditional\ConditionalItemModelProperty.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */