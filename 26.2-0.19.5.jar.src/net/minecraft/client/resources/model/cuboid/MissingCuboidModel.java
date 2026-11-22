/*    */ package net.minecraft.client.resources.model.cuboid;
/*    */ 
/*    */ import com.mojang.math.Quadrant;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*    */ import net.minecraft.client.resources.model.UnbakedModel;
/*    */ import net.minecraft.client.resources.model.sprite.Material;
/*    */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Util;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class MissingCuboidModel {
/*    */   private static final String TEXTURE_SLOT = "missingno";
/*    */   
/* 19 */   public static final Identifier LOCATION = Identifier.withDefaultNamespace("builtin/missing");
/*    */   
/*    */   public static UnbakedModel missingModel() {
/* 22 */     CuboidFace.UVs fullFaceUv = new CuboidFace.UVs(0.0F, 0.0F, 16.0F, 16.0F);
/* 24 */     Map<Direction, CuboidFace> faces = Util.makeEnumMap(Direction.class, direction -> new CuboidFace(direction, -1, "missingno", fullFaceUv, Quadrant.R0));
/* 28 */     CuboidModelElement cube = new CuboidModelElement((Vector3fc)new Vector3f(0.0F, 0.0F, 0.0F), (Vector3fc)new Vector3f(16.0F, 16.0F, 16.0F), faces);
/* 34 */     return new CuboidModel(new UnbakedCuboidGeometry(
/* 35 */           List.of(cube)), null, null, ItemTransforms.NO_TRANSFORMS, (new TextureSlots.Data.Builder())
/*    */         
/* 40 */         .addReference("particle", "missingno")
/* 41 */         .addTexture("missingno", new Material(MissingTextureAtlasSprite.getLocation()))
/* 42 */         .build(), null);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\MissingCuboidModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */