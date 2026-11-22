package net.minecraft.util.profiling;

import com.mojang.datafixers.util.Pair;
import java.util.Set;
import net.minecraft.util.profiling.metrics.MetricCategory;

public interface ProfileCollector extends ProfilerFiller {
  ProfileResults getResults();
  
  ActiveProfiler.PathEntry getEntry(String paramString);
  
  Set<Pair<String, MetricCategory>> getChartedPaths();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\ProfileCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */