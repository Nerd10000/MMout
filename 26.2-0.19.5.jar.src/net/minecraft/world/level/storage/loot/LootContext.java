/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class LootContext {
/*     */   private final LootParams params;
/*     */   
/*     */   private final RandomSource random;
/*     */   
/*     */   private final HolderGetter.Provider lootDataResolver;
/*     */   
/*  29 */   private final Set<VisitedEntry<?>> visitedElements = Sets.newLinkedHashSet();
/*     */   
/*     */   private LootContext(LootParams params, RandomSource random, HolderGetter.Provider lootDataResolver) {
/*  32 */     this.params = params;
/*  33 */     this.random = random;
/*  34 */     this.lootDataResolver = lootDataResolver;
/*     */   }
/*     */   
/*     */   public boolean hasParameter(ContextKey<?> key) {
/*  38 */     return this.params.contextMap().has(key);
/*     */   }
/*     */   
/*     */   public <T> T getParameter(ContextKey<T> key) {
/*  42 */     return (T)this.params.contextMap().getOrThrow(key);
/*     */   }
/*     */   
/*     */   public <T> T getOptionalParameter(ContextKey<T> key) {
/*  46 */     return (T)this.params.contextMap().getOptional(key);
/*     */   }
/*     */   
/*     */   public void addDynamicDrops(Identifier location, Consumer<ItemStack> output) {
/*  50 */     this.params.addDynamicDrops(location, output);
/*     */   }
/*     */   
/*     */   public boolean hasVisitedElement(VisitedEntry<?> element) {
/*  54 */     return this.visitedElements.contains(element);
/*     */   }
/*     */   
/*     */   public boolean pushVisitedElement(VisitedEntry<?> element) {
/*  58 */     return this.visitedElements.add(element);
/*     */   }
/*     */   
/*     */   public void popVisitedElement(VisitedEntry<?> element) {
/*  62 */     this.visitedElements.remove(element);
/*     */   }
/*     */   
/*     */   public HolderGetter.Provider getResolver() {
/*  66 */     return this.lootDataResolver;
/*     */   }
/*     */   
/*     */   public RandomSource getRandom() {
/*  70 */     return this.random;
/*     */   }
/*     */   
/*     */   public float getLuck() {
/*  74 */     return this.params.getLuck();
/*     */   }
/*     */   
/*     */   public ServerLevel getLevel() {
/*  78 */     return this.params.getLevel();
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final LootParams params;
/*     */     
/*     */     private RandomSource random;
/*     */     
/*     */     public Builder(LootParams params) {
/*  86 */       this.params = params;
/*     */     }
/*     */     
/*     */     public Builder withOptionalRandomSeed(long seed) {
/*  90 */       if (seed != 0L)
/*  91 */         this.random = RandomSource.create(seed); 
/*  93 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withOptionalRandomSource(RandomSource randomSource) {
/*  97 */       this.random = randomSource;
/*  98 */       return this;
/*     */     }
/*     */     
/*     */     public ServerLevel getLevel() {
/* 102 */       return this.params.getLevel();
/*     */     }
/*     */     
/*     */     public LootContext create(Optional<Identifier> randomSequenceKey) {
/* 106 */       ServerLevel level = getLevel();
/* 107 */       MinecraftServer server = level.getServer();
/* 110 */       Objects.requireNonNull(level);
/* 110 */       RandomSource random = Optional.<RandomSource>ofNullable(this.random).or(() -> {
/*     */             Objects.requireNonNull(server);
/*     */             return randomSequenceKey.map(server::getRandomSequence);
/* 110 */           }).orElseGet(level::getRandom);
/* 111 */       return new LootContext(this.params, random, (HolderGetter.Provider)server.reloadableRegistries().lookup());
/*     */     }
/*     */   }
/*     */   
/*     */   public enum EntityTarget implements StringRepresentable, LootContextArg.SimpleGetter<Entity> {
/* 116 */     THIS("this", LootContextParams.THIS_ENTITY),
/* 117 */     ATTACKER("attacker", LootContextParams.ATTACKING_ENTITY),
/* 118 */     DIRECT_ATTACKER("direct_attacker", LootContextParams.DIRECT_ATTACKING_ENTITY),
/* 119 */     ATTACKING_PLAYER("attacking_player", LootContextParams.LAST_DAMAGE_PLAYER),
/* 120 */     TARGET_ENTITY("target_entity", LootContextParams.TARGET_ENTITY),
/* 121 */     INTERACTING_ENTITY("interacting_entity", LootContextParams.INTERACTING_ENTITY);
/*     */     
/* 124 */     public static final StringRepresentable.EnumCodec<EntityTarget> CODEC = StringRepresentable.fromEnum(EntityTarget::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final ContextKey<? extends Entity> param;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     EntityTarget(String name, ContextKey<? extends Entity> param) {
/* 131 */       this.name = name;
/* 132 */       this.param = param;
/*     */     }
/*     */     
/*     */     public ContextKey<? extends Entity> contextParam() {
/* 137 */       return this.param;
/*     */     }
/*     */     
/*     */     public static EntityTarget getByName(String name) {
/* 141 */       EntityTarget target = (EntityTarget)CODEC.byName(name);
/* 142 */       if (target != null)
/* 143 */         return target; 
/* 145 */       throw new IllegalArgumentException("Invalid entity target " + name);
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 150 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum BlockEntityTarget implements StringRepresentable, LootContextArg.SimpleGetter<BlockEntity> {
/* 155 */     BLOCK_ENTITY("block_entity", LootContextParams.BLOCK_ENTITY);
/*     */     
/*     */     private final ContextKey<? extends BlockEntity> param;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     BlockEntityTarget(String name, ContextKey<? extends BlockEntity> param) {
/* 163 */       this.name = name;
/* 164 */       this.param = param;
/*     */     }
/*     */     
/*     */     public ContextKey<? extends BlockEntity> contextParam() {
/* 169 */       return this.param;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 174 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum ItemStackTarget implements StringRepresentable, LootContextArg.SimpleGetter<ItemInstance> {
/* 179 */     TOOL("tool", LootContextParams.TOOL);
/*     */     
/*     */     private final ContextKey<? extends ItemInstance> param;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     ItemStackTarget(String name, ContextKey<? extends ItemInstance> param) {
/* 187 */       this.name = name;
/* 188 */       this.param = param;
/*     */     }
/*     */     
/*     */     public ContextKey<? extends ItemInstance> contextParam() {
/* 193 */       return this.param;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 198 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static VisitedEntry<LootTable> createVisitedEntry(LootTable table) {
/* 203 */     return new VisitedEntry<>(LootDataType.TABLE, table);
/*     */   }
/*     */   
/*     */   public static VisitedEntry<LootItemCondition> createVisitedEntry(LootItemCondition table) {
/* 207 */     return new VisitedEntry<>(LootDataType.PREDICATE, table);
/*     */   }
/*     */   
/*     */   public static VisitedEntry<LootItemFunction> createVisitedEntry(LootItemFunction table) {
/* 211 */     return new VisitedEntry<>(LootDataType.MODIFIER, table);
/*     */   }
/*     */   
/*     */   public static final class VisitedEntry<T extends Validatable> extends Record {
/*     */     private final LootDataType<T> type;
/*     */     
/*     */     private final T value;
/*     */     
/*     */     public VisitedEntry(LootDataType<T> type, T value) {
/* 214 */       this.type = type;
/* 214 */       this.value = value;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #214	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #214	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #214	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry<TT;>;
/*     */     }
/*     */     
/*     */     public LootDataType<T> type() {
/* 214 */       return this.type;
/*     */     }
/*     */     
/*     */     public T value() {
/* 214 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\LootContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */