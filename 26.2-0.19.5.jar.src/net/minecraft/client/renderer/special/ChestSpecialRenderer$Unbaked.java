/*    */ package net.minecraft.client.renderer.special;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.object.chest.ChestModel;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.blockentity.ChestRenderer;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteId;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.block.state.properties.ChestType;
/*    */ 
/*    */ public final class Unbaked extends Record implements NoDataSpecialModelRenderer.Unbaked {
/*    */   private final Identifier texture;
/*    */   
/*    */   private final float openness;
/*    */   
/*    */   private final ChestType chestType;
/*    */   
/*    */   public static final MapCodec<Unbaked> MAP_CODEC;
/*    */   
/*    */   public Unbaked(Identifier texture, float openness, ChestType chestType) {
/* 55 */     this.texture = texture;
/* 55 */     this.openness = openness;
/* 55 */     this.chestType = chestType;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/special/ChestSpecialRenderer$Unbaked;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #55	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/special/ChestSpecialRenderer$Unbaked;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/special/ChestSpecialRenderer$Unbaked;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #55	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/special/ChestSpecialRenderer$Unbaked;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/special/ChestSpecialRenderer$Unbaked;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #55	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/special/ChestSpecialRenderer$Unbaked;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier texture() {
/* 55 */     return this.texture;
/*    */   }
/*    */   
/*    */   public float openness() {
/* 55 */     return this.openness;
/*    */   }
/*    */   
/*    */   public ChestType chestType() {
/* 55 */     return this.chestType;
/*    */   }
/*    */   
/*    */   static {
/* 56 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("texture").forGetter(Unbaked::texture), (App)Codec.FLOAT.optionalFieldOf("openness", Float.valueOf(0.0F)).forGetter(Unbaked::openness), (App)ChestType.CODEC.optionalFieldOf("chest_type", ChestType.SINGLE).forGetter(Unbaked::chestType)).apply((Applicative)i, Unbaked::new));
/*    */   }
/*    */   
/*    */   public Unbaked(Identifier texture, ChestType chestType) {
/* 64 */     this(texture, 0.0F, chestType);
/*    */   }
/*    */   
/*    */   public Unbaked(Identifier texture) {
/* 68 */     this(texture, 0.0F, ChestType.SINGLE);
/*    */   }
/*    */   
/*    */   public MapCodec<Unbaked> type() {
/* 73 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ChestSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
/* 78 */     ChestModel model = new ChestModel(context.entityModelSet().bakeLayer((ModelLayerLocation)ChestRenderer.LAYERS.select(this.chestType)));
/* 79 */     SpriteId fullTexture = Sheets.CHEST_MAPPER.apply(this.texture);
/* 81 */     return new ChestSpecialRenderer(context.sprites(), model, fullTexture, this.openness);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\special\ChestSpecialRenderer$Unbaked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */