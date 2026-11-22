package com.mojang.blaze3d.shaders;

import net.minecraft.resources.Identifier;

@FunctionalInterface
public interface ShaderSource {
  String get(Identifier paramIdentifier, ShaderType paramShaderType);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\shaders\ShaderSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */