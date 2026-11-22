/*     */ package net.minecraft.client.sounds;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.resources.sounds.Sound;
/*     */ import net.minecraft.client.resources.sounds.SoundEventRegistration;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.MultipliedFloats;
/*     */ import net.minecraft.util.valueproviders.SampledFloat;
/*     */ 
/*     */ public class Preparations {
/* 134 */   private final Map<Identifier, WeighedSoundEvents> registry = Maps.newHashMap();
/*     */   
/* 135 */   private Map<Identifier, Resource> soundCache = Map.of();
/*     */   
/*     */   private void listResources(ResourceManager resourceManager) {
/* 138 */     this.soundCache = Sound.SOUND_LISTER.listMatchingResources(resourceManager);
/*     */   }
/*     */   
/*     */   private void handleRegistration(Identifier eventLocation, SoundEventRegistration soundEventRegistration) {
/* 142 */     WeighedSoundEvents registration = this.registry.get(eventLocation);
/* 143 */     boolean missesRegistration = (registration == null);
/* 144 */     if (missesRegistration || soundEventRegistration.isReplace()) {
/* 145 */       if (!missesRegistration)
/* 146 */         SoundManager.LOGGER.debug("Replaced sound event location {}", eventLocation); 
/* 148 */       registration = new WeighedSoundEvents(eventLocation, soundEventRegistration.getSubtitle());
/* 149 */       this.registry.put(eventLocation, registration);
/*     */     } 
/* 152 */     ResourceProvider cachedProvider = ResourceProvider.fromMap(this.soundCache);
/* 154 */     for (Sound sound : soundEventRegistration.getSounds()) {
/*     */       Sound sound1;
/*     */       Weighted<Sound> weighted;
/* 155 */       final Identifier soundLocation = sound.getLocation();
/* 158 */       switch (SoundManager.null.$SwitchMap$net$minecraft$client$resources$sounds$Sound$Type[sound.getType().ordinal()]) {
/*     */         case 1:
/* 160 */           if (!SoundManager.validateSoundResource(sound, eventLocation, cachedProvider))
/*     */             continue; 
/* 164 */           sound1 = sound;
/*     */           break;
/*     */         case 2:
/* 166 */           weighted = new Weighted<Sound>() {
/*     */               public int getWeight() {
/* 169 */                 WeighedSoundEvents registration = SoundManager.Preparations.this.registry.get(soundLocation);
/* 170 */                 return (registration == null) ? 0 : registration.getWeight();
/*     */               }
/*     */               
/*     */               public Sound getSound(RandomSource random) {
/* 175 */                 WeighedSoundEvents registration = SoundManager.Preparations.this.registry.get(soundLocation);
/* 176 */                 if (registration == null)
/* 177 */                   return SoundManager.EMPTY_SOUND; 
/* 181 */                 Sound wrappedSound = registration.getSound(random);
/* 182 */                 return new Sound(wrappedSound
/* 183 */                     .getLocation(), (SampledFloat)new MultipliedFloats(new SampledFloat[] { wrappedSound
/* 184 */                         .getVolume(), this.val$sound.getVolume() }, ), (SampledFloat)new MultipliedFloats(new SampledFloat[] { wrappedSound
/* 185 */                         .getPitch(), this.val$sound.getPitch() }, ), sound
/* 186 */                     .getWeight(), Sound.Type.FILE, (wrappedSound
/*     */                     
/* 188 */                     .shouldStream() || sound.shouldStream()), wrappedSound
/* 189 */                     .shouldPreload(), wrappedSound
/* 190 */                     .getAttenuationDistance());
/*     */               }
/*     */               
/*     */               public void preloadIfRequired(SoundEngine soundEngine) {
/* 196 */                 WeighedSoundEvents registration = SoundManager.Preparations.this.registry.get(soundLocation);
/* 197 */                 if (registration == null)
/*     */                   return; 
/* 200 */                 registration.preloadIfRequired(soundEngine);
/*     */               }
/*     */             };
/*     */           break;
/*     */         default:
/* 203 */           throw new IllegalStateException("Unknown SoundEventRegistration type: " + String.valueOf(sound.getType()));
/*     */       } 
/* 206 */       registration.addSound(weighted);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void apply(Map<Identifier, WeighedSoundEvents> registry, Map<Identifier, Resource> soundCache, SoundEngine engine) {
/* 211 */     registry.clear();
/* 212 */     soundCache.clear();
/* 214 */     soundCache.putAll(this.soundCache);
/* 216 */     for (Map.Entry<Identifier, WeighedSoundEvents> entry : this.registry.entrySet()) {
/* 217 */       registry.put(entry.getKey(), entry.getValue());
/* 218 */       ((WeighedSoundEvents)entry.getValue()).preloadIfRequired(engine);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\sounds\SoundManager$Preparations.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */