/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.math.OctahedralGroup;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Mirror implements StringRepresentable {
/* 12 */   NONE("none", OctahedralGroup.IDENTITY),
/* 13 */   LEFT_RIGHT("left_right", OctahedralGroup.INVERT_Z),
/* 14 */   FRONT_BACK("front_back", OctahedralGroup.INVERT_X);
/*    */   
/*    */   public static final Codec<Mirror> CODEC;
/*    */   
/*    */   @Deprecated
/*    */   public static final Codec<Mirror> LEGACY_CODEC;
/*    */   
/*    */   private final String id;
/*    */   
/*    */   private final Component symbol;
/*    */   
/*    */   private final OctahedralGroup rotation;
/*    */   
/*    */   static {
/* 17 */     CODEC = (Codec<Mirror>)StringRepresentable.fromEnum(Mirror::values);
/* 20 */     LEGACY_CODEC = ExtraCodecs.legacyEnum(Mirror::valueOf);
/*    */   }
/*    */   
/*    */   Mirror(String id, OctahedralGroup rotation) {
/* 27 */     this.id = id;
/* 28 */     this.symbol = (Component)Component.translatable("mirror." + id);
/* 29 */     this.rotation = rotation;
/*    */   }
/*    */   
/*    */   public int mirror(int rotation, int steps) {
/* 33 */     int halfSteps = steps / 2;
/* 34 */     int correctedRotation = (rotation > halfSteps) ? (rotation - steps) : rotation;
/* 35 */     switch (ordinal()) {
/*    */       case 2:
/*    */       
/*    */       case 1:
/*    */       
/*    */     } 
/* 35 */     return 
/*    */       
/* 38 */       rotation;
/*    */   }
/*    */   
/*    */   public Rotation getRotation(Direction value) {
/* 43 */     Direction.Axis axis = value.getAxis();
/* 44 */     return ((this == LEFT_RIGHT && axis == Direction.Axis.Z) || (this == FRONT_BACK && axis == Direction.Axis.X)) ? Rotation.CLOCKWISE_180 : Rotation.NONE;
/*    */   }
/*    */   
/*    */   public Direction mirror(Direction direction) {
/* 48 */     if (this == FRONT_BACK && direction.getAxis() == Direction.Axis.X)
/* 49 */       return direction.getOpposite(); 
/* 51 */     if (this == LEFT_RIGHT && direction.getAxis() == Direction.Axis.Z)
/* 52 */       return direction.getOpposite(); 
/* 54 */     return direction;
/*    */   }
/*    */   
/*    */   public OctahedralGroup rotation() {
/* 58 */     return this.rotation;
/*    */   }
/*    */   
/*    */   public Component symbol() {
/* 62 */     return this.symbol;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 67 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\Mirror.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */