setwd(dir = "~/UvA/EC/FinalProject/EvComp.Group46/stats/")
require(ggplot2)
require(plyr)

data1 <- read.csv("RandomMigration.Big.csv")
data2 <- read.csv("RingMigration.Big.csv")
data3 <- read.csv("DistanceMigration.Big.csv")
data4 <- read.csv("AdaptiveMigration.Big.csv")


data1$MigrationPolicy <- "Random"
data2$MigrationPolicy <- "Ring"
data3$MigrationPolicy <- "Distance"
data4$MigrationPolicy <- "Adaptive"


all <- rbind(data1, data2, data3, data4)
cdat <- ddply(all, "MigrationPolicy", summarise, Score.mean=mean(Score))

mc_density <- ggplot(all, aes(Score))+
  geom_density(aes(color = MigrationPolicy), size=0.6)+
  geom_vline(data = cdat, aes(xintercept = Score.mean, color = MigrationPolicy),
             linetype="dashed", size=0.6)
plot(mc_density)

hisotgrams <- ggplot(all, aes(Score, fill = MigrationPolicy)) +
  geom_histogram(position = "identity", colour="grey40", binwidth = 0.1) +
  facet_grid(MigrationPolicy ~ .)
plot(hisotgrams)

violin <- ggplot(all, aes(x=MigrationPolicy, y=Score, fill=MigrationPolicy))+
  geom_violin(trim=F)+
  geom_boxplot(width=0.2, fill="white", outlier.shape = NA)
plot(violin)
