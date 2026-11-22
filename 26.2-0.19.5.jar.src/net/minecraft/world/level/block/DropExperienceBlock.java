/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class DropExperienceBlock extends Block {
/*    */   public static final MapCodec<DropExperienceBlock> CODEC;
/*    */   
/*    */   private final IntProvider xpRange;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)IntProviders.codec(0, 10).fieldOf("experience").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, DropExperienceBlock::new));
/*    */   }
/*    */   
/*    */   public MapCodec<? extends DropExperienceBlock> codec() {
/* 20 */     return CODEC;
/*    */   }
/*    */   
/*    */   public DropExperienceBlock(IntProvider xpRange, BlockBehaviour.Properties properties) {
/* 26 */     super(properties);
/* 27 */     this.xpRange = xpRange;
/*    */   }
/*    */   
/*    */   protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/* 32 */     super.spawnAfterBreak(state, level, pos, tool, dropExperience);
/* 33 */     if (dropExperience)
/* 34 */       tryDropExperience(level, pos, tool, this.xpRange); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\DropExperienceBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */