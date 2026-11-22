package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.resources.Identifier;

public interface Context<T, C, P> {
  T forElementType(ImmutableStringReader paramImmutableStringReader, Identifier paramIdentifier) throws CommandSyntaxException;
  
  Stream<Identifier> listElementTypes();
  
  T forTagType(ImmutableStringReader paramImmutableStringReader, Identifier paramIdentifier) throws CommandSyntaxException;
  
  Stream<Identifier> listTagTypes();
  
  C lookupComponentType(ImmutableStringReader paramImmutableStringReader, Identifier paramIdentifier) throws CommandSyntaxException;
  
  Stream<Identifier> listComponentTypes();
  
  T createComponentTest(ImmutableStringReader paramImmutableStringReader, C paramC, Dynamic<?> paramDynamic) throws CommandSyntaxException;
  
  T createComponentTest(ImmutableStringReader paramImmutableStringReader, C paramC);
  
  P lookupPredicateType(ImmutableStringReader paramImmutableStringReader, Identifier paramIdentifier) throws CommandSyntaxException;
  
  Stream<Identifier> listPredicateTypes();
  
  T createPredicateTest(ImmutableStringReader paramImmutableStringReader, P paramP, Dynamic<?> paramDynamic) throws CommandSyntaxException;
  
  T negate(T paramT);
  
  T anyOf(List<T> paramList);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\item\ComponentPredicateParser$Context.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */