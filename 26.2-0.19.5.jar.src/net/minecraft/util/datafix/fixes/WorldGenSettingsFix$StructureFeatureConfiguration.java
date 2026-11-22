/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ final class StructureFeatureConfiguration {
/*    */   public static final Codec<StructureFeatureConfiguration> CODEC;
/*    */   
/*    */   private final int spacing;
/*    */   
/*    */   private final int separation;
/*    */   
/*    */   private final int salt;
/*    */   
/*    */   static {
/* 81 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("spacing").forGetter(()), (App)Codec.INT.fieldOf("separation").forGetter(()), (App)Codec.INT.fieldOf("salt").forGetter(())).apply((Applicative)i, StructureFeatureConfiguration::new));
/*    */   }
/*    */   
/*    */   public StructureFeatureConfiguration(int spacing, int separation, int salt) {
/* 92 */     this.spacing = spacing;
/* 93 */     this.separation = separation;
/* 94 */     this.salt = salt;
/*    */   }
/*    */   
/*    */   public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
/* 98 */     return new Dynamic(ops, CODEC.encodeStart(ops, this).result().orElse(ops.emptyMap()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\WorldGenSettingsFix$StructureFeatureConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */