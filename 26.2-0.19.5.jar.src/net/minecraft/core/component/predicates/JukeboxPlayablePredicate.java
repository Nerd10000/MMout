/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.predicates.SingleComponentItemPredicate;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.JukeboxPlayable;
/*    */ import net.minecraft.world.item.JukeboxSong;
/*    */ 
/*    */ public final class JukeboxPlayablePredicate extends Record implements SingleComponentItemPredicate<JukeboxPlayable> {
/*    */   private final Optional<HolderSet<JukeboxSong>> song;
/*    */   
/*    */   public static final Codec<JukeboxPlayablePredicate> CODEC;
/*    */   
/*    */   public JukeboxPlayablePredicate(Optional<HolderSet<JukeboxSong>> song) {
/* 18 */     this.song = song;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/JukeboxPlayablePredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<HolderSet<JukeboxSong>> song() {
/* 18 */     return this.song;
/*    */   }
/*    */   
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.JUKEBOX_SONG).optionalFieldOf("song").forGetter(JukeboxPlayablePredicate::song)).apply((Applicative)i, JukeboxPlayablePredicate::new));
/*    */   }
/*    */   
/*    */   public DataComponentType<JukeboxPlayable> componentType() {
/* 28 */     return DataComponents.JUKEBOX_PLAYABLE;
/*    */   }
/*    */   
/*    */   public boolean matches(JukeboxPlayable value) {
/* 33 */     if (this.song.isPresent()) {
/* 34 */       boolean songIsPresent = false;
/* 35 */       for (Holder<JukeboxSong> maybeSong : this.song.get()) {
/* 36 */         Optional<ResourceKey<JukeboxSong>> songId = maybeSong.unwrapKey();
/* 37 */         if (songId.isEmpty())
/*    */           continue; 
/* 41 */         if (songId.equals(value.song().unwrapKey())) {
/* 42 */           songIsPresent = true;
/*    */           break;
/*    */         } 
/*    */       } 
/* 47 */       return songIsPresent;
/*    */     } 
/* 50 */     return true;
/*    */   }
/*    */   
/*    */   public static JukeboxPlayablePredicate any() {
/* 54 */     return new JukeboxPlayablePredicate(Optional.empty());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\predicates\JukeboxPlayablePredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */