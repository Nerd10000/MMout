/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class EndGatewayConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<EndGatewayConfiguration> CODEC;
/*    */   
/*    */   private final Optional<BlockPos> exit;
/*    */   
/*    */   private final boolean exact;
/*    */   
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockPos.CODEC.optionalFieldOf("exit").forGetter(()), (App)Codec.BOOL.fieldOf("exact").forGetter(())).apply((Applicative)i, EndGatewayConfiguration::new));
/*    */   }
/*    */   
/*    */   private EndGatewayConfiguration(Optional<BlockPos> exit, boolean exact) {
/* 19 */     this.exit = exit;
/* 20 */     this.exact = exact;
/*    */   }
/*    */   
/*    */   public static EndGatewayConfiguration knownExit(BlockPos exit, boolean exact) {
/* 24 */     return new EndGatewayConfiguration(Optional.of(exit), exact);
/*    */   }
/*    */   
/*    */   public static EndGatewayConfiguration delayedExitSearch() {
/* 28 */     return new EndGatewayConfiguration(Optional.empty(), false);
/*    */   }
/*    */   
/*    */   public Optional<BlockPos> getExit() {
/* 32 */     return this.exit;
/*    */   }
/*    */   
/*    */   public boolean isExitExact() {
/* 36 */     return this.exact;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\EndGatewayConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */