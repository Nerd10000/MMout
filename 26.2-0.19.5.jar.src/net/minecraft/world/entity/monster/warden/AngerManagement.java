/*     */ package net.minecraft.world.entity.monster.warden;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Streams;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ public class AngerManagement {
/*     */   @VisibleForTesting
/*     */   protected static final int CONVERSION_DELAY = 2;
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static final int MAX_ANGER = 150;
/*     */   
/*     */   private static final int DEFAULT_ANGER_DECREASE = 1;
/*     */   
/*  36 */   private int conversionDelay = Mth.randomBetweenInclusive(RandomSource.createThreadLocalInstance(), 0, 2);
/*     */   
/*     */   private int highestAnger;
/*     */   
/*     */   private static final Codec<Pair<UUID, Integer>> SUSPECT_ANGER_PAIR;
/*     */   
/*     */   private final Predicate<Entity> filter;
/*     */   
/*     */   @VisibleForTesting
/*     */   protected final ArrayList<Entity> suspects;
/*     */   
/*     */   private final Sorter suspectSorter;
/*     */   
/*     */   @VisibleForTesting
/*     */   protected final Object2IntMap<Entity> angerBySuspect;
/*     */   
/*     */   @VisibleForTesting
/*     */   protected final Object2IntMap<UUID> angerByUuid;
/*     */   
/*     */   static {
/*  39 */     SUSPECT_ANGER_PAIR = RecordCodecBuilder.create(i -> i.group((App)UUIDUtil.CODEC.fieldOf("uuid").forGetter(Pair::getFirst), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("anger").forGetter(Pair::getSecond)).apply((Applicative)i, Pair::of));
/*     */   }
/*     */   
/*     */   public static Codec<AngerManagement> codec(Predicate<Entity> filter) {
/*  45 */     return RecordCodecBuilder.create(i -> i.group((App)SUSPECT_ANGER_PAIR.listOf().lenientOptionalFieldOf("suspects", List.of()).forGetter(AngerManagement::createUuidAngerPairs)).apply((Applicative)i, ()));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static final class Sorter extends Record implements Comparator<Entity> {
/*     */     private final AngerManagement angerManagement;
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #57	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #57	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #57	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public AngerManagement angerManagement() {
/*  57 */       return this.angerManagement;
/*     */     }
/*     */     
/*     */     protected Sorter(AngerManagement angerManagement) {
/*  58 */       this.angerManagement = angerManagement;
/*     */     }
/*     */     
/*     */     public int compare(Entity entity1, Entity entity2) {
/*  61 */       if (entity1.equals(entity2))
/*  62 */         return 0; 
/*  65 */       int anger1 = this.angerManagement.angerBySuspect.getOrDefault(entity1, 0);
/*  66 */       int anger2 = this.angerManagement.angerBySuspect.getOrDefault(entity2, 0);
/*  69 */       this.angerManagement.highestAnger = Math.max(this.angerManagement.highestAnger, Math.max(anger1, anger2));
/*  71 */       boolean angryAt1 = AngerLevel.byAnger(anger1).isAngry();
/*  72 */       boolean angryAt2 = AngerLevel.byAnger(anger2).isAngry();
/*  73 */       if (angryAt1 != angryAt2)
/*  74 */         return angryAt1 ? -1 : 1; 
/*  78 */       boolean isPlayer1 = entity1 instanceof net.minecraft.world.entity.player.Player;
/*  79 */       boolean isPlayer2 = entity2 instanceof net.minecraft.world.entity.player.Player;
/*  80 */       if (isPlayer1 != isPlayer2)
/*  81 */         return isPlayer1 ? -1 : 1; 
/*  83 */       return Integer.compare(anger2, anger1);
/*     */     }
/*     */   }
/*     */   
/*     */   public AngerManagement(Predicate<Entity> filter, List<Pair<UUID, Integer>> angerByUuid) {
/*  97 */     this.filter = filter;
/*  98 */     this.suspects = new ArrayList<>();
/*  99 */     this.suspectSorter = new Sorter(this);
/* 100 */     this.angerBySuspect = (Object2IntMap<Entity>)new Object2IntOpenHashMap();
/* 102 */     this.angerByUuid = (Object2IntMap<UUID>)new Object2IntOpenHashMap(angerByUuid.size());
/* 103 */     angerByUuid.forEach(pair -> this.angerByUuid.put(pair.getFirst(), (Integer)pair.getSecond()));
/*     */   }
/*     */   
/*     */   private List<Pair<UUID, Integer>> createUuidAngerPairs() {
/* 107 */     return (List<Pair<UUID, Integer>>)Streams.concat(new Stream[] { this.suspects
/* 108 */           .stream().map(e -> Pair.of(e.getUUID(), Integer.valueOf(this.angerBySuspect.getInt(e)))), this.angerByUuid
/* 109 */           .object2IntEntrySet().stream().map(e -> Pair.of(e.getKey(), Integer.valueOf(e.getIntValue()))) }).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel level, Predicate<Entity> validEntity) {
/* 114 */     this.conversionDelay--;
/* 115 */     if (this.conversionDelay <= 0) {
/* 116 */       convertFromUuids(level);
/* 117 */       this.conversionDelay = 2;
/*     */     } 
/* 121 */     ObjectIterator<Object2IntMap.Entry<UUID>> serializedIterator = this.angerByUuid.object2IntEntrySet().iterator();
/* 122 */     while (serializedIterator.hasNext()) {
/* 123 */       Object2IntMap.Entry<UUID> entry = (Object2IntMap.Entry<UUID>)serializedIterator.next();
/* 124 */       int anger = entry.getIntValue();
/* 126 */       if (anger <= 1) {
/* 127 */         serializedIterator.remove();
/*     */         continue;
/*     */       } 
/* 129 */       entry.setValue(anger - 1);
/*     */     } 
/* 134 */     ObjectIterator<Object2IntMap.Entry<Entity>> iterator = this.angerBySuspect.object2IntEntrySet().iterator();
/* 135 */     while (iterator.hasNext()) {
/* 136 */       Object2IntMap.Entry<Entity> entry = (Object2IntMap.Entry<Entity>)iterator.next();
/* 137 */       int anger = entry.getIntValue();
/* 138 */       Entity entity = (Entity)entry.getKey();
/* 139 */       Entity.RemovalReason removalReason = entity.getRemovalReason();
/* 140 */       if (anger <= 1 || !validEntity.test(entity) || removalReason != null) {
/* 141 */         this.suspects.remove(entity);
/* 142 */         iterator.remove();
/* 145 */         if (anger > 1 && removalReason != null)
/* 146 */           switch (removalReason) {
/*     */             case CHANGED_DIMENSION:
/*     */             case UNLOADED_TO_CHUNK:
/*     */             case UNLOADED_WITH_PLAYER:
/* 147 */               this.angerByUuid.put(entity.getUUID(), anger - 1);
/*     */               continue;
/*     */           }  
/*     */         continue;
/*     */       } 
/* 151 */       entry.setValue(anger - 1);
/*     */     } 
/* 155 */     sortAndUpdateHighestAnger();
/*     */   }
/*     */   
/*     */   private void sortAndUpdateHighestAnger() {
/* 159 */     this.highestAnger = 0;
/* 160 */     this.suspects.sort(this.suspectSorter);
/* 163 */     if (this.suspects.size() == 1)
/* 164 */       this.highestAnger = this.angerBySuspect.getInt(this.suspects.get(0)); 
/*     */   }
/*     */   
/*     */   private void convertFromUuids(ServerLevel level) {
/* 169 */     ObjectIterator<Object2IntMap.Entry<UUID>> iterator = this.angerByUuid.object2IntEntrySet().iterator();
/* 170 */     while (iterator.hasNext()) {
/* 171 */       Object2IntMap.Entry<UUID> entry = (Object2IntMap.Entry<UUID>)iterator.next();
/* 172 */       int anger = entry.getIntValue();
/* 174 */       Entity entity = level.getEntity((UUID)entry.getKey());
/* 175 */       if (entity != null) {
/* 176 */         this.angerBySuspect.put(entity, anger);
/* 177 */         this.suspects.add(entity);
/* 178 */         iterator.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int increaseAnger(Entity entity, int increment) {
/* 184 */     boolean newSuspect = !this.angerBySuspect.containsKey(entity);
/* 185 */     int currentAnger = this.angerBySuspect.computeInt(entity, (k, anger) -> Integer.valueOf(Math.min(150, ((anger == null) ? 0 : anger.intValue()) + increment)));
/* 187 */     if (newSuspect) {
/* 188 */       int serializedAnger = this.angerByUuid.removeInt(entity.getUUID());
/* 189 */       currentAnger += serializedAnger;
/* 190 */       this.angerBySuspect.put(entity, currentAnger);
/* 191 */       this.suspects.add(entity);
/*     */     } 
/* 193 */     sortAndUpdateHighestAnger();
/* 194 */     return currentAnger;
/*     */   }
/*     */   
/*     */   public void clearAnger(Entity entity) {
/* 198 */     this.angerBySuspect.removeInt(entity);
/* 199 */     this.suspects.remove(entity);
/* 200 */     sortAndUpdateHighestAnger();
/*     */   }
/*     */   
/*     */   private Entity getTopSuspect() {
/* 204 */     return this.suspects.stream().filter(this.filter).findFirst().orElse(null);
/*     */   }
/*     */   
/*     */   public int getActiveAnger(Entity currentTarget) {
/* 208 */     return (currentTarget == null) ? this.highestAnger : this.angerBySuspect.getInt(currentTarget);
/*     */   }
/*     */   
/*     */   public Optional<LivingEntity> getActiveEntity() {
/* 212 */     return Optional.<Entity>ofNullable(getTopSuspect())
/* 213 */       .filter(e -> e instanceof LivingEntity)
/* 214 */       .map(e -> (LivingEntity)e);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\warden\AngerManagement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */