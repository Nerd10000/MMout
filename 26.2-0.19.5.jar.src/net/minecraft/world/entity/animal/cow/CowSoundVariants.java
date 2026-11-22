/*    */ package net.minecraft.world.entity.animal.cow;
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
/*    */ public class CowSoundVariants {
/*    */   public enum SoundSet {
/* 14 */     CLASSIC("classic", "cow"),
/* 15 */     MOODY("moody", "cow_moody");
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
/* 34 */   public static final ResourceKey<CowSoundVariant> CLASSIC = createKey(SoundSet.CLASSIC);
/*    */   
/* 35 */   public static final ResourceKey<CowSoundVariant> MOODY = createKey(SoundSet.MOODY);
/*    */   
/*    */   private static ResourceKey<CowSoundVariant> createKey(SoundSet cowSoundVariant) {
/* 38 */     return ResourceKey.create(Registries.COW_SOUND_VARIANT, Identifier.withDefaultNamespace(cowSoundVariant.getIdentifier()));
/*    */   }
/*    */   
/*    */   public static void bootstrap(BootstrapContext<CowSoundVariant> context) {
/* 42 */     register(context, CLASSIC, SoundSet.CLASSIC);
/* 43 */     register(context, MOODY, SoundSet.MOODY);
/*    */   }
/*    */   
/*    */   private static void register(BootstrapContext<CowSoundVariant> context, ResourceKey<CowSoundVariant> key, SoundSet CowSoundVariant) {
/* 47 */     context.register(key, SoundEvents.COW_SOUNDS.get(CowSoundVariant));
/*    */   }
/*    */   
/*    */   public static Holder<CowSoundVariant> pickRandomSoundVariant(RegistryAccess registryAccess, RandomSource random) {
/* 51 */     return registryAccess.lookupOrThrow(Registries.COW_SOUND_VARIANT).getRandom(random).orElseThrow();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\cow\CowSoundVariants.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */