package net.minecraft.client.multiplayer;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementTree;

public interface Listener extends AdvancementTree.Listener {
  void onUpdateAdvancementProgress(AdvancementNode paramAdvancementNode, AdvancementProgress paramAdvancementProgress);
  
  void onSelectedTabChanged(AdvancementHolder paramAdvancementHolder);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientAdvancements$Listener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */