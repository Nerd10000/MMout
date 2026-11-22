/*    */ package net.minecraft.world.entity.npc.villager;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class VillagerData extends Record {
/*    */   private final Holder<VillagerType> type;
/*    */   
/*    */   private final Holder<VillagerProfession> profession;
/*    */   
/*    */   private final int level;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/npc/villager/VillagerData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerData;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/npc/villager/VillagerData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerData;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/npc/villager/VillagerData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<VillagerType> type() {
/* 15 */     return this.type;
/*    */   }
/*    */   
/*    */   public Holder<VillagerProfession> profession() {
/* 15 */     return this.profession;
/*    */   }
/*    */   
/*    */   public int level() {
/* 15 */     return this.level;
/*    */   }
/*    */   
/* 20 */   public static final ResourceKey<VillagerType> DEFAULT_TYPE = VillagerType.PLAINS;
/*    */   
/*    */   public static final int MIN_VILLAGER_LEVEL = 1;
/*    */   
/*    */   public static final int MAX_VILLAGER_LEVEL = 5;
/*    */   
/* 24 */   private static final int[] NEXT_LEVEL_XP_THRESHOLDS = new int[] { 0, 10, 70, 150, 250 };
/*    */   
/*    */   public static final Codec<VillagerData> CODEC;
/*    */   
/*    */   static {
/* 26 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BuiltInRegistries.VILLAGER_TYPE.holderByNameCodec().fieldOf("type").orElseGet(()).forGetter(()), (App)BuiltInRegistries.VILLAGER_PROFESSION.holderByNameCodec().fieldOf("profession").orElseGet(()).forGetter(()), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.INT, "level", Integer.valueOf(1)).forGetter(())).apply((Applicative)i, VillagerData::new));
/*    */   }
/*    */   
/* 32 */   public static final StreamCodec<RegistryFriendlyByteBuf, VillagerData> STREAM_CODEC = StreamCodec.composite(
/* 33 */       ByteBufCodecs.holderRegistry(Registries.VILLAGER_TYPE), VillagerData::type, 
/* 34 */       ByteBufCodecs.holderRegistry(Registries.VILLAGER_PROFESSION), VillagerData::profession, ByteBufCodecs.VAR_INT, VillagerData::level, VillagerData::new);
/*    */   
/*    */   public VillagerData(Holder<VillagerType> type, Holder<VillagerProfession> profession, int level) {
/* 40 */     level = Math.max(1, level);
/*    */     this.type = type;
/*    */     this.profession = profession;
/*    */     this.level = level;
/*    */   }
/*    */   
/*    */   public VillagerData withType(Holder<VillagerType> type) {
/* 44 */     return new VillagerData(type, this.profession, this.level);
/*    */   }
/*    */   
/*    */   public VillagerData withType(HolderGetter.Provider registries, ResourceKey<VillagerType> type) {
/* 48 */     return withType((Holder<VillagerType>)registries.getOrThrow(type));
/*    */   }
/*    */   
/*    */   public VillagerData withProfession(Holder<VillagerProfession> profession) {
/* 52 */     return new VillagerData(this.type, profession, this.level);
/*    */   }
/*    */   
/*    */   public VillagerData withProfession(HolderGetter.Provider registries, ResourceKey<VillagerProfession> profession) {
/* 56 */     return withProfession((Holder<VillagerProfession>)registries.getOrThrow(profession));
/*    */   }
/*    */   
/*    */   public VillagerData withLevel(int level) {
/* 60 */     return new VillagerData(this.type, this.profession, level);
/*    */   }
/*    */   
/*    */   public static int getMinXpPerLevel(int level) {
/* 64 */     return canLevelUp(level) ? NEXT_LEVEL_XP_THRESHOLDS[level - 1] : 0;
/*    */   }
/*    */   
/*    */   public static int getMaxXpPerLevel(int level) {
/* 68 */     return canLevelUp(level) ? NEXT_LEVEL_XP_THRESHOLDS[level] : 0;
/*    */   }
/*    */   
/*    */   public static boolean canLevelUp(int currentLevel) {
/* 72 */     return (currentLevel >= 1 && currentLevel < 5);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\npc\villager\VillagerData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */