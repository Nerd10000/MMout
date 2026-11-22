/*    */ package net.minecraft.client.resources.model.cuboid;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.renderer.block.dispatch.ModelState;
/*    */ import net.minecraft.client.resources.model.ModelBaker;
/*    */ import net.minecraft.client.resources.model.ModelDebugName;
/*    */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*    */ import net.minecraft.client.resources.model.geometry.QuadCollection;
/*    */ import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
/*    */ import net.minecraft.client.resources.model.sprite.Material;
/*    */ import net.minecraft.client.resources.model.sprite.TextureSlots;
/*    */ import net.minecraft.core.Direction;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public final class UnbakedCuboidGeometry extends Record implements UnbakedGeometry {
/*    */   private final List<CuboidModelElement> elements;
/*    */   
/*    */   public UnbakedCuboidGeometry(List<CuboidModelElement> elements) {
/* 17 */     this.elements = elements;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/cuboid/UnbakedCuboidGeometry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/UnbakedCuboidGeometry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/cuboid/UnbakedCuboidGeometry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/model/cuboid/UnbakedCuboidGeometry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/cuboid/UnbakedCuboidGeometry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/model/cuboid/UnbakedCuboidGeometry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<CuboidModelElement> elements() {
/* 17 */     return this.elements;
/*    */   }
/*    */   
/*    */   public QuadCollection bake(TextureSlots textures, ModelBaker modelBaker, ModelState modelState, ModelDebugName name) {
/* 22 */     return bake(this.elements, textures, modelBaker, modelState, name);
/*    */   }
/*    */   
/*    */   public static QuadCollection bake(List<CuboidModelElement> elements, TextureSlots textures, ModelBaker modelBaker, ModelState modelState, ModelDebugName name) {
/* 26 */     QuadCollection.Builder builder = new QuadCollection.Builder();
/* 28 */     for (CuboidModelElement element : elements) {
/* 29 */       boolean drawXFaces = true;
/* 30 */       boolean drawYFaces = true;
/* 31 */       boolean drawZFaces = true;
/* 33 */       Vector3fc from = element.from();
/* 34 */       Vector3fc to = element.to();
/* 36 */       if (from.x() == to.x()) {
/* 37 */         drawYFaces = false;
/* 38 */         drawZFaces = false;
/*    */       } 
/* 41 */       if (from.y() == to.y()) {
/* 42 */         drawXFaces = false;
/* 43 */         drawZFaces = false;
/*    */       } 
/* 46 */       if (from.z() == to.z()) {
/* 47 */         drawXFaces = false;
/* 48 */         drawYFaces = false;
/*    */       } 
/* 51 */       if (drawXFaces || drawYFaces || drawZFaces)
/* 52 */         for (Map.Entry<Direction, CuboidFace> entry : element.faces().entrySet()) {
/* 53 */           Direction facing = entry.getKey();
/* 54 */           CuboidFace face = entry.getValue();
/* 56 */           switch (facing.getAxis()) {
/*    */             default:
/* 56 */               throw new MatchException(null, null);
/*    */             case X:
/*    */             
/*    */             case Y:
/*    */             
/*    */             case Z:
/*    */               break;
/*    */           } 
/* 56 */           boolean shouldDrawFace = 
/*    */             
/* 59 */             drawZFaces;
/* 62 */           if (shouldDrawFace) {
/* 63 */             Material.Baked material = modelBaker.materials().resolveSlot(textures, face.texture(), name);
/* 64 */             BakedQuad quad = FaceBakery.bakeQuad(modelBaker, from, to, face, material, facing, modelState, element.rotation(), element.shade(), element.lightEmission());
/* 65 */             if (face.cullForDirection() == null) {
/* 66 */               builder.addUnculledFace(quad);
/*    */               continue;
/*    */             } 
/* 68 */             builder.addCulledFace(Direction.rotate(modelState.transformation().getMatrix(), face.cullForDirection()), quad);
/*    */           } 
/*    */         }  
/*    */     } 
/* 75 */     return builder.build();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\cuboid\UnbakedCuboidGeometry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */