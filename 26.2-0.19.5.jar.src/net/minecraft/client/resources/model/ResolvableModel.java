package net.minecraft.client.resources.model;

import net.minecraft.resources.Identifier;

public interface ResolvableModel {
  void resolveDependencies(Resolver paramResolver);
  
  public static interface Resolver {
    void markDependency(Identifier param1Identifier);
  }
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ResolvableModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */