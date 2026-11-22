/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Set;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class TextureInput extends Record implements PostChainConfig.Input {
/*     */   private final String samplerName;
/*     */   
/*     */   private final Identifier location;
/*     */   
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final boolean bilinear;
/*     */   
/*     */   public static final Codec<TextureInput> CODEC;
/*     */   
/*     */   public TextureInput(String samplerName, Identifier location, int width, int height, boolean bilinear) {
/*  87 */     this.samplerName = samplerName;
/*  87 */     this.location = location;
/*  87 */     this.width = width;
/*  87 */     this.height = height;
/*  87 */     this.bilinear = bilinear;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #87	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #87	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #87	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String samplerName() {
/*  87 */     return this.samplerName;
/*     */   }
/*     */   
/*     */   public Identifier location() {
/*  87 */     return this.location;
/*     */   }
/*     */   
/*     */   public int width() {
/*  87 */     return this.width;
/*     */   }
/*     */   
/*     */   public int height() {
/*  87 */     return this.height;
/*     */   }
/*     */   
/*     */   public boolean bilinear() {
/*  87 */     return this.bilinear;
/*     */   }
/*     */   
/*     */   static {
/*  94 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("sampler_name").forGetter(TextureInput::samplerName), (App)Identifier.CODEC.fieldOf("location").forGetter(TextureInput::location), (App)ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(TextureInput::width), (App)ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(TextureInput::height), (App)Codec.BOOL.optionalFieldOf("bilinear", Boolean.valueOf(false)).forGetter(TextureInput::bilinear)).apply((Applicative)i, TextureInput::new));
/*     */   }
/*     */   
/*     */   public Set<Identifier> referencedTargets() {
/* 104 */     return Set.of();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PostChainConfig$TextureInput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */