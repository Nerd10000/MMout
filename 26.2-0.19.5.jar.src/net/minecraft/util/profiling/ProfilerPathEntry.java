package net.minecraft.util.profiling;

import it.unimi.dsi.fastutil.objects.Object2LongMap;

public interface ProfilerPathEntry {
  long getDuration();
  
  long getMaxDuration();
  
  long getCount();
  
  Object2LongMap<String> getCounters();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\ProfilerPathEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */