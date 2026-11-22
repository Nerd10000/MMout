/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
/*    */ 
/*    */ public class GeodeCrackSettings {
/*    */   public static final Codec<GeodeCrackSettings> CODEC;
/*    */   
/*    */   public final double generateCrackChance;
/*    */   
/*    */   public final double baseCrackSize;
/*    */   
/*    */   public final int crackPointOffset;
/*    */   
/*    */   static {
/*  8 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)GeodeConfiguration.CHANCE_RANGE.optionalFieldOf("generate_crack_chance", Double.valueOf(1.0D)).forGetter(()), (App)Codec.doubleRange(0.0D, 5.0D).optionalFieldOf("base_crack_size", Double.valueOf(2.0D)).forGetter(()), (App)Codec.intRange(0, 10).optionalFieldOf("crack_point_offset", Integer.valueOf(2)).forGetter(())).apply((Applicative)i, GeodeCrackSettings::new));
/*    */   }
/*    */   
/*    */   public GeodeCrackSettings(double generateCrackChance, double baseCrackSize, int crackPointOffset) {
/* 19 */     this.generateCrackChance = generateCrackChance;
/* 20 */     this.baseCrackSize = baseCrackSize;
/* 21 */     this.crackPointOffset = crackPointOffset;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\GeodeCrackSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */