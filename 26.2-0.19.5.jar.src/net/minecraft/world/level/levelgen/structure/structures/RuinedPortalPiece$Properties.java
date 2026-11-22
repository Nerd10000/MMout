/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class Properties extends Record {
/*    */   private final boolean cold;
/*    */   
/*    */   private final float mossiness;
/*    */   
/*    */   private final boolean airPocket;
/*    */   
/*    */   private final boolean overgrown;
/*    */   
/*    */   private final boolean vines;
/*    */   
/*    */   private final boolean replaceWithBlackstone;
/*    */   
/*    */   public static final Codec<Properties> CODEC;
/*    */   
/*    */   public Properties(boolean cold, float mossiness, boolean airPocket, boolean overgrown, boolean vines, boolean replaceWithBlackstone) {
/* 63 */     this.cold = cold;
/* 63 */     this.mossiness = mossiness;
/* 63 */     this.airPocket = airPocket;
/* 63 */     this.overgrown = overgrown;
/* 63 */     this.vines = vines;
/* 63 */     this.replaceWithBlackstone = replaceWithBlackstone;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalPiece$Properties;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #63	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalPiece$Properties;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalPiece$Properties;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #63	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalPiece$Properties;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalPiece$Properties;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #63	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/RuinedPortalPiece$Properties;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean cold() {
/* 63 */     return this.cold;
/*    */   }
/*    */   
/*    */   public float mossiness() {
/* 63 */     return this.mossiness;
/*    */   }
/*    */   
/*    */   public boolean airPocket() {
/* 63 */     return this.airPocket;
/*    */   }
/*    */   
/*    */   public boolean overgrown() {
/* 63 */     return this.overgrown;
/*    */   }
/*    */   
/*    */   public boolean vines() {
/* 63 */     return this.vines;
/*    */   }
/*    */   
/*    */   public boolean replaceWithBlackstone() {
/* 63 */     return this.replaceWithBlackstone;
/*    */   }
/*    */   
/*    */   static {
/* 71 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.fieldOf("cold").forGetter(Properties::cold), (App)Codec.FLOAT.fieldOf("mossiness").forGetter(Properties::mossiness), (App)Codec.BOOL.fieldOf("air_pocket").forGetter(Properties::airPocket), (App)Codec.BOOL.fieldOf("overgrown").forGetter(Properties::overgrown), (App)Codec.BOOL.fieldOf("vines").forGetter(Properties::vines), (App)Codec.BOOL.fieldOf("replace_with_blackstone").forGetter(Properties::replaceWithBlackstone)).apply((Applicative)i, Properties::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\RuinedPortalPiece$Properties.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */