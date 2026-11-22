/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public enum EntityAttachment {
/*  8 */   PASSENGER(Fallback.AT_HEIGHT),
/* 12 */   VEHICLE(Fallback.AT_FEET),
/* 13 */   NAME_TAG(Fallback.AT_HEIGHT),
/* 14 */   WARDEN_CHEST(Fallback.AT_CENTER);
/*    */   
/*    */   private final Fallback fallback;
/*    */   
/*    */   EntityAttachment(Fallback fallback) {
/* 20 */     this.fallback = fallback;
/*    */   }
/*    */   
/*    */   public List<Vec3> createFallbackPoints(float width, float height) {
/* 24 */     return this.fallback.create(width, height);
/*    */   }
/*    */   
/*    */   public static interface Fallback {
/* 28 */     public static final List<Vec3> ZERO = List.of(Vec3.ZERO);
/*    */     
/*    */     public static final Fallback AT_FEET = (width, height) -> ZERO;
/*    */     
/*    */     public static final Fallback AT_HEIGHT;
/*    */     
/*    */     public static final Fallback AT_CENTER;
/*    */     
/*    */     static {
/* 30 */       AT_HEIGHT = ((width, height) -> List.of(new Vec3(0.0D, height, 0.0D)));
/* 31 */       AT_CENTER = ((width, height) -> List.of(new Vec3(0.0D, height / 2.0D, 0.0D)));
/*    */     }
/*    */     
/*    */     List<Vec3> create(float param1Float1, float param1Float2);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\EntityAttachment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */