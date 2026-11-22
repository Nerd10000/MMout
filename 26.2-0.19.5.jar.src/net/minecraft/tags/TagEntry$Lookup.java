package net.minecraft.tags;

import java.util.Collection;
import net.minecraft.resources.Identifier;

public interface Lookup<T> {
  T element(Identifier paramIdentifier, boolean paramBoolean);
  
  Collection<T> tag(Identifier paramIdentifier);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\TagEntry$Lookup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */