/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class Weapon extends Record {
/*    */   private final int itemDamagePerAttack;
/*    */   
/*    */   private final float disableBlockingForSeconds;
/*    */   
/*    */   public static final float AXE_DISABLES_BLOCKING_FOR_SECONDS = 5.0F;
/*    */   
/*    */   public static final Codec<Weapon> CODEC;
/*    */   
/*    */   public Weapon(int itemDamagePerAttack, float disableBlockingForSeconds) {
/* 10 */     this.itemDamagePerAttack = itemDamagePerAttack;
/* 10 */     this.disableBlockingForSeconds = disableBlockingForSeconds;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/Weapon;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/Weapon;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/Weapon;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/Weapon;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/Weapon;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/Weapon;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int itemDamagePerAttack() {
/* 10 */     return this.itemDamagePerAttack;
/*    */   }
/*    */   
/*    */   public float disableBlockingForSeconds() {
/* 10 */     return this.disableBlockingForSeconds;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("item_damage_per_attack", Integer.valueOf(1)).forGetter(Weapon::itemDamagePerAttack), (App)ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("disable_blocking_for_seconds", Float.valueOf(0.0F)).forGetter(Weapon::disableBlockingForSeconds)).apply((Applicative)i, Weapon::new));
/*    */   }
/*    */   
/* 21 */   public static final StreamCodec<RegistryFriendlyByteBuf, Weapon> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, Weapon::itemDamagePerAttack, ByteBufCodecs.FLOAT, Weapon::disableBlockingForSeconds, Weapon::new);
/*    */   
/*    */   public Weapon(int damagePerAttack) {
/* 28 */     this(damagePerAttack, 0.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\Weapon.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */