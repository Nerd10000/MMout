/*     */ package net.minecraft.world.scores.criteria;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.scores.TeamColor;
/*     */ 
/*     */ public class ObjectiveCriteria {
/*  20 */   private static final Map<String, ObjectiveCriteria> CUSTOM_CRITERIA = Maps.newHashMap();
/*     */   
/*  21 */   private static final Map<String, ObjectiveCriteria> CRITERIA_CACHE = Maps.newHashMap();
/*     */   
/*     */   public static final Codec<ObjectiveCriteria> CODEC;
/*     */   
/*     */   static {
/*  23 */     CODEC = Codec.STRING.comapFlatMap(name -> (DataResult)byName(name).<DataResult>map(DataResult::success).orElse(DataResult.error(())), ObjectiveCriteria::getName);
/*     */   }
/*     */   
/*  30 */   public static final ObjectiveCriteria DUMMY = registerCustom("dummy");
/*     */   
/*  31 */   public static final ObjectiveCriteria TRIGGER = registerCustom("trigger");
/*     */   
/*  32 */   public static final ObjectiveCriteria DEATH_COUNT = registerCustom("deathCount");
/*     */   
/*  33 */   public static final ObjectiveCriteria KILL_COUNT_PLAYERS = registerCustom("playerKillCount");
/*     */   
/*  34 */   public static final ObjectiveCriteria KILL_COUNT_ALL = registerCustom("totalKillCount");
/*     */   
/*  35 */   public static final ObjectiveCriteria HEALTH = registerCustom("health", true, RenderType.HEARTS);
/*     */   
/*  36 */   public static final ObjectiveCriteria FOOD = registerCustom("food", true, RenderType.INTEGER);
/*     */   
/*  37 */   public static final ObjectiveCriteria AIR = registerCustom("air", true, RenderType.INTEGER);
/*     */   
/*  38 */   public static final ObjectiveCriteria ARMOR = registerCustom("armor", true, RenderType.INTEGER);
/*     */   
/*  39 */   public static final ObjectiveCriteria EXPERIENCE = registerCustom("xp", true, RenderType.INTEGER);
/*     */   
/*  40 */   public static final ObjectiveCriteria LEVEL = registerCustom("level", true, RenderType.INTEGER);
/*     */   
/*     */   public static final Map<TeamColor, ObjectiveCriteria> TEAM_KILL;
/*     */   
/*     */   public static final Map<TeamColor, ObjectiveCriteria> KILLED_BY_TEAM;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final boolean readOnly;
/*     */   
/*     */   private final RenderType renderType;
/*     */   
/*     */   static {
/*  42 */     TEAM_KILL = registerForEveryTeamColor(format -> "teamkill." + format.getSerializedName());
/*  43 */     KILLED_BY_TEAM = registerForEveryTeamColor(format -> "killedByTeam." + format.getSerializedName());
/*     */   }
/*     */   
/*     */   private static Map<TeamColor, ObjectiveCriteria> registerForEveryTeamColor(Function<TeamColor, String> idFactory) {
/*  50 */     Map<TeamColor, ObjectiveCriteria> result = new EnumMap<>(TeamColor.class);
/*  51 */     for (TeamColor value : TeamColor.values()) {
/*  52 */       String id = idFactory.apply(value);
/*  53 */       result.put(value, registerCustom(id));
/*     */     } 
/*  55 */     return result;
/*     */   }
/*     */   
/*     */   private static ObjectiveCriteria registerCustom(String name, boolean readOnly, RenderType renderType) {
/*  59 */     ObjectiveCriteria result = new ObjectiveCriteria(name, readOnly, renderType);
/*  60 */     CUSTOM_CRITERIA.put(name, result);
/*  61 */     return result;
/*     */   }
/*     */   
/*     */   private static ObjectiveCriteria registerCustom(String name) {
/*  65 */     return registerCustom(name, false, RenderType.INTEGER);
/*     */   }
/*     */   
/*     */   protected ObjectiveCriteria(String name) {
/*  69 */     this(name, false, RenderType.INTEGER);
/*     */   }
/*     */   
/*     */   protected ObjectiveCriteria(String name, boolean readOnly, RenderType renderType) {
/*  73 */     this.name = name;
/*  74 */     this.readOnly = readOnly;
/*  75 */     this.renderType = renderType;
/*  76 */     CRITERIA_CACHE.put(name, this);
/*     */   }
/*     */   
/*     */   public static Set<String> getCustomCriteriaNames() {
/*  80 */     return (Set<String>)ImmutableSet.copyOf(CUSTOM_CRITERIA.keySet());
/*     */   }
/*     */   
/*     */   public static Optional<ObjectiveCriteria> byName(String name) {
/*  84 */     ObjectiveCriteria value = CRITERIA_CACHE.get(name);
/*  85 */     if (value != null)
/*  86 */       return Optional.of(value); 
/*  88 */     int colonPos = name.indexOf(':');
/*  89 */     if (colonPos < 0)
/*  90 */       return Optional.empty(); 
/*  92 */     return BuiltInRegistries.STAT_TYPE.getOptional(Identifier.bySeparator(name.substring(0, colonPos), '.'))
/*  93 */       .flatMap(statType -> getStat(statType, Identifier.bySeparator(name.substring(colonPos + 1), '.')));
/*     */   }
/*     */   
/*     */   private static <T> Optional<ObjectiveCriteria> getStat(StatType<T> statType, Identifier key) {
/*  97 */     Objects.requireNonNull(statType);
/*  97 */     return statType.getRegistry().getOptional(key).map(statType::get);
/*     */   }
/*     */   
/*     */   public String getName() {
/* 101 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isReadOnly() {
/* 105 */     return this.readOnly;
/*     */   }
/*     */   
/*     */   public RenderType getDefaultRenderType() {
/* 109 */     return this.renderType;
/*     */   }
/*     */   
/*     */   public enum RenderType implements StringRepresentable {
/* 113 */     INTEGER("integer"),
/* 114 */     HEARTS("hearts");
/*     */     
/*     */     private final String id;
/*     */     
/* 132 */     public static final StringRepresentable.EnumCodec<RenderType> CODEC = StringRepresentable.fromEnum(RenderType::values);
/*     */     
/*     */     RenderType(String id) {
/*     */       this.id = id;
/*     */     }
/*     */     
/*     */     public String getId() {
/*     */       return this.id;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/*     */       return this.id;
/*     */     }
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     public static RenderType byId(String key) {
/* 135 */       return (RenderType)CODEC.byName(key, INTEGER);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\scores\criteria\ObjectiveCriteria.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */