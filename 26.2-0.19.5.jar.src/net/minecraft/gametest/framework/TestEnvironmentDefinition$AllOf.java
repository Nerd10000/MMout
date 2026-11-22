/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ 
/*     */ public final class AllOf extends Record implements TestEnvironmentDefinition<List<? extends TestEnvironmentDefinition.Activation<?>>> {
/*     */   private final List<Holder<TestEnvironmentDefinition<?>>> definitions;
/*     */   
/*     */   public static final MapCodec<AllOf> CODEC;
/*     */   
/*     */   public AllOf(List<Holder<TestEnvironmentDefinition<?>>> definitions) {
/* 239 */     this.definitions = definitions;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #239	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #239	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #239	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$AllOf;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public List<Holder<TestEnvironmentDefinition<?>>> definitions() {
/* 239 */     return this.definitions;
/*     */   }
/*     */   
/*     */   static {
/* 240 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TestEnvironmentDefinition.CODEC.listOf().fieldOf("definitions").forGetter(AllOf::definitions)).apply((Applicative)i, AllOf::new));
/*     */   }
/*     */   
/*     */   public AllOf(TestEnvironmentDefinition<?>... defs) {
/* 245 */     this(Arrays.<TestEnvironmentDefinition<?>>stream(defs).map(AllOf::holder).toList());
/*     */   }
/*     */   
/*     */   private static Holder<TestEnvironmentDefinition<?>> holder(TestEnvironmentDefinition<?> holder) {
/* 249 */     return Holder.direct(holder);
/*     */   }
/*     */   
/*     */   public List<? extends TestEnvironmentDefinition.Activation<?>> setup(ServerLevel level) {
/* 254 */     return this.definitions.stream().map(b -> TestEnvironmentDefinition.activate((TestEnvironmentDefinition)b.value(), level)).toList();
/*     */   }
/*     */   
/*     */   public void teardown(ServerLevel level, List<? extends TestEnvironmentDefinition.Activation<?>> activations) {
/* 259 */     activations.reversed().forEach(TestEnvironmentDefinition.Activation::teardown);
/*     */   }
/*     */   
/*     */   public MapCodec<AllOf> codec() {
/* 264 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestEnvironmentDefinition$AllOf.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */