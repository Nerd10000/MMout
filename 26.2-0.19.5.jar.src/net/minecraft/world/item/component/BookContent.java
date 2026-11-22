package net.minecraft.world.item.component;

import java.util.List;
import net.minecraft.server.network.Filterable;

public interface BookContent<T, C> {
  List<Filterable<T>> pages();
  
  C withReplacedPages(List<Filterable<T>> paramList);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\BookContent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */