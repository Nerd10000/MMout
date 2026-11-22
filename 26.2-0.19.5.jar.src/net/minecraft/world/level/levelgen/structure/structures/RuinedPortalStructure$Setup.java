/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function8;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class Setup extends Record {
/*    */   private final RuinedPortalPiece.VerticalPlacement placement;
/*    */   
/*    */   private final float airPocketProbability;
/*    */   
/*    */   private final float mossiness;
/*    */   
/*    */   private final boolean overgrown;
/*    */   
/*    */   private final boolean vines;
/*    */   
/*    */   private final boolean canBeCold;
/*    */   
/*    */   private final boolean replaceWithBlackstone;
/*    */   
/*    */   private final float weight;
/*    */   
/*    */   public static final Codec<Setup> CODEC;
/*    */   
/*    */   public Setup(RuinedPortalPiece.VerticalPlacement placement, float airPocketProbability, float mossiness, boolean overgrown, boolean vines, boolean canBeCold, boolean replaceWithBlackstone, float weight) {
/* 47 */     this.placement = placement;
/* 47 */     this.airPocketProbability = airPocketProbability;
/* 47 */     this.mossiness = mossiness;
/* 47 */     this.overgrown = overgrown;
/* 47 */     this.vines = vines;
/* 47 */     this.canBeCold = canBeCold;
/* 47 */     this.replaceWithBlackstone = replaceWithBlackstone;
/* 47 */     this.weight = weight;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalStructure$Setup;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public RuinedPortalPiece.VerticalPlacement placement() {
/* 47 */     return this.placement;
/*    */   }
/*    */   
/*    */   public float airPocketProbability() {
/* 47 */     return this.airPocketProbability;
/*    */   }
/*    */   
/*    */   public float mossiness() {
/* 47 */     return this.mossiness;
/*    */   }
/*    */   
/*    */   public boolean overgrown() {
/* 47 */     return this.overgrown;
/*    */   }
/*    */   
/*    */   public boolean vines() {
/* 47 */     return this.vines;
/*    */   }
/*    */   
/*    */   public boolean canBeCold() {
/* 47 */     return this.canBeCold;
/*    */   }
/*    */   
/*    */   public boolean replaceWithBlackstone() {
/* 47 */     return this.replaceWithBlackstone;
/*    */   }
/*    */   
/*    */   public float weight() {
/* 47 */     return this.weight;
/*    */   }
/*    */   
/*    */   static {
/* 57 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RuinedPortalPiece.VerticalPlacement.CODEC.fieldOf("placement").forGetter(Setup::placement), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("air_pocket_probability").forGetter(Setup::airPocketProbability), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("mossiness").forGetter(Setup::mossiness), (App)Codec.BOOL.fieldOf("overgrown").forGetter(Setup::overgrown), (App)Codec.BOOL.fieldOf("vines").forGetter(Setup::vines), (App)Codec.BOOL.fieldOf("can_be_cold").forGetter(Setup::canBeCold), (App)Codec.BOOL.fieldOf("replace_with_blackstone").forGetter(Setup::replaceWithBlackstone), (App)ExtraCodecs.POSITIVE_FLOAT.fieldOf("weight").forGetter(Setup::weight)).apply((Applicative)i, Setup::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\RuinedPortalStructure$Setup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */