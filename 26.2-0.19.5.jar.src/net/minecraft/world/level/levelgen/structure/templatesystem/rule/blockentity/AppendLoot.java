/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ 
/*    */ public class AppendLoot implements RuleBlockEntityModifier {
/*    */   public static final MapCodec<AppendLoot> CODEC;
/*    */   
/*    */   private final ResourceKey<LootTable> lootTable;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LootTable.KEY_CODEC.fieldOf("loot_table").forGetter(())).apply((Applicative)i, AppendLoot::new));
/*    */   }
/*    */   
/*    */   public AppendLoot(ResourceKey<LootTable> lootTable) {
/* 20 */     this.lootTable = lootTable;
/*    */   }
/*    */   
/*    */   public CompoundTag apply(RandomSource random, CompoundTag existingTag) {
/* 25 */     CompoundTag result = (existingTag == null) ? new CompoundTag() : existingTag.copy();
/* 27 */     result.store("LootTable", LootTable.KEY_CODEC, this.lootTable);
/* 28 */     result.putLong("LootTableSeed", random.nextLong());
/* 30 */     return result;
/*    */   }
/*    */   
/*    */   public RuleBlockEntityModifierType<?> getType() {
/* 35 */     return RuleBlockEntityModifierType.APPEND_LOOT;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\rule\blockentity\AppendLoot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */