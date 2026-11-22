package net.minecraft.client;

import java.util.function.Function;

interface FieldAccess extends Options.OptionAccess {
  int process(String paramString, int paramInt);
  
  boolean process(String paramString, boolean paramBoolean);
  
  String process(String paramString1, String paramString2);
  
  float process(String paramString, float paramFloat);
  
  <T> T process(String paramString, T paramT, Function<String, T> paramFunction, Function<T, String> paramFunction1);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\Options$FieldAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */