/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class StructureProcessorTypes {
/*    */   public static MapCodec<? extends StructureProcessor> bootstrap(Registry<MapCodec<? extends StructureProcessor>> registry) {
/*  9 */     Registry.register(registry, "blackstone_replace", BlackstoneReplaceProcessor.MAP_CODEC);
/* 10 */     Registry.register(registry, "block_age", BlockAgeProcessor.MAP_CODEC);
/* 11 */     Registry.register(registry, "block_ignore", BlockIgnoreProcessor.MAP_CODEC);
/* 12 */     Registry.register(registry, "block_rot", BlockRotProcessor.MAP_CODEC);
/* 13 */     Registry.register(registry, "capped", CappedProcessor.MAP_CODEC);
/* 14 */     Registry.register(registry, "gravity", GravityProcessor.MAP_CODEC);
/* 15 */     Registry.register(registry, "jigsaw_replacement", JigsawReplacementProcessor.MAP_CODEC);
/* 16 */     Registry.register(registry, "lava_submerged_block", LavaSubmergedBlockProcessor.MAP_CODEC);
/* 17 */     Registry.register(registry, "nop", NopProcessor.MAP_CODEC);
/* 18 */     Registry.register(registry, "protected_blocks", ProtectedBlockProcessor.MAP_CODEC);
/* 19 */     return (MapCodec<? extends StructureProcessor>)Registry.register(registry, "rule", RuleProcessor.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureProcessorTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */