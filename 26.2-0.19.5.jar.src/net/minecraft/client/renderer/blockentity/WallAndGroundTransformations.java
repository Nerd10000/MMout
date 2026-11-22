/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class WallAndGroundTransformations<T> {
/*    */   private final Map<Direction, T> wallTransforms;
/*    */   
/*    */   private final T[] freeTransformations;
/*    */   
/*    */   public WallAndGroundTransformations(Function<Direction, T> wallTransformationFactory, IntFunction<T> freeTransformationFactory, int segments) {
/* 16 */     this.wallTransforms = Util.makeEnumMap(Direction.class, wallTransformationFactory);
/* 19 */     this.freeTransformations = (T[])new Object[segments];
/* 20 */     for (int segment = 0; segment < segments; segment++)
/* 21 */       this.freeTransformations[segment] = freeTransformationFactory.apply(segment); 
/*    */   }
/*    */   
/*    */   public T wallTransformation(Direction facing) {
/* 26 */     return this.wallTransforms.get(facing);
/*    */   }
/*    */   
/*    */   public T freeTransformations(int segment) {
/* 30 */     return this.freeTransformations[segment];
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\WallAndGroundTransformations.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */