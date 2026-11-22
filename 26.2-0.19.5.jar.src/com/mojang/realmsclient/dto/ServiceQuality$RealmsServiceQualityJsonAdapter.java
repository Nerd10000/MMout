/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class RealmsServiceQualityJsonAdapter extends TypeAdapter<ServiceQuality> {
/* 46 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public void write(JsonWriter jsonWriter, ServiceQuality quality) throws IOException {
/* 50 */     jsonWriter.value(quality.value);
/*    */   }
/*    */   
/*    */   public ServiceQuality read(JsonReader jsonReader) throws IOException {
/* 55 */     int value = jsonReader.nextInt();
/* 56 */     ServiceQuality quality = ServiceQuality.byValue(value);
/* 57 */     if (quality == null) {
/* 58 */       LOGGER.warn("Unsupported ServiceQuality {}", Integer.valueOf(value));
/* 59 */       return ServiceQuality.UNKNOWN;
/*    */     } 
/* 61 */     return quality;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\ServiceQuality$RealmsServiceQualityJsonAdapter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */