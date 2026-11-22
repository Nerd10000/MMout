package com.mojang.realmsclient.dto;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.mojang.util.UUIDTypeAdapter;
import java.util.UUID;

public class OutboundPlayer implements ReflectionBasedSerialization {
  @SerializedName("name")
  public String name;
  
  @SerializedName("uuid")
  @JsonAdapter(UUIDTypeAdapter.class)
  public UUID uuid;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\OutboundPlayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */