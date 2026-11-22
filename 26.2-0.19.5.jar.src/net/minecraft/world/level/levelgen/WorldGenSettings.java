/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.HashMap;
/*    */ import java.util.Objects;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.dimension.LevelStem;
/*    */ import net.minecraft.world.level.saveddata.SavedData;
/*    */ import net.minecraft.world.level.saveddata.SavedDataType;
/*    */ 
/*    */ public final class WorldGenSettings extends SavedData {
/*    */   public static final Codec<WorldGenSettings> CODEC;
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)WorldOptions.CODEC.forGetter(WorldGenSettings::options), (App)WorldDimensions.CODEC.forGetter(WorldGenSettings::dimensions)).apply((Applicative)i, i.stable(WorldGenSettings::new)));
/*    */   }
/*    */   
/* 20 */   public static final SavedDataType<WorldGenSettings> TYPE = new SavedDataType(Identifier.withDefaultNamespace("world_gen_settings"), () -> new WorldGenSettings(WorldOptions.defaultWithRandomSeed(), new WorldDimensions(new HashMap<>())), CODEC, DataFixTypes.SAVED_DATA_WORLD_GEN_SETTINGS);
/*    */   
/*    */   private final WorldOptions options;
/*    */   
/*    */   private final WorldDimensions dimensions;
/*    */   
/*    */   public WorldGenSettings(WorldOptions options, WorldDimensions dimensions) {
/* 25 */     this.options = options;
/* 26 */     this.dimensions = dimensions;
/*    */   }
/*    */   
/*    */   public static WorldGenSettings of(WorldOptions options, RegistryAccess registryAccess) {
/* 30 */     return new WorldGenSettings(options, new WorldDimensions(registryAccess.lookupOrThrow(Registries.LEVEL_STEM)));
/*    */   }
/*    */   
/*    */   public WorldOptions options() {
/* 34 */     return this.options;
/*    */   }
/*    */   
/*    */   public WorldDimensions dimensions() {
/* 38 */     return this.dimensions;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 43 */     return Objects.hash(new Object[] { this.options, this.dimensions });
/*    */   }
/*    */   
/*    */   public String toString() {
/* 48 */     return "WorldGenSettings[options=" + String.valueOf(this.options) + ", dimensions=" + String.valueOf(this.dimensions) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\WorldGenSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */