/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ 
/*     */ public final class NoiseSettings extends Record {
/*     */   private final int minY;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final int noiseSizeHorizontal;
/*     */   
/*     */   private final int noiseSizeVertical;
/*     */   
/*     */   public static final Codec<NoiseSettings> CODEC;
/*     */   
/*     */   public NoiseSettings(int minY, int height, int noiseSizeHorizontal, int noiseSizeVertical) {
/*  15 */     this.minY = minY;
/*  15 */     this.height = height;
/*  15 */     this.noiseSizeHorizontal = noiseSizeHorizontal;
/*  15 */     this.noiseSizeVertical = noiseSizeVertical;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/NoiseSettings;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/NoiseSettings;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/NoiseSettings;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/NoiseSettings;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/NoiseSettings;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/NoiseSettings;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int minY() {
/*  15 */     return this.minY;
/*     */   }
/*     */   
/*     */   public int height() {
/*  15 */     return this.height;
/*     */   }
/*     */   
/*     */   public int noiseSizeHorizontal() {
/*  15 */     return this.noiseSizeHorizontal;
/*     */   }
/*     */   
/*     */   public int noiseSizeVertical() {
/*  15 */     return this.noiseSizeVertical;
/*     */   }
/*     */   
/*     */   static {
/*  26 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y).fieldOf("min_y").forGetter(NoiseSettings::minY), (App)Codec.intRange(0, DimensionType.Y_SIZE).fieldOf("height").forGetter(NoiseSettings::height), (App)Codec.intRange(1, 4).fieldOf("size_horizontal").forGetter(NoiseSettings::noiseSizeHorizontal), (App)Codec.intRange(1, 4).fieldOf("size_vertical").forGetter(NoiseSettings::noiseSizeVertical)).apply((Applicative)i, NoiseSettings::new)).comapFlatMap(NoiseSettings::guardY, Function.identity());
/*     */   }
/*     */   
/*  29 */   static final NoiseSettings OVERWORLD_NOISE_SETTINGS = create(-64, 384, 1, 2);
/*     */   
/*  37 */   static final NoiseSettings NETHER_NOISE_SETTINGS = create(0, 128, 1, 2);
/*     */   
/*  45 */   static final NoiseSettings END_NOISE_SETTINGS = create(0, 128, 2, 1);
/*     */   
/*  53 */   static final NoiseSettings CAVES_NOISE_SETTINGS = create(-64, 192, 1, 2);
/*     */   
/*  61 */   static final NoiseSettings FLOATING_ISLANDS_NOISE_SETTINGS = create(0, 256, 2, 1);
/*     */   
/*     */   private static DataResult<NoiseSettings> guardY(NoiseSettings dimensionType) {
/*  69 */     if (dimensionType.minY() + dimensionType.height() > DimensionType.MAX_Y + 1)
/*  70 */       return DataResult.error(() -> "min_y + height cannot be higher than: " + DimensionType.MAX_Y + 1); 
/*  73 */     if (dimensionType.height() % 16 != 0)
/*  74 */       return DataResult.error(() -> "height has to be a multiple of 16"); 
/*  77 */     if (dimensionType.minY() % 16 != 0)
/*  78 */       return DataResult.error(() -> "min_y has to be a multiple of 16"); 
/*  81 */     return DataResult.success(dimensionType);
/*     */   }
/*     */   
/*     */   public static NoiseSettings create(int minY, int height, int noiseSizeHorizontal, int noiseSizeVertical) {
/*  85 */     NoiseSettings noiseSettings = new NoiseSettings(minY, height, noiseSizeHorizontal, noiseSizeVertical);
/*  87 */     guardY(noiseSettings).error().ifPresent(error -> {
/*     */           throw new IllegalStateException(error.message());
/*     */         });
/*  91 */     return noiseSettings;
/*     */   }
/*     */   
/*     */   public int getCellHeight() {
/*  95 */     return QuartPos.toBlock(noiseSizeVertical());
/*     */   }
/*     */   
/*     */   public int getCellWidth() {
/*  99 */     return QuartPos.toBlock(noiseSizeHorizontal());
/*     */   }
/*     */   
/*     */   public NoiseSettings clampToHeightAccessor(LevelHeightAccessor heightAccessor) {
/* 103 */     int newMinY = Math.max(this.minY, heightAccessor.getMinY());
/* 104 */     int newHeight = Math.min(this.minY + this.height, heightAccessor.getMaxY() + 1) - newMinY;
/* 105 */     return new NoiseSettings(newMinY, newHeight, this.noiseSizeHorizontal, this.noiseSizeVertical);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */