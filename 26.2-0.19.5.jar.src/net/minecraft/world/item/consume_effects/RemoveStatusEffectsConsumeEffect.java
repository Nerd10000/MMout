/*    */ package net.minecraft.world.item.consume_effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class RemoveStatusEffectsConsumeEffect extends Record implements ConsumeEffect {
/*    */   private final HolderSet<MobEffect> effects;
/*    */   
/*    */   public static final MapCodec<RemoveStatusEffectsConsumeEffect> CODEC;
/*    */   
/*    */   public RemoveStatusEffectsConsumeEffect(HolderSet<MobEffect> effects) {
/* 17 */     this.effects = effects;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/consume_effects/RemoveStatusEffectsConsumeEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/RemoveStatusEffectsConsumeEffect;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/consume_effects/RemoveStatusEffectsConsumeEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/RemoveStatusEffectsConsumeEffect;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/consume_effects/RemoveStatusEffectsConsumeEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/consume_effects/RemoveStatusEffectsConsumeEffect;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public HolderSet<MobEffect> effects() {
/* 17 */     return this.effects;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.MOB_EFFECT).fieldOf("effects").forGetter(RemoveStatusEffectsConsumeEffect::effects)).apply((Applicative)i, RemoveStatusEffectsConsumeEffect::new));
/*    */   }
/*    */   
/* 21 */   public static final StreamCodec<RegistryFriendlyByteBuf, RemoveStatusEffectsConsumeEffect> STREAM_CODEC = StreamCodec.composite(
/* 22 */       ByteBufCodecs.holderSet(Registries.MOB_EFFECT), RemoveStatusEffectsConsumeEffect::effects, RemoveStatusEffectsConsumeEffect::new);
/*    */   
/*    */   public RemoveStatusEffectsConsumeEffect(Holder<MobEffect> only) {
/* 27 */     this((HolderSet<MobEffect>)HolderSet.direct(new Holder[] { only }));
/*    */   }
/*    */   
/*    */   public ConsumeEffect.Type<RemoveStatusEffectsConsumeEffect> getType() {
/* 32 */     return ConsumeEffect.Type.REMOVE_EFFECTS;
/*    */   }
/*    */   
/*    */   public boolean apply(Level level, ItemStack stack, LivingEntity user) {
/* 37 */     boolean hasRemovedAny = false;
/* 38 */     for (Holder<MobEffect> effect : this.effects) {
/* 39 */       if (user.removeEffect(effect))
/* 40 */         hasRemovedAny = true; 
/*    */     } 
/* 44 */     return hasRemovedAny;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\consume_effects\RemoveStatusEffectsConsumeEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */