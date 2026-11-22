/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class RegionSelectionPreferenceJsonAdapter extends TypeAdapter<RegionSelectionPreference> {
/* 27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public void write(JsonWriter jsonWriter, RegionSelectionPreference regionSelectionPreference) throws IOException {
/* 31 */     jsonWriter.value(regionSelectionPreference.id);
/*    */   }
/*    */   
/*    */   public RegionSelectionPreference read(JsonReader jsonReader) throws IOException {
/* 36 */     int id = jsonReader.nextInt();
/* 37 */     for (RegionSelectionPreference value : RegionSelectionPreference.values()) {
/* 38 */       if (value.id == id)
/* 39 */         return value; 
/*    */     } 
/* 42 */     LOGGER.warn("Unsupported RegionSelectionPreference {}", Integer.valueOf(id));
/* 43 */     return RegionSelectionPreference.DEFAULT_SELECTION;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RegionSelectionPreference$RegionSelectionPreferenceJsonAdapter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */