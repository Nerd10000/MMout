/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ class null implements MultiNoiseBiomeSourceParameterList.Preset.SourceProvider {
/*    */   public <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> lookup) {
/* 75 */     return MultiNoiseBiomeSourceParameterList.Preset.generateOverworldBiomes(lookup);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\MultiNoiseBiomeSourceParameterList$Preset$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */