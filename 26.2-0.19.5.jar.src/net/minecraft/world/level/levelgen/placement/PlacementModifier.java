/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public abstract class PlacementModifier {
/* 11 */   public static final Codec<PlacementModifier> CODEC = BuiltInRegistries.PLACEMENT_MODIFIER_TYPE.byNameCodec().dispatch(PlacementModifier::type, PlacementModifierType::codec);
/*    */   
/*    */   public abstract Stream<BlockPos> getPositions(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos);
/*    */   
/*    */   public abstract PlacementModifierType<?> type();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\placement\PlacementModifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */