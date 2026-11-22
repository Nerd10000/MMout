/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Set;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public final class TargetInput extends Record implements PostChainConfig.Input {
/*     */   private final String samplerName;
/*     */   
/*     */   private final Identifier targetId;
/*     */   
/*     */   private final boolean useDepthBuffer;
/*     */   
/*     */   private final boolean bilinear;
/*     */   
/*     */   public static final Codec<TargetInput> CODEC;
/*     */   
/*     */   public TargetInput(String samplerName, Identifier targetId, boolean useDepthBuffer, boolean bilinear) {
/* 108 */     this.samplerName = samplerName;
/* 108 */     this.targetId = targetId;
/* 108 */     this.useDepthBuffer = useDepthBuffer;
/* 108 */     this.bilinear = bilinear;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #108	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #108	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #108	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String samplerName() {
/* 108 */     return this.samplerName;
/*     */   }
/*     */   
/*     */   public Identifier targetId() {
/* 108 */     return this.targetId;
/*     */   }
/*     */   
/*     */   public boolean useDepthBuffer() {
/* 108 */     return this.useDepthBuffer;
/*     */   }
/*     */   
/*     */   public boolean bilinear() {
/* 108 */     return this.bilinear;
/*     */   }
/*     */   
/*     */   static {
/* 114 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("sampler_name").forGetter(TargetInput::samplerName), (App)Identifier.CODEC.fieldOf("target").forGetter(TargetInput::targetId), (App)Codec.BOOL.optionalFieldOf("use_depth_buffer", Boolean.valueOf(false)).forGetter(TargetInput::useDepthBuffer), (App)Codec.BOOL.optionalFieldOf("bilinear", Boolean.valueOf(false)).forGetter(TargetInput::bilinear)).apply((Applicative)i, TargetInput::new));
/*     */   }
/*     */   
/*     */   public Set<Identifier> referencedTargets() {
/* 123 */     return Set.of(this.targetId);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PostChainConfig$TargetInput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */