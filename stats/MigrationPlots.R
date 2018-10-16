setwd(dir = "~/UvA/EC/FinalProject/EvComp.Group46/stats/")
require(ggplot2)
require(plyr)

data1 <- read.csv("RandomMigration.2.3.csv")
data2 <- read.csv("RingMigration.2.3.csv")
data3 <- read.csv("DistanceMigration.3.csv")
data4 <- read.csv("AdaptiveMigration.4.csv")
data5 <- read.csv("AdaptiveMigration.4.1.csv")

data1$MigrationPolicy <- "Random"
data2$MigrationPolicy <- "Ring"
data3$MigrationPolicy <- "Distance"
data4$MigrationPolicy <- "Adaptive4"
data5$MigrationPolicy <- "Adaptive4.2"

all <- rbind(data1, data2, data3, data4, data5)GenotypeLengthGenotypeLengthGenotypeLengthGenotypeLength
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
