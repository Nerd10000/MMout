package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;

public interface Visitor {
  default void visitItem(Holder<Item> item) {}
  
  default <T> void visitComponent(DataComponentType<T> type, T value) {}
  
  default <T> void visitRemovedComponent(DataComponentType<T> type) {}
  
  default void visitSuggestions(Function<SuggestionsBuilder, CompletableFuture<Suggestions>> suggestions) {}
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\item\ItemParser$Visitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */