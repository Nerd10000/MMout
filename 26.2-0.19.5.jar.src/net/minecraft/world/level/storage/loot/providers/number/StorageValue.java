/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.commands.arguments.NbtPathArgument;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NumericTag;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class StorageValue extends Record implements NumberProvider {
/*    */   private final Identifier storage;
/*    */   
/*    */   private final NbtPathArgument.NbtPath path;
/*    */   
/*    */   public static final MapCodec<StorageValue> MAP_CODEC;
/*    */   
/*    */   public StorageValue(Identifier storage, NbtPathArgument.NbtPath path) {
/* 15 */     this.storage = storage;
/* 15 */     this.path = path;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/StorageValue;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier storage() {
/* 15 */     return this.storage;
/*    */   }
/*    */   
/*    */   public NbtPathArgument.NbtPath path() {
/* 15 */     return this.path;
/*    */   }
/*    */   
/*    */   static {
/* 19 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("storage").forGetter(StorageValue::storage), (App)NbtPathArgument.NbtPath.CODEC.fieldOf("path").forGetter(StorageValue::path)).apply((Applicative)i, StorageValue::new));
/*    */   }
/*    */   
/*    */   public MapCodec<StorageValue> codec() {
/* 26 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   private Number getNumericTag(LootContext context, Number _default) {
/* 30 */     CompoundTag value = context.getLevel().getServer().getCommandStorage().get(this.storage);
/*    */     try {
/* 33 */       List<Tag> selectedTags = this.path.get((Tag)value);
/* 34 */       if (selectedTags.size() == 1) {
/* 35 */         NumericTag numericTag = (NumericTag)selectedTags.getFirst();
/* 35 */         if (numericTag instanceof NumericTag) {
/* 35 */           NumericTag result = numericTag;
/* 36 */           return result.box();
/*    */         } 
/*    */       } 
/* 39 */     } catch (CommandSyntaxException commandSyntaxException) {}
/* 42 */     return _default;
/*    */   }
/*    */   
/*    */   public float getFloat(LootContext context) {
/* 47 */     return getNumericTag(context, Float.valueOf(0.0F)).floatValue();
/*    */   }
/*    */   
/*    */   public int getInt(LootContext context) {
/* 52 */     return getNumericTag(context, Integer.valueOf(0)).intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\number\StorageValue.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */