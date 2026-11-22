/*    */ package net.minecraft.client.resources.model.cuboid;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.io.Reader;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.resources.model.UnbakedModel;
/*    */ import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
/*    */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public final class CuboidModel extends Record implements UnbakedModel {
/*    */   private final UnbakedGeometry geometry;
/*    */   
/*    */   private final UnbakedModel.GuiLight guiLight;
/*    */   
/*    */   private final Boolean ambientOcclusion;
/*    */   
/*    */   private final ItemTransforms transforms;
/*    */   
/*    */   private final TextureSlots.Data textureSlots;
/*    */   
/*    */   private final Identifier parent;
/*    */   
/*    */   public CuboidModel(UnbakedGeometry geometry, UnbakedModel.GuiLight guiLight, Boolean ambientOcclusion, ItemTransforms transforms, TextureSlots.Data textureSlots, Identifier parent) {
/* 24 */     this.geometry = geometry;
/* 24 */     this.guiLight = guiLight;
/* 24 */     this.ambientOcclusion = ambientOcclusion;
/* 24 */     this.transforms = transforms;
/* 24 */     this.textureSlots = textureSlots;
/* 24 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/cuboid/CuboidModel;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidModel;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/cuboid/CuboidModel;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidModel;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/cuboid/CuboidModel;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #24	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/cuboid/CuboidModel;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public UnbakedGeometry geometry() {
/* 24 */     return this.geometry;
/*    */   }
/*    */   
/*    */   public UnbakedModel.GuiLight guiLight() {
/* 24 */     return this.guiLight;
/*    */   }
/*    */   
/*    */   public Boolean ambientOcclusion() {
/* 24 */     return this.ambientOcclusion;
/*    */   }
/*    */   
/*    */   public ItemTransforms transforms() {
/* 24 */     return this.transforms;
/*    */   }
/*    */   
/*    */   public TextureSlots.Data textureSlots() {
/* 24 */     return this.textureSlots;
/*    */   }
/*    */   
/*    */   public Identifier parent() {
/* 24 */     return this.parent;
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/* 34 */   static final Gson GSON = (new GsonBuilder())
/* 35 */     .registerTypeAdapter(CuboidModel.class, new Deserializer())
/* 36 */     .registerTypeAdapter(CuboidModelElement.class, new CuboidModelElement.Deserializer())
/* 37 */     .registerTypeAdapter(CuboidFace.class, new CuboidFace.Deserializer())
/* 38 */     .registerTypeAdapter(ItemTransform.class, new ItemTransform.Deserializer())
/* 39 */     .registerTypeAdapter(ItemTransforms.class, new ItemTransforms.Deserializer())
/* 40 */     .create();
/*    */   
/*    */   public static CuboidModel fromStream(Reader reader) {
/* 43 */     return (CuboidModel)GsonHelper.fromJson(GSON, reader, CuboidModel.class);
/*    */   }
/*    */   
/*    */   public static class Deserializer implements JsonDeserializer<CuboidModel> {
/*    */     public CuboidModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 49 */       JsonObject object = json.getAsJsonObject();
/* 51 */       UnbakedGeometry elements = getElements(context, object);
/* 52 */       String parentName = getParentName(object);
/* 54 */       TextureSlots.Data textureMap = getTextureMap(object);
/* 55 */       Boolean hasAmbientOcclusion = getAmbientOcclusion(object);
/* 57 */       ItemTransforms transforms = null;
/* 58 */       if (object.has("display")) {
/* 59 */         JsonObject display = GsonHelper.getAsJsonObject(object, "display");
/* 60 */         transforms = (ItemTransforms)context.deserialize((JsonElement)display, ItemTransforms.class);
/*    */       } 
/* 63 */       UnbakedModel.GuiLight guiLight = null;
/* 64 */       if (object.has("gui_light"))
/* 65 */         guiLight = UnbakedModel.GuiLight.getByName(GsonHelper.getAsString(object, "gui_light")); 
/* 68 */       Identifier parentLocation = parentName.isEmpty() ? null : Identifier.parse(parentName);
/* 69 */       return new CuboidModel(elements, guiLight, hasAmbientOcclusion, transforms, textureMap, parentLocation);
/*    */     }
/*    */     
/*    */     private TextureSlots.Data getTextureMap(JsonObject object) {
/* 73 */       if (object.has("textures")) {
/* 74 */         JsonObject texturesObject = GsonHelper.getAsJsonObject(object, "textures");
/* 75 */         return TextureSlots.parseTextureMap(texturesObject);
/*    */       } 
/* 77 */       return TextureSlots.Data.EMPTY;
/*    */     }
/*    */     
/*    */     private String getParentName(JsonObject object) {
/* 81 */       return GsonHelper.getAsString(object, "parent", "");
/*    */     }
/*    */     
/*    */     protected Boolean getAmbientOcclusion(JsonObject object) {
/* 85 */       if (object.has("ambientocclusion"))
/* 86 */         return Boolean.valueOf(GsonHelper.getAsBoolean(object, "ambientocclusion")); 
/* 88 */       return null;
/*    */     }
/*    */     
/*    */     protected UnbakedGeometry getElements(JsonDeserializationContext context, JsonObject object) {
/* 92 */       if (object.has("elements")) {
/* 93 */         List<CuboidModelElement> elements = new ArrayList<>();
/* 94 */         for (JsonElement element : GsonHelper.getAsJsonArray(object, "elements"))
/* 95 */           elements.add((CuboidModelElement)context.deserialize(element, CuboidModelElement.class)); 
/* 97 */         return new UnbakedCuboidGeometry(elements);
/*    */       } 
/* 99 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\CuboidModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */