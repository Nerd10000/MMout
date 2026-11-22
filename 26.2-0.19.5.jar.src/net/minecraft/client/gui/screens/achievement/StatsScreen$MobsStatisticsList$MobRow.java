/*     */ package net.minecraft.client.gui.screens.achievement;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ 
/*     */ class MobRow extends ObjectSelectionList.Entry<StatsScreen.MobsStatisticsList.MobRow> {
/*     */   private final Component mobName;
/*     */   
/*     */   private final Component kills;
/*     */   
/*     */   private final Component killedBy;
/*     */   
/*     */   private final boolean hasKills;
/*     */   
/*     */   private final boolean wasKilledBy;
/*     */   
/*     */   public MobRow(EntityType<?> type) {
/* 602 */     this.mobName = type.getDescription();
/* 604 */     int kills = paramMobsStatisticsList.this$0.stats.getValue(Stats.ENTITY_KILLED.get(type));
/* 605 */     if (kills == 0) {
/* 606 */       this.kills = (Component)Component.translatable("stat_type.minecraft.killed.none", new Object[] { this.mobName });
/* 607 */       this.hasKills = false;
/*     */     } else {
/* 609 */       this.kills = (Component)Component.translatable("stat_type.minecraft.killed", new Object[] { Integer.valueOf(kills), this.mobName });
/* 610 */       this.hasKills = true;
/*     */     } 
/* 613 */     int killedBy = paramMobsStatisticsList.this$0.stats.getValue(Stats.ENTITY_KILLED_BY.get(type));
/* 614 */     if (killedBy == 0) {
/* 615 */       this.killedBy = (Component)Component.translatable("stat_type.minecraft.killed_by.none", new Object[] { this.mobName });
/* 616 */       this.wasKilledBy = false;
/*     */     } else {
/* 618 */       this.killedBy = (Component)Component.translatable("stat_type.minecraft.killed_by", new Object[] { this.mobName, Integer.valueOf(killedBy) });
/* 619 */       this.wasKilledBy = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 625 */     graphics.text(StatsScreen.access$1000(this.this$1.this$0), this.mobName, getContentX() + 2, getContentY() + 1, -1);
/* 626 */     Objects.requireNonNull(StatsScreen.access$1200(this.this$1.this$0));
/* 626 */     graphics.text(StatsScreen.access$1100(this.this$1.this$0), this.kills, getContentX() + 2 + 10, getContentY() + 1 + 9, this.hasKills ? -4539718 : -8355712);
/* 627 */     Objects.requireNonNull(StatsScreen.access$1400(this.this$1.this$0));
/* 627 */     graphics.text(StatsScreen.access$1300(this.this$1.this$0), this.killedBy, getContentX() + 2 + 10, getContentY() + 1 + 9 * 2, this.wasKilledBy ? -4539718 : -8355712);
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 632 */     return (Component)Component.translatable("narrator.select", new Object[] { CommonComponents.joinForNarration(new Component[] { this.kills, this.killedBy }) });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\achievement\StatsScreen$MobsStatisticsList$MobRow.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */