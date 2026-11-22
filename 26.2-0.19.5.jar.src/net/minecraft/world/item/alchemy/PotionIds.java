/*    */ package net.minecraft.world.item.alchemy;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class PotionIds {
/*  8 */   public static final ResourceKey<Potion> WATER = register("water");
/*    */   
/*  9 */   public static final ResourceKey<Potion> MUNDANE = register("mundane");
/*    */   
/* 10 */   public static final ResourceKey<Potion> THICK = register("thick");
/*    */   
/* 11 */   public static final ResourceKey<Potion> AWKWARD = register("awkward");
/*    */   
/* 13 */   public static final ResourceKey<Potion> NIGHT_VISION = register("night_vision");
/*    */   
/* 14 */   public static final ResourceKey<Potion> LONG_NIGHT_VISION = register("long_night_vision");
/*    */   
/* 16 */   public static final ResourceKey<Potion> INVISIBILITY = register("invisibility");
/*    */   
/* 17 */   public static final ResourceKey<Potion> LONG_INVISIBILITY = register("long_invisibility");
/*    */   
/* 19 */   public static final ResourceKey<Potion> LEAPING = register("leaping");
/*    */   
/* 20 */   public static final ResourceKey<Potion> LONG_LEAPING = register("long_leaping");
/*    */   
/* 21 */   public static final ResourceKey<Potion> STRONG_LEAPING = register("strong_leaping");
/*    */   
/* 23 */   public static final ResourceKey<Potion> FIRE_RESISTANCE = register("fire_resistance");
/*    */   
/* 24 */   public static final ResourceKey<Potion> LONG_FIRE_RESISTANCE = register("long_fire_resistance");
/*    */   
/* 26 */   public static final ResourceKey<Potion> SWIFTNESS = register("swiftness");
/*    */   
/* 27 */   public static final ResourceKey<Potion> LONG_SWIFTNESS = register("long_swiftness");
/*    */   
/* 28 */   public static final ResourceKey<Potion> STRONG_SWIFTNESS = register("strong_swiftness");
/*    */   
/* 30 */   public static final ResourceKey<Potion> SLOWNESS = register("slowness");
/*    */   
/* 31 */   public static final ResourceKey<Potion> LONG_SLOWNESS = register("long_slowness");
/*    */   
/* 32 */   public static final ResourceKey<Potion> STRONG_SLOWNESS = register("strong_slowness");
/*    */   
/* 34 */   public static final ResourceKey<Potion> TURTLE_MASTER = register("turtle_master");
/*    */   
/* 35 */   public static final ResourceKey<Potion> LONG_TURTLE_MASTER = register("long_turtle_master");
/*    */   
/* 36 */   public static final ResourceKey<Potion> STRONG_TURTLE_MASTER = register("strong_turtle_master");
/*    */   
/* 38 */   public static final ResourceKey<Potion> WATER_BREATHING = register("water_breathing");
/*    */   
/* 39 */   public static final ResourceKey<Potion> LONG_WATER_BREATHING = register("long_water_breathing");
/*    */   
/* 41 */   public static final ResourceKey<Potion> HEALING = register("healing");
/*    */   
/* 42 */   public static final ResourceKey<Potion> STRONG_HEALING = register("strong_healing");
/*    */   
/* 44 */   public static final ResourceKey<Potion> HARMING = register("harming");
/*    */   
/* 45 */   public static final ResourceKey<Potion> STRONG_HARMING = register("strong_harming");
/*    */   
/* 47 */   public static final ResourceKey<Potion> POISON = register("poison");
/*    */   
/* 48 */   public static final ResourceKey<Potion> LONG_POISON = register("long_poison");
/*    */   
/* 49 */   public static final ResourceKey<Potion> STRONG_POISON = register("strong_poison");
/*    */   
/* 51 */   public static final ResourceKey<Potion> REGENERATION = register("regeneration");
/*    */   
/* 52 */   public static final ResourceKey<Potion> LONG_REGENERATION = register("long_regeneration");
/*    */   
/* 53 */   public static final ResourceKey<Potion> STRONG_REGENERATION = register("strong_regeneration");
/*    */   
/* 55 */   public static final ResourceKey<Potion> STRENGTH = register("strength");
/*    */   
/* 56 */   public static final ResourceKey<Potion> LONG_STRENGTH = register("long_strength");
/*    */   
/* 57 */   public static final ResourceKey<Potion> STRONG_STRENGTH = register("strong_strength");
/*    */   
/* 59 */   public static final ResourceKey<Potion> WEAKNESS = register("weakness");
/*    */   
/* 60 */   public static final ResourceKey<Potion> LONG_WEAKNESS = register("long_weakness");
/*    */   
/* 62 */   public static final ResourceKey<Potion> LUCK = register("luck");
/*    */   
/* 64 */   public static final ResourceKey<Potion> SLOW_FALLING = register("slow_falling");
/*    */   
/* 65 */   public static final ResourceKey<Potion> LONG_SLOW_FALLING = register("long_slow_falling");
/*    */   
/* 66 */   public static final ResourceKey<Potion> WIND_CHARGED = register("wind_charged");
/*    */   
/* 67 */   public static final ResourceKey<Potion> WEAVING = register("weaving");
/*    */   
/* 68 */   public static final ResourceKey<Potion> OOZING = register("oozing");
/*    */   
/* 69 */   public static final ResourceKey<Potion> INFESTED = register("infested");
/*    */   
/*    */   private static ResourceKey<Potion> register(String name) {
/* 72 */     return ResourceKey.create(Registries.POTION, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\alchemy\PotionIds.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */