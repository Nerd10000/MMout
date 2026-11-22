/*      */ package net.minecraft.client;
/*      */ 
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import java.util.List;
/*      */ 
/*      */ class null implements Options.OptionAccess {
/*      */   null(Options this$0) {}
/*      */   
/*      */   public <T> void process(String name, OptionInstance<T> option) {
/* 2192 */     optionsForReport.add(Pair.of(name, option.get()));
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\Options$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */