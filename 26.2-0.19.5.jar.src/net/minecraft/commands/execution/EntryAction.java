package net.minecraft.commands.execution;

@FunctionalInterface
public interface EntryAction<T> {
  void execute(ExecutionContext<T> paramExecutionContext, Frame paramFrame);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\execution\EntryAction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */