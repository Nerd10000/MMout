/*     */ package net.minecraft.world.entity.raid;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.PoiTypeTags;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Raids extends SavedData {
/*  31 */   private static final Identifier RAID_FILE_ID = Identifier.withDefaultNamespace("raids");
/*     */   
/*     */   public static final Codec<Raids> CODEC;
/*     */   
/*     */   static {
/*  33 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RaidWithId.CODEC.listOf().optionalFieldOf("raids", List.of()).forGetter(()), (App)Codec.INT.fieldOf("next_id").forGetter(()), (App)Codec.INT.fieldOf("tick").forGetter(())).apply((Applicative)i, Raids::new));
/*     */   }
/*     */   
/*  39 */   public static final SavedDataType<Raids> TYPE = new SavedDataType(RAID_FILE_ID, Raids::new, CODEC, DataFixTypes.SAVED_DATA_RAIDS);
/*     */   
/*  42 */   private final Int2ObjectMap<Raid> raidMap = (Int2ObjectMap<Raid>)new Int2ObjectOpenHashMap();
/*     */   
/*  43 */   private int nextId = 1;
/*     */   
/*     */   private int tick;
/*     */   
/*     */   public Raids() {
/*  47 */     setDirty();
/*     */   }
/*     */   
/*     */   private Raids(List<RaidWithId> raids, int nextId, int tick) {
/*  51 */     for (RaidWithId raid : raids)
/*  52 */       this.raidMap.put(raid.id, raid.raid); 
/*  54 */     this.nextId = nextId;
/*  55 */     this.tick = tick;
/*     */   }
/*     */   
/*     */   public Raid get(int raidId) {
/*  59 */     return (Raid)this.raidMap.get(raidId);
/*     */   }
/*     */   
/*     */   public OptionalInt getId(Raid raid) {
/*  63 */     for (ObjectIterator<Int2ObjectMap.Entry<Raid>> objectIterator = this.raidMap.int2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) {
/*  63 */       Int2ObjectMap.Entry<Raid> entry = objectIterator.next();
/*  64 */       if (entry.getValue() == raid)
/*  65 */         return OptionalInt.of(entry.getIntKey()); 
/*     */     } 
/*  68 */     return OptionalInt.empty();
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel level) {
/*  72 */     this.tick++;
/*  73 */     ObjectIterator<Raid> objectIterator = this.raidMap.values().iterator();
/*  75 */     while (objectIterator.hasNext()) {
/*  76 */       Raid raid = objectIterator.next();
/*  77 */       if (!((Boolean)level.getGameRules().get(GameRules.RAIDS)).booleanValue())
/*  78 */         raid.stop(); 
/*  80 */       if (raid.isStopped()) {
/*  81 */         objectIterator.remove();
/*  82 */         setDirty();
/*     */         continue;
/*     */       } 
/*  86 */       raid.tick(level);
/*     */     } 
/*  90 */     if (this.tick % 200 == 0)
/*  91 */       setDirty(); 
/*     */   }
/*     */   
/*     */   public static boolean canJoinRaid(Raider raider) {
/*  96 */     return (raider.isAlive() && raider.canJoinRaid() && raider.getNoActionTime() <= 2400);
/*     */   }
/*     */   
/*     */   public Raid createOrExtendRaid(ServerPlayer player, BlockPos raidPosition) {
/*     */     BlockPos raidCenterPos;
/* 102 */     if (player.isSpectator())
/* 103 */       return null; 
/* 106 */     ServerLevel level = player.level();
/* 107 */     if (!((Boolean)level.getGameRules().get(GameRules.RAIDS)).booleanValue())
/* 108 */       return null; 
/* 111 */     if (!((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.CAN_START_RAID, raidPosition)).booleanValue())
/* 112 */       return null; 
/* 116 */     List<PoiRecord> posses = level.getPoiManager().getInRange(e -> e.is(PoiTypeTags.VILLAGE), raidPosition, 64, PoiManager.Occupancy.IS_OCCUPIED).toList();
/* 117 */     int count = 0;
/* 118 */     Vec3 posTotals = Vec3.ZERO;
/* 119 */     for (PoiRecord p : posses) {
/* 120 */       BlockPos pos = p.getPos();
/* 121 */       posTotals = posTotals.add(pos.getX(), pos.getY(), pos.getZ());
/* 122 */       count++;
/*     */     } 
/* 126 */     if (count > 0) {
/* 128 */       posTotals = posTotals.scale(1.0D / count);
/* 129 */       raidCenterPos = BlockPos.containing((Position)posTotals);
/*     */     } else {
/* 132 */       raidCenterPos = raidPosition;
/*     */     } 
/* 135 */     Raid raid = getOrCreateRaid(level, raidCenterPos);
/* 137 */     if (!raid.isStarted() && !this.raidMap.containsValue(raid))
/* 138 */       this.raidMap.put(getUniqueId(), raid); 
/* 141 */     if (!raid.isStarted() || raid.getRaidOmenLevel() < raid.getMaxRaidOmenLevel())
/* 142 */       raid.absorbRaidOmen(player); 
/* 145 */     setDirty();
/* 147 */     return raid;
/*     */   }
/*     */   
/*     */   private Raid getOrCreateRaid(ServerLevel level, BlockPos pos) {
/* 151 */     Raid raid = level.getRaidAt(pos);
/* 152 */     return (raid != null) ? raid : new Raid(pos, level.getDifficulty());
/*     */   }
/*     */   
/*     */   public static Raids load(CompoundTag tag) {
/* 156 */     return CODEC.parse((DynamicOps)NbtOps.INSTANCE, tag).resultOrPartial().orElseGet(Raids::new);
/*     */   }
/*     */   
/*     */   private int getUniqueId() {
/* 160 */     return ++this.nextId;
/*     */   }
/*     */   
/*     */   public Raid getNearbyRaid(BlockPos pos, int maxDistSqr) {
/* 164 */     Raid closest = null;
/* 165 */     double closestDistanceSqr = maxDistSqr;
/* 166 */     for (ObjectIterator<Raid> objectIterator = this.raidMap.values().iterator(); objectIterator.hasNext(); ) {
/* 166 */       Raid raid = objectIterator.next();
/* 167 */       double distance = raid.getCenter().distSqr((Vec3i)pos);
/* 168 */       if (!raid.isActive())
/*     */         continue; 
/* 171 */       if (distance < closestDistanceSqr) {
/* 172 */         closest = raid;
/* 173 */         closestDistanceSqr = distance;
/*     */       } 
/*     */     } 
/* 176 */     return closest;
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public List<BlockPos> getRaidCentersInChunk(ChunkPos chunkPos) {
/* 183 */     Objects.requireNonNull(chunkPos);
/* 183 */     return this.raidMap.values().stream().map(Raid::getCenter).filter(chunkPos::contains)
/* 184 */       .toList();
/*     */   }
/*     */   
/*     */   private static final class RaidWithId extends Record {
/*     */     private final int id;
/*     */     
/*     */     private final Raid raid;
/*     */     
/*     */     public static final Codec<RaidWithId> CODEC;
/*     */     
/*     */     private RaidWithId(int id, Raid raid) {
/* 187 */       this.id = id;
/* 187 */       this.raid = raid;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/raid/Raids$RaidWithId;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/raid/Raids$RaidWithId;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/raid/Raids$RaidWithId;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/raid/Raids$RaidWithId;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/raid/Raids$RaidWithId;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #187	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/raid/Raids$RaidWithId;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int id() {
/* 187 */       return this.id;
/*     */     }
/*     */     
/*     */     public Raid raid() {
/* 187 */       return this.raid;
/*     */     }
/*     */     
/*     */     static {
/* 188 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("id").forGetter(RaidWithId::id), (App)Raid.MAP_CODEC.forGetter(RaidWithId::raid)).apply((Applicative)i, RaidWithId::new));
/*     */     }
/*     */     
/*     */     public static RaidWithId from(Int2ObjectMap.Entry<Raid> entry) {
/* 194 */       return new RaidWithId(entry.getIntKey(), (Raid)entry.getValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\raid\Raids.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */