package net.minecraft.server;

@FunctionalInterface
public interface WorldDataSupplier<D> {
  WorldLoader.DataLoadOutput<D> get(WorldLoader.DataLoadContext paramDataLoadContext);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\WorldLoader$WorldDataSupplier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */