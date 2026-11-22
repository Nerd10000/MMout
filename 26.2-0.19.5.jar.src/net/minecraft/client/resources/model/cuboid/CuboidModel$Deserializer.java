/*    */ package net.minecraft.client.resources.model.cuboid;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.resources.model.UnbakedModel;
/*    */ import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
/*    */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public class Deserializer implements JsonDeserializer<CuboidModel> {
/*    */   public CuboidModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 49 */     JsonObject object = json.getAsJsonObject();
/* 51 */     UnbakedGeometry elements = getElements(context, object);
/* 52 */     String parentName = getParentName(object);
/* 54 */     TextureSlots.Data textureMap = getTextureMap(object);
/* 55 */     Boolean hasAmbientOcclusion = getAmbientOcclusion(object);
/* 57 */     ItemTransforms transforms = null;
/* 58 */     if (object.has("display")) {
/* 59 */       JsonObject display = GsonHelper.getAsJsonObject(object, "display");
/* 60 */       transforms = (ItemTransforms)context.deserialize((JsonElement)display, ItemTransforms.class);
/*    */     } 
/* 63 */     UnbakedModel.GuiLight guiLight = null;
/* 64 */     if (object.has("gui_light"))
/* 65 */       guiLight = UnbakedModel.GuiLight.getByName(GsonHelper.getAsString(object, "gui_light")); 
/* 68 */     Identifier parentLocation = parentName.isEmpty() ? null : Identifier.parse(parentName);
/* 69 */     return new CuboidModel(elements, guiLight, hasAmbientOcclusion, transforms, textureMap, parentLocation);
/*    */   }
/*    */   
/*    */   private TextureSlots.Data getTextureMap(JsonObject object) {
/* 73 */     if (object.has("textures")) {
/* 74 */       JsonObject texturesObject = GsonHelper.getAsJsonObject(object, "textures");
/* 75 */       return TextureSlots.parseTextureMap(texturesObject);
/*    */     } 
/* 77 */     return TextureSlots.Data.EMPTY;
/*    */   }
/*    */   
/*    */   private String getParentName(JsonObject object) {
/* 81 */     return GsonHelper.getAsString(object, "parent", "");
/*    */   }
/*    */   
/*    */   protected Boolean getAmbientOcclusion(JsonObject object) {
/* 85 */     if (object.has("ambientocclusion"))
/* 86 */       return Boolean.valueOf(GsonHelper.getAsBoolean(object, "ambientocclusion")); 
/* 88 */     return null;
/*    */   }
/*    */   
/*    */   protected UnbakedGeometry getElements(JsonDeserializationContext context, JsonObject object) {
/* 92 */     if (object.has("elements")) {
/* 93 */       List<CuboidModelElement> elements = new ArrayList<>();
/* 94 */       for (JsonElement element : GsonHelper.getAsJsonArray(object, "elements"))
/* 95 */         elements.add((CuboidModelElement)context.deserialize(element, CuboidModelElement.class)); 
/* 97 */       return new UnbakedCuboidGeometry(elements);
/*    */     } 
/* 99 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\CuboidModel$Deserializer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */