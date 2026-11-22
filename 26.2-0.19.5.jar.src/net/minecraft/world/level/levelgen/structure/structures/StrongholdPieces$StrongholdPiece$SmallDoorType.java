/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public enum SmallDoorType {
/* 230 */   OPENING, WOOD_DOOR, GRATES, IRON_DOOR;
/*     */   
/*     */   @Deprecated
/*     */   public static final Codec<SmallDoorType> LEGACY_CODEC;
/*     */   
/*     */   static {
/* 234 */     LEGACY_CODEC = ExtraCodecs.legacyEnum(SmallDoorType::valueOf);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$StrongholdPiece$SmallDoorType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */