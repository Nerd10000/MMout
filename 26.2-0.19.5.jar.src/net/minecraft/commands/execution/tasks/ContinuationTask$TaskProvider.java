package net.minecraft.commands.execution.tasks;

import net.minecraft.commands.execution.CommandQueueEntry;
import net.minecraft.commands.execution.Frame;

@FunctionalInterface
public interface TaskProvider<T, P> {
  CommandQueueEntry<T> create(Frame paramFrame, P paramP);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\execution\tasks\ContinuationTask$TaskProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */