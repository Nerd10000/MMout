/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.client.renderer.blockentity.BannerRenderer;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.BannerBlock;
/*    */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*    */ 
/*    */ public final class Unbaked extends Record implements SpecialModelRenderer.Unbaked<BannerPatternLayers> {
/*    */   private final DyeColor baseColor;
/*    */   
/*    */   private final BannerBlock.AttachmentType attachment;
/*    */   
/*    */   public static final MapCodec<Unbaked> MAP_CODEC;
/*    */   
/*    */   public Unbaked(DyeColor baseColor, BannerBlock.AttachmentType attachment) {
/* 30 */     this.baseColor = baseColor;
/* 30 */     this.attachment = attachment;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/BannerSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/special/BannerSpecialRenderer$Unbaked;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/BannerSpecialRenderer$Unbaked;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/special/BannerSpecialRenderer$Unbaked;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/BannerSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #30	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/special/BannerSpecialRenderer$Unbaked;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public DyeColor baseColor() {
/* 30 */     return this.baseColor;
/*    */   }
/*    */   
/*    */   public BannerBlock.AttachmentType attachment() {
/* 30 */     return this.attachment;
/*    */   }
/*    */   
/*    */   static {
/* 31 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DyeColor.CODEC.fieldOf("color").forGetter(Unbaked::baseColor), (App)BannerBlock.AttachmentType.CODEC.optionalFieldOf("attachment", BannerBlock.AttachmentType.GROUND).forGetter(Unbaked::attachment)).apply((Applicative)i, Unbaked::new));
/*    */   }
/*    */   
/*    */   public MapCodec<Unbaked> type() {
/* 38 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public BannerSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
/* 43 */     return new BannerSpecialRenderer(this.baseColor, new BannerRenderer(context), this.attachment);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\BannerSpecialRenderer$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */