ResultMatrix <- read.csv(file="current.csv", header=FALSE, sep=",")

iteration <- c(ResultMatrix[1]$V1)
bestSolution <- c(ResultMatrix[2]$V2)
duration <- c(ResultMatrix[3]$V3)

jpeg('current.jpg')
# p_range <- range(bestSolution, duration, iteration)

iteration <- lapply(iteration, as.numeric)
plot(iteration, bestSolution, type="l", xaxt = "n", col="blue", ann=FALSE) # , ylim=p_range

axis(1, iteration)

box()

# lines(duration, type="l", col="green")

title(main="ACO Flow Shop", col.main="red", font.main=4)
title(xlab="Iteration")
title(ylab="Länge")

#legend(1, p_range[2], c("Lösungslänge", "Dauer"), cex=0.8, col=c("blue", "green", "red", "cyan", "gold"), pch=21:21, lty=1:1)
