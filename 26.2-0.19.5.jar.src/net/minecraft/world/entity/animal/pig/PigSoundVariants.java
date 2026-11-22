/*    */ package net.minecraft.world.entity.animal.pig;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class PigSoundVariants {
/*    */   public enum SoundSet {
/* 14 */     CLASSIC("classic", "pig"),
/* 15 */     MINI("mini", "pig_mini"),
/* 16 */     BIG("big", "pig_big");
/*    */     
/*    */     private final String identifier;
/*    */     
/*    */     private final String soundEventIdentifier;
/*    */     
/*    */     SoundSet(String identifier, String soundEventIdentifier) {
/* 22 */       this.identifier = identifier;
/* 23 */       this.soundEventIdentifier = soundEventIdentifier;
/*    */     }
/*    */     
/*    */     public String getIdentifier() {
/* 27 */       return this.identifier;
/*    */     }
/*    */     
/*    */     public String getSoundEventIdentifier() {
/* 31 */       return this.soundEventIdentifier;
/*    */     }
/*    */   }
/*    */   
/* 36 */   public static final ResourceKey<PigSoundVariant> CLASSIC = createKey(SoundSet.CLASSIC);
/*    */   
/* 37 */   public static final ResourceKey<PigSoundVariant> MINI = createKey(SoundSet.MINI);
/*    */   
/* 38 */   public static final ResourceKey<PigSoundVariant> BIG = createKey(SoundSet.BIG);
/*    */   
/*    */   private static ResourceKey<PigSoundVariant> createKey(SoundSet pigSoundVariant) {
/* 41 */     return ResourceKey.create(Registries.PIG_SOUND_VARIANT, Identifier.withDefaultNamespace(pigSoundVariant.getIdentifier()));
/*    */   }
/*    */   
/*    */   public static void bootstrap(BootstrapContext<PigSoundVariant> context) {
/* 45 */     register(context, CLASSIC, SoundSet.CLASSIC);
/* 46 */     register(context, BIG, SoundSet.BIG);
/* 47 */     register(context, MINI, SoundSet.MINI);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<PigSoundVariant> context, ResourceKey<PigSoundVariant> key, SoundSet PigSoundVariant) {
/* 51 */     context.register(key, SoundEvents.PIG_SOUNDS.get(PigSoundVariant));
/*    */   }
/*    */   
/*    */   public static Holder<PigSoundVariant> pickRandomSoundVariant(RegistryAccess registryAccess, RandomSource random) {
/* 55 */     return registryAccess.lookupOrThrow(Registries.PIG_SOUND_VARIANT).getRandom(random).orElseThrow();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\pig\PigSoundVariants.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */