/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.levelgen.structure.SinglePieceStructure;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ 
/*    */ public class JungleTempleStructure extends SinglePieceStructure {
/*  8 */   public static final MapCodec<JungleTempleStructure> CODEC = simpleCodec(JungleTempleStructure::new);
/*    */   
/*    */   public JungleTempleStructure(Structure.StructureSettings settings) {
/* 11 */     super(JungleTemplePiece::new, 12, 15, settings);
/*    */   }
/*    */   
/*    */   public StructureType<?> type() {
/* 16 */     return StructureType.JUNGLE_TEMPLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\JungleTempleStructure.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */