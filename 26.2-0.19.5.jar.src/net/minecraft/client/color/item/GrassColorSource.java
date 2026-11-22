/*    */ package net.minecraft.client.color.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.GrassColor;
/*    */ 
/*    */ public final class GrassColorSource extends Record implements ItemTintSource {
/*    */   private final float temperature;
/*    */   
/*    */   private final float downfall;
/*    */   
/*    */   public static final MapCodec<GrassColorSource> MAP_CODEC;
/*    */   
/*    */   public GrassColorSource(float temperature, float downfall) {
/* 12 */     this.temperature = temperature;
/* 12 */     this.downfall = downfall;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/color/item/GrassColorSource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/color/item/GrassColorSource;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/color/item/GrassColorSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/color/item/GrassColorSource;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/color/item/GrassColorSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/color/item/GrassColorSource;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float temperature() {
/* 12 */     return this.temperature;
/*    */   }
/*    */   
/*    */   public float downfall() {
/* 12 */     return this.downfall;
/*    */   }
/*    */   
/*    */   public GrassColorSource() {
/* 17 */     this(0.5F, 1.0F);
/*    */   }
/*    */   
/*    */   static {
/* 20 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("temperature").forGetter(GrassColorSource::temperature), (App)ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("downfall").forGetter(GrassColorSource::downfall)).apply((Applicative)i, GrassColorSource::new));
/*    */   }
/*    */   
/*    */   public int calculate(ItemStack itemStack, ClientLevel level, LivingEntity owner) {
/* 27 */     return GrassColor.get(this.temperature, this.downfall);
/*    */   }
/*    */   
/*    */   public MapCodec<GrassColorSource> type() {
/* 32 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\color\item\GrassColorSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */