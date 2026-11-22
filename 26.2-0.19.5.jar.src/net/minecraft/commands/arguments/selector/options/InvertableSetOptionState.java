/*    */ package net.minecraft.commands.arguments.selector.options;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class InvertableSetOptionState {
/* 18 */   private Limitation state = Limitation.NONE;
/*    */   
/* 19 */   private final Set<Identifier> tags = new HashSet<>();
/*    */   
/*    */   private boolean canLimitToSingle() {
/* 22 */     return (this.state == Limitation.NONE);
/*    */   }
/*    */   
/*    */   private boolean canLimitToMultiple() {
/* 26 */     return (this.state != Limitation.SINGLE);
/*    */   }
/*    */   
/*    */   public boolean canParsePositiveElement() {
/* 30 */     return canLimitToSingle();
/*    */   }
/*    */   
/*    */   public boolean canParseNegativeElement() {
/* 34 */     return canLimitToMultiple();
/*    */   }
/*    */   
/*    */   public boolean canParseAnyTag() {
/* 38 */     return canLimitToMultiple();
/*    */   }
/*    */   
/*    */   public boolean canParseTag(Identifier tag) {
/* 42 */     return (canParseAnyTag() && !this.tags.contains(tag));
/*    */   }
/*    */   
/*    */   public boolean canParseAny() {
/* 46 */     return (this.state != Limitation.SINGLE);
/*    */   }
/*    */   
/*    */   public boolean canParseElement(boolean inverted) {
/* 50 */     return inverted ? canParseNegativeElement() : canParsePositiveElement();
/*    */   }
/*    */   
/*    */   private void markParsedSingle() {
/* 54 */     this.state = Limitation.SINGLE;
/*    */   }
/*    */   
/*    */   private void markParsedMultiple() {
/* 58 */     this.state = Limitation.MULTIPLE;
/*    */   }
/*    */   
/*    */   public void markParsedPositiveElement() {
/* 62 */     markParsedSingle();
/*    */   }
/*    */   
/*    */   public void markParsedNegativeElement() {
/* 66 */     markParsedMultiple();
/*    */   }
/*    */   
/*    */   public void markParsedTag(Identifier tag) {
/* 70 */     markParsedMultiple();
/* 71 */     this.tags.add(tag);
/*    */   }
/*    */   
/*    */   public void markParsedElement(boolean inverted) {
/* 75 */     if (inverted) {
/* 76 */       markParsedNegativeElement();
/*    */     } else {
/* 78 */       markParsedPositiveElement();
/*    */     } 
/*    */   }
/*    */   
/*    */   private enum Limitation {
/* 83 */     NONE, SINGLE, MULTIPLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\selector\options\InvertableSetOptionState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */