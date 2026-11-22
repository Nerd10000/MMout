/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.math.MatrixUtil;
/*    */ import com.mojang.math.Transformation;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.Util;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ import org.joml.Quaternionf;
/*    */ import org.joml.Quaternionfc;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public class BlockMath {
/* 16 */   private static final Map<Direction, Transformation> VANILLA_UV_TRANSFORM_LOCAL_TO_GLOBAL = Maps.newEnumMap(Map.of(Direction.SOUTH, Transformation.IDENTITY, Direction.EAST, new Transformation(null, (Quaternionfc)(new Quaternionf())
/*    */           
/* 18 */           .rotateY(1.5707964F), null, null), Direction.WEST, new Transformation(null, (Quaternionfc)(new Quaternionf())
/* 19 */           .rotateY(-1.5707964F), null, null), Direction.NORTH, new Transformation(null, (Quaternionfc)(new Quaternionf())
/* 20 */           .rotateY(3.1415927F), null, null), Direction.UP, new Transformation(null, (Quaternionfc)(new Quaternionf())
/* 21 */           .rotateX(-1.5707964F), null, null), Direction.DOWN, new Transformation(null, (Quaternionfc)(new Quaternionf())
/* 22 */           .rotateX(1.5707964F), null, null)));
/*    */   
/* 24 */   private static final Map<Direction, Transformation> VANILLA_UV_TRANSFORM_GLOBAL_TO_LOCAL = Maps.newEnumMap(Util.mapValues(VANILLA_UV_TRANSFORM_LOCAL_TO_GLOBAL, Transformation::inverse));
/*    */   
/*    */   public static Transformation blockCenterToCorner(Transformation transform) {
/* 28 */     Matrix4f ret = (new Matrix4f()).translation(0.5F, 0.5F, 0.5F);
/* 29 */     ret.mul(transform.getMatrix());
/* 30 */     ret.translate(-0.5F, -0.5F, -0.5F);
/* 31 */     return new Transformation((Matrix4fc)ret);
/*    */   }
/*    */   
/*    */   public static Transformation blockCornerToCenter(Transformation transform) {
/* 36 */     Matrix4f ret = (new Matrix4f()).translation(-0.5F, -0.5F, -0.5F);
/* 37 */     ret.mul(transform.getMatrix());
/* 38 */     ret.translate(0.5F, 0.5F, 0.5F);
/* 39 */     return new Transformation((Matrix4fc)ret);
/*    */   }
/*    */   
/*    */   public static Transformation getFaceTransformation(Transformation transformation, Direction originalSide) {
/* 46 */     if (MatrixUtil.isIdentity(transformation.getMatrix()))
/* 47 */       return transformation; 
/* 60 */     Transformation faceAction = VANILLA_UV_TRANSFORM_LOCAL_TO_GLOBAL.get(originalSide);
/* 63 */     faceAction = transformation.compose(faceAction);
/* 67 */     Vector3f transformedNormal = faceAction.getMatrix().transformDirection(new Vector3f(0.0F, 0.0F, 1.0F));
/* 68 */     Direction newSide = Direction.getApproximateNearest(transformedNormal.x, transformedNormal.y, transformedNormal.z);
/* 73 */     return ((Transformation)VANILLA_UV_TRANSFORM_GLOBAL_TO_LOCAL.get(newSide)).compose(faceAction);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\BlockMath.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */