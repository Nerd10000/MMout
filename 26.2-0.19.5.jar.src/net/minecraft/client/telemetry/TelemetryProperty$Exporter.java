package net.minecraft.client.telemetry;

import com.mojang.authlib.minecraft.TelemetryPropertyContainer;

public interface Exporter<T> {
  void apply(TelemetryPropertyContainer paramTelemetryPropertyContainer, String paramString, T paramT);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\telemetry\TelemetryProperty$Exporter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */