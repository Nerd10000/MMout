package net.minecraft.world.attribute;

import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface Positional<Value> extends EnvironmentAttributeLayer<Value> {
  Value applyPositional(Value paramValue, Vec3 paramVec3, SpatialAttributeInterpolator paramSpatialAttributeInterpolator);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\EnvironmentAttributeLayer$Positional.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */