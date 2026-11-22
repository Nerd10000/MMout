/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.gametest.framework.GameTestInstance;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ 
/*     */ public final class Data extends Record {
/*     */   private final Optional<ResourceKey<GameTestInstance>> test;
/*     */   
/*     */   private final Vec3i size;
/*     */   
/*     */   private final Rotation rotation;
/*     */   
/*     */   private final boolean ignoreEntities;
/*     */   
/*     */   private final TestInstanceBlockEntity.Status status;
/*     */   
/*     */   private final Optional<Component> errorMessage;
/*     */   
/*     */   public static final Codec<Data> CODEC;
/*     */   
/*     */   public Data(Optional<ResourceKey<GameTestInstance>> test, Vec3i size, Rotation rotation, boolean ignoreEntities, TestInstanceBlockEntity.Status status, Optional<Component> errorMessage) {
/*  94 */     this.test = test;
/*  94 */     this.size = size;
/*  94 */     this.rotation = rotation;
/*  94 */     this.ignoreEntities = ignoreEntities;
/*  94 */     this.status = status;
/*  94 */     this.errorMessage = errorMessage;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #94	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #94	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #94	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/TestInstanceBlockEntity$Data;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<ResourceKey<GameTestInstance>> test() {
/*  94 */     return this.test;
/*     */   }
/*     */   
/*     */   public Vec3i size() {
/*  94 */     return this.size;
/*     */   }
/*     */   
/*     */   public Rotation rotation() {
/*  94 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public boolean ignoreEntities() {
/*  94 */     return this.ignoreEntities;
/*     */   }
/*     */   
/*     */   public TestInstanceBlockEntity.Status status() {
/*  94 */     return this.status;
/*     */   }
/*     */   
/*     */   public Optional<Component> errorMessage() {
/*  94 */     return this.errorMessage;
/*     */   }
/*     */   
/*     */   static {
/*  95 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ResourceKey.codec(Registries.TEST_INSTANCE).optionalFieldOf("test").forGetter(Data::test), (App)Vec3i.CODEC.fieldOf("size").forGetter(Data::size), (App)Rotation.CODEC.fieldOf("rotation").forGetter(Data::rotation), (App)Codec.BOOL.fieldOf("ignore_entities").forGetter(Data::ignoreEntities), (App)TestInstanceBlockEntity.Status.CODEC.fieldOf("status").forGetter(Data::status), (App)ComponentSerialization.CODEC.optionalFieldOf("error_message").forGetter(Data::errorMessage)).apply((Applicative)i, Data::new));
/*     */   }
/*     */   
/* 103 */   public static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(
/* 104 */       ByteBufCodecs.optional(ResourceKey.streamCodec(Registries.TEST_INSTANCE)), Data::test, Vec3i.STREAM_CODEC, Data::size, Rotation.STREAM_CODEC, Data::rotation, ByteBufCodecs.BOOL, Data::ignoreEntities, TestInstanceBlockEntity.Status.STREAM_CODEC, Data::status, 
/*     */       
/* 109 */       ByteBufCodecs.optional(ComponentSerialization.STREAM_CODEC), Data::errorMessage, Data::new);
/*     */   
/*     */   public Data withSize(Vec3i size) {
/* 114 */     return new Data(this.test, size, this.rotation, this.ignoreEntities, this.status, this.errorMessage);
/*     */   }
/*     */   
/*     */   public Data withStatus(TestInstanceBlockEntity.Status status) {
/* 118 */     return new Data(this.test, this.size, this.rotation, this.ignoreEntities, status, Optional.empty());
/*     */   }
/*     */   
/*     */   public Data withError(Component error) {
/* 122 */     return new Data(this.test, this.size, this.rotation, this.ignoreEntities, TestInstanceBlockEntity.Status.FINISHED, Optional.of(error));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\TestInstanceBlockEntity$Data.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */