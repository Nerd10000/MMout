package net.minecraft.client.gui.screens;

@FunctionalInterface
public interface ChatConstructor<T extends ChatScreen> {
  T create(String paramString, boolean paramBoolean);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\ChatScreen$ChatConstructor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */