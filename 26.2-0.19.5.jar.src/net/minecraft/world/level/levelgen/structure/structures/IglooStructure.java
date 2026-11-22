/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class IglooStructure extends Structure {
/* 16 */   public static final MapCodec<IglooStructure> CODEC = simpleCodec(IglooStructure::new);
/*    */   
/*    */   public IglooStructure(Structure.StructureSettings settings) {
/* 19 */     super(settings);
/*    */   }
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
/* 24 */     return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, builder -> generatePieces(builder, context));
/*    */   }
/*    */   
/*    */   private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
/* 28 */     ChunkPos chunkPos = context.chunkPos();
/* 29 */     WorldgenRandom random = context.random();
/* 31 */     BlockPos startPos = new BlockPos(chunkPos.getMinBlockX(), 90, chunkPos.getMinBlockZ());
/* 32 */     Rotation rotation = Rotation.getRandom((RandomSource)random);
/* 33 */     IglooPieces.addPieces(context.structureTemplateManager(), startPos, rotation, (StructurePieceAccessor)builder, (RandomSource)random);
/*    */   }
/*    */   
/*    */   public StructureType<?> type() {
/* 38 */     return StructureType.IGLOO;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\IglooStructure.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */