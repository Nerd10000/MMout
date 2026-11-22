/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
/*     */ import com.mojang.blaze3d.resource.RenderTargetDescriptor;
/*     */ import com.mojang.blaze3d.resource.ResourceDescriptor;
/*     */ import com.mojang.blaze3d.resource.ResourceHandle;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import org.joml.Vector4fc;
/*     */ 
/*     */ public class PostChain implements AutoCloseable {
/*  31 */   public static final Identifier MAIN_TARGET_ID = Identifier.withDefaultNamespace("main");
/*     */   
/*     */   private final List<PostPass> passes;
/*     */   
/*     */   private final Map<Identifier, PostChainConfig.InternalTarget> internalTargets;
/*     */   
/*     */   private final Set<Identifier> externalTargets;
/*     */   
/*  36 */   private final Map<Identifier, RenderTarget> persistentTargets = new HashMap<>();
/*     */   
/*     */   private final Projection projection;
/*     */   
/*     */   private final ProjectionMatrixBuffer projectionMatrixBuffer;
/*     */   
/*     */   private PostChain(List<PostPass> passes, Map<Identifier, PostChainConfig.InternalTarget> internalTargets, Set<Identifier> externalTargets, Projection projection, ProjectionMatrixBuffer projectionMatrixBuffer) {
/*  41 */     this.passes = passes;
/*  42 */     this.internalTargets = internalTargets;
/*  43 */     this.externalTargets = externalTargets;
/*  44 */     this.projection = projection;
/*  45 */     this.projectionMatrixBuffer = projectionMatrixBuffer;
/*     */   }
/*     */   
/*     */   public static PostChain load(PostChainConfig config, TextureManager textureManager, Set<Identifier> allowedExternalTargets, Identifier id, Projection projection, ProjectionMatrixBuffer projectionMatrixBuffer) throws ShaderManager.CompilationException {
/*  49 */     Stream<Identifier> referencedTargets = config.passes().stream().flatMap(PostChainConfig.Pass::referencedTargets);
/*  53 */     Set<Identifier> referencedExternalTargets = (Set<Identifier>)referencedTargets.filter(targetId -> !config.internalTargets().containsKey(targetId)).collect(Collectors.toSet());
/*  55 */     Sets.SetView setView = Sets.difference(referencedExternalTargets, allowedExternalTargets);
/*  56 */     if (!setView.isEmpty())
/*  57 */       throw new ShaderManager.CompilationException("Referenced external targets are not available in this context: " + String.valueOf(setView)); 
/*  60 */     ImmutableList.Builder<PostPass> passes = ImmutableList.builder();
/*  61 */     for (int i = 0; i < config.passes().size(); i++) {
/*  62 */       PostChainConfig.Pass pass = config.passes().get(i);
/*  63 */       passes.add(createPass(textureManager, pass, id.withSuffix("/" + i)));
/*     */     } 
/*  66 */     return new PostChain((List<PostPass>)passes.build(), config.internalTargets(), referencedExternalTargets, projection, projectionMatrixBuffer);
/*     */   }
/*     */   
/*     */   private static PostPass createPass(TextureManager textureManager, PostChainConfig.Pass config, Identifier id) throws ShaderManager.CompilationException {
/*     */     // Byte code:
/*     */     //   0: iconst_1
/*     */     //   1: anewarray com/mojang/blaze3d/pipeline/RenderPipeline$Snippet
/*     */     //   4: dup
/*     */     //   5: iconst_0
/*     */     //   6: getstatic net/minecraft/client/renderer/RenderPipelines.POST_PROCESSING_SNIPPET : Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;
/*     */     //   9: aastore
/*     */     //   10: invokestatic builder : ([Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;)Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;
/*     */     //   13: aload_1
/*     */     //   14: invokevirtual fragmentShaderId : ()Lnet/minecraft/resources/Identifier;
/*     */     //   17: invokevirtual withFragmentShader : (Lnet/minecraft/resources/Identifier;)Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;
/*     */     //   20: aload_1
/*     */     //   21: invokevirtual vertexShaderId : ()Lnet/minecraft/resources/Identifier;
/*     */     //   24: invokevirtual withVertexShader : (Lnet/minecraft/resources/Identifier;)Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;
/*     */     //   27: aload_2
/*     */     //   28: invokevirtual withLocation : (Lnet/minecraft/resources/Identifier;)Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;
/*     */     //   31: astore_3
/*     */     //   32: invokestatic builder : ()Lcom/mojang/blaze3d/pipeline/BindGroupLayout$Builder;
/*     */     //   35: astore #4
/*     */     //   37: aload_1
/*     */     //   38: invokevirtual inputs : ()Ljava/util/List;
/*     */     //   41: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   46: astore #5
/*     */     //   48: aload #5
/*     */     //   50: invokeinterface hasNext : ()Z
/*     */     //   55: ifeq -> 91
/*     */     //   58: aload #5
/*     */     //   60: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   65: checkcast net/minecraft/client/renderer/PostChainConfig$Input
/*     */     //   68: astore #6
/*     */     //   70: aload #4
/*     */     //   72: aload #6
/*     */     //   74: invokeinterface samplerName : ()Ljava/lang/String;
/*     */     //   79: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   84: invokevirtual withSampler : (Ljava/lang/String;)Lcom/mojang/blaze3d/pipeline/BindGroupLayout$Builder;
/*     */     //   87: pop
/*     */     //   88: goto -> 48
/*     */     //   91: aload #4
/*     */     //   93: ldc 'SamplerInfo'
/*     */     //   95: getstatic com/mojang/blaze3d/shaders/UniformType.UNIFORM_BUFFER : Lcom/mojang/blaze3d/shaders/UniformType;
/*     */     //   98: invokevirtual withUniform : (Ljava/lang/String;Lcom/mojang/blaze3d/shaders/UniformType;)Lcom/mojang/blaze3d/pipeline/BindGroupLayout$Builder;
/*     */     //   101: pop
/*     */     //   102: aload_1
/*     */     //   103: invokevirtual uniforms : ()Ljava/util/Map;
/*     */     //   106: invokeinterface keySet : ()Ljava/util/Set;
/*     */     //   111: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   116: astore #5
/*     */     //   118: aload #5
/*     */     //   120: invokeinterface hasNext : ()Z
/*     */     //   125: ifeq -> 154
/*     */     //   128: aload #5
/*     */     //   130: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   135: checkcast java/lang/String
/*     */     //   138: astore #6
/*     */     //   140: aload #4
/*     */     //   142: aload #6
/*     */     //   144: getstatic com/mojang/blaze3d/shaders/UniformType.UNIFORM_BUFFER : Lcom/mojang/blaze3d/shaders/UniformType;
/*     */     //   147: invokevirtual withUniform : (Ljava/lang/String;Lcom/mojang/blaze3d/shaders/UniformType;)Lcom/mojang/blaze3d/pipeline/BindGroupLayout$Builder;
/*     */     //   150: pop
/*     */     //   151: goto -> 118
/*     */     //   154: aload_3
/*     */     //   155: aload #4
/*     */     //   157: invokevirtual build : ()Lcom/mojang/blaze3d/pipeline/BindGroupLayout;
/*     */     //   160: invokevirtual withBindGroupLayout : (Lcom/mojang/blaze3d/pipeline/BindGroupLayout;)Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;
/*     */     //   163: pop
/*     */     //   164: aload_3
/*     */     //   165: invokevirtual build : ()Lcom/mojang/blaze3d/pipeline/RenderPipeline;
/*     */     //   168: astore #5
/*     */     //   170: invokestatic getDevice : ()Lcom/mojang/blaze3d/systems/GpuDevice;
/*     */     //   173: aload #5
/*     */     //   175: invokevirtual precompilePipeline : (Lcom/mojang/blaze3d/pipeline/RenderPipeline;)Lcom/mojang/blaze3d/pipeline/CompiledRenderPipeline;
/*     */     //   178: invokeinterface isValid : ()Z
/*     */     //   183: ifne -> 207
/*     */     //   186: new net/minecraft/client/renderer/ShaderManager$CompilationException
/*     */     //   189: dup
/*     */     //   190: aload #5
/*     */     //   192: invokevirtual getLocation : ()Lnet/minecraft/resources/Identifier;
/*     */     //   195: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   198: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   203: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   206: athrow
/*     */     //   207: new java/util/ArrayList
/*     */     //   210: dup
/*     */     //   211: invokespecial <init> : ()V
/*     */     //   214: astore #6
/*     */     //   216: aload_1
/*     */     //   217: invokevirtual inputs : ()Ljava/util/List;
/*     */     //   220: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   225: astore #7
/*     */     //   227: aload #7
/*     */     //   229: invokeinterface hasNext : ()Z
/*     */     //   234: ifeq -> 550
/*     */     //   237: aload #7
/*     */     //   239: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   244: checkcast net/minecraft/client/renderer/PostChainConfig$Input
/*     */     //   247: astore #8
/*     */     //   249: aload #8
/*     */     //   251: dup
/*     */     //   252: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   255: pop
/*     */     //   256: astore #9
/*     */     //   258: iconst_0
/*     */     //   259: istore #10
/*     */     //   261: aload #9
/*     */     //   263: iload #10
/*     */     //   265: <illegal opcode> typeSwitch : (Lnet/minecraft/client/renderer/PostChainConfig$Input;I)I
/*     */     //   270: lookupswitch default -> 296, 0 -> 306, 1 -> 445
/*     */     //   296: new java/lang/MatchException
/*     */     //   299: dup
/*     */     //   300: aconst_null
/*     */     //   301: aconst_null
/*     */     //   302: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   305: athrow
/*     */     //   306: aload #9
/*     */     //   308: checkcast net/minecraft/client/renderer/PostChainConfig$TextureInput
/*     */     //   311: astore #11
/*     */     //   313: aload #11
/*     */     //   315: invokevirtual samplerName : ()Ljava/lang/String;
/*     */     //   318: astore #17
/*     */     //   320: aload #17
/*     */     //   322: astore #12
/*     */     //   324: aload #11
/*     */     //   326: invokevirtual location : ()Lnet/minecraft/resources/Identifier;
/*     */     //   329: astore #17
/*     */     //   331: aload #17
/*     */     //   333: astore #13
/*     */     //   335: aload #11
/*     */     //   337: invokevirtual width : ()I
/*     */     //   340: istore #17
/*     */     //   342: iload #17
/*     */     //   344: istore #18
/*     */     //   346: iconst_1
/*     */     //   347: ifeq -> 395
/*     */     //   350: iload #17
/*     */     //   352: istore #14
/*     */     //   354: aload #11
/*     */     //   356: invokevirtual height : ()I
/*     */     //   359: istore #17
/*     */     //   361: iload #17
/*     */     //   363: istore #18
/*     */     //   365: iconst_1
/*     */     //   366: ifeq -> 395
/*     */     //   369: iload #17
/*     */     //   371: istore #15
/*     */     //   373: aload #11
/*     */     //   375: invokevirtual bilinear : ()Z
/*     */     //   378: istore #17
/*     */     //   380: iload #17
/*     */     //   382: istore #18
/*     */     //   384: iconst_1
/*     */     //   385: ifeq -> 395
/*     */     //   388: iload #17
/*     */     //   390: istore #16
/*     */     //   392: goto -> 401
/*     */     //   395: iconst_1
/*     */     //   396: istore #10
/*     */     //   398: goto -> 261
/*     */     //   401: aload_0
/*     */     //   402: aload #13
/*     */     //   404: <illegal opcode> apply : ()Ljava/util/function/UnaryOperator;
/*     */     //   409: invokevirtual withPath : (Ljava/util/function/UnaryOperator;)Lnet/minecraft/resources/Identifier;
/*     */     //   412: invokevirtual getTexture : (Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/texture/AbstractTexture;
/*     */     //   415: astore #17
/*     */     //   417: aload #6
/*     */     //   419: new net/minecraft/client/renderer/PostPass$TextureInput
/*     */     //   422: dup
/*     */     //   423: aload #12
/*     */     //   425: aload #17
/*     */     //   427: iload #14
/*     */     //   429: iload #15
/*     */     //   431: iload #16
/*     */     //   433: invokespecial <init> : (Ljava/lang/String;Lnet/minecraft/client/renderer/texture/AbstractTexture;IIZ)V
/*     */     //   436: invokeinterface add : (Ljava/lang/Object;)Z
/*     */     //   441: pop
/*     */     //   442: goto -> 547
/*     */     //   445: aload #9
/*     */     //   447: checkcast net/minecraft/client/renderer/PostChainConfig$TargetInput
/*     */     //   450: astore #17
/*     */     //   452: aload #17
/*     */     //   454: invokevirtual samplerName : ()Ljava/lang/String;
/*     */     //   457: astore #22
/*     */     //   459: aload #22
/*     */     //   461: astore #18
/*     */     //   463: aload #17
/*     */     //   465: invokevirtual targetId : ()Lnet/minecraft/resources/Identifier;
/*     */     //   468: astore #22
/*     */     //   470: aload #22
/*     */     //   472: astore #19
/*     */     //   474: aload #17
/*     */     //   476: invokevirtual useDepthBuffer : ()Z
/*     */     //   479: istore #22
/*     */     //   481: iload #22
/*     */     //   483: istore #23
/*     */     //   485: iconst_1
/*     */     //   486: ifeq -> 515
/*     */     //   489: iload #22
/*     */     //   491: istore #20
/*     */     //   493: aload #17
/*     */     //   495: invokevirtual bilinear : ()Z
/*     */     //   498: istore #22
/*     */     //   500: iload #22
/*     */     //   502: istore #23
/*     */     //   504: iconst_1
/*     */     //   505: ifeq -> 515
/*     */     //   508: iload #22
/*     */     //   510: istore #21
/*     */     //   512: goto -> 521
/*     */     //   515: iconst_2
/*     */     //   516: istore #10
/*     */     //   518: goto -> 261
/*     */     //   521: aload #6
/*     */     //   523: new net/minecraft/client/renderer/PostPass$TargetInput
/*     */     //   526: dup
/*     */     //   527: aload #18
/*     */     //   529: aload #19
/*     */     //   531: iload #20
/*     */     //   533: iload #21
/*     */     //   535: invokespecial <init> : (Ljava/lang/String;Lnet/minecraft/resources/Identifier;ZZ)V
/*     */     //   538: invokeinterface add : (Ljava/lang/Object;)Z
/*     */     //   543: pop
/*     */     //   544: goto -> 547
/*     */     //   547: goto -> 227
/*     */     //   550: new net/minecraft/client/renderer/PostPass
/*     */     //   553: dup
/*     */     //   554: aload #5
/*     */     //   556: aload_1
/*     */     //   557: invokevirtual outputTarget : ()Lnet/minecraft/resources/Identifier;
/*     */     //   560: aload_1
/*     */     //   561: invokevirtual uniforms : ()Ljava/util/Map;
/*     */     //   564: aload #6
/*     */     //   566: invokespecial <init> : (Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;Ljava/util/Map;Ljava/util/List;)V
/*     */     //   569: areturn
/*     */     //   570: astore #7
/*     */     //   572: new java/lang/MatchException
/*     */     //   575: dup
/*     */     //   576: aload #7
/*     */     //   578: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   581: aload #7
/*     */     //   583: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   586: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #70	-> 0
/*     */     //   #71	-> 14
/*     */     //   #72	-> 21
/*     */     //   #73	-> 28
/*     */     //   #75	-> 32
/*     */     //   #76	-> 37
/*     */     //   #77	-> 70
/*     */     //   #78	-> 88
/*     */     //   #79	-> 91
/*     */     //   #81	-> 102
/*     */     //   #82	-> 140
/*     */     //   #83	-> 151
/*     */     //   #85	-> 154
/*     */     //   #86	-> 164
/*     */     //   #88	-> 170
/*     */     //   #89	-> 186
/*     */     //   #92	-> 207
/*     */     //   #94	-> 216
/*     */     //   #95	-> 249
/*     */     //   #96	-> 306
/*     */     //   #97	-> 320
/*     */     //   #96	-> 324
/*     */     //   #97	-> 331
/*     */     //   #96	-> 335
/*     */     //   #97	-> 342
/*     */     //   #96	-> 354
/*     */     //   #97	-> 361
/*     */     //   #96	-> 373
/*     */     //   #97	-> 380
/*     */     //   #96	-> 395
/*     */     //   #99	-> 401
/*     */     //   #100	-> 417
/*     */     //   #101	-> 442
/*     */     //   #102	-> 445
/*     */     //   #103	-> 459
/*     */     //   #102	-> 463
/*     */     //   #103	-> 470
/*     */     //   #102	-> 474
/*     */     //   #103	-> 481
/*     */     //   #102	-> 493
/*     */     //   #103	-> 500
/*     */     //   #102	-> 515
/*     */     //   #104	-> 521
/*     */     //   #106	-> 547
/*     */     //   #108	-> 550
/*     */     //   #102	-> 570
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   70	18	6	input	Lnet/minecraft/client/renderer/PostChainConfig$Input;
/*     */     //   140	11	6	uniformGroupName	Ljava/lang/String;
/*     */     //   354	41	14	width	I
/*     */     //   373	22	15	height	I
/*     */     //   392	3	16	bilinear	Z
/*     */     //   417	25	17	texture	Lnet/minecraft/client/renderer/texture/AbstractTexture;
/*     */     //   324	121	12	samplerName	Ljava/lang/String;
/*     */     //   335	110	13	location	Lnet/minecraft/resources/Identifier;
/*     */     //   401	44	14	width	I
/*     */     //   401	44	15	height	I
/*     */     //   401	44	16	bilinear	Z
/*     */     //   493	22	20	useDepthBuffer	Z
/*     */     //   512	3	21	bilinear	Z
/*     */     //   463	84	18	samplerName	Ljava/lang/String;
/*     */     //   474	73	19	targetId	Lnet/minecraft/resources/Identifier;
/*     */     //   521	26	20	useDepthBuffer	Z
/*     */     //   521	26	21	bilinear	Z
/*     */     //   249	298	8	input	Lnet/minecraft/client/renderer/PostChainConfig$Input;
/*     */     //   32	538	3	pipelineBuilder	Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;
/*     */     //   37	533	4	bindGroupLayoutBuilder	Lcom/mojang/blaze3d/pipeline/BindGroupLayout$Builder;
/*     */     //   170	400	5	pipeline	Lcom/mojang/blaze3d/pipeline/RenderPipeline;
/*     */     //   216	354	6	inputs	Ljava/util/List;
/*     */     //   0	587	0	textureManager	Lnet/minecraft/client/renderer/texture/TextureManager;
/*     */     //   0	587	1	config	Lnet/minecraft/client/renderer/PostChainConfig$Pass;
/*     */     //   0	587	2	id	Lnet/minecraft/resources/Identifier;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   216	354	6	inputs	Ljava/util/List<Lnet/minecraft/client/renderer/PostPass$Input;>;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   315	318	570	java/lang/Throwable
/*     */     //   326	329	570	java/lang/Throwable
/*     */     //   337	340	570	java/lang/Throwable
/*     */     //   356	359	570	java/lang/Throwable
/*     */     //   375	378	570	java/lang/Throwable
/*     */     //   454	457	570	java/lang/Throwable
/*     */     //   465	468	570	java/lang/Throwable
/*     */     //   476	479	570	java/lang/Throwable
/*     */     //   495	498	570	java/lang/Throwable
/*     */   }
/*     */   
/*     */   public void addToFrame(FrameGraphBuilder frame, int screenWidth, int screenHeight, TargetBundle providedTargets) {
/* 112 */     this.projection.setSize(screenWidth, screenHeight);
/* 113 */     GpuBufferSlice projectionBuffer = this.projectionMatrixBuffer.getBuffer(this.projection);
/* 115 */     Map<Identifier, ResourceHandle<RenderTarget>> targets = new HashMap<>(this.internalTargets.size() + this.externalTargets.size());
/* 116 */     for (Identifier id : this.externalTargets)
/* 117 */       targets.put(id, providedTargets.getOrThrow(id)); 
/* 120 */     for (Map.Entry<Identifier, PostChainConfig.InternalTarget> entry : this.internalTargets.entrySet()) {
/* 121 */       Identifier id = entry.getKey();
/* 122 */       PostChainConfig.InternalTarget target = entry.getValue();
/* 127 */       RenderTargetDescriptor descriptor = new RenderTargetDescriptor(((Integer)target.width().orElse(Integer.valueOf(screenWidth))).intValue(), ((Integer)target.height().orElse(Integer.valueOf(screenHeight))).intValue(), true, (Vector4fc)ARGB.vector4fFromARGB32(target.clearColor()), GpuFormat.RGBA8_UNORM);
/* 130 */       if (target.persistent()) {
/* 131 */         RenderTarget persistentTarget = getOrCreatePersistentTarget(id, descriptor);
/* 132 */         targets.put(id, frame.importExternal(id.toString(), persistentTarget));
/*     */         continue;
/*     */       } 
/* 134 */       targets.put(id, frame.createInternal(id.toString(), (ResourceDescriptor)descriptor));
/*     */     } 
/* 138 */     for (PostPass pass : this.passes)
/* 139 */       pass.addToFrame(frame, targets, projectionBuffer); 
/* 142 */     for (Identifier id : this.externalTargets)
/* 143 */       providedTargets.replace(id, targets.get(id)); 
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void process(RenderTarget mainTarget, GraphicsResourceAllocator resourceAllocator) {
/* 150 */     FrameGraphBuilder frame = new FrameGraphBuilder();
/* 151 */     TargetBundle targets = TargetBundle.of(MAIN_TARGET_ID, frame.importExternal("main", mainTarget));
/* 152 */     addToFrame(frame, mainTarget.width, mainTarget.height, targets);
/* 153 */     frame.execute(resourceAllocator);
/*     */   }
/*     */   
/*     */   private RenderTarget getOrCreatePersistentTarget(Identifier id, RenderTargetDescriptor descriptor) {
/* 157 */     RenderTarget target = this.persistentTargets.get(id);
/* 158 */     if (target == null || target.width != descriptor.width() || target.height != descriptor.height()) {
/* 159 */       if (target != null)
/* 160 */         target.destroyBuffers(); 
/* 162 */       target = descriptor.allocate();
/* 163 */       descriptor.prepare(target);
/* 164 */       this.persistentTargets.put(id, target);
/*     */     } 
/* 166 */     return target;
/*     */   }
/*     */   
/*     */   public void close() {
/* 171 */     this.persistentTargets.values().forEach(RenderTarget::destroyBuffers);
/* 172 */     this.persistentTargets.clear();
/* 173 */     for (PostPass pass : this.passes)
/* 174 */       pass.close(); 
/*     */   }
/*     */   
/*     */   public static interface TargetBundle {
/*     */     static TargetBundle of(final Identifier targetId, final ResourceHandle<RenderTarget> target) {
/* 180 */       return new TargetBundle() {
/* 181 */           private ResourceHandle<RenderTarget> handle = target;
/*     */           
/*     */           public void replace(Identifier id, ResourceHandle<RenderTarget> handle) {
/* 185 */             if (id.equals(targetId)) {
/* 186 */               this.handle = handle;
/*     */             } else {
/* 188 */               throw new IllegalArgumentException("No target with id " + String.valueOf(id));
/*     */             } 
/*     */           }
/*     */           
/*     */           public ResourceHandle<RenderTarget> get(Identifier id) {
/* 194 */             return id.equals(targetId) ? this.handle : null;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     void replace(Identifier param1Identifier, ResourceHandle<RenderTarget> param1ResourceHandle);
/*     */     
/*     */     ResourceHandle<RenderTarget> get(Identifier param1Identifier);
/*     */     
/*     */     default ResourceHandle<RenderTarget> getOrThrow(Identifier id) {
/* 204 */       ResourceHandle<RenderTarget> handle = get(id);
/* 205 */       if (handle == null)
/* 206 */         throw new IllegalArgumentException("Missing target with id " + String.valueOf(id)); 
/* 208 */       return handle;
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements TargetBundle {
/*     */     private ResourceHandle<RenderTarget> handle = target;
/*     */     
/*     */     public void replace(Identifier id, ResourceHandle<RenderTarget> handle) {
/*     */       if (id.equals(targetId)) {
/*     */         this.handle = handle;
/*     */       } else {
/*     */         throw new IllegalArgumentException("No target with id " + String.valueOf(id));
/*     */       } 
/*     */     }
/*     */     
/*     */     public ResourceHandle<RenderTarget> get(Identifier id) {
/*     */       return id.equals(targetId) ? this.handle : null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PostChain.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */