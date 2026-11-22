/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum CaveSurface implements StringRepresentable {
/*  8 */   CEILING(Direction.UP, 1, "ceiling"),
/*  9 */   FLOOR(Direction.DOWN, -1, "floor");
/*    */   
/*    */   public static final Codec<CaveSurface> CODEC;
/*    */   
/*    */   private final Direction direction;
/*    */   
/*    */   private final int y;
/*    */   
/*    */   private final String id;
/*    */   
/*    */   static {
/* 11 */     CODEC = (Codec<CaveSurface>)StringRepresentable.fromEnum(CaveSurface::values);
/*    */   }
/*    */   
/*    */   CaveSurface(Direction direction, int y, String id) {
/* 18 */     this.direction = direction;
/* 19 */     this.y = y;
/* 20 */     this.id = id;
/*    */   }
/*    */   
/*    */   public Direction getDirection() {
/* 24 */     return this.direction;
/*    */   }
/*    */   
/*    */   public int getY() {
/* 28 */     return this.y;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 33 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\CaveSurface.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */