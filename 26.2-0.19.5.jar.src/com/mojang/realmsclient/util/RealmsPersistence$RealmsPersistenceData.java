package com.mojang.realmsclient.util;

import com.google.gson.annotations.SerializedName;
import com.mojang.realmsclient.dto.ReflectionBasedSerialization;

public class RealmsPersistenceData implements ReflectionBasedSerialization {
  @SerializedName("newsLink")
  public String newsLink;
  
  @SerializedName("hasUnreadNews")
  public boolean hasUnreadNews;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclien\\util\RealmsPersistence$RealmsPersistenceData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */