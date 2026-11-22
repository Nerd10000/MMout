/*    */ package net.minecraft.world.item.alchemy;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ 
/*    */ public class Potions {
/* 11 */   public static final Holder<Potion> WATER = register(PotionIds.WATER, new Potion("water", new MobEffectInstance[0]));
/*    */   
/* 12 */   public static final Holder<Potion> MUNDANE = register(PotionIds.MUNDANE, new Potion("mundane", new MobEffectInstance[0]));
/*    */   
/* 13 */   public static final Holder<Potion> THICK = register(PotionIds.THICK, new Potion("thick", new MobEffectInstance[0]));
/*    */   
/* 14 */   public static final Holder<Potion> AWKWARD = register(PotionIds.AWKWARD, new Potion("awkward", new MobEffectInstance[0]));
/*    */   
/* 16 */   public static final Holder<Potion> NIGHT_VISION = register(PotionIds.NIGHT_VISION, new Potion("night_vision", new MobEffectInstance[] { new MobEffectInstance(MobEffects.NIGHT_VISION, 3600) }));
/*    */   
/* 17 */   public static final Holder<Potion> LONG_NIGHT_VISION = register(PotionIds.LONG_NIGHT_VISION, new Potion("night_vision", new MobEffectInstance[] { new MobEffectInstance(MobEffects.NIGHT_VISION, 9600) }));
/*    */   
/* 19 */   public static final Holder<Potion> INVISIBILITY = register(PotionIds.INVISIBILITY, new Potion("invisibility", new MobEffectInstance[] { new MobEffectInstance(MobEffects.INVISIBILITY, 3600) }));
/*    */   
/* 20 */   public static final Holder<Potion> LONG_INVISIBILITY = register(PotionIds.LONG_INVISIBILITY, new Potion("invisibility", new MobEffectInstance[] { new MobEffectInstance(MobEffects.INVISIBILITY, 9600) }));
/*    */   
/* 22 */   public static final Holder<Potion> LEAPING = register(PotionIds.LEAPING, new Potion("leaping", new MobEffectInstance[] { new MobEffectInstance(MobEffects.JUMP_BOOST, 3600) }));
/*    */   
/* 23 */   public static final Holder<Potion> LONG_LEAPING = register(PotionIds.LONG_LEAPING, new Potion("leaping", new MobEffectInstance[] { new MobEffectInstance(MobEffects.JUMP_BOOST, 9600) }));
/*    */   
/* 24 */   public static final Holder<Potion> STRONG_LEAPING = register(PotionIds.STRONG_LEAPING, new Potion("leaping", new MobEffectInstance[] { new MobEffectInstance(MobEffects.JUMP_BOOST, 1800, 1) }));
/*    */   
/* 26 */   public static final Holder<Potion> FIRE_RESISTANCE = register(PotionIds.FIRE_RESISTANCE, new Potion("fire_resistance", new MobEffectInstance[] { new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3600) }));
/*    */   
/* 27 */   public static final Holder<Potion> LONG_FIRE_RESISTANCE = register(PotionIds.LONG_FIRE_RESISTANCE, new Potion("fire_resistance", new MobEffectInstance[] { new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 9600) }));
/*    */   
/* 29 */   public static final Holder<Potion> SWIFTNESS = register(PotionIds.SWIFTNESS, new Potion("swiftness", new MobEffectInstance[] { new MobEffectInstance(MobEffects.SPEED, 3600) }));
/*    */   
/* 30 */   public static final Holder<Potion> LONG_SWIFTNESS = register(PotionIds.LONG_SWIFTNESS, new Potion("swiftness", new MobEffectInstance[] { new MobEffectInstance(MobEffects.SPEED, 9600) }));
/*    */   
/* 31 */   public static final Holder<Potion> STRONG_SWIFTNESS = register(PotionIds.STRONG_SWIFTNESS, new Potion("swiftness", new MobEffectInstance[] { new MobEffectInstance(MobEffects.SPEED, 1800, 1) }));
/*    */   
/* 33 */   public static final Holder<Potion> SLOWNESS = register(PotionIds.SLOWNESS, new Potion("slowness", new MobEffectInstance[] { new MobEffectInstance(MobEffects.SLOWNESS, 1800) }));
/*    */   
/* 34 */   public static final Holder<Potion> LONG_SLOWNESS = register(PotionIds.LONG_SLOWNESS, new Potion("slowness", new MobEffectInstance[] { new MobEffectInstance(MobEffects.SLOWNESS, 4800) }));
/*    */   
/* 35 */   public static final Holder<Potion> STRONG_SLOWNESS = register(PotionIds.STRONG_SLOWNESS, new Potion("slowness", new MobEffectInstance[] { new MobEffectInstance(MobEffects.SLOWNESS, 400, 3) }));
/*    */   
/* 37 */   public static final Holder<Potion> TURTLE_MASTER = register(PotionIds.TURTLE_MASTER, new Potion("turtle_master", new MobEffectInstance[] { new MobEffectInstance(MobEffects.SLOWNESS, 400, 3), new MobEffectInstance(MobEffects.RESISTANCE, 400, 2) }));
/*    */   
/* 38 */   public static final Holder<Potion> LONG_TURTLE_MASTER = register(PotionIds.LONG_TURTLE_MASTER, new Potion("turtle_master", new MobEffectInstance[] { new MobEffectInstance(MobEffects.SLOWNESS, 800, 3), new MobEffectInstance(MobEffects.RESISTANCE, 800, 2) }));
/*    */   
/* 39 */   public static final Holder<Potion> STRONG_TURTLE_MASTER = register(PotionIds.STRONG_TURTLE_MASTER, new Potion("turtle_master", new MobEffectInstance[] { new MobEffectInstance(MobEffects.SLOWNESS, 400, 5), new MobEffectInstance(MobEffects.RESISTANCE, 400, 3) }));
/*    */   
/* 41 */   public static final Holder<Potion> WATER_BREATHING = register(PotionIds.WATER_BREATHING, new Potion("water_breathing", new MobEffectInstance[] { new MobEffectInstance(MobEffects.WATER_BREATHING, 3600) }));
/*    */   
/* 42 */   public static final Holder<Potion> LONG_WATER_BREATHING = register(PotionIds.LONG_WATER_BREATHING, new Potion("water_breathing", new MobEffectInstance[] { new MobEffectInstance(MobEffects.WATER_BREATHING, 9600) }));
/*    */   
/* 44 */   public static final Holder<Potion> HEALING = register(PotionIds.HEALING, new Potion("healing", new MobEffectInstance[] { new MobEffectInstance(MobEffects.INSTANT_HEALTH, 1) }));
/*    */   
/* 45 */   public static final Holder<Potion> STRONG_HEALING = register(PotionIds.STRONG_HEALING, new Potion("healing", new MobEffectInstance[] { new MobEffectInstance(MobEffects.INSTANT_HEALTH, 1, 1) }));
/*    */   
/* 47 */   public static final Holder<Potion> HARMING = register(PotionIds.HARMING, new Potion("harming", new MobEffectInstance[] { new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1) }));
/*    */   
/* 48 */   public static final Holder<Potion> STRONG_HARMING = register(PotionIds.STRONG_HARMING, new Potion("harming", new MobEffectInstance[] { new MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 1) }));
/*    */   
/* 50 */   public static final Holder<Potion> POISON = register(PotionIds.POISON, new Potion("poison", new MobEffectInstance[] { new MobEffectInstance(MobEffects.POISON, 900) }));
/*    */   
/* 51 */   public static final Holder<Potion> LONG_POISON = register(PotionIds.LONG_POISON, new Potion("poison", new MobEffectInstance[] { new MobEffectInstance(MobEffects.POISON, 1800) }));
/*    */   
/* 52 */   public static final Holder<Potion> STRONG_POISON = register(PotionIds.STRONG_POISON, new Potion("poison", new MobEffectInstance[] { new MobEffectInstance(MobEffects.POISON, 432, 1) }));
/*    */   
/* 54 */   public static final Holder<Potion> REGENERATION = register(PotionIds.REGENERATION, new Potion("regeneration", new MobEffectInstance[] { new MobEffectInstance(MobEffects.REGENERATION, 900) }));
/*    */   
/* 55 */   public static final Holder<Potion> LONG_REGENERATION = register(PotionIds.LONG_REGENERATION, new Potion("regeneration", new MobEffectInstance[] { new MobEffectInstance(MobEffects.REGENERATION, 1800) }));
/*    */   
/* 56 */   public static final Holder<Potion> STRONG_REGENERATION = register(PotionIds.STRONG_REGENERATION, new Potion("regeneration", new MobEffectInstance[] { new MobEffectInstance(MobEffects.REGENERATION, 450, 1) }));
/*    */   
/* 58 */   public static final Holder<Potion> STRENGTH = register(PotionIds.STRENGTH, new Potion("strength", new MobEffectInstance[] { new MobEffectInstance(MobEffects.STRENGTH, 3600) }));
/*    */   
/* 59 */   public static final Holder<Potion> LONG_STRENGTH = register(PotionIds.LONG_STRENGTH, new Potion("strength", new MobEffectInstance[] { new MobEffectInstance(MobEffects.STRENGTH, 9600) }));
/*    */   
/* 60 */   public static final Holder<Potion> STRONG_STRENGTH = register(PotionIds.STRONG_STRENGTH, new Potion("strength", new MobEffectInstance[] { new MobEffectInstance(MobEffects.STRENGTH, 1800, 1) }));
/*    */   
/* 62 */   public static final Holder<Potion> WEAKNESS = register(PotionIds.WEAKNESS, new Potion("weakness", new MobEffectInstance[] { new MobEffectInstance(MobEffects.WEAKNESS, 1800) }));
/*    */   
/* 63 */   public static final Holder<Potion> LONG_WEAKNESS = register(PotionIds.LONG_WEAKNESS, new Potion("weakness", new MobEffectInstance[] { new MobEffectInstance(MobEffects.WEAKNESS, 4800) }));
/*    */   
/* 65 */   public static final Holder<Potion> LUCK = register(PotionIds.LUCK, new Potion("luck", new MobEffectInstance[] { new MobEffectInstance(MobEffects.LUCK, 6000) }));
/*    */   
/* 67 */   public static final Holder<Potion> SLOW_FALLING = register(PotionIds.SLOW_FALLING, new Potion("slow_falling", new MobEffectInstance[] { new MobEffectInstance(MobEffects.SLOW_FALLING, 1800) }));
/*    */   
/* 68 */   public static final Holder<Potion> LONG_SLOW_FALLING = register(PotionIds.LONG_SLOW_FALLING, new Potion("slow_falling", new MobEffectInstance[] { new MobEffectInstance(MobEffects.SLOW_FALLING, 4800) }));
/*    */   
/* 69 */   public static final Holder<Potion> WIND_CHARGED = register(PotionIds.WIND_CHARGED, new Potion("wind_charged", new MobEffectInstance[] { new MobEffectInstance(MobEffects.WIND_CHARGED, 3600) }));
/*    */   
/* 70 */   public static final Holder<Potion> WEAVING = register(PotionIds.WEAVING, new Potion("weaving", new MobEffectInstance[] { new MobEffectInstance(MobEffects.WEAVING, 3600) }));
/*    */   
/* 71 */   public static final Holder<Potion> OOZING = register(PotionIds.OOZING, new Potion("oozing", new MobEffectInstance[] { new MobEffectInstance(MobEffects.OOZING, 3600) }));
/*    */   
/* 72 */   public static final Holder<Potion> INFESTED = register(PotionIds.INFESTED, new Potion("infested", new MobEffectInstance[] { new MobEffectInstance(MobEffects.INFESTED, 3600) }));
/*    */   
/*    */   private static Holder<Potion> register(ResourceKey<Potion> key, Potion potion) {
/* 75 */     return (Holder<Potion>)Registry.registerForHolder(BuiltInRegistries.POTION, key, potion);
/*    */   }
/*    */   
/*    */   public static Holder<Potion> bootstrap(Registry<Potion> registry) {
/* 79 */     return WATER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\alchemy\Potions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */