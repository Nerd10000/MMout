/*    */ package net.minecraft.server.dialog.action;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class CustomAll extends Record implements Action {
/*    */   private final Identifier id;
/*    */   
/*    */   private final Optional<CompoundTag> additions;
/*    */   
/*    */   public static final MapCodec<CustomAll> MAP_CODEC;
/*    */   
/*    */   public CustomAll(Identifier id, Optional<CompoundTag> additions) {
/* 12 */     this.id = id;
/* 12 */     this.additions = additions;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/action/CustomAll;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/CustomAll;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/action/CustomAll;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/dialog/action/CustomAll;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/action/CustomAll;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/dialog/action/CustomAll;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/* 12 */     return this.id;
/*    */   }
/*    */   
/*    */   public Optional<CompoundTag> additions() {
/* 12 */     return this.additions;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("id").forGetter(CustomAll::id), (App)CompoundTag.CODEC.optionalFieldOf("additions").forGetter(CustomAll::additions)).apply((Applicative)i, CustomAll::new));
/*    */   }
/*    */   
/*    */   public MapCodec<CustomAll> codec() {
/* 23 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Optional<ClickEvent> createAction(Map<String, Action.ValueGetter> parameters) {
/* 28 */     CompoundTag tag = this.additions.<CompoundTag>map(CompoundTag::copy).orElseGet(CompoundTag::new);
/* 29 */     parameters.forEach((key, value) -> tag.put(key, value.asTag()));
/* 30 */     return (Optional)Optional.of(new ClickEvent.Custom(this.id, Optional.of(tag)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\action\CustomAll.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */