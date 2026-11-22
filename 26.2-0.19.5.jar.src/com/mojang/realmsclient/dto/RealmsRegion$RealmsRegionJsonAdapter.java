/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class RealmsRegionJsonAdapter extends TypeAdapter<RealmsRegion> {
/* 56 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public void write(JsonWriter jsonWriter, RealmsRegion realmsRegion) throws IOException {
/* 60 */     jsonWriter.value(realmsRegion.nameId);
/*    */   }
/*    */   
/*    */   public RealmsRegion read(JsonReader jsonReader) throws IOException {
/* 65 */     String nameId = jsonReader.nextString();
/* 66 */     RealmsRegion realmsRegion = RealmsRegion.findByNameId(nameId);
/* 67 */     if (realmsRegion == null) {
/* 68 */       LOGGER.warn("Unsupported RealmsRegion {}", nameId);
/* 69 */       return RealmsRegion.INVALID_REGION;
/*    */     } 
/* 71 */     return realmsRegion;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RealmsRegion$RealmsRegionJsonAdapter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */