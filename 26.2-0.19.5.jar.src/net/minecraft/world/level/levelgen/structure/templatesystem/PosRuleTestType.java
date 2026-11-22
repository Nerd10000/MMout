/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface PosRuleTestType<P extends PosRuleTest> {
/*  8 */   public static final PosRuleTestType<PosAlwaysTrueTest> ALWAYS_TRUE_TEST = register("always_true", PosAlwaysTrueTest.CODEC);
/*    */   
/*  9 */   public static final PosRuleTestType<LinearPosTest> LINEAR_POS_TEST = register("linear_pos", LinearPosTest.CODEC);
/*    */   
/* 10 */   public static final PosRuleTestType<AxisAlignedLinearPosTest> AXIS_ALIGNED_LINEAR_POS_TEST = register("axis_aligned_linear_pos", AxisAlignedLinearPosTest.CODEC);
/*    */   
/*    */   MapCodec<P> codec();
/*    */   
/*    */   static <P extends PosRuleTest> PosRuleTestType<P> register(String id, MapCodec<P> codec) {
/* 16 */     return (PosRuleTestType<P>)Registry.register(BuiltInRegistries.POS_RULE_TEST, id, () -> codec);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\PosRuleTestType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */