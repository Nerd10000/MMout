/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalInt;
/*    */ 
/*    */ public class Builder {
/* 28 */   private OptionalInt waterColor = OptionalInt.empty();
/*    */   
/* 29 */   private Optional<Integer> foliageColorOverride = Optional.empty();
/*    */   
/* 30 */   private Optional<Integer> dryFoliageColorOverride = Optional.empty();
/*    */   
/* 31 */   private Optional<Integer> grassColorOverride = Optional.empty();
/*    */   
/* 32 */   private BiomeSpecialEffects.GrassColorModifier grassColorModifier = BiomeSpecialEffects.GrassColorModifier.NONE;
/*    */   
/*    */   public Builder waterColor(int waterColor) {
/* 35 */     this.waterColor = OptionalInt.of(waterColor);
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public Builder foliageColorOverride(int foliageColor) {
/* 40 */     this.foliageColorOverride = Optional.of(Integer.valueOf(foliageColor));
/* 41 */     return this;
/*    */   }
/*    */   
/*    */   public Builder dryFoliageColorOverride(int dryFoliageColor) {
/* 45 */     this.dryFoliageColorOverride = Optional.of(Integer.valueOf(dryFoliageColor));
/* 46 */     return this;
/*    */   }
/*    */   
/*    */   public Builder grassColorOverride(int grassColor) {
/* 50 */     this.grassColorOverride = Optional.of(Integer.valueOf(grassColor));
/* 51 */     return this;
/*    */   }
/*    */   
/*    */   public Builder grassColorModifier(BiomeSpecialEffects.GrassColorModifier grassModifier) {
/* 55 */     this.grassColorModifier = grassModifier;
/* 56 */     return this;
/*    */   }
/*    */   
/*    */   public BiomeSpecialEffects build() {
/* 60 */     return new BiomeSpecialEffects(this.waterColor
/* 61 */         .orElseThrow(() -> new IllegalStateException("Missing 'water' color.")), this.foliageColorOverride, this.dryFoliageColorOverride, this.grassColorOverride, this.grassColorModifier);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\BiomeSpecialEffects$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */