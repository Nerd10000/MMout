/*      */ package net.minecraft.world.level.levelgen;
/*      */ 
/*      */ import net.minecraft.util.KeyDispatchDataCodec;
/*      */ import net.minecraft.util.StringRepresentable;
/*      */ 
/*      */ public enum Type implements StringRepresentable {
/* 1037 */   ADD("add"),
/* 1038 */   MUL("mul"),
/* 1039 */   MIN("min"),
/* 1040 */   MAX("max");
/*      */   
/*      */   private final KeyDispatchDataCodec<DensityFunctions.TwoArgumentSimpleFunction> codec;
/*      */   
/*      */   private final String name;
/*      */   
/*      */   Type(String name) {
/* 1043 */     this.codec = DensityFunctions.doubleFunctionArgumentCodec((argument1, argument2) -> DensityFunctions.TwoArgumentSimpleFunction.create(this, argument1, argument2), DensityFunctions.TwoArgumentSimpleFunction::argument1, DensityFunctions.TwoArgumentSimpleFunction::argument2);
/* 1052 */     this.name = name;
/*      */   }
/*      */   
/*      */   public String getSerializedName() {
/* 1057 */     return this.name;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$TwoArgumentSimpleFunction$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */