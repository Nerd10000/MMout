/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class PostChainConfig extends Record {
/*     */   private final Map<Identifier, InternalTarget> internalTargets;
/*     */   
/*     */   private final List<Pass> passes;
/*     */   
/*     */   public static final Codec<PostChainConfig> CODEC;
/*     */   
/*     */   public PostChainConfig(Map<Identifier, InternalTarget> internalTargets, List<Pass> passes) {
/*  18 */     this.internalTargets = internalTargets;
/*  18 */     this.passes = passes;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostChainConfig;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #18	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostChainConfig;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #18	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostChainConfig;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #18	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/PostChainConfig;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Map<Identifier, InternalTarget> internalTargets() {
/*  18 */     return this.internalTargets;
/*     */   }
/*     */   
/*     */   public List<Pass> passes() {
/*  18 */     return this.passes;
/*     */   }
/*     */   
/*     */   static {
/*  22 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.unboundedMap(Identifier.CODEC, InternalTarget.CODEC).optionalFieldOf("targets", Map.of()).forGetter(PostChainConfig::internalTargets), (App)Pass.CODEC.listOf().optionalFieldOf("passes", List.of()).forGetter(PostChainConfig::passes)).apply((Applicative)i, PostChainConfig::new));
/*     */   }
/*     */   
/*     */   public static final class InternalTarget extends Record {
/*     */     private final Optional<Integer> width;
/*     */     
/*     */     private final Optional<Integer> height;
/*     */     
/*     */     private final boolean persistent;
/*     */     
/*     */     private final int clearColor;
/*     */     
/*     */     public static final Codec<InternalTarget> CODEC;
/*     */     
/*     */     public InternalTarget(Optional<Integer> width, Optional<Integer> height, boolean persistent, int clearColor) {
/*  27 */       this.width = width;
/*  27 */       this.height = height;
/*  27 */       this.persistent = persistent;
/*  27 */       this.clearColor = clearColor;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Optional<Integer> width() {
/*  27 */       return this.width;
/*     */     }
/*     */     
/*     */     public Optional<Integer> height() {
/*  27 */       return this.height;
/*     */     }
/*     */     
/*     */     public boolean persistent() {
/*  27 */       return this.persistent;
/*     */     }
/*     */     
/*     */     public int clearColor() {
/*  27 */       return this.clearColor;
/*     */     }
/*     */     
/*     */     static {
/*  33 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("width").forGetter(InternalTarget::width), (App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("height").forGetter(InternalTarget::height), (App)Codec.BOOL.optionalFieldOf("persistent", Boolean.valueOf(false)).forGetter(InternalTarget::persistent), (App)ExtraCodecs.ARGB_COLOR_CODEC.optionalFieldOf("clear_color", Integer.valueOf(0)).forGetter(InternalTarget::clearColor)).apply((Applicative)i, InternalTarget::new));
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Pass extends Record {
/*     */     private final Identifier vertexShaderId;
/*     */     
/*     */     private final Identifier fragmentShaderId;
/*     */     
/*     */     private final List<PostChainConfig.Input> inputs;
/*     */     
/*     */     private final Identifier outputTarget;
/*     */     
/*     */     private final Map<String, List<UniformValue>> uniforms;
/*     */     
/*     */     private static final Codec<List<PostChainConfig.Input>> INPUTS_CODEC;
/*     */     
/*     */     public Pass(Identifier vertexShaderId, Identifier fragmentShaderId, List<PostChainConfig.Input> inputs, Identifier outputTarget, Map<String, List<UniformValue>> uniforms) {
/*  41 */       this.vertexShaderId = vertexShaderId;
/*  41 */       this.fragmentShaderId = fragmentShaderId;
/*  41 */       this.inputs = inputs;
/*  41 */       this.outputTarget = outputTarget;
/*  41 */       this.uniforms = uniforms;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostChainConfig$Pass;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$Pass;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostChainConfig$Pass;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$Pass;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostChainConfig$Pass;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/PostChainConfig$Pass;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Identifier vertexShaderId() {
/*  41 */       return this.vertexShaderId;
/*     */     }
/*     */     
/*     */     public Identifier fragmentShaderId() {
/*  41 */       return this.fragmentShaderId;
/*     */     }
/*     */     
/*     */     public List<PostChainConfig.Input> inputs() {
/*  41 */       return this.inputs;
/*     */     }
/*     */     
/*     */     public Identifier outputTarget() {
/*  41 */       return this.outputTarget;
/*     */     }
/*     */     
/*     */     public Map<String, List<UniformValue>> uniforms() {
/*  41 */       return this.uniforms;
/*     */     }
/*     */     
/*     */     static {
/*  48 */       INPUTS_CODEC = PostChainConfig.Input.CODEC.listOf().validate(inputs -> {
/*     */             ObjectArraySet<String> objectArraySet = new ObjectArraySet(inputs.size());
/*     */             for (PostChainConfig.Input input : inputs) {
/*     */               if (!objectArraySet.add(input.samplerName()))
/*     */                 return DataResult.error(()); 
/*     */             } 
/*     */             return DataResult.success(inputs);
/*     */           });
/*     */     }
/*     */     
/*  57 */     private static final Codec<Map<String, List<UniformValue>>> UNIFORM_BLOCKS_CODEC = (Codec<Map<String, List<UniformValue>>>)Codec.unboundedMap((Codec)Codec.STRING, UniformValue.CODEC.listOf());
/*     */     
/*     */     public static final Codec<Pass> CODEC;
/*     */     
/*     */     static {
/*  59 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("vertex_shader").forGetter(Pass::vertexShaderId), (App)Identifier.CODEC.fieldOf("fragment_shader").forGetter(Pass::fragmentShaderId), (App)INPUTS_CODEC.optionalFieldOf("inputs", List.of()).forGetter(Pass::inputs), (App)Identifier.CODEC.fieldOf("output").forGetter(Pass::outputTarget), (App)UNIFORM_BLOCKS_CODEC.optionalFieldOf("uniforms", Map.of()).forGetter(Pass::uniforms)).apply((Applicative)i, Pass::new));
/*     */     }
/*     */     
/*     */     public Stream<Identifier> referencedTargets() {
/*  68 */       Stream<Identifier> inputTargets = this.inputs.stream().flatMap(input -> input.referencedTargets().stream());
/*  69 */       return Stream.concat(inputTargets, Stream.of(this.outputTarget));
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Input {
/*     */     public static final Codec<Input> CODEC;
/*     */     
/*     */     static {
/*  74 */       CODEC = Codec.xor(PostChainConfig.TextureInput.CODEC, PostChainConfig.TargetInput.CODEC).xmap(either -> (Input)either.map(Function.identity(), Function.identity()), input -> {
/*     */             // Byte code:
/*     */             //   0: aload_0
/*     */             //   1: dup
/*     */             //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */             //   5: pop
/*     */             //   6: astore_1
/*     */             //   7: iconst_0
/*     */             //   8: istore_2
/*     */             //   9: aload_1
/*     */             //   10: iload_2
/*     */             //   11: <illegal opcode> typeSwitch : (Lnet/minecraft/client/renderer/PostChainConfig$Input;I)I
/*     */             //   16: lookupswitch default -> 44, 0 -> 54, 1 -> 66
/*     */             //   44: new java/lang/MatchException
/*     */             //   47: dup
/*     */             //   48: aconst_null
/*     */             //   49: aconst_null
/*     */             //   50: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */             //   53: athrow
/*     */             //   54: aload_1
/*     */             //   55: checkcast net/minecraft/client/renderer/PostChainConfig$TextureInput
/*     */             //   58: astore_3
/*     */             //   59: aload_3
/*     */             //   60: invokestatic left : (Ljava/lang/Object;)Lcom/mojang/datafixers/util/Either;
/*     */             //   63: goto -> 77
/*     */             //   66: aload_1
/*     */             //   67: checkcast net/minecraft/client/renderer/PostChainConfig$TargetInput
/*     */             //   70: astore #4
/*     */             //   72: aload #4
/*     */             //   74: invokestatic right : (Ljava/lang/Object;)Lcom/mojang/datafixers/util/Either;
/*     */             //   77: areturn
/*     */             // Line number table:
/*     */             //   Java source line number -> byte code offset
/*     */             //   #76	-> 0
/*     */             //   #77	-> 54
/*     */             //   #78	-> 66
/*     */             // Local variable table:
/*     */             //   start	length	slot	name	descriptor
/*     */             //   59	7	3	texture	Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;
/*     */             //   72	5	4	target	Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;
/*     */             //   7	70	1	selector0$temp	Lnet/minecraft/client/renderer/PostChainConfig$Input;
/*     */             //   9	68	2	index$1	I
/*     */             //   0	78	0	input	Lnet/minecraft/client/renderer/PostChainConfig$Input;
/*     */           });
/*     */     }
/*     */     
/*     */     Set<Identifier> referencedTargets();
/*     */     
/*     */     String samplerName();
/*     */   }
/*     */   
/*     */   public static final class TextureInput extends Record implements Input {
/*     */     private final String samplerName;
/*     */     
/*     */     private final Identifier location;
/*     */     
/*     */     private final int width;
/*     */     
/*     */     private final int height;
/*     */     
/*     */     private final boolean bilinear;
/*     */     
/*     */     public static final Codec<TextureInput> CODEC;
/*     */     
/*     */     public TextureInput(String samplerName, Identifier location, int width, int height, boolean bilinear) {
/*  87 */       this.samplerName = samplerName;
/*  87 */       this.location = location;
/*  87 */       this.width = width;
/*  87 */       this.height = height;
/*  87 */       this.bilinear = bilinear;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String samplerName() {
/*  87 */       return this.samplerName;
/*     */     }
/*     */     
/*     */     public Identifier location() {
/*  87 */       return this.location;
/*     */     }
/*     */     
/*     */     public int width() {
/*  87 */       return this.width;
/*     */     }
/*     */     
/*     */     public int height() {
/*  87 */       return this.height;
/*     */     }
/*     */     
/*     */     public boolean bilinear() {
/*  87 */       return this.bilinear;
/*     */     }
/*     */     
/*     */     static {
/*  94 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("sampler_name").forGetter(TextureInput::samplerName), (App)Identifier.CODEC.fieldOf("location").forGetter(TextureInput::location), (App)ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(TextureInput::width), (App)ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(TextureInput::height), (App)Codec.BOOL.optionalFieldOf("bilinear", Boolean.valueOf(false)).forGetter(TextureInput::bilinear)).apply((Applicative)i, TextureInput::new));
/*     */     }
/*     */     
/*     */     public Set<Identifier> referencedTargets() {
/* 104 */       return Set.of();
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class TargetInput extends Record implements Input {
/*     */     private final String samplerName;
/*     */     
/*     */     private final Identifier targetId;
/*     */     
/*     */     private final boolean useDepthBuffer;
/*     */     
/*     */     private final boolean bilinear;
/*     */     
/*     */     public static final Codec<TargetInput> CODEC;
/*     */     
/*     */     public TargetInput(String samplerName, Identifier targetId, boolean useDepthBuffer, boolean bilinear) {
/* 108 */       this.samplerName = samplerName;
/* 108 */       this.targetId = targetId;
/* 108 */       this.useDepthBuffer = useDepthBuffer;
/* 108 */       this.bilinear = bilinear;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #108	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #108	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #108	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String samplerName() {
/* 108 */       return this.samplerName;
/*     */     }
/*     */     
/*     */     public Identifier targetId() {
/* 108 */       return this.targetId;
/*     */     }
/*     */     
/*     */     public boolean useDepthBuffer() {
/* 108 */       return this.useDepthBuffer;
/*     */     }
/*     */     
/*     */     public boolean bilinear() {
/* 108 */       return this.bilinear;
/*     */     }
/*     */     
/*     */     static {
/* 114 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("sampler_name").forGetter(TargetInput::samplerName), (App)Identifier.CODEC.fieldOf("target").forGetter(TargetInput::targetId), (App)Codec.BOOL.optionalFieldOf("use_depth_buffer", Boolean.valueOf(false)).forGetter(TargetInput::useDepthBuffer), (App)Codec.BOOL.optionalFieldOf("bilinear", Boolean.valueOf(false)).forGetter(TargetInput::bilinear)).apply((Applicative)i, TargetInput::new));
/*     */     }
/*     */     
/*     */     public Set<Identifier> referencedTargets() {
/* 123 */       return Set.of(this.targetId);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PostChainConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */