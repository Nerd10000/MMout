/*    */ package net.minecraft.server.dialog.input;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ 
/*    */ public final class Entry extends Record {
/*    */   private final String id;
/*    */   
/*    */   private final Optional<Component> display;
/*    */   
/*    */   private final boolean initial;
/*    */   
/*    */   public static final Codec<Entry> FULL_CODEC;
/*    */   
/*    */   public static final Codec<Entry> CODEC;
/*    */   
/*    */   public Entry(String id, Optional<Component> display, boolean initial) {
/* 45 */     this.id = id;
/* 45 */     this.display = display;
/* 45 */     this.initial = initial;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/input/SingleOptionInput$Entry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String id() {
/* 45 */     return this.id;
/*    */   }
/*    */   
/*    */   public Optional<Component> display() {
/* 45 */     return this.display;
/*    */   }
/*    */   
/*    */   public boolean initial() {
/* 45 */     return this.initial;
/*    */   }
/*    */   
/*    */   static {
/* 50 */     FULL_CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("id").forGetter(Entry::id), (App)ComponentSerialization.CODEC.optionalFieldOf("display").forGetter(Entry::display), (App)Codec.BOOL.optionalFieldOf("initial", Boolean.valueOf(false)).forGetter(Entry::initial)).apply((Applicative)i, Entry::new));
/* 56 */     CODEC = Codec.withAlternative(FULL_CODEC, (Codec)Codec.STRING, id -> new Entry(id, Optional.empty(), false));
/*    */   }
/*    */   
/*    */   public Component displayOrDefault() {
/* 62 */     return this.display.orElseGet(() -> Component.literal(this.id));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\input\SingleOptionInput$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */