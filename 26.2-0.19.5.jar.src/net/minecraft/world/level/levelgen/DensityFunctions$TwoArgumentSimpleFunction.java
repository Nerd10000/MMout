/*      */ package net.minecraft.world.level.levelgen;
/*      */ 
/*      */ import com.mojang.logging.LogUtils;
/*      */ import net.minecraft.util.KeyDispatchDataCodec;
/*      */ import net.minecraft.util.StringRepresentable;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public interface TwoArgumentSimpleFunction extends DensityFunction {
/*  971 */   public static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   static TwoArgumentSimpleFunction create(Type type, DensityFunction argument1, DensityFunction argument2) {
/*  974 */     double min1 = argument1.minValue();
/*  975 */     double min2 = argument2.minValue();
/*  977 */     double max1 = argument1.maxValue();
/*  978 */     double max2 = argument2.maxValue();
/*  980 */     if (type == Type.MIN || type == Type.MAX) {
/*  981 */       boolean firstAlwaysBiggerThanSecond = (min1 >= max2);
/*  982 */       boolean secondAlwaysBiggerThanFirst = (min2 >= max1);
/*  983 */       if (firstAlwaysBiggerThanSecond || secondAlwaysBiggerThanFirst)
/*  984 */         LOGGER.warn("Creating a {} function between two non-overlapping inputs: {} and {}", new Object[] { type, argument1, argument2 }); 
/*      */     } 
/*  994 */     switch (type.ordinal()) {
/*      */       default:
/*  994 */         throw new MatchException(null, null);
/*      */       case 0:
/*      */       
/*      */       case 3:
/*      */       
/*      */       case 2:
/*      */       
/*      */       case 1:
/*  999 */         if (min1 > 0.0D && min2 > 0.0D);
/* 1002 */         if (max1 < 0.0D && max2 < 0.0D);
/*      */         break;
/*      */     } 
/* 1005 */     double minValue = Math.min(min1 * max2, max1 * min2);
/* 1009 */     switch (type.ordinal()) {
/*      */       default:
/* 1009 */         throw new MatchException(null, null);
/*      */       case 0:
/*      */       
/*      */       case 3:
/*      */       
/*      */       case 2:
/*      */       
/*      */       case 1:
/* 1014 */         if (min1 > 0.0D && min2 > 0.0D);
/* 1017 */         if (max1 < 0.0D && max2 < 0.0D);
/*      */         break;
/*      */     } 
/* 1020 */     double maxValue = Math.max(min1 * min2, max1 * max2);
/* 1024 */     if (type == Type.MUL || type == Type.ADD) {
/* 1025 */       if (argument1 instanceof DensityFunctions.Constant) {
/* 1025 */         DensityFunctions.Constant constant = (DensityFunctions.Constant)argument1;
/* 1026 */         return new DensityFunctions.MulOrAdd((type == Type.ADD) ? DensityFunctions.MulOrAdd.Type.ADD : DensityFunctions.MulOrAdd.Type.MUL, argument2, minValue, maxValue, constant.value);
/*      */       } 
/* 1028 */       if (argument2 instanceof DensityFunctions.Constant) {
/* 1028 */         DensityFunctions.Constant constant = (DensityFunctions.Constant)argument2;
/* 1029 */         return new DensityFunctions.MulOrAdd((type == Type.ADD) ? DensityFunctions.MulOrAdd.Type.ADD : DensityFunctions.MulOrAdd.Type.MUL, argument1, minValue, maxValue, constant.value);
/*      */       } 
/*      */     } 
/* 1033 */     return new DensityFunctions.Ap2(type, argument1, argument2, minValue, maxValue);
/*      */   }
/*      */   
/*      */   Type type();
/*      */   
/*      */   DensityFunction argument1();
/*      */   
/*      */   DensityFunction argument2();
/*      */   
/*      */   public enum Type implements StringRepresentable {
/* 1037 */     ADD("add"),
/* 1038 */     MUL("mul"),
/* 1039 */     MIN("min"),
/* 1040 */     MAX("max");
/*      */     
/*      */     private final KeyDispatchDataCodec<DensityFunctions.TwoArgumentSimpleFunction> codec;
/*      */     
/*      */     private final String name;
/*      */     
/*      */     Type(String name) {
/* 1043 */       this.codec = DensityFunctions.doubleFunctionArgumentCodec((argument1, argument2) -> DensityFunctions.TwoArgumentSimpleFunction.create(this, argument1, argument2), DensityFunctions.TwoArgumentSimpleFunction::argument1, DensityFunctions.TwoArgumentSimpleFunction::argument2);
/* 1052 */       this.name = name;
/*      */     }
/*      */     
/*      */     public String getSerializedName() {
/* 1057 */       return this.name;
/*      */     }
/*      */   }
/*      */   
/*      */   default KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 1069 */     return (KeyDispatchDataCodec)(type()).codec;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$TwoArgumentSimpleFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */