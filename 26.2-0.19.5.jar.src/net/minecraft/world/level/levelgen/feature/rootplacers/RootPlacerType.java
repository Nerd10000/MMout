/*    */ package net.minecraft.world.level.levelgen.feature.rootplacers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public class RootPlacerType<P extends RootPlacer> {
/*  9 */   public static final RootPlacerType<MangroveRootPlacer> MANGROVE_ROOT_PLACER = register("mangrove_root_placer", MangroveRootPlacer.CODEC);
/*    */   
/*    */   private final MapCodec<P> codec;
/*    */   
/*    */   private static <P extends RootPlacer> RootPlacerType<P> register(String name, MapCodec<P> codec) {
/* 12 */     return (RootPlacerType<P>)Registry.register(BuiltInRegistries.ROOT_PLACER_TYPE, name, new RootPlacerType<>(codec));
/*    */   }
/*    */   
/*    */   private RootPlacerType(MapCodec<P> codec) {
/* 18 */     this.codec = codec;
/*    */   }
/*    */   
/*    */   public MapCodec<P> codec() {
/* 22 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\rootplacers\RootPlacerType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */