/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ 
/*     */ public class ClientboundUpdateMobEffectPacket implements Packet<ClientGamePacketListener> {
/*  13 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateMobEffectPacket> STREAM_CODEC = Packet.codec(ClientboundUpdateMobEffectPacket::write, ClientboundUpdateMobEffectPacket::new);
/*     */   
/*     */   private static final int FLAG_AMBIENT = 1;
/*     */   
/*     */   private static final int FLAG_VISIBLE = 2;
/*     */   
/*     */   private static final int FLAG_SHOW_ICON = 4;
/*     */   
/*     */   private static final int FLAG_BLEND = 8;
/*     */   
/*     */   private final int entityId;
/*     */   
/*     */   private final Holder<MobEffect> effect;
/*     */   
/*     */   private final int effectAmplifier;
/*     */   
/*     */   private final int effectDurationTicks;
/*     */   
/*     */   private final byte flags;
/*     */   
/*     */   public ClientboundUpdateMobEffectPacket(int entityId, MobEffectInstance effect, boolean blend) {
/*  27 */     this.entityId = entityId;
/*  28 */     this.effect = effect.getEffect();
/*  29 */     this.effectAmplifier = effect.getAmplifier();
/*  30 */     this.effectDurationTicks = effect.getDuration();
/*  31 */     byte flags = 0;
/*  33 */     if (effect.isAmbient())
/*  34 */       flags = (byte)(flags | 0x1); 
/*  36 */     if (effect.isVisible())
/*  37 */       flags = (byte)(flags | 0x2); 
/*  39 */     if (effect.showIcon())
/*  40 */       flags = (byte)(flags | 0x4); 
/*  42 */     if (blend)
/*  43 */       flags = (byte)(flags | 0x8); 
/*  45 */     this.flags = flags;
/*     */   }
/*     */   
/*     */   private ClientboundUpdateMobEffectPacket(RegistryFriendlyByteBuf input) {
/*  49 */     this.entityId = input.readVarInt();
/*  50 */     this.effect = (Holder<MobEffect>)MobEffect.STREAM_CODEC.decode(input);
/*  51 */     this.effectAmplifier = input.readVarInt();
/*  52 */     this.effectDurationTicks = input.readVarInt();
/*  53 */     this.flags = input.readByte();
/*     */   }
/*     */   
/*     */   private void write(RegistryFriendlyByteBuf output) {
/*  57 */     output.writeVarInt(this.entityId);
/*  58 */     MobEffect.STREAM_CODEC.encode(output, this.effect);
/*  59 */     output.writeVarInt(this.effectAmplifier);
/*  60 */     output.writeVarInt(this.effectDurationTicks);
/*  61 */     output.writeByte(this.flags);
/*     */   }
/*     */   
/*     */   public PacketType<ClientboundUpdateMobEffectPacket> type() {
/*  66 */     return GamePacketTypes.CLIENTBOUND_UPDATE_MOB_EFFECT;
/*     */   }
/*     */   
/*     */   public void handle(ClientGamePacketListener listener) {
/*  71 */     listener.handleUpdateMobEffect(this);
/*     */   }
/*     */   
/*     */   public int getEntityId() {
/*  75 */     return this.entityId;
/*     */   }
/*     */   
/*     */   public Holder<MobEffect> getEffect() {
/*  79 */     return this.effect;
/*     */   }
/*     */   
/*     */   public int getEffectAmplifier() {
/*  83 */     return this.effectAmplifier;
/*     */   }
/*     */   
/*     */   public int getEffectDurationTicks() {
/*  87 */     return this.effectDurationTicks;
/*     */   }
/*     */   
/*     */   public boolean isEffectVisible() {
/*  91 */     return ((this.flags & 0x2) != 0);
/*     */   }
/*     */   
/*     */   public boolean isEffectAmbient() {
/*  95 */     return ((this.flags & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public boolean effectShowsIcon() {
/*  99 */     return ((this.flags & 0x4) != 0);
/*     */   }
/*     */   
/*     */   public boolean shouldBlend() {
/* 103 */     return ((this.flags & 0x8) != 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundUpdateMobEffectPacket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */