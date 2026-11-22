/*    */ package net.minecraft.world.entity.animal.chicken;
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
/*    */ public class ChickenSoundVariants {
/*    */   public enum SoundSet {
/* 14 */     CLASSIC("classic", "chicken"),
/* 15 */     PICKY("picky", "chicken_picky");
/*    */     
/*    */     private final String identifier;
/*    */     
/*    */     private final String soundEventIdentifier;
/*    */     
/*    */     SoundSet(String identifier, String soundEventIdentifier) {
/* 21 */       this.identifier = identifier;
/* 22 */       this.soundEventIdentifier = soundEventIdentifier;
/*    */     }
/*    */     
/*    */     public String getIdentifier() {
/* 26 */       return this.identifier;
/*    */     }
/*    */     
/*    */     public String getSoundEventIdentifier() {
/* 30 */       return this.soundEventIdentifier;
/*    */     }
/*    */   }
/*    */   
/* 34 */   public static final ResourceKey<ChickenSoundVariant> CLASSIC = createKey(SoundSet.CLASSIC);
/*    */   
/* 35 */   public static final ResourceKey<ChickenSoundVariant> PICKY = createKey(SoundSet.PICKY);
/*    */   
/*    */   private static ResourceKey<ChickenSoundVariant> createKey(SoundSet chickenSoundVariant) {
/* 38 */     return ResourceKey.create(Registries.CHICKEN_SOUND_VARIANT, Identifier.withDefaultNamespace(chickenSoundVariant.getIdentifier()));
/*    */   }
/*    */   
/*    */   public static void bootstrap(BootstrapContext<ChickenSoundVariant> context) {
/* 42 */     register(context, CLASSIC, SoundSet.CLASSIC);
/* 43 */     register(context, PICKY, SoundSet.PICKY);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<ChickenSoundVariant> context, ResourceKey<ChickenSoundVariant> key, SoundSet ChickenSoundVariant) {
/* 47 */     context.register(key, SoundEvents.CHICKEN_SOUNDS.get(ChickenSoundVariant));
/*    */   }
/*    */   
/*    */   public static Holder<ChickenSoundVariant> pickRandomSoundVariant(RegistryAccess registryAccess, RandomSource random) {
/* 51 */     return registryAccess.lookupOrThrow(Registries.CHICKEN_SOUND_VARIANT).getRandom(random).orElseThrow();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\chicken\ChickenSoundVariants.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */