/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class TwistingVinesConfig extends Record implements FeatureConfiguration {
/*    */   private final int spreadWidth;
/*    */   
/*    */   private final int spreadHeight;
/*    */   
/*    */   private final int maxHeight;
/*    */   
/*    */   public static final Codec<TwistingVinesConfig> CODEC;
/*    */   
/*    */   public TwistingVinesConfig(int spreadWidth, int spreadHeight, int maxHeight) {
/*  7 */     this.spreadWidth = spreadWidth;
/*  7 */     this.spreadHeight = spreadHeight;
/*  7 */     this.maxHeight = maxHeight;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/TwistingVinesConfig;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/TwistingVinesConfig;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/TwistingVinesConfig;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/TwistingVinesConfig;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/TwistingVinesConfig;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/TwistingVinesConfig;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int spreadWidth() {
/*  7 */     return this.spreadWidth;
/*    */   }
/*    */   
/*    */   public int spreadHeight() {
/*  7 */     return this.spreadHeight;
/*    */   }
/*    */   
/*    */   public int maxHeight() {
/*  7 */     return this.maxHeight;
/*    */   }
/*    */   
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.POSITIVE_INT.fieldOf("spread_width").forGetter(TwistingVinesConfig::spreadWidth), (App)ExtraCodecs.POSITIVE_INT.fieldOf("spread_height").forGetter(TwistingVinesConfig::spreadHeight), (App)ExtraCodecs.POSITIVE_INT.fieldOf("max_height").forGetter(TwistingVinesConfig::maxHeight)).apply((Applicative)i, TwistingVinesConfig::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\TwistingVinesConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */