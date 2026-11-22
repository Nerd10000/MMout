package net.minecraft.util.profiling.metrics.profiling;

import net.minecraft.util.profiling.ProfilerFiller;

public interface MetricsRecorder {
  void end();
  
  void cancel();
  
  void startTick();
  
  void sampleDuringExtract();
  
  boolean isRecording();
  
  ProfilerFiller getProfiler();
  
  void endTick();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\metrics\profiling\MetricsRecorder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */