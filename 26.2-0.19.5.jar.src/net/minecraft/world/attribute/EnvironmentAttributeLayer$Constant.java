package net.minecraft.world.attribute;

@FunctionalInterface
public interface Constant<Value> extends EnvironmentAttributeLayer<Value> {
  Value applyConstant(Value paramValue);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\EnvironmentAttributeLayer$Constant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */