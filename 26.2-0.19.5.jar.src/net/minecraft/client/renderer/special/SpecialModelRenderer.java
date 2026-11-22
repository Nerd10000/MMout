package net.minecraft.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import java.util.function.Consumer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3fc;

public interface SpecialModelRenderer<T> {
  void submit(T paramT, PoseStack paramPoseStack, SubmitNodeCollector paramSubmitNodeCollector, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3);
  
  void getExtents(Consumer<Vector3fc> paramConsumer);
  
  T extractArgument(ItemStack paramItemStack);
  
  public static interface BakingContext {
    EntityModelSet entityModelSet();
    
    SpriteGetter sprites();
    
    PlayerSkinRenderCache playerSkinRenderCache();
  }
  
  public static interface Unbaked<T> {
    SpecialModelRenderer<T> bake(SpecialModelRenderer.BakingContext param1BakingContext);
    
    MapCodec<? extends Unbaked<T>> type();
  }
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\SpecialModelRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */