/*     */ package net.minecraft.client.renderer.fog;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.Std140Builder;
/*     */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.DeltaTracker;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.MappableRingBuffer;
/*     */ import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
/*     */ import net.minecraft.client.renderer.fog.environment.BlindnessFogEnvironment;
/*     */ import net.minecraft.client.renderer.fog.environment.DarknessFogEnvironment;
/*     */ import net.minecraft.client.renderer.fog.environment.FogEnvironment;
/*     */ import net.minecraft.client.renderer.fog.environment.LavaFogEnvironment;
/*     */ import net.minecraft.client.renderer.fog.environment.PowderedSnowFogEnvironment;
/*     */ import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.material.FogType;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ 
/*     */ public class FogRenderer implements AutoCloseable {
/*  37 */   public static final int FOG_UBO_SIZE = (new Std140SizeCalculator())
/*  38 */     .putVec4()
/*  39 */     .putFloat()
/*  40 */     .putFloat()
/*  41 */     .putFloat()
/*  42 */     .putFloat()
/*  43 */     .putFloat()
/*  44 */     .putFloat()
/*  45 */     .get();
/*     */   
/*     */   public enum FogMode {
/*  48 */     NONE, WORLD;
/*     */   }
/*     */   
/*  52 */   private static final List<FogEnvironment> FOG_ENVIRONMENTS = Lists.newArrayList((Object[])new FogEnvironment[] { (FogEnvironment)new LavaFogEnvironment(), (FogEnvironment)new PowderedSnowFogEnvironment(), (FogEnvironment)new BlindnessFogEnvironment(), (FogEnvironment)new DarknessFogEnvironment(), (FogEnvironment)new WaterFogEnvironment(), (FogEnvironment)new AtmosphericFogEnvironment() });
/*     */   
/*     */   private static boolean fogEnabled = true;
/*     */   
/*     */   private final GpuBuffer emptyBuffer;
/*     */   
/*     */   private final MappableRingBuffer regularBuffer;
/*     */   
/*     */   public FogRenderer() {
/*  66 */     GpuDevice device = RenderSystem.getDevice();
/*  67 */     this.regularBuffer = new MappableRingBuffer(() -> "Fog UBO", 130, FOG_UBO_SIZE);
/*  69 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/*  70 */       ByteBuffer buffer = stack.malloc(FOG_UBO_SIZE);
/*  71 */       updateBuffer(buffer, 0, new Vector4f(0.0F), Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
/*  72 */       this.emptyBuffer = device.createBuffer(() -> "Empty fog", 128, buffer.flip());
/*  73 */       if (stack != null)
/*  73 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*  76 */     RenderSystem.setShaderFog(getBuffer(FogMode.NONE));
/*     */   }
/*     */   
/*     */   public void close() {
/*  81 */     this.emptyBuffer.close();
/*  82 */     this.regularBuffer.close();
/*     */   }
/*     */   
/*     */   public void endFrame() {
/*  86 */     this.regularBuffer.rotate();
/*     */   }
/*     */   
/*     */   public GpuBufferSlice getBuffer(FogMode mode) {
/*  90 */     if (!fogEnabled)
/*  91 */       return this.emptyBuffer.slice(0L, FOG_UBO_SIZE); 
/*  93 */     switch (mode.ordinal()) {
/*     */       default:
/*  93 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */         break;
/*     */     } 
/*  93 */     return 
/*     */       
/*  95 */       this.regularBuffer.currentBuffer().slice(0L, FOG_UBO_SIZE);
/*     */   }
/*     */   
/*     */   private void computeFogColor(Camera camera, float partialTicks, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector4f dest) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokevirtual getFogType : (Lnet/minecraft/client/Camera;)Lnet/minecraft/world/level/material/FogType;
/*     */     //   5: astore #7
/*     */     //   7: aload_1
/*     */     //   8: invokevirtual entity : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   11: astore #8
/*     */     //   13: aconst_null
/*     */     //   14: astore #9
/*     */     //   16: aconst_null
/*     */     //   17: astore #10
/*     */     //   19: getstatic net/minecraft/client/renderer/fog/FogRenderer.FOG_ENVIRONMENTS : Ljava/util/List;
/*     */     //   22: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   27: astore #11
/*     */     //   29: aload #11
/*     */     //   31: invokeinterface hasNext : ()Z
/*     */     //   36: ifeq -> 100
/*     */     //   39: aload #11
/*     */     //   41: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   46: checkcast net/minecraft/client/renderer/fog/environment/FogEnvironment
/*     */     //   49: astore #12
/*     */     //   51: aload #12
/*     */     //   53: aload #7
/*     */     //   55: aload #8
/*     */     //   57: invokevirtual isApplicable : (Lnet/minecraft/world/level/material/FogType;Lnet/minecraft/world/entity/Entity;)Z
/*     */     //   60: ifeq -> 97
/*     */     //   63: aload #9
/*     */     //   65: ifnonnull -> 80
/*     */     //   68: aload #12
/*     */     //   70: invokevirtual providesColor : ()Z
/*     */     //   73: ifeq -> 80
/*     */     //   76: aload #12
/*     */     //   78: astore #9
/*     */     //   80: aload #10
/*     */     //   82: ifnonnull -> 97
/*     */     //   85: aload #12
/*     */     //   87: invokevirtual modifiesDarkness : ()Z
/*     */     //   90: ifeq -> 97
/*     */     //   93: aload #12
/*     */     //   95: astore #10
/*     */     //   97: goto -> 29
/*     */     //   100: aload #9
/*     */     //   102: ifnonnull -> 115
/*     */     //   105: new java/lang/IllegalStateException
/*     */     //   108: dup
/*     */     //   109: ldc 'No color source environment found'
/*     */     //   111: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   114: athrow
/*     */     //   115: aload #9
/*     */     //   117: aload_3
/*     */     //   118: aload_1
/*     */     //   119: iload #4
/*     */     //   121: fload_2
/*     */     //   122: invokevirtual getBaseColor : (Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/Camera;IF)I
/*     */     //   125: istore #11
/*     */     //   127: aload_3
/*     */     //   128: invokevirtual getLevelData : ()Lnet/minecraft/client/multiplayer/ClientLevel$ClientLevelData;
/*     */     //   131: invokevirtual voidDarknessOnsetRange : ()F
/*     */     //   134: fstore #12
/*     */     //   136: fload #12
/*     */     //   138: aload_3
/*     */     //   139: invokevirtual getMinY : ()I
/*     */     //   142: i2f
/*     */     //   143: fadd
/*     */     //   144: aload_1
/*     */     //   145: invokevirtual position : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   148: getfield y : D
/*     */     //   151: d2f
/*     */     //   152: fsub
/*     */     //   153: fload #12
/*     */     //   155: fdiv
/*     */     //   156: fconst_0
/*     */     //   157: fconst_1
/*     */     //   158: invokestatic clamp : (FFF)F
/*     */     //   161: fstore #13
/*     */     //   163: aload #10
/*     */     //   165: ifnull -> 187
/*     */     //   168: aload #8
/*     */     //   170: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   173: astore #14
/*     */     //   175: aload #10
/*     */     //   177: aload #14
/*     */     //   179: fload #13
/*     */     //   181: fload_2
/*     */     //   182: invokevirtual getModifiedDarkness : (Lnet/minecraft/world/entity/LivingEntity;FF)F
/*     */     //   185: fstore #13
/*     */     //   187: iload #11
/*     */     //   189: invokestatic redFloat : (I)F
/*     */     //   192: fstore #14
/*     */     //   194: iload #11
/*     */     //   196: invokestatic greenFloat : (I)F
/*     */     //   199: fstore #15
/*     */     //   201: iload #11
/*     */     //   203: invokestatic blueFloat : (I)F
/*     */     //   206: fstore #16
/*     */     //   208: fload #13
/*     */     //   210: fconst_0
/*     */     //   211: fcmpl
/*     */     //   212: ifle -> 261
/*     */     //   215: aload #7
/*     */     //   217: getstatic net/minecraft/world/level/material/FogType.LAVA : Lnet/minecraft/world/level/material/FogType;
/*     */     //   220: if_acmpeq -> 261
/*     */     //   223: aload #7
/*     */     //   225: getstatic net/minecraft/world/level/material/FogType.POWDER_SNOW : Lnet/minecraft/world/level/material/FogType;
/*     */     //   228: if_acmpeq -> 261
/*     */     //   231: fconst_1
/*     */     //   232: fload #13
/*     */     //   234: fsub
/*     */     //   235: invokestatic square : (F)F
/*     */     //   238: fstore #17
/*     */     //   240: fload #14
/*     */     //   242: fload #17
/*     */     //   244: fmul
/*     */     //   245: fstore #14
/*     */     //   247: fload #15
/*     */     //   249: fload #17
/*     */     //   251: fmul
/*     */     //   252: fstore #15
/*     */     //   254: fload #16
/*     */     //   256: fload #17
/*     */     //   258: fmul
/*     */     //   259: fstore #16
/*     */     //   261: fload #5
/*     */     //   263: fconst_0
/*     */     //   264: fcmpl
/*     */     //   265: ifle -> 310
/*     */     //   268: fload #5
/*     */     //   270: fload #14
/*     */     //   272: fload #14
/*     */     //   274: ldc 0.7
/*     */     //   276: fmul
/*     */     //   277: invokestatic lerp : (FFF)F
/*     */     //   280: fstore #14
/*     */     //   282: fload #5
/*     */     //   284: fload #15
/*     */     //   286: fload #15
/*     */     //   288: ldc 0.6
/*     */     //   290: fmul
/*     */     //   291: invokestatic lerp : (FFF)F
/*     */     //   294: fstore #15
/*     */     //   296: fload #5
/*     */     //   298: fload #16
/*     */     //   300: fload #16
/*     */     //   302: ldc 0.6
/*     */     //   304: fmul
/*     */     //   305: invokestatic lerp : (FFF)F
/*     */     //   308: fstore #16
/*     */     //   310: aload #7
/*     */     //   312: getstatic net/minecraft/world/level/material/FogType.WATER : Lnet/minecraft/world/level/material/FogType;
/*     */     //   315: if_acmpne -> 349
/*     */     //   318: aload #8
/*     */     //   320: instanceof net/minecraft/client/player/LocalPlayer
/*     */     //   323: ifeq -> 343
/*     */     //   326: aload #8
/*     */     //   328: checkcast net/minecraft/client/player/LocalPlayer
/*     */     //   331: astore #19
/*     */     //   333: aload #19
/*     */     //   335: invokevirtual getWaterVision : ()F
/*     */     //   338: fstore #17
/*     */     //   340: goto -> 400
/*     */     //   343: fconst_1
/*     */     //   344: fstore #17
/*     */     //   346: goto -> 400
/*     */     //   349: aload #8
/*     */     //   351: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   354: ifeq -> 397
/*     */     //   357: aload #8
/*     */     //   359: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   362: astore #18
/*     */     //   364: aload #18
/*     */     //   366: getstatic net/minecraft/world/effect/MobEffects.NIGHT_VISION : Lnet/minecraft/core/Holder;
/*     */     //   369: invokevirtual hasEffect : (Lnet/minecraft/core/Holder;)Z
/*     */     //   372: ifeq -> 397
/*     */     //   375: aload #18
/*     */     //   377: getstatic net/minecraft/world/effect/MobEffects.DARKNESS : Lnet/minecraft/core/Holder;
/*     */     //   380: invokevirtual hasEffect : (Lnet/minecraft/core/Holder;)Z
/*     */     //   383: ifne -> 397
/*     */     //   386: aload #18
/*     */     //   388: fload_2
/*     */     //   389: invokestatic nightVisionScale : (Lnet/minecraft/world/entity/LivingEntity;F)F
/*     */     //   392: fstore #17
/*     */     //   394: goto -> 400
/*     */     //   397: fconst_0
/*     */     //   398: fstore #17
/*     */     //   400: fload #14
/*     */     //   402: fconst_0
/*     */     //   403: fcmpl
/*     */     //   404: ifeq -> 547
/*     */     //   407: fload #15
/*     */     //   409: fconst_0
/*     */     //   410: fcmpl
/*     */     //   411: ifeq -> 547
/*     */     //   414: fload #16
/*     */     //   416: fconst_0
/*     */     //   417: fcmpl
/*     */     //   418: ifeq -> 547
/*     */     //   421: fload #14
/*     */     //   423: fload #15
/*     */     //   425: fload #16
/*     */     //   427: invokestatic max : (FF)F
/*     */     //   430: invokestatic max : (FF)F
/*     */     //   433: fstore #18
/*     */     //   435: fconst_1
/*     */     //   436: fload #18
/*     */     //   438: ldc_w 0.07
/*     */     //   441: fconst_1
/*     */     //   442: invokestatic clamp : (FFF)F
/*     */     //   445: fdiv
/*     */     //   446: fstore #19
/*     */     //   448: fconst_1
/*     */     //   449: fload #18
/*     */     //   451: fdiv
/*     */     //   452: fstore #20
/*     */     //   454: fload #18
/*     */     //   456: fload #14
/*     */     //   458: fcmpl
/*     */     //   459: ifeq -> 467
/*     */     //   462: fload #19
/*     */     //   464: goto -> 469
/*     */     //   467: fload #20
/*     */     //   469: fstore #21
/*     */     //   471: fload #17
/*     */     //   473: fload #14
/*     */     //   475: fload #14
/*     */     //   477: fload #21
/*     */     //   479: fmul
/*     */     //   480: invokestatic lerp : (FFF)F
/*     */     //   483: fstore #14
/*     */     //   485: fload #18
/*     */     //   487: fload #15
/*     */     //   489: fcmpl
/*     */     //   490: ifeq -> 498
/*     */     //   493: fload #19
/*     */     //   495: goto -> 500
/*     */     //   498: fload #20
/*     */     //   500: fstore #21
/*     */     //   502: fload #17
/*     */     //   504: fload #15
/*     */     //   506: fload #15
/*     */     //   508: fload #21
/*     */     //   510: fmul
/*     */     //   511: invokestatic lerp : (FFF)F
/*     */     //   514: fstore #15
/*     */     //   516: fload #18
/*     */     //   518: fload #16
/*     */     //   520: fcmpl
/*     */     //   521: ifeq -> 529
/*     */     //   524: fload #19
/*     */     //   526: goto -> 531
/*     */     //   529: fload #20
/*     */     //   531: fstore #21
/*     */     //   533: fload #17
/*     */     //   535: fload #16
/*     */     //   537: fload #16
/*     */     //   539: fload #21
/*     */     //   541: fmul
/*     */     //   542: invokestatic lerp : (FFF)F
/*     */     //   545: fstore #16
/*     */     //   547: aload #6
/*     */     //   549: fload #14
/*     */     //   551: fload #15
/*     */     //   553: fload #16
/*     */     //   555: fconst_1
/*     */     //   556: invokevirtual set : (FFFF)Lorg/joml/Vector4f;
/*     */     //   559: pop
/*     */     //   560: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #100	-> 0
/*     */     //   #101	-> 7
/*     */     //   #103	-> 13
/*     */     //   #104	-> 16
/*     */     //   #105	-> 19
/*     */     //   #106	-> 51
/*     */     //   #107	-> 63
/*     */     //   #108	-> 76
/*     */     //   #110	-> 80
/*     */     //   #111	-> 93
/*     */     //   #114	-> 97
/*     */     //   #116	-> 100
/*     */     //   #117	-> 105
/*     */     //   #120	-> 115
/*     */     //   #122	-> 127
/*     */     //   #123	-> 136
/*     */     //   #124	-> 163
/*     */     //   #125	-> 168
/*     */     //   #126	-> 175
/*     */     //   #129	-> 187
/*     */     //   #130	-> 194
/*     */     //   #131	-> 201
/*     */     //   #133	-> 208
/*     */     //   #134	-> 231
/*     */     //   #135	-> 240
/*     */     //   #136	-> 247
/*     */     //   #137	-> 254
/*     */     //   #140	-> 261
/*     */     //   #141	-> 268
/*     */     //   #142	-> 282
/*     */     //   #143	-> 296
/*     */     //   #147	-> 310
/*     */     //   #148	-> 318
/*     */     //   #149	-> 333
/*     */     //   #151	-> 343
/*     */     //   #153	-> 349
/*     */     //   #154	-> 386
/*     */     //   #156	-> 397
/*     */     //   #159	-> 400
/*     */     //   #161	-> 421
/*     */     //   #162	-> 435
/*     */     //   #163	-> 448
/*     */     //   #166	-> 454
/*     */     //   #167	-> 471
/*     */     //   #169	-> 485
/*     */     //   #170	-> 502
/*     */     //   #172	-> 516
/*     */     //   #173	-> 533
/*     */     //   #176	-> 547
/*     */     //   #177	-> 560
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   51	46	12	fogEnvironment	Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;
/*     */     //   175	12	14	livingEntity	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   240	21	17	brightness	F
/*     */     //   340	3	17	brightenFactor	F
/*     */     //   333	10	19	localPlayer	Lnet/minecraft/client/player/LocalPlayer;
/*     */     //   346	3	17	brightenFactor	F
/*     */     //   394	3	17	brightenFactor	F
/*     */     //   364	33	18	livingEntity	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   435	112	18	maxColor	F
/*     */     //   448	99	19	targetScale	F
/*     */     //   454	93	20	targetScaleMax	F
/*     */     //   471	76	21	scale	F
/*     */     //   0	561	0	this	Lnet/minecraft/client/renderer/fog/FogRenderer;
/*     */     //   0	561	1	camera	Lnet/minecraft/client/Camera;
/*     */     //   0	561	2	partialTicks	F
/*     */     //   0	561	3	level	Lnet/minecraft/client/multiplayer/ClientLevel;
/*     */     //   0	561	4	renderDistance	I
/*     */     //   0	561	5	darkenWorldAmount	F
/*     */     //   0	561	6	dest	Lorg/joml/Vector4f;
/*     */     //   7	554	7	fogType	Lnet/minecraft/world/level/material/FogType;
/*     */     //   13	548	8	entity	Lnet/minecraft/world/entity/Entity;
/*     */     //   16	545	9	colorSourceEnvironment	Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;
/*     */     //   19	542	10	darknessModifyingEnvironment	Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;
/*     */     //   127	434	11	color	I
/*     */     //   136	425	12	voidDarknessOnsetRange	F
/*     */     //   163	398	13	darkness	F
/*     */     //   194	367	14	fogRed	F
/*     */     //   201	360	15	fogGreen	F
/*     */     //   208	353	16	fogBlue	F
/*     */     //   400	161	17	brightenFactor	F
/*     */   }
/*     */   
/*     */   public static boolean toggleFog() {
/* 180 */     return fogEnabled = !fogEnabled;
/*     */   }
/*     */   
/*     */   public FogData setupFog(Camera camera, int renderDistanceInChunks, DeltaTracker deltaTracker, float darkenWorldAmount, ClientLevel level) {
/* 184 */     float partialTickTime = deltaTracker.getGameTimeDeltaPartialTick(false);
/* 185 */     float renderDistanceInBlocks = (renderDistanceInChunks * 16);
/* 187 */     FogType fogType = getFogType(camera);
/* 188 */     Entity entity = camera.entity();
/* 189 */     FogData fog = new FogData();
/* 191 */     computeFogColor(camera, partialTickTime, level, renderDistanceInChunks, darkenWorldAmount, fog.color);
/* 193 */     for (FogEnvironment fogEnvironment : FOG_ENVIRONMENTS) {
/* 194 */       if (fogEnvironment.isApplicable(fogType, entity)) {
/* 195 */         fogEnvironment.setupFog(fog, camera, level, renderDistanceInBlocks, deltaTracker);
/*     */         break;
/*     */       } 
/*     */     } 
/* 200 */     float renderDistanceFogSpan = Mth.clamp(renderDistanceInBlocks / 10.0F, 4.0F, 64.0F);
/* 201 */     fog.renderDistanceStart = renderDistanceInBlocks - renderDistanceFogSpan;
/* 202 */     fog.renderDistanceEnd = renderDistanceInBlocks;
/* 204 */     return fog;
/*     */   }
/*     */   
/*     */   public void updateBuffer(FogData fog) {
/* 208 */     GpuBufferSlice.MappedView view = this.regularBuffer.currentBuffer().map(false, true);
/*     */     try {
/* 209 */       updateBuffer(view.data(), 0, fog.color, fog.environmentalStart, fog.environmentalEnd, fog.renderDistanceStart, fog.renderDistanceEnd, fog.skyEnd, fog.cloudEnd);
/* 210 */       if (view != null)
/* 210 */         view.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (view != null)
/*     */         try {
/*     */           view.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private FogType getFogType(Camera camera) {
/* 214 */     FogType blockFogType = camera.getFluidInCamera();
/* 215 */     if (blockFogType == FogType.NONE)
/* 216 */       return FogType.ATMOSPHERIC; 
/* 218 */     return blockFogType;
/*     */   }
/*     */   
/*     */   private void updateBuffer(ByteBuffer byteBuffer, int offset, Vector4f fogColor, float environmentalStart, float environmentalEnd, float renderDistanceStart, float renderDistanceEnd, float skyEnd, float endClouds) {
/* 222 */     byteBuffer.position(offset);
/* 223 */     Std140Builder.intoBuffer(byteBuffer)
/* 224 */       .putVec4((Vector4fc)fogColor)
/* 225 */       .putFloat(environmentalStart)
/* 226 */       .putFloat(environmentalEnd)
/* 227 */       .putFloat(renderDistanceStart)
/* 228 */       .putFloat(renderDistanceEnd)
/* 229 */       .putFloat(skyEnd)
/* 230 */       .putFloat(endClouds);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\fog\FogRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */