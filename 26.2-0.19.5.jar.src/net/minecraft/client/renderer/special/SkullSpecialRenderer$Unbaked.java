/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.model.object.skull.SkullModelBase;
/*    */ import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.block.SkullBlock;
/*    */ 
/*    */ public final class Unbaked extends Record implements NoDataSpecialModelRenderer.Unbaked {
/*    */   private final SkullBlock.Type kind;
/*    */   
/*    */   private final Optional<Identifier> textureOverride;
/*    */   
/*    */   private final float animation;
/*    */   
/*    */   public static final MapCodec<Unbaked> MAP_CODEC;
/*    */   
/*    */   public Unbaked(SkullBlock.Type kind, Optional<Identifier> textureOverride, float animation) {
/* 30 */     this.kind = kind;
/* 30 */     this.textureOverride = textureOverride;
/* 30 */     this.animation = animation;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/SkullSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/special/SkullSpecialRenderer$Unbaked;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/SkullSpecialRenderer$Unbaked;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/special/SkullSpecialRenderer$Unbaked;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/SkullSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/special/SkullSpecialRenderer$Unbaked;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public SkullBlock.Type kind() {
/* 30 */     return this.kind;
/*    */   }
/*    */   
/*    */   public Optional<Identifier> textureOverride() {
/* 30 */     return this.textureOverride;
/*    */   }
/*    */   
/*    */   public float animation() {
/* 30 */     return this.animation;
/*    */   }
/*    */   
/*    */   static {
/* 31 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SkullBlock.Type.CODEC.fieldOf("kind").forGetter(Unbaked::kind), (App)Identifier.CODEC.optionalFieldOf("texture").forGetter(Unbaked::textureOverride), (App)Codec.FLOAT.optionalFieldOf("animation", Float.valueOf(0.0F)).forGetter(Unbaked::animation)).apply((Applicative)i, Unbaked::new));
/*    */   }
/*    */   
/*    */   public Unbaked(SkullBlock.Type kind) {
/* 38 */     this(kind, Optional.empty(), 0.0F);
/*    */   }
/*    */   
/*    */   public MapCodec<Unbaked> type() {
/* 43 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public SkullSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
/* 48 */     SkullModelBase model = SkullBlockRenderer.createModel(context.entityModelSet(), this.kind);
/* 49 */     Identifier textureOverride = this.textureOverride.<Identifier>map(t -> t.withPath(())).orElse(null);
/* 51 */     if (model == null)
/* 52 */       return null; 
/* 55 */     RenderType renderType = SkullBlockRenderer.getSkullRenderType(this.kind, textureOverride);
/* 56 */     return new SkullSpecialRenderer(model, this.animation, renderType);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\SkullSpecialRenderer$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */