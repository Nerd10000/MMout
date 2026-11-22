/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public final class DiskConfiguration extends Record implements FeatureConfiguration {
/*    */   private final BlockStateProvider stateProvider;
/*    */   
/*    */   private final BlockPredicate target;
/*    */   
/*    */   private final IntProvider radius;
/*    */   
/*    */   private final int halfHeight;
/*    */   
/*    */   public static final Codec<DiskConfiguration> CODEC;
/*    */   
/*    */   public DiskConfiguration(BlockStateProvider stateProvider, BlockPredicate target, IntProvider radius, int halfHeight) {
/* 10 */     this.stateProvider = stateProvider;
/* 10 */     this.target = target;
/* 10 */     this.radius = radius;
/* 10 */     this.halfHeight = halfHeight;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockStateProvider stateProvider() {
/* 10 */     return this.stateProvider;
/*    */   }
/*    */   
/*    */   public BlockPredicate target() {
/* 10 */     return this.target;
/*    */   }
/*    */   
/*    */   public IntProvider radius() {
/* 10 */     return this.radius;
/*    */   }
/*    */   
/*    */   public int halfHeight() {
/* 10 */     return this.halfHeight;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(DiskConfiguration::stateProvider), (App)BlockPredicate.CODEC.fieldOf("target").forGetter(DiskConfiguration::target), (App)IntProviders.codec(0, 8).fieldOf("radius").forGetter(DiskConfiguration::radius), (App)Codec.intRange(0, 4).fieldOf("half_height").forGetter(DiskConfiguration::halfHeight)).apply((Applicative)i, DiskConfiguration::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\DiskConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */