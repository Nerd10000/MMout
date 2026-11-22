package net.minecraft.nbt.visitors;

import net.minecraft.nbt.Tag;

interface ContainerBuilder {
  default void acceptKey(String id) {}
  
  void acceptValue(Tag paramTag);
  
  Tag build();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\visitors\CollectToTag$ContainerBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */