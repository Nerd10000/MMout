/*    */ package net.minecraft.world.level.storage.loot.providers.score;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.scores.ScoreHolder;
/*    */ 
/*    */ public final class ContextScoreboardNameProvider extends Record implements ScoreboardNameProvider {
/*    */   private final LootContext.EntityTarget target;
/*    */   
/*    */   public static final MapCodec<ContextScoreboardNameProvider> MAP_CODEC;
/*    */   
/*    */   public ContextScoreboardNameProvider(LootContext.EntityTarget target) {
/* 13 */     this.target = target;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public LootContext.EntityTarget target() {
/* 13 */     return this.target;
/*    */   }
/*    */   
/*    */   static {
/* 14 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LootContext.EntityTarget.CODEC.fieldOf("target").forGetter(ContextScoreboardNameProvider::target)).apply((Applicative)i, ContextScoreboardNameProvider::new));
/*    */   }
/*    */   
/* 18 */   public static final Codec<ContextScoreboardNameProvider> INLINE_CODEC = LootContext.EntityTarget.CODEC.xmap(ContextScoreboardNameProvider::new, ContextScoreboardNameProvider::target);
/*    */   
/*    */   public static ScoreboardNameProvider forTarget(LootContext.EntityTarget target) {
/* 21 */     return new ContextScoreboardNameProvider(target);
/*    */   }
/*    */   
/*    */   public MapCodec<ContextScoreboardNameProvider> codec() {
/* 26 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ScoreHolder getScoreHolder(LootContext context) {
/* 31 */     return (ScoreHolder)context.getOptionalParameter(this.target.contextParam());
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 36 */     return Set.of(this.target.contextParam());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\score\ContextScoreboardNameProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */