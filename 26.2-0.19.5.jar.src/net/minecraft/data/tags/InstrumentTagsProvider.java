/*    */ package net.minecraft.data.tags;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.PackOutput;
/*    */ import net.minecraft.tags.InstrumentTags;
/*    */ import net.minecraft.world.item.Instrument;
/*    */ import net.minecraft.world.item.Instruments;
/*    */ 
/*    */ public class InstrumentTagsProvider extends TagsProvider<Instrument> {
/*    */   public InstrumentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/* 14 */     super(output, Registries.INSTRUMENT, lookupProvider);
/*    */   }
/*    */   
/*    */   protected void addTags(HolderLookup.Provider registries) {
/* 19 */     tag(InstrumentTags.REGULAR_GOAT_HORNS)
/* 20 */       .add(Instruments.PONDER_GOAT_HORN)
/* 21 */       .add(Instruments.SING_GOAT_HORN)
/* 22 */       .add(Instruments.SEEK_GOAT_HORN)
/* 23 */       .add(Instruments.FEEL_GOAT_HORN);
/* 26 */     tag(InstrumentTags.SCREAMING_GOAT_HORNS)
/* 27 */       .add(Instruments.ADMIRE_GOAT_HORN)
/* 28 */       .add(Instruments.CALL_GOAT_HORN)
/* 29 */       .add(Instruments.YEARN_GOAT_HORN)
/* 30 */       .add(Instruments.DREAM_GOAT_HORN);
/* 33 */     tag(InstrumentTags.GOAT_HORNS)
/* 34 */       .addTag(InstrumentTags.REGULAR_GOAT_HORNS)
/* 35 */       .addTag(InstrumentTags.SCREAMING_GOAT_HORNS);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\tags\InstrumentTagsProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */