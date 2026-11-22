/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public final class Unbaked extends Record implements NoDataSpecialModelRenderer.Unbaked {
/*    */   private final Identifier texture;
/*    */   
/*    */   private final float openness;
/*    */   
/*    */   public static final MapCodec<Unbaked> MAP_CODEC;
/*    */   
/*    */   public Unbaked(Identifier texture, float openness) {
/* 28 */     this.texture = texture;
/* 28 */     this.openness = openness;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/ShulkerBoxSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/special/ShulkerBoxSpecialRenderer$Unbaked;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/ShulkerBoxSpecialRenderer$Unbaked;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/special/ShulkerBoxSpecialRenderer$Unbaked;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/ShulkerBoxSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/special/ShulkerBoxSpecialRenderer$Unbaked;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier texture() {
/* 28 */     return this.texture;
/*    */   }
/*    */   
/*    */   public float openness() {
/* 28 */     return this.openness;
/*    */   }
/*    */   
/*    */   static {
/* 29 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("texture").forGetter(Unbaked::texture), (App)Codec.FLOAT.optionalFieldOf("openness", Float.valueOf(0.0F)).forGetter(Unbaked::openness)).apply((Applicative)i, Unbaked::new));
/*    */   }
/*    */   
/*    */   public Unbaked() {
/* 36 */     this(Identifier.withDefaultNamespace("shulker"), 0.0F);
/*    */   }
/*    */   
/*    */   public Unbaked(DyeColor color) {
/* 40 */     this(Sheets.colorToShulkerSprite(color), 0.0F);
/*    */   }
/*    */   
/*    */   public MapCodec<Unbaked> type() {
/* 45 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ShulkerBoxSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
/* 50 */     return new ShulkerBoxSpecialRenderer(new ShulkerBoxRenderer(context), this.openness, Sheets.SHULKER_MAPPER.apply(this.texture));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\ShulkerBoxSpecialRenderer$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */