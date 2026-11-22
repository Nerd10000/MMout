/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class Pass extends Record {
/*    */   private final Identifier vertexShaderId;
/*    */   
/*    */   private final Identifier fragmentShaderId;
/*    */   
/*    */   private final List<PostChainConfig.Input> inputs;
/*    */   
/*    */   private final Identifier outputTarget;
/*    */   
/*    */   private final Map<String, List<UniformValue>> uniforms;
/*    */   
/*    */   private static final Codec<List<PostChainConfig.Input>> INPUTS_CODEC;
/*    */   
/*    */   public Pass(Identifier vertexShaderId, Identifier fragmentShaderId, List<PostChainConfig.Input> inputs, Identifier outputTarget, Map<String, List<UniformValue>> uniforms) {
/* 41 */     this.vertexShaderId = vertexShaderId;
/* 41 */     this.fragmentShaderId = fragmentShaderId;
/* 41 */     this.inputs = inputs;
/* 41 */     this.outputTarget = outputTarget;
/* 41 */     this.uniforms = uniforms;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostChainConfig$Pass;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$Pass;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostChainConfig$Pass;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$Pass;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostChainConfig$Pass;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #41	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/PostChainConfig$Pass;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier vertexShaderId() {
/* 41 */     return this.vertexShaderId;
/*    */   }
/*    */   
/*    */   public Identifier fragmentShaderId() {
/* 41 */     return this.fragmentShaderId;
/*    */   }
/*    */   
/*    */   public List<PostChainConfig.Input> inputs() {
/* 41 */     return this.inputs;
/*    */   }
/*    */   
/*    */   public Identifier outputTarget() {
/* 41 */     return this.outputTarget;
/*    */   }
/*    */   
/*    */   public Map<String, List<UniformValue>> uniforms() {
/* 41 */     return this.uniforms;
/*    */   }
/*    */   
/*    */   static {
/* 48 */     INPUTS_CODEC = PostChainConfig.Input.CODEC.listOf().validate(inputs -> {
/*    */           ObjectArraySet<String> objectArraySet = new ObjectArraySet(inputs.size());
/*    */           for (PostChainConfig.Input input : inputs) {
/*    */             if (!objectArraySet.add(input.samplerName()))
/*    */               return DataResult.error(()); 
/*    */           } 
/*    */           return DataResult.success(inputs);
/*    */         });
/*    */   }
/*    */   
/* 57 */   private static final Codec<Map<String, List<UniformValue>>> UNIFORM_BLOCKS_CODEC = (Codec<Map<String, List<UniformValue>>>)Codec.unboundedMap((Codec)Codec.STRING, UniformValue.CODEC.listOf());
/*    */   
/*    */   public static final Codec<Pass> CODEC;
/*    */   
/*    */   static {
/* 59 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("vertex_shader").forGetter(Pass::vertexShaderId), (App)Identifier.CODEC.fieldOf("fragment_shader").forGetter(Pass::fragmentShaderId), (App)INPUTS_CODEC.optionalFieldOf("inputs", List.of()).forGetter(Pass::inputs), (App)Identifier.CODEC.fieldOf("output").forGetter(Pass::outputTarget), (App)UNIFORM_BLOCKS_CODEC.optionalFieldOf("uniforms", Map.of()).forGetter(Pass::uniforms)).apply((Applicative)i, Pass::new));
/*    */   }
/*    */   
/*    */   public Stream<Identifier> referencedTargets() {
/* 68 */     Stream<Identifier> inputTargets = this.inputs.stream().flatMap(input -> input.referencedTargets().stream());
/* 69 */     return Stream.concat(inputTargets, Stream.of(this.outputTarget));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PostChainConfig$Pass.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */