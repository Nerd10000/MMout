/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class Partial extends Record {
/*     */   private final Optional<String> name;
/*     */   
/*     */   private final Optional<UUID> id;
/*     */   
/*     */   private final PropertyMap properties;
/*     */   
/*     */   protected Partial(Optional<String> name, Optional<UUID> id, PropertyMap properties) {
/* 112 */     this.name = name;
/* 112 */     this.id = id;
/* 112 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/ResolvableProfile$Partial;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/ResolvableProfile$Partial;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/ResolvableProfile$Partial;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/component/ResolvableProfile$Partial;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/ResolvableProfile$Partial;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/component/ResolvableProfile$Partial;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<String> name() {
/* 112 */     return this.name;
/*     */   }
/*     */   
/*     */   public Optional<UUID> id() {
/* 112 */     return this.id;
/*     */   }
/*     */   
/*     */   public PropertyMap properties() {
/* 112 */     return this.properties;
/*     */   }
/*     */   
/* 117 */   public static final Partial EMPTY = new Partial(Optional.empty(), Optional.empty(), PropertyMap.EMPTY);
/*     */   
/*     */   private static final MapCodec<Partial> MAP_CODEC;
/*     */   
/*     */   static {
/* 120 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.PLAYER_NAME.optionalFieldOf("name").forGetter(Partial::name), (App)UUIDUtil.CODEC.optionalFieldOf("id").forGetter(Partial::id), (App)ExtraCodecs.PROPERTY_MAP.optionalFieldOf("properties", PropertyMap.EMPTY).forGetter(Partial::properties)).apply((Applicative)i, Partial::new));
/*     */   }
/*     */   
/* 126 */   public static final StreamCodec<ByteBuf, Partial> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.PLAYER_NAME
/* 127 */       .apply(ByteBufCodecs::optional), Partial::name, UUIDUtil.STREAM_CODEC
/* 128 */       .apply(ByteBufCodecs::optional), Partial::id, ByteBufCodecs.GAME_PROFILE_PROPERTIES, Partial::properties, Partial::new);
/*     */   
/*     */   private GameProfile createProfile() {
/* 134 */     return ResolvableProfile.createPartialProfile(this.name, this.id, this.properties);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\ResolvableProfile$Partial.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */