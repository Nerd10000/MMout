/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class StrongholdStructure extends Structure {
/* 13 */   public static final MapCodec<StrongholdStructure> CODEC = simpleCodec(StrongholdStructure::new);
/*    */   
/*    */   public StrongholdStructure(Structure.StructureSettings settings) {
/* 16 */     super(settings);
/*    */   }
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
/* 21 */     return Optional.of(new Structure.GenerationStub(context.chunkPos().getWorldPosition(), builder -> generatePieces(builder, context)));
/*    */   }
/*    */   
/*    */   private static void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
/*    */     StrongholdPieces.StartPiece startRoom;
/* 25 */     int tries = 0;
/*    */     do {
/* 31 */       builder.clear();
/* 32 */       context.random().setLargeFeatureSeed(context.seed() + tries++, context.chunkPos().x(), context.chunkPos().z());
/* 33 */       StrongholdPieces.resetPieces();
/* 35 */       startRoom = new StrongholdPieces.StartPiece((RandomSource)context.random(), context.chunkPos().getBlockX(2), context.chunkPos().getBlockZ(2));
/* 36 */       builder.addPiece(startRoom);
/* 37 */       startRoom.addChildren(startRoom, (StructurePieceAccessor)builder, (RandomSource)context.random());
/* 39 */       List<StructurePiece> pendingChildren = startRoom.pendingChildren;
/* 40 */       while (!pendingChildren.isEmpty()) {
/* 41 */         int pos = context.random().nextInt(pendingChildren.size());
/* 42 */         StructurePiece structurePiece = pendingChildren.remove(pos);
/* 43 */         structurePiece.addChildren(startRoom, (StructurePieceAccessor)builder, (RandomSource)context.random());
/*    */       } 
/* 46 */       builder.moveBelowSeaLevel(context.chunkGenerator().getSeaLevel(), context.chunkGenerator().getMinY(), (RandomSource)context.random(), 10);
/* 47 */     } while (builder.isEmpty() || startRoom.portalRoomPiece == null);
/*    */   }
/*    */   
/*    */   public StructureType<?> type() {
/* 52 */     return StructureType.STRONGHOLD;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdStructure.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */