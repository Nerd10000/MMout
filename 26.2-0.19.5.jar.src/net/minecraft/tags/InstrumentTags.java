/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.Instrument;
/*    */ 
/*    */ public interface InstrumentTags {
/*  8 */   public static final TagKey<Instrument> REGULAR_GOAT_HORNS = create("regular_goat_horns");
/*    */   
/*  9 */   public static final TagKey<Instrument> SCREAMING_GOAT_HORNS = create("screaming_goat_horns");
/*    */   
/* 10 */   public static final TagKey<Instrument> GOAT_HORNS = create("goat_horns");
/*    */   
/*    */   private static TagKey<Instrument> create(String name) {
/* 13 */     return TagKey.create(Registries.INSTRUMENT, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\InstrumentTags.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */