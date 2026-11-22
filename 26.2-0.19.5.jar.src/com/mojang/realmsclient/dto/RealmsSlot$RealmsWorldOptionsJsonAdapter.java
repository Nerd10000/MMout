/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import java.io.IOException;
/*    */ 
/*    */ class RealmsWorldOptionsJsonAdapter extends TypeAdapter<RealmsWorldOptions> {
/*    */   public void write(JsonWriter jsonWriter, RealmsWorldOptions realmsSlotOptions) throws IOException {
/* 46 */     jsonWriter.jsonValue((new GuardedSerializer()).toJson(realmsSlotOptions));
/*    */   }
/*    */   
/*    */   public RealmsWorldOptions read(JsonReader jsonReader) throws IOException {
/* 51 */     String json = jsonReader.nextString();
/* 52 */     return RealmsWorldOptions.parse(new GuardedSerializer(), json);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RealmsSlot$RealmsWorldOptionsJsonAdapter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */