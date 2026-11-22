package net.minecraft.commands.arguments;

import net.minecraft.network.chat.MessageSignature;

@FunctionalInterface
public interface Signer {
  MessageSignature sign(String paramString);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ArgumentSignatures$Signer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */