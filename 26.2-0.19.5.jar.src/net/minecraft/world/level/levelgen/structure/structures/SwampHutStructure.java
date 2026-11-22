/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class SwampHutStructure extends Structure {
/* 12 */   public static final MapCodec<SwampHutStructure> CODEC = simpleCodec(SwampHutStructure::new);
/*    */   
/*    */   public SwampHutStructure(Structure.StructureSettings settings) {
/* 15 */     super(settings);
/*    */   }
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
/* 20 */     return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, builder -> generatePieces(builder, context));
/*    */   }
/*    */   
/*    */   private static void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
/* 24 */     builder.addPiece((StructurePiece)new SwampHutPiece((RandomSource)context.random(), context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ()));
/*    */   }
/*    */   
/*    */   public StructureType<?> type() {
/* 29 */     return StructureType.SWAMP_HUT;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\SwampHutStructure.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */