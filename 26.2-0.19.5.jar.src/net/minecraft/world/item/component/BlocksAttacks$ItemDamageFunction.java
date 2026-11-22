/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class ItemDamageFunction extends Record {
/*     */   private final float threshold;
/*     */   
/*     */   private final float base;
/*     */   
/*     */   private final float factor;
/*     */   
/*     */   public static final Codec<ItemDamageFunction> CODEC;
/*     */   
/*     */   public ItemDamageFunction(float threshold, float base, float factor) {
/* 147 */     this.threshold = threshold;
/* 147 */     this.base = base;
/* 147 */     this.factor = factor;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #147	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #147	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #147	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/BlocksAttacks$ItemDamageFunction;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float threshold() {
/* 147 */     return this.threshold;
/*     */   }
/*     */   
/*     */   public float base() {
/* 147 */     return this.base;
/*     */   }
/*     */   
/*     */   public float factor() {
/* 147 */     return this.factor;
/*     */   }
/*     */   
/*     */   static {
/* 148 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("threshold").forGetter(ItemDamageFunction::threshold), (App)Codec.FLOAT.fieldOf("base").forGetter(ItemDamageFunction::base), (App)Codec.FLOAT.fieldOf("factor").forGetter(ItemDamageFunction::factor)).apply((Applicative)i, ItemDamageFunction::new));
/*     */   }
/*     */   
/* 153 */   public static final StreamCodec<ByteBuf, ItemDamageFunction> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, ItemDamageFunction::threshold, ByteBufCodecs.FLOAT, ItemDamageFunction::base, ByteBufCodecs.FLOAT, ItemDamageFunction::factor, ItemDamageFunction::new);
/*     */   
/* 160 */   public static final ItemDamageFunction DEFAULT = new ItemDamageFunction(1.0F, 0.0F, 1.0F);
/*     */   
/*     */   public int apply(float dealtDamage) {
/* 163 */     if (dealtDamage < this.threshold)
/* 164 */       return 0; 
/* 166 */     return Mth.floor(this.base + this.factor * dealtDamage);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\BlocksAttacks$ItemDamageFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */