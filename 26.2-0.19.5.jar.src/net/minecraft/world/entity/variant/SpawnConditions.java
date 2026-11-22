/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class SpawnConditions {
/*    */   public static MapCodec<? extends SpawnCondition> bootstrap(Registry<MapCodec<? extends SpawnCondition>> registry) {
/*  9 */     Registry.register(registry, "structure", StructureCheck.MAP_CODEC);
/* 10 */     Registry.register(registry, "moon_brightness", MoonBrightnessCheck.MAP_CODEC);
/* 11 */     return (MapCodec<? extends SpawnCondition>)Registry.register(registry, "biome", BiomeCheck.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\variant\SpawnConditions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */