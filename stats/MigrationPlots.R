setwd(dir = "~/UvA/EC/FinalProject/EvComp.Group46/stats/")
require(ggplot2)
require(plyr)

data1 <- read.csv("RandomMigration.csv")
data2 <- read.csv("RingMigration.csv")
data3 <- read.csv("DistanceMigration.csv")
data4 <- read.csv("AdaptiveMigration.csv")

data1$MigrationPolicy <- "Random"
data2$MigrationPolicy <- "Ring"
data3$MigrationPolicy <- "Distance"
data4$MigrationPolicy <- "Adaptive"

all <- rbind(data1, data2, data3, data4)
cdat <- ddply(all, "MigrationPolicy", summarise, Score.mean=mean(Score))

mc_density <- ggplot(all, aes(Score))+
  geom_density(aes(color = MigrationPolicy), alpha = 0.8)+
  geom_vline(data = cdat, aes(xintercept = Score.mean, color = MigrationPolicy),
             linetype="dashed", size=1)
plot(mc_density)
