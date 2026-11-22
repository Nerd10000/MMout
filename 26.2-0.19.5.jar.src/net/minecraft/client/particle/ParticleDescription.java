/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.Streams;
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ public class ParticleDescription {
/*    */   private final List<Identifier> textures;
/*    */   
/*    */   private ParticleDescription(List<Identifier> textures) {
/* 16 */     this.textures = textures;
/*    */   }
/*    */   
/*    */   public List<Identifier> getTextures() {
/* 20 */     return this.textures;
/*    */   }
/*    */   
/*    */   public static ParticleDescription fromJson(JsonObject data) {
/* 24 */     JsonArray texturesData = GsonHelper.getAsJsonArray(data, "textures", null);
/* 25 */     if (texturesData == null)
/* 26 */       return new ParticleDescription(List.of()); 
/* 32 */     List<Identifier> textures = (List<Identifier>)Streams.stream((Iterable)texturesData).map(element -> GsonHelper.convertToString(element, "texture")).map(Identifier::parse).collect(ImmutableList.toImmutableList());
/* 34 */     return new ParticleDescription(textures);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\ParticleDescription.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */