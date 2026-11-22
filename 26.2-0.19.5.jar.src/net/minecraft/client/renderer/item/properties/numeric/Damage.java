/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class Damage extends Record implements RangeSelectItemModelProperty {
/*    */   private final boolean normalize;
/*    */   
/*    */   public static final MapCodec<Damage> MAP_CODEC;
/*    */   
/*    */   public Damage(boolean normalize) {
/* 12 */     this.normalize = normalize;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/numeric/Damage;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/Damage;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/numeric/Damage;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/Damage;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/numeric/Damage;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/Damage;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean normalize() {
/* 12 */     return this.normalize;
/*    */   }
/*    */   
/*    */   static {
/* 13 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.optionalFieldOf("normalize", Boolean.valueOf(true)).forGetter(Damage::normalize)).apply((Applicative)i, Damage::new));
/*    */   }
/*    */   
/*    */   public float get(ItemStack itemStack, ClientLevel level, ItemOwner owner, int seed) {
/* 19 */     float damage = itemStack.getDamageValue();
/* 20 */     float maxDamage = itemStack.getMaxDamage();
/* 22 */     if (this.normalize)
/* 23 */       return Mth.clamp(damage / maxDamage, 0.0F, 1.0F); 
/* 25 */     return Mth.clamp(damage, 0.0F, maxDamage);
/*    */   }
/*    */   
/*    */   public MapCodec<Damage> type() {
/* 30 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\numeric\Damage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */