/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class UseCooldown extends Record {
/*    */   private final float seconds;
/*    */   
/*    */   private final Optional<Identifier> cooldownGroup;
/*    */   
/*    */   public static final Codec<UseCooldown> CODEC;
/*    */   
/*    */   public UseCooldown(float seconds, Optional<Identifier> cooldownGroup) {
/* 17 */     this.seconds = seconds;
/* 17 */     this.cooldownGroup = cooldownGroup;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/UseCooldown;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/UseCooldown;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/UseCooldown;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/UseCooldown;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/UseCooldown;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/UseCooldown;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float seconds() {
/* 17 */     return this.seconds;
/*    */   }
/*    */   
/*    */   public Optional<Identifier> cooldownGroup() {
/* 17 */     return this.cooldownGroup;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.POSITIVE_FLOAT.fieldOf("seconds").forGetter(UseCooldown::seconds), (App)Identifier.CODEC.optionalFieldOf("cooldown_group").forGetter(UseCooldown::cooldownGroup)).apply((Applicative)i, UseCooldown::new));
/*    */   }
/*    */   
/* 22 */   public static final StreamCodec<RegistryFriendlyByteBuf, UseCooldown> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, UseCooldown::seconds, Identifier.STREAM_CODEC
/*    */       
/* 24 */       .apply(ByteBufCodecs::optional), UseCooldown::cooldownGroup, UseCooldown::new);
/*    */   
/*    */   public UseCooldown(float seconds) {
/* 29 */     this(seconds, Optional.empty());
/*    */   }
/*    */   
/*    */   public int ticks() {
/* 33 */     return (int)(this.seconds * 20.0F);
/*    */   }
/*    */   
/*    */   public void apply(ItemStack stack, LivingEntity user) {
/* 37 */     if (user instanceof Player) {
/* 37 */       Player player = (Player)user;
/* 38 */       player.getCooldowns().addCooldown(stack, ticks());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\UseCooldown.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */