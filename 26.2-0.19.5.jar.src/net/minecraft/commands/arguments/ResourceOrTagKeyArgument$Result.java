package net.minecraft.commands.arguments;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public interface Result<T> extends Predicate<Holder<T>> {
  Either<ResourceKey<T>, TagKey<T>> unwrap();
  
  <E> Optional<Result<E>> cast(ResourceKey<? extends Registry<E>> paramResourceKey);
  
  String asPrintable();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ResourceOrTagKeyArgument$Result.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */