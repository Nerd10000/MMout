/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public final class Tool extends Record {
/*    */   private final List<Rule> rules;
/*    */   
/*    */   private final float defaultMiningSpeed;
/*    */   
/*    */   private final int damagePerBlock;
/*    */   
/*    */   private final boolean canDestroyBlocksInCreative;
/*    */   
/*    */   public static final Codec<Tool> CODEC;
/*    */   
/*    */   public Tool(List<Rule> rules, float defaultMiningSpeed, int damagePerBlock, boolean canDestroyBlocksInCreative) {
/* 18 */     this.rules = rules;
/* 18 */     this.defaultMiningSpeed = defaultMiningSpeed;
/* 18 */     this.damagePerBlock = damagePerBlock;
/* 18 */     this.canDestroyBlocksInCreative = canDestroyBlocksInCreative;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/Tool;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/Tool;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/Tool;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/Tool;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/Tool;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/Tool;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<Rule> rules() {
/* 18 */     return this.rules;
/*    */   }
/*    */   
/*    */   public float defaultMiningSpeed() {
/* 18 */     return this.defaultMiningSpeed;
/*    */   }
/*    */   
/*    */   public int damagePerBlock() {
/* 18 */     return this.damagePerBlock;
/*    */   }
/*    */   
/*    */   public boolean canDestroyBlocksInCreative() {
/* 18 */     return this.canDestroyBlocksInCreative;
/*    */   }
/*    */   
/*    */   static {
/* 24 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Rule.CODEC.listOf().fieldOf("rules").forGetter(Tool::rules), (App)Codec.FLOAT.optionalFieldOf("default_mining_speed", Float.valueOf(1.0F)).forGetter(Tool::defaultMiningSpeed), (App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("damage_per_block", Integer.valueOf(1)).forGetter(Tool::damagePerBlock), (App)Codec.BOOL.optionalFieldOf("can_destroy_blocks_in_creative", Boolean.valueOf(true)).forGetter(Tool::canDestroyBlocksInCreative)).apply((Applicative)i, Tool::new));
/*    */   }
/*    */   
/* 31 */   public static final StreamCodec<RegistryFriendlyByteBuf, Tool> STREAM_CODEC = StreamCodec.composite(Rule.STREAM_CODEC
/* 32 */       .apply(ByteBufCodecs.list()), Tool::rules, ByteBufCodecs.FLOAT, Tool::defaultMiningSpeed, ByteBufCodecs.VAR_INT, Tool::damagePerBlock, ByteBufCodecs.BOOL, Tool::canDestroyBlocksInCreative, Tool::new);
/*    */   
/*    */   public float getMiningSpeed(BlockState state) {
/* 40 */     for (Rule rule : this.rules) {
/* 41 */       if (rule.speed.isPresent() && state.is(rule.blocks))
/* 42 */         return ((Float)rule.speed.get()).floatValue(); 
/*    */     } 
/* 45 */     return this.defaultMiningSpeed;
/*    */   }
/*    */   
/*    */   public boolean isCorrectForDrops(BlockState state) {
/* 49 */     for (Rule rule : this.rules) {
/* 50 */       if (rule.correctForDrops.isPresent() && state.is(rule.blocks))
/* 51 */         return ((Boolean)rule.correctForDrops.get()).booleanValue(); 
/*    */     } 
/* 54 */     return false;
/*    */   }
/*    */   
/*    */   public static final class Rule extends Record {
/*    */     private final HolderSet<Block> blocks;
/*    */     
/*    */     private final Optional<Float> speed;
/*    */     
/*    */     private final Optional<Boolean> correctForDrops;
/*    */     
/*    */     public static final Codec<Rule> CODEC;
/*    */     
/*    */     public Rule(HolderSet<Block> blocks, Optional<Float> speed, Optional<Boolean> correctForDrops) {
/* 57 */       this.blocks = blocks;
/* 57 */       this.speed = speed;
/* 57 */       this.correctForDrops = correctForDrops;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/Tool$Rule;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #57	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/component/Tool$Rule;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/Tool$Rule;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #57	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/component/Tool$Rule;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/Tool$Rule;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #57	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/component/Tool$Rule;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public HolderSet<Block> blocks() {
/* 57 */       return this.blocks;
/*    */     }
/*    */     
/*    */     public Optional<Float> speed() {
/* 57 */       return this.speed;
/*    */     }
/*    */     
/*    */     public Optional<Boolean> correctForDrops() {
/* 57 */       return this.correctForDrops;
/*    */     }
/*    */     
/*    */     static {
/* 58 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(Rule::blocks), (App)ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("speed").forGetter(Rule::speed), (App)Codec.BOOL.optionalFieldOf("correct_for_drops").forGetter(Rule::correctForDrops)).apply((Applicative)i, Rule::new));
/*    */     }
/*    */     
/* 64 */     public static final StreamCodec<RegistryFriendlyByteBuf, Rule> STREAM_CODEC = StreamCodec.composite(
/* 65 */         ByteBufCodecs.holderSet(Registries.BLOCK), Rule::blocks, ByteBufCodecs.FLOAT
/* 66 */         .apply(ByteBufCodecs::optional), Rule::speed, ByteBufCodecs.BOOL
/* 67 */         .apply(ByteBufCodecs::optional), Rule::correctForDrops, Rule::new);
/*    */     
/*    */     public static Rule minesAndDrops(HolderSet<Block> blocks, float speed) {
/* 72 */       return new Rule(blocks, 
/*    */           
/* 74 */           Optional.of(Float.valueOf(speed)), 
/* 75 */           Optional.of(Boolean.valueOf(true)));
/*    */     }
/*    */     
/*    */     public static Rule deniesDrops(HolderSet<Block> blocks) {
/* 80 */       return new Rule(blocks, 
/*    */           
/* 82 */           Optional.empty(), 
/* 83 */           Optional.of(Boolean.valueOf(false)));
/*    */     }
/*    */     
/*    */     public static Rule overrideSpeed(HolderSet<Block> blocks, float speed) {
/* 88 */       return new Rule(blocks, 
/*    */           
/* 90 */           Optional.of(Float.valueOf(speed)), 
/* 91 */           Optional.empty());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\Tool.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */