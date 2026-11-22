package net.minecraft.util.profiling.metrics;

import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.util.profiling.ProfileCollector;

public interface MetricsSamplerProvider {
  Set<MetricSampler> samplers(Supplier<ProfileCollector> paramSupplier);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\metrics\MetricsSamplerProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */