/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.Passthrough;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier;
/*    */ 
/*    */ public class ProcessorRule {
/* 16 */   public static final Passthrough DEFAULT_BLOCK_ENTITY_MODIFIER = Passthrough.INSTANCE;
/*    */   
/*    */   public static final Codec<ProcessorRule> CODEC;
/*    */   
/*    */   private final RuleTest inputPredicate;
/*    */   
/*    */   private final RuleTest locPredicate;
/*    */   
/*    */   private final PosRuleTest posPredicate;
/*    */   
/*    */   private final BlockState outputState;
/*    */   
/*    */   private final RuleBlockEntityModifier blockEntityModifier;
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RuleTest.CODEC.fieldOf("input_predicate").forGetter(()), (App)RuleTest.CODEC.fieldOf("location_predicate").forGetter(()), (App)PosRuleTest.CODEC.lenientOptionalFieldOf("position_predicate", PosAlwaysTrueTest.INSTANCE).forGetter(()), (App)BlockState.CODEC.fieldOf("output_state").forGetter(()), (App)RuleBlockEntityModifier.CODEC.lenientOptionalFieldOf("block_entity_modifier", DEFAULT_BLOCK_ENTITY_MODIFIER).forGetter(())).apply((Applicative)i, ProcessorRule::new));
/*    */   }
/*    */   
/*    */   public ProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, BlockState outputState) {
/* 36 */     this(inputPredicate, locPredicate, PosAlwaysTrueTest.INSTANCE, outputState);
/*    */   }
/*    */   
/*    */   public ProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, PosRuleTest posPredicate, BlockState outputState) {
/* 40 */     this(inputPredicate, locPredicate, posPredicate, outputState, (RuleBlockEntityModifier)DEFAULT_BLOCK_ENTITY_MODIFIER);
/*    */   }
/*    */   
/*    */   public ProcessorRule(RuleTest inputPredicate, RuleTest locPredicate, PosRuleTest posPredicate, BlockState outputState, RuleBlockEntityModifier blockEntityModifier) {
/* 44 */     this.inputPredicate = inputPredicate;
/* 45 */     this.locPredicate = locPredicate;
/* 46 */     this.posPredicate = posPredicate;
/* 47 */     this.outputState = outputState;
/* 48 */     this.blockEntityModifier = blockEntityModifier;
/*    */   }
/*    */   
/*    */   public boolean test(LevelReader level, BlockState inputState, BlockPos inTemplatePos, BlockPos worldPos, BlockPos reference, RandomSource random) {
/* 52 */     return (this.inputPredicate.test(inputState, random) && this.locPredicate.testAgainstWorldState(level, worldPos, random) && this.posPredicate.test(inTemplatePos, worldPos, reference, random));
/*    */   }
/*    */   
/*    */   public BlockState getOutputState() {
/* 56 */     return this.outputState;
/*    */   }
/*    */   
/*    */   public CompoundTag getOutputTag(RandomSource random, CompoundTag existingTag) {
/* 60 */     return this.blockEntityModifier.apply(random, existingTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\ProcessorRule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */