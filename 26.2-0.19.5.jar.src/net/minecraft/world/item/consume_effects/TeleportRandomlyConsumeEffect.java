/*    */ package net.minecraft.world.item.consume_effects;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class TeleportRandomlyConsumeEffect extends Record implements ConsumeEffect {
/*    */   private final float diameter;
/*    */   
/*    */   private static final float DEFAULT_DIAMETER = 16.0F;
/*    */   
/*    */   public static final MapCodec<TeleportRandomlyConsumeEffect> CODEC;
/*    */   
/*    */   public TeleportRandomlyConsumeEffect(float diameter) {
/* 22 */     this.diameter = diameter;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/consume_effects/TeleportRandomlyConsumeEffect;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/TeleportRandomlyConsumeEffect;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/consume_effects/TeleportRandomlyConsumeEffect;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/consume_effects/TeleportRandomlyConsumeEffect;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/consume_effects/TeleportRandomlyConsumeEffect;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/consume_effects/TeleportRandomlyConsumeEffect;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float diameter() {
/* 22 */     return this.diameter;
/*    */   }
/*    */   
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("diameter", Float.valueOf(16.0F)).forGetter(TeleportRandomlyConsumeEffect::diameter)).apply((Applicative)i, TeleportRandomlyConsumeEffect::new));
/*    */   }
/*    */   
/* 27 */   public static final StreamCodec<RegistryFriendlyByteBuf, TeleportRandomlyConsumeEffect> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, TeleportRandomlyConsumeEffect::diameter, TeleportRandomlyConsumeEffect::new);
/*    */   
/*    */   public TeleportRandomlyConsumeEffect() {
/* 33 */     this(16.0F);
/*    */   }
/*    */   
/*    */   public ConsumeEffect.Type<TeleportRandomlyConsumeEffect> getType() {
/* 38 */     return ConsumeEffect.Type.TELEPORT_RANDOMLY;
/*    */   }
/*    */   
/*    */   public boolean apply(Level level, ItemStack stack, LivingEntity user) {
/* 43 */     boolean teleported = false;
/* 44 */     for (int attempt = 0; attempt < 16; attempt++) {
/* 45 */       double xx = user.getX() + (user.getRandom().nextDouble() - 0.5D) * this.diameter;
/* 46 */       double yy = Mth.clamp(user.getY() + (user.getRandom().nextDouble() - 0.5D) * this.diameter, level.getMinY(), (level.getMinY() + ((ServerLevel)level).getLogicalHeight() - 1));
/* 47 */       double zz = user.getZ() + (user.getRandom().nextDouble() - 0.5D) * this.diameter;
/* 48 */       if (user.isPassenger())
/* 49 */         user.stopRiding(); 
/* 51 */       Vec3 oldPos = user.position();
/* 52 */       if (user.randomTeleport(xx, yy, zz, true)) {
/*    */         SoundSource soundSource;
/*    */         SoundEvent soundEvent;
/* 53 */         level.gameEvent((Holder)GameEvent.TELEPORT, oldPos, GameEvent.Context.of((Entity)user));
/* 57 */         if (user instanceof net.minecraft.world.entity.animal.fox.Fox) {
/* 58 */           soundEvent = SoundEvents.FOX_TELEPORT;
/* 59 */           soundSource = SoundSource.NEUTRAL;
/*    */         } else {
/* 61 */           soundEvent = SoundEvents.CHORUS_FRUIT_TELEPORT;
/* 62 */           soundSource = SoundSource.PLAYERS;
/*    */         } 
/* 64 */         level.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, soundSource);
/* 65 */         user.resetFallDistance();
/* 66 */         teleported = true;
/*    */         break;
/*    */       } 
/*    */     } 
/* 71 */     if (teleported)
/* 72 */       user.resetCurrentImpulseContext(); 
/* 75 */     return teleported;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\consume_effects\TeleportRandomlyConsumeEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */