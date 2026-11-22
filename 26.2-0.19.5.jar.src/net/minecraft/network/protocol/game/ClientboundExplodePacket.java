/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.particles.ExplosionParticleInfo;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ClientboundExplodePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Vec3 center;
/*    */   
/*    */   private final float radius;
/*    */   
/*    */   private final int blockCount;
/*    */   
/*    */   private final Optional<Vec3> playerKnockback;
/*    */   
/*    */   private final ParticleOptions explosionParticle;
/*    */   
/*    */   private final Holder<SoundEvent> explosionSound;
/*    */   
/*    */   private final WeightedList<ExplosionParticleInfo> blockParticles;
/*    */   
/*    */   public ClientboundExplodePacket(Vec3 center, float radius, int blockCount, Optional<Vec3> playerKnockback, ParticleOptions explosionParticle, Holder<SoundEvent> explosionSound, WeightedList<ExplosionParticleInfo> blockParticles) {
/* 18 */     this.center = center;
/* 18 */     this.radius = radius;
/* 18 */     this.blockCount = blockCount;
/* 18 */     this.playerKnockback = playerKnockback;
/* 18 */     this.explosionParticle = explosionParticle;
/* 18 */     this.explosionSound = explosionSound;
/* 18 */     this.blockParticles = blockParticles;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vec3 center() {
/* 18 */     return this.center;
/*    */   }
/*    */   
/*    */   public float radius() {
/* 18 */     return this.radius;
/*    */   }
/*    */   
/*    */   public int blockCount() {
/* 18 */     return this.blockCount;
/*    */   }
/*    */   
/*    */   public Optional<Vec3> playerKnockback() {
/* 18 */     return this.playerKnockback;
/*    */   }
/*    */   
/*    */   public ParticleOptions explosionParticle() {
/* 18 */     return this.explosionParticle;
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> explosionSound() {
/* 18 */     return this.explosionSound;
/*    */   }
/*    */   
/*    */   public WeightedList<ExplosionParticleInfo> blockParticles() {
/* 18 */     return this.blockParticles;
/*    */   }
/*    */   
/* 27 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundExplodePacket> STREAM_CODEC = StreamCodec.composite(Vec3.STREAM_CODEC, ClientboundExplodePacket::center, ByteBufCodecs.FLOAT, ClientboundExplodePacket::radius, ByteBufCodecs.INT, ClientboundExplodePacket::blockCount, Vec3.STREAM_CODEC
/*    */       
/* 31 */       .apply(ByteBufCodecs::optional), ClientboundExplodePacket::playerKnockback, ParticleTypes.STREAM_CODEC, ClientboundExplodePacket::explosionParticle, SoundEvent.STREAM_CODEC, ClientboundExplodePacket::explosionSound, 
/*    */       
/* 34 */       WeightedList.streamCodec(ExplosionParticleInfo.STREAM_CODEC), ClientboundExplodePacket::blockParticles, ClientboundExplodePacket::new);
/*    */   
/*    */   public PacketType<ClientboundExplodePacket> type() {
/* 40 */     return GamePacketTypes.CLIENTBOUND_EXPLODE;
/*    */   }
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 45 */     listener.handleExplosion(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundExplodePacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */