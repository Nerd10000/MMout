/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class NetherForestVegetationConfig extends BlockPileConfiguration {
/*    */   public static final Codec<NetherForestVegetationConfig> CODEC;
/*    */   
/*    */   public final int spreadWidth;
/*    */   
/*    */   public final int spreadHeight;
/*    */   
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(()), (App)ExtraCodecs.POSITIVE_INT.fieldOf("spread_width").forGetter(()), (App)ExtraCodecs.POSITIVE_INT.fieldOf("spread_height").forGetter(())).apply((Applicative)i, NetherForestVegetationConfig::new));
/*    */   }
/*    */   
/*    */   public NetherForestVegetationConfig(BlockStateProvider stateProvider, int spreadWidth, int spreadHeight) {
/* 19 */     super(stateProvider);
/* 20 */     this.spreadWidth = spreadWidth;
/* 21 */     this.spreadHeight = spreadHeight;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\NetherForestVegetationConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */