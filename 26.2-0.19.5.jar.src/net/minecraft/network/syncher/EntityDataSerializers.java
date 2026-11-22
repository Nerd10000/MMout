/*     */ package net.minecraft.network.syncher;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.core.Rotations;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.animal.armadillo.Armadillo;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenSoundVariant;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenVariant;
/*     */ import net.minecraft.world.entity.animal.cow.CowSoundVariant;
/*     */ import net.minecraft.world.entity.animal.cow.CowVariant;
/*     */ import net.minecraft.world.entity.animal.feline.CatSoundVariant;
/*     */ import net.minecraft.world.entity.animal.feline.CatVariant;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariant;
/*     */ import net.minecraft.world.entity.animal.golem.CopperGolemState;
/*     */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
/*     */ import net.minecraft.world.entity.animal.pig.PigSoundVariant;
/*     */ import net.minecraft.world.entity.animal.pig.PigVariant;
/*     */ import net.minecraft.world.entity.animal.sniffer.Sniffer;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfSoundVariant;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfVariant;
/*     */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerData;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.WeatheringCopper;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class EntityDataSerializers {
/*  53 */   private static final CrudeIncrementalIntIdentityHashBiMap<EntityDataSerializer<?>> SERIALIZERS = CrudeIncrementalIntIdentityHashBiMap.create(16);
/*     */   
/*  55 */   public static final EntityDataSerializer<Byte> BYTE = EntityDataSerializer.forValueType(ByteBufCodecs.BYTE);
/*     */   
/*  57 */   public static final EntityDataSerializer<Integer> INT = EntityDataSerializer.forValueType(ByteBufCodecs.VAR_INT);
/*     */   
/*  59 */   public static final EntityDataSerializer<Long> LONG = EntityDataSerializer.forValueType(ByteBufCodecs.VAR_LONG);
/*     */   
/*  61 */   public static final EntityDataSerializer<Float> FLOAT = EntityDataSerializer.forValueType(ByteBufCodecs.FLOAT);
/*     */   
/*  63 */   public static final EntityDataSerializer<String> STRING = EntityDataSerializer.forValueType(ByteBufCodecs.STRING_UTF8);
/*     */   
/*  65 */   public static final EntityDataSerializer<Component> COMPONENT = EntityDataSerializer.forValueType(ComponentSerialization.TRUSTED_STREAM_CODEC);
/*     */   
/*  67 */   public static final EntityDataSerializer<Optional<Component>> OPTIONAL_COMPONENT = EntityDataSerializer.forValueType(ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC);
/*     */   
/*  69 */   public static final EntityDataSerializer<ItemStack> ITEM_STACK = new EntityDataSerializer<ItemStack>() {
/*     */       public StreamCodec<? super RegistryFriendlyByteBuf, ItemStack> codec() {
/*  72 */         return ItemStack.OPTIONAL_STREAM_CODEC;
/*     */       }
/*     */       
/*     */       public ItemStack copy(ItemStack value) {
/*  77 */         return value.copy();
/*     */       }
/*     */     };
/*     */   
/*  81 */   public static final EntityDataSerializer<BlockState> BLOCK_STATE = EntityDataSerializer.forValueType(ByteBufCodecs.idMapper((IdMap)Block.BLOCK_STATE_REGISTRY));
/*     */   
/*  83 */   private static final StreamCodec<ByteBuf, Optional<BlockState>> OPTIONAL_BLOCK_STATE_CODEC = new StreamCodec<ByteBuf, Optional<BlockState>>() {
/*     */       public void encode(ByteBuf output, Optional<BlockState> value) {
/*  86 */         if (value.isPresent()) {
/*  87 */           VarInt.write(output, Block.getId(value.get()));
/*     */         } else {
/*  89 */           VarInt.write(output, 0);
/*     */         } 
/*     */       }
/*     */       
/*     */       public Optional<BlockState> decode(ByteBuf input) {
/*  95 */         int id = VarInt.read(input);
/*  96 */         if (id == 0)
/*  97 */           return Optional.empty(); 
/*  99 */         return Optional.of(Block.stateById(id));
/*     */       }
/*     */     };
/*     */   
/* 104 */   public static final EntityDataSerializer<Optional<BlockState>> OPTIONAL_BLOCK_STATE = EntityDataSerializer.forValueType((StreamCodec)OPTIONAL_BLOCK_STATE_CODEC);
/*     */   
/* 106 */   public static final EntityDataSerializer<Boolean> BOOLEAN = EntityDataSerializer.forValueType(ByteBufCodecs.BOOL);
/*     */   
/* 108 */   public static final EntityDataSerializer<ParticleOptions> PARTICLE = EntityDataSerializer.forValueType(ParticleTypes.STREAM_CODEC);
/*     */   
/* 110 */   public static final EntityDataSerializer<List<ParticleOptions>> PARTICLES = EntityDataSerializer.forValueType(ParticleTypes.STREAM_CODEC.apply(ByteBufCodecs.list()));
/*     */   
/* 112 */   public static final EntityDataSerializer<Rotations> ROTATIONS = EntityDataSerializer.forValueType(Rotations.STREAM_CODEC);
/*     */   
/* 114 */   public static final EntityDataSerializer<BlockPos> BLOCK_POS = EntityDataSerializer.forValueType(BlockPos.STREAM_CODEC);
/*     */   
/* 116 */   public static final EntityDataSerializer<Optional<BlockPos>> OPTIONAL_BLOCK_POS = EntityDataSerializer.forValueType(BlockPos.STREAM_CODEC.apply(ByteBufCodecs::optional));
/*     */   
/* 118 */   public static final EntityDataSerializer<Direction> DIRECTION = EntityDataSerializer.forValueType(Direction.STREAM_CODEC);
/*     */   
/* 120 */   public static final EntityDataSerializer<Optional<EntityReference<LivingEntity>>> OPTIONAL_LIVING_ENTITY_REFERENCE = EntityDataSerializer.forValueType(EntityReference.streamCodec().apply(ByteBufCodecs::optional));
/*     */   
/* 122 */   public static final EntityDataSerializer<Optional<GlobalPos>> OPTIONAL_GLOBAL_POS = EntityDataSerializer.forValueType(GlobalPos.STREAM_CODEC.apply(ByteBufCodecs::optional));
/*     */   
/* 124 */   public static final EntityDataSerializer<VillagerData> VILLAGER_DATA = EntityDataSerializer.forValueType(VillagerData.STREAM_CODEC);
/*     */   
/* 126 */   private static final StreamCodec<ByteBuf, OptionalInt> OPTIONAL_UNSIGNED_INT_CODEC = new StreamCodec<ByteBuf, OptionalInt>() {
/*     */       public OptionalInt decode(ByteBuf input) {
/* 129 */         int v = VarInt.read(input);
/* 130 */         return (v == 0) ? OptionalInt.empty() : OptionalInt.of(v - 1);
/*     */       }
/*     */       
/*     */       public void encode(ByteBuf output, OptionalInt value) {
/* 135 */         VarInt.write(output, value.orElse(-1) + 1);
/*     */       }
/*     */     };
/*     */   
/* 139 */   public static final EntityDataSerializer<OptionalInt> OPTIONAL_UNSIGNED_INT = EntityDataSerializer.forValueType((StreamCodec)OPTIONAL_UNSIGNED_INT_CODEC);
/*     */   
/* 141 */   public static final EntityDataSerializer<Pose> POSE = EntityDataSerializer.forValueType(Pose.STREAM_CODEC);
/*     */   
/* 143 */   public static final EntityDataSerializer<Holder<CatVariant>> CAT_VARIANT = EntityDataSerializer.forValueType(CatVariant.STREAM_CODEC);
/*     */   
/* 145 */   public static final EntityDataSerializer<Holder<CatSoundVariant>> CAT_SOUND_VARIANT = EntityDataSerializer.forValueType(CatSoundVariant.STREAM_CODEC);
/*     */   
/* 147 */   public static final EntityDataSerializer<Holder<ChickenVariant>> CHICKEN_VARIANT = EntityDataSerializer.forValueType(ChickenVariant.STREAM_CODEC);
/*     */   
/* 149 */   public static final EntityDataSerializer<Holder<ChickenSoundVariant>> CHICKEN_SOUND_VARIANT = EntityDataSerializer.forValueType(ChickenSoundVariant.STREAM_CODEC);
/*     */   
/* 151 */   public static final EntityDataSerializer<Holder<CowVariant>> COW_VARIANT = EntityDataSerializer.forValueType(CowVariant.STREAM_CODEC);
/*     */   
/* 153 */   public static final EntityDataSerializer<Holder<CowSoundVariant>> COW_SOUND_VARIANT = EntityDataSerializer.forValueType(CowSoundVariant.STREAM_CODEC);
/*     */   
/* 155 */   public static final EntityDataSerializer<Holder<WolfVariant>> WOLF_VARIANT = EntityDataSerializer.forValueType(WolfVariant.STREAM_CODEC);
/*     */   
/* 157 */   public static final EntityDataSerializer<Holder<WolfSoundVariant>> WOLF_SOUND_VARIANT = EntityDataSerializer.forValueType(WolfSoundVariant.STREAM_CODEC);
/*     */   
/* 159 */   public static final EntityDataSerializer<Holder<FrogVariant>> FROG_VARIANT = EntityDataSerializer.forValueType(FrogVariant.STREAM_CODEC);
/*     */   
/* 161 */   public static final EntityDataSerializer<Holder<PigVariant>> PIG_VARIANT = EntityDataSerializer.forValueType(PigVariant.STREAM_CODEC);
/*     */   
/* 163 */   public static final EntityDataSerializer<Holder<PigSoundVariant>> PIG_SOUND_VARIANT = EntityDataSerializer.forValueType(PigSoundVariant.STREAM_CODEC);
/*     */   
/* 165 */   public static final EntityDataSerializer<Holder<ZombieNautilusVariant>> ZOMBIE_NAUTILUS_VARIANT = EntityDataSerializer.forValueType(ZombieNautilusVariant.STREAM_CODEC);
/*     */   
/* 167 */   public static final EntityDataSerializer<Holder<PaintingVariant>> PAINTING_VARIANT = EntityDataSerializer.forValueType(PaintingVariant.STREAM_CODEC);
/*     */   
/* 169 */   public static final EntityDataSerializer<Armadillo.ArmadilloState> ARMADILLO_STATE = EntityDataSerializer.forValueType(Armadillo.ArmadilloState.STREAM_CODEC);
/*     */   
/* 171 */   public static final EntityDataSerializer<Sniffer.State> SNIFFER_STATE = EntityDataSerializer.forValueType(Sniffer.State.STREAM_CODEC);
/*     */   
/* 173 */   public static final EntityDataSerializer<WeatheringCopper.WeatherState> WEATHERING_COPPER_STATE = EntityDataSerializer.forValueType(WeatheringCopper.WeatherState.STREAM_CODEC);
/*     */   
/* 175 */   public static final EntityDataSerializer<CopperGolemState> COPPER_GOLEM_STATE = EntityDataSerializer.forValueType(CopperGolemState.STREAM_CODEC);
/*     */   
/* 177 */   public static final EntityDataSerializer<Vector3fc> VECTOR3 = EntityDataSerializer.forValueType(ByteBufCodecs.VECTOR3F);
/*     */   
/* 179 */   public static final EntityDataSerializer<Quaternionfc> QUATERNION = EntityDataSerializer.forValueType(ByteBufCodecs.QUATERNIONF);
/*     */   
/* 181 */   public static final EntityDataSerializer<ResolvableProfile> RESOLVABLE_PROFILE = EntityDataSerializer.forValueType(ResolvableProfile.STREAM_CODEC);
/*     */   
/* 183 */   public static final EntityDataSerializer<HumanoidArm> HUMANOID_ARM = EntityDataSerializer.forValueType(HumanoidArm.STREAM_CODEC);
/*     */   
/*     */   static {
/* 186 */     registerSerializer(BYTE);
/* 187 */     registerSerializer(INT);
/* 188 */     registerSerializer(LONG);
/* 189 */     registerSerializer(FLOAT);
/* 190 */     registerSerializer(STRING);
/* 191 */     registerSerializer(COMPONENT);
/* 192 */     registerSerializer(OPTIONAL_COMPONENT);
/* 193 */     registerSerializer(ITEM_STACK);
/* 194 */     registerSerializer(BOOLEAN);
/* 195 */     registerSerializer(ROTATIONS);
/* 196 */     registerSerializer(BLOCK_POS);
/* 197 */     registerSerializer(OPTIONAL_BLOCK_POS);
/* 198 */     registerSerializer(DIRECTION);
/* 199 */     registerSerializer(OPTIONAL_LIVING_ENTITY_REFERENCE);
/* 200 */     registerSerializer(BLOCK_STATE);
/* 201 */     registerSerializer(OPTIONAL_BLOCK_STATE);
/* 202 */     registerSerializer(PARTICLE);
/* 203 */     registerSerializer(PARTICLES);
/* 204 */     registerSerializer(VILLAGER_DATA);
/* 205 */     registerSerializer(OPTIONAL_UNSIGNED_INT);
/* 206 */     registerSerializer(POSE);
/* 207 */     registerSerializer(CAT_VARIANT);
/* 208 */     registerSerializer(CAT_SOUND_VARIANT);
/* 209 */     registerSerializer(COW_VARIANT);
/* 210 */     registerSerializer(COW_SOUND_VARIANT);
/* 211 */     registerSerializer(WOLF_VARIANT);
/* 212 */     registerSerializer(WOLF_SOUND_VARIANT);
/* 213 */     registerSerializer(FROG_VARIANT);
/* 214 */     registerSerializer(PIG_VARIANT);
/* 215 */     registerSerializer(PIG_SOUND_VARIANT);
/* 216 */     registerSerializer(CHICKEN_VARIANT);
/* 217 */     registerSerializer(CHICKEN_SOUND_VARIANT);
/* 218 */     registerSerializer(ZOMBIE_NAUTILUS_VARIANT);
/* 219 */     registerSerializer(OPTIONAL_GLOBAL_POS);
/* 220 */     registerSerializer(PAINTING_VARIANT);
/* 221 */     registerSerializer(SNIFFER_STATE);
/* 222 */     registerSerializer(ARMADILLO_STATE);
/* 223 */     registerSerializer(COPPER_GOLEM_STATE);
/* 224 */     registerSerializer(WEATHERING_COPPER_STATE);
/* 225 */     registerSerializer(VECTOR3);
/* 226 */     registerSerializer(QUATERNION);
/* 227 */     registerSerializer(RESOLVABLE_PROFILE);
/* 228 */     registerSerializer(HUMANOID_ARM);
/*     */   }
/*     */   
/*     */   public static void registerSerializer(EntityDataSerializer<?> serializer) {
/* 232 */     SERIALIZERS.add(serializer);
/*     */   }
/*     */   
/*     */   public static EntityDataSerializer<?> getSerializer(int id) {
/* 236 */     return (EntityDataSerializer)SERIALIZERS.byId(id);
/*     */   }
/*     */   
/*     */   public static int getSerializedId(EntityDataSerializer<?> serializer) {
/* 240 */     return SERIALIZERS.getId(serializer);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\syncher\EntityDataSerializers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */