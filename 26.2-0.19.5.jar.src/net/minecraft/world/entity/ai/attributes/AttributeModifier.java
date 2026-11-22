/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public final class AttributeModifier extends Record {
/*    */   private final Identifier id;
/*    */   
/*    */   private final double amount;
/*    */   
/*    */   private final Operation operation;
/*    */   
/*    */   public static final MapCodec<AttributeModifier> MAP_CODEC;
/*    */   
/*    */   public AttributeModifier(Identifier id, double amount, Operation operation) {
/* 15 */     this.id = id;
/* 15 */     this.amount = amount;
/* 15 */     this.operation = operation;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Identifier id() {
/* 15 */     return this.id;
/*    */   }
/*    */   
/*    */   public double amount() {
/* 15 */     return this.amount;
/*    */   }
/*    */   
/*    */   public Operation operation() {
/* 15 */     return this.operation;
/*    */   }
/*    */   
/*    */   public enum Operation implements StringRepresentable {
/* 17 */     ADD_VALUE("add_value", 0),
/* 18 */     ADD_MULTIPLIED_BASE("add_multiplied_base", 1),
/* 19 */     ADD_MULTIPLIED_TOTAL("add_multiplied_total", 2);
/*    */     
/* 21 */     public static final IntFunction<Operation> BY_ID = ByIdMap.continuous(Operation::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */     
/* 23 */     public static final StreamCodec<ByteBuf, Operation> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Operation::id);
/*    */     
/* 25 */     public static final Codec<Operation> CODEC = (Codec<Operation>)StringRepresentable.fromEnum(Operation::values);
/*    */     
/*    */     private final String name;
/*    */     
/*    */     private final int id;
/*    */     
/*    */     static {
/*    */     
/*    */     }
/*    */     
/*    */     Operation(String name, int id) {
/* 31 */       this.name = name;
/* 32 */       this.id = id;
/*    */     }
/*    */     
/*    */     public int id() {
/* 36 */       return this.id;
/*    */     }
/*    */     
/*    */     public String getSerializedName() {
/* 41 */       return this.name;
/*    */     }
/*    */   }
/*    */   
/*    */   static {
/* 45 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("id").forGetter(AttributeModifier::id), (App)Codec.DOUBLE.fieldOf("amount").forGetter(AttributeModifier::amount), (App)Operation.CODEC.fieldOf("operation").forGetter(AttributeModifier::operation)).apply((Applicative)i, AttributeModifier::new));
/*    */   }
/*    */   
/* 50 */   public static final Codec<AttributeModifier> CODEC = MAP_CODEC.codec();
/*    */   
/* 52 */   public static final StreamCodec<ByteBuf, AttributeModifier> STREAM_CODEC = StreamCodec.composite(Identifier.STREAM_CODEC, AttributeModifier::id, ByteBufCodecs.DOUBLE, AttributeModifier::amount, Operation.STREAM_CODEC, AttributeModifier::operation, AttributeModifier::new);
/*    */   
/*    */   public boolean is(Identifier id) {
/* 60 */     return id.equals(this.id);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\attributes\AttributeModifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */