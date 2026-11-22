/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ public class HugeFungusConfiguration implements FeatureConfiguration {
/*    */   public static final Codec<HugeFungusConfiguration> CODEC;
/*    */   
/*    */   public final BlockState validBaseState;
/*    */   
/*    */   public final BlockState stemState;
/*    */   
/*    */   public final BlockState hatState;
/*    */   
/*    */   public final BlockState decorState;
/*    */   
/*    */   public final BlockPredicate replaceableBlocks;
/*    */   
/*    */   public final boolean planted;
/*    */   
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockState.CODEC.fieldOf("valid_base_block").forGetter(()), (App)BlockState.CODEC.fieldOf("stem_state").forGetter(()), (App)BlockState.CODEC.fieldOf("hat_state").forGetter(()), (App)BlockState.CODEC.fieldOf("decor_state").forGetter(()), (App)BlockPredicate.CODEC.fieldOf("replaceable_blocks").forGetter(()), (App)Codec.BOOL.optionalFieldOf("planted", Boolean.valueOf(false)).forGetter(())).apply((Applicative)i, HugeFungusConfiguration::new));
/*    */   }
/*    */   
/*    */   public HugeFungusConfiguration(BlockState validBaseState, BlockState stemState, BlockState hatState, BlockState decorState, BlockPredicate replaceableBlocks, boolean planted) {
/* 28 */     this.validBaseState = validBaseState;
/* 29 */     this.stemState = stemState;
/* 30 */     this.hatState = hatState;
/* 31 */     this.decorState = decorState;
/* 32 */     this.replaceableBlocks = replaceableBlocks;
/* 33 */     this.planted = planted;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\HugeFungusConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */