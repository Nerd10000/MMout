/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum PositionSourceType implements StringRepresentable {
/*    */   ENTITY_POSITION, BOUNDING_BOX;
/*    */   
/*    */   public static final Codec<PositionSourceType> CODEC;
/*    */   
/*    */   private final String id;
/*    */   
/*    */   private final CoordinateSource source;
/*    */   
/*    */   static {
/* 38 */     ENTITY_POSITION = new PositionSourceType("ENTITY_POSITION", 0, "entity_position", (pos, center, bbSpan, random) -> pos);
/* 39 */     BOUNDING_BOX = new PositionSourceType("BOUNDING_BOX", 1, "in_bounding_box", (pos, center, bbSpan, random) -> center + (random.nextDouble() - 0.5D) * bbSpan);
/*    */   }
/*    */   
/*    */   static {
/* 42 */     CODEC = (Codec<PositionSourceType>)StringRepresentable.fromEnum(PositionSourceType::values);
/*    */   }
/*    */   
/*    */   PositionSourceType(String id, CoordinateSource source) {
/* 53 */     this.id = id;
/* 54 */     this.source = source;
/*    */   }
/*    */   
/*    */   public double getCoordinate(double position, double center, float boundingBoxSpan, RandomSource random) {
/* 58 */     return this.source.getCoordinate(position, center, boundingBoxSpan, random);
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 63 */     return this.id;
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   private static interface CoordinateSource {
/*    */     double getCoordinate(double param2Double1, double param2Double2, float param2Float, RandomSource param2RandomSource);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\SpawnParticlesEffect$PositionSourceType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */