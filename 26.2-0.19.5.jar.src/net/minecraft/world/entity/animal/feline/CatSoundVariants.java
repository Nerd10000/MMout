/*    */ package net.minecraft.world.entity.animal.feline;
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
/*    */ public class CatSoundVariants {
/*    */   public enum SoundSet {
/* 14 */     CLASSIC("classic", "cat"),
/* 15 */     ROYAL("royal", "cat_royal");
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
/* 34 */   public static final ResourceKey<CatSoundVariant> CLASSIC = createKey(SoundSet.CLASSIC);
/*    */   
/* 35 */   public static final ResourceKey<CatSoundVariant> ROYAL = createKey(SoundSet.ROYAL);
/*    */   
/*    */   private static ResourceKey<CatSoundVariant> createKey(SoundSet catSoundVariant) {
/* 38 */     return ResourceKey.create(Registries.CAT_SOUND_VARIANT, Identifier.withDefaultNamespace(catSoundVariant.getIdentifier()));
/*    */   }
/*    */   
/*    */   public static void bootstrap(BootstrapContext<CatSoundVariant> context) {
/* 42 */     register(context, CLASSIC, SoundSet.CLASSIC);
/* 43 */     register(context, ROYAL, SoundSet.ROYAL);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<CatSoundVariant> context, ResourceKey<CatSoundVariant> key, SoundSet CatSoundVariant) {
/* 47 */     context.register(key, SoundEvents.CAT_SOUNDS.get(CatSoundVariant));
/*    */   }
/*    */   
/*    */   public static Holder<CatSoundVariant> pickRandomSoundVariant(RegistryAccess registryAccess, RandomSource random) {
/* 51 */     return registryAccess.lookupOrThrow(Registries.CAT_SOUND_VARIANT).getRandom(random).orElseThrow();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\feline\CatSoundVariants.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */