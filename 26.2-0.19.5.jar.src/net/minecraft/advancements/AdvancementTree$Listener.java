package net.minecraft.advancements;

public interface Listener {
  void onAddAdvancementRoot(AdvancementNode paramAdvancementNode);
  
  void onRemoveAdvancementRoot(AdvancementNode paramAdvancementNode);
  
  void onAddAdvancementTask(AdvancementNode paramAdvancementNode);
  
  void onRemoveAdvancementTask(AdvancementNode paramAdvancementNode);
  
  void onAdvancementsCleared();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\AdvancementTree$Listener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */