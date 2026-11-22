package net.minecraft.client.renderer.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.resources.model.ResolvableModel;
import org.joml.Matrix4fc;

public interface Unbaked extends ResolvableModel {
  MapCodec<? extends Unbaked> type();
  
  ItemModel bake(ItemModel.BakingContext paramBakingContext, Matrix4fc paramMatrix4fc);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\ItemModel$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */