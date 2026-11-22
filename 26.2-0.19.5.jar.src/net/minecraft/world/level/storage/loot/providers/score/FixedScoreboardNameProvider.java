/*    */ package net.minecraft.world.level.storage.loot.providers.score;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.scores.ScoreHolder;
/*    */ 
/*    */ public final class FixedScoreboardNameProvider extends Record implements ScoreboardNameProvider {
/*    */   private final String name;
/*    */   
/*    */   public static final MapCodec<FixedScoreboardNameProvider> MAP_CODEC;
/*    */   
/*    */   public FixedScoreboardNameProvider(String name) {
/*  9 */     this.name = name;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String name() {
/*  9 */     return this.name;
/*    */   }
/*    */   
/*    */   static {
/* 10 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("name").forGetter(FixedScoreboardNameProvider::name)).apply((Applicative)i, FixedScoreboardNameProvider::new));
/*    */   }
/*    */   
/*    */   public static ScoreboardNameProvider forName(String name) {
/* 15 */     return new FixedScoreboardNameProvider(name);
/*    */   }
/*    */   
/*    */   public MapCodec<FixedScoreboardNameProvider> codec() {
/* 20 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ScoreHolder getScoreHolder(LootContext context) {
/* 25 */     return ScoreHolder.forNameOnly(this.name);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\score\FixedScoreboardNameProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */