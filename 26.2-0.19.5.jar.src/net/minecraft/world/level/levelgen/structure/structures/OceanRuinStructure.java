/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.Structure;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*    */ 
/*    */ public class OceanRuinStructure extends Structure {
/*    */   public static final MapCodec<OceanRuinStructure> CODEC;
/*    */   
/*    */   public final Type biomeTemp;
/*    */   
/*    */   public final float largeProbability;
/*    */   
/*    */   public final float clusterProbability;
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)settingsCodec(i), (App)Type.CODEC.fieldOf("biome_temp").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("large_probability").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("cluster_probability").forGetter(())).apply((Applicative)i, OceanRuinStructure::new));
/*    */   }
/*    */   
/*    */   public OceanRuinStructure(Structure.StructureSettings settings, Type biomeTemp, float largeProbability, float clusterProbability) {
/* 30 */     super(settings);
/* 31 */     this.biomeTemp = biomeTemp;
/* 32 */     this.largeProbability = largeProbability;
/* 33 */     this.clusterProbability = clusterProbability;
/*    */   }
/*    */   
/*    */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
/* 38 */     return onTopOfChunkCenter(context, Heightmap.Types.OCEAN_FLOOR_WG, builder -> generatePieces(builder, context));
/*    */   }
/*    */   
/*    */   private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
/* 42 */     BlockPos offset = new BlockPos(context.chunkPos().getMinBlockX(), 90, context.chunkPos().getMinBlockZ());
/* 43 */     Rotation rotation = Rotation.getRandom((RandomSource)context.random());
/* 44 */     OceanRuinPieces.addPieces(context.structureTemplateManager(), offset, rotation, (StructurePieceAccessor)builder, (RandomSource)context.random(), this);
/*    */   }
/*    */   
/*    */   public StructureType<?> type() {
/* 49 */     return StructureType.OCEAN_RUIN;
/*    */   }
/*    */   
/*    */   public enum Type implements StringRepresentable {
/* 53 */     WARM("warm"),
/* 54 */     COLD("cold");
/*    */     
/* 57 */     public static final Codec<Type> CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*    */     
/*    */     @Deprecated
/* 60 */     public static final Codec<Type> LEGACY_CODEC = ExtraCodecs.legacyEnum(Type::valueOf);
/*    */     
/*    */     private final String name;
/*    */     
/*    */     static {
/*    */     
/*    */     }
/*    */     
/*    */     Type(String name) {
/* 65 */       this.name = name;
/*    */     }
/*    */     
/*    */     public String getName() {
/* 69 */       return this.name;
/*    */     }
/*    */     
/*    */     public String getSerializedName() {
/* 74 */       return this.name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanRuinStructure.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */