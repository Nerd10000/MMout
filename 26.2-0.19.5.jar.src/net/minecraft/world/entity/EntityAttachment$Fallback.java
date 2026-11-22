/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface Fallback {
/* 28 */   public static final List<Vec3> ZERO = List.of(Vec3.ZERO);
/*    */   
/*    */   public static final Fallback AT_FEET = (width, height) -> ZERO;
/*    */   
/*    */   public static final Fallback AT_HEIGHT;
/*    */   
/*    */   public static final Fallback AT_CENTER;
/*    */   
/*    */   static {
/* 30 */     AT_HEIGHT = ((width, height) -> List.of(new Vec3(0.0D, height, 0.0D)));
/* 31 */     AT_CENTER = ((width, height) -> List.of(new Vec3(0.0D, height / 2.0D, 0.0D)));
/*    */   }
/*    */   
/*    */   List<Vec3> create(float paramFloat1, float paramFloat2);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\EntityAttachment$Fallback.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */