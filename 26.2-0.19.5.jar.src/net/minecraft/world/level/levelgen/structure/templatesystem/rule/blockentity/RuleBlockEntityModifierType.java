/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface RuleBlockEntityModifierType<P extends RuleBlockEntityModifier> {
/*  8 */   public static final RuleBlockEntityModifierType<Clear> CLEAR = register("clear", Clear.CODEC);
/*    */   
/*  9 */   public static final RuleBlockEntityModifierType<Passthrough> PASSTHROUGH = register("passthrough", Passthrough.CODEC);
/*    */   
/* 10 */   public static final RuleBlockEntityModifierType<AppendStatic> APPEND_STATIC = register("append_static", AppendStatic.CODEC);
/*    */   
/* 11 */   public static final RuleBlockEntityModifierType<AppendLoot> APPEND_LOOT = register("append_loot", AppendLoot.CODEC);
/*    */   
/*    */   MapCodec<P> codec();
/*    */   
/*    */   private static <P extends RuleBlockEntityModifier> RuleBlockEntityModifierType<P> register(String id, MapCodec<P> codec) {
/* 16 */     return (RuleBlockEntityModifierType<P>)Registry.register(BuiltInRegistries.RULE_BLOCK_ENTITY_MODIFIER, id, () -> codec);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\rule\blockentity\RuleBlockEntityModifierType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */