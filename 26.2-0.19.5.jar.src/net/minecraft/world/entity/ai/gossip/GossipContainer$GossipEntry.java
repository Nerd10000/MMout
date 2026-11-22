/*    */ package net.minecraft.world.entity.ai.gossip;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ final class GossipEntry extends Record {
/*    */   private final UUID target;
/*    */   
/*    */   private final GossipType type;
/*    */   
/*    */   private final int value;
/*    */   
/*    */   public static final Codec<GossipEntry> CODEC;
/*    */   
/*    */   private GossipEntry(UUID target, GossipType type, int value) {
/* 37 */     this.target = target;
/* 37 */     this.type = type;
/* 37 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/gossip/GossipContainer$GossipEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public UUID target() {
/* 37 */     return this.target;
/*    */   }
/*    */   
/*    */   public GossipType type() {
/* 37 */     return this.type;
/*    */   }
/*    */   
/*    */   public int value() {
/* 37 */     return this.value;
/*    */   }
/*    */   
/*    */   static {
/* 38 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)UUIDUtil.CODEC.fieldOf("Target").forGetter(GossipEntry::target), (App)GossipType.CODEC.fieldOf("Type").forGetter(GossipEntry::type), (App)ExtraCodecs.POSITIVE_INT.fieldOf("Value").forGetter(GossipEntry::value)).apply((Applicative)i, GossipEntry::new));
/*    */   }
/*    */   
/*    */   public int weightedValue() {
/* 45 */     return this.value * this.type.weight;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\gossip\GossipContainer$GossipEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */