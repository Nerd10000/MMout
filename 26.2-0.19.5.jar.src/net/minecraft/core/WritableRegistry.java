package net.minecraft.core;

import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public interface WritableRegistry<T> extends Registry<T> {
  Holder.Reference<T> register(ResourceKey<T> paramResourceKey, T paramT, RegistrationInfo paramRegistrationInfo);
  
  void bindTags(Map<TagKey<T>, List<Holder<T>>> paramMap);
  
  boolean isEmpty();
  
  HolderGetter<T> createRegistrationLookup();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\WritableRegistry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */