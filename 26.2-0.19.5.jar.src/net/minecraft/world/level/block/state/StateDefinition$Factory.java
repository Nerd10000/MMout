package net.minecraft.world.level.block.state;

import net.minecraft.world.level.block.state.properties.Property;

public interface Factory<O, S> {
  S create(O paramO, Property<?>[] paramArrayOfProperty, Comparable<?>[] paramArrayOfComparable);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\StateDefinition$Factory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */