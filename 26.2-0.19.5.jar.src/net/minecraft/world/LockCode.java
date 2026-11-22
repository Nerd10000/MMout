/*    */ package net.minecraft.world;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.predicates.ItemPredicate;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public final class LockCode extends Record {
/*    */   private final ItemPredicate predicate;
/*    */   
/*    */   public LockCode(ItemPredicate predicate) {
/* 10 */     this.predicate = predicate;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/LockCode;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/LockCode;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/LockCode;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/LockCode;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/LockCode;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/LockCode;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ItemPredicate predicate() {
/* 10 */     return this.predicate;
/*    */   }
/*    */   
/* 11 */   public static final LockCode NO_LOCK = new LockCode(ItemPredicate.Builder.item().build());
/*    */   
/* 13 */   public static final Codec<LockCode> CODEC = ItemPredicate.CODEC.xmap(LockCode::new, LockCode::predicate);
/*    */   
/*    */   public static final String TAG_LOCK = "lock";
/*    */   
/*    */   public boolean unlocksWith(ItemStack itemStack) {
/* 18 */     return this.predicate.test((ItemInstance)itemStack);
/*    */   }
/*    */   
/*    */   public void addToTag(ValueOutput parent) {
/* 22 */     if (this != NO_LOCK)
/* 23 */       parent.store("lock", CODEC, this); 
/*    */   }
/*    */   
/*    */   public boolean canUnlock(Player player) {
/* 28 */     return (player.isSpectator() || unlocksWith(player.getMainHandItem()));
/*    */   }
/*    */   
/*    */   public static LockCode fromTag(ValueInput parent) {
/* 32 */     return parent.read("lock", CODEC).orElse(NO_LOCK);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\LockCode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */