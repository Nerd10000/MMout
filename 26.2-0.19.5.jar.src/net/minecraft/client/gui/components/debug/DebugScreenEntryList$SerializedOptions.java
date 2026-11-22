/*     */ package net.minecraft.client.gui.components.debug;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ final class SerializedOptions extends Record {
/*     */   private final Optional<DebugScreenProfile> profile;
/*     */   
/*     */   private final Optional<Map<Identifier, DebugScreenEntryStatus>> custom;
/*     */   
/*     */   private SerializedOptions(Optional<DebugScreenProfile> profile, Optional<Map<Identifier, DebugScreenEntryStatus>> custom) {
/* 186 */     this.profile = profile;
/* 186 */     this.custom = custom;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #186	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #186	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #186	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<DebugScreenProfile> profile() {
/* 186 */     return this.profile;
/*     */   }
/*     */   
/*     */   public Optional<Map<Identifier, DebugScreenEntryStatus>> custom() {
/* 186 */     return this.custom;
/*     */   }
/*     */   
/* 187 */   private static final Codec<Map<Identifier, DebugScreenEntryStatus>> CUSTOM_ENTRIES_CODEC = (Codec<Map<Identifier, DebugScreenEntryStatus>>)Codec.unboundedMap(Identifier.CODEC, DebugScreenEntryStatus.CODEC);
/*     */   
/*     */   public static final Codec<SerializedOptions> CODEC;
/*     */   
/*     */   static {
/* 189 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)DebugScreenProfile.CODEC.optionalFieldOf("profile").forGetter(SerializedOptions::profile), (App)CUSTOM_ENTRIES_CODEC.optionalFieldOf("custom").forGetter(SerializedOptions::custom)).apply((Applicative)i, SerializedOptions::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugScreenEntryList$SerializedOptions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */