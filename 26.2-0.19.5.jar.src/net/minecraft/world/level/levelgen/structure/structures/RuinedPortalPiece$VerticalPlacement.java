/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum VerticalPlacement implements StringRepresentable {
/* 302 */   ON_LAND_SURFACE("on_land_surface"),
/* 303 */   PARTLY_BURIED("partly_buried"),
/* 304 */   ON_OCEAN_FLOOR("on_ocean_floor"),
/* 305 */   IN_MOUNTAIN("in_mountain"),
/* 306 */   UNDERGROUND("underground"),
/* 307 */   IN_NETHER("in_nether");
/*     */   
/*     */   public static final Codec<VerticalPlacement> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/* 310 */     CODEC = (Codec<VerticalPlacement>)StringRepresentable.fromEnum(VerticalPlacement::values);
/*     */   }
/*     */   
/*     */   VerticalPlacement(String name) {
/* 315 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 319 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 324 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\RuinedPortalPiece$VerticalPlacement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */