ResultMatrix <- read.csv(file="current.csv", header=FALSE, sep=",")

iteration <- c(ResultMatrix[1]$V1)
bestSolutionACO <- c(ResultMatrix[2]$V2[1:length(iteration)-1])
durationACO <- c(ResultMatrix[3]$V3[1:length(iteration)-1])/1000

bestSolutionNEH <- c(ResultMatrix[2]$V2)[length(iteration):length(iteration)]
durationNEH <- c(ResultMatrix[3]$V3)[length(iteration):length(iteration)]/1000
jpeg('current.jpg')
range <- range(bestSolutionNEH, bestSolutionACO)

#duration <- lapply(duration, as.numeric)
plot(durationACO, bestSolutionACO, type="l", ylim=range, col="blue", ann=FALSE)#, xaxt = "n"

lines(durationNEH, bestSolutionNEH, type="p", col="green")
# axis(1, at=1:length(durationACO), lab=durationACO)

box()

title(main="ACO Flow Shop", col.main="red", font.main=4)
title(xlab="Dauer in s")
title(ylab="Länge")

#legend(1, p_range[2], c("Lösungslänge", "Dauer"), cex=0.8, col=c("blue", "green", "red", "cyan", "gold"), pch=21:21, lty=1:1)
