/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
/*    */ 
/*    */ public class TargetBlockState {
/*    */   public static final Codec<TargetBlockState> CODEC;
/*    */   
/*    */   public final RuleTest target;
/*    */   
/*    */   public final BlockState state;
/*    */   
/*    */   static {
/* 45 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RuleTest.CODEC.fieldOf("target").forGetter(()), (App)BlockState.CODEC.fieldOf("state").forGetter(())).apply((Applicative)i, TargetBlockState::new));
/*    */   }
/*    */   
/*    */   private TargetBlockState(RuleTest target, BlockState state) {
/* 54 */     this.target = target;
/* 55 */     this.state = state;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\OreConfiguration$TargetBlockState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */